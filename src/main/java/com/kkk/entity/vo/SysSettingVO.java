package com.kkk.entity.vo;

import java.io.Serializable;

public class SysSettingVO implements Serializable {
    private Integer maxImageSize = 2;
    private Integer maxVideoSize = 5;
    private Integer maxFileSize = 5;

    public Integer getMaxImageSize() {
        return maxImageSize;
    }

    public void setMaxImageSize(Integer maxImageSize) {
        this.maxImageSize = maxImageSize;
    }

    public Integer getMaxVideoSize() {
        return maxVideoSize;
    }

    public void setMaxVideoSize(Integer maxVideoSize) {
        this.maxVideoSize = maxVideoSize;
    }

    public Integer getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(Integer maxFileSize) {
        this.maxFileSize = maxFileSize;
    }
}
