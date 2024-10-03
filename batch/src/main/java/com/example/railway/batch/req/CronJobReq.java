package com.example.railway.batch.req;

public class CronJobReq {
    private String group; // 分组

    private String name; // 类名

    private String description; // 描述

    private String cronExpression; // cron表达式

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

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    @Override
    public String toString() {
        return "CronJobReq{" +
                "group='" + group + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                '}';
    }
}
