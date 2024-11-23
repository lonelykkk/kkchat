package com.kkk.entity.enums;


import com.kkk.utils.StringTools;

public enum JoinTypeEnum {
    JOIN(0, "直接加入"),
    APPLY(1, "需要审核");

    private Integer type;
    private String desc;

    JoinTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }


    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static JoinTypeEnum getByName(String name) {
        try {
            if (StringTools.isEmpty(name)) {
                return null;
            }
            return JoinTypeEnum.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static JoinTypeEnum getByType(Integer joinType) {
        for (JoinTypeEnum jonTypeEnum : JoinTypeEnum.values()) {
            if (jonTypeEnum.getType().equals(joinType)) {
                return jonTypeEnum;
            }
        }
        return null;
    }
}
