package com.hisense.serverestimate.entity;

import java.io.Serializable;
import java.util.Date;

public class XsAccount implements Serializable {
    private String aid;

    private String cisCode;

    private String mdmCode;

    private String account;

    private String mobile;

    private String email;

    private String username;

    private String fullName;

    private String password;

    private Date createDate;

    private static final long serialVersionUID = 1L;

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid == null ? null : aid.trim();
    }

    public String getCisCode() {
        return cisCode;
    }

    public void setCisCode(String cisCode) {
        this.cisCode = cisCode == null ? null : cisCode.trim();
    }

    public String getMdmCode() {
        return mdmCode;
    }

    public void setMdmCode(String mdmCode) {
        this.mdmCode = mdmCode == null ? null : mdmCode.trim();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName == null ? null : fullName.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", aid=").append(aid);
        sb.append(", cisCode=").append(cisCode);
        sb.append(", mdmCode=").append(mdmCode);
        sb.append(", account=").append(account);
        sb.append(", mobile=").append(mobile);
        sb.append(", email=").append(email);
        sb.append(", username=").append(username);
        sb.append(", fullName=").append(fullName);
        sb.append(", createDate=").append(createDate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}