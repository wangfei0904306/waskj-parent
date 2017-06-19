package com.waskj.base.shiro.jwt.filter;

import com.waskj.base.shiro.core.filter.AbstractCleanupFilter;
import com.waskj.base.shiro.jwt.util.JwtTokenHolder;

/**
 * Created by poet on 2016/12/23.
 */
public class JwtCleanupFilter extends AbstractCleanupFilter {
    @Override
    protected void doCleanup() {
        JwtTokenHolder.remove();
    }
}
