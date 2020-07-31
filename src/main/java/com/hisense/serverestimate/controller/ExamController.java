package com.hisense.serverestimate.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hisense.serverestimate.entity.*;
import com.hisense.serverestimate.mapper.*;
import com.hisense.serverestimate.service.ExamService;
import com.hisense.serverestimate.service.ServerService;
import com.hisense.serverestimate.service.impl.UserServiceImpl;
import com.hisense.serverestimate.utils.Encryption;
import com.hisense.serverestimate.utils.HiStringUtil;
import com.hisense.serverestimate.utils.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
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
    private static final Logger logger = LoggerFactory.getLogger(ExamController.class);

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
    private ErrorExamDetailMapper errorExamDetailMapper;
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
    @Autowired
    private ServerService serverService;
    @Autowired
    private XsAccountMapper xsAccountMapper;

    @RequestMapping(value = "getExamList", method = RequestMethod.GET)
    @ResponseBody
    public String getExamList(@RequestParam("jsonParam") String jsonParam) {
        JSONObject parseObject = JSON.parseObject(jsonParam);
        int page = HiStringUtil.getJsonIntByKey(parseObject, "page");
        Map<String, Object> param = new HashMap<>(10);
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
        return SUCCESS;
    }

    @RequestMapping(value = "deleteExamByQid", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public String deleteExamByQid(@RequestParam("jsonParam") String jsonParam) {
        examMainMapper.setExpireByQid(jsonParam);
        examDetailMapper.deleteByQid(jsonParam);
        examTitleMapper.deleteByQid(jsonParam);
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
                    if (newAnswer != main.getAnswercount() || !entity.getName().equals(main.getExamName())) {
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
                    if (!StringUtils.isEmpty(titleLine[i]) && titleLine[i].contains(":")) {
                        String[] lineEntity = titleLine[i].split(":");
                        if (2 == lineEntity.length) {
                            ExamTitle examTitle = new ExamTitle();
                            examTitle.setTitleId(HiStringUtil.getRandomUUID());
                            examTitle.setQid(entity.getQid());
                            examTitle.setTitleNo(lineEntity[0]);
                            examTitle.setTitleName(lineEntity[1]);
                            examTitleMapper.insert(examTitle);
                        }
                    }
                }
            }
        }
        return SUCCESS;
    }

    @RequestMapping(value = "getExamDetailListByLoginAccount", method = RequestMethod.GET)
    @ResponseBody
    public String getExamDetailListByLoginAccount(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            XsAccount account=null;
            Map<String, Object> param = new HashMap<>();
            HttpSession session = request.getSession();
            if(null!=session.getAttribute("account")){
                account = (XsAccount) session.getAttribute("account");
                param.put("cis", account.getCisCode());
            }else if(null!=session.getAttribute("loginUser")){
                BaseUser user= (BaseUser) session.getAttribute("loginUser");
                param.put("cis",user.getUsername());
            }else{
                return "[]";
            }
//            测试数据
//            param.put("cis", "2007651");
            if (!StringUtils.isEmpty(jsonParam)) {
                JSONObject parseObject = JSON.parseObject(jsonParam);
                String hasDealed = HiStringUtil.getJsonStringByKey(parseObject, "hasDealed");
                String startDate = HiStringUtil.getJsonStringByKey(parseObject, "startDate");
                String endDate = HiStringUtil.getJsonStringByKey(parseObject, "endDate");
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
            }
            List<Map<String, Object>> examInfoByCisList = examMainMapper.getExamInfoByCis(param);
            if(CollectionUtils.isEmpty(examInfoByCisList)&&null!=account){
                param.put("cis",account.getMdmCode());
                examInfoByCisList = examMainMapper.getExamInfoByCis(param);
            }
            for (int i = 0; i < examInfoByCisList.size(); i++) {
                Map<String, Object> exams = examInfoByCisList.get(i);
                StringBuilder stringBuilder = new StringBuilder(exams.get("url").toString());
                stringBuilder.append(Encryption.encrypByMD5(param.get("cis").toString().concat(",").concat(exams.getOrDefault("server_code", "").toString())));
                exams.put("url", stringBuilder.toString());
            }
            return JSON.toJSONString(examInfoByCisList);
        } catch (Exception e) {
            logger.error(e.toString());
            return "";
        }
    }

    /**
     * 接收推送的填写者提交的数据
     */
    @RequestMapping(value = "receiveExamResult", method = RequestMethod.POST)
    @ResponseBody
    public void receiveExamResult(HttpServletRequest request, HttpServletResponse response, BufferedReader br) {
        String jsonParam = "";
        StringBuilder stringBuilder=new StringBuilder();
        try {
            //body部分
            String inputLine;
            try {
                while ((inputLine = br.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                br.close();
            } catch (IOException e) {
                System.out.println("IOException: " + e);
            }
            jsonParam=stringBuilder.toString();
            System.out.println("str:" + jsonParam);
            logger.error("==================");
            logger.error(jsonParam);
            errorExamDetailMapper.addErrorExamDetail("received",jsonParam);
            logger.error("------------------");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            jsonParam="{'activity':'41943184','sojumpparm':'242076AC66EC61FF9E348218E3995C4F','name':'问卷名称','parteruser':'15581018823','parterjoiner':'test4','timetaken':'528','submittime':'2016-08-23 10:01:59', 'q1':'50','q2': '7','q3':'13','q4':'9','q5':'2','q6':'878787','q7':'似的sss发射点发射点',joinid:'101812480275','totalvalue':'15'}";
            JSONObject parseObject = JSON.parseObject(jsonParam);
            String qid = HiStringUtil.getJsonStringByKey(parseObject, "activity");
            String sojumpparm = HiStringUtil.getJsonStringByKey(parseObject, "sojumpparm");
            Map<String, String> cisServerCodeMd5Map = serverService.getCisServerCodeMd5Map();
            if (cisServerCodeMd5Map.containsKey(sojumpparm)) {
                sojumpparm = cisServerCodeMd5Map.get(sojumpparm);
            } else {
                errorExamDetailMapper.addErrorExamDetail("sojumpparm错误",jsonParam);
                logger.error("sojumpparm错误");
                return;
            }
            String cis = null;
            String serverCode = null;
            if (!StringUtils.isEmpty(sojumpparm)) {
                String[] split = sojumpparm.split(",");
                if (split.length == 2) {
                    cis = split[0];
                    serverCode = split[1];
                }
            }
            if (StringUtils.isEmpty(cis) || StringUtils.isEmpty(serverCode)) {
                errorExamDetailMapper.addErrorExamDetail("缺少cis或者服务商编码",jsonParam);
                logger.error("缺少cis或者服务商编码");
                return;
            }
            if (StringUtils.isEmpty(qid)) {
                return;
            }
            ExamMain examMain = examMainMapper.selectByQid(qid);
            Map<String, String> param = new HashMap<>();
            param.put("qid", qid);
            param.put("serverCode", serverCode);
            param.put("cis", cis);
            ExamDetail examDetail = examDetailMapper.selectByQidCisServerCode(param);
            if (null == examMain || null == examDetail) {
                errorExamDetailMapper.addErrorExamDetail("查不到相关问卷",jsonParam);
                logger.error("查不到相关问卷");
                return;
            }
            int timetaken = HiStringUtil.getJsonIntByKey(parseObject, "timetaken");
            Date submittime = new Date();
            String submittimeStr = HiStringUtil.getJsonStringByKey(parseObject, "submittime");
            if (StringUtils.isEmpty(submittimeStr)) {
                submittime = sdf.parse(submittimeStr);
            }
            String scoreTypeIndexs = examMain.getScoreTypeIndexs();
            String textTypeIndexs = examMain.getTextTypeIndexs();
            String[] scoreTypeQuestionNo = null;
            String[] textTypeQuestionNo = null;
            StringBuilder scoreStringBuilder = new StringBuilder();
            StringBuilder textStringBuilder = new StringBuilder();
            int totalScore = 0;
            if (!StringUtils.isEmpty(scoreTypeIndexs)) {
                scoreTypeQuestionNo = scoreTypeIndexs.split(",");
            }
            if (!StringUtils.isEmpty(textTypeIndexs)) {
                textTypeQuestionNo = textTypeIndexs.split(",");
            }
            if (null != scoreTypeQuestionNo) {
                for (int i = 0; i < scoreTypeQuestionNo.length; i++) {
                    int jsonIntByKey = HiStringUtil.getJsonIntByKey(parseObject, "q" + scoreTypeQuestionNo[i]);
                    if (i != 0) {
                        scoreStringBuilder.append(',');
                    }
                    scoreStringBuilder.append(jsonIntByKey);
                    totalScore += jsonIntByKey;
                }
            }
            if (null != textTypeQuestionNo) {
                for (int i = 0; i < textTypeQuestionNo.length; i++) {
                    String jsonStringByKey = HiStringUtil.getJsonStringByKey(parseObject, "q" + textTypeQuestionNo[i]);
                    if (i != 0) {
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
            logger.error("---------success-----------");
        } catch (Exception e) {
            logger.error(e.toString());
            errorExamDetailMapper.addErrorExamDetail(e.toString().substring(0,99),jsonParam);
        }
    }
    /**
     * 接收用户提交的问卷答案
     */
    @RequestMapping(value = "sendExamResult", method = RequestMethod.POST)
    @ResponseBody
    public String sendExamResult(@RequestBody String jsonParam,HttpServletRequest request, HttpServletResponse response) {
        try {
            errorExamDetailMapper.addErrorExamDetail("received",jsonParam);
            logger.error("------------------");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            jsonParam="{'activity':'41943184','sojumpparm':'242076AC66EC61FF9E348218E3995C4F','name':'问卷名称','parteruser':'15581018823','parterjoiner':'test4','timetaken':'528','submittime':'2016-08-23 10:01:59', 'q1':'50','q2': '7','q3':'13','q4':'9','q5':'2','q6':'878787','q7':'似的sss发射点发射点',joinid:'101812480275','totalvalue':'15'}";
            JSONObject parseObject = JSON.parseObject(jsonParam);
            parseObject = JSON.parseObject(parseObject.getString("jsonParam").toString());
            jsonParam=parseObject.toJSONString();
            String qid = HiStringUtil.getJsonStringByKey(parseObject, "activity");
            String sojumpparm = HiStringUtil.getJsonStringByKey(parseObject, "sojumpparm");
            Map<String, String> cisServerCodeMd5Map = serverService.getCisServerCodeMd5Map();
            if (cisServerCodeMd5Map.containsKey(sojumpparm)) {
                sojumpparm = cisServerCodeMd5Map.get(sojumpparm);
            } else {
                errorExamDetailMapper.addErrorExamDetail("sojumpparm错误",jsonParam);
                logger.error("sojumpparm错误");
                return "error";
            }
            String cis = null;
            String serverCode = null;
            if (!StringUtils.isEmpty(sojumpparm)) {
                String[] split = sojumpparm.split(",");
                if (split.length == 2) {
                    cis = split[0];
                    serverCode = split[1];
                }
            }
            if (StringUtils.isEmpty(cis) || StringUtils.isEmpty(serverCode)) {
                errorExamDetailMapper.addErrorExamDetail("缺少cis或者服务商编码",jsonParam);
                logger.error("缺少cis或者服务商编码");
                return "error";
            }
            if (StringUtils.isEmpty(qid)) {
                return "error";
            }
            ExamMain examMain = examMainMapper.selectByQid(qid);
            Map<String, String> param = new HashMap<>();
            param.put("qid", qid);
            param.put("serverCode", serverCode);
            param.put("cis", cis);
            ExamDetail examDetail = examDetailMapper.selectByQidCisServerCode(param);
            if (null == examMain || null == examDetail) {
                errorExamDetailMapper.addErrorExamDetail("查不到相关问卷",jsonParam);
                logger.error("查不到相关问卷");
                return "error";
            }
            int timetaken = HiStringUtil.getJsonIntByKey(parseObject, "timetaken");
            Date submittime = new Date();
            String submittimeStr = HiStringUtil.getJsonStringByKey(parseObject, "submittime");
            if (!StringUtils.isEmpty(submittimeStr)) {
                submittime = sdf.parse(submittimeStr);
            }
            String scoreTypeIndexs = examMain.getScoreTypeIndexs();
            String textTypeIndexs = examMain.getTextTypeIndexs();
            String[] scoreTypeQuestionNo = null;
            String[] textTypeQuestionNo = null;
            StringBuilder scoreStringBuilder = new StringBuilder();
            StringBuilder textStringBuilder = new StringBuilder();
            int totalScore = 0;
            if (!StringUtils.isEmpty(scoreTypeIndexs)) {
                scoreTypeQuestionNo = scoreTypeIndexs.split(",");
            }
            if (!StringUtils.isEmpty(textTypeIndexs)) {
                textTypeQuestionNo = textTypeIndexs.split(",");
            }
            if (null != scoreTypeQuestionNo) {
                for (int i = 0; i < scoreTypeQuestionNo.length; i++) {
                    int jsonIntByKey = HiStringUtil.getJsonIntByKey(parseObject, "q" + scoreTypeQuestionNo[i]);
                    if (i != 0) {
                        scoreStringBuilder.append(',');
                    }
                    scoreStringBuilder.append(jsonIntByKey);
                    totalScore += jsonIntByKey;
                }
            }
            if (null != textTypeQuestionNo) {
                for (int i = 0; i < textTypeQuestionNo.length; i++) {
                    String jsonStringByKey = HiStringUtil.getJsonStringByKey(parseObject, "q" + textTypeQuestionNo[i]);
                    if (i != 0) {
                        scoreStringBuilder.append(',');
                    }
                    textStringBuilder.append(jsonStringByKey);
                }

            }
            examDetail.setSubmittime(submittime);
            examDetail.setTimetaken(0);
            examDetail.setTotleScore(totalScore);
            examDetail.setScoreArray(scoreStringBuilder.toString());
            examDetail.setTextArray(textStringBuilder.toString());
            examDetail.setSourceData(jsonParam);
            examDetailMapper.updateByPrimaryKey(examDetail);
            examDetailMapper.updateMeanScoreByQidCis(param);
            logger.error("---------success-----------");
            return "success";
        } catch (Exception e) {
            logger.error(e.toString());
            errorExamDetailMapper.addErrorExamDetail(e.toString().substring(0,99),jsonParam);
            return "error";
        }
    }

    /**
     * 下载问卷结果
     *
     * @param qid
     */
    @RequestMapping(value = "downloadExamResultData", method = RequestMethod.GET)
    @ResponseBody
    public void downloadExamResultData(@RequestParam("qid") String qid, HttpServletRequest request, HttpServletResponse response) {
        BaseUser loginUser = SessionUtil.getLoginUser();
        Map<String, Object> param = new HashMap<>(2);
        param.put("qid", qid);
        if ("guest".equals(loginUser.getRoleId())) {
            param.put("company", loginUser.getCompany());
        }
        List<Map<String, Object>> examResult = examDetailMapper.getAllExamResult(param);
        ExamMain main = examMainMapper.selectByQid(qid);
        List<ExamTitle> examTitle = examTitleMapper.selectByQid(qid);
        examService.downloadExamResultData(response, main, examResult, examTitle);
    }


    @RequestMapping(value = "getExamResultList", method = RequestMethod.GET)
    @ResponseBody
    public String getExamResultList(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request, HttpServletResponse response) {
        BaseUser loginUser = SessionUtil.getLoginUser();
        Map<String, Object> param = new HashMap<>(5);
        JSONObject parseObject = JSON.parseObject(jsonParam);
        String qid = HiStringUtil.getJsonStringByKey(parseObject, "qid");
        param.put("qid", qid);
        if ("guest".equals(loginUser.getRoleId())) {
            param.put("company", loginUser.getCompany());
        }
        String keyword = HiStringUtil.getJsonStringByKey(parseObject, "keyword");
        if (!StringUtils.isEmpty(keyword)) {
            keyword = "%" + keyword + "%";
            param.put("keyword", keyword);
        }
        int page = HiStringUtil.getJsonIntByKey(parseObject, "page");
        param.put("startIndex", (page - 1) * numberPerPage);
        param.put("pCount", numberPerPage);
        List<Map<String, Object>> examResult = examDetailMapper.getEnterpriseExamResult(param);
        if (!CollectionUtils.isEmpty(examResult)) {
            double listNum = examDetailMapper.getEnterpriseExamResultNum(param);
            Map<String, Object> result = new HashMap<>();
            result.put("totalPage", Math.ceil(listNum / numberPerPage));
            result.put("list", examResult);
            result.put("currentPage", page);
            return JSON.toJSONString(result);
        }
        return "";
    }
    @RequestMapping(value = "getExamProcessList", method = RequestMethod.GET)
    @ResponseBody
    public String getExamProcessList(@RequestParam("jsonParam") String jsonParam, HttpServletRequest request, HttpServletResponse response) {
        BaseUser loginUser = SessionUtil.getLoginUser();
        Map<String, Object> param = new HashMap<>(5);
        JSONObject parseObject = JSON.parseObject(jsonParam);
        String qid = HiStringUtil.getJsonStringByKey(parseObject, "qid");
        param.put("qid", qid);
        if ("guest".equals(loginUser.getRoleId())) {
            param.put("company", loginUser.getCompany());
        }
        String keyword = HiStringUtil.getJsonStringByKey(parseObject, "keyword");
        if (!StringUtils.isEmpty(keyword)) {
            keyword = "%" + keyword + "%";
            param.put("keyword", keyword);
        }
        int page = HiStringUtil.getJsonIntByKey(parseObject, "page");
        param.put("startIndex", (page - 1) * numberPerPage);
        param.put("pCount", numberPerPage);
        List<Map<String, Object>> examResult = examDetailMapper.getEnterpriseExamProcess(param);
        if (!CollectionUtils.isEmpty(examResult)) {
            double listNum = examDetailMapper.getEnterpriseExamProcessNum(param);
            Map<String, Object> result = new HashMap<>();
            result.put("totalPage", Math.ceil(listNum / numberPerPage));
            result.put("list", examResult);
            result.put("currentPage", page);
            return JSON.toJSONString(result);
        }
        return "";
    }
    /**
     * 下载问卷过程
     */
    @RequestMapping(value = "downloadExamProcessData", method = RequestMethod.GET)
    @ResponseBody
    public void downloadExamProcessData(@RequestParam("qid") String qid, HttpServletRequest request, HttpServletResponse response) {
        BaseUser loginUser = SessionUtil.getLoginUser();
        Map<String, Object> param = new HashMap<>(2);
        param.put("qid", qid);
        if ("guest".equals(loginUser.getRoleId())) {
            param.put("company", loginUser.getCompany());
        }
        List<Map<String, Object>> examResult = examDetailMapper.getAllExamProcess(param);
        examService.downloadExamProcessData(response, examResult);
    }
    /**
     * 下载评价汇总
     *
     * @param qid
     */
    @RequestMapping(value = "downloadStaticByServerCompany", method = RequestMethod.GET)
    @ResponseBody
    public void downloadStaticByServerCompany(@RequestParam("qid") String qid, HttpServletRequest request, HttpServletResponse response) {
        BaseUser loginUser = SessionUtil.getLoginUser();
        Map<String, Object> param = new HashMap<>(2);
        param.put("qid", qid);
        if (!"admin".equals(loginUser.getRoleId())) {
            return;
        }
//        List<Map<String, Object>> examResult = examDetailMapper.staticByServerCompany(param);
//        examService.staticByServerCompany(response, examResult);
        List<Map<String, Object>> examResult = examDetailMapper.collection2(param);
        examService.staticByServerCompany2(response, examResult);
    }
    /**
     * 给信商账号新增一个密码
     *
     */
    @RequestMapping(value = "dealCis", method = RequestMethod.GET)
    @ResponseBody
    public XsAccount dealCis(@RequestParam("account") String account, HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> result=new HashMap<>(5);
        XsAccount xsAccount = xsAccountMapper.selectByAccount("fwpjxt"+account);

        if(null!=xsAccount){
            xsAccount.setPassword(get6RandomNumber());
            xsAccountMapper.setPassword(xsAccount.getAid(),xsAccount.getPassword());
        }else{
            xsAccount=new XsAccount();
            xsAccount.setPassword(get6RandomNumber());
            xsAccount.setCisCode(account);
            xsAccount.setAccount("fwpjxt"+account);
            xsAccount.setAid(UUID.randomUUID().toString());
            xsAccount.setEmail("this account is insert by fwpjxt");
            xsAccountMapper.insert(xsAccount);
        }
        return xsAccount;
    }
    private  String get6RandomNumber(){
        int newNum = (int)((Math.random()*9+1)*100000);
        return String.valueOf(newNum);
    }
    @GetMapping("examInfo")
    public Result<List<ExamInfo>> getExamInfo(@RequestParam("account") String account){
        List<ExamInfo> result=new ArrayList<>(1);
        if(StringUtils.isEmpty(account)){
            return Result.error("参数不能为空");
        }
        XsAccount xsAccount= xsAccountMapper.selectByAccount(account);
        if(xsAccount==null){
            return Result.error("该账号为空");
        }
        return Result.success(examService.getExamInfo(xsAccount));
    }
}

