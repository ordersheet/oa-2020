package com.byoa.system.dao;


import com.byoa.system.domain.RoleDTO;

import java.util.List;
import java.util.Map;

public interface RoleDTOMapper {

    RoleDTO get(Long roleId);

    List<RoleDTO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(RoleDTO role);

    int update(RoleDTO role);

    int remove(Long roleId);

    int batchRemove(Long[] roleIds);

}