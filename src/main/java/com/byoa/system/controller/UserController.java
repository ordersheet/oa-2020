package com.byoa.system.controller;
import com.byoa.common.controller.BaseController;
import com.byoa.common.domain.Tree;
import com.byoa.common.utils.PageUtils;
import com.byoa.common.utils.Query;
import com.byoa.common.utils.R;
import com.byoa.system.domain.DeptDTO;
import com.byoa.system.domain.RoleDTO;
import com.byoa.system.domain.UserDTO;
import com.byoa.system.service.DeptService;
import com.byoa.system.service.RoleService;
import com.byoa.system.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/sys/user")
@Controller
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    DeptService deptService;


    private String prefix = "system/user";


    @RequiresPermissions("sys:user")
    @GetMapping("")
    String user(Model model){
        return prefix+"/user";
    }

    @RequiresPermissions("sys:user:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Long userId, Model model){
        UserDTO userDTO = userService.get(userId);
        model.addAttribute("user",userDTO);
        List<RoleDTO> roles = roleService.list(userId);
        model.addAttribute("roles",roles);
        return prefix+"/edit";
    }


    @RequiresPermissions("sys:user:repassword")
    @GetMapping("/resetPwd/{id}")
    String resetPwd(@PathVariable("id") Long userId, Model model){

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        model.addAttribute("user",userDTO);
        return prefix+"/reset_pwd";
    }


    @PostMapping("/adminResetPwd")
    @ResponseBody
    R adminResetPwd(@RequestParam Map<String,Object> map){
        UserDTO userDTO = new UserDTO();
//        System.out.println("user/adminResetPwd:userDTO:"+ JackSonUtils.toJackString(map));
        Long userId = Long.valueOf(map.get("userDTO.userId").toString());
        String password = map.get("password").toString();
        userDTO.setUserId(userId);
        userDTO.setPassword(password);
        int i = userService.resetPwd(userDTO);
        if(i <= 0){
            return R.error(1,"重设失败，请联系管理员");
        }
        return R.ok();
    }


    @RequiresPermissions("sys:user:batchDelete")
    @PostMapping("/batchRemove")
    @ResponseBody
    R batchRemove(@RequestParam("userIds[]") Long[] userIds){
        int i = userService.batchRemove(userIds);
        if(i == 0){
            return R.error(1,"删除失败");
        }
        return R.ok();
    }

    @RequiresPermissions("sys:user")
    @GetMapping("/tree")
    @ResponseBody
    Tree<DeptDTO> tree(){
       Tree<DeptDTO> tree = new Tree<>();
       tree = userService.getTree();
       return tree;
    }

    @RequiresPermissions("sys:user")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String,Object> param){
     Query query = new Query(param);
     List<UserDTO> list = userService.list(query);
     int total = userService.count(query);
     PageUtils pageUtil = new PageUtils(list,total);
     return pageUtil;
    }

    @RequiresPermissions("sys:user:save")
    @PutMapping("/save")
    @ResponseBody
    R save(@RequestParam Map<String,Object> map){
        //这里需要在user_role表中也保存 user-role对应的信息
        int save = userService.save(map);
        if(save > 0){
            return R.ok();
        }
        return R.error();

    }

    @RequiresPermissions("sys:user:add")
    @GetMapping("/add")
    String add(Model model){
        List<RoleDTO> roles = roleService.list();
        model.addAttribute("roles",roles);
        return prefix+"/add";
    }

    @RequiresPermissions("sys:user:update")
    @PostMapping("/update")
    @ResponseBody
    R update(@RequestParam Map<String,Object> map){
//        System.out.println("user/update:map"+JackSonUtils.toJackString(map));
        //更新需要更新两个东西 user_role和user
        int update = userService.update(map);
        if (update<=0){
            return R.error(1,"更新失败");
        }
        return R.ok();
    }


    @RequiresPermissions("sys:user:delete")
    @PostMapping("/remove")
    @ResponseBody
    R remove(@RequestParam Long userId){
//        System.out.println("remove:userId:"+userId);
        int remove = userService.remove(userId);
        if(remove <=0 ){
            return R.error(1,"删除失败");
        }
        return R.ok();

    }


    @PostMapping("/exit")
    @ResponseBody
    boolean exit(@RequestParam Map<String,Object> params){
        return !userService.exit(params);
    }

    @GetMapping("/personal")
    String personal(Model model) {
        UserDTO userDO  = userService.get(getUserId());
        deptService.get(userDO.getDeptId());
        userDO.setDeptName(deptService.get(userDO.getDeptId()).getName());
        model.addAttribute("user",userDO);
        return prefix + "/personal";
    }

    @GetMapping("/treeView")
    String treeView() {
        return  prefix + "/userTree";
    }

    @ResponseBody
    @PostMapping("/uploadImg")
    R uploadImg(@RequestParam("avatar_file") MultipartFile file, String avatar_data, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            result = userService.updatePersonalImg(file, avatar_data, getUserId());
        } catch (Exception e) {
            return R.error("更新图像失败！");
        }
        if(result!=null && result.size()>0){
            return R.ok(result);
        }else {
            return R.error("更新图像失败！");
        }
    }
}
