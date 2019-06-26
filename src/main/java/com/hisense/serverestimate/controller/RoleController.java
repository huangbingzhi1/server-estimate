package com.hisense.serverestimate.controller;

import com.alibaba.fastjson.JSON;
import com.hisense.serverestimate.entity.BaseCompany;
import com.hisense.serverestimate.entity.BaseRole;
import com.hisense.serverestimate.mapper.BaseCompanyMapper;
import com.hisense.serverestimate.mapper.BaseRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Huang.bingzhi
 * @Date: 2019/6/25 18:32
 * @Version 1.0
 */
@RestController
@RequestMapping("roleController")
public class RoleController extends BaseController{
    @Autowired
    private BaseRoleMapper roleMapper;


    @RequestMapping(value = "getAllRoleList", method = RequestMethod.GET)
    @ResponseBody
    public String getAllRoleList() {
        List<BaseRole> roleList=roleMapper.selectAll();
        if(!CollectionUtils.isEmpty(roleList)) {
            return JSON.toJSONString(roleList);
        }
        return "";
    }
}
