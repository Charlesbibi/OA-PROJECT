package com.oa.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.charles.model.system.SysUser;
import com.charles.vo.system.LoginVo;
import com.charles.vo.system.RouterVo;
import com.oa.auth.service.impl.SysUserServiceImpl;
import com.oa.common.MD5.MD5Helper;
import com.oa.common.exception.OAException;
import com.oa.common.jwt.JWTHelper;
import com.oa.common.result.Result;
import com.oa.auth.service.impl.SysMenuServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles
 * @create 2023-04-10-22:30
 */

@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private SysMenuServiceImpl sysMenuService;
    /**
     * 登录
     * @return
     */
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) throws Exception{
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getUsername,loginVo.getUsername());
        SysUser sysUser = sysUserService.getOne(lambdaQueryWrapper);
        if(sysUser == null)
            throw new OAException("找不到该用户");

        //加密密码来比较
        String encryptValue = MD5Helper.encrypt(loginVo.getPassword());
        if(!StringUtils.pathEquals(encryptValue,sysUser.getPassword()))
            throw new OAException("密码错误");
        //判断状态
        if(sysUser.getStatus() == 0)
            throw new OAException("该用户已被禁用");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+encryptValue);
        //存放token到请求头中
        Map<String, Object> map = new HashMap<>();
        String token = JWTHelper.createToken(sysUser.getId(), sysUser.getUsername());
        map.put("token",token);
        return Result.success(map);
    }
    /**
     * 获取用户信息
     * @return
     */
    @PostMapping("info")
    public Result info(HttpServletRequest request) {
        String token = request.getHeader("token");

        //获取用户id
        Long userId = JWTHelper.getUserId(token);
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getId,userId);
        SysUser sysUser = sysUserService.getOne(lambdaQueryWrapper);

        //根据id获取所有菜单列表
        List<RouterVo> routerList = sysMenuService.getAllRouterListByUserId(userId);

        //根据id获取所有按钮列表
        List<String> permsList = sysMenuService.getAllMenuListByUserId(userId);

        //map中插入相应的值
        Map<String, Object> map = new HashMap<>();
        map.put("routers",routerList);
        map.put("buttons",permsList);
        map.put("roles",sysUser.getRoleList());
        map.put("name",sysUser.getName());
        //默认头像
        String avatar = StringUtils.isEmpty(sysUser.getHeadUrl()) ? "https://avatars.githubusercontent.com/u/93185368?s=200&v=4" : sysUser.getHeadUrl();
        map.put("avatar",avatar);

        return Result.success(map);
    }
    /**
     * 退出
     * @return
     */
    @PostMapping("logout")
    public Result logout(){
        return Result.success();
    }
}
