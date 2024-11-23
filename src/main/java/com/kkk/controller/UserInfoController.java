package com.kkk.controller;

import com.kkk.entity.constants.Constants;
import com.kkk.entity.dto.TokenUserInfoDto;
import com.kkk.entity.po.UserInfo;
import com.kkk.entity.vo.ResponseVO;
import com.kkk.entity.vo.UserInfoVO;
import com.kkk.service.UserInfoService;
import com.kkk.utils.CopyTools;
import com.kkk.utils.StringTools;
import com.kkk.websocket.ChannelContextUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.IOException;

/**
 * 账号信息 Controller
 */
@RestController("userInfoController")
@RequestMapping("/userInfo")
public class UserInfoController extends ABaseController {

    @Resource
    private UserInfoService userInfoService;
    @Resource
    private ChannelContextUtils channelContextUtils;


    @RequestMapping("/getUserInfo")
    public ResponseVO getUserInfo(HttpServletRequest request) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
        UserInfo userInfo = userInfoService.getUserInfoByUserId(tokenUserInfoDto.getUserId());
        UserInfoVO userInfoVO = CopyTools.copy(userInfo, UserInfoVO.class);
        userInfoVO.setAdmin(tokenUserInfoDto.getAdmin());
        return getSuccessResponseVO(userInfoVO);
    }

    @RequestMapping("/saveUserInfo")
    public ResponseVO saveUserInfo(HttpServletRequest request, UserInfo userInfo, MultipartFile avatarFile, MultipartFile avatarCover) throws IOException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
        userInfo.setUserId(tokenUserInfoDto.getUserId());
        userInfo.setPassword(null);
        userInfo.setStatus(null);
        userInfo.setCreateTime(null);
        userInfo.setLastLoginTime(null);
        this.userInfoService.updateUserInfo(userInfo, avatarFile, avatarCover);
        if (!tokenUserInfoDto.getNickName().equals(userInfo.getNickName())) {
            tokenUserInfoDto.setNickName(userInfo.getNickName());
            resetTokenUserInfo(request, tokenUserInfoDto);
        }
        return getUserInfo(request);
    }

    @RequestMapping("/updatePassword")
    public ResponseVO updatePassword(HttpServletRequest request, @NotEmpty @Pattern(regexp = Constants.REGEX_PASSWORD) String password) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(StringTools.encodeByMD5(password));
        this.userInfoService.updateUserInfoByUserId(userInfo, tokenUserInfoDto.getUserId());
       // channelContextUtils.closeContext(tokenUserInfoDto.getUserId());
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/logout")
    public ResponseVO logout(HttpServletRequest request) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
        // 关闭ws
        channelContextUtils.closeContext(tokenUserInfoDto.getUserId());
        return getSuccessResponseVO(null);
    }
}