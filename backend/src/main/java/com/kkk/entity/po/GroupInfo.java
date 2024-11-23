package com.kkk.entity.po;

import com.kkk.entity.enums.DateTimePatternEnum;
import com.kkk.utils.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 *
 */
public class GroupInfo implements Serializable {


    /**
     * 群ID
     */
    private String groupId;

    /**
     * 群组名
     */
    private String groupName;

    /**
     * 群主id
     */
    private String groupOwnerId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 群公告
     */
    private String groupNotice;

    /**
     * 0:直接加入 1:管理员同意后加入
     */
    private Integer joinType;

    /**
     * 状态 1:正常 0:解散
     */
    private Integer status;

    private Integer memberCount;

    private String groupOwnerNickName;


    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupOwnerId(String groupOwnerId) {
        this.groupOwnerId = groupOwnerId;
    }

    public String getGroupOwnerId() {
        return this.groupOwnerId;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setGroupNotice(String groupNotice) {
        this.groupNotice = groupNotice;
    }

    public String getGroupNotice() {
        return this.groupNotice;
    }

    public void setJoinType(Integer joinType) {
        this.joinType = joinType;
    }

    public Integer getJoinType() {
        return this.joinType;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public String getGroupOwnerNickName() {
        return groupOwnerNickName;
    }

    public void setGroupOwnerNickName(String groupOwnerNickName) {
        this.groupOwnerNickName = groupOwnerNickName;
    }

    @Override
    public String toString() {
        return "群ID:" + (groupId == null ? "空" : groupId) + "，群组名:" + (groupName == null ? "空" : groupName) + "，群主id:" + (groupOwnerId == null ? "空" : groupOwnerId) + "，创建时间:" + (createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "，群公告:" + (groupNotice == null ? "空" : groupNotice) + "，0:直接加入 1:管理员同意后加入:" + (joinType == null ? "空" : joinType) + "，状态 1:正常 0:解散:" + (status == null ? "空" : status);
    }
}
