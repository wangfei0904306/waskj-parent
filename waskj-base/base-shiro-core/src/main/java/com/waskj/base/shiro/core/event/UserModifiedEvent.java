package com.waskj.base.shiro.core.event;

import com.waskj.base.domain.core.UserEntity;
import org.springframework.context.ApplicationEvent;

/**
 * 用户信息修改事件
 */
public class UserModifiedEvent extends ApplicationEvent {

    private UserEntity modifiedUser;

    public UserModifiedEvent(UserEntity modifiedUser) {
        super(modifiedUser);
        this.modifiedUser = modifiedUser;
    }

    public UserEntity getModifiedUser() {
        return modifiedUser;
    }
}
