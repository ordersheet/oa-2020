package com.byoa.system.service;

import com.byoa.common.domain.Tree;
import com.byoa.system.domain.DeptDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface DeptService {

  DeptDTO get(Long deptId);

  List<DeptDTO> list(Map<String, Object> map);

  int count(Map<String, Object> map);

  int save(DeptDTO record);

  int update(DeptDTO record);

  int remove(Long deptId);

  int batchRemove(Long[] deptIds);

  Tree<DeptDTO> getTree();

  boolean checkDeptHasUser(Long deptId);

  List<Long> listChildrenIds(Long parentId);

  Long[] listParentDept();



}
