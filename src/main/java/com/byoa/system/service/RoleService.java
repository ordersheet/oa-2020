package com.byoa.system.service;

import com.byoa.system.domain.RoleDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface RoleService {

    RoleDTO get(Long roleId);

    List<RoleDTO> list();

    int save(Map<String, Object> map);

    int update(RoleDTO role);

    int remove(Long roleId);

    List<RoleDTO> list(Long userId);

    int batchremove(Long[] ids);
}
