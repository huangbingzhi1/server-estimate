package com.hisense.serverestimate.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author huangbingzhi
 * @version 1.0
 * @description
 * @date 2020/7/31 10:38
 */
public class ExamInfo implements Serializable {
    /**
     * 问卷ID
     */
    private String id;
    /**
     * 问卷的详情，包括问卷标题以及各个题目
     */
    private List<ExamTitle> desc;
    /**
     * 对每个商家的评价详情
     */
    private List<ExamDetail> details;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id=id;
    }

    public List<ExamTitle> getDesc() {
        return desc;
    }

    public void setDesc(List<ExamTitle> desc) {
        this.desc=desc;
    }

    public List<ExamDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ExamDetail> details) {
        this.details=details;
    }
}
