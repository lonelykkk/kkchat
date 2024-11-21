package com.kkk.service.impl;

import com.kkk.entity.constants.Constants;
import com.kkk.entity.dto.MessageSendDto;
import com.kkk.entity.dto.TokenUserInfoDto;
import com.kkk.entity.enums.*;
import com.kkk.entity.po.GroupInfo;
import com.kkk.entity.po.UserContact;
import com.kkk.entity.po.UserContactApply;
import com.kkk.entity.po.UserInfo;
import com.kkk.entity.query.*;
import com.kkk.entity.vo.PaginationResultVO;
import com.kkk.exception.BusinessException;
import com.kkk.mappers.GroupInfoMapper;
import com.kkk.mappers.UserContactApplyMapper;
import com.kkk.mappers.UserContactMapper;
import com.kkk.mappers.UserInfoMapper;
import com.kkk.service.UserContactApplyService;
import com.kkk.service.UserContactService;
import com.kkk.utils.StringTools;
import com.kkk.websocket.MessageHandler;
import jodd.util.ArraysUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 联系人申请 业务接口实现
 */
@Service("userContactApplyService")
public class UserContactApplyServiceImpl implements UserContactApplyService {

    @Resource
    private UserContactApplyMapper<UserContactApply, UserContactApplyQuery> userContactApplyMapper;

    @Resource
    private UserContactMapper<UserContact, UserContactQuery> userContactMapper;

    @Resource
    private GroupInfoMapper<GroupInfo, GroupInfoQuery> groupInfoMapper;

    @Resource
    private UserInfoMapper<UserInfo, UserInfoQuery> userInfoMapper;


    @Resource
    private UserContactService userContactService;
    @Resource
    private MessageHandler messageHandler;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<UserContactApply> findListByParam(UserContactApplyQuery param) {
        return this.userContactApplyMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(UserContactApplyQuery param) {
        return this.userContactApplyMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<UserContactApply> findListByPage(UserContactApplyQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<UserContactApply> list = this.findListByParam(param);
        PaginationResultVO<UserContactApply> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(UserContactApply bean) {
        return this.userContactApplyMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<UserContactApply> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userContactApplyMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<UserContactApply> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.userContactApplyMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(UserContactApply bean, UserContactApplyQuery param) {
        StringTools.checkParam(param);
        return this.userContactApplyMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(UserContactApplyQuery param) {
        StringTools.checkParam(param);
        return this.userContactApplyMapper.deleteByParam(param);
    }

    /**
     * 根据ApplyId获取对象
     */
    @Override
    public UserContactApply getUserContactApplyByApplyId(Integer applyId) {
        return this.userContactApplyMapper.selectByApplyId(applyId);
    }

    /**
     * 根据ApplyId修改
     */
    @Override
    public Integer updateUserContactApplyByApplyId(UserContactApply bean, Integer applyId) {
        return this.userContactApplyMapper.updateByApplyId(bean, applyId);
    }

    /**
     * 根据ApplyId删除
     */
    @Override
    public Integer deleteUserContactApplyByApplyId(Integer applyId) {
        return this.userContactApplyMapper.deleteByApplyId(applyId);
    }

    /**
     * 根据ApplyUserIdAndReceiveUserIdAndContactId获取对象
     */
    @Override
    public UserContactApply getUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId) {
        return this.userContactApplyMapper.selectByApplyUserIdAndReceiveUserIdAndContactId(applyUserId, receiveUserId, contactId);
    }

    /**
     * 根据ApplyUserIdAndReceiveUserIdAndContactId修改
     */
    @Override
    public Integer updateUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(UserContactApply bean, String applyUserId, String receiveUserId, String contactId) {
        return this.userContactApplyMapper.updateByApplyUserIdAndReceiveUserIdAndContactId(bean, applyUserId, receiveUserId, contactId);
    }

    /**
     * 根据ApplyUserIdAndReceiveUserIdAndContactId删除
     */
    @Override
    public Integer deleteUserContactApplyByApplyUserIdAndReceiveUserIdAndContactId(String applyUserId, String receiveUserId, String contactId) {
        return this.userContactApplyMapper.deleteByApplyUserIdAndReceiveUserIdAndContactId(applyUserId, receiveUserId, contactId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer applyAdd(TokenUserInfoDto tokenUserInfoDto, String contactId, String contactType, String applyInfo) {
        //获取添加类型(加群还是加好友)
        UserContactTypeEnum typeEnum = UserContactTypeEnum.getByPrefix(contactId);
        if (typeEnum == null) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        //申请人id
        final String applyUserId = tokenUserInfoDto.getUserId();
        //默认申请信息
        applyInfo = StringTools.isEmpty(applyInfo) ? String.format(Constants.APPLY_INFO_TEMPLATE, tokenUserInfoDto.getNickName()) : applyInfo;
        Long curDate = System.currentTimeMillis();
        Integer joinType = null;
        //被添加人id
        String receiveUserId = contactId;
        //查询对方好友是否已经添加，如果已经拉黑无法添加
        UserContact userContact = userContactMapper.selectByUserIdAndContactId(applyUserId, contactId);
        if (userContact != null &&
                ArraysUtil.contains(new Integer[]{
                        UserContactStatusEnum.BLACKLIST_BE.getStatus(),
                        UserContactStatusEnum.BLACKLIST_BE_FIRST.getStatus()
                }, userContact.getStatus())) {
            throw new BusinessException("对方已经你拉黑，无法添加");
        }
        //如果添加类型为群组
        if (UserContactTypeEnum.GROUP == typeEnum) {
            GroupInfo groupInfo = groupInfoMapper.selectByGroupId(contactId);
            if (groupInfo == null||GroupStatusEnum.DISSOLUTION.getStatus().equals(groupInfo.getStatus())) {
                throw new BusinessException("群聊不存在或已解散");
            }
            //获取群主id
            receiveUserId = groupInfo.getGroupOwnerId();
            //获取添加类型（是否需要验证通过）
            joinType = groupInfo.getJoinType();
        }else {
            UserInfo userInfo = userInfoMapper.selectByUserId(contactId);
            if (userInfo == null) {
                throw new BusinessException(ResponseCodeEnum.CODE_600);
            }
            joinType = userInfo.getJoinType();
        }
        //直接加入不用记录申请记录
        if (JoinTypeEnum.JOIN.getType().equals(joinType)) {
            this.userContactService.addContact(applyUserId, receiveUserId, contactId, typeEnum.getType(), applyInfo);
            return joinType;
        }
        //记录申请记录
        UserContactApply dbApply = this.userContactApplyMapper.selectByApplyUserIdAndReceiveUserIdAndContactId(applyUserId, receiveUserId, contactId);
        if (dbApply == null) {
            UserContactApply contactApply = new UserContactApply();
            contactApply.setApplyUserId(applyUserId);
            contactApply.setContactType(typeEnum.getType());
            contactApply.setReceiveUserId(receiveUserId);
            contactApply.setLastApplyTime(curDate);
            contactApply.setContactId(contactId);
            contactApply.setStatus(UserContactApplyStatusEnum.INIT.ordinal());
            contactApply.setApplyInfo(applyInfo);
            this.userContactApplyMapper.insert(contactApply);
        } else {
            //更新状态
            UserContactApply contactApply = new UserContactApply();
            contactApply.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
            contactApply.setLastApplyTime(curDate);
            contactApply.setApplyInfo(applyInfo);
            this.userContactApplyMapper.updateByApplyId(contactApply, dbApply.getApplyId());
        }
        if (dbApply == null || !UserContactApplyStatusEnum.INIT.getStatus().equals(dbApply.getStatus())) {
            //如果是待处理状态就不发消息，避免重复发送
            //发送ws消息
            MessageSendDto messageSend = new MessageSendDto();
            messageSend.setMessageType(MessageTypeEnum.CONTACT_APPLY.getType());
            messageSend.setMessageContent(applyInfo);
            messageSend.setContactId(receiveUserId);
            messageHandler.sendMessage(messageSend);
        }
        return joinType;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dealWithApply(String userId, Integer applyId, Integer status) {
        UserContactApplyStatusEnum statusEnum = UserContactApplyStatusEnum.getByStatus(status);
        if (null == statusEnum || UserContactApplyStatusEnum.INIT == statusEnum) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        UserContactApply applyInfo = this.userContactApplyMapper.selectByApplyId(applyId);
        if (applyInfo == null || !userId.equals(applyInfo.getReceiveUserId())) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        //更新申请信息 只能由待处理更新为其他状态
        UserContactApply updateInfo = new UserContactApply();
        updateInfo.setStatus(statusEnum.getStatus());
        updateInfo.setLastApplyTime(System.currentTimeMillis());

        UserContactApplyQuery applyQuery = new UserContactApplyQuery();
        applyQuery.setApplyId(applyId);
        applyQuery.setStatus(UserContactApplyStatusEnum.INIT.getStatus());
        Integer count = userContactApplyMapper.updateByParam(updateInfo, applyQuery);
        if (count == 0) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        if (UserContactApplyStatusEnum.PASS.getStatus().equals(status)) {
            //添加联系人
            userContactService.addContact(applyInfo.getApplyUserId(), applyInfo.getReceiveUserId(), applyInfo.getContactId(), applyInfo.getContactType(), applyInfo.getApplyInfo());
            return;
        }

        if (UserContactApplyStatusEnum.BLACKLIST == statusEnum) {
            //拉黑 将接收人添加到申请人的联系人中，标记申请人被拉黑
            Date curDate = new Date();
            UserContact userContact = new UserContact();
            userContact.setUserId(applyInfo.getApplyUserId());
            userContact.setContactId(applyInfo.getContactId());
            userContact.setContactType(applyInfo.getContactType());
            userContact.setCreateTime(curDate);
            userContact.setStatus(UserContactStatusEnum.BLACKLIST_BE_FIRST.getStatus());
            userContact.setLastUpdateTime(curDate);
            userContactMapper.insertOrUpdate(userContact);
            return;
        }
    }
}