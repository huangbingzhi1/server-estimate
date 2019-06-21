package com.hisense.serverestimate.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: Huang.bingzhi
 * @Date: 2019/4/22 9:29
 * @Version 1.0
 */
public interface ServerService {
    boolean importServerEnterprise(MultipartFile dataFile);
}
