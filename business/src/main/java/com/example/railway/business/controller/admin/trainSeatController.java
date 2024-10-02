package com.example.railway.business.controller.admin;

import com.example.railway.context.LoginMemberContext;
import com.example.railway.business.req.trainSeatQueryReq;
import com.example.railway.business.req.trainSeatSaveReq;
import com.example.railway.business.resp.trainSeatQueryResp;
import com.example.railway.business.service.trainSeatService;
import com.example.railway.resp.CommonResq;
import com.example.railway.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-seat")
public class trainSeatController {

    @Resource
    private trainSeatService trainSeatService;

    @PostMapping("/save")
    public CommonResq<Object> save(@Valid @RequestBody trainSeatSaveReq req) {
        trainSeatService.save(req);
        return new CommonResq<>();
    }

    @GetMapping("/query-list")
    public CommonResq<PageResp<trainSeatQueryResp>> queryList(@Valid trainSeatQueryReq req) {
        PageResp<trainSeatQueryResp> list = trainSeatService.queryList(req);
        return new CommonResq<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResq<Object> delete(@PathVariable Long id) {
        trainSeatService.delete(id);
        return new CommonResq<>();
    }
}
