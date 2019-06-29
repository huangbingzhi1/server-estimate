package com.hisense.serverestimate.mapper;

import com.hisense.serverestimate.entity.ExamTitle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ExamTitleMapper {
    int deleteByPrimaryKey(String titleId);

    int insert(ExamTitle record);

    ExamTitle selectByPrimaryKey(String titleId);

    List<ExamTitle> selectAll();

    int updateByPrimaryKey(ExamTitle record);

    void deleteByQid(String qid);

    List<ExamTitle> selectByQid(String qid);
}