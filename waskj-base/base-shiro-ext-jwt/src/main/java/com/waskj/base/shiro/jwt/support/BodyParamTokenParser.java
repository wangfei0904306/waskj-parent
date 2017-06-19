package com.waskj.base.shiro.jwt.support;

import javax.servlet.http.HttpServletRequest;

/**
 * parse token only by param body
 * Created by poet on 2016/12/21.
 */
public class BodyParamTokenParser extends AbstractTokenParser {
    @Override
    public String parseToken(HttpServletRequest request) {
        return request.getParameter(tokenName);
    }
}
