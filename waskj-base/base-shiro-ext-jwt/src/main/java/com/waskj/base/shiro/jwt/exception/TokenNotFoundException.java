package com.waskj.base.shiro.jwt.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * Created by poet on 2016/12/20.
 */
public class TokenNotFoundException extends AuthenticationException {

    public TokenNotFoundException(){
        super("Token must not be null or empty!");
    }

}
