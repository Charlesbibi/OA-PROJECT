package com.oa.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charles.model.system.SysDept;

import java.util.List;

/**
 * @author Charles
 * @create 2023-06-23-0:53
 */
public interface SysDeptService extends IService<SysDept> {
    List<SysDept> findTreeNode();
}
