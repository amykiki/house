package com.april.house.web.controller;

import com.april.house.biz.service.UserService;
import com.april.house.common.constants.CommonConstants;
import com.april.house.common.model.User;
import com.april.house.common.result.ResultMsg;
import com.april.house.common.util.UserHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/accounts")
public class UserController {
    Logger logger = LogManager.getLogger(UserController.class);

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
        try {
            if (resultMsg.isSuccess() && userService.addAccount(account)) {
                modelMap.put("email", account.getEmail());
                return "/user/accounts/registerEmail";
            } else {
                return CommonConstants.REDIRECT + "/accounts/register?" + resultMsg.asUrlParams();
            }
        } catch (Exception e) {
            logger.error("注册用户失败", e);
        }
        return CommonConstants.REDIRECT + "/accounts/register?" + ResultMsg.errorMsg("注册失败").asUrlParams();
    }

    @RequestMapping("/verify")
    public String verify(String key) {
        boolean result = userService.enableUser(key);
        if (result) {
            return CommonConstants.REDIRECT + "/index?" + ResultMsg.successMsg("激活成功").asUrlParams();
        } else {
            return CommonConstants.REDIRECT + "/accounts/register?" + ResultMsg.errorMsg("激活失败，请确认链接是否过期").asUrlParams();
        }
    }

    @RequestMapping("/getresetcache")
    @ResponseBody
    public String getResetCaches() {
        String caches = userService.getResetCaches();
        return caches;
    }

    @RequestMapping("/delresetcache")
    @ResponseBody
    public String invalidResetCache(String key) {
        return userService.invalidResetCache(key);
    }

    @RequestMapping("/getregcache")
    @ResponseBody
    public String getRegisterCaches() {
        String caches = userService.getRegisterCaches();
        return caches;
    }

    @RequestMapping("/delregcache")
    @ResponseBody
    public String invalidRegisterCache(String key) {
        return userService.invalidRegisterCache(key);
    }

// ----------------------------登录流程------------------------------------

    @RequestMapping ("/signin")
    public String signin(HttpServletRequest req) {
        String email = req.getParameter("username");
        String password = req.getParameter("password");
        String target = req.getParameter("target");

        if (email == null || password == null) {
            //返回视图，内部是用了forward的形式
            //因此如果本次请求经过了拦截器，那么下次请求就不会再经过拦截器了
            //target就不会被authInterceptor处理，不会被自动放入request attribute中
            //因此这里手动加入
            req.setAttribute("target", target);
            return "/user/accounts/signin";
        }

        //校验用户名
        User user = userService.auth(email, password);
        if (user == null) {
            //redirect是不同请求，因此会再次通过拦截器
            //不需要手动把参数放入req attribute中
            return CommonConstants.REDIRECT + "/accounts/signin?"
                    + "target=" +target
                    + "&email=" + email
                    + "&" + ResultMsg.errorMsg("用户名或密码错误").asUrlParams();
        }else {
            HttpSession session = req.getSession(true);
            session.setAttribute(CommonConstants.LOGIN_USER_ATTRIBUTE, user);
            return StringUtils.isNotBlank(target) ? CommonConstants.REDIRECT + target : CommonConstants.REDIRECT + "/index";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        session.invalidate();
        return CommonConstants.REDIRECT + "/index";
    }

    // ---------------------个人信息页-------------------------

    @RequestMapping("/profile")
    public String profile(HttpServletRequest request, User updateUser) {
        //进入视图
        if (updateUser.getEmail() == null) {
            return "/user/accounts/profile";
        }

        userService.updateUser(updateUser, updateUser.getEmail());
        User query = new User();
        query.setEmail(updateUser.getEmail());
        List<User> users = userService.getUserByQuery(query);
        request.getSession(true).setAttribute(CommonConstants.LOGIN_USER_ATTRIBUTE, users.get(0));
        return CommonConstants.REDIRECT + "/accounts/profile?" + ResultMsg.successMsg("更新成功").asUrlParams();
    }

    @RequestMapping("/changePassword")
    public String changePassword(String email, String password, String newPassword, String confirmPassword) {
        User user = userService.auth(email, password);
        if (user == null || !confirmPassword.equals(newPassword)) {
            return CommonConstants.REDIRECT + "/accounts/profile?" + ResultMsg.errorMsg("密码错误").asUrlParams();
        }
        boolean result = userService.updatePassword(newPassword, email);
        if (result) {
            return CommonConstants.REDIRECT + "/accounts/profile?" + ResultMsg.successMsg("更新成功").asUrlParams();
        } else {
            return CommonConstants.REDIRECT + "/accounts/profile?" + ResultMsg.errorMsg("更新失败").asUrlParams();
        }
    }

    @RequestMapping("/remember")
    public String remember(String username, ModelMap modelMap) {
        if (StringUtils.isBlank(username)) {
            return CommonConstants.REDIRECT + "/accounts/signin?" + ResultMsg.errorMsg("邮箱不能为空").asUrlParams();
        }
        userService.resetNotify(username);
        modelMap.put("email", username);
        return "/user/accounts/remember";
    }

    @RequestMapping("/reset")
    public String reset(String key, ModelMap modelMap) {
        String email = userService.getResetEmail(key);
        if (StringUtils.isBlank(email)) {
            return CommonConstants.REDIRECT + "/accounts/signin?" + ResultMsg.errorMsg("重置链接已失效").asUrlParams();
        }
        modelMap.put("email", email);
        modelMap.put("sucess_key", key);
        return "/user/accounts/reset";
    }

    @RequestMapping(value = "/resetSubmit")
    public String resestSubmit(HttpServletRequest req, User user) {
        ResultMsg retMsg = UserHelper.validateResetPassword(user.getKey(), user.getPasswd(), user.getConfirmPasswd());
        if (!retMsg.isSuccess()) {
            String suffix = "";
            if (StringUtils.isNotBlank(user.getKey())) {
                suffix = "email=" + userService.getResetEmail(user.getKey()) + "&key=" + user.getKey() + "&";
            }
            return CommonConstants.REDIRECT + "/accounts/reset?" + suffix + retMsg.asUrlParams();
        }

        User updateUser = userService.resetPassword(user.getKey(), user.getPasswd());
        req.getSession(true).setAttribute(CommonConstants.LOGIN_USER_ATTRIBUTE, updateUser);
        return CommonConstants.REDIRECT + "/index?" + ResultMsg.successMsg("重置密码成功").asUrlParams();
    }


}
