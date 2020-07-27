package com.byoa.oa.service;

import com.byoa.oa.domain.Event;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface EventService {
    List<Event> listAll(Long userId);
    int insert(Event record);
    int update(Event record);
    int remove(Integer id);
}
