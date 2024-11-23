package com.kkk.entity.po;

import java.io.Serializable;


/**
 * 聊天消息表
 */
public class ChatMessage implements Serializable {


    /**
     * 消息自增ID
     */
    private Long messageId;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 消息类型
     */
    private Integer messageType;

    /**
     * 消息内容
     */
    private String messageContent;

    /**
     * 发送人ID
     */
    private String sendUserId;

    /**
     * 发送人昵称
     */
    private String sendUserNickName;

    /**
     * 发送时间
     */
    private Long sendTime;

    /**
     * 接收联系人ID
     */
    private String contactId;

    /**
     * 联系人类型 0:单聊 1:群聊
     */
    private Integer contactType;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件类型
     */
    private Integer fileType;

    /**
     * 状态 0:正在发送 1:已发送
     */
    private Integer status;


    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getMessageId() {
        return this.messageId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Integer getMessageType() {
        return this.messageType;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageContent() {
        return this.messageContent;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUserId() {
        return this.sendUserId;
    }

    public void setSendUserNickName(String sendUserNickName) {
        this.sendUserNickName = sendUserNickName;
    }

    public String getSendUserNickName() {
        return this.sendUserNickName;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public Long getSendTime() {
        return this.sendTime;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactId() {
        return this.contactId;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public Integer getContactType() {
        return this.contactType;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public Integer getFileType() {
        return this.fileType;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return "消息自增ID:" + (messageId == null ? "空" : messageId) + "，会话ID:" + (sessionId == null ? "空" : sessionId) + "，消息类型:" + (messageType == null ? "空" : messageType) + "，消息内容:" + (messageContent == null ? "空" : messageContent) + "，发送人ID:" + (sendUserId == null ? "空" : sendUserId) + "，发送人昵称:" + (sendUserNickName == null ? "空" : sendUserNickName) + "，发送时间:" + (sendTime == null ? "空" : sendTime) + "，接收联系人ID:" + (contactId == null ? "空" : contactId) + "，联系人类型 0:单聊 1:群聊:" + (contactType == null ? "空" : contactType) + "，文件大小:" + (fileSize == null ? "空" : fileSize) + "，文件名:" + (fileName == null ? "空" : fileName) + "，文件类型:" + (fileType == null ? "空" : fileType) + "，状态 0:正在发送 1:已发送:" + (status == null ? "空" : status);
    }
}
