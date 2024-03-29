package com.yjiang.base.modular.ChatMessage.service;

import com.yjiang.base.modular.system.model.ChatMessage;
import com.baomidou.mybatisplus.service.IService;

import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jiangying
 * @since 2023-02-20
 */
public interface IChatMessageService extends IService<ChatMessage> {
    String sendMsg(String msg, int maxTokens) throws IOException;
}
