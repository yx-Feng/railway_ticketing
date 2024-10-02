package com.example.railway.business.enums;

import java.math.BigDecimal;

public enum TrainTypeEnum {

    G("G", "高铁", new BigDecimal("1.2")),
    D("D", "动车", new BigDecimal("1")),
    K("K", "快速", new BigDecimal("0.8"));

    private String code;
    private String desc;

    // 票价比例，例：1.1，则票价=1.1 * 每公里单价（SeatTypeEnum.price） * 公里
    private BigDecimal priceRate;

    TrainTypeEnum(String code, String desc, BigDecimal priceRate) {
        this.code = code;
        this.desc = desc;
        this.priceRate = priceRate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "TrainTypeEnum{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                ", priceRate=" + priceRate +
                '}';
    }
}
