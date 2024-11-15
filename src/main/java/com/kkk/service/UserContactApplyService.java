package com.kkk.service;

import com.kkk.entity.dto.TokenUserInfoDto;
import com.kkk.entity.po.UserContactApply;
import com.kkk.entity.query.UserContactApplyQuery;
import com.kkk.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 联系人申请 业务接口
 */
public interface UserContactApplyService {

    /**
     * 根据条件查询列表
     */
    List<UserContactApply> findListByParam(UserContactApplyQuery param);

    /**
     * 根据条件查询列表
     */
    Integer findCountByParam(UserContactApplyQuery param);

    /**
     * 分页查询
     */
    PaginationResultVO<UserContactApply> findListByPage(UserContactApplyQuery param);

    /**
     * 新增
     */
    Integer add(UserContactApply bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<UserContactApply> listBean);

    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<UserContactApply> listBean);

    /**
     * 多条件更新
     */
    Integer updateByParam(UserContactApply bean, UserContactApplyQuery param);

    /**
     * 多条件删除
     */
    Integer deleteByParam(UserContactApplyQuery param);

    /**
     * 根据ApplyId查询对象
     */
    UserContactApply getUserContactApplyByApplyId(Integer applyId);


    /**
     * 根据ApplyId修改
     */
    Integer updateUserContactApplyByApplyId(UserContactApply bean, Integer applyId);


    /**
     * 根据ApplyId删除
     */
    Integer deleteUserContactApplyByApplyId(Integer applyId);


    /**
     * 根据ApplyUserIdAndReceiveUserIdAndContactId查询对象
     */
    UserContactApply getUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId);


    /**
     * 根据ApplyUserIdAndReceiveUserIdAndContactId修改
     */
    Integer updateUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(UserContactApply bean, String applyUserId, String receiveUserId, String contactId);


    /**
     * 根据ApplyUserIdAndReceiveUserIdAndContactId删除
     */
    Integer deleteUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId);

    Integer applyAdd(TokenUserInfoDto tokenUserInfoDto, String contactId, String contactType, String applyInfo);

    void dealWithApply(String userId, Integer applyId, Integer status);
}