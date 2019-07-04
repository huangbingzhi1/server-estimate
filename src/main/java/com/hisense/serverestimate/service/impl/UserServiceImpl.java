package com.hisense.serverestimate.service.impl;

import com.hisense.serverestimate.controller.ApiEnterpriseController;
import com.hisense.serverestimate.entity.BaseUser;
import com.hisense.serverestimate.mapper.BaseUserMapper;
import com.hisense.serverestimate.service.UserService;
import com.hisense.serverestimate.utils.Encryption;
import com.hisense.serverestimate.utils.HiStringUtil;
import com.hisense.serverestimate.utils.PinYinUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Huang.bingzhi
 * @Date: 2019/4/22 10:33
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {
    @Value("${baseinfo.defaultPassword}")
    private String defaultPassword;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private BaseUserMapper userMapper;
    private final static Map<String, String> duplicationNameMap = new HashMap<String, String>(){{
        put("zhongqing","chongqing");
    }};

    /**
     * 添加公司用户
     *
     * @param companySet
     */
    @Override
    public void addCompanyUser(Set<String> companySet) {
        for (String companyName:companySet) {
            String pinYin= PinYinUtils.getHanziPinYin(companyName);
            if(duplicationNameMap.containsKey(pinYin)){
                pinYin=duplicationNameMap.get(pinYin);
            }
            BaseUser user=new BaseUser(HiStringUtil.getRandomUUID(),pinYin,companyName,
                    Encryption.encrypByMD5(defaultPassword),companyName,"guest");
            try{
                userMapper.insert(user);
            }catch (Exception e){
                logger.error(e.toString());
            }
        }
    }
}
