package com.hisense.serverestimate.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hisense.serverestimate.entity.BaseRole;
import com.hisense.serverestimate.entity.BaseUser;
import com.hisense.serverestimate.entity.XsAccount;
import com.hisense.serverestimate.mapper.BaseRoleMapper;
import com.hisense.serverestimate.mapper.BaseUserMapper;
import com.hisense.serverestimate.mapper.XsAccountMapper;
import com.hisense.serverestimate.utils.Encryption;
import com.hisense.serverestimate.utils.HiStringUtil;
import com.hisense.serverestimate.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Huang.bingzhi
 * @Date: 2019/4/22 01:09
 * @Version 1.0
 */
//@Controller
@RestController
@RequestMapping("userController")
public class UserController extends BaseController {
    @Value("${baseinfo.defaultPassword}")
    public String defaultPassword;
    @Value("${xinshang.callApiTokenUrl}")
    public String callApiTokenUrl;
    @Value("${xinshang.checkSsoLoginTokenUrl}")
    public String checkSsoLoginTokenUrl;

    @Autowired
    private BaseUserMapper userMapper;
    @Autowired
    private BaseRoleMapper roleMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private XsAccountMapper xsAccountMapper;
    @Autowired
    private ExamController examController;

    @RequestMapping(value = "ssoTest", method = RequestMethod.GET)
    public void ssoTest(HttpServletRequest request,HttpServletResponse response) {
        try {
            response.sendRedirect("../files/examPage.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 单点登录
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "ssoLogin", method = RequestMethod.GET)
    @ResponseBody
    public void ssoLogin(HttpServletRequest request,HttpServletResponse response) {
        String apiTokenStr = restTemplate.getForObject(callApiTokenUrl, String.class);
        JSONObject apiTokenObj = JSON.parseObject(apiTokenStr);
        String resCode = HiStringUtil.getJsonStringByKey(apiTokenObj, "resCode");
        String tokenId = HiStringUtil.getJsonStringByKey(apiTokenObj, "tokenId");
        HttpSession session = SessionUtil.getSession();
        session.removeAttribute("loginUser");
        if(!StringUtils.isEmpty(resCode)&&!StringUtils.isEmpty(tokenId)&&SUCCESS.equalsIgnoreCase(resCode)){
            String ssoLoginToken=getCookieValue(request,"ssoLoginToken");
            if(!StringUtils.isEmpty(ssoLoginToken)){
                StringBuilder sb=new StringBuilder(checkSsoLoginTokenUrl);
                sb.append("?tokenId=")
                        .append(tokenId)
                        .append("&ssoLoginToken=")
                        .append(ssoLoginToken);
                String accountStr = restTemplate.getForObject(sb.toString(), String.class);
                final JSONObject responseObj = JSON.parseObject(accountStr);
                resCode=HiStringUtil.getJsonStringByKey(responseObj,"resCode");
                if(resCode.equalsIgnoreCase(SUCCESS)){
                    String uid=HiStringUtil.getJsonStringByKey(responseObj,"uid");
                    XsAccount xsAccount= xsAccountMapper.selectByAccount(uid);
                    if(null!=xsAccount){
                        BaseUser user=new BaseUser("temp","temp","temp","temp","temp","temp");
                        session.setMaxInactiveInterval(-1);
                        session.setAttribute("loginUser", user);
                        session.setAttribute("account",xsAccount);
                        toExamPage(request, response);

                    }
                }
            }
        }
    }

    private void toExamPage(HttpServletRequest request,HttpServletResponse response) {
        try {
            String examDetailListByLoginAccount = examController.getExamDetailListByLoginAccount(null,request);
            Cookie cookie=new Cookie("examDetails", URLEncoder.encode(URLEncoder.encode(examDetailListByLoginAccount, "utf-8"), "utf-8"));
            cookie.setMaxAge(60);
            cookie.setPath("/");
            response.addCookie(cookie);
            response.sendRedirect("../files/examPage.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户登录
     *
     * @param jsonParam
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    @ResponseBody
    public String login(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request,HttpServletResponse response) {
        JSONObject parseObject = JSON.parseObject(jsonParam);
        HttpSession session = SessionUtil.getSession();
        String username = HiStringUtil.getJsonStringByKey(parseObject, "username");
        String password = HiStringUtil.getJsonStringByKey(parseObject, "password");
        Map<String, String> param = new HashMap<>();
        param.put("username", username);
        param.put("password", Encryption.encrypByMD5(password));
        BaseUser user = userMapper.getUserByNamePassword(param);
        if (null != user) {
            session.setMaxInactiveInterval(-1);
            session.setAttribute("loginUser", user);
            return SUCCESS;
        }
        return FAILED;
    }
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    @ResponseBody
    public String logout(HttpServletRequest request,HttpServletResponse response) {
        HttpSession session = SessionUtil.getSession();
        try {
            session.removeAttribute("loginUser");
        }catch (Exception e){
            e.printStackTrace();
            return FAILED;
        }
        return SUCCESS;
    }
    @RequestMapping(value = "getUserRole", method = RequestMethod.GET)
    @ResponseBody
    public String getUserRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Object obj = session.getAttribute("loginUser");
        if(null!=obj){
            BaseUser loginUser=(BaseUser)obj;
            List<BaseRole> roleByUserId = roleMapper.getRoleByUserId(loginUser.getUserId());
            return JSON.toJSONString(roleByUserId);
        }
        return "";
    }
    @RequestMapping(value = "getUser", method = RequestMethod.GET)
    @ResponseBody
    public Object getUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        Object obj = session.getAttribute("loginUser");
        if(null!=obj){
            return obj;
        }
        return "";
    }
    /**
     * 获取用户列表
     * @param jsonParam
     * @param request
     * @return
     */

    @RequestMapping(value = "getUserList", method = RequestMethod.GET)
    @ResponseBody
    public String getUserList(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        JSONObject parseObject = JSON.parseObject(jsonParam);
        HttpSession session = request.getSession();
        int page= HiStringUtil.getJsonIntByKey(parseObject,"page");
        Map<String,Object> param=new HashMap<>();
        param.put("startIndex",(page-1)*numberPerPage);
        param.put("pCount",numberPerPage);
        List<BaseUser> userList=userMapper.getUserList(param);

        if(!CollectionUtils.isEmpty(userList)){
            double listNum=userMapper.getUserListNum();
            Map<String,Object> result =new HashMap<>();
            result.put("totalPage",Math.ceil(listNum/numberPerPage));
            result.put("list",userList);
            result.put("currentPage",page);
            return JSON.toJSONString(result);
        }
        return "";
    }
    @RequestMapping(value = "changePassword", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String changePassword(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try {
            JSONObject parseObject = JSON.parseObject(jsonParam);
            String oldPassword = HiStringUtil.getJsonStringByKey(parseObject, "oldPassword");
            String newPassword = HiStringUtil.getJsonStringByKey(parseObject, "newPassword");
            String newPassword2 = HiStringUtil.getJsonStringByKey(parseObject, "newPassword2");
            BaseUser loginUser = SessionUtil.getLoginUser();
            if(null!=loginUser&&!StringUtils.isEmpty(oldPassword)&&
                    !StringUtils.isEmpty(newPassword)&&!StringUtils.isEmpty(newPassword2)){
                Map<String, String> param = new HashMap<>();
                if(loginUser.getPassword().equals(Encryption.encrypByMD5(oldPassword))){
                    userMapper.resetPassword(loginUser.getUserId(), Encryption.encrypByMD5(newPassword));
                }else{
                    return FAILED;
                }
            }else{
                return FAILED;
            }
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FAILED;
    }
    /**
     * 根据id删除用户
     * @param jsonParam
     * @param request
     * @return
     */

    @RequestMapping(value = "deleteUserById", method = RequestMethod.GET)
    @ResponseBody
    public String deleteUserById(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try{
            userMapper.deleteByPrimaryKey(jsonParam);
            return SUCCESS;
        }catch (Exception e){

        }
        return FAILED;
    }
    /**
     * 根据重置密码
     * @param jsonParam
     * @param request
     * @return
     */

    @RequestMapping(value = "resetPassword", method = RequestMethod.GET)
    @ResponseBody
    public String resetPassword(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try{
            BaseUser userById = userMapper.selectByPrimaryKey(jsonParam);
            userById.setPassword(Encryption.encrypByMD5(defaultPassword));
            userMapper.updateByPrimaryKey(userById);
            return SUCCESS;
        }catch (Exception e){

        }
        return FAILED;
    }
    @RequestMapping(value = "getUserById", method = RequestMethod.GET)
    @ResponseBody
    public String getUserById(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try{
            BaseUser user=userMapper.selectByPrimaryKey(jsonParam);
            if(null!=user){
                return JSON.toJSONString(user);
            }

        }catch (Exception e){

        }
        return "";
    }
    @RequestMapping(value = "getUserByUsername", method = RequestMethod.GET)
    @ResponseBody
    public String getUserByUsername(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try{
            BaseUser user=userMapper.getUserByUsername(jsonParam);
            if(null!=user){
                return JSON.toJSONString(user);
            }

        }catch (Exception e){

        }
        return "";
    }
    /**
     * 新增或者修改用户
     * 根据id是否为空来判断新增或者修改
     * @param jsonParam
     * @param request
     * @return
     */

    @RequestMapping(value = "saveUser", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String saveUser(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try {
            JSONObject parseObject = JSON.parseObject(jsonParam);
            String id = HiStringUtil.getJsonStringByKey(parseObject, "id");
            String username = HiStringUtil.getJsonStringByKey(parseObject, "username");
            String truename = HiStringUtil.getJsonStringByKey(parseObject, "truename");
            String company = HiStringUtil.getJsonStringByKey(parseObject, "company");
            String roleId = HiStringUtil.getJsonStringByKey(parseObject, "roleId");
            BaseUser entity=new BaseUser(id,username,truename,null,company,roleId);
            if(StringUtils.isEmpty(id)){
                //新增
                entity.setUserId(HiStringUtil.getRandomUUID());
                entity.setPassword(Encryption.encrypByMD5(defaultPassword));
                userMapper.insert(entity);
            }else{
                //修改
                userMapper.updateByPrimaryKey(entity);
            }
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FAILED;
    }
    private String getCookieValue(HttpServletRequest request,String key){
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            if(cookies[i].getName().equals(key)) {
                return cookies[i].getValue();
            }
        }
        return "";
    }
}
