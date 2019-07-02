package com.hisense.serverestimate.mapper;

import com.hisense.serverestimate.entity.XsAccount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface XsAccountMapper {
    int deleteByPrimaryKey(String aid);

    int insert(XsAccount record);

    XsAccount selectByPrimaryKey(String aid);

    List<XsAccount> selectAll();

    int updateByPrimaryKey(XsAccount record);

    XsAccount selectByAccount(String account);
}