package com.example.railway.business.controller.admin;

import com.example.railway.context.LoginMemberContext;
import com.example.railway.business.req.DailyTrainStationQueryReq;
import com.example.railway.business.req.DailyTrainStationSaveReq;
import com.example.railway.business.resp.DailyTrainStationQueryResp;
import com.example.railway.business.service.DailyTrainStationService;
import com.example.railway.resp.CommonResp;
import com.example.railway.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/daily-train-station")
public class DailyTrainStationController {

    @Resource
    private DailyTrainStationService dailyTrainStationService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody DailyTrainStationSaveReq req) {
        dailyTrainStationService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainStationQueryResp>> queryList(@Valid DailyTrainStationQueryReq req) {
        PageResp<DailyTrainStationQueryResp> list = dailyTrainStationService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        dailyTrainStationService.delete(id);
        return new CommonResp<>();
    }
}
