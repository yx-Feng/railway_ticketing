package com.example.railway.member.controller;

import com.example.railway.member.req.MemberLoginReq;
import com.example.railway.member.req.MemberRegisterReq;
import com.example.railway.member.req.MemberSendCodeReq;
import com.example.railway.member.resp.MemberLoginResp;
import com.example.railway.member.service.MemberService;
import com.example.railway.resp.CommonResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Resource
    private MemberService memberService;

    @GetMapping("/count")
    public CommonResp<Integer> count() {
        int count = memberService.count();
        CommonResp<Integer> commonResp = new CommonResp<Integer>();
        commonResp.setContent(count);
        return commonResp;
    }

    @PostMapping("/register")
    public CommonResp<Long> register(@Valid MemberRegisterReq req) {
        long register = memberService.register(req);
        CommonResp<Long> commonResp = new CommonResp<Long>();
        commonResp.setContent(register);
        return commonResp;
    }

    @PostMapping("/send-code")
    public CommonResp<Long> sendCode(@Valid @RequestBody MemberSendCodeReq req) {
        memberService.sendCode(req);
        CommonResp<Long> commonResp = new CommonResp<Long>();
        return commonResp;
    }

    @PostMapping("/login")
    public CommonResp<MemberLoginResp> login(@Valid @RequestBody MemberLoginReq req) {
        MemberLoginResp resp = memberService.login(req);
        return new CommonResp<MemberLoginResp>(resp);
    }
}
