package com.example.railway.business.controller.admin;

import com.example.railway.context.LoginMemberContext;
import com.example.railway.business.req.DailyTrainCarriageQueryReq;
import com.example.railway.business.req.DailyTrainCarriageSaveReq;
import com.example.railway.business.resp.DailyTrainCarriageQueryResp;
import com.example.railway.business.service.DailyTrainCarriageService;
import com.example.railway.resp.CommonResp;
import com.example.railway.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/daily-train-carriage")
public class DailyTrainCarriageController {

    @Resource
    private DailyTrainCarriageService dailyTrainCarriageService;

    @PostMapping("/save")
    public CommonResp<Object> save(@Valid @RequestBody DailyTrainCarriageSaveReq req) {
        dailyTrainCarriageService.save(req);
        return new CommonResp<>();
    }

    @GetMapping("/query-list")
    public CommonResp<PageResp<DailyTrainCarriageQueryResp>> queryList(@Valid DailyTrainCarriageQueryReq req) {
        PageResp<DailyTrainCarriageQueryResp> list = dailyTrainCarriageService.queryList(req);
        return new CommonResp<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp<Object> delete(@PathVariable Long id) {
        dailyTrainCarriageService.delete(id);
        return new CommonResp<>();
    }
}
