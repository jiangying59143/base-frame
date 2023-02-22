package com.yjiang.base.core.util;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ChatGPTUtil {
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static final String API_KEY = System.getenv("OPEN_API_KEY");
    private static final String API_URL = "https://api.openai.com/v1/engines/davinci-codex/completions";
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // 设置连接超时时间为10秒
            .readTimeout(60, TimeUnit.SECONDS) // 设置读取数据超时时间为30秒
            .writeTimeout(60, TimeUnit.SECONDS) // 设置发送数据超时时间为30秒
            .build();

    public static String sendMsg(String msg, int maxTokens) throws IOException {
        StringBuilder responseStr = new StringBuilder();
        JSONObject requestObject = new JSONObject();
        requestObject.put("prompt", msg);
        requestObject.put("max_tokens", maxTokens);

        String json = requestObject.toString();

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .post(body)
                .build();

        System.out.println(json + " " + API_KEY);
        Call call = client.newCall(request);
        Response response = call.execute();

        if (response.isSuccessful()) {
            JSONObject responseObject = new JSONObject(response.body().string());
            String text = responseObject.getJSONArray("choices")
                    .getJSONObject(0)
                    .getString("text");
            responseStr.append(text);
        } else {
            responseStr.append("Request failed with status code: ");
            responseStr.append(response.code());
            System.out.println("Request failed with status code: " + response.code());
            System.out.println("Error message: " + response.body().string());
        }
        return responseStr.toString();
    }

    public static void main(String[] args) {
        Properties properties = System.getProperties();
        for (Map.Entry<Object, Object> prop : properties.entrySet()) {
            System.out.println(prop.getKey() + " :" + prop.getValue());
        }
//        System.out.println(System.getenv("OPEN_API_KEY"));
    }
}
