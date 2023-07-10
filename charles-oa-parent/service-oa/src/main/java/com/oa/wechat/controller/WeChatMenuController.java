package com.oa.wechat.controller;

import com.charles.model.wechat.Menu;
import com.oa.common.result.Result;
import com.oa.wechat.service.impl.WeChatMenuServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author Charles
 * @create 2023-06-14-23:53
 */

@RestController
@RequestMapping("/admin/wechat/menu")
@Slf4j
@CrossOrigin
public class WeChatMenuController {

    @Autowired
    private WeChatMenuServiceImpl weChatMenuService;

    //@PreAuthorize("hasAuthority('bnt.menu.syncMenu')")
    @ApiOperation(value = "同步菜单")
    @GetMapping("syncMenu")
    public Result createMenu() {
        weChatMenuService.syncMenu();
        return Result.success();
    }

    //@PreAuthorize("hasAuthority('bnt.menu.removeMenu')")
    @ApiOperation(value = "删除菜单")
    @DeleteMapping("removeMenu")
    public Result removeMenu() {
        weChatMenuService.removeMenu();
        return Result.success();
    }

    //@PreAuthorize("hasAuthority('bnt.menu.list')")
    @ApiOperation(value = "获取全部菜单")
    @GetMapping("findMenuInfo")
    public Result findMenuInfo(){
        return Result.success(weChatMenuService.findMenuInfo());
    }

    //@PreAuthorize("hasAuthority('bnt.menu.list')")
    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        Menu menu = weChatMenuService.getById(id);
        return Result.success(menu);
    }

    //@PreAuthorize("hasAuthority('bnt.menu.add')")
    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody Menu menu) {
        weChatMenuService.save(menu);
        return Result.success();
    }

    //@PreAuthorize("hasAuthority('bnt.menu.update')")
    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody Menu menu) {
        weChatMenuService.updateById(menu);
        return Result.success();
    }

    //@PreAuthorize("hasAuthority('bnt.menu.remove')")
    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        weChatMenuService.removeById(id);
        return Result.success();
    }
}
