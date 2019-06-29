package com.hisense.serverestimate.entity;

import ch.qos.logback.core.pattern.FormatInfo;
import org.springframework.util.StringUtils;

import java.io.Serializable;

public class ExamTitle implements Serializable {
    private String titleId;

    private String qid;

    private String titleNo;

    private String titleName;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", titleId=").append(titleId);
        sb.append(", qid=").append(qid);
        sb.append(", titleNo=").append(titleNo);
        sb.append(", titleName=").append(titleName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}