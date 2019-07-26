package com.hisense.serverestimate.mapper;

import com.hisense.serverestimate.entity.ExamDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExamDetailMapper {
    int deleteByPrimaryKey(String detailId);

    int insert(ExamDetail record);

    ExamDetail selectByPrimaryKey(String detailId);

    List<ExamDetail> selectAll();

    int updateByPrimaryKey(ExamDetail record);

    void addExamDetail(String qid);

    ExamDetail selectByQidCisServerCode(Map<String,String> param);

    void updateMeanScoreByQidCis(Map<String, String> param);

    List<Map<String, Object>> getAllExamResult(Map<String, Object> param);

    List<Map<String, Object>> getEnterpriseExamResult(Map<String, Object> param);

    int getEnterpriseExamResultNum(Map<String, Object> param);

    List<Map<String, Object>> getExamDetailListByCis(String cisCode);

    void deleteByQid(String qid);

    List<Map<String, Object>> getEnterpriseExamProcess(Map<String, Object> param);

    int getEnterpriseExamProcessNum(Map<String, Object> param);

    List<Map<String, Object>> getAllExamProcess(Map<String, Object> param);

    List<Map<String, Object>> staticByServerCompany(Map<String, Object> param);

}