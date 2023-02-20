package com.yjiang.base.modular.HealthAnswer.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.yjiang.base.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Date;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.yjiang.base.modular.system.model.HealthAnswer;
import com.yjiang.base.modular.HealthAnswer.service.IHealthAnswerService;

/**
 * healthAnswer控制器
 *
 * @author fengshuonan
 * @Date 2022-09-07 00:08:02
 */
@Controller
@RequestMapping("/healthAnswer")
public class HealthAnswerController extends BaseController {

    private String PREFIX = "/HealthAnswer/healthAnswer/";

    @Autowired
    private IHealthAnswerService healthAnswerService;

    /**
     * 跳转到healthAnswer首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "healthAnswer.html";
    }

    /**
     * 跳转到添加healthAnswer
     */
    @RequestMapping("/healthAnswer_add")
    public String healthAnswerAdd() {
        return PREFIX + "healthAnswer_add.html";
    }

    /**
     * 跳转到修改healthAnswer
     */
    @RequestMapping("/healthAnswer_update/{healthAnswerId}")
    public String healthAnswerUpdate(@PathVariable Integer healthAnswerId, Model model) {
        HealthAnswer healthAnswer = healthAnswerService.selectById(healthAnswerId);
        model.addAttribute("item",healthAnswer);
        LogObjectHolder.me().set(healthAnswer);
        return PREFIX + "healthAnswer_edit.html";
    }

    /**
     * 获取healthAnswer列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Wrapper<HealthAnswer> wrapper = new EntityWrapper<HealthAnswer>();
        wrapper.orderDesc(Arrays.asList("id"));
        wrapper.like("title", condition);
        return healthAnswerService.selectList(wrapper);
    }

    /**
     * 新增healthAnswer
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(HealthAnswer healthAnswer) {
        healthAnswerService.insert(healthAnswer);
        return SUCCESS_TIP;
    }

    /**
     * 删除healthAnswer
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer healthAnswerId) {
        healthAnswerService.deleteById(healthAnswerId);
        return SUCCESS_TIP;
    }

    /**
     * 修改healthAnswer
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(HealthAnswer healthAnswer) {
        healthAnswer.setUpdateDate(new Date());
        healthAnswerService.updateById(healthAnswer);
        return SUCCESS_TIP;
    }

    /**
     * healthAnswer详情
     */
    @RequestMapping(value = "/detail/{healthAnswerId}")
    @ResponseBody
    public Object detail(@PathVariable("healthAnswerId") Integer healthAnswerId) {
        return healthAnswerService.selectById(healthAnswerId);
    }
}
