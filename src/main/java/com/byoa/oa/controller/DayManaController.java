package com.byoa.oa.controller;


import com.byoa.common.utils.ShiroUtils;
import com.byoa.oa.domain.Event;
import com.byoa.oa.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/oa/daymana")
public class DayManaController {
    String prefix = "oa/daymana/";

    @Autowired
    EventService eventService;

    @GetMapping("")
    public String daymana(){
        return prefix+"daymana";
    }


    @ResponseBody
    @RequestMapping(value = "/PostTi",method = RequestMethod.POST)
    public Event postTi(Event event){
        event.setUserId(ShiroUtils.getUserId());
        int insert = eventService.insert(event);
        return event;
    }

    @ResponseBody
    @RequestMapping(value = "/PostDetail",method = RequestMethod.POST)
    public Event postDetail(Event event){
        int update = eventService.update(event);
        return event;
    }
    @GetMapping("/eventList")
    @ResponseBody
    public List<Event> events(){
        List<Event> events = eventService.listAll(ShiroUtils.getUserId());
        return events;
    }

    @PostMapping("/remove")
    @ResponseBody
    public Event remove(Integer id){
        int remove = eventService.remove(id);
        return new Event();
    }

}
