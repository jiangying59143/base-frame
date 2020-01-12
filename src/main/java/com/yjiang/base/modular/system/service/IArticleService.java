package com.yjiang.base.modular.system.service;

import com.yjiang.base.modular.system.model.Article;
import com.yjiang.base.modular.system.transfer.ArticleDto;
import com.baomidou.mybatisplus.service.IService;

import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-28
 */
public interface IArticleService extends IService<Article> {

    void add(ArticleDto article) throws IOException;

    void update(ArticleDto article) throws IOException;

    void bannerSet(ArticleDto article);

    void bannerCancel(ArticleDto articleDto);

    ArticleDto get(Long id);

    void deleteById(Long id);
}
