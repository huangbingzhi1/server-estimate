package com.hisense.serverestimate.mapper;

import com.hisense.serverestimate.entity.BaseEnterprise;
import com.hisense.serverestimate.entity.BaseServer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    void addEnterpriseByServerCode(@Param("serverCode")String serverCode, @Param("cisArray")String[] cisArray);

    void deleteEnterpriseByServerCode(String serverCode);

    void deleteServerByEnterpriseId(String jsonParam);

    BaseEnterprise getEnterpriseByCis(String jsonParam);

    void addServerRelByEnterpriseCis(@Param("cis")String cis, @Param("serverCodeArray")String[] split);
}