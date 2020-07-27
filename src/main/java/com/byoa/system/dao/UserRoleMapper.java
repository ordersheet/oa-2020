package com.byoa.system.dao;
import com.byoa.system.domain.UserRoleDTO;

import java.util.List;
import java.util.Map;

public interface UserRoleMapper {

    Long[] listRoleId(Long userId);

    int count(Map<String, Object> map);

    int save(UserRoleDTO userRole);

    int update(UserRoleDTO userRole);


    int removeByRoleId(Long roleId);

    int batchSave(List<UserRoleDTO> list);

    int batchRemoveByUserId(Long[] ids);

    int removeByUserId(Long userId);

    int batchRemoveByRoleId(Long[] ids);
}