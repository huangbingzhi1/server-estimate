package com.hisense.serverestimate.mapper;

import com.hisense.serverestimate.entity.BaseEnterprise;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mapper
public interface BaseEnterpriseMapper {
    int deleteByPrimaryKey(String enterpriseId);

    int insert(BaseEnterprise record);

    BaseEnterprise selectByPrimaryKey(String enterpriseId);

    List<BaseEnterprise> selectAll();

    int updateByPrimaryKey(BaseEnterprise record);

    void deleteAll();

    void insertAll(Collection<BaseEnterprise> values);

    List<BaseEnterprise> getEnterpriseList(Map<String, Object> param);

    int getEnterpriseListNum(Map<String, Object> param);
}