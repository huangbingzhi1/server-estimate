package com.hisense.serverestimate.mapper;

import com.hisense.serverestimate.entity.ExamMain;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExamMainMapper {
    int deleteByPrimaryKey(String mainId);

    int insert(ExamMain record);

    ExamMain selectByPrimaryKey(String mainId);

    List<ExamMain> selectAll();

    int updateByPrimaryKey(ExamMain record);

    List<ExamMain> getExamList(Map<String, Object> param);

    int getExamListNum(Map<String, Object> param);

    List<Map<String, Object>> getExamInfoByCis(Map<String, Object> param);

    ExamMain selectByQid(String qid);

    void setExpireByPrimaryKey(String jsonParam);
}