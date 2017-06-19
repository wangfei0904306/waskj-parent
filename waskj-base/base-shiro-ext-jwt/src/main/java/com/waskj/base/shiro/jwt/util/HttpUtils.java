package com.waskj.base.shiro.jwt.util;

import org.apache.shiro.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by poet on 2016/12/28.
 */
public abstract class HttpUtils {

    public static void addCorsHeaders(HttpServletResponse response){
        response.addHeader("Access-Control-Allow-Methods","POST, GET, PUT, OPTIONS, DELETE");
        response.addHeader("Access-Control-Allow-Credentials","true");
        response.addHeader("Access-Control-Expose-Headers","auth_token");
        response.addHeader("Access-Control-Allow-Origin","*");
    }

    public static boolean isOptionsRequest(HttpServletRequest request){
        String method = request.getMethod();
        return "OPTIONS".equalsIgnoreCase(method);
    }

}
