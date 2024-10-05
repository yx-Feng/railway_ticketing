package com.example.railway.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.example.railway.business.domain.TrainStation;
import com.example.railway.business.domain.TrainStationExample;
import com.example.railway.business.mapper.TrainStationMapper;
import com.example.railway.business.req.TrainStationQueryReq;
import com.example.railway.business.req.TrainStationSaveReq;
import com.example.railway.business.resp.TrainStationQueryResp;
import com.example.railway.common.exception.BusinessException;
import com.example.railway.common.exception.BusinessExceptionEnum;
import com.example.railway.common.resp.PageResp;
import com.example.railway.common.util.SnowUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainStationService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainStationService.class);
    @Resource
    private TrainStationMapper trainStationMapper;

    // 新增乘车人
    public void save(TrainStationSaveReq req) {
        DateTime now = DateTime.now();
        TrainStation trainStation = BeanUtil.copyProperties(req, TrainStation.class);
        // id为空，表示新增操作，不为空，表示更新操作
        if(ObjectUtil.isNull(trainStation.getId())) {
            // 保存之前，先校验唯一键是否存在
            TrainStation trainStationDB = selectByUnique(req.getTrainCode(), req.getIndex());
            if (ObjectUtil.isNotNull(trainStationDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_STATION_INDEX_UNIQUE_ERROR);
            }
            trainStationDB = selectByUnique(req.getTrainCode(), req.getName());
            if (ObjectUtil.isNotNull(trainStationDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_STATION_NAME_UNIQUE_ERROR);
            }

            trainStation.setId(SnowUtil.getSnowflakeNextId());
            trainStation.setCreateTime(now);
            trainStation.setUpdateTime(now);
            trainStationMapper.insert(trainStation);
        } else {
            trainStation.setUpdateTime(now);
            trainStationMapper.updateByPrimaryKey(trainStation);
        }
    }

    // 按照唯一键查询trainCode和index
    private TrainStation selectByUnique(String trainCode , Integer index) {
        TrainStationExample stationExample = new TrainStationExample();
        stationExample.createCriteria().andTrainCodeEqualTo(trainCode).andIndexEqualTo(index);
        List<TrainStation> list = trainStationMapper.selectByExample(stationExample);
        if(CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    // 按照唯一键查询trainCode和name
    private TrainStation selectByUnique(String trainCode , String name) {
        TrainStationExample stationExample = new TrainStationExample();
        stationExample.createCriteria().andTrainCodeEqualTo(trainCode).andNameEqualTo(name);
        List<TrainStation> list = trainStationMapper.selectByExample(stationExample);
        if(CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    // 查询乘车人列表
    public PageResp<TrainStationQueryResp> queryList(TrainStationQueryReq req) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.setOrderByClause("id desc");
        TrainStationExample.Criteria criteria = trainStationExample.createCriteria();

        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(),req.getSize()); // 分页查询，对这句往下的第一个SQL做拦截，增加分页limit
        List<TrainStation> trainStationList = trainStationMapper.selectByExample(trainStationExample);

        PageInfo<TrainStation> pageInfo = new PageInfo<>(trainStationList);
        LOG.info("总条数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<TrainStationQueryResp> list = BeanUtil.copyToList(trainStationList, TrainStationQueryResp.class);

        PageResp<TrainStationQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    public void delete(Long id) {
        trainStationMapper.deleteByPrimaryKey(id);
    }

    public List<TrainStation> selectByTrainCode(String trainCode) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.setOrderByClause("train_code asc, `index` asc");
        trainStationExample.createCriteria().andTrainCodeEqualTo(trainCode);
        List<TrainStation> list = trainStationMapper.selectByExample(trainStationExample);
        return list;
    }
}