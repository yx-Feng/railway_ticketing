package com.example.railway.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import com.example.railway.context.LoginMemberContext;
import com.example.railway.member.domain.Passenger;
import com.example.railway.member.mapper.PassengerMapper;
import com.example.railway.member.req.PassengerSaveReq;
import com.example.railway.util.SnowUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class PassengerService {

    @Resource
    private PassengerMapper passengerMapper;

    public void save(PassengerSaveReq req) {
        DateTime now = DateTime.now();
        Passenger passenger = BeanUtil.copyProperties(req, Passenger.class);
        passenger.setMemberId(LoginMemberContext.getId());
        passenger.setId(SnowUtil.getSnowflakeNextId());
        passenger.setCreateTime(now);
        passenger.setUpdateTime(now);
        passengerMapper.insert(passenger);
    }

}
