package com.example.railway.business.mapper;

import java.util.Date;

public interface DailyTrainTicketMapperCust {
    void updateCountBySell(String date, String trainCode, String seatTypeCode, Integer minStartIndex, Integer maxStartIndex, Integer minEndIndex, Integer maxEndIndex);

}
