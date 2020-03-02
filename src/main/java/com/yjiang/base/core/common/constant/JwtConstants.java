package com.yjiang.base.core.common.constant;

import java.util.Arrays;
import java.util.List;

/**
 * jwt相关配置
 *
 * @author yjiang
 * @date 2017-08-23 9:23
 */
public interface JwtConstants {
    String TOKEN_TYPE_REFRESH = "REFRESH_TOKEN";

    String TOKEN_TYPE_ACCESS = "ACCESS_TOKEN";

    String ACCESS_AUTH_HEADER = "Authorization";

    String SECRET = "yjiang-base-frame";

    Long ACCESS_TOKEN_EXPIRATION = 1800L;

    Long REFRESH_TOKEN_EXPIRATION = 604800L;

    List<String> AUTH_PATH = Arrays.asList("/userApi/register","/userApi/auth","/userApi/getTokenByFreshToken");

}
