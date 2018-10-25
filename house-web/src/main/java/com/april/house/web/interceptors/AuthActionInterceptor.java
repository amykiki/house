package com.april.house.web.interceptors;

import com.april.house.common.model.User;
import com.april.house.common.result.ResultMsg;
import com.april.house.common.util.UserContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * @Description :
 * @Creation Date:  2018-10-24 13:23
 * --------  ---------  --------------------------
 */
@Component
public class AuthActionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = UserContext.getUser();
        if (user == null) {
            ResultMsg errorMsg = ResultMsg.errorMsg("请先登录");
            String target = URLEncoder.encode(request.getRequestURL().toString(), "utf-8");
            if ("GET".equalsIgnoreCase(request.getMethod())){
                response.sendRedirect("/accounts/signin?"+errorMsg.asUrlParams() + "&target=" + target);
            }else {
                response.sendRedirect("/accounts/signin?"+errorMsg);
            }
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
