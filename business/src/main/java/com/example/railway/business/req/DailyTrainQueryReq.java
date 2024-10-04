package com.example.railway.business.req;

import com.example.railway.req.PageReq;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class DailyTrainQueryReq extends PageReq {

    private String code;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "DailyTrainQueryReq{" +
                "code='" + code + '\'' +
                ", date=" + date +
                '}';
    }
}