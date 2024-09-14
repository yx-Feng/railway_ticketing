package com.example.railway.member.service;

import cn.hutool.core.collection.CollUtil;
import com.example.railway.member.domain.Member;
import com.example.railway.member.domain.MemberExample;
import com.example.railway.member.mapper.MemberMapper;
import com.example.railway.member.req.MemberRegisterReq;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Resource
    private MemberMapper memberMapper;

    public int count() {
        return (int) memberMapper.countByExample(null);
    }

    public long register(MemberRegisterReq req) {
        String mobile = req.getMobile();
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> list = memberMapper.selectByExample(memberExample);

        if(CollUtil.isNotEmpty(list)) {
            // return list.get(0).getId();
            throw new RuntimeException("手机号已注册");
        }

        Member member = new Member();
        member.setId(System.currentTimeMillis());
        member.setMobile(mobile);
        memberMapper.insert(member);
        return member.getId();
    }
}
