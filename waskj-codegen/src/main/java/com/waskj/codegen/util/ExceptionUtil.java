package com.waskj.codegen.util;

/**
 * Created by poet on 2016/10/27.
 */
public class ExceptionUtil {

    public static void wrapException(Exception ex){
        throw new RuntimeException(ex);
    }

}
