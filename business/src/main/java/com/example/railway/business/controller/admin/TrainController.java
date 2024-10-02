package com.example.railway.business.controller.admin;

import com.example.railway.business.service.TrainSeatService;
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

import java.util.List;

@RestController
@RequestMapping("/admin/train")
public class TrainController {

    @Resource
    private TrainService trainService;

    @Resource
    private TrainSeatService trainSeatService;

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

    @GetMapping("/query-all")
    public CommonResq<List<TrainQueryResp>> queryList() {
        List<TrainQueryResp> list = trainService.queryAll();
        return new CommonResq<>(list);
    }

    @GetMapping("/gen-seat/{trainCode}")
    public CommonResq<Object> genSeat(@PathVariable String trainCode) {
        trainSeatService.genTrainSeat(trainCode);
        return new CommonResq<>();
    }
}
