package com.yjiang.base.modular.system.controller;

import com.yjiang.base.core.log.LogObjectHolder;
import com.yjiang.base.core.shiro.ShiroKit;
import com.yjiang.base.modular.system.model.Category;
import com.yjiang.base.modular.system.service.ICategoryService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 控制器
 *
 * @author fengshuonan
 * @Date 2018-11-26 11:06:50
 */
@Controller
@RequestMapping("/category")
public class CategoryController extends BaseController {

    private String PREFIX = "/system/category/";

    @Autowired
    private ICategoryService categoryService;

    /**
     * 跳转到首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "category.html";
    }

    /**
     * 跳转到添加
     */
    @RequestMapping("/category_add")
    public String categoryAdd() {
        return PREFIX + "category_add.html";
    }

    /**
     * 跳转到修改
     */
    @RequestMapping("/category_update/{categoryId}")
    public String categoryUpdate(@PathVariable Integer categoryId, Model model) {
        Category category = categoryService.selectById(categoryId);
        model.addAttribute("item",category);
        LogObjectHolder.me().set(category);
        return PREFIX + "category_edit.html";
    }

    /**
     * 获取列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Wrapper<Category> wrapper = new EntityWrapper<Category>();
        wrapper.orderDesc(Arrays.asList("create_date"));
        wrapper.notIn("id", Arrays.asList(1L,2L));
        List<Category> categories = categoryService.selectList(wrapper);
        List<Map<String, String>> maps = new ArrayList<>();
        for (Category category : categories) {
            Map<String, String> map = BeanUtils.describe(category);
            map.put("defaultTagDesc",category.getDefaultTag() ? "是" : "否");
            maps.add(map);
        }
        return maps;
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Category category) {
        category.setCreateBy(ShiroKit.getUser().getId());
        category.setCreateDate(new Date());
        category.setUpdateBy(ShiroKit.getUser().getId());
        category.setUpdateDate(new Date());
        category.setVersion(1);
        categoryService.insert(category);
        return SUCCESS_TIP;
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Long categoryId) {
        categoryService.deleteById(categoryId);
        return SUCCESS_TIP;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @Transactional
    public Object update(Category category) {
        category.setUpdateBy(ShiroKit.getUser().getId());
        category.setUpdateDate(new Date());
        categoryService.updateById(category);
        return SUCCESS_TIP;
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{categoryId}")
    @ResponseBody
    public Object detail(@PathVariable("categoryId") Integer categoryId) {
        return categoryService.selectById(categoryId);
    }
}
