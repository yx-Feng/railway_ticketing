package com.example.railway.member.controller;

import com.example.railway.context.LoginMemberContext;
import com.example.railway.member.req.${Domain}QueryReq;
import com.example.railway.member.req.${Domain}SaveReq;
import com.example.railway.member.resp.${Domain}QueryResp;
import com.example.railway.member.service.${Domain}Service;
import com.example.railway.resp.CommonResq;
import com.example.railway.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${do_main}")
public class ${Domain}Controller {

    @Resource
    private ${Domain}Service ${domain}Service;

    @PostMapping("/save")
    public CommonResq<Object> save(@Valid @RequestBody ${Domain}SaveReq req) {
        ${domain}Service.save(req);
        return new CommonResq<>();
    }

    @GetMapping("/query-list")
    public CommonResq<PageResp<${Domain}QueryResp>> queryList(@Valid ${Domain}QueryReq req) {
        req.setMemberId(LoginMemberContext.getId()); // 直接拿到memberId
        PageResp<${Domain}QueryResp> list = ${domain}Service.queryList(req);
        return new CommonResq<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResq<Object> delete(@PathVariable Long id) {
        ${domain}Service.delete(id);
        return new CommonResq<>();
    }
}
