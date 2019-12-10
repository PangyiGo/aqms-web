package com.osen.aqms.webSecurity.config;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.osen.aqms.common.exception.type.ServiceException;
import com.osen.aqms.common.utils.RedisOpsUtil;
import com.osen.aqms.webSecurity.utils.JwtTokenUtil;
import com.osen.aqms.webSecurity.utils.JwtUser;
import com.osen.aqms.webSecurity.utils.TransferUserToJwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.osen.aqms.common.enums.InfoMessage.User_Login_Guoqi;

/**
 * User: PangYi
 * Date: 2019-12-09
 * Time: 18:17
 * Description:
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 获取请求头
        String header = request.getHeader(JwtTokenUtil.TOKEN_HEADER);

        String username = null;
        String authToken = null;
        String jwtToken = null;
        if (StringUtils.isNotEmpty(header) && header.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
            // 获取登录令牌-随机数
            authToken = header.substring(7);
            boolean hashKeyToMap = redisOpsUtil.hashKeyToMap(JwtTokenUtil.LOGIN_TOKEN, authToken);
            if (hashKeyToMap) {
                // 获取访问令牌，并判断令牌是否过期
                jwtToken = redisOpsUtil.getToMap(JwtTokenUtil.LOGIN_TOKEN, authToken);
                if (!jwtTokenUtil.isTokenExpired(jwtToken)) {
                    try {
                        username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                    } catch (Exception e) {
                        log.error("Token parse failed");
                    }
                } else {
                    // 删除无效令牌
                    redisOpsUtil.deleteToMap(JwtTokenUtil.LOGIN_TOKEN, authToken);
                    redisOpsUtil.deleteToMap(JwtTokenUtil.ACCESS_TOKEN, jwtToken);
                    throw new ServiceException(User_Login_Guoqi.getCode(), User_Login_Guoqi.getMessage());
                }
            }
        }

        if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            boolean key = redisOpsUtil.hashKeyToMap(JwtTokenUtil.ACCESS_TOKEN, jwtToken);
            // 通过访问令牌，获取登录认证主体信息
            if (key) {
                // 从 redis 缓存中获取认证主体
                String jwtUserJson = redisOpsUtil.getToMap(JwtTokenUtil.ACCESS_TOKEN, jwtToken);

                TransferUserToJwt transferUserToJwt = JSON.parseObject(jwtUserJson, TransferUserToJwt.class);

                assert transferUserToJwt != null;
                JwtUser userDetails = JwtTokenUtil.toJwt(transferUserToJwt);

                if (userDetails != null) {
                    // 验证token是否有效
                    if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                        // 刷新令牌
                        String refreshToken = jwtTokenUtil.refreshToken(jwtToken);
                        // 登录令牌
                        redisOpsUtil.putToMap(JwtTokenUtil.LOGIN_TOKEN, authToken, refreshToken);
                        // 删除原来的访问令牌，更新访问令牌
                        redisOpsUtil.deleteToMap(JwtTokenUtil.ACCESS_TOKEN, jwtToken);
                        redisOpsUtil.putToMap(JwtTokenUtil.ACCESS_TOKEN, refreshToken, jwtUserJson);

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        // 删除无效令牌
                        redisOpsUtil.deleteToMap(JwtTokenUtil.LOGIN_TOKEN, authToken);
                        redisOpsUtil.deleteToMap(JwtTokenUtil.ACCESS_TOKEN, jwtToken);
                        throw new ServiceException(User_Login_Guoqi.getCode(), User_Login_Guoqi.getMessage());
                    }
                }
            }
        }

        // 请求过滤器链
        filterChain.doFilter(request, response);
    }
}
