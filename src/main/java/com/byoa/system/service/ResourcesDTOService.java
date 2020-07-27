package com.byoa.system.service;


import com.byoa.common.domain.Tree;
import com.byoa.system.domain.ResourcesDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public interface ResourcesDTOService {

    Tree<ResourcesDTO> getSysResourcesTree(Long id);

    List<Tree<ResourcesDTO>> listResourcesTree(Long id);

    List<Tree<ResourcesDTO>> listAllResourcesTree();

    Tree<ResourcesDTO> getTree();

    Tree<ResourcesDTO> getTree(Long id);

    List<ResourcesDTO> list(Map<String, Object> map);

    int remove(Long id);

    int save(ResourcesDTO record);

    int update(ResourcesDTO record);

    ResourcesDTO get(Long id);

    Set<String> listPerms(Long userId);

    List<ResourcesDTO> listResourcesByUserId(Long userId);

    Tree<ResourcesDTO> get();
}
