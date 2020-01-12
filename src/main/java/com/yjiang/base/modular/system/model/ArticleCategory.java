package com.yjiang.base.modular.system.model;

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
@TableName("article_category")
public class ArticleCategory extends Model<ArticleCategory> {

    private static final long serialVersionUID = 1L;

    @TableField("article_id")
    private Long articleId;
    @TableField("category_id")
    private Long categoryId;


    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "ArticleCategory{" +
        ", articleId=" + articleId +
        ", categoryId=" + categoryId +
        "}";
    }
}
