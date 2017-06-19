package com.waskj.base.shiro.core.support;

import com.waskj.base.domain.core.UserEntity;
import com.waskj.base.shiro.core.ShiroCacheKeyProvider;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * Created by poet on 2016/12/20.
 */
public class DefaultShiroCacheKeyProvider implements ShiroCacheKeyProvider {

    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 认证名称
     */
    private String authcName;
    /**
     * 认证名称
     */
    private String authzName;
    private String separator = ":";

    @Override
    public Object getAuthorizationCacheKey(PrincipalCollection principals) {
        UserEntity user = principals.oneByType(UserEntity.class);
        Serializable id = user.getId();
        return getKeyName(this.projectName,this.authzName,String.valueOf(id),this.separator);
    }

    @Override
    public Object getAuthenticationCacheKey(AuthenticationToken token) {
        String val = token == null ? null : String.valueOf(token.getPrincipal());
        return val == null ? null : getKeyName(projectName,authcName,val,separator);
    }

    @Override
    public Object getAuthenticationCacheKey(PrincipalCollection principals) {
        UserEntity user = principals.oneByType(UserEntity.class);
        Serializable id = user.getId();
        return getKeyName(this.projectName,this.authcName,String.valueOf(id),this.separator);
    }

    @Override
    public Object getNewAuthorizationCacheKey(Object val) {
        return getKeyName(this.projectName,this.authzName,String.valueOf(val),this.separator);
    }

    @Override
    public Object getNewAuthenticationCacheKey(Object val) {
        return getKeyName(this.projectName,this.authcName,String.valueOf(val),this.separator);

    }

    protected String getKeyName(String project,String type,String val,String separator){
        StringBuilder sb = new StringBuilder();
        if(StringUtils.hasText(project))
            sb.append(project).append(separator);
        if( StringUtils.hasText(type) )
            sb.append(type).append(separator);

        sb.append(val); // val must not null
        return sb.toString();
    }

}
