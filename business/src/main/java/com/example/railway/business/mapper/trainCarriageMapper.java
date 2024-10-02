package com.example.railway.business.mapper;

import com.example.railway.business.domain.trainCarriage;
import com.example.railway.business.domain.trainCarriageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface trainCarriageMapper {
    long countByExample(trainCarriageExample example);

    int deleteByExample(trainCarriageExample example);

    int deleteByPrimaryKey(Long id);

    int insert(trainCarriage record);

    int insertSelective(trainCarriage record);

    List<trainCarriage> selectByExample(trainCarriageExample example);

    trainCarriage selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") trainCarriage record, @Param("example") trainCarriageExample example);

    int updateByExample(@Param("record") trainCarriage record, @Param("example") trainCarriageExample example);

    int updateByPrimaryKeySelective(trainCarriage record);

    int updateByPrimaryKey(trainCarriage record);
}