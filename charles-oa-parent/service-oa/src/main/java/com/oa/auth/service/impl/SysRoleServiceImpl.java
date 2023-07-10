package com.oa.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charles.model.system.SysRole;
import com.oa.auth.service.SysRoleService;
import com.oa.auth.mapper.SysRoleMapper;
import org.springframework.stereotype.Service;

/**
 * @author Charles
 * @create 2023-04-01-11:19
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper,SysRole> implements SysRoleService {
}
