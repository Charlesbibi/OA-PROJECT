package com.oa.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charles.model.system.SysRoleMenu;
import com.oa.auth.mapper.SysRoleMenuMapper;
import com.oa.auth.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Charles
 * @create 2023-05-02-11:41
 */

@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu>
        implements SysRoleMenuService {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<Long> getAssign(Long id) {
        return sysRoleMenuMapper.getAssignById(id);
    }

    @Override
    public void deleteRoleMenu(Long id, Long id1) {
        sysRoleMenuMapper.deleteRoleMenu(id,id1);
    }
}
