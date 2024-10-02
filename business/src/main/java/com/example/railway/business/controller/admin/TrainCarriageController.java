package com.example.railway.business.controller.admin;

import com.example.railway.business.req.TrainCarriageQueryReq;
import com.example.railway.business.req.TrainCarriageSaveReq;
import com.example.railway.business.resp.TrainCarriageQueryResp;
import com.example.railway.business.service.TrainCarriageService;
import com.example.railway.resp.CommonResq;
import com.example.railway.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-carriage")
public class TrainCarriageController {

    @Resource
    private TrainCarriageService trainCarriageService;

    @PostMapping("/save")
    public CommonResq<Object> save(@Valid @RequestBody TrainCarriageSaveReq req) {
        trainCarriageService.save(req);
        return new CommonResq<>();
    }

    @GetMapping("/query-list")
    public CommonResq<PageResp<TrainCarriageQueryResp>> queryList(@Valid TrainCarriageQueryReq req) {
        PageResp<TrainCarriageQueryResp> list = trainCarriageService.queryList(req);
        return new CommonResq<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResq<Object> delete(@PathVariable Long id) {
        trainCarriageService.delete(id);
        return new CommonResq<>();
    }
}
