package com.waskj.base.shiro.jwt.support;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * parse token only by cookie
 * Created by poet on 2016/12/21.
 */
public class CookieTokenParser extends AbstractTokenParser {


    @Override
    public String parseToken(HttpServletRequest request) {
        String retToken = null;
        Cookie[] cookies = request.getCookies();

        if( cookies != null ){
            for (Cookie c : cookies) {
                if( tokenName.equals(c.getName()) ){
                    retToken = c.getValue();
                    break;
                }
            }
        }
        return retToken;
    }
}
