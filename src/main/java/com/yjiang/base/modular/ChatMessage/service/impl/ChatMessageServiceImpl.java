package com.yjiang.base.modular.ChatMessage.service.impl;

import com.yjiang.base.config.properties.ChatGptProproperties;
import com.yjiang.base.modular.system.model.ChatMessage;
import com.yjiang.base.modular.system.dao.ChatMessageMapper;
import com.yjiang.base.modular.ChatMessage.service.IChatMessageService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiangying
 * @since 2023-02-20
 */
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements IChatMessageService {

    @Autowired
    private ChatGptProproperties chatGptProproperties;

    @Autowired
    private OkHttpClient chatClient;

    @Override
    public String sendMsg(String msg, int maxTokens) throws IOException {
        StringBuilder responseStr = new StringBuilder();
        JSONObject requestObject = new JSONObject();
        requestObject.put("prompt", msg);
        requestObject.put("max_tokens", maxTokens);

        String json = requestObject.toString();

        String apiKey = StringUtils.isNotBlank(chatGptProproperties.getApiKey()) ? chatGptProproperties.getApiKey() : System.getenv("OPEN_API_KEY");
        RequestBody body = RequestBody.create(MediaType.parse(chatGptProproperties.getMediaType()), json);
        Request request = new Request.Builder()
                .url(chatGptProproperties.getApiUrl())
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        Call call = chatClient.newCall(request);
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
}
