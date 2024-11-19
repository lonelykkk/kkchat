package com.kkk.redis;

import com.kkk.entity.constants.Constants;
import com.kkk.entity.dto.SysSettingDto;
import com.kkk.entity.dto.TokenUserInfoDto;
import com.kkk.utils.StringTools;
import io.netty.channel.Channel;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class RedisComponent {
    @Resource
    private RedisUtils redisUtils;

    @Resource
    private RedissonClient redissonClient;

    /**
     * 获取token信息
     *
     * @param token
     * @return
     */
    public TokenUserInfoDto getTokenUserInfoDto(String token) {
        TokenUserInfoDto tokenUserInfoDto = (TokenUserInfoDto) redisUtils.get(Constants.REDIS_KEY_WS_TOKEN + token);
        return tokenUserInfoDto;
    }

    public TokenUserInfoDto getTokenUserInfoDtoByUserId(String userId) {
        String token = (String) redisUtils.get(Constants.REDIS_KEY_WS_TOKEN_USERID + userId);
        return getTokenUserInfoDto(token);
    }
    
    public void saveTokenUserInfoDto(TokenUserInfoDto tokenUserInfoDto) {
        redisUtils.setex(Constants.REDIS_KEY_WS_TOKEN + tokenUserInfoDto.getToken(), tokenUserInfoDto, Constants.REDIS_KEY_EXPIRES_DAY * 2);
        redisUtils.setex(Constants.REDIS_KEY_WS_TOKEN_USERID + tokenUserInfoDto.getUserId(), tokenUserInfoDto.getToken(), Constants.REDIS_KEY_EXPIRES_DAY * 2);
    }

    /**
     * 清除token信息
     *
     * @param userId
     */
    public void cleanUserTokenByUserId(String userId) {
        String token = (String) redisUtils.get(Constants.REDIS_KEY_WS_TOKEN_USERID + userId);
        if (!StringTools.isEmpty(token)) {
            redisUtils.delete(Constants.REDIS_KEY_WS_TOKEN + token);
        }
    }


    //保存最后心跳时间
    public void saveUserHeartBeat(String userId) {
        redisUtils.setex(Constants.REDIS_KEY_WS_USER_HEART_BEAT + userId, System.currentTimeMillis(), Constants.REDIS_KEY_EXPIRES_HEART_BEAT);
    }

    //删除用户心跳
    public void removeUserHeartBeat(String userId) {
        redisUtils.delete(Constants.REDIS_KEY_WS_USER_HEART_BEAT + userId);
    }


    //获取用户心跳
    public Long getUserHeartBeat(String userId) {
        return (Long) redisUtils.get(Constants.REDIS_KEY_WS_USER_HEART_BEAT + userId);
    }

    //获取用户联系人
    public List<String> getUserContactList(String userId) {
        return redisUtils.getQueueList(Constants.REDIS_KEY_USER_CONTACT + userId);
    }

    //添加用户联系人
    public void addUserContact(String userId, String contactId) {
        List<String> contactList = redisUtils.getQueueList(Constants.REDIS_KEY_USER_CONTACT + userId);
        if (!contactList.contains(contactId)) {
            redisUtils.lpush(Constants.REDIS_KEY_USER_CONTACT + userId, contactId, Constants.REDIS_KEY_TOKEN_EXPIRES);
        }
    }

    //清空用户联系人
    public void cleanUserContact(String userId) {
        redisUtils.delete(Constants.REDIS_KEY_USER_CONTACT + userId);
    }

    //删除用户联系人
    public void removeUserContact(String userId, String contactId) {
        redisUtils.remove(Constants.REDIS_KEY_USER_CONTACT + userId, contactId);
    }

    //批量添加用户联系人
    public void addUserContactBatch(String userId, List<String> contactIdList) {
        redisUtils.lpushAll(Constants.REDIS_KEY_USER_CONTACT + userId, contactIdList, Constants.REDIS_KEY_TOKEN_EXPIRES);
    }

    //获取用户session列表
    public List<String> getUserSessionList(String userId) {
        return redisUtils.getQueueList(Constants.REDIS_KEY_USER_SESSION + userId);
    }

    //添加用户Session
    public void addUserSession(String userId, String sessionId) {
        List<String> sessionList = redisUtils.getQueueList(Constants.REDIS_KEY_USER_SESSION + userId);
        if (!sessionList.contains(sessionId)) {
            redisUtils.lpush(Constants.REDIS_KEY_USER_SESSION + userId, sessionId, Constants.REDIS_KEY_TOKEN_EXPIRES);
        }
    }

    //清空用户Session
    public void cleanUserSession(String userId) {
        redisUtils.delete(Constants.REDIS_KEY_USER_SESSION + userId);
    }

    public void saveSysSetting(SysSettingDto sysSettingDto) {
        redisUtils.set(Constants.REDIS_KEY_SYS_SETTING, sysSettingDto);
    }

    public SysSettingDto getSysSetting() {
        SysSettingDto sysSettingDto = (SysSettingDto) redisUtils.get(Constants.REDIS_KEY_SYS_SETTING);
        sysSettingDto = sysSettingDto == null ? new SysSettingDto() : sysSettingDto;
        return sysSettingDto;
    }
    public TokenUserInfoDto getTokenUserInfo(String token) {
        TokenUserInfoDto tokenUserInfoDto = (TokenUserInfoDto) redisUtils.get(Constants.REDIS_KEY_WS_TOKEN + token);
        return tokenUserInfoDto;
    }


}
