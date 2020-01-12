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
@TableName("article_body4")
public class ArticleBody4 extends Model<ArticleBody4> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String content;
    @TableField("order_count")
    private Integer orderCount;
    @TableField("article_id")
    private Long articleId;
    @TableField("article_image_id")
    private Long articleImageId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Long getArticleImageId() {
        return articleImageId;
    }

    public void setArticleImageId(Long articleImageId) {
        this.articleImageId = articleImageId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ArticleBody4{" +
        ", id=" + id +
        ", content=" + content +
        ", orderCount=" + orderCount +
        ", articleId=" + articleId +
        ", articleImageId=" + articleImageId +
        "}";
    }
}
