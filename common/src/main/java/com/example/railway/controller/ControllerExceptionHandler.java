package com.example.railway.controller;

import com.example.railway.exception.BusinessException;
import com.example.railway.resp.CommonResq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

// 统一异常处理、数据预处理
@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    // 系统异常统一处理
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResq exceptionHandler(Exception e) throws Exception {
        CommonResq commonResq = new CommonResq();
        LOG.error("系统异常：", e);
        commonResq.setSuccess(false);
        commonResq.setMessage("系统出现异常，请联系管理员");
        return commonResq;
    }

    // 业务异常统一处理
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public CommonResq exceptionHandler(BusinessException e) {
        CommonResq commonResq = new CommonResq();
        LOG.error("业务异常：", e);
        commonResq.setSuccess(false);
        commonResq.setMessage(e.getE().getDesc());
        return commonResq;
    }

    // 校验异常统一处理
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public CommonResq exceptionHandler(BindException e) {
        CommonResq commonResq = new CommonResq();
        LOG.error("校验异常：", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        commonResq.setSuccess(false);
        commonResq.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return commonResq;
    }
}
