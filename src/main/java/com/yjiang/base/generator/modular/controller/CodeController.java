package com.yjiang.base.generator.modular.controller;

import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.yjiang.base.core.common.exception.BizExceptionEnum;
import com.yjiang.base.generator.modular.factory.DefaultTemplateFactory;
import com.yjiang.base.generator.modular.service.TableService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.config.properties.DruidProperties;
import com.yjiang.base.generator.executor.config.WebGeneratorConfig;
import com.yjiang.base.generator.executor.model.GenQo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 代码生成控制器
 *
 * @author fengshuonan
 * @Date 2017年11月30日16:39:19
 */
@Controller
@RequestMapping("/code")
public class CodeController extends BaseController {

    private static String PREFIX = "/code";

    @Autowired
    private TableService tableService;

    @Autowired
    private DruidProperties druidProperties;

    /**
     * 跳转到代码生成主页
     */
    @RequestMapping("")
    public String blackboard(Model model) {
        model.addAttribute("tables", tableService.getAllTables());
        model.addAttribute("params", DefaultTemplateFactory.getDefaultParams());
        model.addAttribute("templates", DefaultTemplateFactory.getDefaultTemplates());
        return PREFIX + "/code.html";
    }

    /**
     * 生成代码
     */
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    @ResponseBody
    public Object generate(GenQo genQo) {
        if (ToolUtil.isOneEmpty(genQo, genQo.getModuleName())) {
            throw new ServiceException(BizExceptionEnum.ERROR_MODULE_NAME_EMPTY);
        }
        if (ToolUtil.isOneEmpty(genQo, genQo.getBizName())) {
            throw new ServiceException(BizExceptionEnum.ERROR_BIZ_NAME_EMPTY);
        }
        genQo.setUrl(druidProperties.getUrl());
        genQo.setUserName(druidProperties.getUsername());
        genQo.setPassword(druidProperties.getPassword());
        WebGeneratorConfig webGeneratorConfig = new WebGeneratorConfig(genQo);
        webGeneratorConfig.doMpGeneration();
        webGeneratorConfig.doGunsGeneration();
        return SUCCESS_TIP;
    }
}
