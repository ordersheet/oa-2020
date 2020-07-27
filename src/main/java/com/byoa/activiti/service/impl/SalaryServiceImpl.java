package com.byoa.activiti.service.impl;

import com.byoa.activiti.config.ActivitiConstant;
import com.byoa.activiti.dao.SalaryDao;
import com.byoa.activiti.domain.SalaryDO;
import com.byoa.activiti.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/**
 *@autuor ysk@bysj
 */
import org.springframework.transaction.annotation.Transactional;

@Service
public class SalaryServiceImpl implements SalaryService {
	@Autowired
	private SalaryDao salaryDao;

	@Autowired
	private ActTaskServiceImpl actTaskService;
	
	@Override
	public SalaryDO get(String id){
		return salaryDao.get(id);
	}
	
	@Override
	public List<SalaryDO> list(Map<String, Object> map){
		return salaryDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return salaryDao.count(map);
	}


	@Transactional(rollbackFor=Exception.class)
	@Override
	public int save(SalaryDO salary){
		System.out.println("保存一个salary表，表中数据为：");
		System.out.println("salary:"+salary.toString());
		//这里保存信息后开始启动流程
		//设置一个ID
		salary.setId(UUID.randomUUID().toString().replace("-",""));
		//processKey = ActivitiConstant.ACTIVITI_SALARY[0] = salary
		//businessTable = ActivitiConstant.ACTIVITI_SALARY[1] = salary
		actTaskService.startProcess(ActivitiConstant.ACTIVITI_SALARY[0],ActivitiConstant.ACTIVITI_SALARY[1],salary.getId(),salary.getContent(),new HashMap<>());
		return salaryDao.save(salary);
	}

	@Transactional(rollbackFor=Exception.class)
	@Override
	public int update(SalaryDO salary){
		System.out.println("update salary:"+salary.toString());
		Map<String,Object> vars = new HashMap<>(16);
		vars.put("pass",  salary.getTaskPass() );
		vars.put("title","");
		//这里完成任务  传递过来的pass = 0 或者 1 也就是流转条件
		actTaskService.complete(salary.getTaskId(),vars);
		return salaryDao.update(salary);
	}
	
	@Override
	public int remove(String id){
		return salaryDao.remove(id);
	}
	
	@Override
	public int batchRemove(String[] ids){
		return salaryDao.batchRemove(ids);
	}
	
}
