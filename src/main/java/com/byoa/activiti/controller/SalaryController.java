package com.byoa.activiti.controller;

import com.byoa.activiti.domain.SalaryDO;
import com.byoa.activiti.service.SalaryService;
import com.byoa.activiti.utils.ActivitiUtils;
import com.byoa.common.controller.BaseController;
import com.byoa.common.utils.PageUtils;
import com.byoa.common.utils.Query;
import com.byoa.common.utils.R;
import com.byoa.common.utils.ShiroUtils;
import com.byoa.system.domain.UserDTO;
import com.byoa.system.service.UserService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *@autuor ysk@bysj
 */
@Controller
@RequestMapping("/act/salary")

public class SalaryController extends BaseController{
    /**
     * 审批流程测试表
     */
    @Autowired
    private SalaryService salaryService;

    @Autowired
    ActivitiUtils activitiUtils;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;

    @GetMapping()
    String Salary() {
        return "activiti/salary/salary";
    }

    @ResponseBody
    @GetMapping("/list")
    public PageUtils list(@RequestParam Map<String, Object> params) {
        Query query = new Query(params);
        List<SalaryDO> salaryList = salaryService.list(query);
        int total = salaryService.count(query);
        PageUtils pageUtils = new PageUtils(salaryList, total);
        return pageUtils;
    }

    /**
     * 添加一个任务页面
     * @return
     */
    @GetMapping("/form")
    String add() {
        return "act/salary/add";
    }

    /**
     * 通过taskId 来修改任务
     * @param taskId
     * @param model
     * @return
     */
    @GetMapping("/form/{taskId}")
    String edit(@PathVariable("taskId") String taskId, Model model) {
        SalaryDO salary = salaryService.get(activitiUtils.getBusinessKeyByTaskId(taskId));
        salary.setTaskId(taskId);
        salary.setUserName(userService.get(Long.valueOf(salary.getUserId())).getName());
        model.addAttribute("salary", salary);
        return "act/salary/edit";
    }



    @GetMapping("/his/{proId}")
    String hisPro(@PathVariable("proId") String proId, Model model){
        //下面是获取salary中的内容
    /*    List<Task> list1 = taskService.createTaskQuery()
                .processDefinitionId(proId)
                .orderByProcessDefinitionId()
                .asc()
                .list();
        for (Task task : list1) {
            System.out.println(task.getId());
            System.out.println("---------");
            String businessKey = activitiUtils.getBusinessKeyByTaskId(task.getId());
            System.out.println("busi:"+businessKey);
            SalaryDO salaryDO = salaryService.get(businessKey);
            System.out.println("bu:salary:"+salaryDO.toString());
            System.out.println(task.toString());
        }*/
        System.out.println("proId"+proId);

       Task task = taskService.createTaskQuery()
                .processDefinitionId(proId)
                .singleResult();
   /*             .orderByProcessDefinitionId()
                .asc()*/
//                .list();
        System.out.println(task.toString());
        if(task!=null){
            String businessKey = activitiUtils.getBusinessKeyByTaskId(task.getId());
            SalaryDO salary = salaryService.get(businessKey);
            String userId = salary.getUserId();
            UserDTO userDTO = userService.get(Long.valueOf(userId));
            salary.setUserName(userDTO.getName());
            String taskId = task.getId();
            salary.setTaskId(taskId);
            System.out.println("taskId:"+salary.getTaskId());
            System.out.println(salary.toString());
            //!!!!!
            model.addAttribute("salary",salary);
        }

        return "act/salary/hidetail";
    }


    /**
     * 保存
     */
    @ResponseBody
    @PostMapping("/save")
    public R saveOrUpdate(SalaryDO salary) {
        salary.setCreateDate(new Date());
        salary.setUpdateDate(new Date());
        salary.setCreateBy(ShiroUtils.getUserId().toString());
        salary.setUpdateBy(ShiroUtils.getUserId().toString());
        salary.setDelFlag("1");
        if (salaryService.save(salary) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    public R update(SalaryDO salary) {
        String taskKey = activitiUtils.getTaskByTaskId(salary.getTaskId()).getTaskDefinitionKey();
        if ("audit2".equals(taskKey)) {
            salary.setHrText(salary.getTaskComment());
        } else if ("audit3".equals(taskKey)) {
            salary.setLeadText(salary.getTaskComment());
        } else if ("audit4".equals(taskKey)) {
            salary.setMainLeadText(salary.getTaskComment());
        } else if("apply_end".equals(taskKey)){
            String userId = salary.getUserId();
            Map<String,Object> map = new HashMap<>();
            map.put("sal",salary.getNewb());
            map.put("userId",Long.valueOf(userId));
            int update = userService.updateSal(map);
            if(update<=0){
                return R.error("更新薪资失败");
            }
            //这里获取流程的表单
            //流程完成，兑现
        }
       if (salaryService.update(salary) <= 0){
           return R.error();
       }
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/remove")
    @ResponseBody
    public R remove(String id) {
        if (salaryService.remove(id) > 0) {
            return R.ok();
        }
        return R.error();
    }

    /**
     * 删除
     */
    @PostMapping("/batchRemove")
    @ResponseBody
    public R remove(@RequestParam("ids[]") String[] ids) {
        salaryService.batchRemove(ids);
        return R.ok();
    }

}
