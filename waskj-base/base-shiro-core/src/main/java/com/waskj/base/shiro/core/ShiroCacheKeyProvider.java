package com.waskj.base.shiro.core;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * Created by poet on 2016/12/20.
 */
public interface ShiroCacheKeyProvider {

    /**
     * 获取权限缓存key
     * @param principals
     * @return
     */
    Object getAuthorizationCacheKey(PrincipalCollection principals);

    /**
     * 认证缓存key(登录判断)
     * @param token
     * @return
     */
    Object getAuthenticationCacheKey(AuthenticationToken token);

    /**
     * 认证缓存key(根据用户)
     * @param principals
     * @return
     */
    Object getAuthenticationCacheKey(PrincipalCollection principals);

    Object getNewAuthorizationCacheKey(Object val);
    Object getNewAuthenticationCacheKey(Object val);

}
