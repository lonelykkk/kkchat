package com.kkk.entity.enums;


import com.kkk.utils.StringTools;

public enum UserContactTypeEnum {
    USER(0, "U", "好友"),
    GROUP(1, "G", "群");

    private Integer type;
    private String prefix;
    private String desc;

    UserContactTypeEnum(Integer type, String prefix, String desc) {
        this.type = type;
        this.prefix = prefix;
        this.desc = desc;
    }

    public String getPrefix() {
        return prefix;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static UserContactTypeEnum getByName(String name) {
        try {
            if (StringTools.isEmpty(name)) {
                return null;
            }
            return UserContactTypeEnum.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static UserContactTypeEnum getByPrefix(String prefix) {
        if (StringTools.isEmpty(prefix) || prefix.trim().length() == 0) {
            return null;
        }
        prefix = prefix.substring(0, 1);
        for (UserContactTypeEnum typeEnum : UserContactTypeEnum.values()) {
            if (typeEnum.getPrefix().equals(prefix)) {
                return typeEnum;
            }
        }
        return null;
    }
}
