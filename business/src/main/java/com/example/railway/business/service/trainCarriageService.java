package com.example.railway.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.example.railway.context.LoginMemberContext;
import com.example.railway.business.domain.trainCarriage;
import com.example.railway.business.domain.trainCarriageExample;
import com.example.railway.business.mapper.trainCarriageMapper;
import com.example.railway.business.req.trainCarriageQueryReq;
import com.example.railway.business.req.trainCarriageSaveReq;
import com.example.railway.business.resp.trainCarriageQueryResp;
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
public class trainCarriageService {

    private static final Logger LOG = LoggerFactory.getLogger(trainCarriageService.class);
    @Resource
    private trainCarriageMapper trainCarriageMapper;

    // 新增乘车人
    public void save(trainCarriageSaveReq req) {
        DateTime now = DateTime.now();
        trainCarriage trainCarriage = BeanUtil.copyProperties(req, trainCarriage.class);
        // id为空，表示新增操作，不为空，表示更新操作
        if(ObjectUtil.isNull(trainCarriage.getId())) {
            trainCarriage.setId(SnowUtil.getSnowflakeNextId());
            trainCarriage.setCreateTime(now);
            trainCarriage.setUpdateTime(now);
            trainCarriageMapper.insert(trainCarriage);
        } else {
            trainCarriage.setUpdateTime(now);
            trainCarriageMapper.updateByPrimaryKey(trainCarriage);
        }
    }

    // 查询乘车人列表
    public PageResp<trainCarriageQueryResp> queryList(trainCarriageQueryReq req) {
        trainCarriageExample trainCarriageExample = new trainCarriageExample();
        trainCarriageExample.setOrderByClause("id desc");
        trainCarriageExample.Criteria criteria = trainCarriageExample.createCriteria();

        LOG.info("查询页码：{}", req.getPage());
        LOG.info("每页条数：{}", req.getSize());
        PageHelper.startPage(req.getPage(),req.getSize()); // 分页查询，对这句往下的第一个SQL做拦截，增加分页limit
        List<trainCarriage> trainCarriageList = trainCarriageMapper.selectByExample(trainCarriageExample);

        PageInfo<trainCarriage> pageInfo = new PageInfo<>(trainCarriageList);
        LOG.info("总条数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        List<trainCarriageQueryResp> list = BeanUtil.copyToList(trainCarriageList, trainCarriageQueryResp.class);

        PageResp<trainCarriageQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);

        return pageResp;
    }

    public void delete(Long id) {
        trainCarriageMapper.deleteByPrimaryKey(id);
    }
}