package com.hisense.serverestimate.service;

import com.hisense.serverestimate.entity.ExamMain;
import com.hisense.serverestimate.entity.ExamTitle;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Author Huang.bingzhi
 * @Date 2019/6/26 22:16
 * @Version 1.0
 */
public interface ExamService {

    void addExamDetail(ExamMain main);

    void downloadExamResultData(HttpServletResponse response, ExamMain main, List<Map<String, Object>> examResult, List<ExamTitle> examTitle);
}
