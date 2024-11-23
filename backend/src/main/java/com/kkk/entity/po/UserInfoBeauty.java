package com.kkk.entity.po;

import java.io.Serializable;


/**
 * 靓号
 */
public class UserInfoBeauty implements Serializable {


    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 状态0:未使用 1:已使用
     */
    private Integer status;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return "自增ID:" + (id == null ? "空" : id) + "，邮箱:" + (email == null ? "空" : email) + "，用户ID:" + (userId == null ? "空" : userId) + "，状态0:未使用 1:已使用:" + (status == null ? "空" : status);
    }
}
