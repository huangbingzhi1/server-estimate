package com.hisense.serverestimate.mapper;

import com.hisense.serverestimate.entity.BaseCompany;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface BaseCompanyMapper {
    int deleteByPrimaryKey(String companyId);

    int insert(BaseCompany record);

    BaseCompany selectByPrimaryKey(String companyId);

    List<BaseCompany> selectAll();

    int updateByPrimaryKey(BaseCompany record);
}