package com.byoa.activiti.domain;

import java.util.Date;
/**
 *@autuor ysk@bysj
 */
public class VolidayDO extends TaskDO {

    private String id;
    //流程实例ID
    private String procInsId;

    //变动用户
    private String userId;

    private Date startDate;

    private Date stopDate;

    private String hrText;
    //分管领导意见
    private String leaderText;
    //集团主要领导意见
    private Integer type;

    private Integer flag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStopDate() {
        return stopDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
    }

    public String getHrText() {
        return hrText;
    }

    public void setHrText(String hrText) {
        this.hrText = hrText;
    }

    public String getLeaderText() {
        return leaderText;
    }

    public void setLeaderText(String leaderText) {
        this.leaderText = leaderText;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
