package com.waskj.base.shiro.jwt.support;

import com.waskj.base.core.util.Constants;
import com.waskj.base.core.util.jwt.JwtUtil;
import com.waskj.base.shiro.core.ShiroCacheKeyProvider;
import com.waskj.base.shiro.jwt.util.JwtTokenHolder;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Created by poet on 2016/12/20.
 */
public class JwtShiroCacheKeyProvider implements ShiroCacheKeyProvider {

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
//        UserEntity user = principals.oneByType(UserEntity.class);
//        String val = user.getToken();
        String val = getUserLogin();

        return getKeyName(this.projectName,this.authzName,val,this.separator);
    }

    @Override
    public Object getAuthenticationCacheKey(AuthenticationToken token) {
        String val = String.valueOf(token.getPrincipal());
        return val == null ? null : getKeyName(projectName,authcName,val,separator);
    }

    @Override
    public Object getAuthenticationCacheKey(PrincipalCollection principals) {
        String val = getUserLogin();
        return getKeyName(this.projectName,this.authcName,val,this.separator);
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAuthcName() {
        return authcName;
    }

    public void setAuthcName(String authcName) {
        this.authcName = authcName;
    }

    public String getAuthzName() {
        return authzName;
    }

    public void setAuthzName(String authzName) {
        this.authzName = authzName;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    private String getUserLogin(){
        String token = JwtTokenHolder.getToken();
        Map priMap = JwtUtil.getPriMap(token);
        return String.valueOf(priMap.get(Constants.USER_LOGIN_KEY));
    }
}
