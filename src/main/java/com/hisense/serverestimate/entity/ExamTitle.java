package com.hisense.serverestimate.entity;

import ch.qos.logback.core.pattern.FormatInfo;
import org.springframework.util.StringUtils;

import java.io.Serializable;

public class ExamTitle implements Serializable {
    private String titleId;

    private String qid;

    private String titleNo;

    private String titleName;

    private String titleType;

    private static final long serialVersionUID = 1L;

    public String getTitleId() {
        return titleId;
    }

    public void setTitleId(String titleId) {
        this.titleId = titleId == null ? null : titleId.trim();
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid == null ? null : qid.trim();
    }

    public String getTitleNo() {
        return titleNo;
    }

    public void setTitleNo(String titleNo) {
        this.titleNo = titleNo == null ? null : titleNo.trim();
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName == null ? null : titleName.trim();
    }

    public String getTitleType() {
        return titleType;
    }

    public void setTitleType(String titleType) {
        this.titleType=titleType;
    }

    @Override
    public String toString() {
        return "ExamTitle{" +
                "titleId='" + titleId + '\'' +
                ", qid='" + qid + '\'' +
                ", titleNo='" + titleNo + '\'' +
                ", titleName='" + titleName + '\'' +
                ", titleType='" + titleType + '\'' +
                '}';
    }
}