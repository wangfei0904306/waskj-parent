package com.waskj.codegen.config;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by poet on 2016/10/27.
 */
public class TableExcludeConfig {

    // 排除的表
    private Set<String> excludeTables = new HashSet<>(0);

    // 排除的表前缀
    private Set<String> excludePrefixes = new HashSet<>(0);

    public void addExcludeTable(String tableName){
        this.excludeTables.add(tableName);
    }

    public void addExcludeTablePrefix(String tablePrefix){
        this.excludePrefixes.add(tablePrefix);
    }

    public void removeExcludeTable(String tableName){
        this.excludeTables.remove(tableName);
    }

    public void removeExcludeTablePrefix(String tablePrefix){
        this.excludePrefixes.remove(tablePrefix);
    }

    public Set<String> getExcludeTables() {
        return Collections.unmodifiableSet(excludeTables);
    }

    public Set<String> getExcludePrefixes() {
        return Collections.unmodifiableSet(excludePrefixes);
    }
}
