package com.kkk.controller;

import com.kkk.annotation.GlobalInterceptor;
import com.kkk.entity.po.UserInfoBeauty;
import com.kkk.entity.query.UserInfoBeautyQuery;
import com.kkk.entity.query.UserInfoQuery;
import com.kkk.entity.vo.PaginationResultVO;
import com.kkk.entity.vo.ResponseVO;
import com.kkk.service.UserInfoBeautyService;
import com.kkk.service.UserInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author lonelykkk
 * @email 2765314967@qq.com
 * @date 2024/11/18 8:11
 * @Version V1.0
 */
@RestController("userInfoBeautyController")
@RequestMapping("/admin")
@Validated
public class AdminUserInfoBeautyController extends ABaseController{

    @Resource
    private UserInfoBeautyService userInfoBeautyService;

    /**
     * 根据条件分页查询
     */
    @RequestMapping("/loadBeautyAccountList")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO loadBeautyAccountList(UserInfoBeautyQuery query) {
        return getSuccessResponseVO(userInfoBeautyService.findListByPage(query));
    }

    @RequestMapping("/saveBeautAccount")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO saveBeautAccount(UserInfoBeauty beauty) {
        userInfoBeautyService.saveAccount(beauty);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/delBeautAccount")
    @GlobalInterceptor(checkAdmin = true)
    public ResponseVO delBeautAccount(@NotNull Integer id) {
        return getSuccessResponseVO(userInfoBeautyService.deleteUserInfoBeautyById(id));
    }
}
