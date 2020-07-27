package com.byoa.system.dao;


import com.byoa.system.domain.ResourcesDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ResourcesDTOMapper {

    int remove(Long ResourcesId);

    int batchRemove(Long[] id);

    int count(Map<String, Object> map);

    int update(ResourcesDTO resourcesDTO);

    ResourcesDTO getResourcesById(Long resourcesId);

    List<ResourcesDTO> getAllResources(Map<String, Object> map);

    int insert(ResourcesDTO record);

    List<String> userPermission(Long userId);

    List<ResourcesDTO> listResourcesByUserId(Long userId);

}