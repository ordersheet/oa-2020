package com.byoa.common.service;

import com.byoa.common.domain.LogDO;
import com.byoa.common.domain.PageDO;
import com.byoa.common.utils.Query;
import org.springframework.stereotype.Service;

@Service
public interface LogService {
	void save(LogDO logDO);
	PageDO<LogDO> queryList(Query query);
	int remove(Long id);
	int batchRemove(Long[] ids);
}
