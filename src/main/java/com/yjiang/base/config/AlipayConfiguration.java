package com.yjiang.base.config;


import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.yjiang.base.config.properties.AlipayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 两个支付宝客户端，用户可以使用任意一个.
 *
 * alipay-trade-sdk 是对alipay-sdk-java的封装，建议使用alipay-trade-sdk.
 *
 * @author mengday zhang
 */
@Configuration
@EnableConfigurationProperties(AlipayProperties.class)
public class AlipayConfiguration {

	@Autowired
	private AlipayProperties properties;
	/**
	 * alipay-sdk-java
	 * @return
	 */
	@Bean
	public AlipayClient alipayClient(){
		AlipayClient client = new DefaultAlipayClient(properties.getGatewayUrl(),
				properties.getAppId(),
				properties.getPrivateKey(),
				properties.getFormat(),
				properties.getCharset(),
				properties.getPublicKey(),
				properties.getSignType());
		return client;
	}
}
