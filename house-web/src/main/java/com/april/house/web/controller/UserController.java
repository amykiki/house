package com.april.house.web.controller;

import com.april.house.biz.service.UserService;
import com.april.house.common.model.User;
import com.april.house.common.result.ResultMsg;
import com.april.house.common.util.UserHelper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accounts")
public class UserController {

    @Autowired
    private UserService userService;

    // ----------------------------注册流程------------------------------------
    /**
     * 注册提交
     * 1. 注册验证
     * 2. 发送邮件
     * 3. 验证失败重定向到注册页面
     * 注册页获取
     * 根据account对象为依据判断是否是注册页获取请求
     * @param account
     * @param modelMap
     * @return
     */
    @RequestMapping("/register")
    public String accountsRegister(User account, ModelMap modelMap) {
        if (account == null || account.getName() == null) {
            //返回注册页面
            return "/user/accounts/register";
        }

        //用户验证
        ResultMsg resultMsg = UserHelper.validate(account);
        if (resultMsg.isSuccess() && userService.addAccount(account)) {
            modelMap.put("email", account.getEmail());
            return "/user/accounts/registerEmail";
        } else {
            return "redirect:/accounts/register?" + resultMsg.asUrlParams();
        }
    }

    @RequestMapping("/verify")
    public String verify(String key) {
        boolean result = userService.enableUser(key);
        if (result) {
            return "redirect:/index?" + ResultMsg.successMsg("激活成功").asUrlParams();
        } else {
            return "redirect:/accounts/register?" + ResultMsg.errorMsg("激活失败，请确认链接是否过期").asUrlParams();
        }
    }




}
