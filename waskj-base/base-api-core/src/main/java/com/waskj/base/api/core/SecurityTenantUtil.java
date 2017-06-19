package com.waskj.base.api.core;

import com.waskj.base.domain.core.ObTenantEntity;
import com.waskj.base.domain.core.TenantEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 权限工具类(租户)
 *
 * @author sungang
 * @date 2017-03-13
 */
public abstract class SecurityTenantUtil extends SecurityUtil {


    private static Logger logger = LoggerFactory.getLogger(SecurityTenantUtil.class);

    /**
     * 获取租户信息
     * @return
     */
    public static TenantEntity getTenantInfo(){
        TenantEntity tenantEntity = null;

        ObTenantEntity user = currentUser(ObTenantEntity.class);
        if(user!=null){
            tenantEntity = user.getTenantEntity();
        }
        return tenantEntity;

    }

    /**
     * 获取租户ID
     * @return
     */
    public static Long getTenantId(){
        Long id = 0l;
        TenantEntity<Long> tenantEntity = getTenantInfo();

        if(tenantEntity!=null){
            id = tenantEntity.getId();
        }
        return id;

    }

}