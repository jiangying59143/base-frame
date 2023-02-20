package com.yjiang.base.core.common.controller;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class MyWebSocketHandler implements WebSocketHandler {
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static final String API_KEY = System.getenv("OPEN_API_KEY");;
    private static final String API_URL = "https://api.openai.com/v1/engines/davinci-codex/completions";
    private static final int maxTokens = 1000;
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // 设置连接超时时间为10秒
            .readTimeout(60, TimeUnit.SECONDS) // 设置读取数据超时时间为30秒
            .writeTimeout(60, TimeUnit.SECONDS) // 设置发送数据超时时间为30秒
            .build();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("WebSocket connected: " + session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage message) throws IOException {
        System.out.println("WebSocket message received: " + message.getPayload());
        if(!message.getPayload().toString().contains("{")){
            return;
        }

        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("username", "chatGpt");
        try {
            JSONObject websocketMessage = new JSONObject(message.getPayload().toString());
            String msg = websocketMessage.getString("content");

            System.out.println("WebSocket message received: " + msg);
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
                jsonMessage.put("content", text);
                System.out.println(text);
            } else {
                jsonMessage.put("content", "Request failed with status code: " + response.code());
                session.sendMessage(new TextMessage("Request failed with status code: " + response.code()));
                System.out.println("Request failed with status code: " + response.code());
                System.out.println("Error message: " + response.body().string());
            }
        }catch (Exception e){
            jsonMessage.put("content", "system error");
            e.printStackTrace();
        }

        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(message);
                s.sendMessage(new TextMessage(jsonMessage.toString()));
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("WebSocket error: " + exception.getMessage());
        sessions.remove(session);
        session.close();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("WebSocket closed: " + session.getId());
        sessions.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
