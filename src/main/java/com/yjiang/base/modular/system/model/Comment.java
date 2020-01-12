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
 * @since 2018-11-12
 */
@TableName("comment")
public class Comment extends Model<Comment> {

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
    private String content;
    private String level;
    @TableField("article_id")
    private Long articleId;
    @TableField("author_user_id")
    private Long authorUserId;
    @TableField("parent_id")
    private Long parentId;
    @TableField("to_uid")
    private Long toUid;
    @TableField("thumbs_up_count_id")
    private Long thumbsUpCountId;
    @TableField("location_id")
    private Long locationId;
    @TableField("original_parent_id")
    private Long originalParentId;
    @TableField("comments_count_id")
    private Long commentsCountId;
    @TableField("scan_flag")
    private Boolean scanFlag;


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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getAuthorUserId() {
        return authorUserId;
    }

    public void setAuthorUserId(Long authorUserId) {
        this.authorUserId = authorUserId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getToUid() {
        return toUid;
    }

    public void setToUid(Long toUid) {
        this.toUid = toUid;
    }

    public Long getThumbsUpCountId() {
        return thumbsUpCountId;
    }

    public void setThumbsUpCountId(Long thumbsUpCountId) {
        this.thumbsUpCountId = thumbsUpCountId;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public Long getOriginalParentId() {
        return originalParentId;
    }

    public void setOriginalParentId(Long originalParentId) {
        this.originalParentId = originalParentId;
    }

    public Long getCommentsCountId() {
        return commentsCountId;
    }

    public void setCommentsCountId(Long commentsCountId) {
        this.commentsCountId = commentsCountId;
    }

    public Boolean getScanFlag() {
        return scanFlag;
    }

    public void setScanFlag(Boolean scanFlag) {
        this.scanFlag = scanFlag;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Comment{" +
        ", id=" + id +
        ", createBy=" + createBy +
        ", createDate=" + createDate +
        ", updateBy=" + updateBy +
        ", updateDate=" + updateDate +
        ", version=" + version +
        ", content=" + content +
        ", level=" + level +
        ", articleId=" + articleId +
        ", authorUserId=" + authorUserId +
        ", parentId=" + parentId +
        ", toUid=" + toUid +
        ", thumbsUpCountId=" + thumbsUpCountId +
        ", locationId=" + locationId +
        ", originalParentId=" + originalParentId +
        ", commentsCountId=" + commentsCountId +
        ", scanFlag=" + scanFlag +
        "}";
    }
}
