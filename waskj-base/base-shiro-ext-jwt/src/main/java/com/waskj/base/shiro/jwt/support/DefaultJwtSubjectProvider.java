package com.waskj.base.shiro.jwt.support;

import com.waskj.base.core.util.UtilMisc;
import com.waskj.base.domain.core.UserEntity;
import com.waskj.base.shiro.jwt.JwtSubjectProvider;
import org.apache.shiro.authc.AuthenticationToken;

import java.util.Map;

/**
 * Created by poet on 2016/12/20.
 */
public class DefaultJwtSubjectProvider implements JwtSubjectProvider {
    @Override
    public Map providePublicSubject(UserEntity user, AuthenticationToken token) {
        return UtilMisc.toMap("username",user.getUserName());
    }

    @Override
    public Map providePrivateSubject(UserEntity user, AuthenticationToken token) {
        return UtilMisc.toMap("id",user.getId(),"userLogin",token.getPrincipal());
    }
}
