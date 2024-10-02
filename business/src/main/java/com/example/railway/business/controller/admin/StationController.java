package com.example.railway.business.controller.admin;

import com.example.railway.business.req.StationQueryReq;
import com.example.railway.business.req.StationSaveReq;
import com.example.railway.business.resp.StationQueryResp;
import com.example.railway.business.service.StationService;
import com.example.railway.resp.CommonResq;
import com.example.railway.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/station")
public class StationController {

    @Resource
    private StationService stationService;

    @PostMapping("/save")
    public CommonResq<Object> save(@Valid @RequestBody StationSaveReq req) {
        stationService.save(req);
        return new CommonResq<>();
    }

    @GetMapping("/query-list")
    public CommonResq<PageResp<StationQueryResp>> queryList(@Valid StationQueryReq req) {
        PageResp<StationQueryResp> list = stationService.queryList(req);
        return new CommonResq<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResq<Object> delete(@PathVariable Long id) {
        stationService.delete(id);
        return new CommonResq<>();
    }
}
