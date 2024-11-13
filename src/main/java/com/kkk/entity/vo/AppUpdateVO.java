package com.kkk.entity.vo;

import java.io.Serializable;
import java.util.List;


/**
 * app发布
 */
public class AppUpdateVO implements Serializable {

    private static final long serialVersionUID = 4756060542150096340L;
    private Integer id;

    /**
     * 版本号
     */
    private String version;

    /**
     * 更新描述
     */
    private List<String> updateList;

    private Long size;

    private String fileName;

    private Integer fileType;

    private String outerLink;

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public String getOuterLink() {
        return outerLink;
    }

    public void setOuterLink(String outerLink) {
        this.outerLink = outerLink;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getUpdateList() {
        return updateList;
    }

    public void setUpdateList(List<String> updateList) {
        this.updateList = updateList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
