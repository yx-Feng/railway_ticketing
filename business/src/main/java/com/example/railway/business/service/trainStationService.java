package com.example.railway.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.example.railway.context.LoginMemberContext;
import com.example.railway.business.domain.trainStation;
import com.example.railway.business.domain.trainStationExample;
import com.example.railway.business.mapper.trainStationMapper;
import com.example.railway.business.req.trainStationQueryReq;
import com.example.railway.business.req.trainStationSaveReq;
import com.example.railway.business.resp.trainStationQueryResp;
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
public class trainStationService {

    private static final Logger LOG = LoggerFactory.getLogger(trainStationService.class);
    @Resource
    private trainStationMapper trainStationMapper;

    // 新增乘车人
    public void save(trainStationSaveReq req) {
        DateTime now = DateTime.now();
        trainStation trainStation = BeanUtil.copyProperties(req, trainStation.class);
        // id为空，表示新增操作，不为空，表示更新操作
        if(ObjectUtil.isNull(trainStation.getId())) {
            trainStation.setId(SnowUtil.getSnowflakeNextId());
            trainStation.setCreateTime(now);
            trainStation.setUpdateTime(now);
            trainStationMapper.insert(trainStation);
        } else {
            trainStation.setUpdateTime(now);
            trainStationMapper.updateByPrimaryKey(trainStation);
        }
    }

    // 查询乘车人列表
    public PageResp<trainStationQueryResp> queryList(trainStationQueryReq req) {
        trainStationExample trainStationExample = new trainStationExample();
        trainStationExample.setOrderByClause("id desc");
        trainStationExample.Criteria criteria = trainStationExample.createCriteria();

        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(),req.getSize()); // 分页查询，对这句往下的第一个SQL做拦截，增加分页limit
        List<trainStation> trainStationList = trainStationMapper.selectByExample(trainStationExample);

        PageInfo<trainStation> pageInfo = new PageInfo<>(trainStationList);
        LOG.info("总条数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<trainStationQueryResp> list = BeanUtil.copyToList(trainStationList, trainStationQueryResp.class);

        PageResp<trainStationQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    public void delete(Long id) {
        trainStationMapper.deleteByPrimaryKey(id);
    }
}