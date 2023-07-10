package com.oa.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charles.model.system.SysDept;
import com.charles.model.system.SysPost;
import com.charles.model.system.SysUser;
import com.oa.auth.mapper.SysDeptMapper;
import com.oa.auth.mapper.SysPostMapper;
import com.oa.auth.mapper.SysUserMapper;
import com.oa.auth.service.SysUserService;
import com.oa.security.custom.LoginUserInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles
 * @create 2023-04-22-14:38
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper,SysUser> implements SysUserService {

    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    SysPostMapper sysPostMapper;
    @Autowired
    SysDeptMapper sysDeptMapper;

    @Override
    public boolean changeStatusService(int status, Long id) {
        boolean isChange = sysUserMapper.changeStatusMapper(status, id);
        return isChange;
    }

    //获取当前用户的具体信息
    @Override
    public Map<String,Object> getCurrentUser() {
        Map<String,Object> map = new HashMap<>();
        SysUser user = sysUserMapper.getUserByUsername(LoginUserInfoHelper.getUsername());

        //获取岗位消息
        Long postId = user.getPostId();
        String postName = "暂无岗位";
        if(postId != null){
            SysPost sysPost = sysPostMapper.selectById(postId);
            postName = sysPost.getName();
        }

        //获取部门消息
        Long deptId = user.getDeptId();
        String deptName = "暂无部门";
        if(deptId != null){
            SysDept sysDept = sysDeptMapper.selectById(deptId);
            deptName = sysDept.getName();
        }

        //存放数据
        map.put("name", user.getName());
        map.put("phone", user.getPhone());
        map.put("postName", postName);
        map.put("deptName", deptName);

        return map;
    }

    @Override
    public Boolean allocatePostById(Long userId, Long postId) {
        SysUser sysUser = sysUserMapper.selectById(userId);
        sysUser.setPostId(postId);

        return sysUserMapper.updateById(sysUser) == 1;
    }
}
