import { queryCount, queryOne, queryAll, insertOrReplace, update } from "./ADB";
import { updateNoReadCount } from "./ChatSessionUserModel"
import store from "../store"

const getPageOffset = (pageNo = 1, totalCount) => {
    const pageSize = 20;
    const pageTotal = (totalCount % pageSize) == 0 ? totalCount / pageSize : Number.parseInt(totalCount / pageSize) + 1;
    //pageNo小于等于1就是1
    pageNo = pageNo <= 1 ? 1 : pageNo;
    //pageNo大于等于总数就是总数
    pageNo = pageNo >= pageTotal ? pageTotal : pageNo;
    return {
        pageTotal,
        offset: (pageNo - 1) * pageSize,
        limit: pageSize
    }
}

const selectMessageList = (query) => {
    return new Promise(async (resolve, reject) => {
        const { sessionId, pageNo, maxMessageId } = query;
        let sql = "select count(1) from chat_message where session_id = ? and user_id = ?";
        const totalCount = await queryCount(sql, [sessionId, store.getUserId()]);
        const { pageTotal, offset, limit } = getPageOffset(pageNo, totalCount);
        const params = [sessionId, store.getUserId()];
        sql = "select * from chat_message where session_id = ? and user_id = ?";
        if (maxMessageId) {
            sql = sql + "and message_id <=?";
            params.push(maxMessageId);
        }
        params.push(offset);
        params.push(limit);
        sql = sql + "order by message_id desc limit ?,?";
        const dataList = await queryAll(sql, params);
        resolve({
            dataList,
            pageTotal,
            pageNo
        })
    })
}

const saveMessage = (data) => {
    data.userId = store.getUserId();
    return insertOrReplace("chat_message", data);
}


const updateMessage = (data, paramData) => {
    paramData.userId = store.getUserId();
    return update("chat_message", data, paramData);
}

const saveMessageBatch = (chatMessageList) => {
    return new Promise(async (resolve, reject) => {
        //插入聊天数据
        const chatSessionCountMap = {};
        chatMessageList.forEach(item => {
            let contactId = item.contactType == 1 ? item.contactId : item.sendUserId;
            let noReadCount = chatSessionCountMap[contactId];
            if (!noReadCount) {
                chatSessionCountMap[contactId] = 1;
            } else {
                chatSessionCountMap[contactId] = noReadCount + 1;
            }
        });
        //跟新未读数
        for (let item in chatSessionCountMap) {
            await updateNoReadCount({ contactId: item, noReadCount: chatSessionCountMap[item] })
        }
        //批量插入
        chatMessageList.forEach(async item => {
            await saveMessage(item);
        });
        //所有处理完成返回
        resolve();
    })
}


const selectByMessageId = (messageId) => {
    let sql = "select * from chat_message where message_id = ? and user_id =?";
    const params = [messageId, store.getUserId()];
    return queryOne(sql, params)
}

export {
    saveMessage,
    updateMessage,
    selectMessageList,
    saveMessageBatch,
    selectByMessageId
}