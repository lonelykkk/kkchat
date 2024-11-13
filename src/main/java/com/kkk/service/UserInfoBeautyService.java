package com.kkk.service;

import com.kkk.entity.po.UserInfoBeauty;
import com.kkk.entity.query.UserInfoBeautyQuery;
import com.kkk.entity.vo.PaginationResultVO;

import java.util.List;


/**
 * 靓号 业务接口
 */
public interface UserInfoBeautyService {

    /**
     * 根据条件查询列表
     */
    List<UserInfoBeauty> findListByParam(UserInfoBeautyQuery param);

    /**
     * 根据条件查询列表
     */
    Integer findCountByParam(UserInfoBeautyQuery param);

    /**
     * 分页查询
     */
    PaginationResultVO<UserInfoBeauty> findListByPage(UserInfoBeautyQuery param);

    /**
     * 新增
     */
    Integer add(UserInfoBeauty bean);

    /**
     * 批量新增
     */
    Integer addBatch(List<UserInfoBeauty> listBean);

    /**
     * 批量新增/修改
     */
    Integer addOrUpdateBatch(List<UserInfoBeauty> listBean);

    /**
     * 多条件更新
     */
    Integer updateByParam(UserInfoBeauty bean, UserInfoBeautyQuery param);

    /**
     * 多条件删除
     */
    Integer deleteByParam(UserInfoBeautyQuery param);

    /**
     * 根据Id查询对象
     */
    UserInfoBeauty getUserInfoBeautyById(Integer id);


    /**
     * 根据Id修改
     */
    Integer updateUserInfoBeautyById(UserInfoBeauty bean, Integer id);


    /**
     * 根据Id删除
     */
    Integer deleteUserInfoBeautyById(Integer id);


    /**
     * 根据UserId查询对象
     */
    UserInfoBeauty getUserInfoBeautyByUserId(String userId);


    /**
     * 根据UserId修改
     */
    Integer updateUserInfoBeautyByUserId(UserInfoBeauty bean, String userId);


    /**
     * 根据UserId删除
     */
    Integer deleteUserInfoBeautyByUserId(String userId);


    /**
     * 根据Email查询对象
     */
    UserInfoBeauty getUserInfoBeautyByEmail(String email);


    /**
     * 根据Email修改
     */
    Integer updateUserInfoBeautyByEmail(UserInfoBeauty bean, String email);


    /**
     * 根据Email删除
     */
    Integer deleteUserInfoBeautyByEmail(String email);

    void saveAccount(UserInfoBeauty beauty);
}