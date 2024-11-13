package com.kkk.entity.po;

import com.kkk.entity.enums.DateTimePatternEnum;
import com.kkk.utils.DateUtil;
import com.kkk.utils.StringTools;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


/**
 * app发布
 */
public class AppUpdate implements Serializable {


    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 版本号
     */
    private String version;

    /**
     * 更新描述
     */
    private String updateDesc;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 0:未发布 1:灰度发布 2:全网发布
     */
    private Integer status;

    /**
     * 灰度uid
     */
    private String grayscaleUid;

    /**
     * 文件类型0:本地文件 1:外链
     */
    private Integer fileType;

    /**
     * 外链地址
     */
    private String outerLink;

    private String[] updateDescArray;

    public String[] getUpdateDescArray() {
        if (!StringTools.isEmpty(updateDesc)) {
            return updateDesc.split("\\|");
        }
        return updateDescArray;
    }

    public void setUpdateDescArray(String[] updateDescArray) {
        this.updateDescArray = updateDescArray;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return this.version;
    }

    public void setUpdateDesc(String updateDesc) {
        this.updateDesc = updateDesc;
    }

    public String getUpdateDesc() {
        return this.updateDesc;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setGrayscaleUid(String grayscaleUid) {
        this.grayscaleUid = grayscaleUid;
    }

    public String getGrayscaleUid() {
        return this.grayscaleUid;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public Integer getFileType() {
        return this.fileType;
    }

    public void setOuterLink(String outerLink) {
        this.outerLink = outerLink;
    }

    public String getOuterLink() {
        return this.outerLink;
    }

    @Override
    public String toString() {
        return "自增ID:" + (id == null ? "空" : id) + "，版本号:" + (version == null ? "空" : version) + "，更新描述:" + (updateDesc == null ? "空" : updateDesc) + "，创建时间:" + (createTime == null ? "空" : DateUtil.format(createTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + "，0:未发布 1:灰度发布 2:全网发布:" + (status == null ? "空" : status) + "，灰度uid:" + (grayscaleUid == null ? "空" : grayscaleUid) + "，文件类型0:本地文件 1:外链:" + (fileType == null ? "空" : fileType) + "，外链地址:" + (outerLink == null ? "空" : outerLink);
    }
}
