package com.oa.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.charles.model.system.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Charles
 * @create 2023-04-22-14:35
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    //更改用户的状态
    boolean changeStatusMapper(int status, Long id);
    //根据用户名获取用户
    SysUser getUserByUsername(String username);
}
