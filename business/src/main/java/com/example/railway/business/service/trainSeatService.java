package com.example.railway.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.example.railway.context.LoginMemberContext;
import com.example.railway.business.domain.trainSeat;
import com.example.railway.business.domain.trainSeatExample;
import com.example.railway.business.mapper.trainSeatMapper;
import com.example.railway.business.req.trainSeatQueryReq;
import com.example.railway.business.req.trainSeatSaveReq;
import com.example.railway.business.resp.trainSeatQueryResp;
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
public class trainSeatService {

    private static final Logger LOG = LoggerFactory.getLogger(trainSeatService.class);
    @Resource
    private trainSeatMapper trainSeatMapper;

    // 新增乘车人
    public void save(trainSeatSaveReq req) {
        DateTime now = DateTime.now();
        trainSeat trainSeat = BeanUtil.copyProperties(req, trainSeat.class);
        // id为空，表示新增操作，不为空，表示更新操作
        if(ObjectUtil.isNull(trainSeat.getId())) {
            trainSeat.setId(SnowUtil.getSnowflakeNextId());
            trainSeat.setCreateTime(now);
            trainSeat.setUpdateTime(now);
            trainSeatMapper.insert(trainSeat);
        } else {
            trainSeat.setUpdateTime(now);
            trainSeatMapper.updateByPrimaryKey(trainSeat);
        }
    }

    // 查询乘车人列表
    public PageResp<trainSeatQueryResp> queryList(trainSeatQueryReq req) {
        trainSeatExample trainSeatExample = new trainSeatExample();
        trainSeatExample.setOrderByClause("id desc");
        trainSeatExample.Criteria criteria = trainSeatExample.createCriteria();

        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(),req.getSize()); // 分页查询，对这句往下的第一个SQL做拦截，增加分页limit
        List<trainSeat> trainSeatList = trainSeatMapper.selectByExample(trainSeatExample);

        PageInfo<trainSeat> pageInfo = new PageInfo<>(trainSeatList);
        LOG.info("总条数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<trainSeatQueryResp> list = BeanUtil.copyToList(trainSeatList, trainSeatQueryResp.class);

        PageResp<trainSeatQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    public void delete(Long id) {
        trainSeatMapper.deleteByPrimaryKey(id);
    }
}