package com.yjiang.base.modular.SsqLottery.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.yjiang.base.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.yjiang.base.modular.system.model.SsqLottery;
import com.yjiang.base.modular.SsqLottery.service.ISsqLotteryService;

/**
 * 双色球控制器
 *
 * @author fengshuonan
 * @Date 2020-03-08 21:50:12
 */
@Controller
@RequestMapping("/ssqLottery")
public class SsqLotteryController extends BaseController {

    private String PREFIX = "/SsqLottery/ssqLottery/";

    @Autowired
    private ISsqLotteryService ssqLotteryService;

    /**
     * 跳转到双色球首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "ssqLottery.html";
    }

    /**
     * 跳转到添加双色球
     */
    @RequestMapping("/ssqLottery_add")
    public String ssqLotteryAdd() {
        return PREFIX + "ssqLottery_add.html";
    }

    /**
     * 跳转到修改双色球
     */
    @RequestMapping("/ssqLottery_update/{ssqLotteryId}")
    public String ssqLotteryUpdate(@PathVariable Integer ssqLotteryId, Model model) {
        SsqLottery ssqLottery = ssqLotteryService.selectById(ssqLotteryId);
        model.addAttribute("item",ssqLottery);
        LogObjectHolder.me().set(ssqLottery);
        return PREFIX + "ssqLottery_edit.html";
    }

    /**
     * 获取双色球列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return ssqLotteryService.selectList(null);
    }

    /**
     * 新增双色球
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(SsqLottery ssqLottery) {
        ssqLotteryService.insert(ssqLottery);
        return SUCCESS_TIP;
    }

    /**
     * 删除双色球
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer ssqLotteryId) {
        ssqLotteryService.deleteById(ssqLotteryId);
        return SUCCESS_TIP;
    }

    /**
     * 修改双色球
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(SsqLottery ssqLottery) {
        ssqLotteryService.updateById(ssqLottery);
        return SUCCESS_TIP;
    }

    /**
     * 双色球详情
     */
    @RequestMapping(value = "/detail/{ssqLotteryId}")
    @ResponseBody
    public Object detail(@PathVariable("ssqLotteryId") Integer ssqLotteryId) {
        return ssqLotteryService.selectById(ssqLotteryId);
    }
}
