package com.yjiang.base.core.util;


import com.yjiang.base.modular.system.model.User;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class HttpUtils {

    public static String getSystemUrl(HttpServletRequest request, String filePath, User user, String fileName) {
        String strBackUrl = "http://" + request.getServerName() + ":"
                + request.getServerPort()
                + request.getContextPath() + "/"
                + (StringUtils.isEmpty(filePath)? "": filePath+"/")
                + (user==null? "":user.getId() + "/")
                + fileName;
        return strBackUrl;
    }

}
