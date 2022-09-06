package com.yjiang.base.modular.HealthUsers.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.yjiang.base.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.yjiang.base.modular.system.model.HealthUsers;
import com.yjiang.base.modular.HealthUsers.service.IHealthUsersService;

/**
 * HealthUser控制器
 *
 * @author fengshuonan
 * @Date 2022-09-06 16:55:37
 */
@Controller
@RequestMapping("/healthUsers")
public class HealthUsersController extends BaseController {

    private String PREFIX = "/HealthUsers/healthUsers/";

    @Autowired
    private IHealthUsersService healthUsersService;

    /**
     * 跳转到HealthUser首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "healthUsers.html";
    }

    /**
     * 跳转到添加HealthUser
     */
    @RequestMapping("/healthUsers_add")
    public String healthUsersAdd() {
        return PREFIX + "healthUsers_add.html";
    }

    /**
     * 跳转到修改HealthUser
     */
    @RequestMapping("/healthUsers_update/{healthUsersId}")
    public String healthUsersUpdate(@PathVariable Integer healthUsersId, Model model) {
        HealthUsers healthUsers = healthUsersService.selectById(healthUsersId);
        model.addAttribute("item",healthUsers);
        LogObjectHolder.me().set(healthUsers);
        return PREFIX + "healthUsers_edit.html";
    }

    /**
     * 获取HealthUser列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return healthUsersService.selectList(null);
    }

    /**
     * 新增HealthUser
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(HealthUsers healthUsers) {
        healthUsersService.insert(healthUsers);
        return SUCCESS_TIP;
    }

    /**
     * 删除HealthUser
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer healthUsersId) {
        healthUsersService.deleteById(healthUsersId);
        return SUCCESS_TIP;
    }

    /**
     * 修改HealthUser
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(HealthUsers healthUsers) {
        healthUsersService.updateById(healthUsers);
        return SUCCESS_TIP;
    }

    /**
     * HealthUser详情
     */
    @RequestMapping(value = "/detail/{healthUsersId}")
    @ResponseBody
    public Object detail(@PathVariable("healthUsersId") Integer healthUsersId) {
        return healthUsersService.selectById(healthUsersId);
    }
}
