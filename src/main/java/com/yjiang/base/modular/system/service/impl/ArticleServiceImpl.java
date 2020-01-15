package com.yjiang.base.modular.system.service.impl;

import com.yjiang.base.config.properties.GunsProperties;
import com.yjiang.base.core.shiro.ShiroKit;
import com.yjiang.base.core.util.AliyunOSSClientUtil;
import com.yjiang.base.modular.system.dao.ArticleMapper;
import com.yjiang.base.modular.system.transfer.ArticleDto;
import com.yjiang.base.modular.system.transfer.ArticleDto4;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.yjiang.base.modular.system.model.*;
import com.yjiang.base.modular.system.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2018-10-28
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Autowired
    private IArticleBlackListService articleBlackListService;

    @Autowired
    private IArticleBody1Service articleBody1Service;

    @Autowired
    private IArticleVideoBodyService articleVideoBodyService;

    @Autowired
    private IArticleBody3Service articleBody3Service;

    @Autowired
    private IArticleBody4Service articleBody4Service;

    @Autowired
    private IArticleCategoryService articleCategoryService;

    @Autowired
    private IArticleFavoriteService articleFavoriteService;

    @Autowired
    private IArticleImageService articleImageService;

    @Autowired
    private IArticleShareHistoryService articleShareHistoryService;

    @Autowired
    private IArticleTagService articleTagService;

    @Autowired
    private IArticleThumbsDownCountService articleThumbsDownCountService;

    @Autowired
    private IArticleThumbsDownService articleThumbsDownService;

    @Autowired
    private IArticleThumbsUpCountService articleThumbsUpCountService;

    @Autowired
    private IArticleThumbsUpService articleThumbsUpService;

    @Autowired
    private IArticleViewCountService articleViewCountService;

    @Autowired
    private ICommentCountService commentCountService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private ICommentThumbsUpCountService commentThumbsUpCountService;

    @Autowired
    private ICommentThumbsUpService commentThumbsUpService;

    @Autowired
    private IUserScanHistoryService userScanHistoryService;

    @Autowired
    private ILocationService locationService;

    @Autowired
    private GunsProperties gunsProperties;

    @Override
    @Transactional
    public void add(ArticleDto articleDto) throws IOException {

        ArticleViewCount articleViewCount = new ArticleViewCount();
        articleViewCount.setViewCount(0L);
        articleViewCountService.insert(articleViewCount);

        ArticleThumbsDownCount articleThumbsDownCount = new ArticleThumbsDownCount();
        articleThumbsDownCount.setThumbsDownCount(0L);
        articleThumbsDownCountService.insert(articleThumbsDownCount);

        ArticleThumbsUpCount articleThumbsUpCount = new ArticleThumbsUpCount();
        articleThumbsUpCount.setThumbsUpCount(0L);
        articleThumbsUpCountService.insert(articleThumbsUpCount);

        CommentCount commentCount = new CommentCount();
        commentCount.setCommentCount(0L);
        commentCountService.insert(commentCount);

        ArticleVideoBody articleVideoBody = new ArticleVideoBody();
        if (articleDto.getArticleType() == ArticleDto.ARTICLE_TYPE_VEDIO){
            articleVideoBody.setFileName(articleDto.getContent());
            articleVideoBodyService.insert(articleVideoBody);
        }

        ArticleBody3 articleBody3 = new ArticleBody3();
        if(articleDto.getArticleType() == ArticleDto.ARTICLE_TYPE_HTML) {
            articleBody3.setContent(articleDto.getContent());
            articleBody3Service.insert(articleBody3);
        }

        Location location = new Location();
        if(articleDto.getLatitude() != null && articleDto.getLongitude() != null && Strings.isNotBlank(articleDto.getCityDesc())){
            location.setLocation(articleDto.getCityDesc());
            location.setLatitude(articleDto.getLatitude());
            location.setLongitude(articleDto.getLongitude());
            location.setCreateBy(ShiroKit.getUser().getId());
            location.setUpdateBy(ShiroKit.getUser().getId());
            location.setCreateDate(new Date());
            location.setUpdateDate(new Date());
            locationService.insert(location);
        }

        Article article1 = new Article();
        article1.setTitle(articleDto.getTitle());
        article1.setArticleType(articleDto.getArticleType());
        article1.setPrivilege(articleDto.getPrivilege());
        article1.setBannerFlag(false);
        article1.setAdminFlag(true);
        article1.setWeight(2);
        article1.setAuthorUserId(ShiroKit.getUser().getId());
        article1.setCreateDate(new Date());
        article1.setCreateBy(ShiroKit.getUser().getId());

        article1.setViewCountId(articleViewCount.getId());
        article1.setThumbsDownCountId(articleThumbsDownCount.getId());
        article1.setThumbsUpCountId(articleThumbsUpCount.getId());
        article1.setCommentsCountId(commentCount.getId());
        if(articleDto.getArticleType()  == ArticleDto.ARTICLE_TYPE_VEDIO) {
            article1.setArticleVideoBodyId(articleVideoBody.getId());
        }
        if(articleDto.getArticleType()  == ArticleDto.ARTICLE_TYPE_HTML) {
            article1.setArticleBody3Id(articleBody3.getId());
        }
        article1.setLocationId(location.getId());
        this.insert(article1);

        if(articleDto.getArticleType()  == ArticleDto.ARTICLE_TYPE_VEDIO) {
            ArticleImage articleImage = new ArticleImage();
            articleImage.setFileName(articleDto.getFileName());
            articleImage.setBucketName(AliyunOSSClientUtil.IMAGE_BUCKET_NAME);
            articleImage.setArticleId(article1.getId());
            articleImage.setOrderCount(0);
            articleImage.setBannerFlag(false);
            articleImageService.insert(articleImage);
            String fileSavePath = gunsProperties.getFileUploadPath();
            File file = new File(fileSavePath + articleDto.getFileName());
            AliyunOSSClientUtil.upload(AliyunOSSClientUtil.IMAGE_BUCKET_NAME, articleDto.getFileName(), file);
        }

        if(articleDto.getArticleType()  == ArticleDto.ARTICLE_TYPE_HTML) {
            articleImageService.insertImages(article1.getId(), articleDto.getContent());
        }

        if(articleDto.getArticleType()  == ArticleDto.ARTICLE_TYPE_IMAG_CONTENT_LIST) {
            if(articleDto.getFiles() != null && articleDto.getFileComments()!= null && articleDto.getFiles().length == articleDto.getFileComments().length){
                List<String> images = Arrays.asList(articleDto.getFiles());
                for (int i = 0; i < images.size(); i++) {
                    String imageName = images.get(i);
                    if(StringUtils.isNotBlank(imageName)) {

                        String fileSavePath = gunsProperties.getFileUploadPath();
                        File file = new File(fileSavePath + imageName);
                        if(file.exists()) {
                            AliyunOSSClientUtil.upload(AliyunOSSClientUtil.IMAGE_BUCKET_NAME, imageName, file);
                            file.delete();

                            ArticleImage articleImage = new ArticleImage();
                            articleImage.setArticleId(article1.getId());
                            articleImage.setOrderCount(i);
                            articleImage.setBannerFlag(false);
                            if (!imageName.startsWith("http"))
                                articleImage.setBucketName(AliyunOSSClientUtil.IMAGE_BUCKET_NAME);
                            articleImage.setFileName(imageName);
                            articleImageService.insert(articleImage);

                            ArticleBody4 articleBody4 = new ArticleBody4();
                            articleBody4.setArticleImageId(articleImage.getId());
                            articleBody4.setArticleId(article1.getId());
                            articleBody4.setContent(articleDto.getFileComments()[i]);
                            articleBody4.setOrderCount(i);
                            articleBody4Service.insert(articleBody4);
                        }
                    }
                }
            }
        }

        ArticleCategory articleCategory = new ArticleCategory();
        articleCategory.setArticleId(article1.getId());
        articleCategory.setCategoryId(articleDto.getCategoryId());
        articleCategoryService.insert(articleCategory);
    }

    @Override
    @Transactional
    public void update(ArticleDto articleDto) throws IOException {
        Article article = this.selectById(articleDto.getId());

        Map<String, Object> map = new HashMap<>();
        map.put("article_id", articleDto.getId());

        if (articleDto.getArticleType() == ArticleDto.ARTICLE_TYPE_VEDIO){
            //更新内容
            ArticleVideoBody articleVideoBody = articleVideoBodyService.selectById(article.getArticleVideoBodyId());
            articleVideoBody.setFileName(articleDto.getContent());
            articleVideoBodyService.updateById(articleVideoBody);

            List<ArticleImage> aritcleImages = articleImageService.selectByMap(map);
            if(!CollectionUtils.isEmpty(aritcleImages)){
                ArticleImage articleImage = aritcleImages.get(0);
                if(!articleImage.getFileName().contains(articleDto.getFileName())){
                    AliyunOSSClientUtil.delete(articleImage.getBucketName(), articleImage.getFileName());
                    articleImage.setFileName(articleDto.getFileName());
                    articleImageService.updateById(articleImage);
                    String fileSavePath = gunsProperties.getFileUploadPath();
                    File file = new File(fileSavePath + articleDto.getFileName());
                    AliyunOSSClientUtil.upload(AliyunOSSClientUtil.IMAGE_BUCKET_NAME, articleDto.getFileName(), file);
                }
            }
        }else if (articleDto.getArticleType() == ArticleDto.ARTICLE_TYPE_HTML){
            //更新内容
            ArticleBody3 articleBody3 = articleBody3Service.selectById(article.getArticleBody3Id());
            articleBody3.setContent(articleDto.getContent());
            articleBody3Service.updateById(articleBody3);

            //更新图片（先删除后插入）

            Wrapper<ArticleImage> articleImageWrapper = new EntityWrapper<ArticleImage>();
            articleImageWrapper.allEq(map);
            articleImageService.delete(articleImageWrapper);
            articleImageService.insertImages(articleDto.getId(), articleDto.getContent());
        }else if(articleDto.getArticleType() == ArticleDto.ARTICLE_TYPE_IMAG_CONTENT_LIST){
            if(articleDto.getFiles() != null && articleDto.getFileComments()!= null && articleDto.getFiles().length == articleDto.getFileComments().length){
                List<String> images = Arrays.asList(articleDto.getFiles());
                List<ArticleBody4> articleBody4s = articleBody4Service.selectByMap(map);
                List<Long> articleImageIds = articleBody4s.stream().map(ab->ab.getArticleImageId()).collect(Collectors.toList());
                List<ArticleImage> articleImages = articleImageService.selectBatchIds(articleImageIds);
                for (ArticleImage articleImage : articleImages) {
                    if(!images.contains(articleImage.getFileName())){
                        AliyunOSSClientUtil.delete(articleImage.getBucketName(), articleImage.getFileName());
                    }
                }
                List<String> articleImageNames = articleImages.stream().map(ab->ab.getFileName()).collect(Collectors.toList());
                articleBody4Service.deleteByMap(map);
                articleImageService.deleteBatchIds(articleImageIds);

                for (int i = 0; i < images.size(); i++) {
                    String imageName = images.get(i);
                    if(StringUtils.isNotBlank(imageName)) {
                        String fileSavePath = gunsProperties.getFileUploadPath();
                        File file = new File(fileSavePath + imageName);
                        if(file.exists()) {
                            AliyunOSSClientUtil.upload(AliyunOSSClientUtil.IMAGE_BUCKET_NAME, imageName, file);
                            file.delete();

                            ArticleImage articleImage = new ArticleImage();
                            articleImage.setArticleId(article.getId());
                            articleImage.setOrderCount(i);
                            articleImage.setBannerFlag(false);
                            if (!imageName.startsWith("http"))
                                articleImage.setBucketName(AliyunOSSClientUtil.IMAGE_BUCKET_NAME);
                            articleImage.setFileName(imageName);
                            articleImageService.insert(articleImage);

                            ArticleBody4 articleBody4 = new ArticleBody4();
                            articleBody4.setArticleImageId(articleImage.getId());
                            articleBody4.setArticleId(article.getId());
                            articleBody4.setContent(articleDto.getFileComments()[i]);
                            articleBody4.setOrderCount(i);
                            articleBody4Service.insert(articleBody4);
                        }else{
                            ArticleImage articleImage = new ArticleImage();
                            articleImage.setArticleId(article.getId());
                            articleImage.setOrderCount(i);
                            articleImage.setBannerFlag(false);
                            if (!imageName.startsWith("http"))
                                articleImage.setBucketName(AliyunOSSClientUtil.IMAGE_BUCKET_NAME);
                            articleImage.setFileName(imageName);
                            articleImageService.insert(articleImage);

                            ArticleBody4 articleBody4 = new ArticleBody4();
                            articleBody4.setArticleImageId(articleImage.getId());
                            articleBody4.setArticleId(article.getId());
                            articleBody4.setContent(articleDto.getFileComments()[i]);
                            articleBody4.setOrderCount(i);
                            articleBody4Service.insert(articleBody4);
                        }
                    }
                }
            }
        }

        if(article.getViewCountId() == null) {
            ArticleViewCount articleViewCount = new ArticleViewCount();
            articleViewCount.setViewCount(0L);
            articleViewCountService.insert(articleViewCount);
            article.setViewCountId(articleViewCount.getId());
        }

        if(article.getThumbsDownCountId() == null) {
            ArticleThumbsDownCount articleThumbsDownCount = new ArticleThumbsDownCount();
            articleThumbsDownCount.setThumbsDownCount(0L);
            articleThumbsDownCountService.insert(articleThumbsDownCount);
            article.setThumbsDownCountId(articleThumbsDownCount.getId());
        }

        if(article.getThumbsUpCountId() == null) {
            ArticleThumbsUpCount articleThumbsUpCount = new ArticleThumbsUpCount();
            articleThumbsUpCount.setThumbsUpCount(0L);
            articleThumbsUpCountService.insert(articleThumbsUpCount);
            article.setThumbsUpCountId(articleThumbsUpCount.getId());
        }

        if(article.getCommentsCountId() == null) {
            CommentCount commentCount = new CommentCount();
            commentCount.setCommentCount(0L);
            commentCountService.insert(commentCount);
            article.setCommentsCountId(commentCount.getId());
        }

        if(article.getLocationId() == null){
            if(articleDto.getLatitude() != null && articleDto.getLongitude() != null && Strings.isNotBlank(articleDto.getCityDesc())){
                Location location = new Location();
                location.setLocation(articleDto.getCityDesc());
                location.setLatitude(articleDto.getLatitude());
                location.setLongitude(articleDto.getLongitude());
                location.setCreateBy(ShiroKit.getUser().getId());
                location.setUpdateBy(ShiroKit.getUser().getId());
                location.setCreateDate(new Date());
                location.setUpdateDate(new Date());
                locationService.insert(location);
                article.setLocationId(location.getId());
            }
        }else{
            if(articleDto.getLatitude() != null && articleDto.getLongitude() != null && Strings.isNotBlank(articleDto.getCityDesc())){
                Location location = locationService.selectById(article.getLocationId());
                location.setLocation(articleDto.getCityDesc());
                location.setLatitude(articleDto.getLatitude());
                location.setLongitude(articleDto.getLongitude());
                location.setUpdateBy(ShiroKit.getUser().getId());
                location.setUpdateDate(new Date());
                location.setVersion(location.getVersion()+1);
                locationService.updateById(location);
            }
        }

        Wrapper<ArticleCategory> articleCategoryEntityWrapper = new EntityWrapper<ArticleCategory>();
        articleCategoryEntityWrapper.allEq(map);
        ArticleCategory articleCategory = articleCategoryService.selectOne(articleCategoryEntityWrapper);
        if(articleCategory == null){
            ArticleCategory articleCategory1 = new ArticleCategory();
            articleCategory1.setArticleId(articleDto.getId());
            articleCategory1.setCategoryId(articleDto.getCategoryId());
            articleCategoryService.insert(articleCategory1);
        }else {
            articleCategory.setCategoryId(articleDto.getCategoryId());
            articleCategoryService.update(articleCategory, articleCategoryEntityWrapper);
        }

        //更新文章
        article.setTitle(articleDto.getTitle());
        article.setPrivilege(articleDto.getPrivilege());
        article.setUpdateBy(ShiroKit.getUser().getId());
        article.setUpdateDate(new Date());
        this.updateById(article);


    }

    @Override
    @Transactional
    public void bannerSet(ArticleDto articleDto) {
        Wrapper<ArticleImage> articleImageWrapper = new EntityWrapper<ArticleImage>();
        articleImageWrapper.eq("article_id", articleDto.getId());
        List<ArticleImage> articleImages = articleImageService.selectList(articleImageWrapper);
        Article article = this.selectById(articleDto.getId());
        if(!CollectionUtils.isEmpty(articleImages)) {
            for (ArticleImage articleImage : articleImages) {
                if(articleImage.getId().longValue() == articleDto.getImageId().longValue()){
                    articleImage.setBannerFlag(true);
                    articleImageService.updateById(articleImage);
                }else{
                    articleImage.setBannerFlag(false);
                    articleImageService.updateById(articleImage);
                }
            }
        }

        article.setBannerFlag(true);
        this.updateById(article);

    }

    @Override
    @Transactional
    public void bannerCancel(ArticleDto articleDto) {
        Wrapper<ArticleImage> articleImageWrapper = new EntityWrapper<ArticleImage>();
        articleImageWrapper.eq("article_id", articleDto.getId());
        List<ArticleImage> articleImages = articleImageService.selectList(articleImageWrapper);
        Article article = this.selectById(articleDto.getId());
        if(!CollectionUtils.isEmpty(articleImages)) {
            for (ArticleImage articleImage : articleImages) {
                articleImage.setBannerFlag(false);
                articleImageService.updateById(articleImage);
            }
        }

        article.setBannerFlag(false);
        this.updateById(article);

    }

    @Override
    public ArticleDto get(Long id) {
        Article article = this.selectById(id);
        ArticleDto articleDto = new ArticleDto();
        articleDto.setPrivilege(article.getPrivilege());
        articleDto.setTitle(article.getTitle());
        articleDto.setArticleType(article.getArticleType());
        articleDto.setId(id);

        Map<String, Object> map = new HashMap<>();
        map.put("article_id", articleDto.getId());

        if(ArticleDto.ARTICLE_TYPE_HTML == article.getArticleType()) {
            articleDto.setContent(articleBody3Service.selectById(article.getArticleBody3Id()).getContent());
        }

        if(ArticleDto.ARTICLE_TYPE_VEDIO == article.getArticleType()) {
            articleDto.setContent(articleVideoBodyService.selectById(article.getArticleVideoBodyId()).getFileName());
            List<ArticleImage> articleImages = articleImageService.selectByMap(map);
            if(!CollectionUtils.isEmpty(articleImages)) {
                articleDto.setFileName(articleImages.get(0).getFileName());
                articleDto.setUrl(AliyunOSSClientUtil.getPath(articleImages.get(0).getBucketName(), articleImages.get(0).getFileName()));
            }
        }

        Wrapper<ArticleCategory> articleCategoryEntityWrapper = new EntityWrapper<ArticleCategory>();
        articleCategoryEntityWrapper.allEq(map);
        ArticleCategory articleCategory = articleCategoryService.selectOne(articleCategoryEntityWrapper);
        if(articleCategory != null){
            articleDto.setCategoryId(articleCategory.getCategoryId());
        }

        if(ArticleDto.ARTICLE_TYPE_IMAG_CONTENT_LIST == article.getArticleType()) {
            Wrapper<ArticleBody4> articleBody4EntityWrapper = new EntityWrapper<ArticleBody4>();
            articleBody4EntityWrapper.eq("article_id", articleDto.getId());
            List<ArticleBody4> articleBody4List = articleBody4Service.selectList(articleBody4EntityWrapper);
            List<ArticleDto4> articleDto4List = new ArrayList<>();
            if (!CollectionUtils.isEmpty(articleBody4List)) {
                for (int i = 0; i < articleBody4List.size(); i++) {
                    ArticleDto4 articleDto4 = new ArticleDto4();
                    ArticleBody4 articleBody4 = articleBody4List.get(i);
                    ArticleImage articleImage = articleImageService.selectById(articleBody4.getArticleImageId());
                    articleDto4.setArticleImage(articleImage);
                    articleDto4.setArticleBody4(articleBody4);
                    articleDto4List.add(articleDto4);
                }
            }
            articleDto.setArticleDto4s(articleDto4List);
        }

        if(article.getLocationId() != null) {
            Location location = locationService.selectById(article.getLocationId());
            articleDto.setCityDesc(location.getLocation());
            articleDto.setLatitude(location.getLatitude());
            articleDto.setLongitude(location.getLongitude());
        }
        return articleDto;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        Map<String, Object> map = new HashMap<>();
        map.put("article_id", id);

        Wrapper<ArticleBody4> articleBody4EntityWrapper = new EntityWrapper<ArticleBody4>();
        articleBody4EntityWrapper.allEq(map);
        articleBody4Service.delete(articleBody4EntityWrapper);

        Wrapper<UserScanHistory> userScanHistoryEntityWrapper = new EntityWrapper<UserScanHistory>();
        userScanHistoryEntityWrapper.allEq(map);
        userScanHistoryService.delete(userScanHistoryEntityWrapper);

        Wrapper<ArticleBlackList> articleBlackListEntityWrapper = new EntityWrapper<ArticleBlackList>();
        articleBlackListEntityWrapper.allEq(map);
        articleBlackListService.delete(articleBlackListEntityWrapper);

        Wrapper<ArticleCategory> articleCategoryEntityWrapper = new EntityWrapper<ArticleCategory>();
        articleCategoryEntityWrapper.allEq(map);
        articleCategoryService.delete(articleCategoryEntityWrapper);

        Wrapper<ArticleFavorite> articleFavoriteEntityWrapper = new EntityWrapper<ArticleFavorite>();
        articleFavoriteEntityWrapper.allEq(map);
        articleFavoriteService.delete(articleFavoriteEntityWrapper);

        Wrapper<ArticleImage> articleImageWrapper = new EntityWrapper<ArticleImage>();
        articleImageWrapper.allEq(map);
        articleImageService.delete(articleImageWrapper);

        Wrapper<ArticleShareHistory> articleShareHistoryWrapper = new EntityWrapper<ArticleShareHistory>();
        articleShareHistoryWrapper.allEq(map);
        articleShareHistoryService.delete(articleShareHistoryWrapper);

        Wrapper<ArticleTag> articleTagWrapper = new EntityWrapper<ArticleTag>();
        articleTagWrapper.allEq(map);
        articleTagService.delete(articleTagWrapper);

        Wrapper<ArticleThumbsDown> articleThumbsDownEntityWrapper = new EntityWrapper<ArticleThumbsDown>();
        articleThumbsDownEntityWrapper.allEq(map);
        articleThumbsDownService.delete(articleThumbsDownEntityWrapper);

        Wrapper<ArticleThumbsUp> articleThumbsUpEntityWrapper = new EntityWrapper<ArticleThumbsUp>();
        articleThumbsUpEntityWrapper.allEq(map);
        articleThumbsUpService.delete(articleThumbsUpEntityWrapper);

        Wrapper<Comment> commentWrapper = new EntityWrapper<Comment>();
        commentWrapper.allEq(map);
        List<Comment> comments = commentService.selectList(commentWrapper);

        if(!CollectionUtils.isEmpty(comments)){
            for (Comment comment : comments) {

                Map<String, Object> map1 = new HashMap<>();
                map1.put("comment_id", comment.getId());
                Wrapper<CommentThumbsUp> commentThumbsUpWrapper = new EntityWrapper<>();
                commentThumbsUpWrapper.allEq(map1);
                commentThumbsUpService.delete(commentThumbsUpWrapper);

                this.deleteComment(comment.getId());

                if(comment.getCommentsCountId() != null) {
                    commentCountService.deleteById(comment.getCommentsCountId());
                }

                if(comment.getThumbsUpCountId() != null) {
                    commentThumbsUpCountService.deleteById(comment.getThumbsUpCountId());
                }
            }
        }

        Article article = this.selectById(id);

        super.deleteById(id);

        if(article.getLocationId() != null){
            locationService.deleteById(article.getLocationId());
        }

        if(article.getCommentsCountId() != null) {
            commentCountService.deleteById(article.getCommentsCountId());
        }

        if(article.getArticleBody1Id() != null) {
            articleBody1Service.deleteById(article.getArticleBody1Id());
        }

        if(article.getArticleVideoBodyId() != null) {
            articleVideoBodyService.deleteById(article.getArticleVideoBodyId());
        }

        if(article.getArticleBody3Id() != null) {
            articleBody3Service.deleteById(article.getArticleBody3Id());
        }

        if(article.getThumbsDownCountId() != null) {
            articleThumbsDownCountService.deleteById(article.getThumbsDownCountId());
        }

        if(article.getThumbsUpCountId() != null) {
            articleThumbsUpCountService.deleteById(article.getThumbsUpCountId());
        }

        if(article.getViewCountId() != null) {
            articleViewCountService.deleteById(article.getViewCountId());
        }

    }

    public void deleteComment(Long commentId){
        Wrapper<Comment> childrenCommentWrapper = new EntityWrapper<Comment>();
        childrenCommentWrapper.eq("parent_id", commentId);
        List<Comment> childrenCommentcomments = commentService.selectList(childrenCommentWrapper);
        if(!CollectionUtils.isEmpty(childrenCommentcomments)){
            for (Comment childrenCommentcomment : childrenCommentcomments) {
                deleteComment(childrenCommentcomment.getId());
                commentService.deleteById(childrenCommentcomment.getId());
            }
        }
        commentService.deleteById(commentId);
    }


}
