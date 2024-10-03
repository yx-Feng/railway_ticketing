package com.example.railway.batch.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CronJobResp {

    private String group;

    private String name;

    private String description;

    private String state;

    private String cronExpression;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date nextFireTime;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date preFireTime;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public Date getPreFireTime() {
        return preFireTime;
    }

    public void setPreFireTime(Date preFireTime) {
        this.preFireTime = preFireTime;
    }

    @Override
    public String toString() {
        return "CronJobResp{" +
                "group='" + group + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", state='" + state + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                ", nextFireTime=" + nextFireTime +
                ", preFireTime=" + preFireTime +
                '}';
    }
}
