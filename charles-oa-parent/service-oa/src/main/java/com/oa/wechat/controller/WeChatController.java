package com.oa.wechat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.charles.model.system.SysUser;
import com.charles.vo.wechat.BindPhoneVo;
import com.oa.auth.service.impl.SysUserServiceImpl;
import com.oa.common.jwt.JWTHelper;
import com.oa.common.result.Result;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Charles
 * @create 2023-06-17-0:24
 */

@Controller
@RequestMapping("/admin/wechat")
@CrossOrigin
public class WeChatController {

    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private WxMpService wxMpService;

    @Value("${wechat.userInfoUrl}")
    private String InfoUrl;

    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl, HttpServletRequest request) {
        //设置回调URL
        /* 1. 获取info信息地址
           2. 定义URL类型，这里是UserInfo
           3. 回调地址
        * */
        System.out.println("InfoUrl  "  + InfoUrl);
        System.out.println("returnUrl " + returnUrl);
        String redirectUrl = "";
        try {
            redirectUrl = wxMpService.getOAuth2Service().buildAuthorizationUrl(InfoUrl,
                    WxConsts.OAuth2Scope.SNSAPI_USERINFO,
                    URLEncoder.encode(returnUrl.replace("guiguoa", "#"), "utf-8"));
            System.out.println("微信网页端回调地址"+redirectUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //跳转
        return "redirect:" + redirectUrl;
    }

    //获取微信用户信息
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) throws Exception {
        System.out.println("code " + code);
        System.out.println("returnUrl " + returnUrl);
        //获取认证token
        WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);
        //从token中获取openId
        String openId = accessToken.getOpenId();

        //查找user表中是否存在该openId
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<SysUser>();
        lambdaQueryWrapper.eq(SysUser::getOpenId,openId);
        SysUser sysUser = sysUserService.getOne(lambdaQueryWrapper);

        //最终前端都是通过token来判断
        String token = "";
        //null != sysUser 说明已经绑定，反之为建立账号绑定，去页面建立账号绑定
        if(null != sysUser) {
            token = JWTHelper.createToken(sysUser.getId(), sysUser.getUsername());
            System.out.println("token " + token);
        }
        if(returnUrl.indexOf("?") == -1) {
            return "redirect:" + returnUrl + "?token=" + token + "&openId=" + openId;
        } else {
            return "redirect:" + returnUrl + "&token=" + token + "&openId=" + openId;
        }
    }

    @ApiOperation(value = "微信账号绑定手机")
    @PostMapping("/bindPhone")
    @ResponseBody
    public Result bindPhone(@RequestBody BindPhoneVo bindPhoneVo) {
        String phone = bindPhoneVo.getPhone();

        //查找user表中是否有当前记录
        SysUser sysUser = sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhone, phone));

        if(null != sysUser){
            //如果用户存在则绑定当前手机号
            sysUser.setOpenId(bindPhoneVo.getOpenId());
            sysUserService.updateById(sysUser);
        }else
            return Result.fail("找不到当前号码，请联系管理员");

        return Result.success(JWTHelper.createToken(sysUser.getId(),sysUser.getUsername()));
    }
}
