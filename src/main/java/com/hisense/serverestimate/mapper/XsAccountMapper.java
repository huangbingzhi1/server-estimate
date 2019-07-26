package com.hisense.serverestimate.mapper;

import com.hisense.serverestimate.entity.XsAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface XsAccountMapper {
    int deleteByPrimaryKey(String aid);

    int insert(XsAccount record);

    XsAccount selectByPrimaryKey(String aid);

    List<XsAccount> selectAll();

    int updateByPrimaryKey(XsAccount record);

    XsAccount selectByAccount(String account);

    int setPassword(@Param("aid") String aid, @Param("password")String password);

    void clearAccountPassword();
}