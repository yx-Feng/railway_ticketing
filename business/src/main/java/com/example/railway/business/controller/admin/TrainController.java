package com.example.railway.business.controller.admin;

import com.example.railway.context.LoginMemberContext;
import com.example.railway.business.req.TrainQueryReq;
import com.example.railway.business.req.TrainSaveReq;
import com.example.railway.business.resp.TrainQueryResp;
import com.example.railway.business.service.TrainService;
import com.example.railway.resp.CommonResq;
import com.example.railway.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train")
public class TrainController {

    @Resource
    private TrainService trainService;

    @PostMapping("/save")
    public CommonResq<Object> save(@Valid @RequestBody TrainSaveReq req) {
        trainService.save(req);
        return new CommonResq<>();
    }

    @GetMapping("/query-list")
    public CommonResq<PageResp<TrainQueryResp>> queryList(@Valid TrainQueryReq req) {
        PageResp<TrainQueryResp> list = trainService.queryList(req);
        return new CommonResq<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResq<Object> delete(@PathVariable Long id) {
        trainService.delete(id);
        return new CommonResq<>();
    }
}