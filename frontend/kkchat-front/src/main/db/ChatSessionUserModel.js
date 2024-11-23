
import { run, queryAll, insertOrIgnore, update, queryOne } from "./ADB";
import store from "../store"


//新增
const addChatSession = (sessionInfo) => {
    sessionInfo.userId = store.getUserId();
    insertOrIgnore("chat_session_user", sessionInfo);
}

//更新会话信息
const updateChatSession = (sessionInfo) => {
    const paramData = {
        userId: store.getUserId(),
        contactId: sessionInfo.contactId
    }
    const updateInfo = Object.assign({}, sessionInfo);
    //更新的时候不更新userId contactId
    updateInfo.contactId = null;
    updateInfo.userId = null;
    return update("chat_session_user", updateInfo, paramData);
}

//批量保存会话
const saveOrUpdateChatSessionBatch4Init = (chatSessionList) => {
    return new Promise(async (resolve, reject) => {
        try {
            for (let i = 0; i < chatSessionList.length; i++) {
                const sessionInfo = chatSessionList[i]
                sessionInfo.status = 1;
                let sessionData = await selectUserSessionByContactId(sessionInfo.contactId);
                if (sessionData) {
                    await updateChatSession(sessionInfo);
                } else {
                    await addChatSession(sessionInfo);
                }
            }
            resolve();
        } catch (error) {
            resolve();
        }
    })
}

//更新未读数
const updateNoReadCount = ({ contactId, noReadCount }) => {
    let sql = "update chat_session_user set no_read_count = no_read_count+? where user_id=? and contact_id=?";
    return run(sql, [noReadCount, store.getUserId(), contactId]);
}

//查询用户的会话列表
const selectUserSessionList = () => {
    let sql = "select * from chat_session_user where user_id = ? and status = 1";
    return queryAll(sql, [store.getUserId()]);
}
//根据联系人查询会话
const selectUserSessionByContactId = (concatId) => {
    let sql = "select * from chat_session_user where user_id = ? and contact_id = ?";
    return queryOne(sql, [store.getUserId(), concatId]);
}

//阅读会话所有消息
const readAll = (contactId) => {
    let sql = "update chat_session_user set no_read_count = 0 where user_id=? and contact_id=?"
    return run(sql, [store.getUserId(), contactId]);
}


//收到消息新增或者更新会话
const saveOrUpdate4Message = (currentSessionId, sessionInfo) => {
    return new Promise(async (resolve, reject) => {
        let sessionData = await selectUserSessionByContactId(sessionInfo.contactId);
        if (sessionData) {
            await updateSessionInfo4Message(currentSessionId, sessionInfo);
        } else {
            sessionInfo.noReadCount = 1;
            await addChatSession(sessionInfo);
        }
        resolve();
    });
}
//收到消息更新会话
const updateSessionInfo4Message = async (currentSessionId, { sessionId, contactName, lastMessage, lastReceiveTime, contactId, memberCount }) => {
    const params = [lastMessage, lastReceiveTime];
    let sql = "update chat_session_user set last_message=?,last_receive_time=?,status = 1";
    //实时更新联系人信息
    if (contactName) {
        sql = sql + ",contact_name = ?"
        params.push(contactName);
    }
    //成员数量
    if (memberCount != null) {
        sql = sql + ",member_count =?"
        params.push(memberCount);
    }
    //未选中当前session增加未读消息数
    if (sessionId !== currentSessionId) {
        sql = sql + ",no_read_count = no_read_count + 1";
    }
    sql = sql + " where user_id = ? and contact_id = ?";
    params.push(store.getUserId());
    params.push(contactId);
    return run(sql, params);
}

//删除会话
const delChatSession = (contactId) => {
    const paramData = {
        userId: store.getUserId(),
        contactId
    }
    const sessionInfo = {
        status: 0
    }
    return update("chat_session_user", sessionInfo, paramData);
}
//置顶会话
const topChatSession = (contactId, topType) => {
    const paramData = {
        userId: store.getUserId(),
        contactId
    }
    const sessionInfo = {
        topType
    }
    return update("chat_session_user", sessionInfo, paramData);
}

//更新群名称
const updateGroupName = (contactId, groupName) => {
    const paramData = {
        userId: store.getUserId(),
        contactId
    }
    const sessionInfo = {
        contactName: groupName
    }
    return update("chat_session_user", sessionInfo, paramData);
}

const updateStatus = (contactId) => {
    const paramData = {
        userId: store.getUserId(),
        contactId
    }
    const sessionInfo = {
        status: 1
    }
    return update("chat_session_user", sessionInfo, paramData);
}


export {
    updateSessionInfo4Message,
    saveOrUpdate4Message,
    saveOrUpdateChatSessionBatch4Init,
    selectUserSessionList,
    selectUserSessionByContactId,
    updateNoReadCount,
    readAll,
    delChatSession,
    updateGroupName,
    topChatSession,
    updateStatus
}