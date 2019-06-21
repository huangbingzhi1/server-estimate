package com.hisense.serverestimate.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: Huang.bingzhi
 * @Date: 2019/4/23 19:19
 * @Version 1.0
 */
public class HiStringUtil {
    public static String getRandomUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 根据jsonObject的key获取值的字符串形式
     * @param parseObject
     * @param key
     * @return
     */
    public static String getJsonStringByKey(JSONObject parseObject,String key){
        if(parseObject.containsKey(key)){
            String parseObjectString = parseObject.getString(key);
            if(StringUtils.isEmpty(parseObjectString)||"undefined".equals(parseObjectString)){
                return "";
            }else {
                return parseObjectString;
            }
        }
        return "";
    }
    /**
     * 根据jsonObject的key获取值的int形式
     * @param parseObject
     * @param key
     * @return
     */
    public static int getJsonIntByKey(JSONObject parseObject,String key){
        if(parseObject.containsKey(key)){
            return parseObject.getInteger(key);
        }
        return 0;
    }
    public static String uuid(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 根据yyyy-MM-dd样式字符串，转化成日期
     * @param dateString
     * @return
     */
    public static Date getDateFromYYYYMMDD(String dateString){
        //注意：SimpleDateFormat构造函数的样式与strDate的样式必须相符
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        //必须捕获异常
        try {
            Date date=simpleDateFormat.parse(dateString);
            return date;
        } catch(Exception px) {
            px.printStackTrace();
        }
        return null;
    }


    /**
     * 根据当前时间，获取yyyy-MM-dd字符串
     * @param date 参数可为空
     * @return
     */
    public static String getDateString(Date... dates){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String result=null;
        for (Date date : dates){
            result=df.format(date);
        }
        if(StringUtils.isEmpty(result)){
            return df.format(new Date());
        }
        return result;
    }

    /**
     * 根据key获取Map类型的值
     * @param param
     * @param key
     * @return
     */
    public static String getMapValueString(Map<String,String> param,String key){
        if(param.containsKey(key)){
            String s = param.get(key);
            if(StringUtils.isEmpty(s)){
                return "";
            }else{
                return s;
            }
        }
        return "";
    }

}





