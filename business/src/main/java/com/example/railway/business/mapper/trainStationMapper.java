package com.example.railway.business.mapper;

import com.example.railway.business.domain.trainStation;
import com.example.railway.business.domain.trainStationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface trainStationMapper {
    long countByExample(trainStationExample example);

    int deleteByExample(trainStationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(trainStation record);

    int insertSelective(trainStation record);

    List<trainStation> selectByExample(trainStationExample example);

    trainStation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") trainStation record, @Param("example") trainStationExample example);

    int updateByExample(@Param("record") trainStation record, @Param("example") trainStationExample example);

    int updateByPrimaryKeySelective(trainStation record);

    int updateByPrimaryKey(trainStation record);
}