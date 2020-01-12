package com.yjiang.base.modular.system.transfer;

import com.yjiang.base.core.util.AliyunOSSClientUtil;
import com.yjiang.base.modular.system.model.ArticleBody4;
import com.yjiang.base.modular.system.model.ArticleImage;

import java.io.Serializable;

public class ArticleDto4 implements Serializable {
    private ArticleBody4 articleBody4;

    private ArticleImage articleImage;

    private String url;

    public ArticleBody4 getArticleBody4() {
        return articleBody4;
    }

    public void setArticleBody4(ArticleBody4 articleBody4) {
        this.articleBody4 = articleBody4;
    }

    public ArticleImage getArticleImage() {
        return articleImage;
    }

    public void setArticleImage(ArticleImage articleImage) {
        this.articleImage = articleImage;
    }

    public String getUrl() {
        if(articleImage != null){
            return AliyunOSSClientUtil.getPath(articleImage.getBucketName(), articleImage.getFileName());
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
