package com.kkk.controller;

import com.kkk.annotation.GlobalInterceptor;
import com.kkk.entity.dto.TokenUserInfoDto;
import com.kkk.entity.vo.ResponseVO;
import com.kkk.service.GroupInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName GroupController
 * @Description TODO
 * @Author 程序员老罗 https://space.bilibili.com/499388891
 * @Date 2023/12/24 20:04
 */
@RestController("groupController")
@RequestMapping("/group")
public class GroupController extends ABaseController {

    @Resource
    private GroupInfoService groupInfoService;

    @RequestMapping(value = "/saveGroup")
    @GlobalInterceptor
    public ResponseVO saveGroup(HttpServletRequest request,
                                String groupId,
                                @NotEmpty String groupName,
                                String groupNotice,
                                @NotNull Integer joinType,
                                MultipartFile avatarFile,
                                MultipartFile avatarCover)  {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);

        return getSuccessResponseVO(null);
    }
}
