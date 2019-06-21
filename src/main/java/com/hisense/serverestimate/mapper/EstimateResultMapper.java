package com.hisense.serverestimate.mapper;

import com.hisense.serverestimate.entity.EstimateResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface EstimateResultMapper {
    int deleteByPrimaryKey(String resultId);

    int insert(EstimateResult record);

    EstimateResult selectByPrimaryKey(String resultId);

    List<EstimateResult> selectAll();

    int updateByPrimaryKey(EstimateResult record);
}