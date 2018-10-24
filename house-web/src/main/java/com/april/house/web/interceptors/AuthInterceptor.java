package com.april.house.web.interceptors;

import com.april.house.common.constants.CommonConstants;
import com.april.house.common.model.User;
import com.april.house.common.result.ResultMsg;
import com.april.house.common.util.UserContext;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author :  ZouShumin
 * @Project Name :  house
 * @Package Name :  com.april.house.web.interceptors
 * @Description :
 * @Creation Date:  2018-10-24 11:41
 * --------  ---------  --------------------------
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String[]> map = request.getParameterMap();
        //将返回参数添加到请求attribute中，方便页面获取属性值
        map.forEach((k, v) -> {
            if(k.equals(ResultMsg.errorMsgKey) || k.equals(ResultMsg.successMsgKey) || k.equals("target")) {
                request.setAttribute(k, Joiner.on(",").join(v));
            }
        });
        //略过静态文件或错误页面请求
        String reqUri = request.getRequestURI();
        if(reqUri.startsWith("/static") ||
           reqUri.startsWith("/error")) {
            return true;
        }

        HttpSession session = request.getSession(true);
        User loginUser = (User) session.getAttribute(CommonConstants.LOGIN_USER_ATTRIBUTE);
        if (loginUser != null) {
            //当前请求中加入已登录用户
            //主要用于页面显示已登录用户属性
            //或者AuthActionInterceptor中判断用户是否已登录
            UserContext.setUser(loginUser);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //在当前请求结束后，删除ThreadLocal中缓存的登录user对象
        UserContext.removeUser();
    }
}
