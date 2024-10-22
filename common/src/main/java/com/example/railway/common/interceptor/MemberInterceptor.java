package com.example.railway.common.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.railway.common.util.JwtUtil;
import com.example.railway.common.context.LoginMemberContext;
import com.example.railway.common.resp.MemberLoginResp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MemberInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(MemberInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LOG.info("MemberInterceptor开始");
        // 获取header的token参数
        String token = request.getHeader("token");
        if(StrUtil.isNotBlank(token)) {
            LOG.info("获取会员登录token:{}", token);
            JSONObject loginMember = JwtUtil.getJSONObject(token);
            LOG.info("当前登录用户:{}", loginMember);
            MemberLoginResp member = JSONUtil.toBean(loginMember, MemberLoginResp.class);
            LoginMemberContext.setMember(member);
        }
        LOG.info("MemberInterceptor结束");
        return true;
    }
}
