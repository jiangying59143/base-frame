package com.yjiang.base.modular.system.transfer;

import java.io.Serializable;
import java.util.List;

public class ArticleDto implements Serializable{

    //内容+图片
    public static final short ARTICLE_TYPE_IMAGE_ARTICLE = 1;

    //视频 返回视频url
    public static final short ARTICLE_TYPE_VEDIO = 2;

    //普通 返回文章+图片URL List
    public static final short ARTICLE_TYPE_HTML= 3;

    public static final short ARTICLE_TYPE_IMAG_CONTENT_LIST= 4;

    private String[] multiselect_to;

    private Long id;
    private Integer articleType;

    private Boolean bannerFlag;
    private Boolean privilege = true;
    private String title;

    private String content;

    private Long categoryId;

    private Long imageId;

    private String[] files;

    private String[] urls;

    private String[] fileComments;

    private List<ArticleDto4> articleDto4s;

    private String fileName;

    private String url;

    private Double latitude;

    private Double longitude;

    private String cityDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getArticleType() {
        return articleType;
    }

    public void setArticleType(Integer articleType) {
        this.articleType = articleType;
    }

    public Boolean getBannerFlag() {
        return bannerFlag;
    }

    public void setBannerFlag(Boolean bannerFlag) {
        this.bannerFlag = bannerFlag;
    }

    public Boolean getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Boolean privilege) {
        this.privilege = privilege;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String[] getMultiselect_to() {
        return multiselect_to;
    }

    public void setMultiselect_to(String[] multiselect_to) {
        this.multiselect_to = multiselect_to;
    }

    public String[] getFiles() {
        return files;
    }

    public void setFiles(String[] files) {
        this.files = files;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    public String[] getFileComments() {
        return fileComments;
    }

    public void setFileComments(String[] fileComments) {
        this.fileComments = fileComments;
    }

    public List<ArticleDto4> getArticleDto4s() {
        return articleDto4s;
    }

    public void setArticleDto4s(List<ArticleDto4> articleDto4s) {
        this.articleDto4s = articleDto4s;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCityDesc() {
        return cityDesc;
    }

    public void setCityDesc(String cityDesc) {
        this.cityDesc = cityDesc;
    }
}
