package com.waskj.base.domain.core.vo;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class MenuVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 标题 */
    private String name;

    /** 地址 */
    private String url;

    /** 权限编码 */
    private String permCode;

    /** 图标 */
    private String icon;

    private List<MenuVO> mvList;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermCode() {
        return this.permCode;
    }

    public void setPermCode(String permCode) {
        this.permCode = permCode;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<MenuVO> getMvList() {
        return mvList;
    }

    public void setMvList(List<MenuVO> mvList) {
        this.mvList = mvList;
    }

}
