package com.waskj.base.shiro.core.support;

import com.waskj.base.shiro.core.LoginLogoutService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;

/**
 * Created by poet on 2016/12/20.
 */
public class DefaultLoginLogoutServiceImpl implements LoginLogoutService {

    @Override
    public void login(AuthenticationToken token) throws AuthenticationException {
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
    }

    @Override
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

}
