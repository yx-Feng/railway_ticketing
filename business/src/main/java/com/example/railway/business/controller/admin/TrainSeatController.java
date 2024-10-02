package com.example.railway.business.controller.admin;

import com.example.railway.business.req.TrainSeatQueryReq;
import com.example.railway.business.req.TrainSeatSaveReq;
import com.example.railway.business.resp.TrainSeatQueryResp;
import com.example.railway.business.service.TrainSeatService;
import com.example.railway.resp.CommonResq;
import com.example.railway.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-seat")
public class TrainSeatController {

    @Resource
    private TrainSeatService trainSeatService;

    @PostMapping("/save")
    public CommonResq<Object> save(@Valid @RequestBody TrainSeatSaveReq req) {
        trainSeatService.save(req);
        return new CommonResq<>();
    }

    @GetMapping("/query-list")
    public CommonResq<PageResp<TrainSeatQueryResp>> queryList(@Valid TrainSeatQueryReq req) {
        PageResp<TrainSeatQueryResp> list = trainSeatService.queryList(req);
        return new CommonResq<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResq<Object> delete(@PathVariable Long id) {
        trainSeatService.delete(id);
        return new CommonResq<>();
    }
}
