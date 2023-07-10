package com.oa.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charles.model.system.SysUserRole;
import com.oa.auth.mapper.SysUserRoleMapper;
import com.oa.auth.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Charles
 * @create 2023-04-25-20:13
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Autowired
    SysUserRoleMapper sysUserRoleMapper;

    //获取指定用户的角色的id
    @Override
    public List<Long> getAllRole(Long id) {
        List<Long> idLists = sysUserRoleMapper.getAllRoleIDbyUserId(id);
        return idLists;
    }

    @Override
    public void assignRole(Long uid, Long rid) {
        sysUserRoleMapper.doAssign(uid,rid);
    }
}
