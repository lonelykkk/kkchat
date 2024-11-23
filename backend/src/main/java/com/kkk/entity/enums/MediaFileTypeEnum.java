package com.kkk.entity.enums;


public enum MediaFileTypeEnum {
    IMAGE(0, "图片"),
    video(1, "视频"),
    FILE(2, "文件");

    private Integer type;
    private String desc;

    MediaFileTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static MediaFileTypeEnum getByType(Integer type) {
        for (MediaFileTypeEnum item : MediaFileTypeEnum.values()) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }
}
