package com.waskj.base.shiro.core.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Created by poet on 2016/12/20.
 */
public class CustomCodeFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        Subject subject = this.getSubject(servletRequest, servletResponse);
        return subject.isAuthenticated();
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject subject = this.getSubject(servletRequest, servletResponse);
        if (subject.getPrincipal() == null) {
            WebUtils.toHttp(servletResponse).sendError(401);
        } else {
            WebUtils.toHttp(servletResponse).sendError(403);
        }
        return false;
    }
}
