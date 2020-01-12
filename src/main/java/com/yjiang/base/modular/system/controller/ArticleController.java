package com.yjiang.base.modular.system.controller;

import com.yjiang.base.config.properties.GunsProperties;
import com.yjiang.base.core.common.annotion.BussinessLog;
import com.yjiang.base.core.common.constant.dictmap.NoticeMap;
import com.yjiang.base.core.common.constant.factory.ConstantFactory;
import com.yjiang.base.core.common.exception.BizExceptionEnum;
import com.yjiang.base.core.log.LogObjectHolder;
import com.yjiang.base.core.shiro.ShiroKit;
import com.yjiang.base.core.util.AliyunOSSClientUtil;
import com.yjiang.base.core.util.DateUtils;
import com.yjiang.base.modular.system.model.Article;
import com.yjiang.base.modular.system.model.ArticleCategory;
import com.yjiang.base.modular.system.model.Category;
import com.yjiang.base.modular.system.model.Dict;
import cn.stylefeng.base.modular.system.service.*;
import com.yjiang.base.modular.system.transfer.ArticleDto;
import cn.stylefeng.guns.modular.system.model.*;
import cn.stylefeng.guns.modular.system.service.*;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.yjiang.base.modular.system.service.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 文章控制器
 *
 * @author fengshuonan
 * @Date 2018-10-28 00:41:14
 */
@Controller
@RequestMapping("/article")
public class ArticleController extends BaseController {

    private String PREFIX = "/system/article/";

    @Autowired
    private IArticleService articleService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IArticleCategoryService articleCategoryService;

    @Autowired
    private IArticleImageService articleImageService;

    @Autowired
    private GunsProperties gunsProperties;

    @Autowired

    /**
     * 跳转到文章首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "article.html";
    }

    /**
     * 跳转到文章首页
     */
    @RequestMapping("/index")
    public String index1() {
        return PREFIX + "article1.html";
    }

    /**
     * 跳转到添加文章
     */
    @RequestMapping("/article_add")
    public String articleAdd(Model model) {
        List<Dict> list = ConstantFactory.me().getDictsByName("文章类型");
        for (int i = list.size()-1; i >=0; i--) {
            if(!"3".equals(list.get(i).getCode())){
                list.remove(i);
            }
        }
        model.addAttribute("articleTypes",list);
        model.addAttribute("categories",categoryService.list());
        return PREFIX + "article_add1.html";
    }

    /**
     * 跳转到添加文章
     */
    @RequestMapping("/article_add2")
    public String articleAdd2(Model model) {
        List<Dict> list = ConstantFactory.me().getDictsByName("文章类型");
        for (int i = list.size()-1; i >=0; i--) {
            if(!"4".equals(list.get(i).getCode())){
                list.remove(i);
            }
        }
        model.addAttribute("articleTypes",list);
        model.addAttribute("categories",categoryService.list());
        return PREFIX + "article_add2.html";
    }

    @RequestMapping("/article_add3")
    public String articleAdd3(Model model) {
        List<Dict> list = ConstantFactory.me().getDictsByName("文章类型");
        for (int i = list.size()-1; i >=0; i--) {
            if(!"2".equals(list.get(i).getCode())){
                list.remove(i);
            }
        }
        model.addAttribute("articleTypes",list);
        model.addAttribute("categories",categoryService.list());
        return PREFIX + "article_add3.html";
    }

    /**
     * 上传图片
     */
    @RequestMapping(method = RequestMethod.POST, path = "/upload")
    @ResponseBody
    public String upload(@RequestPart("upfile") MultipartFile picture) {
        String pictureName = UUID.randomUUID().toString() + "." + ToolUtil.getFileSuffix(picture.getOriginalFilename());
        try {
            String fileSavePath = gunsProperties.getFileUploadPath();
            picture.transferTo(new File(fileSavePath + pictureName));
        } catch (Exception e) {
            throw new ServiceException(BizExceptionEnum.UPLOAD_ERROR);
        }
        return pictureName;
    }

    /**
     * 跳转到修改文章
     */
    @RequestMapping("/article_update/{articleId}")
    public String articleUpdate(@PathVariable Long articleId, Model model) {
        ArticleDto articleDto = articleService.get(articleId);
        model.addAttribute("item",articleDto);
        List<Dict> list = ConstantFactory.me().getDictsByName("文章类型");
//        for (int i = list.size()-1; i >=0; i--) {
//            if("1".equals(list.get(i).getCode()) || "2".equals(list.get(i).getCode())){
//                list.remove(i);
//            }
//        }
        model.addAttribute("articleTypes",list);
        model.addAttribute("articleBody4s",articleDto.getArticleDto4s());
        model.addAttribute("categories",categoryService.list());
        LogObjectHolder.me().set(articleDto);
        if(articleDto.getArticleType() == ArticleDto.ARTICLE_TYPE_VEDIO){
            return PREFIX + "article_edit3.html";
        }
        if(articleDto.getArticleType() == ArticleDto.ARTICLE_TYPE_IMAG_CONTENT_LIST){
            return PREFIX + "article_edit2.html";
        }
        return PREFIX + "article_edit1.html";
    }

    /**
     * 跳转到修改文章
     */
    @RequestMapping("/article_bannerSet/{articleId}")
    public String articleBannerSet(@PathVariable Long articleId, Model model) {
        ArticleDto articleDto = articleService.get(articleId);
        model.addAttribute("item",articleDto);
        model.addAttribute("images",articleImageService.listByArticleId(articleId));
        LogObjectHolder.me().set(articleDto);
        return PREFIX + "article_bannerSet.html";
    }

    /**
     * 获取文章列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Wrapper<Article> wrapper = new EntityWrapper<Article>();
        Wrapper<Article> wrapper1 = new EntityWrapper<Article>();
        wrapper.like("title",condition);
        wrapper.eq("admin_flag", 1);
        if(!"admin".equals(ShiroKit.getUser().getAccount())) {
            wrapper.eq("author_user_id", ShiroKit.getUser().getId());
        }
        wrapper.orderDesc(Arrays.asList("create_date"));
        List<Article> articles = articleService.selectList(wrapper);
        List<Map<String, String>> maps = new ArrayList<>();
        for (Article article : articles) {
            Map<String, String> map = BeanUtils.describe(article);
            map.put("articleTypeDesc", ConstantFactory.me().getDictsByName("文章类型", article.getArticleType()));

            Map<String, Object> articleCategoryMap = new HashMap<>();
            articleCategoryMap.put("article_id", article.getId());
            List<ArticleCategory> articleCategories = articleCategoryService.selectByMap(articleCategoryMap);
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("id", articleCategories.get(0).getCategoryId());
            List<Category> categories = categoryService.selectByMap(categoryMap);
            map.put("categoryDesc", categories.get(0).getCategoryName());

            map.put("createDateStr", DateFormatUtils.format(article.getCreateDate(), DateUtils.DATE_TIME_TO_SECOND));
            map.put("username", StringUtils.isNotBlank(userService.selectById(article.getAuthorUserId()).getName()) ? userService.selectById(article.getAuthorUserId()).getName() : userService.selectById(article.getAuthorUserId()).getAccount());
            map.put("banner", article.getBannerFlag() ? "是" : "否");
            map.put("privilegeStr", article.getPrivilege() ? "是" : "否");
            maps.add(map);
        }
        return maps;
    }

    /**
     * 获取文章列表
     */
    @RequestMapping(value = "/list1")
    @ResponseBody
    public Object list1(String condition) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Wrapper<Article> wrapper = new EntityWrapper<Article>();
        wrapper.like("title",condition);
        wrapper.eq("admin_flag", 0);
        if(!"admin".equals(ShiroKit.getUser().getAccount())) {
            wrapper.eq("author_user_id", ShiroKit.getUser().getId());
        }
        wrapper.orderDesc(Arrays.asList("create_date"));
        List<Article> articles = articleService.selectList(wrapper);
        List<Map<String, String>> maps = new ArrayList<>();
        for (Article article : articles) {
            Map<String, String> map = BeanUtils.describe(article);
            map.put("articleTypeDesc", ConstantFactory.me().getDictsByName("文章类型", article.getArticleType()));

            Map<String, Object> articleCategoryMap = new HashMap<>();
            articleCategoryMap.put("article_id", article.getId());
            List<ArticleCategory> articleCategories = articleCategoryService.selectByMap(articleCategoryMap);
            Map<String, Object> categoryMap = new HashMap<>();
            categoryMap.put("id", articleCategories.get(0).getCategoryId());
            List<Category> categories = categoryService.selectByMap(categoryMap);
            map.put("categoryDesc", categories.get(0).getCategoryName());

            map.put("createDateStr", DateFormatUtils.format(article.getCreateDate(), DateUtils.DATE_TIME_TO_SECOND));
            map.put("username", StringUtils.isNotBlank(userService.selectById(article.getAuthorUserId()).getName()) ? userService.selectById(article.getAuthorUserId()).getName() : userService.selectById(article.getAuthorUserId()).getAccount());
            map.put("banner", article.getBannerFlag() ? "是" : "否");
            map.put("privilegeStr", article.getPrivilege() ? "是" : "否");
            maps.add(map);
        }
        return maps;
    }

    /**
     * 新增文章
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增文章", key = "title", dict = NoticeMap.class)
    public Object add(ArticleDto article) throws IOException {
        articleService.add(article);
        return SUCCESS_TIP;
    }

    /**
     * 新增文章
     */
    @RequestMapping(value = "/add2")
    @ResponseBody
    @BussinessLog(value = "新增文章2", key = "title", dict = NoticeMap.class)
    public Object add2(ArticleDto article) throws IOException {
        articleService.add(article);
        return SUCCESS_TIP;
    }

    /**
     * 删除文章
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Long articleId) {
        articleService.deleteById(articleId);
        return SUCCESS_TIP;
    }

    /**
     * 修改文章
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(ArticleDto article) throws IOException {
        articleService.update(article);
        return SUCCESS_TIP;
    }

    /**
     * 修改文章
     */
    @RequestMapping(value = "/bannerSet")
    @ResponseBody
    public Object bannerSet(ArticleDto article) {
        articleService.bannerSet(article);
        return SUCCESS_TIP;
    }

    /**
     * 修改文章
     */
    @RequestMapping(value = "/bannerCancel")
    @ResponseBody
    public Object bannerCancel(@RequestParam Long articleId) {
        ArticleDto article = articleService.get(articleId);
        articleService.bannerCancel(article);
        return SUCCESS_TIP;
    }

    /**
     * 文章详情
     */
    @RequestMapping(value = "/detail/{articleId}")
    @ResponseBody
    public Object detail(@PathVariable("articleId") Integer articleId) {
        return articleService.selectById(articleId);
    }

    @PostMapping("/image/upload")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String pictureName = UUID.randomUUID().toString() + "." + ToolUtil.getFileSuffix(file.getOriginalFilename());
        AliyunOSSClientUtil.upload(AliyunOSSClientUtil.HTML_IMAGE_BUCKET_NAME, pictureName, file);
        return AliyunOSSClientUtil.getPath(AliyunOSSClientUtil.HTML_IMAGE_BUCKET_NAME, "a6aad221-95e4-4b68-9d8b-bc293ad835e3.jpg");
    }

    @RequestMapping(value = "/uploadImg", method = RequestMethod.POST)
    @ResponseBody
    public void uploadImg(@RequestParam(value = "file", required = false) MultipartFile[] cardFile,
                          HttpServletRequest request, HttpServletResponse response) {
        System.out.println("*************接收上传文件*************");
        try {
            if (cardFile != null) {
                JSONObject json = new JSONObject();
                JSONArray arr = new JSONArray();
                String[] pictureNames = new String[cardFile.length];
                for (int i=0; i< cardFile.length; i ++) {
                    MultipartFile multipartFile = cardFile[i];
                    String pictureName = UUID.randomUUID().toString() + "." + ToolUtil.getFileSuffix(multipartFile.getOriginalFilename());
                    AliyunOSSClientUtil.upload(AliyunOSSClientUtil.HTML_IMAGE_BUCKET_NAME, pictureName, multipartFile);
                    String imgUrl = AliyunOSSClientUtil.getPath(AliyunOSSClientUtil.HTML_IMAGE_BUCKET_NAME, pictureName);
                    arr.add(imgUrl);
                    pictureNames[i]=pictureName;
                }
//                getSession().setAttribute(ShiroKit.getUser().getId() + "articleImages", pictureNames); //提交时候要删除，作废，不是一个好的解决方式

                json.put("errno", 0);
                json.put("data", arr);
                response.setContentType("text/text;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.print(json.toString());
                out.flush();
                out.close();
            } else {
                System.out.println("************上传文件为空***************");
            }
            System.out.println("*************接收上传文件结束*************");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
