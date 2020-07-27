package com.byoa.system.controller;
import com.byoa.common.controller.BaseController;
import com.byoa.common.domain.Tree;
import com.byoa.common.utils.R;
import com.byoa.common.utils.ShiroUtils;
import com.byoa.system.domain.ResourcesDTO;
import com.byoa.system.service.ResourcesDTOService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RequestMapping("/sys/resources")
@Controller
public class ResourcesController extends BaseController {

    String prefix = "system/menu";

    @Autowired
    ResourcesDTOService resourcesService;

    @RequiresPermissions("sys:resources")
    @GetMapping
    String resources(Model model){
        return prefix+"/resources";
    }


    @RequiresPermissions("sys:resources")
    @GetMapping("/tree")
    @ResponseBody
    List<Tree<ResourcesDTO>> resourcesTree(){
        return resourcesService.listResourcesTree(ShiroUtils.getUserId());
    }

    @RequiresPermissions("sys:resources")
    @GetMapping("/alltree")
    @ResponseBody
    List<Tree<ResourcesDTO>> sTree(){
        return resourcesService.listAllResourcesTree();
    }

    @RequiresPermissions("sys:resources")
    @GetMapping("/list")
    @ResponseBody
    List<ResourcesDTO> list(Map<String,Object> map) {
        List<ResourcesDTO> list = resourcesService.list(map);
        return list;
    }

    @RequiresPermissions("sys:resources:add")
    @GetMapping("/add/{pId}")
    String add(Model model, @PathVariable("pId") Long pId) {
        model.addAttribute("pId" , pId);
        if (pId == 0) {
            model.addAttribute("pName" , "根目录");
        } else {
            model.addAttribute("pName" , resourcesService.get(pId).getName());
        }
        return prefix + "/add";
    }


    @RequiresPermissions("sys:resources:edit")
    @GetMapping("/edit/{id}")
    String edit(Model model, @PathVariable("id") Long id) {
        ResourcesDTO r = resourcesService.get(id);
        Long pId = r.getParentId();
        model.addAttribute("pId" , pId);
        if (pId == 0) {
            model.addAttribute("pName" , "根目录");
        } else {
            model.addAttribute("pName" , resourcesService.get(pId).getName());
        }
        model.addAttribute("resources" , r);
        return prefix + "/edit";
    }

    @RequiresPermissions("sys:resources:update")
    @PostMapping("/update")
    @ResponseBody
    R update(ResourcesDTO resourcesDTO){
        int update = resourcesService.update(resourcesDTO);
        if(update<=0){
            return R.error();
        }
        return R.ok();
    }

    @RequiresPermissions("sys:resources:delete")
    @PostMapping("/remove")
    @ResponseBody
    R remove(@RequestParam("id") Long resourcesId){
        int remove = resourcesService.remove(resourcesId);
        if (remove<=0){
            return R.error(1,"删除失败");
        }
        return R.ok();
    }

    @RequiresPermissions("sys:resources:save")
    @PostMapping("/save")
    @ResponseBody
    R save(ResourcesDTO resourcesDTO){
        int save = resourcesService.save(resourcesDTO);
        if(save <= 0){
            return R.error(1,"保存失败");
        }
        return R.ok();
    }
}
