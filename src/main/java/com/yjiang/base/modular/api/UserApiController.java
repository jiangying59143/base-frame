package com.yjiang.base.modular.api;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ErrorResponseData;
import cn.stylefeng.roses.core.reqres.response.SuccessResponseData;
import com.yjiang.base.core.common.constant.JwtConstants;
import com.yjiang.base.core.shiro.ShiroKit;
import com.yjiang.base.core.shiro.ShiroUser;
import com.yjiang.base.core.util.JwtTokenUtil;
import com.yjiang.base.modular.system.model.User;
import com.yjiang.base.modular.system.service.IUserService;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户接口
 *
 * @author yjiang
 * @Date 2020/01/21
 */
@RestController
@RequestMapping("/userApi")
public class UserApiController extends BaseController {

    @Autowired
    private IUserService userService;

    /**
     * api登录接口，通过电话密码获取token
     */
    @ApiOperation(value="电话登陆", notes="电话密码登陆")
    @RequestMapping(value="/phoneAuth", method = RequestMethod.POST)
    public Object phoneAuth(@RequestParam("phone") String phone,
                       @RequestParam("password") String password) {

        User user = userService.getByPhone(phone);
        return auth(user.getAccount(), password);
    }

    /**
     *
     * @param
     * @return
     */
    @ApiOperation(value="通过refreshToken获取token", notes="通过refreshToken获取token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "令牌(Bearer )", required = true, dataType = "String", paramType = "header")
    })
    @RequestMapping(value="/getTokenByFreshToken", method = RequestMethod.POST)
    public Object getTokenByFreshToken(HttpServletRequest request) {
        final String requestHeader = request.getHeader(JwtConstants.ACCESS_AUTH_HEADER);
        String authToken = requestHeader.substring(7);
        String userId = JwtTokenUtil.getUsernameFromToken(authToken);
        User user = userService.selectById(userId);
        if(JwtTokenUtil.getIssuedAtDateFromToken(authToken).compareTo(user.getLoginTime()) == 0){
            HashMap<String, Object> result = new HashMap<>();
            Date date = new Date();
            user.setLoginTime(date);
            userService.updateById(user);
            result.put("refresh_token", JwtTokenUtil.generateToken(JwtConstants.TOKEN_TYPE_REFRESH, String.valueOf(userId), date, JwtConstants.REFRESH_TOKEN_EXPIRATION));
            result.put("token", JwtTokenUtil.generateToken(JwtConstants.TOKEN_TYPE_ACCESS, String.valueOf(userId), date, JwtConstants.ACCESS_TOKEN_EXPIRATION));
            return new SuccessResponseData(200, "成功", result);
        }else{
            return new ErrorResponseData(700, "信息已过期");
        }
    }

    /**
     * api登录接口，通过电话密码获取token
     */
    @ApiOperation(value="根据电话获得用户信息", notes="根据电话获得用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "令牌(Bearer )", required = true, dataType = "String", paramType = "header")
    })
    @RequestMapping(value="/getByPhone", method = RequestMethod.POST)
    public Object getUserInfo(@RequestParam("phone") String phone) {
        User user = userService.getByPhone(phone);
        return user;
    }

    @ApiOperation(value="用户注册", notes="用户注册")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "0:成功"),
            @ApiResponse(code = 403, message = "20005:用户已存在", response=Map.class),
            @ApiResponse(code = 201, message = "1:失败")})
    @PostMapping("/register")
    public Object register(String username, String password, String email) {
        if(StringUtils.isEmpty(username)|| StringUtils.isEmpty(password) || StringUtils.isEmpty(email)){
            return new ErrorResponseData(406, "参数有误！");
        }

        User user = userService.getByAccount(username);
        if (null != user) {
            return new ErrorResponseData(20005, "用户已经存在！");
        }

        User user1 = new User();
        user1.setAccount(username);
        user1.setPassword(password);
        user1.setRoleid("6");
        user1.setEmail(email);
        userService.saveUser(user1);
        return new SuccessResponseData(200, "注册成功", null);
    }

    /**
     * api登录接口，通过账号密码获取token
     */
    @ApiOperation(value="用户名密码登陆", notes="用户名密码登陆")
    @RequestMapping(value="/auth", method = RequestMethod.POST)
    public Object auth(@RequestParam("username") String username,
                       @RequestParam("password") String password) {

        //封装请求账号密码为shiro可验证的token
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password.toCharArray());

        //获取数据库中的账号密码，准备比对
        try {
            User user = userService.getByAccount(username);

            String credentials = user.getPassword();
            String salt = user.getAccount() + user.getSalt();
            ByteSource credentialsSalt = ByteSource.Util.bytes(salt);
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                    new ShiroUser(), credentials, credentialsSalt, "");

            //校验用户账号密码
            HashedCredentialsMatcher md5CredentialsMatcher = new HashedCredentialsMatcher();
            md5CredentialsMatcher.setHashAlgorithmName(ShiroKit.hashAlgorithmName);
            md5CredentialsMatcher.setHashIterations(ShiroKit.hashIterations);
            boolean passwordTrueFlag = md5CredentialsMatcher.doCredentialsMatch(
                    usernamePasswordToken, simpleAuthenticationInfo);

            if (passwordTrueFlag) {
                HashMap<String, Object> result = new HashMap<>();
                Date date = new Date();
                result.put("refresh_token", JwtTokenUtil.generateToken(JwtConstants.TOKEN_TYPE_REFRESH, String.valueOf(user.getId()), date, JwtConstants.REFRESH_TOKEN_EXPIRATION));
                result.put("token", JwtTokenUtil.generateToken(JwtConstants.TOKEN_TYPE_ACCESS, String.valueOf(user.getId()), date, JwtConstants.ACCESS_TOKEN_EXPIRATION));
                result.put("user", user);
                user.setLoginTime(date);
                userService.updateById(user);
                return new SuccessResponseData(200, "登录成功", result);
            } else {
                return new ErrorResponseData(500, "账号密码错误！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ErrorResponseData(500, "账号密码错误！");
        }
    }



}

