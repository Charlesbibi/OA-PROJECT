package com.oa.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charles.model.system.SysPost;

/**
 * @author Charles
 * @create 2023-06-23-1:16
 */
public interface SysPostService extends IService<SysPost> {
    void changeStatus(Long id);
}
