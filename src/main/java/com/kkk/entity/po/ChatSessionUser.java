package com.kkk.entity.po;

import com.kkk.entity.enums.UserContactTypeEnum;

import java.io.Serializable;


/**
 * 会话用户
 */
public class ChatSessionUser implements Serializable {


    /**
     * 用户ID
     */
    private String userId;

    /**
     * 联系人ID
     */
    private String contactId;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 联系人名称
     */
    private String contactName;

    private String lastMessage;

    private Long lastReceiveTime;

    private Integer contactType;

    private Integer memberCount;

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public Integer getContactType() {
        return UserContactTypeEnum.getByPrefix(contactId).getType();
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Long getLastReceiveTime() {
        return lastReceiveTime;
    }

    public void setLastReceiveTime(Long lastReceiveTime) {
        this.lastReceiveTime = lastReceiveTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactId() {
        return this.contactId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactName() {
        return this.contactName;
    }

    @Override
    public String toString() {
        return "用户ID:" + (userId == null ? "空" : userId) + "，联系人ID:" + (contactId == null ? "空" : contactId) + "，会话ID:" + (sessionId == null ? "空" : sessionId) +
                "，联系人名称:" + (contactName == null ? "空" : contactName);
    }
}
