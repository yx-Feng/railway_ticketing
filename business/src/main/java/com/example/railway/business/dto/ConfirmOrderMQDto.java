package com.example.railway.business.dto;

import java.util.Date;

public class ConfirmOrderMQDto {

    private Date date;

    private String trainCode;
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    @Override
    public String toString() {
        return "ConfirmOrderMQDto{" +
                "date=" + date +
                ", trainCode='" + trainCode + '\'' +
                '}';
    }
}
