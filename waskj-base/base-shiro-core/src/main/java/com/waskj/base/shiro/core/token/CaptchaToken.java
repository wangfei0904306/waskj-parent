package com.waskj.base.shiro.core.token;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 验证码token
 */
public class CaptchaToken extends UsernamePasswordToken {

    private String captcha;

    private CaptchaToken(){}

    public CaptchaToken(String username,String password){
        this(username,password,null);
    }

    public CaptchaToken(String username,String password,String captcha){
        super(username,password);
        this.captcha = captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
