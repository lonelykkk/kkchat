package com.kkk.service;

import com.kkk.entity.dto.MessageSendDto;
import com.kkk.entity.dto.TokenUserInfoDto;
import com.kkk.entity.po.ChatMessage;
import com.kkk.entity.query.ChatMessageQuery;
import com.kkk.entity.vo.PaginationResultVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;


/**
 * 聊天消息表 业务接口
 */
public interface ChatMessageService {

    /**
     * 根据条件查询列表
     */
    List<ChatMessage> findListByParam(ChatMessageQuery param);

    /**
     * 根据条件查询列表
     */
    Integer findCountByParam(ChatMessageQuery param);

    /**
     * 分页查询
     */
    PaginationResultVO<ChatMessage> findListByPage(ChatMessageQuery param);

    /**
     * 新增
     */
    Integer add(ChatMessage bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<ChatMessage> listBean);

    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<ChatMessage> listBean);

    /**
     * 多条件更新
     */
    Integer updateByParam(ChatMessage bean, ChatMessageQuery param);

    /**
     * 多条件删除
     */
    Integer deleteByParam(ChatMessageQuery param);

    /**
     * 根据MessageId查询对象
     */
    ChatMessage getChatMessageByMessageId(Long messageId);


    /**
     * 根据MessageId修改
     */
    Integer updateChatMessageByMessageId(ChatMessage bean, Long messageId);


    /**
     * 根据MessageId删除
     */
    Integer deleteChatMessageByMessageId(Long messageId);

    MessageSendDto saveMessage(ChatMessage chatMessage, TokenUserInfoDto tokenUserInfoDto);

    void saveMessageFile(String userId, Long messageId, MultipartFile file, MultipartFile cover);

    File downloadFile(TokenUserInfoDto userInfoDto, Long messageId, Boolean cover);
}