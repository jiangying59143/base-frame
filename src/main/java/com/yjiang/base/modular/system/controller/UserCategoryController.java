package com.yjiang.base.modular.system.controller;

import com.yjiang.base.core.log.LogObjectHolder;
import com.yjiang.base.modular.system.model.UserCategory;
import com.yjiang.base.modular.system.service.IUserCategoryService;
import com.yjiang.base.modular.system.transfer.UserCategoryDto;
import cn.stylefeng.roses.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户主题控制器
 *
 * @author fengshuonan
 * @Date 2018-11-20 18:11:10
 */
@Controller
@RequestMapping("/userCategory")
public class UserCategoryController extends BaseController {

    private String PREFIX = "/system/userCategory/";

    @Autowired
    private IUserCategoryService userCategoryService;

    /**
     * 跳转到用户主题首页
     */
    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("myCategories",userCategoryService.getUserCategories());
        model.addAttribute("categories",userCategoryService.getNotUserCategories());
        return PREFIX + "userCategory.html";
    }

    /**
     * 跳转到添加用户主题
     */
    @RequestMapping("/userCategory_add")
    public String userCategoryAdd() {
        return PREFIX + "userCategory_add.html";
    }

    /**
     * 跳转到修改用户主题
     */
    @RequestMapping("/userCategory_update/{userCategoryId}")
    public String userCategoryUpdate(@PathVariable Integer userCategoryId, Model model) {
        UserCategory userCategory = userCategoryService.selectById(userCategoryId);
        model.addAttribute("item",userCategory);
        LogObjectHolder.me().set(userCategory);
        return PREFIX + "userCategory_edit.html";
    }

    /**
     * 获取用户主题列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return userCategoryService.selectList(null);
    }

    /**
     * 新增用户主题
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(UserCategory userCategory) {
        userCategoryService.insert(userCategory);
        return SUCCESS_TIP;
    }

    /**
     * 删除用户主题
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer userCategoryId) {
        userCategoryService.deleteById(userCategoryId);
        return SUCCESS_TIP;
    }

    /**
     * 修改用户主题
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(UserCategoryDto userCategoryDto) {
        userCategoryService.update(userCategoryDto.getCategoryIds());
        return SUCCESS_TIP;
    }

    /**
     * 用户主题详情
     */
    @RequestMapping(value = "/detail/{userCategoryId}")
    @ResponseBody
    public Object detail(@PathVariable("userCategoryId") Integer userCategoryId) {
        return userCategoryService.selectById(userCategoryId);
    }
}
