package com.yjiang.base.modular.system.service;

import com.yjiang.base.modular.system.model.ArticleImage;
import com.yjiang.base.modular.system.transfer.ArticleImageDto;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author stylefeng
 * @since 2018-11-12
 */
public interface IArticleImageService extends IService<ArticleImage> {
    public void insertImages(Long articleId, String content);

    public List<ArticleImageDto> listByArticleId(Long articleId);
}
