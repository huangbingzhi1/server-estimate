package com.hisense.serverestimate.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hisense.serverestimate.entity.BaseCompany;
import com.hisense.serverestimate.entity.BaseServer;
import com.hisense.serverestimate.mapper.BaseCompanyMapper;
import com.hisense.serverestimate.mapper.BaseEnterpriseMapper;
import com.hisense.serverestimate.mapper.BaseServerMapper;
import com.hisense.serverestimate.service.ServerService;
import com.hisense.serverestimate.utils.HiStringUtil;
import com.hisense.serverestimate.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Huang.bingzhi
 * @Date: 2019/6/25 18:32
 * @Version 1.0
 */
@RestController
@RequestMapping("companyController")
public class CompanyController extends BaseController{
    @Autowired
    private BaseCompanyMapper companyMapper;


    @RequestMapping(value = "getAllCompanyList", method = RequestMethod.GET)
    @ResponseBody
    public String getAllCompanyList() {
        List<BaseCompany> companyList=companyMapper.selectAll();
        if(!CollectionUtils.isEmpty(companyList)) {
            return JSON.toJSONString(companyList);
        }
        return "";
    }
}
