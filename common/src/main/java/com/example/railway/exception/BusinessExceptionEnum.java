package com.example.railway.exception;

public enum BusinessExceptionEnum {
    MEMEBER_MOBILE_EXIST("手机号已注册"),
    MEMEBER_MOBILE_NOT_EXIST("手机号不存在"),
    MEMEBER_MOBILE_CODE_ERROR("短信验证码错误");

    private String desc;

    BusinessExceptionEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "BusinessExceptionEnum{" +
                "desc='" + desc + '\'' +
                '}';
    }
}
