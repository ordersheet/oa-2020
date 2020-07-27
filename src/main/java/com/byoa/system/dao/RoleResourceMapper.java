package com.byoa.system.dao;


import com.byoa.system.domain.RoleResourceDTO;

import java.util.List;

public interface RoleResourceMapper {

     int batchSave(List<RoleResourceDTO> list);

     int removeByRoleId(Long roleId);

     int batchRemoveByRoleId(Long[] ids);

     int removeByResourcesId(Long resourcesId);

}