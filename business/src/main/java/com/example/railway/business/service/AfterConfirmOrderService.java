package com.example.railway.business.service;

import com.example.railway.business.domain.DailyTrainSeat;
import com.example.railway.business.domain.DailyTrainTicket;
import com.example.railway.business.feign.MemberFeign;
import com.example.railway.business.mapper.ConfirmOrderMapper;
import com.example.railway.business.mapper.DailyTrainSeatMapper;
import com.example.railway.business.mapper.DailyTrainTicketMapperCust;
import com.example.railway.business.req.ConfirmOrderTicketReq;
import com.example.railway.common.context.LoginMemberContext;
import com.example.railway.common.req.MemberTicketReq;
import com.example.railway.common.resp.CommonResp;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class AfterConfirmOrderService {

    private static final Logger LOG = LoggerFactory.getLogger(AfterConfirmOrderService.class);

    @Resource
    DailyTrainSeatMapper dailyTrainSeatMapper;

    @Resource
    DailyTrainTicketMapperCust dailyTrainTicketMapperCust;

    @Resource
    MemberFeign memberFeign;

    /**
     * 选中座位后事务处理
     *  座位表修改售卖情况sell字段
     *  余票详情表修改余票
     *  为会员增加购票记录
     *  更新确认订单为成功
     */
    @Transactional
    public void afterDoConfirm(DailyTrainTicket dailyTrainTicket, List<DailyTrainSeat> finalSeatList, List<ConfirmOrderTicketReq> tickets) {
        for (int j = 0; j < finalSeatList.size(); j++) {
            DailyTrainSeat dailyTrainSeat = finalSeatList.get(j);
            DailyTrainSeat seatForUpdate = new DailyTrainSeat();
            seatForUpdate.setId(dailyTrainSeat.getId());
            seatForUpdate.setSell(dailyTrainSeat.getSell());
            seatForUpdate.setUpdateTime(new Date());
            dailyTrainSeatMapper.updateByPrimaryKeySelective(seatForUpdate);

            // 计算这个站卖出去后，影响了哪些站的余票库存
            // 影响的库存：本次选座之前没卖过票的，和本次购买区间有交集的区间
            // 假设0~9, 10个站, 本次买4~7站
            // 原售: 001000001
            // 购买: 000011100
            // 新售: 001011101
            // 影响:
            Integer startIndex = dailyTrainTicket.getStartIndex();
            Integer endIndex = dailyTrainTicket.getEndIndex();
            char[] chars = seatForUpdate.getSell().toCharArray();
            Integer maxStartIndex = endIndex - 1;
            Integer minEndIndex = startIndex + 1;
            Integer minStartIndex = 0;
            for (int i = startIndex-1; i >=0; i--)  {
                char aChar = chars[i];
                if (aChar == '1') {
                    minStartIndex = i+1;
                    break;
                }
            }
            LOG.info("影响出发站区间："+ minStartIndex + "-" + maxStartIndex);

            Integer maxEndIndex = seatForUpdate.getSell().length();
            for (int i = endIndex; i < seatForUpdate.getSell().length(); i++) {
                char aChar = chars[i];
                if (aChar == '1') {
                    maxEndIndex = i;
                    break;
                }
            }
            LOG.info("影响到达站区间："+minEndIndex+"-"+maxEndIndex);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(dailyTrainSeat.getDate());

            dailyTrainTicketMapperCust.updateCountBySell(formattedDate, dailyTrainSeat.getTrainCode(), dailyTrainSeat.getSeatType(), minStartIndex, maxStartIndex, minEndIndex, maxEndIndex);

            // 调用会员服务接口，为会员增加一张车票
            MemberTicketReq memberTicketReq = new MemberTicketReq();
            memberTicketReq.setMemberId(LoginMemberContext.getId());
            memberTicketReq.setPassengerId(tickets.get(j).getPassengerId());
            memberTicketReq.setPassengerName(tickets.get(j).getPassengerName());
            memberTicketReq.setDate(dailyTrainTicket.getDate());
            memberTicketReq.setTrainCode(dailyTrainTicket.getTrainCode());
            memberTicketReq.setCarriageIndex(dailyTrainSeat.getCarriageIndex());
            memberTicketReq.setRow(dailyTrainSeat.getRow());
            memberTicketReq.setCol(dailyTrainSeat.getCol());
            memberTicketReq.setStart(dailyTrainTicket.getStart());
            memberTicketReq.setStartTime(dailyTrainTicket.getStartTime());
            memberTicketReq.setEnd(dailyTrainTicket.getEnd());
            memberTicketReq.setEndTime(dailyTrainTicket.getEndTime());
            memberTicketReq.setSeatType(dailyTrainSeat.getSeatType());
            CommonResp<Object> commonResp = memberFeign.save(memberTicketReq);
            LOG.info("调用member接口，返回：{}", commonResp);
        }
    }
}
