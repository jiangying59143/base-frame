package com.yjiang.base.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.yjiang.base.config.properties.AlipayProperties;
import com.yjiang.base.config.properties.ChatGptProproperties;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(ChatGptProproperties.class)
public class ChatGptConfig {
    @Autowired
    private ChatGptProproperties properties;

    @Bean
    public OkHttpClient chatClient(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(properties.getTimeout(), TimeUnit.SECONDS) // 设置连接超时时间为10秒
                .readTimeout(properties.getTimeout(), TimeUnit.SECONDS) // 设置读取数据超时时间为30秒
                .writeTimeout(properties.getTimeout(), TimeUnit.SECONDS) // 设置发送数据超时时间为30秒
                .build();
        return client;
    }
}
