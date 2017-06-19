package com.waskj.base.shiro.jwt.support;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by poet on 2016/12/20.
 */
public class DefaultTokenParser extends AbstractTokenParser {

    @Override
    public String parseToken(HttpServletRequest request) {
        String token = request.getHeader(getTokenName());
        if(!StringUtils.hasText(token))
            token = request.getParameter(getTokenName());
        return token;
    }

}
