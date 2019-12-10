package com.osen.aqms.webSecurity.handler;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.osen.aqms.common.result.RestResult;
import com.osen.aqms.common.utils.RedisOpsUtil;
import com.osen.aqms.webSecurity.utils.JwtTokenUtil;
import com.osen.aqms.webSecurity.utils.JwtUser;
import com.osen.aqms.webSecurity.utils.TransferUserToJwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.osen.aqms.common.enums.InfoMessage.User_Login_Success;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 9:07
 * Description: 用户登录成功时，返回给前端数据
 */
@Component
@Slf4j
public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("User login successful......");

        // 登录成功，逻辑处理代码....
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        //生成token
        String token = jwtTokenUtil.generateToken(jwtUser);

        TransferUserToJwt transferUserToJwt = JwtTokenUtil.toUser(jwtUser);

        // 登录令牌
        String randomUUID = IdUtil.randomUUID();
        redisOpsUtil.putToMap(JwtTokenUtil.LOGIN_TOKEN, randomUUID, token);
        // 登录主体信息
        String principal = JSON.toJSONString(transferUserToJwt);
        redisOpsUtil.putToMap(JwtTokenUtil.ACCESS_TOKEN, token, principal);

        response.setContentType("application/json;charset=utf-8");

        // 返回信息体
        RestResult<String> restResult = new RestResult<>();
        restResult.setCode(User_Login_Success.getCode());
        restResult.setMessage(User_Login_Success.getMessage());
        restResult.setData(randomUUID);

        response.getWriter().write(JSON.toJSONString(restResult));
    }
}
