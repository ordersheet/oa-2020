package com.byoa.system.service.impl;

import com.byoa.common.domain.Tree;
import com.byoa.common.utils.BuildTree;
import com.byoa.system.dao.DeptDTOMapper;
import com.byoa.system.domain.DeptDTO;
import com.byoa.system.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    DeptDTOMapper deptMapper;

    @Override
    public DeptDTO get(Long deptId) {
        return deptMapper.getDeptById(deptId);
    }

    @Override
    public List<DeptDTO> list(Map<String, Object> map) {
        return deptMapper.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return deptMapper.count(map);
    }

    @Override
    public int save(DeptDTO record) {
        return deptMapper.insert(record);
    }

    @Override
    public int update(DeptDTO record) {
        return deptMapper.update(record);
    }

    @Override
    public int remove(Long deptId) {
        return deptMapper.remove(deptId);
    }

    @Override
    public int batchRemove(Long[] deptIds) {
        return deptMapper.batchRemove(deptIds);
    }

    @Override
    public Tree<DeptDTO> getTree() {
        List<Tree<DeptDTO>> trees = new ArrayList<>();
        List<DeptDTO> depts = deptMapper.list(new HashMap<>(16));
        for (DeptDTO dept : depts) {
            Tree<DeptDTO> tree = new Tree<>();
            tree.setId(dept.getDeptId().toString());
            tree.setParentId(dept.getParentId().toString());
            tree.setText(dept.getName());
            Map<String,Object> state = new HashMap<>(16);
            state.put("opened",true);
            tree.setState(state);
            trees.add(tree);
        }
        Tree<DeptDTO> t = BuildTree.build(trees);
        return t;

    }

    @Override
    public boolean checkDeptHasUser(Long deptId) {
        int result = deptMapper.getDeptUserNumber(deptId);
        return result == 0 ?true:false;
    }

    @Override
    public List<Long> listChildrenIds(Long parentId) {
        List<DeptDTO> deptDTOS = list(null);
        return treeMenuList(deptDTOS,parentId);
    }

    @Override
    public Long[] listParentDept() {
        return deptMapper.listParentDept();
    }


    List<Long> treeMenuList(List<DeptDTO> menuList,long pid){
        List<Long> childIds = new ArrayList<>();
        for (DeptDTO dto : menuList) {

            if (dto.getDeptId() == pid){
                treeMenuList(menuList,dto.getDeptId());
                childIds.add(dto.getDeptId());
            }
        }
        return childIds;
    }
}
