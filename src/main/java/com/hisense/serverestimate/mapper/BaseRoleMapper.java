package com.hisense.serverestimate.mapper;

import com.hisense.serverestimate.entity.BaseRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface BaseRoleMapper {
    int deleteByPrimaryKey(String roleId);

    int insert(BaseRole record);

    BaseRole selectByPrimaryKey(String roleId);

    List<BaseRole> selectAll();

    int updateByPrimaryKey(BaseRole record);

    List<BaseRole> getRoleByUserId(String userId);
}