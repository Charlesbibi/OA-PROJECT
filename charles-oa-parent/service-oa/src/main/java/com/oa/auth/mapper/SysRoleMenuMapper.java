package com.oa.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.charles.model.system.SysMenu;
import com.charles.model.system.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Charles
 * @create 2023-05-02-11:40
 */

@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
    //根据id获取角色id集合
    List<Long> getAssignById(Long id);
    //删除role与menu对应关系
    void deleteRoleMenu(Long id, Long id1);
    //获取所有菜单
    List<SysMenu> getAllMenuById(@Param("userId") Long userId);
}
