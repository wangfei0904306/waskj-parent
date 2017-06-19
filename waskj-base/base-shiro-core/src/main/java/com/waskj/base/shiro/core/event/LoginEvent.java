package com.waskj.base.shiro.core.event;

import com.waskj.base.domain.core.UserEntity;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.context.ApplicationEvent;

/**
 * Created by poet on 2016/12/26.
 */
public class LoginEvent extends ApplicationEvent {

    private LoginType loginType;
    private UserEntity userEntity;
    private AuthenticationToken usedToken;

    public LoginEvent(LoginType loginType, UserEntity userEntity, AuthenticationToken usedToken) {
        super(LoginEvent.class);
        this.loginType = loginType;
        this.userEntity = userEntity;
        this.usedToken = usedToken;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public AuthenticationToken getUsedToken() {
        return usedToken;
    }

    public static enum LoginType {
        LOGIN_SUCCESS,
        LOGIN_FAIL
    }
}
