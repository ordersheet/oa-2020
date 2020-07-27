package com.byoa.system.dao;

import com.byoa.system.domain.UserDTO;

import java.util.List;
import java.util.Map;

public interface UserDTOMapper {

    UserDTO getByPrimaryKey(Long userId);

    List<UserDTO> list(Map<String, Object> map);

    int save(UserDTO record);

    int update(UserDTO record);

    int remove(Long userId);

    int batchremove(Long[] userIds);

    int count(Map<String, Object> map);

    Long[] listAllDept();

}