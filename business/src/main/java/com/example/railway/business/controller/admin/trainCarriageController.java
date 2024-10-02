package com.example.railway.business.controller.admin;

import com.example.railway.context.LoginMemberContext;
import com.example.railway.business.req.trainCarriageQueryReq;
import com.example.railway.business.req.trainCarriageSaveReq;
import com.example.railway.business.resp.trainCarriageQueryResp;
import com.example.railway.business.service.trainCarriageService;
import com.example.railway.resp.CommonResq;
import com.example.railway.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/train-carriage")
public class trainCarriageController {

    @Resource
    private trainCarriageService trainCarriageService;

    @PostMapping("/save")
    public CommonResq<Object> save(@Valid @RequestBody trainCarriageSaveReq req) {
        trainCarriageService.save(req);
        return new CommonResq<>();
    }

    @GetMapping("/query-list")
    public CommonResq<PageResp<trainCarriageQueryResp>> queryList(@Valid trainCarriageQueryReq req) {
        PageResp<trainCarriageQueryResp> list = trainCarriageService.queryList(req);
        return new CommonResq<>(list);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResq<Object> delete(@PathVariable Long id) {
        trainCarriageService.delete(id);
        return new CommonResq<>();
    }
}
