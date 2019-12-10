package com.osen.aqms.webSecurity.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.osen.aqms.modules.entity.system.Role;
import com.osen.aqms.modules.entity.system.User;
import com.osen.aqms.modules.service.UserService;
import com.osen.aqms.webSecurity.utils.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User: PangYi
 * Date: 2019-08-29
 * Time: 15:48
 * Description: 用户授权服务层接口实现类
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    /**
     * 根据用户名验证用户是否有效
     *
     * @param username 用户名
     * @return 认证
     * @throws UsernameNotFoundException 异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (BeanUtil.isEmpty(user) || StringUtils.isEmpty(user.getAccount()))
            throw new UsernameNotFoundException("Account does not exist");
        if (user.getStatus() == 0) {
            throw new UsernameNotFoundException("Account does not user");
        }
        // 封装用户对应角色
        List<SimpleGrantedAuthority> authorityList = user.getRoles().stream().map(Role::getRoleName).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return new JwtUser(user.getId(), user.getAccount(), user.getPassword(), authorityList);
    }
}
