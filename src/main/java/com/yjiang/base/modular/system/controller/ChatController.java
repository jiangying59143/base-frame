package com.yjiang.base.modular.system.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
public class ChatController  extends BaseController {

    private String PREFIX = "/system/chat/";

    @RequestMapping("")
    public String index() {
        return PREFIX + "room.html";
    }

}
