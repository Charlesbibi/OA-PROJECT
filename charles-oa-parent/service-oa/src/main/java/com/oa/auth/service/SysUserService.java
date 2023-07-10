package com.oa.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charles.model.system.SysUser;

import java.util.Map;

/**
 * @author Charles
 * @create 2023-04-22-14:58
 */
public interface SysUserService extends IService<SysUser> {
    //更改用户的状态
    boolean changeStatusService(int status, Long id);

    Map<String,Object> getCurrentUser();

    //分配岗位
    Boolean allocatePostById(Long userId, Long postId);
}
