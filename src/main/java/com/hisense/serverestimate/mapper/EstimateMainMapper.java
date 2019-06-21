package com.hisense.serverestimate.mapper;

import com.hisense.serverestimate.entity.EstimateMain;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface EstimateMainMapper {
    int deleteByPrimaryKey(String mainId);

    int insert(EstimateMain record);

    EstimateMain selectByPrimaryKey(String mainId);

    List<EstimateMain> selectAll();

    int updateByPrimaryKey(EstimateMain record);
}