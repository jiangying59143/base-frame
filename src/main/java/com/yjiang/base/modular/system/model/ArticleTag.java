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
@TableName("article_tag")
public class ArticleTag extends Model<ArticleTag> {

    private static final long serialVersionUID = 1L;

    @TableField("article_id")
    private Long articleId;
    @TableField("tag_id")
    private Long tagId;


    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "ArticleTag{" +
        ", articleId=" + articleId +
        ", tagId=" + tagId +
        "}";
    }
}
