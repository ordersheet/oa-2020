package com.byoa.common.controller;

import com.byoa.system.domain.UserDTO;
import com.byoa.common.utils.ShiroUtils;
import org.springframework.stereotype.Controller;
/**
 *@autuor ysk@bysj
 */
@Controller
public class BaseController {
	public UserDTO getUser() {
		return ShiroUtils.getUser();
	}

	public Long getUserId() {
		return getUser().getUserId();
	}

	public String getUsername() {
		return getUser().getUsername();
	}
}