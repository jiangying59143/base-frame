package com.yjiang.base.modular.system.controller;

import com.yjiang.base.core.log.LogObjectHolder;
import com.yjiang.base.modular.system.model.ArticleCategory;
import com.yjiang.base.modular.system.service.IArticleCategoryService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 文章主题控制器
 *
 * @author fengshuonan
 * @Date 2018-11-20 18:00:58
 */
@Controller
@RequestMapping("/articleCategory")
public class ArticleCategoryController extends BaseController {

    private String PREFIX = "/system/articleCategory/";

    @Autowired
    private IArticleCategoryService articleCategoryService;

    /**
     * 跳转到文章主题首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "articleCategory.html";
    }

    /**
     * 跳转到添加文章主题
     */
    @RequestMapping("/articleCategory_add")
    public String articleCategoryAdd() {
        return PREFIX + "articleCategory_add.html";
    }

    /**
     * 跳转到修改文章主题
     */
    @RequestMapping("/articleCategory_update/{articleCategoryId}")
    public String articleCategoryUpdate(@PathVariable Integer articleCategoryId, Model model) {
        ArticleCategory articleCategory = articleCategoryService.selectById(articleCategoryId);
        model.addAttribute("item",articleCategory);
        LogObjectHolder.me().set(articleCategory);
        return PREFIX + "articleCategory_edit.html";
    }

    /**
     * 获取文章主题列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return articleCategoryService.selectList(null);
    }

    /**
     * 新增文章主题
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(ArticleCategory articleCategory) {
        articleCategoryService.insert(articleCategory);
        return SUCCESS_TIP;
    }

    /**
     * 删除文章主题
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer articleCategoryId) {
        articleCategoryService.deleteById(articleCategoryId);
        return SUCCESS_TIP;
    }

    /**
     * 修改文章主题
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(ArticleCategory articleCategory) {
        articleCategoryService.updateById(articleCategory);
        return SUCCESS_TIP;
    }

    /**
     * 文章主题详情
     */
    @RequestMapping(value = "/detail/{articleCategoryId}")
    @ResponseBody
    public Object detail(@PathVariable("articleCategoryId") Integer articleCategoryId) {
        return articleCategoryService.selectById(articleCategoryId);
    }
}
