package com.waskj.base.shiro.jwt.support;

import com.waskj.base.core.util.jwt.JwtUtil;
import com.waskj.base.domain.core.UserEntity;
import com.waskj.base.shiro.core.LoginLogoutService;
import com.waskj.base.shiro.jwt.JwtSubjectProvider;
import com.waskj.base.shiro.jwt.util.JwtTokenHolder;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;

/**
 * Created by poet on 2016/12/20.
 */
public class JwtLoginLogoutServiceImpl implements LoginLogoutService {

    private Logger log = LoggerFactory.getLogger(LoginLogoutService.class);

    private JwtSubjectProvider subjectProvider;

    private long expireTime = 30 * 60 * 1000;

    @Override
    public void login(AuthenticationToken token) throws AuthenticationException {
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
        // after login, bind jwt token to subject
        if( token instanceof UsernamePasswordToken){
            UserEntity user = subject.getPrincipals().oneByType(UserEntity.class);
            if( user != null ){
                Map priMap = subjectProvider.providePrivateSubject(user,token);
                Map pubMap = subjectProvider.providePublicSubject(user,token);

                priMap = priMap == null ? Collections.emptyMap() : priMap;
                pubMap = pubMap == null ? Collections.emptyMap() : pubMap;

                try {
                    String tokenStr = JwtUtil.createJWT(priMap,pubMap,expireTime);
                    user.setToken(tokenStr);
                    JwtTokenHolder.pushToken(tokenStr);
                } catch (Exception e) {
                    log.error("生成JWT Token失败",e);
                }
            }
        }
    }

    @Override
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

    public JwtSubjectProvider getSubjectProvider() {
        return subjectProvider;
    }

    public void setSubjectProvider(JwtSubjectProvider subjectProvider) {
        this.subjectProvider = subjectProvider;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
