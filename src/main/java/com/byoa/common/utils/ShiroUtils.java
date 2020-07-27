package com.byoa.common.utils;

import com.byoa.system.domain.UserDTO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

public class ShiroUtils {

    @Autowired
    private static SessionDAO sessionDAO;

    public static Subject getSubjct() {
        return SecurityUtils.getSubject();
    }

    public static UserDTO getUser() {
        Object object = getSubjct().getPrincipal();
//        System.out.println("ShiroUtils:SecurityUtils.getSubject().getPrincipal():"+JackSonUtils.toJackString(SecurityUtils.getSubject().getPrincipal()));
        return (UserDTO) object;
    }

    public static Long getUserId() {
//        System.out.println("当前用户:"+ JackSonUtils.toJackString(getUser()));
        return getUser().getUserId();
    }

    public static void logout() {
        getSubjct().logout();
    }

    public static List<Principal> getPrinciples() {
        List<Principal> principals = null;
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        return principals;
    }
}
