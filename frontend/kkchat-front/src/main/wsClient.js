import WebSocket from 'ws'
const NODE_ENV = process.env.NODE_ENV
import { saveMessage, saveMessageBatch, updateMessage } from "./db/ChatMessageModel"
import {
    saveOrUpdateChatSessionBatch4Init, saveOrUpdate4Message,
    updateGroupName, delChatSession, selectUserSessionByContactId
} from "./db/ChatSessionUserModel"
import { updateContactNoReadCount } from "./db/UserSetting"
import { getWindow } from "./windowProxy";
import store from "./store"
//ws相关
let ws = null
//重连次数
let maxReConnectTimes = null;
//避免onclose onerror都重连相当于加个锁
let lockReconnect = false;

//wsUrl
let wsUrl = null;
let sender = null;
let needReconnect = null;

const initWs = (config, _sender) => {
    wsUrl = `${NODE_ENV !== 'development' ? store.getData("prodWsDomain") : store.getData("devWsDomain")}?token=${config.token}`;
    sender = _sender;
    needReconnect = true;
    maxReConnectTimes = 5;
    createWs();
}

const closeWs = () => {
    needReconnect = false;
    ws.close();
}

const createWs = () => {
    if (wsUrl == null) {
        return
    }
    ws = new WebSocket(wsUrl)
    ws.onopen = function (params) {
        console.log('客户端连接成功')
        ws.send('heart beat')
        maxReConnectTimes = 5
    }

    // 从服务器接受到信息时的回调函数
    ws.onmessage = async function (e) {
        let mainWindow = getWindow("main");
        //信息消息闪烁
        if (!mainWindow.isFocused()) {
            mainWindow.flashFrame(true);
        }
        console.log('收到服务器消息', e.data)
        const message = JSON.parse(e.data);
        const leaveGroupUserId = message.extendData;
        const messageType = message.messageType;
        switch (messageType) {
            case 0://ws链接成功
                //保存会话信息
                await saveOrUpdateChatSessionBatch4Init(message.extendData.chatSessionList);
                //保存聊天消息
                await saveMessageBatch(message.extendData.chatMessageList);
                //保存好友通知消息数
                await updateContactNoReadCount({ userId: store.getUserId(), noReadCount: message.extendData.applyCount });
                //发送消息
                sender.send("reciveMessage", { messageType: message.messageType });
                break;
            case 4://好友申请
                await updateContactNoReadCount({ userId: store.getUserId(), noReadCount: 1 });
                sender.send("reciveMessage", { messageType: message.messageType });
                break;
            case 6://文件上传完成
                updateMessage({ status: message.status }, { messageId: message.messageId });
                sender.send("reciveMessage", message);
                break;
            case 10://修改群昵称
                updateGroupName(message.contactId, message.extendData);
                sender.send("reciveMessage", message);
                break;
            case 7://强制下线
                sender.send("reciveMessage", message);
                closeWs();
                break;
            case 1: //添加好友成功
            case 3://群创建成功
            case 9://好友加入群组
            case 2://聊条消息
            case 5://图片，视频消息
            case 8://解散群聊
            case 11://退出群聊
            case 12://提出群聊
                //如果是群聊消息，那么这个群里的所有人都会收到聊天消息，发送人和接收人是同一个人不做处理 
                if (message.sendUserId === store.getUserId() && message.contactType == 1) {
                    break;
                }
                //收到ws消息更新会话信息
                const sessionInfo = {};
                if (message.extendData && typeof message.extendData === "object") {
                    Object.assign(sessionInfo, message.extendData);
                } else {
                    Object.assign(sessionInfo, message);
                    //单聊更新联系人名称
                    if (message.contactType == 0 && messageType != 1) {
                        sessionInfo.contactName = message.sendUserNickName;
                    }
                    sessionInfo.lastReceiveTime = message.sendTime;
                }
                //11退出群聊 12移除群聊 减少成员数量
                if (messageType == 9 || messageType == 12 || messageType == 11) {
                    sessionInfo.memberCount = message.memberCount;
                }
                console.log("sessionInfo", sessionInfo);
                await saveOrUpdate4Message(store.getUserData("currentSessionId"), sessionInfo);
                //写入本地消息
                await saveMessage(message);
                //查询本地session 单聊联系人就是发送人，群聊联系人就是群号
                const dbSessionInfo = await selectUserSessionByContactId(message.contactId);
                message.extendData = dbSessionInfo;
                //退出群聊，当前用户不收到消息
                if (messageType == 11 && leaveGroupUserId == store.getUserId()) {
                    break;
                }
                sender.send("reciveMessage", message);
                break;
        }
    }

    // 连接关闭后的回调函数存储数据
    ws.onclose = function (evt) {
        console.log('关闭客户端连接准备重连')
        reconnect('onclose')
    }

    // 连接失败后的回调函数
    ws.onerror = function (evt) {
        console.log('连接失败了准备重连')
        reconnect('onerror')
    }

    const reconnect = (type) => {
        if (!needReconnect) {
            console.log("链接断开无须重连");
            return;
        }
        if (ws != null) {
            ws.close()
        }
        if (lockReconnect) {
            return;
        }
        console.log(type + "准备重连");
        lockReconnect = true;
        if (maxReConnectTimes > 0) {
            console.log('准备重连，剩余重连次数' + maxReConnectTimes, new Date().getTime())
            maxReConnectTimes--
            // 进行重连
            setTimeout(function () {
                createWs()
                lockReconnect = false;
            }, 5000)
        } else {
            console.log('TCP连接已超时')
        }
    }

    //发送心跳
    setInterval(() => {
        if (ws != null && ws.readyState == 1) {
            ws.send('heart beat')
        }
    }, 1000 * 5);
}

export {
    initWs,
    closeWs
}