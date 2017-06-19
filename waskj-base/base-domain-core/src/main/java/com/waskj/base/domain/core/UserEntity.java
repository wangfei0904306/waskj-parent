package com.waskj.base.domain.core;

import java.io.Serializable;

/**
 *  用户实体类接口抽象
 */
public interface UserEntity<ID extends Serializable> extends Serializable{

    ID getId();

    void setId(ID id);

    String getUserLogin();

    void setUserLogin(String userLogin);

    String getUserName();

    void setUserName(String userName);

    String getPassword();

    void setPassword(String password);

    String getSalt();

    void setSalt(String salt);

    String getEmail();

    void setEmail(String email);

    String getPhoneNumber();

    void setPhoneNumber(String phoneNumber);

    String getToken();
    void setToken(String token);

//    String getIsActive();
//
//    void setIsActive(String isActive);

//    String getUserTypeCode();
//
//    void setUserTypeCode(String userTypeCode);
}
