package com.example.railway.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.example.railway.business.domain.Train;
import com.example.railway.business.domain.TrainCarriage;
import com.example.railway.business.domain.TrainExample;
import com.example.railway.business.mapper.TrainMapper;
import com.example.railway.business.req.TrainQueryReq;
import com.example.railway.business.req.TrainSaveReq;
import com.example.railway.business.resp.TrainQueryResp;
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

import java.util.List;

@Service
public class TrainService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainService.class);
    @Resource
    private TrainMapper trainMapper;

    // 新增车次
    public void save(TrainSaveReq req) {
        DateTime now = DateTime.now();
        Train train = BeanUtil.copyProperties(req, Train.class);
        // id为空，表示新增操作，不为空，表示更新操作
        if(ObjectUtil.isNull(train.getId())) {
            // 保存之前，先校验唯一键是否存在
            Train trainDB = selectByUnique(req.getCode());
            if (ObjectUtil.isNotNull(trainDB)) {
                throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_STATION_CODE_UNIQUE_ERROR);
            }
            train.setId(SnowUtil.getSnowflakeNextId());
            train.setCreateTime(now);
            train.setUpdateTime(now);
            trainMapper.insert(train);
        } else {
            train.setUpdateTime(now);
            trainMapper.updateByPrimaryKey(train);
        }
    }

    // 按照唯一键查询
    private Train selectByUnique(String code) {
        TrainExample trainExample = new TrainExample();
        trainExample.createCriteria().andCodeEqualTo(code);
        List<Train> list = trainMapper.selectByExample(trainExample);
        if(CollUtil.isNotEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    // 查询车次列表
    public PageResp<TrainQueryResp> queryList(TrainQueryReq req) {
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("id desc");
        TrainExample.Criteria criteria = trainExample.createCriteria();

        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(),req.getSize()); // 分页查询，对这句往下的第一个SQL做拦截，增加分页limit
        List<Train> trainList = trainMapper.selectByExample(trainExample);

        PageInfo<Train> pageInfo = new PageInfo<>(trainList);
        LOG.info("总条数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<TrainQueryResp> list = BeanUtil.copyToList(trainList, TrainQueryResp.class);

        PageResp<TrainQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    public void delete(Long id) {
        trainMapper.deleteByPrimaryKey(id);
    }

    public List<TrainQueryResp> queryAll() {
        TrainExample trainExample = new TrainExample();
        trainExample.setOrderByClause("code asc");
        List<Train> trainList = trainMapper.selectByExample(trainExample);
        return BeanUtil.copyToList(trainList, TrainQueryResp.class);
    }
}