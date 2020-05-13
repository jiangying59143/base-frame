package com.yjiang.base.modular.health.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.yjiang.base.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.yjiang.base.modular.system.model.Health;
import com.yjiang.base.modular.health.service.IHealthService;

/**
 * 健康素养控制器
 *
 * @author fengshuonan
 * @Date 2020-05-13 19:51:35
 */
@Controller
@RequestMapping("/health")
public class HealthController extends BaseController {

    private String PREFIX = "/Health/health/";

    @Autowired
    private IHealthService healthService;

    /**
     * 跳转到健康素养首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "health.html";
    }

    /**
     * 跳转到添加健康素养
     */
    @RequestMapping("/health_add")
    public String healthAdd() {
        return PREFIX + "health_add.html";
    }

    /**
     * 跳转到修改健康素养
     */
    @RequestMapping("/health_update/{healthId}")
    public String healthUpdate(@PathVariable Integer healthId, Model model) {
        Health health = healthService.selectById(healthId);
        model.addAttribute("item",health);
        LogObjectHolder.me().set(health);
        return PREFIX + "health_edit.html";
    }

    /**
     * 获取健康素养列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return healthService.selectList(null);
    }

    /**
     * 新增健康素养
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(Health health) {
        healthService.insert(health);
        return SUCCESS_TIP;
    }

    /**
     * 删除健康素养
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer healthId) {
        healthService.deleteById(healthId);
        return SUCCESS_TIP;
    }

    /**
     * 修改健康素养
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(Health health) {
        healthService.updateById(health);
        return SUCCESS_TIP;
    }

    /**
     * 健康素养详情
     */
    @RequestMapping(value = "/detail/{healthId}")
    @ResponseBody
    public Object detail(@PathVariable("healthId") Integer healthId) {
        return healthService.selectById(healthId);
    }
}
