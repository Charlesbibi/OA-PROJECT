package com.oa.wechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charles.model.wechat.Menu;
import com.charles.vo.wechat.MenuVo;

import java.util.List;

/**
 * @author Charles
 * @create 2023-06-14-23:44
 */

public interface WeChatMenuService extends IService<Menu> {
    List<MenuVo> findMenuInfo();

    void syncMenu();

    void removeMenu();
}
