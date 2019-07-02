package com.hisense.serverestimate;

import com.hisense.serverestimate.controller.ExamController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author Huang.bingzhi
 * @Date 2019/5/26 10:14
 * @Version 1.0
 */
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class ExamSchedule {
    @Autowired
    private ExamController examController;
    //每隔1小时执行一次拉取和更新问卷任务
    @Scheduled(cron = "0 0 0/1 * * ?")
    private void configureTasks() {
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
        examController.synchronizeExam();
    }
}