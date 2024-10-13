package com.example.railway.member.controller.admin;

import com.example.railway.member.req.TicketQueryReq;
import com.example.railway.member.resp.TicketQueryResp;
import com.example.railway.member.service.TicketService;
import com.example.railway.common.resp.CommonResp;
import com.example.railway.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/ticket")
public class TicketController {

    @Resource
    private TicketService ticketService;

    @GetMapping("/query-list")
    public CommonResp<PageResp<TicketQueryResp>> queryList(@Valid TicketQueryReq req) {
        PageResp<TicketQueryResp> list = ticketService.queryList(req);
        return new CommonResp<>(list);
    }
}
