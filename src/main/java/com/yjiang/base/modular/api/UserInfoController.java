package com.yjiang.base.modular.api;

import com.yjiang.base.core.shiro.ShiroKit;
import com.yjiang.base.core.shiro.ShiroUser;
import com.yjiang.base.core.util.JwtTokenUtil;
import com.yjiang.base.lottery.CaipiaoService;
import com.yjiang.base.lottery.DoubleColorBall;
import com.yjiang.base.modular.system.model.User;
import com.yjiang.base.modular.system.service.IUserService;
import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.reqres.response.ErrorResponseData;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 用户接口
 *
 * @author yjiang
 * @Date 2020/01/21
 */
@RestController
@RequestMapping("/user")
public class UserInfoController extends BaseController {

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
     * api登录接口，通过电话密码获取token
     */
    @ApiOperation(value="根据电话获得用户信息", notes="根据电话获得用户信息")
    @RequestMapping(value="/getByPhone", method = RequestMethod.POST)
    public Object getUserInfo(@RequestParam("phone") String phone) {
        User user = userService.getByPhone(phone);
        return user;
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
            result.put("token", JwtTokenUtil.generateToken(String.valueOf(user.getId())));
            return result;
        } else {
            return new ErrorResponseData(500, "账号密码错误！");
        }
    }

}

