package com.hisense.serverestimate.entity;

import java.io.Serializable;
import java.util.Date;

public class ExamMain implements Serializable {
    private String mainId;

    private String examQid;

    private String examName;

    private String examDomain;

    private Date begindate;

    private int answercount;

    private String status;

    private String scoreTypeIndexs;

    private String textTypeIndexs;

    private static final Long serialVersionUID = 1L;

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId == null ? null : mainId.trim();
    }

    public String getExamQid() {
        return examQid;
    }

    public void setExamQid(String examQid) {
        this.examQid = examQid == null ? null : examQid.trim();
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName == null ? null : examName.trim();
    }

    public String getExamDomain() {
        return examDomain;
    }

    public void setExamDomain(String examDomain) {
        this.examDomain = examDomain == null ? null : examDomain.trim();
    }

    public Date getBegindate() {
        return begindate;
    }

    public void setBegindate(Date begindate) {
        this.begindate = begindate;
    }

    public int getAnswercount() {
        return answercount;
    }

    public void setAnswercount(int answercount) {
        this.answercount = answercount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getScoreTypeIndexs() {
        return scoreTypeIndexs;
    }

    public void setScoreTypeIndexs(String scoreTypeIndexs) {
        this.scoreTypeIndexs = scoreTypeIndexs == null ? null : scoreTypeIndexs.trim();
    }

    public String getTextTypeIndexs() {
        return textTypeIndexs;
    }

    public void setTextTypeIndexs(String textTypeIndexs) {
        this.textTypeIndexs = textTypeIndexs == null ? null : textTypeIndexs.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", mainId=").append(mainId);
        sb.append(", examQid=").append(examQid);
        sb.append(", examName=").append(examName);
        sb.append(", examDomain=").append(examDomain);
        sb.append(", begindate=").append(begindate);
        sb.append(", answercount=").append(answercount);
        sb.append(", status=").append(status);
        sb.append(", scoreTypeIndexs=").append(scoreTypeIndexs);
        sb.append(", textTypeIndexs=").append(textTypeIndexs);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}