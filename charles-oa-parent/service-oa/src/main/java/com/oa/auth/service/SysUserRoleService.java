package com.oa.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charles.model.system.SysUserRole;

import java.util.List;

/**
 * @author Charles
 * @create 2023-04-25-20:12
 */
public interface SysUserRoleService extends IService<SysUserRole> {
    //获取指定用户的角色
    List<Long> getAllRole(Long id);
    //角色分配
    void assignRole(Long uid, Long rid);
}
