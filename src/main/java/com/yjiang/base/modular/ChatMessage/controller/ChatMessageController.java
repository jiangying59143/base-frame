package com.yjiang.base.modular.ChatMessage.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.yjiang.base.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Date;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import java.util.Arrays;
import com.yjiang.base.modular.system.model.ChatMessage;
import com.yjiang.base.modular.ChatMessage.service.IChatMessageService;

/**
 * chatMessage控制器
 *
 * @author fengshuonan
 * @Date 2023-02-20 18:29:53
 */
@Controller
@RequestMapping("/chatMessage")
public class ChatMessageController extends BaseController {

    private String PREFIX = "/ChatMessage/chatMessage/";

    @Autowired
    private IChatMessageService chatMessageService;

    /**
     * 跳转到chatMessage首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "chatMessage.html";
    }

    /**
     * 跳转到添加chatMessage
     */
    @RequestMapping("/chatMessage_add")
    public String chatMessageAdd() {
        return PREFIX + "chatMessage_add.html";
    }

    /**
     * 跳转到修改chatMessage
     */
    @RequestMapping("/chatMessage_update/{chatMessageId}")
    public String chatMessageUpdate(@PathVariable Integer chatMessageId, Model model) {
        ChatMessage chatMessage = chatMessageService.selectById(chatMessageId);
        model.addAttribute("item",chatMessage);
        LogObjectHolder.me().set(chatMessage);
        return PREFIX + "chatMessage_edit.html";
    }

    /**
     * 获取chatMessage列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Wrapper<ChatMessage> wrapper = new EntityWrapper<ChatMessage>();
        wrapper.orderDesc(Arrays.asList("id"));
        return chatMessageService.selectList(wrapper);
    }

    /**
     * 新增chatMessage
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add(ChatMessage chatMessage) {
        chatMessageService.insert(chatMessage);
        return SUCCESS_TIP;
    }

    /**
     * 删除chatMessage
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam Integer chatMessageId) {
        chatMessageService.deleteById(chatMessageId);
        return SUCCESS_TIP;
    }

    /**
     * 修改chatMessage
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update(ChatMessage chatMessage) {
        chatMessage.setUpdateDate(new Date());
        chatMessageService.updateById(chatMessage);
        return SUCCESS_TIP;
    }

    /**
     * chatMessage详情
     */
    @RequestMapping(value = "/detail/{chatMessageId}")
    @ResponseBody
    public Object detail(@PathVariable("chatMessageId") Integer chatMessageId) {
        return chatMessageService.selectById(chatMessageId);
    }
}
