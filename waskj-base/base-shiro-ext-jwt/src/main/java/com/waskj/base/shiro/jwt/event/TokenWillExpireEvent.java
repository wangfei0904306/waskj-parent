package com.waskj.base.shiro.jwt.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by poet on 2016/12/20.
 */
public class TokenWillExpireEvent extends ApplicationEvent {

    private String oldToken;
    private String newToken;

    public TokenWillExpireEvent(Object source) {
        super(source);
    }

    public TokenWillExpireEvent(String oldToken,String newToken){
        super(newToken);
        this.oldToken = oldToken;
        this.newToken = newToken;
    }

    public String getOldToken() {
        return oldToken;
    }

    public String getNewToken() {
        return newToken;
    }
}
