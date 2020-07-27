package com.byoa.oa.service.impl;

import com.byoa.oa.dao.EventDao;
import com.byoa.oa.domain.Event;
import com.byoa.oa.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventDao eventDao;


    @Override
    public List<Event> listAll(Long userId) {
        return eventDao.listAll(userId);
    }

    @Override
    public int insert(Event record) {
        return eventDao.insert(record);
    }

    @Override
    public int update(Event record) {
        return eventDao.update(record);
    }

    @Override
    public int remove(Integer id) {
        return eventDao.remove(id);
    }
}
