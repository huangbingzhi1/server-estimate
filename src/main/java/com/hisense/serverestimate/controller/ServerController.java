package com.hisense.serverestimate.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hisense.serverestimate.entity.BaseRole;
import com.hisense.serverestimate.entity.BaseServer;
import com.hisense.serverestimate.entity.BaseUser;
import com.hisense.serverestimate.mapper.BaseRoleMapper;
import com.hisense.serverestimate.mapper.BaseServerMapper;
import com.hisense.serverestimate.mapper.BaseUserMapper;
import com.hisense.serverestimate.service.ServerService;
import com.hisense.serverestimate.utils.Encryption;
import com.hisense.serverestimate.utils.HiStringUtil;
import com.hisense.serverestimate.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private ServerService serverService;

    @RequestMapping(value = "getServerList", method = RequestMethod.GET)
    @ResponseBody
    public String getServerList(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        JSONObject parseObject = JSON.parseObject(jsonParam);
        int page= HiStringUtil.getJsonIntByKey(parseObject,"page");
        Map<String,Object> param=new HashMap<>();
        param.put("startIndex",(page-1)*numberPerPage);
        param.put("pCount",numberPerPage);
        param.put("keyword",HiStringUtil.getJsonStringByKey(parseObject,"keyword"));
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
        final String id = HiStringUtil.getRandomUUID();
        String fileUrl = null;
        String fileName = null;
        boolean result=false;
        final String loginUserId = SessionUtil.getLoginUserId();
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
    public String deleteServerById(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try{
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
            BaseServer entity=new BaseServer(serverId,serverCode,serverName,companyName,serverType,manager,province,city,district);
            if(StringUtils.isEmpty(serverId)){
                //新增
                entity.setServerId(HiStringUtil.getRandomUUID());
                serverMapper.insert(entity);
            }else{
                //修改
                serverMapper.updateByPrimaryKey(entity);
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


}
