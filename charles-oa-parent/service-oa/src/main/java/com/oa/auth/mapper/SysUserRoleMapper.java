package com.oa.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.charles.model.system.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Charles
 * @create 2023-04-25-20:10
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
    //角色查询
    List<Long> getAllRoleIDbyUserId(Long id);
    //角色分配
    void doAssign(Long uid, Long rid);
}
