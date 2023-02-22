package com.yjiang.base.modular.AlipayOrder.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.yjiang.base.core.common.constant.OrderStatusEnum;
import com.yjiang.base.modular.AlipayOrder.service.IAlipayOrderService;
import com.yjiang.base.modular.system.model.AlipayOrder;
import com.yjiang.base.modular.system.model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/alipay")
public class AlipayController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlipayController.class);

    private String PREFIX = "/AlipayOrder/alipayOrder/";

    @Autowired
    private IAlipayOrderService alipayOrderService;

    @PostMapping("/submitOrder")
    @ResponseBody
    public JSONObject submitOrder(@RequestParam String subject, @RequestParam String amount) throws Exception {
        JSONObject qrCode = alipayOrderService.generateQRCode(subject, new BigDecimal(amount));
        return qrCode;
    }

    @PostMapping("/submitRefundOrder")
    @ResponseBody
    public JSONObject submitRefundOrder(@RequestParam String orderId, @RequestParam BigDecimal refundAmount, @RequestParam String refundReason) throws Exception {
        JSONObject qrCode = alipayOrderService.refund(orderId, refundAmount, refundReason);
        return qrCode;
    }

    @GetMapping("/notifyUrl")
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> params = alipayOrderService.convertAliPayRequestToMap(request);
        LOGGER.info("notifyUrl params: {}", params);
        boolean verifyResult = alipayOrderService.verifyAlipayParams(params);
        if (verifyResult) {
            String tradeStatus = params.get("trade_status");
            if ("TRADE_SUCCESS".equals(tradeStatus)) {
                //支付成功，更新订单状态
                String orderId = params.get("out_trade_no");
                Wrapper<AlipayOrder> wrapper = new EntityWrapper<>();
                wrapper.eq("order_no", orderId);
                AlipayOrder order = alipayOrderService.selectOne(wrapper);
                order.setStatus(OrderStatusEnum.PAID.getCode());
                alipayOrderService.updateById(order);
            }
            response.getWriter().write("success");
        } else {
            response.getWriter().write("fail");
        }
    }

    @GetMapping("/returnUrl")
    public String returnUrl(HttpServletRequest request) throws Exception {
        Map<String, String> params = alipayOrderService.convertAliPayRequestToMap(request);
        LOGGER.info("returnUrl params: {}", params);
        boolean verifyResult = alipayOrderService.verifyAlipayParams(params);
        if (verifyResult) {
            String tradeStatus = params.get("trade_status");
            if ("TRADE_SUCCESS".equals(tradeStatus)) {
                //支付成功，更新订单状态
                String orderId = params.get("out_trade_no");
                return "success";
            } else {
                return "fail";
            }
        } else {
            return "fail";
        }
    }
}
