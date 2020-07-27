package com.byoa.activiti.dao;

import com.byoa.activiti.domain.VolidayDO;

import java.util.List;
import java.util.Map;

/**
 *@autuor ysk@bysj
 */
public interface VolidayDao {
    VolidayDO get(String id);

    List<VolidayDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(VolidayDO salary);

    int update(VolidayDO salary);

    int remove(String id);

    int batchRemove(String[] ids);
}
