package com.waskj.base.shiro.core;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created by poet on 2016/12/20.
 */
public interface LoginLogoutService {

    /**
     * 登录
     * @param token
     * @throws AuthenticationException
     */
    void login(AuthenticationToken token) throws AuthenticationException;

    /**
     * 退出
     */
    void logout();
}
