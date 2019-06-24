package com.hisense.serverestimate.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hisense.serverestimate.entity.BaseEnterprise;
import com.hisense.serverestimate.entity.BaseServer;
import com.hisense.serverestimate.mapper.BaseEnterpriseMapper;
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
 * @Date: 2019/6/20 18:32
 * @Version 1.0
 */
@RestController
@RequestMapping("enterpriseController")
public class EnterpriseController extends BaseController{
    @Autowired
    private BaseEnterpriseMapper enterpriseMapper;

    @RequestMapping(value = "getEnterpriseList", method = RequestMethod.GET)
    @ResponseBody
    public String getEnterpriseList(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        JSONObject parseObject = JSON.parseObject(jsonParam);
        int page= HiStringUtil.getJsonIntByKey(parseObject,"page");
        int numberPerPage=5;
        Map<String,Object> param=new HashMap<>();
        param.put("startIndex",(page-1)*numberPerPage);
        param.put("pCount",numberPerPage);
        String keyword=HiStringUtil.getJsonStringByKey(parseObject,"keyword");
        if(!StringUtils.isEmpty(keyword)){
            keyword="%"+keyword+"%";
            param.put("keyword",keyword);
        }
        List<BaseEnterprise> serverList= enterpriseMapper.getEnterpriseList(param);
        if(!CollectionUtils.isEmpty(serverList)) {
            double listNum = enterpriseMapper.getEnterpriseListNum(param);
            Map<String, Object> result = new HashMap<>();
            result.put("totalPage", Math.ceil(listNum / numberPerPage));
            result.put("list", serverList);
            result.put("currentPage", page);
            return JSON.toJSONString(result);
        }
        return "";
    }

    /**
     * 根据id删除商家
     * @param jsonParam
     * @param request
     * @return
     */
    @RequestMapping(value = "deleteEnterpriseById", method = RequestMethod.GET)
    @ResponseBody
    public String deleteEnterpriseById(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try{
            enterpriseMapper.deleteByPrimaryKey(jsonParam);
            return SUCCESS;
        }catch (Exception e){

        }
        return FAILED;
    }

    /**
     * 新增或者修改商家
     * 根据id是否为空来判断新增或者修改
     * @param jsonParam
     * @param request
     * @return
     */
    @RequestMapping(value = "saveEnterprise", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String saveEnterprise(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try {
            JSONObject parseObject = JSON.parseObject(jsonParam);
            String enterpriseId=HiStringUtil.getJsonStringByKey(parseObject,"enterpriseId");
            String cis=HiStringUtil.getJsonStringByKey(parseObject,"cis");
            String enterpriseName=HiStringUtil.getJsonStringByKey(parseObject,"enterpriseName");
            String office=HiStringUtil.getJsonStringByKey(parseObject,"office");
            String companyId=HiStringUtil.getJsonStringByKey(parseObject,"companyId");
            String companyName=HiStringUtil.getJsonStringByKey(parseObject,"companyName");

            BaseEnterprise entity=new BaseEnterprise(enterpriseId, cis, enterpriseName, office, companyId, companyName);
            if(StringUtils.isEmpty(enterpriseId)){
                //新增
                entity.setEnterpriseId(HiStringUtil.getRandomUUID());
                enterpriseMapper.insert(entity);
            }else{
                //修改
                enterpriseMapper.updateByPrimaryKey(entity);
            }
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FAILED;
    }
    /*@RequestMapping(value = "getEnterpriseByCis", method = RequestMethod.GET)
    @ResponseBody
    public String getEnterpriseByCis(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try{
            BaseServer u= enterpriseMapper.getEnterpriseByCis(jsonParam);
            if(null!=u){
                return JSON.toJSONString(u);
            }

        }catch (Exception e){

        }
        return "";
    }*/


}
