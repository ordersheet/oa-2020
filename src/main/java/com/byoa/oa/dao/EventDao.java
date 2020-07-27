package com.byoa.oa.dao;

import com.byoa.oa.domain.Event;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EventDao {

  List<Event> listAll(Long userId);

  int update(Event record);

  int insert(Event record);

  int remove(Integer id);
}
