package com.hisense.serverestimate.entity;

import java.io.Serializable;

public class BaseServer implements Serializable {
    private String serverId;

    private String serverCode;

    private String serverName;

    private String companyName;

    private String serverType;

    private String manager;

    private String province;

    private String city;

    private String district;

    private static final long serialVersionUID = 1L;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId == null ? null : serverId.trim();
    }

    public String getServerCode() {
        return serverCode;
    }

    public void setServerCode(String serverCode) {
        this.serverCode = serverCode == null ? null : serverCode.trim();
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName == null ? null : serverName.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType == null ? null : serverType.trim();
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager == null ? null : manager.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district == null ? null : district.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", serverId=").append(serverId);
        sb.append(", serverCode=").append(serverCode);
        sb.append(", serverName=").append(serverName);
        sb.append(", companyName=").append(companyName);
        sb.append(", serverType=").append(serverType);
        sb.append(", manager=").append(manager);
        sb.append(", province=").append(province);
        sb.append(", city=").append(city);
        sb.append(", district=").append(district);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }

    public BaseServer() {
    }

    public BaseServer(String serverId, String serverCode, String serverName, String companyName, String serverType, String manager, String province, String city, String district) {
        this.serverId = serverId;
        this.serverCode = serverCode;
        this.serverName = serverName;
        this.companyName = companyName;
        this.serverType = serverType;
        this.manager = manager;
        this.province = province;
        this.city = city;
        this.district = district;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseServer) {
            BaseServer temp = (BaseServer) obj;
            return this.serverCode.equals(temp.serverCode) &&
                    this.serverName.equals(temp.serverName) &&
                    this.companyName.equals(temp.companyName) &&
                    this.serverType.equals(temp.serverType) &&
                    this.manager.equals(temp.manager) &&
                    this.province.equals(temp.province) &&
                    this.city.equals(temp.city) &&
                    this.district.equals(temp.district);
        }
        return false;
    }
}