package com.waskj.base.api.core;

import com.waskj.base.domain.core.UserEntity;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by poet on 2016/12/19.
 */
public interface ShiroSecurityService {


    /**
     * 判断账号是否锁定
     * @param userEntity
     * @return  true 账号锁定 </br>
     *          false 账号未锁定
     */
    boolean isAccountLocked(UserEntity userEntity);

    /**
     * 判断账号是否禁用
     * @param userEntity
     * @return  true 账号禁用 </br>
     *          false 账号未禁用
     */
    boolean isAccountDisabled(UserEntity userEntity);

    /**
     * 根据id获取账号信息
     * @param id
     * @return
     */
    UserEntity getUserEntityById(Serializable id);

    /**
     * 根据id获取账号信息
     * @param email
     * @return
     */
    UserEntity getUserEntityByEmail(String email);

    /**
     * 根据id获取账号信息
     * @param phone
     * @return
     */
    UserEntity getUserEntityByPhone(String phone);

    /**
     * 根据账号获取账户信息
     * @param account
     * @return
     */
    UserEntity getUserEntityByAccount(String account);

    /**
     * 获取用户所有角色
     * @param userEntity
     * @return
     */
    Collection<String> getUserRoles(UserEntity userEntity);

    /**
     * 获取用户所有权限
     * @param userEntity
     * @return
     */
    Collection<String> getUserPermissions(UserEntity userEntity);

}
