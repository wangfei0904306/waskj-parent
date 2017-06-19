package com.waskj.codegen.config;

import java.util.*;

/**
 * Created by poet on 2016/10/27.
 */
public class ShortAliasConfig {

//    private Set<ShortAlias> shortAliases = new HashSet<>(0);
    private Map<String,ShortAlias> shortAliases = new HashMap<>();

    public void addShortAlias(ShortAlias alias){
        this.shortAliases.put(alias.getShortAlias(),alias);
    }

    public void addShortAlias(String shortAlias){
        this.shortAliases.put(shortAlias,new ShortAlias(shortAlias));
    }

    public void addShortAlias(String shortAlias,String packageAlias,String entityAlias){
        this.shortAliases.put(shortAlias,new ShortAlias(shortAlias,packageAlias,entityAlias));
    }

    public Map<String,ShortAlias> getShortAliases() {
        return Collections.unmodifiableMap(shortAliases);
    }
}
