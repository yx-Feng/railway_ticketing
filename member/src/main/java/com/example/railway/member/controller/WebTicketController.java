package com.example.railway.member.controller;

import com.example.railway.common.context.LoginMemberContext;
import com.example.railway.common.resp.CommonResp;
import com.example.railway.common.resp.PageResp;
import com.example.railway.member.req.TicketQueryReq;
import com.example.railway.member.resp.TicketQueryResp;
import com.example.railway.member.service.TicketService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
public class WebTicketController {

    @Resource
    private TicketService ticketService;

    @GetMapping("/query-list")
    public CommonResp<PageResp<TicketQueryResp>> queryList(@Valid TicketQueryReq req) {
        CommonResp<PageResp<TicketQueryResp>> commonResp = new CommonResp<>();
        req.setMemberId(LoginMemberContext.getId());
        PageResp<TicketQueryResp> pageResp = ticketService.queryList(req);
        commonResp.setContent(pageResp);
        return commonResp;
    }
}
