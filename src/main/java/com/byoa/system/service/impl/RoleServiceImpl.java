package com.byoa.system.service.impl;


import com.byoa.system.config.StringUtils;
import com.byoa.system.dao.RoleDTOMapper;
import com.byoa.system.dao.RoleResourceMapper;
import com.byoa.system.dao.UserRoleMapper;
import com.byoa.system.domain.RoleDTO;
import com.byoa.system.domain.RoleResourceDTO;
import com.byoa.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleDTOMapper roleMapper;

    @Autowired
    UserRoleMapper userRoleMapper;

    @Autowired
    RoleResourceMapper roleResourceMapper;

    /**
     * 根据role_id查询到的role信息
     * @param id
     * @return
     */
    @Override
    public RoleDTO get(Long id) {
        RoleDTO roleDTO = roleMapper.get(id);
        return roleDTO;
    }

    /**
     * 获取所有角色信息
     * @return
     */
    @Override
    public List<RoleDTO> list() {
      List<RoleDTO> roles = roleMapper.list(new HashMap<>(16));
      return roles;
    }


    /**
     * 保存一个角色
     * @param
     * @return
     */
    @Override
    public int save(Map<String,Object> map) {
        //这里需要一个事务来解决
        //这里应当在role表中先保存role信息
        //然后在role_resources表中保存resources信息
        String name = (String) map.get("name");
        String decription = (String) map.get("description");
        RoleDTO roleDTO = new RoleDTO();

        roleDTO.setName(name);
        roleDTO.setDescription(decription);

//        System.out.println(map.get("resourcesIds"));
        String resourcesIds = map.get("resourcesIds").toString();

        Long[] longs = StringUtils.getLongArray(resourcesIds);

//        System.out.println(Arrays.toString(longs));

        int save = roleMapper.save(roleDTO);
        //这里自增主键会返回到roleDTO 中 也就是 插入时的实体类
        Long roleId = roleDTO.getRoleId();

        if(save <= 0){
            return 0;
        }
        List<RoleResourceDTO> roleResourceDTOS = new ArrayList<>();
        for (Long id : longs) {
            RoleResourceDTO r = new RoleResourceDTO();
            r.setRoleId(roleId);
            r.setResourcesId(id);
            roleResourceDTOS.add(r);
        }
        //新增的roleId-resourcesId关系保存到roleResource表中
//        System.out.println(roleResourceDTOS);
        System.out.println("接下来执行roleResourcesMapp.batchSave-----");
        if (roleResourceMapper.batchSave(roleResourceDTOS) <= 0) {
            return 0;
        }
        //保存成功
        return 1;
    }
    //更新同下
    @Override
    public int update(RoleDTO role) {
        int r = roleMapper.update(role);
        return r;
    }

    //这里删除角色 的同时也删除了 role_resources表中的角色相关 ，同时删除user_role中的对应关系
    @Override
    public int remove(Long roleId) {
        int r = roleMapper.remove(roleId);
        System.out.println("roleService.remove:roleMapper:"+r);
        if(r<=0){
            return 0;
        }
        //这里可能没有user_role 对应关系 也就是 没有用户使用这个角色
        int i = userRoleMapper.removeByRoleId(roleId);
        System.out.println("roleService.remove:userRoleMapper:"+i);

        int i1 = roleResourceMapper.removeByRoleId(roleId);
        System.out.println("roleService.remove:roleResourcesMapper:"+i1);
        if(i1<=0){
            return 0;
        }
        return 1;
    }


    /**
     * 获取用户角色
     * @param userId
     * @return 根据用户ID查询到的角色信息
     */
    @Override
    public List<RoleDTO> list(Long userId) {
        Long[] roleId = userRoleMapper.listRoleId(userId);
        List<RoleDTO> roles = roleMapper.list(new HashMap<>(16));
        for (RoleDTO role : roles) {
            role.setSign(false);
            for (Long l : roleId) {
                if(role.getRoleId() == l){
                    role.setSign(true);
                    break;
                }
            }
        }
        return roles;
    }


    //这里删除角色 的同时也删除了 role_resources表中的角色相关 ，同时删除user_role中的对应关系
    @Override
    public int batchremove(Long[] ids) {
        int r = roleMapper.batchRemove(ids);
        System.out.println("roleService.batchRemove:roleMapper:"+r);
        int i = roleResourceMapper.batchRemoveByRoleId(ids);
        System.out.println("roleService.batchRemove:roleResourcesMapper:"+i);
        int g = userRoleMapper.batchRemoveByRoleId(ids);
        System.out.println("roleService.batchRemove:userRoleMapper:"+i);
        //如何删掉user_role 中的对应关系，
        if(i<=0 || r<=0 ){
            return 0;
        }
        return 1;
    }
}
