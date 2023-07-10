package com.oa.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charles.model.system.SysMenu;
import com.charles.model.system.SysRoleMenu;

import java.util.List;

/**
 * @author Charles
 * @create 2023-05-02-11:41
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {
    List<Long> getAssign(Long id);

    void deleteRoleMenu(Long id, Long id1);
}
