package com.waskj.codegen.config;

/**
 * Created by poet on 2016/10/27.
 */
public class ShortAlias {

    private String shortAlias;

    private String packageAlias;

    private String entityAlias;

    public ShortAlias(String shortAlias) {
        this.shortAlias = shortAlias;
    }

    public ShortAlias(String shortAlias, String packageAlias, String entityAlias) {
        this.shortAlias = shortAlias;
        this.packageAlias = packageAlias;
        this.entityAlias = entityAlias;
    }

    public String getShortAlias() {
        return shortAlias;
    }


    public String getPackageAlias() {
        return packageAlias == null ? shortAlias : packageAlias;
    }


    public String getEntityAlias() {
        return entityAlias == null ? shortAlias : entityAlias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShortAlias that = (ShortAlias) o;

        return shortAlias.equals(that.shortAlias);

    }

    @Override
    public int hashCode() {
        return shortAlias.hashCode();
    }
}
