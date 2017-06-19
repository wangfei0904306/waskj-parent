package com.waskj.base.provider.core.util;

import org.apache.commons.beanutils.BeanUtilsBean;

/**
 * Bean复制工具类
 */
public abstract class BeanCopyUtil {

    /**
     * 复制bean属性,忽略null属性
     */
    public static void copyPropertiesIgnoreNull(Object from,Object to){
        copyProperties(from,to,new NullAwareBeanUtilsBean());
    }

    /**
     * 复制bean，不过滤null属性
     * @param from
     * @param to
     */
    public static void copyPropertiesAllowNull(Object from,Object to){
        copyProperties(from,to,new BeanUtilsBean());
    }

    private static void copyProperties(Object from,Object to,BeanUtilsBean bub){
        try {
            bub.copyProperties(to,from);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
