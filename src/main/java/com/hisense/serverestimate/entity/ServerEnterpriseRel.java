package com.hisense.serverestimate.entity;

import java.io.Serializable;

public class ServerEnterpriseRel implements Serializable {
    private String id;

    private String serverCode;

    private String enterpriseCis;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getServerCode() {
        return serverCode;
    }

    public void setServerCode(String serverCode) {
        this.serverCode = serverCode == null ? null : serverCode.trim();
    }

    public String getEnterpriseCis() {
        return enterpriseCis;
    }

    public void setEnterpriseCis(String enterpriseCis) {
        this.enterpriseCis = enterpriseCis == null ? null : enterpriseCis.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", serverCode=").append(serverCode);
        sb.append(", enterpriseCis=").append(enterpriseCis);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    public ServerEnterpriseRel(String id, String serverCode, String enterpriseCis) {
        this.id = id;
        this.serverCode = serverCode;
        this.enterpriseCis = enterpriseCis;
    }

    public ServerEnterpriseRel() {
    }
}