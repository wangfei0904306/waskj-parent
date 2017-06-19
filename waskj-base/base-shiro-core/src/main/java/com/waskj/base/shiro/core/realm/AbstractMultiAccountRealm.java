package com.waskj.base.shiro.core.realm;

import com.waskj.base.domain.core.UserEntity;
import org.apache.shiro.authc.*;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.StringUtils;

/**
 * Created by poet on 2016/12/19.
 */
public abstract class AbstractMultiAccountRealm extends AbstractServiceRealm {

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        AuthenticationInfo info = null;

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // 通过表单接收的用户名
        String username = token.getUsername();
        if (StringUtils.hasText(username)) {

            UserEntity user = doGetUserEntity(username);

            super.doCheckUser(user);

            // 成功获取用户信息，开始密码校验
            // 生成盐值
            ByteSource bs = this.getPasswordSalt(user);

            info = new SimpleAuthenticationInfo(user, user.getPassword(), bs, getName());

        }
        return info;
    }

    /**
     * 根据账号获取用户信息
     * @param account
     * @return
     */
    protected abstract UserEntity doGetUserEntity(String account);

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof UsernamePasswordToken;
    }

}
