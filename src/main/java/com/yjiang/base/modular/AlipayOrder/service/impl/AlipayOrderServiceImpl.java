package com.yjiang.base.modular.AlipayOrder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.yjiang.base.config.properties.AlipayProperties;
import com.yjiang.base.core.common.constant.OrderStatusEnum;
import com.yjiang.base.modular.system.model.AlipayOrder;
import com.yjiang.base.modular.system.dao.AlipayOrderMapper;
import com.yjiang.base.modular.AlipayOrder.service.IAlipayOrderService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 支付宝订单表 服务实现类
 * </p>
 *
 * @author jiangying
 * @since 2023-02-21
 */
@Service
public class AlipayOrderServiceImpl extends ServiceImpl<AlipayOrderMapper, AlipayOrder> implements IAlipayOrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlipayOrderServiceImpl.class);

    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private AlipayProperties alipayProperties;

    @Override
    @Transactional
    public JSONObject generateQRCode(String subject, BigDecimal amount) throws Exception {
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        String orderNo = UUID.randomUUID().toString();

        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();

        model.setOutTradeNo(orderNo);
        model.setTotalAmount(amount.toString());
        model.setSubject(subject);
        model.setBody(subject + " " + amount);

        request.setBizModel(model);
        request.setNotifyUrl("http://localhost:9999/alipay/notifyUrl");
        AlipayTradePrecreateResponse response = alipayClient.execute(request);
        LOGGER.warn(response.getCode() + "\n" + response.getBody());
        JSONObject jsonObject = newJSONObject("生成支付二维码失败");
        if (response.isSuccess()) {
            insert(new AlipayOrder(orderNo, amount, subject, subject, OrderStatusEnum.UNPAID.getCode(), new Date()));
            jsonObject.put("code", 1);
            jsonObject.put("content", getQrCodeBase64(response.getQrCode()));
        }
        return jsonObject;
    }

    @Override
    public JSONObject refund(String orderId, BigDecimal refundAmount, String reason) throws AlipayApiException {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel alipayTradeRefundModel = new AlipayTradeRefundModel();
        alipayTradeRefundModel.setOutTradeNo(orderId);
        alipayTradeRefundModel.setOutRequestNo(orderId);
        alipayTradeRefundModel.setRefundAmount(refundAmount.toString());
        alipayTradeRefundModel.setRefundReason(reason);
        // 3. 发送退款请求
        AlipayTradeRefundResponse response = alipayClient.execute(request);
        LOGGER.warn(response.getCode() + "\n" + response.getBody());
        // 4. 处理退款响应结果
        JSONObject jsonObject = newJSONObject("退款请求失败");
        if (response.isSuccess()) {
            jsonObject.put("code", 1);
            jsonObject.put("content", "退款请求成功");
        }
        LOGGER.warn(response.getCode() + "\n" + response.getBody());
        return jsonObject;
    }

    private String getQrCodeBase64(String qrCodeUrl) throws WriterException, IOException {
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new QRCodeWriter().encode(qrCodeUrl, BarcodeFormat.QR_CODE, 200, 200, hints);
        BufferedImage qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(qrCodeImage, "png", outputStream);
        byte[] bytes = outputStream.toByteArray();
        String qrCodeBase64 = Base64.getEncoder().encodeToString(bytes);
        return qrCodeBase64;
    }

    @Override
    public Map<String, String> convertAliPayRequestToMap(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            StringBuilder valueStr = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                valueStr.append((i == values.length - 1) ? values[i] : values[i] + ",");
            }
            //乱码解决，这段代码在出现乱码时使用。
            valueStr = new StringBuilder(new String(valueStr.toString().getBytes("ISO-8859-1"), "utf-8"));
            params.put(name, valueStr.toString());
        }
        return params;
    }

    public boolean verifyAlipayParams(Map<String, String> params) throws AlipayApiException {
        return AlipaySignature.rsaCertCheckV1(params, alipayProperties.getPublicKey(), alipayProperties.getCharset(), alipayProperties.getSignType());
    }

    private JSONObject newJSONObject(String errorMsg){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        jsonObject.put("content", errorMsg);
        return jsonObject;
    }
}
