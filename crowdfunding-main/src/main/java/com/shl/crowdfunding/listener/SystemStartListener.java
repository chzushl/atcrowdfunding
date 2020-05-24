package com.shl.crowdfunding.listener;

import com.shl.crowdfunding.bean.Permission;
import com.shl.crowdfunding.manager.service.PermissionService;
import com.shl.crowdfunding.util.Const;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SystemStartListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        String contextPath = servletContext.getContextPath();
        //System.out.println(contextPath);
        servletContext.setAttribute("APP_PATH",contextPath);

        /*再application域中存放所有所有permission对象*/
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        PermissionService permissionService = (PermissionService) applicationContext.getBean(PermissionService.class);
        List<Permission> allPermissions = permissionService.getAllallPermissions();
        Set<String> allUris = new HashSet<>();
        for (Permission allPermission : allPermissions) {
            allUris.add("/"+allPermission.getUrl());
        }
        servletContext.setAttribute(Const.ALL_PERMISSION_URI,allUris);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
