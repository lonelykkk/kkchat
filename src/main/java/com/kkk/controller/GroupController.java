package com.kkk.controller;

import com.kkk.annotation.GlobalInterceptor;
import com.kkk.entity.dto.TokenUserInfoDto;
import com.kkk.entity.enums.GroupStatusEnum;
import com.kkk.entity.enums.UserContactStatusEnum;
import com.kkk.entity.po.GroupInfo;
import com.kkk.entity.po.UserContact;
import com.kkk.entity.query.GroupInfoQuery;
import com.kkk.entity.query.UserContactQuery;
import com.kkk.entity.vo.GroupInfoVO;
import com.kkk.entity.vo.ResponseVO;
import com.kkk.exception.BusinessException;
import com.kkk.service.GroupInfoService;
import com.kkk.service.UserContactService;
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
 * @Author lonelykkk https://github.com/lonelykkk
 * @Date 2024/10/24 20:04
 */
@RestController("groupController")
@RequestMapping("/group")
public class GroupController extends ABaseController {

    @Resource
    private GroupInfoService groupInfoService;
    @Resource
    private UserContactService userContactService;
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
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setGroupId(groupId);
        groupInfo.setGroupOwnerId(tokenUserInfoDto.getUserId());
        groupInfo.setGroupName(groupName);
        groupInfo.setGroupNotice(groupNotice);
        groupInfo.setJoinType(joinType);
        this.groupInfoService.saveGroup(groupInfo, avatarFile, avatarCover);
        return getSuccessResponseVO(null);
    }

    @RequestMapping(value = "/loadMyGroup")
    @GlobalInterceptor
    public ResponseVO loadMyGroup(HttpServletRequest request) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
        GroupInfoQuery infoQuery = new GroupInfoQuery();
        infoQuery.setGroupOwnerId(tokenUserInfoDto.getUserId());
        infoQuery.setOrderBy("create_time desc");
        List<GroupInfo> groupInfoList = this.groupInfoService.findListByParam(infoQuery);
        return getSuccessResponseVO(groupInfoList);
    }

    /**
     * 获取群信息
     *
     * @param request
     * @param groupId
     * @return
     */
    @RequestMapping(value = "/getGroupInfo")
    @GlobalInterceptor
    public ResponseVO getGroupInfo(HttpServletRequest request,
                                   @NotEmpty String groupId) {
        GroupInfo groupInfo = getGroupDetailCommon(request, groupId);
        UserContactQuery userContactQuery = new UserContactQuery();
        userContactQuery.setContactId(groupId);
        Integer memberCount = this.userContactService.findCountByParam(userContactQuery);
        groupInfo.setMemberCount(memberCount);
        return getSuccessResponseVO(groupInfo);
    }

    private GroupInfo getGroupDetailCommon(HttpServletRequest request, String groupId) {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
        UserContact userContact = this.userContactService.getUserContactByUserIdAndContactId(tokenUserInfoDto.getUserId(), groupId);
        if (userContact == null || !UserContactStatusEnum.FRIEND.getStatus().equals(userContact.getStatus())) {
            throw new BusinessException("你不在群聊或者群聊不存在或已经解散");
        }
        GroupInfo groupInfo = this.groupInfoService.getGroupInfoByGroupId(groupId);
        if (groupInfo == null || !GroupStatusEnum.NORMAL.getStatus().equals(groupInfo.getStatus())) {
            throw new BusinessException("群聊不存在或已经解散");
        }
        return groupInfo;
    }

    @RequestMapping(value = "/getGroupInfo4Chat")
    @GlobalInterceptor
    public ResponseVO getGroupInfo4Chat(HttpServletRequest request, @NotEmpty String groupId) {
        GroupInfo groupInfo = getGroupDetailCommon(request, groupId);

        UserContactQuery userContactQuery = new UserContactQuery();
        userContactQuery.setContactId(groupId);
        userContactQuery.setQueryUserInfo(true);
        userContactQuery.setOrderBy("create_time asc");
        userContactQuery.setStatus(UserContactStatusEnum.FRIEND.getStatus());
        List<UserContact> userContactList = this.userContactService.findListByParam(userContactQuery);

        GroupInfoVO groupInfoVo = new GroupInfoVO();
        groupInfoVo.setGroupInfo(groupInfo);
        groupInfoVo.setUserContactList(userContactList);
        return getSuccessResponseVO(groupInfoVo);
    }
}
