package com.byoa.activiti.service;

import com.byoa.activiti.domain.SalaryDO;

import java.util.List;
import java.util.Map;

/**
 *@autuor ysk@bysj
 */
public interface SalaryService {
	
	SalaryDO get(String id);
	
	List<SalaryDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(SalaryDO salary);
	
	int update(SalaryDO salary);
	
	int remove(String id);
	
	int batchRemove(String[] ids);
}
