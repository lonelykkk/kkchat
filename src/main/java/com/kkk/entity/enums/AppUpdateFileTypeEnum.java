package com.kkk.entity.enums;

public enum AppUpdateFileTypeEnum {
    LOCAL(0, "本地"), OUTER_LINK(1, "外链");

    private Integer type;
    private String description;

    AppUpdateFileTypeEnum(int type, String description) {
        this.type = type;
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static AppUpdateFileTypeEnum getByType(Integer type) {
        for (AppUpdateFileTypeEnum at : AppUpdateFileTypeEnum.values()) {
            if (at.type.equals(type)) {
                return at;
            }
        }
        return null;
    }
}
