package com.waskj.base.shiro.jwt.filter;

import com.waskj.base.shiro.jwt.JwtTokenParser;
import com.waskj.base.shiro.jwt.util.HttpUtils;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by poet on 2016/12/28.
 */
public class JwtTokenCheckoutFilter extends AccessControlFilter {

    protected JwtTokenParser tokenParser;
    protected boolean enableCors;

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        if(this.isEnableCors()){
            HttpServletRequest req = WebUtils.toHttp(servletRequest);
            if( HttpUtils.isOptionsRequest(req) ){
                return true;
            }
        }

        HttpServletRequest request = WebUtils.toHttp(servletRequest);
        return StringUtils.hasText(tokenParser.parseToken(request));
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        if(this.isEnableCors()){
            HttpServletRequest req = WebUtils.toHttp(servletRequest);
            if( HttpUtils.isOptionsRequest(req) ){
                return true;
            }
        }

        HttpServletResponse httpResponse = WebUtils.toHttp(servletResponse);
        if(this.isEnableCors()){
            HttpUtils.addCorsHeaders(httpResponse);
        }
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        return false;
    }

    public JwtTokenParser getTokenParser() {
        return tokenParser;
    }

    public void setTokenParser(JwtTokenParser tokenParser) {
        this.tokenParser = tokenParser;
    }

    public boolean isEnableCors() {
        return enableCors;
    }

    public void setEnableCors(boolean enableCors) {
        this.enableCors = enableCors;
    }

}
