package com.byoa.system.controller;
import com.byoa.common.controller.BaseController;
import com.byoa.common.utils.R;
import com.byoa.system.config.StringUtils;
import com.byoa.system.dao.RoleResourceMapper;
import com.byoa.system.domain.RoleDTO;
import com.byoa.system.domain.RoleResourceDTO;
import com.byoa.system.service.ResourcesDTOService;
import com.byoa.system.service.RoleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sys/role")
public class RoleController  extends BaseController {
    private String prefix = "system/role";

    @Autowired
    RoleService roleService;

    @Autowired
    ResourcesDTOService resourcesService;



    @RequiresPermissions("sys:role")
    @GetMapping("")
    String role(){
        return prefix+"/role";
    }

    @Autowired
    RoleResourceMapper roleResourceMapper;

    @RequiresPermissions("sys:role")
    @GetMapping("/list")
    @ResponseBody
    List<RoleDTO>  list() throws JsonProcessingException {
       List<RoleDTO> roles = roleService.list();
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(roles);
//        System.out.println("@@@ role+"+s);
       return roles;
    }

    @RequiresPermissions("sys:role:add")
    @GetMapping("/add")
    String add(){
        return prefix+"/add";
    }

    @RequiresPermissions("sys:role:add")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Long roleId, Model model){
        RoleDTO roleDTO = roleService.get(roleId);
        model.addAttribute("role",roleDTO);
        return prefix+"/edit";
    }

    @RequiresPermissions("sys:role:update")
    @PostMapping("/update")
    @ResponseBody
    R update(@RequestParam Map<String,Object> map) {
//        System.out.println(map);
        Long roleId = Long.valueOf(map.get("roleId").toString());
        String ids = map.get("resourcesIds").toString();
        Long[] resourcesIds = StringUtils.getLongArray(ids);
        String name = map.get("name").toString();
        String description = map.get("description").toString();
        RoleDTO role = new RoleDTO();
        role.setDescription(description);
        role.setRoleId(roleId);
        role.setName(name);
        int update = roleService.update(role);
        if(update <= 0){
            return R.error();
        }
        int i = roleResourceMapper.removeByRoleId(roleId);
        if (i <=0 ){
            return R.error();
        }
        List<RoleResourceDTO> roleResourceDTOS= new ArrayList<>();
        for (Long resourcesId : resourcesIds) {
            RoleResourceDTO r= new RoleResourceDTO();
            r.setRoleId(roleId);
            r.setResourcesId(resourcesId);
            roleResourceDTOS.add(r);
        }
        int i1 = roleResourceMapper.batchSave(roleResourceDTOS);
        if (i1 <= 0){
            return R.error();
        }
        return R.ok();
    }

    @RequiresPermissions("sys:role:save")
    @PostMapping("/save")
    @ResponseBody
    R save(@RequestParam Map<String,Object> role){
        //这是是可以接收到map的  再用map把他包装成一个RoleDTO

        //把menuIds 接收过来   调用其他的service 插入到 role_resources表中

//        System.out.println(JackSonUtils.toJackString(role));

        int save = roleService.save(role);
        if(save<=0){
            return R.error(0,"添加失败");
        }

        return R.ok();
    }


    @RequiresPermissions("sys:role:delete")
    @DeleteMapping("/remove")
    @ResponseBody
    R remove(@RequestParam("roleId") Long roleId) {
//        System.out.println("role/remove:roleId:"+roleId);
        int remove = roleService.remove(roleId);
        if (remove <= 0) {
            return R.error(1, "删除失败");
        }
            return R.ok();
    }

    @RequiresPermissions("sys:role:batchDelete")
    @PostMapping("/batchRemove")
    @ResponseBody
    R batchRemove(@RequestParam("ids[]") Long[] ids) {
        int batchremove = roleService.batchremove(ids);
        if (batchremove <= 0) {
            return R.error(1, "删除失败");
        }
            return R.ok();
    }
}
