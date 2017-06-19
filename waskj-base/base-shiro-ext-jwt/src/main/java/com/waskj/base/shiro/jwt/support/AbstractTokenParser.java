package com.waskj.base.shiro.jwt.support;

import com.waskj.base.shiro.jwt.JwtTokenParser;

/**
 * Created by poet on 2016/12/21.
 */
public abstract class AbstractTokenParser implements JwtTokenParser {


    // token param name, default token
    protected String tokenName = "token";

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }
}
