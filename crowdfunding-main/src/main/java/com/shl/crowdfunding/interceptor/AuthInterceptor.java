package com.shl.crowdfunding.interceptor;

import com.shl.crowdfunding.util.Const;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String servletPath = request.getServletPath();
        Set<String> allUris = (Set<String>) request.getSession().getServletContext().getAttribute(Const.ALL_PERMISSION_URI);
        if(allUris.contains(servletPath)){
            Set<String> myUris = (Set<String>) request.getSession().getAttribute(Const.MY_URIS);
                if(myUris.contains(servletPath)){
                    return true;
                }else {
                    response.sendRedirect(request.getContextPath()+"/login.htm");
                    return false;
                }
        }else {
            return true;
        }
    }
}
