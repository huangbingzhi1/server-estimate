package com.hisense.serverestimate.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class EstimateResult implements Serializable {
    private String resultId;

    private String mainId;

    private String enterpriseCis;

    private String serverCode;

    private Date createDate;

    private String scoreArray;

    private String textArray;

    private BigDecimal totleScore;

    private BigDecimal meanScore;

    private String sourceData;

    private static final long serialVersionUID = 1L;

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId == null ? null : resultId.trim();
    }

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId == null ? null : mainId.trim();
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public BigDecimal getTotleScore() {
        return totleScore;
    }

    public void setTotleScore(BigDecimal totleScore) {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", resultId=").append(resultId);
        sb.append(", mainId=").append(mainId);
        sb.append(", enterpriseCis=").append(enterpriseCis);
        sb.append(", serverCode=").append(serverCode);
        sb.append(", createDate=").append(createDate);
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