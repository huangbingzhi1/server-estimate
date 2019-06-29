package com.hisense.serverestimate.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Author: Huang.bingzhi
 * @Date: 2019/4/22 9:29
 * @Version 1.0
 */
public interface ServerService {
    boolean importServerEnterprise(MultipartFile dataFile);
    Map<String,String> getCisServerCodeMd5Map();
}
