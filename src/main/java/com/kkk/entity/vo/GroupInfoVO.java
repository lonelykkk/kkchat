package com.kkk.entity.vo;

import com.kkk.entity.po.GroupInfo;
import com.kkk.entity.po.UserContact;

import java.util.List;

/**
 * @ClassName GroupInfoVO
 * @Description TODO
 * @Author 程序员老罗 https://space.bilibili.com/499388891
 * @Date 2024/2/6 14:01
 */
public class GroupInfoVO {
    private GroupInfo groupInfo;
    private List<UserContact> userContactList;

    public List<UserContact> getUserContactList() {
        return userContactList;
    }

    public void setUserContactList(List<UserContact> userContactList) {
        this.userContactList = userContactList;
    }

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }
}
