package com.yjiang.base.modular.AlipayOrder.service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.google.zxing.WriterException;
import com.yjiang.base.modular.system.model.AlipayOrder;
import com.baomidou.mybatisplus.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 支付宝订单表 服务类
 * </p>
 *
 * @author jiangying
 * @since 2023-02-21
 */
public interface IAlipayOrderService extends IService<AlipayOrder> {
    JSONObject generateQRCode(String subject, BigDecimal amount) throws Exception;

    JSONObject refund(String orderId, BigDecimal refundAmount, String reason) throws AlipayApiException;

    Map<String, String> convertAliPayRequestToMap(HttpServletRequest request) throws UnsupportedEncodingException;

    boolean verifyAlipayParams(Map<String, String> params) throws AlipayApiException;
}
