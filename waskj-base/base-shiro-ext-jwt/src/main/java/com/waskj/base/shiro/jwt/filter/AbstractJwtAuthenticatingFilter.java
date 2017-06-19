package com.waskj.base.shiro.jwt.filter;

import com.waskj.base.shiro.jwt.JwtTokenParser;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;

/**
 * Created by poet on 2016/12/28.
 */
public abstract class AbstractJwtAuthenticatingFilter extends AuthenticatingFilter {


    protected JwtTokenParser tokenParser;
    protected boolean enableCors;

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
