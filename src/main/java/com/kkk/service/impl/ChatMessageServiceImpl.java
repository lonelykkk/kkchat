package com.kkk.service.impl;

import com.kkk.entity.config.AppConfig;
import com.kkk.entity.constants.Constants;
import com.kkk.entity.dto.MessageSendDto;
import com.kkk.entity.dto.SysSettingDto;
import com.kkk.entity.dto.TokenUserInfoDto;
import com.kkk.entity.enums.*;
import com.kkk.entity.po.ChatMessage;
import com.kkk.entity.po.ChatSession;
import com.kkk.entity.po.UserContact;
import com.kkk.entity.query.ChatMessageQuery;
import com.kkk.entity.query.ChatSessionQuery;
import com.kkk.entity.query.SimplePage;
import com.kkk.entity.query.UserContactQuery;
import com.kkk.entity.vo.PaginationResultVO;
import com.kkk.exception.BusinessException;
import com.kkk.mappers.ChatMessageMapper;
import com.kkk.mappers.ChatSessionMapper;
import com.kkk.mappers.UserContactMapper;
import com.kkk.redis.RedisComponent;
import com.kkk.service.ChatMessageService;
import com.kkk.utils.CopyTools;
import com.kkk.utils.DateUtil;
import com.kkk.utils.StringTools;
import jodd.util.ArraysUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;


/**
 * 聊天消息表 业务接口实现
 */
@Service("chatMessageService")
public class ChatMessageServiceImpl implements ChatMessageService {

    private static final Logger logger = LoggerFactory.getLogger(ChatMessageServiceImpl.class);

    @Resource
    private ChatMessageMapper<ChatMessage, ChatMessageQuery> chatMessageMapper;

    @Resource
    private ChatSessionMapper<ChatSession, ChatSessionQuery> chatSessionMapper;


    @Resource
    private AppConfig appConfig;

    @Resource
    private UserContactMapper<UserContact, UserContactQuery> userContactMapper;

    @Resource
    private RedisComponent redisComponent;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<ChatMessage> findListByParam(ChatMessageQuery param) {
        return this.chatMessageMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(ChatMessageQuery param) {
        return this.chatMessageMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<ChatMessage> findListByPage(ChatMessageQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<ChatMessage> list = this.findListByParam(param);
        PaginationResultVO<ChatMessage> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(ChatMessage bean) {
        return this.chatMessageMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<ChatMessage> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.chatMessageMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<ChatMessage> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.chatMessageMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(ChatMessage bean, ChatMessageQuery param) {
        StringTools.checkParam(param);
        return this.chatMessageMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(ChatMessageQuery param) {
        StringTools.checkParam(param);
        return this.chatMessageMapper.deleteByParam(param);
    }

    /**
     * 根据MessageId获取对象
     */
    @Override
    public ChatMessage getChatMessageByMessageId(Long messageId) {
        return this.chatMessageMapper.selectByMessageId(messageId);
    }

    /**
     * 根据MessageId修改
     */
    @Override
    public Integer updateChatMessageByMessageId(ChatMessage bean, Long messageId) {
        return this.chatMessageMapper.updateByMessageId(bean, messageId);
    }

    /**
     * 根据MessageId删除
     */
    @Override
    public Integer deleteChatMessageByMessageId(Long messageId) {
        return this.chatMessageMapper.deleteByMessageId(messageId);
    }


    @Override
    public MessageSendDto saveMessage(ChatMessage chatMessage, TokenUserInfoDto tokenUserInfoDto) {
        //不是机器人回复，判断好友状态
        if (!Constants.ROBOT_UID.equals(tokenUserInfoDto.getUserId())) {
            List<String> contactList = redisComponent.getUserContactList(tokenUserInfoDto.getUserId());
            if (!contactList.contains(chatMessage.getContactId())) {
                UserContactTypeEnum userContactTypeEnum = UserContactTypeEnum.getByPrefix(chatMessage.getContactId());
                if (userContactTypeEnum == UserContactTypeEnum.USER) {
                    throw new BusinessException(ResponseCodeEnum.CODE_902);
                } else {
                    throw new BusinessException(ResponseCodeEnum.CODE_903);
                }
            }
        }

        return null;
    }

    @Override
    public void saveMessageFile(String userId, Long messageId, MultipartFile file, MultipartFile cover) {

    }

    @Override
    public File downloadFile(TokenUserInfoDto userInfoDto, Long messageId, Boolean cover) {
        return null;
    }
}