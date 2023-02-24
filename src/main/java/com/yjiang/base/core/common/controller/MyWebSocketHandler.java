package com.yjiang.base.core.common.controller;

import com.yjiang.base.core.shiro.ShiroKit;
import com.yjiang.base.core.util.ChatGPTUtil;
import com.yjiang.base.modular.ChatMessage.service.IChatMessageService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyWebSocketHandler implements WebSocketHandler {
    @Autowired
    private IChatMessageService chatMessageService;

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

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
        if(session.isOpen()){
            ShiroKit.getUser();
            session.sendMessage(message);
        }else{
            System.out.println("session is closed !!!!!!!!!!!!!!");
        }
        JSONObject jsonMessage = new JSONObject();
        try {
            JSONObject websocketMessage = new JSONObject(message.getPayload().toString());
            String msg = websocketMessage.getString("content");
            jsonMessage.put("content", msg);
            System.out.println("WebSocket message received: " + msg);
            String chatResp = chatMessageService.sendMsg(msg, 3000);
            jsonMessage.put("content", chatResp);
            System.out.println("chatGPT response: " + chatResp);
        }catch (Exception e){
            jsonMessage.put("content", "ERR:" + e.getMessage());
            e.printStackTrace();
        }

        if(session.isOpen()){
            jsonMessage.put("username", "chatGPT");
            session.sendMessage(new TextMessage(jsonMessage.toString()));
        }else{
            System.out.println("session is closed !!!!!!!!!!!!!!");
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
