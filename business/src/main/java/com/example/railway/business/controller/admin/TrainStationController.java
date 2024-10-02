package com.example.railway.business.controller.admin;

import com.example.railway.business.req.TrainStationQueryReq;
import com.example.railway.business.req.TrainStationSaveReq;
import com.example.railway.business.resp.TrainStationQueryResp;
import com.example.railway.business.service.TrainStationService;
import com.example.railway.resp.CommonResq;
import com.example.railway.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-station")
public class TrainStationController {

    @Resource
    private TrainStationService trainStationService;

    @PostMapping("/save")
    public CommonResq<Object> save(@Valid @RequestBody TrainStationSaveReq req) {
        trainStationService.save(req);
        return new CommonResq<>();
    }

    @GetMapping("/query-list")
    public CommonResq<PageResp<TrainStationQueryResp>> queryList(@Valid TrainStationQueryReq req) {
        PageResp<TrainStationQueryResp> list = trainStationService.queryList(req);
        return new CommonResq<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResq<Object> delete(@PathVariable Long id) {
        trainStationService.delete(id);
        return new CommonResq<>();
    }
}
