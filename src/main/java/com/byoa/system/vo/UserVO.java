package com.byoa.system.vo;

import com.byoa.system.domain.UserDTO;

public class UserVO {
    /**
     * 更新的用户对象
     */
    private UserDTO userDO = new UserDTO();
    /**
     * 旧密码
     */
    private String pwdOld;
    /**
     * 新密码
     */
    private String pwdNew;

    public UserDTO getUserDO() {
        return userDO;
    }

    public void setUserDO(UserDTO userDO) {
        this.userDO = userDO;
    }

    public String getPwdOld() {
        return pwdOld;
    }

    public void setPwdOld(String pwdOld) {
        this.pwdOld = pwdOld;
    }

    public String getPwdNew() {
        return pwdNew;
    }

    public void setPwdNew(String pwdNew) {
        this.pwdNew = pwdNew;
    }
}
