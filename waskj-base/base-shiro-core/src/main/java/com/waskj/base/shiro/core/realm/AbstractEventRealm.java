package com.waskj.base.shiro.core.realm;

import com.waskj.base.domain.core.UserEntity;
import com.waskj.base.shiro.core.event.UserModifiedEvent;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.context.ApplicationListener;

/**
 * Created by poet on 2016/12/26.
 */
public abstract class AbstractEventRealm extends AbstractServiceRealm implements ApplicationListener<UserModifiedEvent> {


    @Override
    public void onApplicationEvent(UserModifiedEvent userModifiedEvent) {
        if( userModifiedEvent.getModifiedUser() == null )
            return;
        UserEntity user = userModifiedEvent.getModifiedUser();

        String userLogin = user.getUserLogin();

        this.doUpdateAuthenticationCache(user);
        this.doUpdateAuthorizationCache(user);
    }


    protected AuthenticationInfo generateNewAuthenticationCache(AuthenticationInfo oldInfo, Object principal){
        SimpleAuthenticationInfo old = (SimpleAuthenticationInfo)oldInfo;
        SimpleAuthenticationInfo newInfo = new SimpleAuthenticationInfo();

        SimplePrincipalCollection spc = new SimplePrincipalCollection();
        spc.add(principal,getName());

        newInfo.setCredentials(old.getCredentials());
        newInfo.setCredentialsSalt(old.getCredentialsSalt());
        newInfo.setPrincipals(spc);

        return newInfo;
    }

    protected AuthorizationInfo generateNewAuthorizationCache(UserEntity user){
        SimpleAuthorizationInfo newInfo = new SimpleAuthorizationInfo();
        newInfo.addRoles(super.securityService.getUserRoles(user));
        newInfo.addStringPermissions(super.securityService.getUserPermissions(user));
        return newInfo;
    }

    private void doUpdateAuthenticationCache(UserEntity user){
        if( super.isAuthenticationCachingEnabled() ){
            // authentication cache enabled
            String cacheKey = String.valueOf(super.cacheKeyProvider.getNewAuthenticationCacheKey(user.getUserLogin()));
            AuthenticationInfo cachedInfo = super.getAuthenticationCache().get(cacheKey);
            AuthenticationInfo newCache = this.generateNewAuthenticationCache(cachedInfo,user);
            super.getAuthenticationCache().put(cacheKey,newCache);
        }
    }

    private void doUpdateAuthorizationCache(UserEntity user){
        if( super.isAuthorizationCachingEnabled() ) {
            // authorization cache enabled
            String cacheKey = String.valueOf(super.cacheKeyProvider.getNewAuthorizationCacheKey(user.getUserLogin()));
            AuthorizationInfo newCache = this.generateNewAuthorizationCache(user);
            super.getAuthorizationCache().put(cacheKey,newCache);
        }
    }
}
