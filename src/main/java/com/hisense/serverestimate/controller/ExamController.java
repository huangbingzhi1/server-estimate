package com.hisense.serverestimate.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hisense.serverestimate.entity.*;
import com.hisense.serverestimate.mapper.ExamDetailMapper;
import com.hisense.serverestimate.mapper.ExamMainMapper;
import com.hisense.serverestimate.mapper.ExamTitleMapper;
import com.hisense.serverestimate.service.ExamService;
import com.hisense.serverestimate.utils.HiStringUtil;
import com.hisense.serverestimate.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Huang.bingzhi
 * @Date 2019/6/26 14:39
 * @Version 1.0
 */
@RestController
@RequestMapping("examController")
public class ExamController extends BaseController {
    @Value("${wjx.examListUrl}")
    public String wjxExamListUrl;
    @Value("${wjx.examTitleListUrl}")
    public String wjxExamTitleListUrl;
    @Value("${scoreTypeIndexs}")
    public String scoreTypeIndexs;
    @Value("${textTypeIndexs}")
    public String textTypeIndexs;
    @Value("${wjx.domain}")
    public String wjxDomain;
    @Autowired
    private ExamMainMapper examMainMapper;
    @Autowired
    private ExamTitleMapper examTitleMapper;
    @Autowired
    private ExamDetailMapper examDetailMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ExamService examService;

    @RequestMapping(value = "getExamList", method = RequestMethod.GET)
    @ResponseBody
    public String getExamList(@RequestParam("jsonParam") String jsonParam) {
        JSONObject parseObject = JSON.parseObject(jsonParam);
        int page = HiStringUtil.getJsonIntByKey(parseObject, "page");
        Map<String, Object> param = new HashMap<>();
        param.put("startIndex", (page - 1) * numberPerPage);
        param.put("pCount", numberPerPage);
        String keyword = HiStringUtil.getJsonStringByKey(parseObject, "keyword");
        if (!StringUtils.isEmpty(keyword)) {
            keyword = "%" + keyword + "%";
            param.put("keyword", keyword);
        }
        List<ExamMain> examList = examMainMapper.getExamList(param);
        if (!CollectionUtils.isEmpty(examList)) {
            double listNum = examMainMapper.getExamListNum(param);
            Map<String, Object> result = new HashMap<>();
            result.put("totalPage", Math.ceil(listNum / numberPerPage));
            result.put("list", examList);
            result.put("currentPage", page);
            return JSON.toJSONString(result);
        }
        return "";
    }

    @RequestMapping(value = "saveExam", method = RequestMethod.GET)
    @ResponseBody
    public String saveExam(@RequestParam("jsonParam") String jsonParam) {
        JSONObject parseObject = JSON.parseObject(jsonParam);
        String mainId = HiStringUtil.getJsonStringByKey(parseObject, "mainId");
        String status = HiStringUtil.getJsonStringByKey(parseObject, "status");
        String examName = HiStringUtil.getJsonStringByKey(parseObject, "examName");
        String scoreTypeIndexs = HiStringUtil.getJsonStringByKey(parseObject, "scoreTypeIndexs");
        String textTypeIndexs = HiStringUtil.getJsonStringByKey(parseObject, "textTypeIndexs");
        String examDomain = HiStringUtil.getJsonStringByKey(parseObject, "examDomain");
        if (!StringUtils.isEmpty(mainId)) {
            ExamMain examMain = examMainMapper.selectByPrimaryKey(mainId);
            examMain.setStatus(status);
            examMain.setExamName(examName);
            examMain.setScoreTypeIndexs(scoreTypeIndexs);
            examMain.setTextTypeIndexs(textTypeIndexs);
            examMain.setExamDomain(examDomain);
            examMainMapper.updateByPrimaryKey(examMain);
        }

        System.out.println(parseObject);
        return SUCCESS;
    }

    @RequestMapping(value = "deleteExamById", method = RequestMethod.GET)
    @ResponseBody
    public String deleteExamById(@RequestParam("jsonParam") String jsonParam) {
        examMainMapper.setExpireByPrimaryKey(jsonParam);
        return SUCCESS;
    }

    @RequestMapping(value = "synchronizeExam", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String synchronizeExam() {
        String wjxExamList = restTemplate.getForObject(wjxExamListUrl, String.class);
        List<WjxExam> exams = JSON.parseArray(wjxExamList, WjxExam.class);
        List<ExamMain> examMains = examMainMapper.selectAll();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (WjxExam entity : exams) {
            boolean existFlag = false;
            for (ExamMain main : examMains) {
                if (entity.getQid().equals(main.getExamQid())) {
                    existFlag = true;
                    int newAnswer = Integer.parseInt(entity.getAnswercount());
                    if (newAnswer != main.getAnswercount()||!entity.getName().equals(main.getExamName())) {
                        main.setAnswercount(newAnswer);
                        main.setExamName(entity.getName());
                        examMainMapper.updateByPrimaryKey(main);
                    }
                }
            }
            //如果本系统中不存在此问卷，则新增
            if (!existFlag) {
                ExamMain main = new ExamMain();
                main.setMainId(HiStringUtil.getRandomUUID());
                main.setExamName(entity.getName());
                main.setExamQid(entity.getQid());
                try {
                    main.setBegindate(sdf.parse(entity.getBegindate()));
                } catch (ParseException e) {
                    main.setBegindate(new Date());
                    e.printStackTrace();
                }
                main.setScoreTypeIndexs(scoreTypeIndexs);
                main.setTextTypeIndexs(textTypeIndexs);
                main.setStatus("关闭");
                main.setExamDomain(wjxDomain);
                main.setAnswercount(Integer.parseInt(entity.getAnswercount()));
                examMainMapper.insert(main);
                examDetailMapper.addExamDetail(entity.getQid());
                String wjxExamTitleList = restTemplate.getForObject(wjxExamTitleListUrl.concat(entity.getQid()), String.class);
                String[] titleLine = wjxExamTitleList.split("<br/>");
                examTitleMapper.deleteByQid(entity.getQid());
                for (int i = 0; i < titleLine.length; i++) {
                    if(!StringUtils.isEmpty(titleLine[i])&&titleLine[i].contains(":")){
                        String[] lineEntity = titleLine[i].split(":");
                        if(2==lineEntity.length){
                            ExamTitle examTitle=new ExamTitle();
                            examTitle.setTitleId(HiStringUtil.getRandomUUID());
                            examTitle.setQid(entity.getQid());
                            examTitle.setTitleNo(lineEntity[0]);
                            examTitle.setTitleName(lineEntity[1]);
                            examTitleMapper.insert(examTitle);
                        }
                    }
                }
                System.out.println(wjxExamTitleList);
            }
        }
        return SUCCESS;
    }

    @RequestMapping(value = "getExamInfoListApi", method = RequestMethod.POST)
    @ResponseBody
    public String getExamInfoListApi(@RequestParam("jsonParam") String jsonParam) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (StringUtils.isEmpty(jsonParam)) {
                return "";
            }
            JSONObject parseObject = JSON.parseObject(jsonParam);
            String cis = HiStringUtil.getJsonStringByKey(parseObject, "cis");
            String hasDealed = HiStringUtil.getJsonStringByKey(parseObject, "hasDealed");
            String startDate = HiStringUtil.getJsonStringByKey(parseObject, "startDate");
            String endDate = HiStringUtil.getJsonStringByKey(parseObject, "endDate");
            if (StringUtils.isEmpty(cis)) {
                return "";
            }
            Map<String, Object> param = new HashMap<>();
            param.put("cis", cis);
            if (!StringUtils.isEmpty(hasDealed)) {
                param.put("hasDealed", hasDealed);
            }
            if (!StringUtils.isEmpty(startDate)) {
                Date parseDate = sdf.parse(startDate);
                param.put("startDate", parseDate);
            }
            if (!StringUtils.isEmpty(endDate)) {
                Date parseDate = sdf.parse(endDate);
                param.put("endDate", endDate);
            }
            List<Map<String, Object>> examInfoByCisList = examMainMapper.getExamInfoByCis(param);
            return JSON.toJSONString(examInfoByCisList);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 接收推送的填写者提交的数据
      * @param jsonParam
     */
    @RequestMapping(value = "receiveExamResult", method = RequestMethod.GET)
    @ResponseBody
    public void receiveExamResult(@RequestParam("jsonParam") String jsonParam) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            jsonParam="{'activity':'41962622','sojumpparm':'7111487,JMS1608010167','name':'问卷名称','parteruser':'15581018823','parterjoiner':'test4','timetaken':'528','submittime':'2016-08-23 10:01:59', 'q1':'50','q2': '7','q3':'13','q4':'9','q5':'2','q6':'878787','q7':'似的sss发射点发射点',joinid:'101812480275','totalvalue':'15'}";
            JSONObject parseObject = JSON.parseObject(jsonParam);
            String qid = HiStringUtil.getJsonStringByKey(parseObject, "activity");
            String sojumpparm = HiStringUtil.getJsonStringByKey(parseObject, "sojumpparm");
            String cis=null;
            String serverCode=null;
            if(!StringUtils.isEmpty(sojumpparm)){
                String[] split = sojumpparm.split(",");
                if(split.length==2){
                    cis = split[0];
                    serverCode=split[1];
                }
            }
            if(StringUtils.isEmpty(cis)||StringUtils.isEmpty(serverCode)){
                System.out.println("缺少cis或者服务商编码");
            }
            if(StringUtils.isEmpty(qid)){
                return;
            }
            ExamMain examMain=examMainMapper.selectByQid(qid);
            Map<String,String> param=new HashMap<>();
            param.put("qid",qid);
            param.put("serverCode",serverCode);
            param.put("cis",cis);
            ExamDetail examDetail = examDetailMapper.selectByQidCisServerCode(param);
            if(null==examMain||null==examDetail){
                System.out.println("查不到相关问卷");
            }
            int timetaken=HiStringUtil.getJsonIntByKey(parseObject,"timetaken");
            Date submittime=new Date();
            String submittimeStr = HiStringUtil.getJsonStringByKey(parseObject, "submittime");
            if(StringUtils.isEmpty(submittimeStr)){
                submittime =sdf.parse(submittimeStr);
            }
            String scoreTypeIndexs = examMain.getScoreTypeIndexs();
            String textTypeIndexs = examMain.getTextTypeIndexs();
            String[] scoreTypeQuestionNo = null;
            String[]  textTypeQuestionNo= null;
            StringBuilder scoreStringBuilder=new StringBuilder();
            StringBuilder textStringBuilder=new StringBuilder();
            int totalScore=0;
            if(!StringUtils.isEmpty(scoreTypeIndexs)){
                scoreTypeQuestionNo=scoreTypeIndexs.split(",");
            }
            if(!StringUtils.isEmpty(textTypeIndexs)){
                textTypeQuestionNo=textTypeIndexs.split(",");
            }
            if(null!=scoreTypeQuestionNo) {
                for (int i = 0; i < scoreTypeQuestionNo.length; i++) {
                    int jsonIntByKey = HiStringUtil.getJsonIntByKey(parseObject, "q" +scoreTypeQuestionNo[i]);
                    if(i!=0){
                        scoreStringBuilder.append(',');
                    }
                    scoreStringBuilder.append(jsonIntByKey);
                    totalScore+=jsonIntByKey;
                }
            }
            if(null!=textTypeQuestionNo) {
                for (int i = 0; i < textTypeQuestionNo.length; i++) {
                    String jsonStringByKey = HiStringUtil.getJsonStringByKey(parseObject, "q" +textTypeQuestionNo[i]);
                    if(i!=0){
                        scoreStringBuilder.append(',');
                    }
                    textStringBuilder.append(jsonStringByKey);
                }

            }
            examDetail.setSubmittime(submittime);
            examDetail.setTimetaken(timetaken);
            examDetail.setTotleScore(totalScore);
            examDetail.setScoreArray(scoreStringBuilder.toString());
            examDetail.setTextArray(textStringBuilder.toString());
            examDetail.setSourceData(jsonParam);
            examDetailMapper.updateByPrimaryKey(examDetail);
            examDetailMapper.updateMeanScoreByQidCis(param);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    /**
     * 接收推送的填写者提交的数据
     * @param qid
     */
    @RequestMapping(value = "downloadExamResultData", method = RequestMethod.GET)
    @ResponseBody
    public void downloadExamResultData(@RequestParam("qid") String qid, HttpServletRequest request, HttpServletResponse response) {
        BaseUser loginUser = SessionUtil.getLoginUser();
        Map<String,Object> param=new HashMap<>(2);
        param.put("qid",qid);
        if(loginUser.getRoleId().equals("guest")){
            param.put("companpy",loginUser.getCompany());
        }
        List<Map<String,Object>> examResult= examDetailMapper.getAllExamResult(param);
        ExamMain main=examMainMapper.selectByQid(qid);
        List<ExamTitle> examTitle = examTitleMapper.selectByQid(qid);
        examService.downloadExamResultData(response,main,examResult,examTitle);
    }

    @RequestMapping(value = "getExamResultList", method = RequestMethod.GET)
    @ResponseBody
    public String getExamResultList(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request, HttpServletResponse response) {
        BaseUser loginUser = SessionUtil.getLoginUser();
        Map<String,Object> param=new HashMap<>(5);
        JSONObject parseObject = JSON.parseObject(jsonParam);
        String qid = HiStringUtil.getJsonStringByKey(parseObject, "qid");
        param.put("qid",qid);
        if(loginUser.getRoleId().equals("guest")){
            param.put("companpy",loginUser.getCompany());
        }
        String keyword=HiStringUtil.getJsonStringByKey(parseObject, "keyword");
        if(!StringUtils.isEmpty(keyword)){
            keyword="%"+keyword+"%";
            param.put("keyword",keyword);
        }
        int page= HiStringUtil.getJsonIntByKey(parseObject,"page");
        param.put("startIndex",(page-1)*numberPerPage);
        param.put("pCount",numberPerPage);
        List<Map<String,Object>> examResult= examDetailMapper.getEnterpriseExamResult(param);
        if(!CollectionUtils.isEmpty(examResult)) {
            double listNum = examDetailMapper.getEnterpriseExamResultNum(param);
            Map<String, Object> result = new HashMap<>();
            result.put("totalPage", Math.ceil(listNum / numberPerPage));
            result.put("list", examResult);
            result.put("currentPage", page);
            return JSON.toJSONString(result);
        }
        return "";
    }


}

