package com.hisense.serverestimate.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hisense.serverestimate.entity.BaseRole;
import com.hisense.serverestimate.entity.BaseServer;
import com.hisense.serverestimate.entity.BaseUser;
import com.hisense.serverestimate.entity.XsAccount;
import com.hisense.serverestimate.mapper.BaseEnterpriseMapper;
import com.hisense.serverestimate.mapper.BaseRoleMapper;
import com.hisense.serverestimate.mapper.BaseServerMapper;
import com.hisense.serverestimate.mapper.BaseUserMapper;
import com.hisense.serverestimate.service.ServerService;
import com.hisense.serverestimate.utils.Encryption;
import com.hisense.serverestimate.utils.HiStringUtil;
import com.hisense.serverestimate.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Huang.bingzhi
 * @Date: 2019/6/20 18:32
 * @Version 1.0
 */
@RestController
@RequestMapping("serverController")
public class ServerController extends BaseController{
    @Autowired
    private BaseServerMapper serverMapper;

    @Autowired
    private BaseEnterpriseMapper enterpriseMapper;

    @Autowired
    private ServerService serverService;

    @RequestMapping(value = "getServerList", method = RequestMethod.GET)
    @ResponseBody
    public String getServerList(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        JSONObject parseObject = JSON.parseObject(jsonParam);
        int page= HiStringUtil.getJsonIntByKey(parseObject,"page");
        Map<String,Object> param=new HashMap<>();
        param.put("startIndex",(page-1)*numberPerPage);
        param.put("pCount",numberPerPage);
        String keyword=HiStringUtil.getJsonStringByKey(parseObject,"keyword");
        if(!StringUtils.isEmpty(keyword)){
            keyword="%"+keyword+"%";
            param.put("keyword",keyword);
        }
        List<BaseServer> serverList=serverMapper.getServerList(param);
        if(!CollectionUtils.isEmpty(serverList)) {
            double listNum = serverMapper.getServerListNum(param);
            Map<String, Object> result = new HashMap<>();
            result.put("totalPage", Math.ceil(listNum / numberPerPage));
            result.put("list", serverList);
            result.put("currentPage", page);
            return JSON.toJSONString(result);
        }
        return "";
    }
    @RequestMapping(value = "importServerEnterprise", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public String importServerEnterprise(MultipartFile dataFile){
        boolean result=false;
        if(null!=dataFile&&!StringUtils.isEmpty(dataFile.getOriginalFilename())) {
            result = serverService.importServerEnterprise(dataFile);
        }
        if(result) {
            return SUCCESS;
        }else{
            return FAILED;
        }
    }

    /**
     * 根据id删除服务商
     * @param jsonParam
     * @param request
     * @return
     */
    @RequestMapping(value = "deleteServerById", method = RequestMethod.GET)
    @ResponseBody
    @CacheEvict(value = "cacheEnterpriseCisServerCodeMD5",allEntries = true)
    public String deleteServerById(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try{
            serverMapper.deleteEnterpriseByServerId(jsonParam);
            serverMapper.deleteByPrimaryKey(jsonParam);
            return SUCCESS;
        }catch (Exception e){

        }
        return FAILED;
    }

    /**
     * 新增或者修改服务商
     * 根据id是否为空来判断新增或者修改
     * @param jsonParam
     * @param request
     * @return
     */
    @RequestMapping(value = "saveServer", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    @CacheEvict(value = "cacheEnterpriseCisServerCodeMD5",allEntries = true)
    public String saveServer(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try {
            JSONObject parseObject = JSON.parseObject(jsonParam);
            String serverId = HiStringUtil.getJsonStringByKey(parseObject, "serverId");
            String serverCode = HiStringUtil.getJsonStringByKey(parseObject, "serverCode");
            String serverName = HiStringUtil.getJsonStringByKey(parseObject, "serverName");
            String companyName = HiStringUtil.getJsonStringByKey(parseObject, "companyName");
            String serverType = HiStringUtil.getJsonStringByKey(parseObject, "serverType");
            String manager = HiStringUtil.getJsonStringByKey(parseObject, "manager");
            String province = HiStringUtil.getJsonStringByKey(parseObject, "province");
            String city = HiStringUtil.getJsonStringByKey(parseObject, "city");
            String district = HiStringUtil.getJsonStringByKey(parseObject, "district");
            String cisArray= HiStringUtil.getJsonStringByKey(parseObject, "cisArray");
            BaseServer entity=new BaseServer(serverId,serverCode,serverName,companyName,serverType,manager,province,city,district);
            if(StringUtils.isEmpty(serverId)){
                //新增
                entity.setServerId(HiStringUtil.getRandomUUID());
                serverMapper.insert(entity);
            }else{
                //修改
                serverMapper.updateByPrimaryKey(entity);
            }
            //修改关联的商家
            //1.删除关联的商家
            enterpriseMapper.deleteEnterpriseByServerCode(serverCode);
            //2.添加关联的商家
            if(!StringUtils.isEmpty(cisArray)){
                enterpriseMapper.addEnterpriseByServerCode(serverCode,cisArray.split(","));
            }
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FAILED;
    }
    @RequestMapping(value = "getServerByServerCode", method = RequestMethod.GET)
    @ResponseBody
    public String getServerByServerCode(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try{
            BaseServer u=serverMapper.getServerByServerCode(jsonParam);
            if(null!=u){
                return JSON.toJSONString(u);
            }

        }catch (Exception e){

        }
        return "";
    }

    @RequestMapping(value = "getServerById", method = RequestMethod.GET)
    @ResponseBody
    public String getServerById(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try{
            BaseServer u=serverMapper.selectByPrimaryKey(jsonParam);
            if(null!=u){
                return JSON.toJSONString(u);
            }

        }catch (Exception e){

        }
        return "";
    }
}
