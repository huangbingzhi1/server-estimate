package com.hisense.serverestimate.utils;

import com.hisense.serverestimate.entity.BaseUser;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author: Huang.bingzhi
 * @Date: 2019/4/27 19:21
 * @Version 1.0
 */
public class SessionUtil {
    public static HttpSession getSession(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //已经拿到session,就可以拿到session中保存的用户信息了。
        HttpSession session=request.getSession();
        return session;
    }
    /**
     * 根据session的key获取值。
     * @param key
     * @return
     */
    public static Object getSessionObject(String key){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //已经拿到session,就可以拿到session中保存的用户信息了。
        HttpSession session=request.getSession();
        if(null!=session){
            return session.getAttribute(key);
        }
        return null;
    }
    public static BaseUser getLoginUser(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //已经拿到session,就可以拿到session中保存的用户信息了。
        HttpSession session=request.getSession();
        if(null!=session){
            BaseUser loginUser = (BaseUser) session.getAttribute("loginUser");
            if(null!=loginUser){
                return loginUser;
            }
        }
        return null;
    }
    public static String getLoginUserId(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //已经拿到session,就可以拿到session中保存的用户信息了。
        HttpSession session=request.getSession();
        if(null!=session){
            BaseUser loginUser = (BaseUser) session.getAttribute("loginUser");
            if(null!=loginUser){
                return loginUser.getUserId();
            }
        }
        return null;
    }
}
