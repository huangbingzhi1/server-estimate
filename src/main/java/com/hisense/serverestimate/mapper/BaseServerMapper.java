package com.hisense.serverestimate.mapper;

import com.hisense.serverestimate.entity.BaseServer;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mapper
public interface BaseServerMapper {
    int deleteByPrimaryKey(String serverId);

    int insert(BaseServer record);

    BaseServer selectByPrimaryKey(String serverId);

    List<BaseServer> selectAll();

    int updateByPrimaryKey(BaseServer record);

    List<BaseServer> getServerList(Map<String, Object> param);

    int getServerListNum(Map<String, Object> param);

    void deleteAll();

    void insertAll(Collection<BaseServer> values);

    BaseServer getServerByServerCode(String serverCode);

    void deleteEnterpriseByServerId(String jsonParam);
}