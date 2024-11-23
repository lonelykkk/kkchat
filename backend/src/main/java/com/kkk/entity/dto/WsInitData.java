package com.kkk.entity.dto;

import com.kkk.entity.po.ChatMessage;
import com.kkk.entity.po.ChatSessionUser;

import java.util.List;

public class WsInitData {
    private List<ChatSessionUser> chatSessionList;
    private List<ChatMessage> chatMessageList;
    private Integer applyCount;

    public List<ChatSessionUser> getChatSessionList() {
        return chatSessionList;
    }

    public void setChatSessionList(List<ChatSessionUser> chatSessionList) {
        this.chatSessionList = chatSessionList;
    }

    public List<ChatMessage> getChatMessageList() {
        return chatMessageList;
    }

    public void setChatMessageList(List<ChatMessage> chatMessageList) {
        this.chatMessageList = chatMessageList;
    }

    public Integer getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(Integer applyCount) {
        this.applyCount = applyCount;
    }
}
