package com.hisense.serverestimate.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hisense.serverestimate.entity.BaseRole;
import com.hisense.serverestimate.entity.BaseUser;
import com.hisense.serverestimate.mapper.BaseRoleMapper;
import com.hisense.serverestimate.mapper.BaseUserMapper;
import com.hisense.serverestimate.utils.Encryption;
import com.hisense.serverestimate.utils.HiStringUtil;
import com.hisense.serverestimate.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    @Autowired
    private BaseUserMapper userMapper;
    @Autowired
    private BaseRoleMapper roleMapper;

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

    /*


    */
/**
     * 批量新增用户
     * @param jsonParam
     * @param request
     * @return
     *//*

    @RequestMapping(value = "batchAddUsers", method = RequestMethod.GET)
    @ResponseBody
    public String batchAddUsers(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        List<BaseUser> users = JSON.parseArray(jsonParam, BaseUser.class);
        for (int i = 0; i <users.size(); i++) {
            users.get(i).setPassword(Encryption.encrypByMD5(users.get(i).getUsername()));
        }
        int num=userMapper.batchAddUsers(users);
        if(num==users.size()){
            return SUCCESS;
        }
        return FAILED;
    }

    */
/**
     * 获取用户列表
     * @param jsonParam
     * @param request
     * @return
     *//*

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

    */
/**
     * 获取所有用户
     * @return
     *//*

    @RequestMapping(value = "getAllUsers", method = RequestMethod.GET)
    @ResponseBody
    public String getAllUsers() {
        List<BaseUser> userList=userMapper.getAllUsers();

        if(!CollectionUtils.isEmpty(userList)){
            return JSON.toJSONString(userList);
        }
        return "";
    }

    */
/**
     * 新增或者修改用户
     * 根据id是否为空来判断新增或者修改
     * @param jsonParam
     * @param request
     * @return
     *//*

    @RequestMapping(value = "saveUser", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String saveUser(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try {
            JSONObject parseObject = JSON.parseObject(jsonParam);
            String id = HiStringUtil.getJsonStringByKey(parseObject, "id");
            String username = HiStringUtil.getJsonStringByKey(parseObject, "username");
            String trueName = HiStringUtil.getJsonStringByKey(parseObject, "trueName");
            String email = HiStringUtil.getJsonStringByKey(parseObject, "email");
            String company = HiStringUtil.getJsonStringByKey(parseObject, "company");
            BaseUser entity=new BaseUser(id,username,trueName,email,company);
            if(StringUtils.isEmpty(id)){
                //新增
                entity.setId(HiStringUtil.getRandomUUID());
                entity.setPassword(Encryption.encrypByMD5(entity.getUsername()));
                userMapper.insertUser(entity);
            }else{
                //修改
                userMapper.updateUser(entity);
            }
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FAILED;
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
                param.put("username", loginUser.getUsername());
                param.put("password", Encryption.encrypByMD5(oldPassword));
                BaseUser user = userMapper.getUserByNamePassword(param);
                if (null != user) {
                    userMapper.resetPassword(loginUser.getId(), Encryption.encrypByMD5(newPassword));
                }
            }
            return SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FAILED;
    }


    @RequestMapping(value = "getUserByUsername", method = RequestMethod.GET)
    @ResponseBody
    public String getUserByUsername(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try{
            BaseUser u=userMapper.getUserByUsername(jsonParam);
            if(null!=u){
                return JSON.toJSONString(u);
            }

        }catch (Exception e){

        }
        return "";
    }
    @RequestMapping(value = "getUserById", method = RequestMethod.GET)
    @ResponseBody
    public String getUserById(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try{
            BaseUser u=userMapper.getUserById(jsonParam);
            if(null!=u){
                return JSON.toJSONString(u);
            }

        }catch (Exception e){

        }
        return "";
    }

    */
/**
     * 根据id删除用户
     * @param jsonParam
     * @param request
     * @return
     *//*

    @RequestMapping(value = "deleteUserById", method = RequestMethod.GET)
    @ResponseBody
    public String deleteUserById(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try{
            userMapper.deleteUserById(jsonParam);
            return SUCCESS;
        }catch (Exception e){

        }
        return FAILED;
    }
    */
/**
     * 根据重置密码
     * @param jsonParam
     * @param request
     * @return
     *//*

    @RequestMapping(value = "resetPassword", method = RequestMethod.GET)
    @ResponseBody
    public String resetPassword(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try{
            BaseUser userById = userMapper.getUserById(jsonParam);
            userMapper.resetPassword(jsonParam,Encryption.encrypByMD5("1"));
            return SUCCESS;
        }catch (Exception e){

        }
        return FAILED;
    }





    @RequestMapping("getUserById/{id}")
    public BaseUser getUserById(@PathVariable(name = "id") String id) {
        if (!StringUtils.isEmpty(id)) {
//            return new BaseUser();
            BaseUser userById = userMapper.getUserById("1");
            System.out.println(userById.getTrueName());
            return userMapper.getUserById(id);
        }
        return null;
    }

    @RequestMapping("mail")
    public String mail(Model model) {
        System.out.println("sdfsdfsdfsd");
        MailBean mail;
        mail = new MailBean();
        mail.setContent("内容");
        mail.setRecipient("huangbingzhi@hisense.com");
        mail.setSubject("zhuti");
//        mailUtil.sendSimpleMail(mail);
//        mailUtil.sendHTMLMail(mail);
//        mailUtil.sendAttachmentMail(mail);
        return "/index";
    }
*/




}
