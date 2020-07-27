package com.byoa.system.service.impl;

import com.byoa.common.domain.Tree;
import com.byoa.common.utils.BuildTree;
import com.byoa.common.utils.ShiroUtils;
import com.byoa.system.dao.ResourcesDTOMapper;
import com.byoa.system.dao.RoleResourceMapper;
import com.byoa.system.domain.ResourcesDTO;
import com.byoa.system.service.ResourcesDTOService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ResourcesDTOServiceImpl implements ResourcesDTOService {

    @Autowired
    ResourcesDTOMapper resourcesDTOMapper;

    @Autowired
    RoleResourceMapper roleResourceMapper;

    @Override
    public Tree<ResourcesDTO> getSysResourcesTree(Long id) {
        return null;
    }

    @Override
    public List<Tree<ResourcesDTO>> listResourcesTree(Long id) {

      List<Tree<ResourcesDTO>> trees = new ArrayList<>();
      List<ResourcesDTO> resourcesDTOS = resourcesDTOMapper.listResourcesByUserId(ShiroUtils.getUserId());
//      List<ResourcesDTO> resourcesDTOS = resourcesDTOMapper.getAllResources(new HashMap<>(16));
//        System.out.println("菜单列表:"+ JackSonUtils.toJackString(resourcesDTOS));
        for (ResourcesDTO r : resourcesDTOS) {
            Tree<ResourcesDTO> tree = new Tree<>();
            tree.setId(r.getResourcesId().toString());
            tree.setParentId(r.getParentId().toString());
            tree.setText(r.getName());
            Map<String,Object> attributes = new HashMap<>(16);
            attributes.put("url",r.getUrl());
            attributes.put("icon",r.getIcon());
            tree.setAttributes(attributes);
            trees.add(tree);
        }
        List<Tree<ResourcesDTO>> list = BuildTree.buildList(trees,"0");
        return list;
    }

    @Override
    public List<Tree<ResourcesDTO>> listAllResourcesTree() {
        List<Tree<ResourcesDTO>> trees = new ArrayList<>();
        //List<ResourcesDTO> resourcesDTOS = resourcesDTOMapper.listResourcesByUserId(ShiroUtils.getUserId());
        List<ResourcesDTO> resourcesDTOS = resourcesDTOMapper.getAllResources(new HashMap<>(16));
//        System.out.println("菜单列表:"+ JackSonUtils.toJackString(resourcesDTOS));
        for (ResourcesDTO r : resourcesDTOS) {
            Tree<ResourcesDTO> tree = new Tree<>();
            tree.setId(r.getResourcesId().toString());
            tree.setParentId(r.getParentId().toString());
            tree.setText(r.getName());
            Map<String,Object> attributes = new HashMap<>(16);
            attributes.put("url",r.getUrl());
            attributes.put("icon",r.getIcon());
            tree.setAttributes(attributes);
            trees.add(tree);
        }
        List<Tree<ResourcesDTO>> list = BuildTree.buildList(trees,"0");
        return list;
    }

    @Override
    public Tree<ResourcesDTO> getTree() {
        List<Tree<ResourcesDTO>> trees = new ArrayList<>();
        List<ResourcesDTO> resourcesDTOS = resourcesDTOMapper.getAllResources(new HashMap<>(16));
        for (ResourcesDTO r : resourcesDTOS) {
            Tree<ResourcesDTO> tree = new Tree<>();
            tree.setId(r.getResourcesId().toString());
            tree.setParentId(r.getParentId().toString());
            tree.setText(r.getName());
            trees.add(tree);
        }
        Tree<ResourcesDTO> t = BuildTree.build(trees);
        return t;
    }


    public Tree<ResourcesDTO> get() {
        List<Tree<ResourcesDTO>> trees = new ArrayList<>();
        List<ResourcesDTO> resourcesDTOS = resourcesDTOMapper.getAllResources(new HashMap<>(16));
        for (ResourcesDTO r : resourcesDTOS) {
            Tree<ResourcesDTO> tree = new Tree<>();
            tree.setId(r.getResourcesId().toString());
            tree.setParentId(r.getParentId().toString());
            tree.setText(r.getName());
            trees.add(tree);
        }
        Tree<ResourcesDTO> t = BuildTree.getRoot(trees,"0");
        return t;
    }

    @Override
    public Tree<ResourcesDTO> getTree(Long id) {
        return null;
    }

    @Override
    public List<ResourcesDTO> list(Map<String, Object> map) {
       List<ResourcesDTO> resourcesDTOS = resourcesDTOMapper.getAllResources(map);

        return resourcesDTOS;
    }

    @Override
    public int remove(Long id) {
        //删除两个东西
        //1.resources表中的数据
        //2.role_resources表中的数据
        int result = resourcesDTOMapper.remove(id);
        if(result <=0){
            return 0;
        }
        int i = roleResourceMapper.removeByResourcesId(id);
        return result;
    }

    @Override
    public int save(ResourcesDTO record) {
        int r = resourcesDTOMapper.insert(record);
        return r;
    }

    @Override
    public int update(ResourcesDTO record) {
        int r = resourcesDTOMapper.update(record);
        return r;
    }

    @Override
    public ResourcesDTO get(Long id) {
        ResourcesDTO resourcesDTO = resourcesDTOMapper.getResourcesById(id);
//        System.out.println("resourcesImpl.get:resources:"+ JackSonUtils.toJackString(resourcesDTO));
        return resourcesDTO;
    }

    /**
     *
     * @param userId
     * @return 返回用户的权限级
     */
    @Override
    public Set<String> listPerms(Long userId) {
        List<String> permission = resourcesDTOMapper.userPermission(userId);
        Set<String> perms = new HashSet<>();
        for (String perm : permission) {
            if(StringUtils.isNotBlank(perm)){
               perms.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return perms;
    }

    @Override
    public List<ResourcesDTO> listResourcesByUserId(Long userId) {
        return resourcesDTOMapper.listResourcesByUserId(userId);
    }
}
