package com.waskj.base.shiro.jwt.filter;

import com.waskj.base.core.util.Constants;
import com.waskj.base.core.util.jwt.JwtUtil;
import com.waskj.base.shiro.jwt.JwtTokenParser;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by poet on 2016/12/20.
 */
public class JwtTimeoutFilter extends AccessControlFilter implements ApplicationEventPublisherAware {

    // when to check the token, default 5 minutes
    private long diffTimeToCheck = 5 * 60 * 1000;

    // jwt token cache expire time
    private long jwtTokenExpireTime = 30 * 60 * 1000;

    private String newTokenName = "auth_token";

    private JwtTokenParser tokenParser;

    private ApplicationEventPublisher publisher;

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {

        String newToken = null;

        HttpServletRequest request = WebUtils.toHttp(servletRequest);
        HttpServletResponse response = WebUtils.toHttp(servletResponse);
        String token = tokenParser.parseToken(request);

        if(JwtUtil.validationJWT(token)){ // verify token

            // verify exp time
            long diffTimes = JwtUtil.compareJWTExpWithNow(token);
            if( diffTimes <= diffTimeToCheck && diffTimes > 0 ){
                newToken = JwtUtil.updateJWTExp(token, this.jwtTokenExpireTime);
//                publisher.publishEvent(new TokenWillExpireEvent(token,newToken));
                // after cache done,send new token to client
                response.addHeader(newTokenName,newToken);
            }
        }

        return true; // just check the token expire,so this always return true
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return false;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public JwtTokenParser getTokenParser() {
        return tokenParser;
    }

    public void setTokenParser(JwtTokenParser tokenParser) {
        this.tokenParser = tokenParser;
    }

    public long getDiffTimeToCheck() {
        return diffTimeToCheck;
    }

    public void setDiffTimeToCheck(long diffTimeToCheck) {
        this.diffTimeToCheck = diffTimeToCheck;
    }

    public long getJwtTokenExpireTime() {
        return jwtTokenExpireTime;
    }

    public void setJwtTokenExpireTime(long jwtTokenExpireTime) {
        this.jwtTokenExpireTime = jwtTokenExpireTime;
    }

    public String getNewTokenName() {
        return newTokenName;
    }

    public void setNewTokenName(String newTokenName) {
        this.newTokenName = newTokenName;
    }
}
