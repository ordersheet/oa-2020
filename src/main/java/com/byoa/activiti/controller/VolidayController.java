package com.byoa.activiti.controller;

import com.byoa.activiti.domain.SalaryDO;
import com.byoa.activiti.service.SalaryService;
import com.byoa.activiti.utils.ActivitiUtils;
import com.byoa.common.controller.BaseController;
import com.byoa.common.utils.PageUtils;
import com.byoa.common.utils.Query;
import com.byoa.common.utils.R;
import com.byoa.common.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *@autuor ysk@bysj
 */
@Controller
@RequestMapping("/act/voliday")
public class VolidayController  extends BaseController{

 @Autowired
 private SalaryService salaryService;
 @Autowired
 ActivitiUtils activitiUtils;

 @ResponseBody
 @GetMapping("/list")
 public PageUtils list(@RequestParam Map<String, Object> params) {
     Query query = new Query(params);
     List<SalaryDO> salaryList = salaryService.list(query);
     int total = salaryService.count(query);
     PageUtils pageUtils = new PageUtils(salaryList, total);
     return pageUtils;
 }

 @GetMapping("/form")
 String form() {
     return "act/voliday/add";
 }

 @GetMapping("/form/{taskId}")
 String edit(@PathVariable("taskId") String taskId, Model model) {
     SalaryDO salary = salaryService.get(activitiUtils.getBusinessKeyByTaskId(taskId));
     salary.setTaskId(taskId);
     model.addAttribute("salary", salary);
     return "act/voliday/edit";
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
     } else if("apply_end".equals(salary.getTaskComment())){
         //流程完成，兑现
     }
     salaryService.update(salary);
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
