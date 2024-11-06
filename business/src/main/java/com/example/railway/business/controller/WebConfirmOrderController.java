package com.example.railway.business.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.railway.business.req.ConfirmOrderDoReq;
import com.example.railway.business.req.ConfirmOrderQueryReq;
import com.example.railway.business.resp.ConfirmOrderQueryResp;
import com.example.railway.business.service.BeforeConfirmOrderService;
import com.example.railway.business.service.ConfirmOrderService;
import com.example.railway.common.exception.BusinessExceptionEnum;
import com.example.railway.common.resp.CommonResp;
import com.example.railway.common.resp.PageResp;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/confirm-order")
public class WebConfirmOrderController {

    private static final Logger LOG = LoggerFactory.getLogger(WebConfirmOrderController.class);

    @Resource
    private BeforeConfirmOrderService beforeConfirmOrderService;

    @Resource
    private ConfirmOrderService confirmOrderService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${spring.profiles.active}")
    private String env;

    @SentinelResource(value = "confirmOrderDo", blockHandler = "doConfirmBlock")
    @PostMapping("/do")
    public CommonResp<Object> doConfirm(@Valid @RequestBody ConfirmOrderDoReq req) {
        // 开发环境不做验证码，方便做压测
        if (!env.equals("dev")) {
            // 图形验证码校验
            String imageCodeToken = req.getImageCodeToken();
            String imageCode = req.getImageCode();
            String imageCodeRedis = redisTemplate.opsForValue().get(imageCodeToken);
            LOG.info("从redis中获取到的验证码：{}", imageCodeRedis);
            if (ObjectUtils.isEmpty(imageCodeRedis)) {
                return new CommonResp<>(false, "验证码已过期", null);
            }
            // 验证码校验，大小写忽略，提升体验
            if (!imageCodeRedis.equalsIgnoreCase(imageCode)) {
                return new CommonResp<>(false, "验证码不正确", null);
            } else {
                // 验证通过后，移除验证码
                redisTemplate.delete(imageCodeToken);
            }
        }

        Long id = beforeConfirmOrderService.beforeConfirm(req);
        return new CommonResp<>(String.valueOf(id));
    }

    @GetMapping("/query-line-count/{id}")
    public CommonResp<Integer> queryLineCount(@PathVariable Long id) {
        Integer count = confirmOrderService.queryLineCount(id);
        return new CommonResp<>(count);
    }

    @GetMapping("/cancel/{id}")
    public CommonResp<Integer> cancel(@PathVariable Long id) {
        Integer count = confirmOrderService.cancel(id);
        return new CommonResp<>(count);
    }

    public CommonResp<Object> doConfirmBlock(ConfirmOrderDoReq req, BlockException e) {
        LOG.info("购票请求被限流：{}", req);
        CommonResp<Object> commonResp = new CommonResp<>();
        commonResp.setSuccess(false);
        commonResp.setMessage(BusinessExceptionEnum.CONFIRM_ORDER_FLOW_EXCEPTION.getDesc());
        return commonResp;
    }

    @GetMapping("/admin/query-list")
    public CommonResp<PageResp<ConfirmOrderQueryResp>> queryList(@Valid ConfirmOrderQueryReq req) {
        PageResp<ConfirmOrderQueryResp> list = confirmOrderService.queryList(req);
        return new CommonResp<>(list);
    }
}
