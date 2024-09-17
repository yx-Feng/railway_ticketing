package com.example.railway.member.controller;

import com.example.railway.member.req.MemberLoginReq;
import com.example.railway.member.req.MemberRegisterReq;
import com.example.railway.member.req.MemberSendCodeReq;
import com.example.railway.member.resp.MemberLoginResp;
import com.example.railway.member.service.MemberService;
import com.example.railway.resp.CommonResq;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private MemberService memberService;

    @GetMapping("/count")
    public CommonResq<Integer> count() {
        int count = memberService.count();
        CommonResq<Integer> commonResq = new CommonResq<Integer>();
        commonResq.setContent(count);
        return commonResq;
    }

    @PostMapping("/register")
    public CommonResq<Long> register(@Valid MemberRegisterReq req) {
        long register = memberService.register(req);
        CommonResq<Long> commonResq = new CommonResq<Long>();
        commonResq.setContent(register);
        return commonResq;
    }

    @PostMapping("/send-code")
    public CommonResq<Long> sendCode(@Valid @RequestBody MemberSendCodeReq req) {
        memberService.sendCode(req);
        CommonResq<Long> commonResq = new CommonResq<Long>();
        return commonResq;
    }

    @PostMapping("/login")
    public CommonResq<MemberLoginResp> login(@Valid @RequestBody MemberLoginReq req) {
        MemberLoginResp resp = memberService.login(req);
        return new CommonResq<MemberLoginResp>(resp);
    }
}
