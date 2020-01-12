package com.yjiang.base.modular.system.service.impl;

import com.yjiang.base.core.util.AliyunOSSClientUtil;
import com.yjiang.base.core.util.HtmlRegexpUtil;
import com.yjiang.base.modular.system.dao.ArticleImageMapper;
import com.yjiang.base.modular.system.model.ArticleImage;
import com.yjiang.base.modular.system.service.IArticleImageService;
import com.yjiang.base.modular.system.transfer.ArticleImageDto;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-11-12
 */
@Service
public class ArticleImageServiceImpl extends ServiceImpl<ArticleImageMapper, ArticleImage> implements IArticleImageService {

    @Override
    @Transient
    public void insertImages(Long articleId, String content) {
        List<String> images = HtmlRegexpUtil.filterHtmlImageSrc(content);
        for (int i = 0; i < images.size(); i++) {
            String imageName = images.get(i);
            if(StringUtils.isNotBlank(imageName)) {
                ArticleImage articleImage = new ArticleImage();
                articleImage.setArticleId(articleId);
                articleImage.setOrderCount(i);
                articleImage.setBannerFlag(false);
                if(!imageName.startsWith("http"))articleImage.setBucketName(AliyunOSSClientUtil.HTML_IMAGE_BUCKET_NAME);
                articleImage.setFileName(imageName);
                this.insert(articleImage);
            }
        }
    }

    @Override
    public List<ArticleImageDto> listByArticleId(Long articleId){
        Wrapper<ArticleImage> articleImageWrapper = new EntityWrapper<ArticleImage>();
        articleImageWrapper.eq("article_id", articleId);
        List<ArticleImage> articleImages = this.selectList(articleImageWrapper);
        List<ArticleImageDto> articleImageDtos = articleImages.stream().map(articleImage -> {
            ArticleImageDto articleImageDto = new ArticleImageDto();
            articleImageDto.setId(articleImage.getId());
            articleImageDto.setBucketName(articleImage.getBucketName());
            articleImageDto.setFileName(articleImage.getFileName());
            articleImageDto.setBannerFlag(articleImage.getBannerFlag());
            return articleImageDto;
        }).collect(Collectors.toList());
        return articleImageDtos;
    }
}
