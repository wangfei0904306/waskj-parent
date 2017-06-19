package com.waskj.base.shiro.core.filter;

import org.apache.shiro.web.servlet.AbstractFilter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by poet on 2016/12/23.
 */
public abstract class AbstractCleanupFilter extends AbstractFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request,response);

        this.doCleanup();
    }

    protected abstract void doCleanup();


}
