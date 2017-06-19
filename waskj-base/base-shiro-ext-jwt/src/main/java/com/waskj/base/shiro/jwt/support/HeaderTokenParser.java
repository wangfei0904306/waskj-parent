package com.waskj.base.shiro.jwt.support;

import javax.servlet.http.HttpServletRequest;

/**
 * parse token only in header
 * Created by poet on 2016/12/21.
 */
public class HeaderTokenParser extends AbstractTokenParser {
    @Override
    public String parseToken(HttpServletRequest request) {
        return request.getHeader(tokenName);
    }
}
