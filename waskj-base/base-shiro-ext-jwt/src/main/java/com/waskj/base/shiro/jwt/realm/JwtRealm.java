package com.waskj.base.shiro.jwt.realm;

import com.waskj.base.core.util.jwt.JwtUtil;
import com.waskj.base.domain.core.UserEntity;
import com.waskj.base.shiro.core.realm.AbstractServiceRealm;
import com.waskj.base.shiro.jwt.event.TokenWillExpireEvent;
import com.waskj.base.shiro.jwt.exception.TokenInvalidException;
import com.waskj.base.shiro.jwt.token.JwtToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.subject.Subject;
import org.springframework.context.ApplicationListener;

/**
 * Created by poet on 2016/12/19.
 */
public class JwtRealm extends AbstractServiceRealm implements ApplicationListener<TokenWillExpireEvent> {

    @Override
    protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws AuthenticationException {
        // do nothing
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        throw new AuthenticationException("Jwt login just use cache!");
        JwtToken jwtToken = (JwtToken)authenticationToken;
        String token = jwtToken.getJwtToken();

        // validation the token
        doCheckToken(token);

        AuthenticationInfo retInfo = null;

        AuthenticationInfo cachedInfo = null;

        if( super.isCachingEnabled() && super.isAuthenticationCachingEnabled() ){
            // get cache by token
            Object cacheKey = super.cacheKeyProvider.getAuthenticationCacheKey(authenticationToken);
            cachedInfo = super.getAuthenticationCache().get(cacheKey);
        }

        if( cachedInfo == null ){
            // if we do not hit the cache,the call onCacheMiss(),
            // this method do nothing by default,subclass can implement this
            cachedInfo = onCacheMiss(authenticationToken);
        }

        if( cachedInfo != null ){
            retInfo = this.copyAuthenticationInfo(token,cachedInfo);
        }

        return retInfo;
    }

    protected Object getCacheKey(String token){
        return super.cacheKeyProvider.getNewAuthenticationCacheKey(token);
    }

    /**
     * when cache miss,realm will invoke this method
     * @return
     */
    protected AuthenticationInfo onCacheMiss(AuthenticationToken token){
        return null;
    }

    /**
     * 验证Token
     * @param token
     */
    protected void doCheckToken(String token){
        if(!JwtUtil.checkToken(token))
            throw new TokenInvalidException();
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof JwtToken;
    }

    @Override
    public void onApplicationEvent(TokenWillExpireEvent event) {
        String newToken = event.getNewToken();
        this.addNewTokenFromCache(newToken);
    }

    protected void addNewTokenFromCache(String newToken){
        if (super.isCachingEnabled()) {
            Subject subject = SecurityUtils.getSubject();
            if (super.isAuthenticationCachingEnabled()) {
                Object authcKey = super.getAuthenticationCacheKey(subject.getPrincipals());
                Cache<Object, AuthenticationInfo> authcCache = super.getAuthenticationCache();
                AuthenticationInfo cachedInfo = authcCache.get(authcKey);

                AuthenticationInfo copiedInfo = this.copyAuthenticationInfo(newToken,cachedInfo);

                if (copiedInfo != null) {
                    authcCache.put(super.cacheKeyProvider.getNewAuthenticationCacheKey(newToken),copiedInfo);
                }
            }

            if( super.isAuthorizationCachingEnabled() ) {
                Object authzKey = super.getAuthorizationCacheKey(subject.getPrincipals());
                Cache<Object, AuthorizationInfo> authzCache = super.getAuthorizationCache();
                if( authzCache != null ){
                    authzCache.put(super.cacheKeyProvider.getNewAuthorizationCacheKey(newToken),authzCache.get(authzKey));
                }
            }
        }
    }

    private AuthenticationInfo copyAuthenticationInfo(String token,AuthenticationInfo oldInfo){
        AuthenticationInfo retInfo = null;
        if( oldInfo == null )
            return retInfo;

        Object principal = oldInfo.getPrincipals().oneByType(UserEntity.class);

        if( principal == null )
            return retInfo;

        retInfo = new SimpleAuthenticationInfo(principal,token,getName());
        return retInfo;
    }
}
