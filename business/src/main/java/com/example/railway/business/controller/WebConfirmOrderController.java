package com.example.railway.business.controller;

import com.example.railway.business.req.ConfirmOrderDoReq;
import com.example.railway.business.req.ConfirmOrderQueryReq;
import com.example.railway.business.resp.ConfirmOrderQueryResp;
import com.example.railway.business.service.ConfirmOrderService;
import com.example.railway.common.resp.CommonResp;
import com.example.railway.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/confirm-order")
public class WebConfirmOrderController {

    @Resource
    private ConfirmOrderService confirmOrderService;

    @PostMapping("/do")
    public CommonResp<Object> doConfirm(@Valid @RequestBody ConfirmOrderDoReq req) {
        confirmOrderService.doConfirm(req);
        return new CommonResp<>();
    }

    @GetMapping("/admin/query-list")
    public CommonResp<PageResp<ConfirmOrderQueryResp>> queryList(@Valid ConfirmOrderQueryReq req) {
        PageResp<ConfirmOrderQueryResp> list = confirmOrderService.queryList(req);
        return new CommonResp<>(list);
    }
}
