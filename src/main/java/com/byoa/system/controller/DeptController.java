package com.byoa.system.controller;


import com.byoa.common.controller.BaseController;
import com.byoa.common.domain.Tree;
import com.byoa.common.utils.R;
import com.byoa.system.domain.DeptDTO;
import com.byoa.system.service.DeptService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/sys/dept")
@Controller
public class DeptController extends BaseController {

    private String prefix = "system/dept";

    @Autowired
    private DeptService deptService;


    @RequiresPermissions("sys:dept")
    @GetMapping()
    String dept(){
//        System.out.println(JackSonUtils.toJackString("dept/:url:" + prefix + "/dept"));
        return prefix+"/dept";
    }

    @ApiOperation(value = "获取部门列表",notes = "")
    @ResponseBody
    @GetMapping("/list")
    public List<DeptDTO> list(){
//        System.out.println(JackSonUtils.toJackString("dept/list:"));
        Map<String,Object> query = new HashMap<>(16);
        List<DeptDTO> deptDTOList = deptService.list(query);
//        System.out.println(JackSonUtils.toJackString("dept/list:deptList:responsebody"+deptDTOList));
        return deptDTOList;
    }

    @RequiresPermissions("sys:dept:add")
    @GetMapping("/add/{pId}")
    String add(@PathVariable("pId") Long pId, Model model) {
//        System.out.println(JackSonUtils.toJackString("dept/add/{pid}:pid:" + pId));
        model.addAttribute("pId" , pId);
        if (pId == 0) {
            model.addAttribute("pName" , "总部门");
        } else {
            model.addAttribute("pName" , deptService.get(pId).getName());
        }
//        System.out.println("dept/add/{pid}:model"+JackSonUtils.toJackString(model));
//        System.out.println("dept/add/{pid}:url"+prefix + "/add");
        return prefix + "/add";
    }

    @RequiresPermissions("sys:dept:edit")
    @GetMapping("/edit/{deptId}")
    String edit(@PathVariable("deptId") Long deptId, Model model){
        DeptDTO sysDept = deptService.get(deptId);
        model.addAttribute("sysDept" , sysDept);
        model.addAttribute("parentDeptName" , "无");
        DeptDTO parDept ;
        if (sysDept.getParentId() == 0){
         model.addAttribute("parentDeptName","总部门");
        }else {
          parDept = deptService.get(sysDept.getParentId());
          model.addAttribute("parentDeptName" , parDept.getName());
        }
        return prefix + "/edit";
    }

    @RequiresPermissions("sys:dept:update")
    @ResponseBody
    @RequestMapping("/update")
    public R update(DeptDTO deptDTO){
        int update = deptService.update(deptDTO);
        if(update <= 0){
            return R.error();
        }
        return R.ok();
    }


    @RequiresPermissions("sys:dept:save")
    @ResponseBody
    @PostMapping("/save")
    public R save(DeptDTO deptDTO){
        int save = deptService.save(deptDTO);
        if (save <= 0){
            return R.error();
        }
        return R.ok();
    }


    @RequiresPermissions("sys:dept:batchDelete")
    @PostMapping("/batchRemove")
    @ResponseBody
    public R remove(@RequestParam("ids[]") Long[] deptIds){
//        System.out.println("dept/batchRemove:deptIds:"+JackSonUtils.toJackString(deptIds));
        int i = deptService.batchRemove(deptIds);
//        System.out.println("batchRemove:"+i);
        if (i <= 0){
            return R.error();
        }
        return R.ok();
    }


    @RequiresPermissions("sys:dept:delete")
    @PostMapping("/remove")
    @ResponseBody
    public R remove(@RequestParam Long deptId){
       Map<String,Object> map = new HashMap<>();
       map.put("parentId",deptId);
       if(deptService.count(map) > 0){
           return R.error(1,"包含下级部门，请删除下级部门后修改");
       }
       if (deptService.checkDeptHasUser(deptId)){
           if (deptService.remove(deptId) > 0){
               return R.ok();
           }
       }else
           return R.error(1,"部门包含用户，不允许修改");
       return R.error();
    }


    @GetMapping("/tree")
    @ResponseBody
    public Tree<DeptDTO> tree(){
        Tree<DeptDTO> tree = new Tree<>();
        tree = deptService.getTree();
        return tree;
    }

    @GetMapping("/treeView")
    String treeView(){
        return prefix+"/deptTree";
    }
}
