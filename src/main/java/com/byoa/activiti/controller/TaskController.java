package com.byoa.activiti.controller;

import com.byoa.activiti.config.ActivitiConstant;
import com.byoa.activiti.domain.SalaryDO;
import com.byoa.activiti.service.ActTaskService;
import com.byoa.activiti.service.SalaryService;
import com.byoa.activiti.utils.ActivitiUtils;
import com.byoa.activiti.vo.ProcessVO;
import com.byoa.activiti.vo.TaskVO;
import com.byoa.common.utils.PageUtils;
import com.byoa.common.utils.ShiroUtils;
import com.byoa.system.domain.UserDTO;
import com.byoa.system.service.UserService;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import java.util.List;

/**
 *@autuor ysk@bysj
 */
@RequestMapping("activiti/task")
@RestController
public class TaskController {

    @Autowired
    RepositoryService repositoryService;
    @Autowired
    FormService formService;
    @Autowired
    TaskService taskService;
    @Autowired
    ActTaskService actTaskService;
    @Autowired
    HistoryService historyService;

    @Autowired
    SalaryService salaryService;

    @Autowired
    ActivitiUtils activitiUtils;
    @Autowired
    UserService userService;


    @GetMapping("goto")
    public ModelAndView gotoTask(){
        return new ModelAndView("act/task/gotoTask");
    }

    @GetMapping("/gotoList")
    PageUtils list(int offset, int limit) {
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .listPage(offset, limit);
        int count = (int) repositoryService.createProcessDefinitionQuery().count();
        List<Object> list = new ArrayList<>();
        for(ProcessDefinition processDefinition: processDefinitions){
            list.add(new ProcessVO(processDefinition));
        }
        PageUtils pageUtils = new PageUtils(list, count);
        return pageUtils;
    }



    @GetMapping("/form/{procDefId}")
    public void startForm(@PathVariable("procDefId") String procDefId  ,HttpServletResponse response) throws IOException {
        String formKey = actTaskService.getFormKey(procDefId, null);
        System.out.println("formkey:"+formKey);
        response.sendRedirect(formKey);
    }

    @GetMapping("/form/{procDefId}/{taskId}")
    public void form(@PathVariable("procDefId") String procDefId,@PathVariable("taskId") String taskId ,HttpServletResponse response) throws IOException {
        // 获取流程XML上的表单KEY
        String formKey = actTaskService.getFormKey(procDefId, taskId);
        response.sendRedirect(formKey+"/"+taskId);
    }

    @GetMapping("/todo")
    ModelAndView todo(){
        return new ModelAndView("act/task/todoTask");
    }

     /**
     * 查询我的个人任务
     * @return
     */
    @GetMapping("/todoList")
    List<TaskVO> todoList(){
        //查询当前用户需要办理的任务
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(ShiroUtils.getUser().getUsername()).list();
        List<TaskVO> taskVOS =  new ArrayList<>();
        for(Task task : tasks){
            String businessKey = activitiUtils.getBusinessKeyByTaskId(task.getId());
            SalaryDO salaryDO = salaryService.get(businessKey);;
            UserDTO userDTO = userService.get(Long.valueOf(salaryDO.getUserId()));
            TaskVO taskVO = new TaskVO(task);
            taskVO.setProName(ActivitiConstant.SALARY_NAME);
            taskVO.setUserName(userDTO.getName());
            taskVOS.add(taskVO);
        }
        return taskVOS;
    }

    @GetMapping("/hisList")
    List<TaskVO> hisList(){
        //查询出所有已完成任务
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(ShiroUtils.getUser().getName())
                .finished()
                .list();
        //将已经完成的任务存入TaskVo中供前端显示
        List<TaskVO> hisList = new ArrayList<>();
        for (HistoricTaskInstance instance : list) {
            String processDefinitionId = instance.getProcessDefinitionId();
            TaskVO t = new TaskVO();
            t.setKey(instance.getTaskDefinitionKey());
            t.setId(instance.getId());
            t.setProcessDefinitionId(processDefinitionId);
            t.setProcessId(instance.getProcessInstanceId());
            t.setName(instance.getName());
            t.setStartTime(instance.getStartTime());
            t.setEndTime(instance.getEndTime());
            hisList.add(t);

        }
    return hisList;
    }

    /**
     *读取带跟踪的图片
      */
    @RequestMapping(value = "/trace/photo/{procDefId}/{execId}")
    public void tracePhoto(@PathVariable("procDefId") String procDefId, @PathVariable("execId") String execId, HttpServletResponse response) throws Exception {
        InputStream imageStream = actTaskService.tracePhoto(procDefId, execId);
        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len;
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }


}
