package com.hisense.serverestimate.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hisense.serverestimate.entity.BaseEnterprise;
import com.hisense.serverestimate.entity.BaseServer;
import com.hisense.serverestimate.mapper.BaseEnterpriseMapper;
import com.hisense.serverestimate.utils.HiStringUtil;
import com.hisense.serverestimate.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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
        Map<String,Object> param=new HashMap<>(10);
//        numberPerPage=100;
        param.put("startIndex",(page-1)*numberPerPage);
        param.put("pCount",numberPerPage);
        String keyword=HiStringUtil.getJsonStringByKey(parseObject,"keyword");
        if(!StringUtils.isEmpty(keyword)){
            keyword="%"+keyword+"%";
            param.put("keyword",keyword);
        }
        param.put("startIndex",(page-1)*numberPerPage);
        param.put("pCount",numberPerPage);
        List<BaseEnterprise> serverList= enterpriseMapper.getEnterpriseList(param);
        if(!CollectionUtils.isEmpty(serverList)) {
            double listNum = enterpriseMapper.getEnterpriseListNum(param);
            Map<String, Object> result = new HashMap<>(10);
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
    @CacheEvict(value = "cacheEnterpriseCisServerCodeMD5",allEntries = true)
    public String deleteEnterpriseById(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try{
            enterpriseMapper.deleteServerByEnterpriseId(jsonParam);
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
    @CacheEvict(value = "cacheEnterpriseCisServerCodeMD5",allEntries = true)
    public String saveEnterprise(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try {
            JSONObject parseObject = JSON.parseObject(jsonParam);
            String enterpriseId=HiStringUtil.getJsonStringByKey(parseObject,"enterpriseId");
            String cis=HiStringUtil.getJsonStringByKey(parseObject,"cis");
            String enterpriseName=HiStringUtil.getJsonStringByKey(parseObject,"enterpriseName");
            String office=HiStringUtil.getJsonStringByKey(parseObject,"office");
            String companyId=HiStringUtil.getJsonStringByKey(parseObject,"companyId");
            String companyName=HiStringUtil.getJsonStringByKey(parseObject,"companyName");
            String serverCodeArray= HiStringUtil.getJsonStringByKey(parseObject, "serverCodeArray");
            BaseEnterprise entity=new BaseEnterprise(enterpriseId, cis, enterpriseName, office, companyId, companyName);
            if(StringUtils.isEmpty(enterpriseId)){
                //新增
                entity.setEnterpriseId(HiStringUtil.getRandomUUID());
                enterpriseMapper.insert(entity);
            }else{
                //修改
                enterpriseMapper.updateByPrimaryKey(entity);
            }
            //修改关联的商家
            if(!StringUtils.isEmpty(enterpriseId)) {
                //1.删除关联的商家
                enterpriseMapper.deleteServerByEnterpriseId(enterpriseId);
            }
            //2.添加关联的商家
            if(!StringUtils.isEmpty(serverCodeArray)){
                enterpriseMapper.addServerRelByEnterpriseCis(cis,serverCodeArray.split(","));
            }
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FAILED;
    }
    @RequestMapping(value = "getEnterpriseById", method = RequestMethod.GET)
    @ResponseBody
    public String getEnterpriseById(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try{
            BaseEnterprise enterprise= enterpriseMapper.selectByPrimaryKey(jsonParam);
            if(null!=enterprise){
                return JSON.toJSONString(enterprise);
            }

        }catch (Exception e){

        }
        return "";
    }

    @RequestMapping(value = "getEnterpriseByCis", method = RequestMethod.GET)
    @ResponseBody
    public String getEnterpriseByCis(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try{
            BaseEnterprise en=enterpriseMapper.getEnterpriseByCis(jsonParam);
            if(null!=en){
                return JSON.toJSONString(en);
            }

        }catch (Exception e){

        }
        return "";
    }

}
