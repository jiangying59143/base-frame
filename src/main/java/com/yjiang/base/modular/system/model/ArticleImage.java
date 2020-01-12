package com.yjiang.base.modular.system.model;

import com.baomidou.mybatisplus.enums.IdType;
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
@TableName("article_image")
public class ArticleImage extends Model<ArticleImage> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("file_name")
    private String fileName;
    @TableField("order_count")
    private Integer orderCount;
    @TableField("article_id")
    private Long articleId;
    @TableField("banner_flag")
    private Boolean bannerFlag;
    @TableField("bucket_name")
    private String bucketName;

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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ArticleImage{" +
        ", id=" + id +
        ", fileName=" + fileName +
        ", orderCount=" + orderCount +
        ", articleId=" + articleId +
        ", bannerFlag=" + bannerFlag +
        ", bucketName=" + bucketName +
        "}";
    }
}
