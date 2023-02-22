package com.yjiang.base.modular.AlipayOrder.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.yjiang.base.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Date;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import java.util.Arrays;
import com.yjiang.base.modular.system.model.AlipayOrder;
import com.yjiang.base.modular.AlipayOrder.service.IAlipayOrderService;

/**
 * AlipayOrder控制器
 *
 * @author fengshuonan
 * @Date 2023-02-22 13:06:57
 */
@Controller
@RequestMapping("/alipayOrder")
public class AlipayOrderController extends BaseController {

    private String PREFIX = "/AlipayOrder/alipayOrder/";

    @Autowired
    private IAlipayOrderService alipayOrderService;

    /**
     * 跳转到AlipayOrder首页
     */
    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("subject","test");
        model.addAttribute("amount","0.01");
        model.addAttribute("refundAmount","0.01");
        model.addAttribute("refundReason","refund test Reason");
        return PREFIX + "alipayOrder.html";
    }

    /**
     * 跳转到添加AlipayOrder
     */
    @RequestMapping("/alipayOrder_add")
    public String alipayOrderAdd() {
        return PREFIX + "alipayOrder_add.html";
    }

    /**
     * 跳转到修改AlipayOrder
     */
    @RequestMapping("/alipayOrder_update/{alipayOrderId}")
    public String alipayOrderUpdate(@PathVariable Integer alipayOrderId, Model model) {
        AlipayOrder alipayOrder = alipayOrderService.selectById(alipayOrderId);
        model.addAttribute("item",alipayOrder);
        LogObjectHolder.me().set(alipayOrder);
        return PREFIX + "alipayOrder_edit.html";
    }

    /**
     * 获取AlipayOrder列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Wrapper<AlipayOrder> wrapper = new EntityWrapper<AlipayOrder>();
        wrapper.orderDesc(Arrays.asList("id"));
        return alipayOrderService.selectList(wrapper);
    }

    /**
     * 新增AlipayOrder
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(AlipayOrder alipayOrder) {
        alipayOrderService.insert(alipayOrder);
        return SUCCESS_TIP;
    }

    /**
     * 删除AlipayOrder
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer alipayOrderId) {
        alipayOrderService.deleteById(alipayOrderId);
        return SUCCESS_TIP;
    }

    /**
     * 修改AlipayOrder
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(AlipayOrder alipayOrder) {
        alipayOrder.setUpdateTime(new Date());
        alipayOrderService.updateById(alipayOrder);
        return SUCCESS_TIP;
    }

    /**
     * AlipayOrder详情
     */
    @RequestMapping(value = "/detail/{alipayOrderId}")
    @ResponseBody
    public Object detail(@PathVariable("alipayOrderId") Integer alipayOrderId) {
        return alipayOrderService.selectById(alipayOrderId);
    }
}
