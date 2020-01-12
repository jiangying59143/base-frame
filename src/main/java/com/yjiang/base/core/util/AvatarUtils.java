package com.yjiang.base.core.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class AvatarUtils {

    public static String setAvatarPath(String bucketName, String avatar, HttpServletRequest request) {
        if(StringUtils.isEmpty(avatar)){
            return "";
        }
        if(avatar.startsWith("/static/user")) {
            return HttpUtils.getSystemUrl(request, null, null, avatar);
        }else if(avatar.startsWith("http")){
            return avatar;
        }else if(!StringUtils.isEmpty(avatar)){
            return AliyunOSSClientUtil.getPath(bucketName, avatar);
        }
        return "";
    }
    
}
