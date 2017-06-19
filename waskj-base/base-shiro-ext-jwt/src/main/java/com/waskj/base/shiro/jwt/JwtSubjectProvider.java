package com.waskj.base.shiro.jwt;

import com.waskj.base.domain.core.UserEntity;
import org.apache.shiro.authc.AuthenticationToken;

import java.util.Map;

/**
 * jwt subject 数据域 提供
 * Created by poet on 2016/12/20.
 */
public interface JwtSubjectProvider {

    /**
     * 提供公共部分
     * @return
     */
    Map providePublicSubject(UserEntity user, AuthenticationToken token);

    /**
     * 提供私有部分
     * @param user
     * @param token
     * @return
     */
    Map providePrivateSubject(UserEntity user, AuthenticationToken token);

}
