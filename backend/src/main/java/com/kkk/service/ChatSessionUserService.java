package com.kkk.service;

import com.kkk.entity.po.ChatSessionUser;
import com.kkk.entity.query.ChatSessionUserQuery;
import com.kkk.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 会话用户 业务接口
 */
public interface ChatSessionUserService {

    /**
     * 根据条件查询列表
     */
    List<ChatSessionUser> findListByParam(ChatSessionUserQuery param);

    /**
     * 根据条件查询列表
     */
    Integer findCountByParam(ChatSessionUserQuery param);

    /**
     * 分页查询
     */
    PaginationResultVO<ChatSessionUser> findListByPage(ChatSessionUserQuery param);

    /**
     * 新增
     */
    Integer add(ChatSessionUser bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<ChatSessionUser> listBean);

    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<ChatSessionUser> listBean);

    /**
     * 多条件更新
     */
    Integer updateByParam(ChatSessionUser bean, ChatSessionUserQuery param);

    /**
     * 多条件删除
     */
    Integer deleteByParam(ChatSessionUserQuery param);

    /**
     * 根据UserIdAndContactId查询对象
     */
    ChatSessionUser getChatSessionUserByUserIdAndContactId(String userId, String contactId);


    /**
     * 根据UserIdAndContactId修改
     */
    Integer updateChatSessionUserByUserIdAndContactId(ChatSessionUser bean, String userId, String contactId);


    /**
     * 根据UserIdAndContactId删除
     */
    Integer deleteChatSessionUserByUserIdAndContactId(String userId, String contactId);

    void updateRedundanceInfo(String contactName, String contactId);
}