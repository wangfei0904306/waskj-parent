package com.waskj.base.shiro.core.realm;

import com.waskj.base.domain.core.UserEntity;
import com.waskj.base.shiro.core.ShiroCacheKeyProvider;
import com.waskj.base.api.core.ShiroSecurityService;
import com.waskj.base.shiro.core.util.SerializableByteSource;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.StringUtils;

/**
 * Created by poet on 2016/12/19.
 */
public abstract class AbstractServiceRealm extends AuthorizingRealm {

    protected ShiroSecurityService securityService;
    protected ShiroCacheKeyProvider cacheKeyProvider;

    @Override
    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        return cacheKeyProvider.getAuthorizationCacheKey(principals);
    }

    @Override
    protected Object getAuthenticationCacheKey(AuthenticationToken token) {
        return cacheKeyProvider.getAuthenticationCacheKey(token);
    }

    @Override
    protected Object getAuthenticationCacheKey(PrincipalCollection principals) {
        return cacheKeyProvider.getAuthenticationCacheKey(principals);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo retInfo = new SimpleAuthorizationInfo();
        UserEntity user = principals.oneByType(UserEntity.class);
        if (null != user) {
            retInfo.addRoles(this.securityService.getUserRoles(user));
            retInfo.addStringPermissions(this.securityService.getUserPermissions(user));
        }

        return retInfo;
    }

    protected ByteSource getPasswordSalt(UserEntity user) {
        ByteSource ret = null;
        if (StringUtils.hasText(user.getSalt())) {
            ret = new SerializableByteSource(user.getSalt());
        }
        return ret;
    }

    protected void doCheckUser(UserEntity user) {
        if (user == null)
            throw new UnknownAccountException(); // 如果用户名错误

        // 检测账号是否锁定
        if (securityService.isAccountLocked(user))
            throw new LockedAccountException();

        // 检测账号是否禁用
        if (securityService.isAccountDisabled(user))
            throw new DisabledAccountException();

        doOtherLoginCheck(user);
    }

    /**
     * 当检测到用户没有锁定，也没有禁用后，执行的其他检查，具体检查项与业务相关
     * <br/>
     * 一旦检查初步不允许登录的情况，可手动抛出 AuthenticationException 异常
     *
     * @throws AuthenticationException
     */
    protected void doOtherLoginCheck(UserEntity user) throws AuthenticationException {}

    public ShiroSecurityService getSecurityService() {
        return securityService;
    }

    public void setSecurityService(ShiroSecurityService securityService) {
        this.securityService = securityService;
    }

    public ShiroCacheKeyProvider getCacheKeyProvider() {
        return cacheKeyProvider;
    }

    public void setCacheKeyProvider(ShiroCacheKeyProvider cacheKeyProvider) {
        this.cacheKeyProvider = cacheKeyProvider;
    }
}
