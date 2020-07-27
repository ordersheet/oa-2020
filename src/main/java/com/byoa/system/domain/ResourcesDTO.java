package com.byoa.system.domain;



public class ResourcesDTO extends BaseDTO {

    //资源ID
    private Long resourcesId;
    //父资源ID，一级资源为0
    private Long parentId;
    //资源名称
    private String name;
    //资源类型 0:目录 1：菜单 2：按钮
    private Integer type;
    //资源URL
    private String url;
    //资源授权 shiro
    private String permission;
    //资源状态 0-不可用 1-可用
    private Integer statu;
    //资源图标
    private String icon;
    //资源排序
    private Integer sort;

    public Long getResourcesId() {
        return resourcesId;
    }

    public void setResourcesId(Long resourcesId) {
        this.resourcesId = resourcesId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Integer getStatu() {
        return statu;
    }

    public void setStatu(Integer statu) {
        this.statu = statu;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }


}