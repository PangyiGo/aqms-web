package com.osen.aqms.webSecurity.config;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.osen.aqms.webSecurity.utils.JwtTokenUtil;
import com.osen.aqms.webSecurity.utils.JwtUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    @Qualifier("jwtUserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 获取请求头
        String header = request.getHeader(JwtTokenUtil.TOKEN_HEADER);

        String username = null;
        String authToken;
        if (StringUtils.isNotEmpty(header) && header.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
            // 获取登录令牌-jwtToken
            authToken = header.substring(7);
            try {
                username = jwtTokenUtil.jiemi2username(authToken);
            } catch (Exception e) {
                log.error("Token parse failed");
            }
        }

        if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 从数据库获取用户信息
            JwtUser userDetails = (JwtUser) this.userDetailsService.loadUserByUsername(username);

            // Boolean key = stringRedisTemplate.hasKey(JwtTokenUtil.ACCESS_TOKEN + authToken);
            // // 通过访问令牌，获取登录认证主体信息
            // if (key != null && key) {
            //     // 从 redis 缓存中获取认证主体
            //     String jwtUserJson = stringRedisTemplate.boundValueOps(JwtTokenUtil.ACCESS_TOKEN + authToken).get();
            //
            //     TransferUserToJwt transferUserToJwt = JSON.parseObject(jwtUserJson, TransferUserToJwt.class);
            //
            //     assert transferUserToJwt != null;
            //     JwtUser userDetails = JwtTokenUtil.toJwt(transferUserToJwt);

            if (userDetails != null) {
                // 验证token是否有效
                if (username.equals(userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                // }
            }
        }

        // 请求过滤器链
        filterChain.doFilter(request, response);
    }
}
