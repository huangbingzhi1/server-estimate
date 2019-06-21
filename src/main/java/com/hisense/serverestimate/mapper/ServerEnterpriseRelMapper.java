package com.hisense.serverestimate.mapper;

import com.hisense.serverestimate.entity.ServerEnterpriseRel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ServerEnterpriseRelMapper {
    int deleteByPrimaryKey(String id);

    int insert(ServerEnterpriseRel record);

    ServerEnterpriseRel selectByPrimaryKey(String id);

    List<ServerEnterpriseRel> selectAll();

    int updateByPrimaryKey(ServerEnterpriseRel record);

    void deleteAll();

    void insertAll(List<ServerEnterpriseRel> serverEnterpriseRels);
}