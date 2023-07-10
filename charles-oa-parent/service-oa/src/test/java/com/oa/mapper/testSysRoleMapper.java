package com.oa.mapper;

import com.charles.model.system.SysRole;
import com.oa.auth.mapper.SysRoleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Charles
 * @create 2023-03-31-11:36
 */
@SpringBootTest
public class testSysRoleMapper {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Test
    public void test01(){
        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
        System.out.println(sysRoles);
    }
}
