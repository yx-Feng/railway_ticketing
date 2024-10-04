package com.example.railway.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.example.railway.business.enums.SeatColEnum;
import com.example.railway.business.domain.DailyTrainCarriage;
import com.example.railway.business.domain.DailyTrainCarriageExample;
import com.example.railway.business.mapper.DailyTrainCarriageMapper;
import com.example.railway.business.req.DailyTrainCarriageQueryReq;
import com.example.railway.business.req.DailyTrainCarriageSaveReq;
import com.example.railway.business.resp.DailyTrainCarriageQueryResp;
import com.example.railway.exception.BusinessException;
import com.example.railway.exception.BusinessExceptionEnum;
import com.example.railway.resp.PageResp;
import com.example.railway.util.SnowUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DailyTrainCarriageService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainCarriageService.class);
    @Resource
    private DailyTrainCarriageMapper dailyTrainCarriageMapper;

    // 新增乘车人
    public void save(DailyTrainCarriageSaveReq req) {
        DateTime now = DateTime.now();

        // 自动计算出列数和总座位数
        List<SeatColEnum> seatColEnums = SeatColEnum.getColsByType(req.getSeatType());
        req.setColumnCount(seatColEnums.size());
        req.setSeatCount(req.getColumnCount()*req.getRowCount());

        DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(req, DailyTrainCarriage.class);
        // id为空，表示新增操作，不为空，表示更新操作
        if(ObjectUtil.isNull(dailyTrainCarriage.getId())) {
            // 保存之前，先校验唯一键是否存在
            DailyTrainCarriage dailyTrainCarriageDB = selectByUnique(req.getDate(), req.getTrainCode(), req.getIndex());
            if (ObjectUtil.isNotNull(dailyTrainCarriageDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_CARRIAGE_INDEX_UNIQUE_ERROR);
            }
            dailyTrainCarriage.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainCarriage.setCreateTime(now);
            dailyTrainCarriage.setUpdateTime(now);
            dailyTrainCarriageMapper.insert(dailyTrainCarriage);
        } else {
            dailyTrainCarriage.setUpdateTime(now);
            dailyTrainCarriageMapper.updateByPrimaryKey(dailyTrainCarriage);
        }
    }

    // 按照唯一键查询
    private DailyTrainCarriage selectByUnique(Date date, String trainCode , Integer index) {
        DailyTrainCarriageExample stationExample = new DailyTrainCarriageExample();
        stationExample.createCriteria().andDateEqualTo(date).andTrainCodeEqualTo(trainCode).andIndexEqualTo(index);
        List<DailyTrainCarriage> list = dailyTrainCarriageMapper.selectByExample(stationExample);
        if(CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    // 查询乘车人列表
    public PageResp<DailyTrainCarriageQueryResp> queryList(DailyTrainCarriageQueryReq req) {
        DailyTrainCarriageExample dailyTrainCarriageExample = new DailyTrainCarriageExample();
        dailyTrainCarriageExample.setOrderByClause("date desc, train_code asc, `index` asc");
        DailyTrainCarriageExample.Criteria criteria = dailyTrainCarriageExample.createCriteria();
        if (ObjectUtil.isNotNull(req.getDate())) {
            criteria.andDateEqualTo(req.getDate());
        }
        if (ObjectUtil.isNotEmpty(req.getTrainCode())){
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(),req.getSize()); // 分页查询，对这句往下的第一个SQL做拦截，增加分页limit
        List<DailyTrainCarriage> dailyTrainCarriageList = dailyTrainCarriageMapper.selectByExample(dailyTrainCarriageExample);

        PageInfo<DailyTrainCarriage> pageInfo = new PageInfo<>(dailyTrainCarriageList);
        LOG.info("总条数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<DailyTrainCarriageQueryResp> list = BeanUtil.copyToList(dailyTrainCarriageList, DailyTrainCarriageQueryResp.class);

        PageResp<DailyTrainCarriageQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    public void delete(Long id) {
        dailyTrainCarriageMapper.deleteByPrimaryKey(id);
    }
}