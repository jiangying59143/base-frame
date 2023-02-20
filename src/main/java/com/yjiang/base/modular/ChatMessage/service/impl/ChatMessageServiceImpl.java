package com.yjiang.base.modular.ChatMessage.service.impl;

import com.yjiang.base.modular.system.model.ChatMessage;
import com.yjiang.base.modular.system.dao.ChatMessageMapper;
import com.yjiang.base.modular.ChatMessage.service.IChatMessageService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
