package com.hisense.serverestimate.entity;

import java.io.Serializable;

public class BaseEnterprise implements Serializable {
    private String enterpriseId;

    private String cis;

    private String enterpriseName;

    private String office;

    private String companyId;

    private String companyName;

    private static final long serialVersionUID = 1L;

    public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId == null ? null : enterpriseId.trim();
    }

    public String getCis() {
        return cis;
    }

    public void setCis(String cis) {
        this.cis = cis == null ? null : cis.trim();
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName == null ? null : enterpriseName.trim();
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office == null ? null : office.trim();
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", enterpriseId=").append(enterpriseId);
        sb.append(", cis=").append(cis);
        sb.append(", enterpriseName=").append(enterpriseName);
        sb.append(", office=").append(office);
        sb.append(", companyId=").append(companyId);
        sb.append(", companyName=").append(companyName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    public BaseEnterprise(String enterpriseId, String cis, String enterpriseName, String office, String companyId, String companyName) {
        this.enterpriseId = enterpriseId;
        this.cis = cis;
        this.enterpriseName = enterpriseName;
        this.office = office;
        this.companyId = companyId;
        this.companyName = companyName;
    }

    public BaseEnterprise() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseEnterprise) {
            BaseEnterprise temp = (BaseEnterprise) obj;
//            System.out.println(this.cis.equals(temp.cis));
//            System.out.println(this.enterpriseName.equals(temp.enterpriseName));
//            System.out.println(this.office.equals(temp.office));
//            System.out.println(this.companyId.equals(temp.companyId));
//            System.out.println(this.companyName.equals(temp.companyName));

            return this.cis.equals(temp.cis) &&
                    this.enterpriseName.equals(temp.enterpriseName) &&
                    this.office.equals(temp.office) &&
                    this.companyId.equals(temp.companyId) &&
                    this.companyName.equals(temp.companyName);
        }
        return false;
    }
}