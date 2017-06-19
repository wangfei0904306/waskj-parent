package com.waskj.base.shiro.jwt;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by poet on 2016/12/20.
 */
public interface JwtTokenParser {

    String parseToken(HttpServletRequest request);

}
