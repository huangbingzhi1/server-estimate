package com.hisense.serverestimate.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ExamDetail implements Serializable {
    private String detailId;

    private String mainQid;

    private String enterpriseCis;

    private String serverCode;

    private String serverName;

    private Date submittime;

    private Integer timetaken;

    private String scoreArray;

    private String textArray;

    private Integer totleScore;

    private BigDecimal meanScore;

    private String sourceData;

    private static final long serialVersionUID = 1L;

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId == null ? null : detailId.trim();
    }

    public String getMainQid() {
        return mainQid;
    }

    public void setMainQid(String mainQid) {
        this.mainQid = mainQid == null ? null : mainQid.trim();
    }

    public String getEnterpriseCis() {
        return enterpriseCis;
    }

    public void setEnterpriseCis(String enterpriseCis) {
        this.enterpriseCis = enterpriseCis == null ? null : enterpriseCis.trim();
    }

    public String getServerCode() {
        return serverCode;
    }

    public void setServerCode(String serverCode) {
        this.serverCode = serverCode == null ? null : serverCode.trim();
    }

    public Date getSubmittime() {
        return submittime;
    }

    public void setSubmittime(Date submittime) {
        this.submittime = submittime;
    }

    public Integer getTimetaken() {
        return timetaken;
    }

    public void setTimetaken(Integer timetaken) {
        this.timetaken = timetaken;
    }

    public String getScoreArray() {
        return scoreArray;
    }

    public void setScoreArray(String scoreArray) {
        this.scoreArray = scoreArray == null ? null : scoreArray.trim();
    }

    public String getTextArray() {
        return textArray;
    }

    public void setTextArray(String textArray) {
        this.textArray = textArray == null ? null : textArray.trim();
    }

    public Integer getTotleScore() {
        return totleScore;
    }

    public void setTotleScore(Integer totleScore) {
        this.totleScore = totleScore;
    }

    public BigDecimal getMeanScore() {
        return meanScore;
    }

    public void setMeanScore(BigDecimal meanScore) {
        this.meanScore = meanScore;
    }

    public String getSourceData() {
        return sourceData;
    }

    public void setSourceData(String sourceData) {
        this.sourceData = sourceData == null ? null : sourceData.trim();
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", detailId=").append(detailId);
        sb.append(", mainQid=").append(mainQid);
        sb.append(", enterpriseCis=").append(enterpriseCis);
        sb.append(", serverCode=").append(serverCode);
        sb.append(", submittime=").append(submittime);
        sb.append(", timetaken=").append(timetaken);
        sb.append(", scoreArray=").append(scoreArray);
        sb.append(", textArray=").append(textArray);
        sb.append(", totleScore=").append(totleScore);
        sb.append(", meanScore=").append(meanScore);
        sb.append(", sourceData=").append(sourceData);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}