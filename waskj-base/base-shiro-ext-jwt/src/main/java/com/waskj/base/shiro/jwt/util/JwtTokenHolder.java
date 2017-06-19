package com.waskj.base.shiro.jwt.util;

import org.springframework.util.StringUtils;

/**
 * Created by poet on 2016/12/20.
 */
public abstract class JwtTokenHolder {

    private static ThreadLocal<String> tokenHolder = new ThreadLocal<>();

    public static void pushToken(String token){
        if(StringUtils.hasText(token))
            tokenHolder.set(token);
    }

    public static String getToken(){
        return tokenHolder.get();
    }

    public static void remove(){
        tokenHolder.remove();
    }


}
