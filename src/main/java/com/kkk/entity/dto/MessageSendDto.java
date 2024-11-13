package com.kkk.entity.dto;

import com.kkk.utils.StringTools;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageSendDto<T> implements Serializable {
    private static final long serialVersionUID = -1045752033171142417L;
    //消息ID
    private Long messageId;
    //会话ID
    private String sessionId;
    //发送人
    private String sendUserId;
    //发送人昵称
    private String sendUserNickName;
    //联系人ID
    private String contactId;
    //联系人名称
    private String contactName;
    //消息内容
    private String messageContent;
    //最后的消息
    private String lastMessage;
    //消息类型
    private Integer messageType;
    //发送时间
    private Long sendTime;
    //联系人类型
    private Integer contactType;
    //扩展信息
    private T extendData;

    //消息状态 0:发送中  1:已发送 对于文件是异步上传用状态处理
    private Integer status;

    //文件信息
    private Long fileSize;
    private String fileName;
    private Integer fileType;

    //群员
    private Integer memberCount;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getContactType() {
        return contactType;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    public String getSendUserNickName() {
        return sendUserNickName;
    }

    public void setSendUserNickName(String sendUserNickName) {
        this.sendUserNickName = sendUserNickName;
    }

    public T getExtendData() {
        return extendData;
    }

    public void setExtendData(T extendData) {
        this.extendData = extendData;
    }

    public String getLastMessage() {
        if (StringTools.isEmpty(lastMessage)) {
            return messageContent;
        }
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }
}
