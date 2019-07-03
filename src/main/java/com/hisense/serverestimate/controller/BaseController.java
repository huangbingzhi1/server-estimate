package com.hisense.serverestimate.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: Huang.bingzhi
 * @Date: 2019/4/25 22:29
 * @Version 1.0
 * 基类，定义各个controller常用到的方法和变量
 */
@Controller
public class BaseController {
    @Value("${user.numberperpage}")
    public int numberPerPage;
    public static final String SUCCESS = "success";
    public static final String FAILED = "failed";
}
