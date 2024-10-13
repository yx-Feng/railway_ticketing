package com.example.railway.member.req;

import com.example.railway.common.req.PageReq;

public class TicketQueryReq extends PageReq {

    private Long memberId;

    public TicketQueryReq(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "TicketQueryReq{" +
                "memberId=" + memberId +
                '}';
    }
}