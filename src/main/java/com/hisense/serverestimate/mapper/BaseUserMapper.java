package com.hisense.serverestimate.mapper;

import com.hisense.serverestimate.entity.BaseUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseUserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(BaseUser record);

    BaseUser selectByPrimaryKey(String userId);

    List<BaseUser> selectAll();

    int updateByPrimaryKey(BaseUser record);

    BaseUser getUserByNamePassword(Map<String, String> param);
}