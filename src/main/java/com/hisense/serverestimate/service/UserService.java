package com.hisense.serverestimate.service;

import java.util.Set;

/**
 * @Author: Huang.bingzhi
 * @Date: 2019/4/22 9:29
 * @Version 1.0
 */
public interface UserService {
    /**
     * 添加公司用户
     * @param companySet
     */
    void addCompanyUser(Set<String> companySet);
}
