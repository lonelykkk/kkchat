package com.kkk.controller;

import com.kkk.annotation.GlobalInterceptor;
import com.kkk.entity.dto.TokenUserInfoDto;
import com.kkk.entity.dto.UserContactSearchResultDto;
import com.kkk.entity.vo.ResponseVO;
import com.kkk.redis.RedisUtils;
import com.kkk.service.GroupInfoService;
import com.kkk.service.UserContactApplyService;
import com.kkk.service.UserContactService;
import com.kkk.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;

/**
 * @author lonelykkk
 * @email 2765314967@qq.com
 * @date 2024/11/15 16:30
 * @Version V1.0
 */
@RestController
@RequestMapping("/contact")
public class UserContactController extends ABaseController{
    private static final Logger logger = LoggerFactory.getLogger(UserContactController.class);

    @Resource
    private UserInfoService userInfoService;
    @Resource
    private GroupInfoService groupInfoService;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private UserContactService userContactService;

    @Resource
    private UserContactApplyService userContactApplyService;

    /**
     * 搜索好友/群组
     * @param request
     * @param contactId
     * @return
     */
    @RequestMapping("/search")
    @GlobalInterceptor
    public ResponseVO search(HttpServletRequest request, @NotEmpty String contactId) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
        UserContactSearchResultDto resultDto = userContactService.searchContact(tokenUserInfoDto.getUserId(), contactId);
        return getSuccessResponseVO(resultDto);
    }

    @RequestMapping("/applyAdd")
    @GlobalInterceptor
    public ResponseVO applyAdd(HttpServletRequest request, @NotEmpty String contactId,
                               @NotEmpty String contactType, String applyInfo) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
        Integer joinType = userContactApplyService.applyAdd(tokenUserInfoDto, contactId, contactType, applyInfo);
        return getSuccessResponseVO(joinType);
    }
}
