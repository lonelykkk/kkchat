package com.kkk.service;

import com.kkk.entity.po.ChatSession;
import com.kkk.entity.query.ChatSessionQuery;
import com.kkk.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 会话信息 业务接口
 */
public interface ChatSessionService {

	/**
	 * 根据条件查询列表
	 */
	List<ChatSession> findListByParam(ChatSessionQuery param);

	/**
	 * 根据条件查询列表
	 */
	Integer findCountByParam(ChatSessionQuery param);

	/**
	 * 分页查询
	 */
	PaginationResultVO<ChatSession> findListByPage(ChatSessionQuery param);

	/**
	 * 新增
	 */
	Integer add(ChatSession bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<ChatSession> listBean);

	/**
	 * 批量新增/修改
	 */
	Integer addOrUpdateBatch(List<ChatSession> listBean);

	/**
	 * 多条件更新
	 */
	Integer updateByParam(ChatSession bean,ChatSessionQuery param);

	/**
	 * 多条件删除
	 */
	Integer deleteByParam(ChatSessionQuery param);

	/**
	 * 根据SessionId查询对象
	 */
	ChatSession getChatSessionBySessionId(String sessionId);


	/**
	 * 根据SessionId修改
	 */
	Integer updateChatSessionBySessionId(ChatSession bean,String sessionId);


	/**
	 * 根据SessionId删除
	 */
	Integer deleteChatSessionBySessionId(String sessionId);

}