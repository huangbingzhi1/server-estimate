package com.hisense.serverestimate.mapper;

import com.hisense.serverestimate.entity.BaseUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    List<BaseUser> getUserList(Map<String, Object> param);

    int getUserListNum();

    void resetPassword(@Param("userId")String userId, @Param("newPassword")String newPassword);

    BaseUser getUserByUsername(String jsonParam);
}