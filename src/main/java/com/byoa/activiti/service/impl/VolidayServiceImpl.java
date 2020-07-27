package com.byoa.activiti.service.impl;
import com.byoa.activiti.dao.VolidayDao;
import com.byoa.activiti.domain.VolidayDO;
import com.byoa.activiti.service.VolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
/**
 *@autuor ysk@bysj
 */
public class VolidayServiceImpl  implements VolidayService {

    @Autowired
    private VolidayDao volidayDao;

    @Autowired
    private ActTaskServiceImpl actTaskService;

    @Override
    public VolidayDO get(String id) {
        return volidayDao.get(id);
    }

    @Override
    public List<VolidayDO> list(Map<String, Object> map) {
        return volidayDao.list(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return volidayDao.count(map);
    }


    @Transactional(rollbackFor=Exception.class)
    @Override
    public int save(VolidayDO voliday) {
        voliday.setId(UUID.randomUUID().toString().replace("-",""));
//        actTaskService.startProcess()
        return 0;
    }

    @Override
    public int update(VolidayDO voliday) {
        return 0;
    }

    @Override
    public int remove(String id) {
        return 0;
    }

    @Override
    public int batchRemove(String[] ids) {
        return 0;
    }
}
