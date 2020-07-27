package com.byoa.activiti.service;

import com.byoa.activiti.domain.VolidayDO;

import java.util.List;
import java.util.Map;
/**
 *@autuor ysk@bysj
 */
public interface VolidayService {

    VolidayDO get(String id);

    List<VolidayDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(VolidayDO voliday);

    int update(VolidayDO voliday);

    int remove(String id);

    int batchRemove(String[] ids);
}
