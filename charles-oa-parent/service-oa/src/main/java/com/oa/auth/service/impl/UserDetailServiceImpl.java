package com.oa.auth.service.impl;

import com.charles.model.system.SysUser;
import com.oa.auth.mapper.SysUserMapper;
import com.oa.security.custom.CustomUser;
import com.oa.security.custom.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charles
 * @create 2023-05-24-2:52
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysMenuServiceImpl sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserMapper.getUserByUsername(username);

        if(null == sysUser) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        if(sysUser.getStatus().intValue() == 0) {
            throw new RuntimeException("账号已停用");
        }

        List<String> buttonList = sysMenuService.getAllMenuListByUserId(sysUser.getId());
        List<SimpleGrantedAuthority> authority = new ArrayList<SimpleGrantedAuthority>();
        for (String button : buttonList) {
            authority.add(new SimpleGrantedAuthority(button));
        }

        return new CustomUser(sysUser, authority);
    }
}
