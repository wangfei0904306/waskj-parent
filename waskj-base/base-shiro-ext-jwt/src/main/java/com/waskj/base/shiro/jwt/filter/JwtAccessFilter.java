package com.waskj.base.shiro.jwt.filter;

import com.waskj.base.shiro.jwt.token.JwtToken;
import com.waskj.base.shiro.jwt.util.HttpUtils;
import com.waskj.base.shiro.jwt.util.JwtTokenHolder;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by poet on 2016/12/19.
 */
public class JwtAccessFilter extends AbstractJwtAuthenticatingFilter {

    protected static final String AUTHORIZATION_HEADER = "Authorization";

    public JwtAccessFilter() {}

    @Override
    public void setLoginUrl(String loginUrl) {
        String previous = getLoginUrl();
        if (previous != null) {
            this.appliedPaths.remove(previous);
        }
        super.setLoginUrl(loginUrl);
        this.appliedPaths.put(getLoginUrl(), null);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        boolean loggedIn = false;

        if(this.isEnableCors()){
            HttpServletRequest req = WebUtils.toHttp(request);
            if( HttpUtils.isOptionsRequest(req) ){
                return true;
            }
        }

        if (isLoginRequest(request, response) || isLoggedAttempt(request, response)) {
            loggedIn = executeLogin(request, response);
        }

        if (!loggedIn) {
            HttpServletResponse httpResponse = WebUtils.toHttp(response);
            if(this.isEnableCors()){
                HttpUtils.addCorsHeaders(httpResponse);
            }
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        return loggedIn;
    }


    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws IOException {
        AuthenticationToken token = null;

        if (isLoginRequest(request, response)) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            token = new UsernamePasswordToken(username,password);
        }

        if (isLoggedAttempt(request, response)) {
            HttpServletRequest req = WebUtils.toHttp(request);
            JwtTokenHolder.pushToken(getTokenParser().parseToken(req));
            token = new JwtToken(JwtTokenHolder.getToken());
        }

        return token;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        return false;
    }

    protected boolean isLoggedAttempt(ServletRequest request, ServletResponse response) {
        return getTokenParser().parseToken(WebUtils.toHttp(request)) != null;
    }

    protected String getAuthzHeader(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        return httpRequest.getHeader(AUTHORIZATION_HEADER);
    }

    public JwtToken createToken(String token) {
        return new JwtToken(token);
    }

}
