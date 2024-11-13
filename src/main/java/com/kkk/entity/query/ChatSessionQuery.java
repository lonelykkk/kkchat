package com.kkk.entity.query;


/**
 * 会话信息参数
 */
public class ChatSessionQuery extends BaseParam {


    /**
     * 会话ID
     */
    private String sessionId;

    private String sessionIdFuzzy;

    /**
     * 最后接受的消息
     */
    private String lastMessage;

    private String lastMessageFuzzy;

    /**
     * 最后接受消息时间毫秒
     */
    private Long lastReceiveTime;


    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionIdFuzzy(String sessionIdFuzzy) {
        this.sessionIdFuzzy = sessionIdFuzzy;
    }

    public String getSessionIdFuzzy() {
        return this.sessionIdFuzzy;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessage() {
        return this.lastMessage;
    }

    public void setLastMessageFuzzy(String lastMessageFuzzy) {
        this.lastMessageFuzzy = lastMessageFuzzy;
    }

    public String getLastMessageFuzzy() {
        return this.lastMessageFuzzy;
    }

    public void setLastReceiveTime(Long lastReceiveTime) {
        this.lastReceiveTime = lastReceiveTime;
    }

    public Long getLastReceiveTime() {
        return this.lastReceiveTime;
    }

}
