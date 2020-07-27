package com.byoa.system.domain;

public class RoleResourceDTO extends BaseDTO {

    private Long id;

    private Long roleId;

    private Long resourcesId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getResourcesId() {
        return resourcesId;
    }

    public void setResourcesId(Long resourcesId) {
        this.resourcesId = resourcesId;
    }

    @Override
    public String toString() {
        return "RoleResourceDTO{" +
                "id=" + id +
                ", roleId=" + roleId +
                ", resourcesId=" + resourcesId +
                ", createTime=" + createTime +
                ", createUserId=" + createUserId +
                ", updateTime=" + updateTime +
                ", updateUserId=" + updateUserId +
                ", version=" + version +
                '}';
    }
}