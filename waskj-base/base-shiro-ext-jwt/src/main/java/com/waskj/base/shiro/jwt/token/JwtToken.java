package com.waskj.base.shiro.jwt.token;

import com.waskj.base.core.util.Constants;
import com.waskj.base.core.util.jwt.JwtUtil;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Created by poet on 2016/12/19.
 */
public class JwtToken implements AuthenticationToken {

    private String jwtToken;

    private JwtToken(){}

    public JwtToken(String jwtToken){
        this.jwtToken = jwtToken;
    }

    @Override
    public Object getPrincipal() {
        String principal = "_NOT_AUTHC_";

        if(!JwtUtil.checkToken(this.jwtToken))
            return principal;

        Map map = JwtUtil.getPriMap(jwtToken);
        String userLogin = String.valueOf(map.get(Constants.USER_LOGIN_KEY));

        if(StringUtils.hasText(userLogin))
            principal = userLogin;

        return principal;
    }

    @Override
    public Object getCredentials() {
        return this.jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}
