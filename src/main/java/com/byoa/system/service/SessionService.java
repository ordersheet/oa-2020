package com.byoa.system.service;

import java.util.Collection;
import java.util.List;

import com.byoa.system.domain.UserDTO;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Service;

import com.byoa.system.domain.UserOnline;

@Service
public interface SessionService {
	List<UserOnline> list();

	List<UserDTO> listOnlineUser();

	Collection<Session> sessionList();
	
	boolean forceLogout(String sessionId);
}
