package com.oa.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charles.model.system.SysDept;
import com.oa.auth.mapper.SysDeptMapper;
import com.oa.auth.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charles
 * @create 2023-06-23-0:54
 */

@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    //获取层级数据
    @Override
    public List<SysDept> findTreeNode() {
        //获取到全部原生数据
        List<SysDept> sysDepts = sysDeptMapper.selectList(null);

        SysDept treeNodes = createTree(sysDepts,sysDepts.get(0));

        //封装为集合 规定 0 为根结点
        List<SysDept> ones = new ArrayList<>();
        ones.add(treeNodes);
        return ones;
    }

    private SysDept createTree(List<SysDept> sysDepts, SysDept parentNode) {
        //存放子结点
        List<SysDept> twos = new ArrayList<>();
        for(SysDept sysDept : sysDepts){
            if(sysDept.getParentId().longValue() == parentNode.getId()) {
                //递归添加数据
                twos.add(createTree(sysDepts, sysDept));
            }
        }

        if(twos.size() == 0)
            parentNode.setChildren(null);
        else
            parentNode.setChildren(twos);
        return parentNode;
    }
}
