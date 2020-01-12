package com.yjiang.base.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-28
 */
@TableName("article")
public class Article extends Model<Article> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("create_by")
    private Long createBy;
    @TableField("create_date")
    private Date createDate;
    @TableField("update_by")
    private Long updateBy;
    @TableField("update_date")
    private Date updateDate;
    private Integer version;
    @TableField("article_type")
    private Integer articleType;

    @TableField("banner_flag")
    private Boolean bannerFlag;
    private Boolean privilege;
    private String title;
    private Integer weight;
    @TableField("article_body1_id")
    private Long articleBody1Id;
    @TableField("article_body3_id")
    private Long articleBody3Id;
    @TableField("comments_count_id")
    private Long commentsCountId;
    @TableField("thumbs_down_count_id")
    private Long thumbsDownCountId;
    @TableField("thumbs_up_count_id")
    private Long thumbsUpCountId;
    @TableField("article_video_body_id")
    private Long articleVideoBodyId;
    @TableField("view_count_id")
    private Long viewCountId;
    @TableField("author_user_id")
    private Long authorUserId;
    @TableField("location_id")
    private Long locationId;
    @TableField("admin_flag")
    private Boolean adminFlag;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Long getArticleBody1Id() {
        return articleBody1Id;
    }

    public void setArticleBody1Id(Long articleBody1Id) {
        this.articleBody1Id = articleBody1Id;
    }

    public Long getArticleBody3Id() {
        return articleBody3Id;
    }

    public void setArticleBody3Id(Long articleBody3Id) {
        this.articleBody3Id = articleBody3Id;
    }

    public Long getCommentsCountId() {
        return commentsCountId;
    }

    public void setCommentsCountId(Long commentsCountId) {
        this.commentsCountId = commentsCountId;
    }

    public Long getThumbsDownCountId() {
        return thumbsDownCountId;
    }

    public void setThumbsDownCountId(Long thumbsDownCountId) {
        this.thumbsDownCountId = thumbsDownCountId;
    }

    public Long getThumbsUpCountId() {
        return thumbsUpCountId;
    }

    public void setThumbsUpCountId(Long thumbsUpCountId) {
        this.thumbsUpCountId = thumbsUpCountId;
    }

    public Long getArticleVideoBodyId() {
        return articleVideoBodyId;
    }

    public void setArticleVideoBodyId(Long articleVideoBodyId) {
        this.articleVideoBodyId = articleVideoBodyId;
    }

    public Long getViewCountId() {
        return viewCountId;
    }

    public void setViewCountId(Long viewCountId) {
        this.viewCountId = viewCountId;
    }

    public Long getAuthorUserId() {
        return authorUserId;
    }

    public void setAuthorUserId(Long authorUserId) {
        this.authorUserId = authorUserId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Boolean getAdminFlag() {
        return adminFlag;
    }

    public void setAdminFlag(Boolean adminFlag) {
        this.adminFlag = adminFlag;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Article{" +
        ", id=" + id +
        ", createBy=" + createBy +
        ", createDate=" + createDate +
        ", updateBy=" + updateBy +
        ", updateDate=" + updateDate +
        ", version=" + version +
        ", articleType=" + articleType +
        ", bannerFlag=" + bannerFlag +
        ", privilege=" + privilege +
        ", title=" + title +
        ", weight=" + weight +
        ", articleBody1Id=" + articleBody1Id +
        ", articleBody3Id=" + articleBody3Id +
        ", commentsCountId=" + commentsCountId +
        ", thumbsDownCountId=" + thumbsDownCountId +
        ", thumbsUpCountId=" + thumbsUpCountId +
        ", articleVideoBodyId=" + articleVideoBodyId +
        ", viewCountId=" + viewCountId +
        ", authorUserId=" + authorUserId +
        ", locationId=" + locationId +
        "}";
    }
}
