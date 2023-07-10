package com.oa.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charles.model.system.SysPost;
import com.oa.auth.mapper.SysPostMapper;
import com.oa.auth.service.SysPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Charles
 * @create 2023-06-23-1:17
 */

@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements SysPostService {

    @Autowired
    private SysPostMapper sysPostMapper;

    @Override
    public void changeStatus(Long id) {
        SysPost sysPost = sysPostMapper.selectById(id);
        sysPost.setStatus(sysPost.getStatus() == 1? 0 : 1);
        sysPostMapper.updateById(sysPost);
    }
}
