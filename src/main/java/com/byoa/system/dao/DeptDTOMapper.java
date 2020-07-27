package com.byoa.system.dao;



import com.byoa.system.domain.DeptDTO;

import java.util.List;
import java.util.Map;

public interface DeptDTOMapper {

    DeptDTO getDeptById(Long deptId);

    List<DeptDTO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int insert(DeptDTO dept);

    int update(DeptDTO dept);

    int remove(Long deptId);

    int batchRemove(Long[] deptIds);

    Long[] listParentDept();

    int getDeptUserNumber(Long deptId);

    Long[] listAllDept();

}