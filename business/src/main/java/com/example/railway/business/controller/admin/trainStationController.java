package com.example.railway.business.controller.admin;

import com.example.railway.context.LoginMemberContext;
import com.example.railway.business.req.trainStationQueryReq;
import com.example.railway.business.req.trainStationSaveReq;
import com.example.railway.business.resp.trainStationQueryResp;
import com.example.railway.business.service.trainStationService;
import com.example.railway.resp.CommonResq;
import com.example.railway.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-station")
public class trainStationController {

    @Resource
    private trainStationService trainStationService;

    @PostMapping("/save")
    public CommonResq<Object> save(@Valid @RequestBody trainStationSaveReq req) {
        trainStationService.save(req);
        return new CommonResq<>();
    }

    @GetMapping("/query-list")
    public CommonResq<PageResp<trainStationQueryResp>> queryList(@Valid trainStationQueryReq req) {
        PageResp<trainStationQueryResp> list = trainStationService.queryList(req);
        return new CommonResq<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResq<Object> delete(@PathVariable Long id) {
        trainStationService.delete(id);
        return new CommonResq<>();
    }
}
