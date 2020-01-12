package com.yjiang.base.modular.system.transfer;

import com.yjiang.base.core.util.AliyunOSSClientUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class ArticleImageDto implements Serializable {

    private Long id;

    private String fileName;

    private Integer orderCount;

    private Long articleId;

    private Boolean bannerFlag;

    private String bucketName;

    private String path;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Boolean getBannerFlag() {
        return bannerFlag;
    }

    public void setBannerFlag(Boolean bannerFlag) {
        this.bannerFlag = bannerFlag;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getPath() {
        if(StringUtils.isNotBlank(bucketName) && StringUtils.isNotBlank(fileName)) {
            path = AliyunOSSClientUtil.getPath(bucketName, fileName);
        }
        if(StringUtils.isBlank(bucketName) && StringUtils.isNotBlank(fileName)){
            path = fileName;
        }
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
