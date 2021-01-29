package com.hisense.serverestimate.service.impl;

import com.hisense.serverestimate.controller.ExamController;
import com.hisense.serverestimate.entity.*;
import com.hisense.serverestimate.mapper.ExamDetailMapper;
import com.hisense.serverestimate.mapper.ExamMainMapper;
import com.hisense.serverestimate.mapper.ExamTitleMapper;
import com.hisense.serverestimate.service.ExamService;
import com.hisense.serverestimate.utils.Encryption;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author Huang.bingzhi
 * @Date 2019/6/26 22:16
 * @Version 1.0
 */
@Service
public class ExamServiceImpl implements ExamService {
    @Resource
    private ExamMainMapper mainMapper;
    @Autowired
    private ExamDetailMapper detailMapper;
    @Autowired
    private ExamTitleMapper titleMapper;
    public static final int LINE_START_ENTERPRISE=2;

    private CellStyle normalCellStyle;
    private CellStyle titleCellStyle;

    @Override
    public void addExamDetail(ExamMain main) {

    }
    /**
     * 生成样式
     */
    private void createCellStyle(Workbook workbook) {
        normalCellStyle = workbook.createCellStyle();
        normalCellStyle.setBorderBottom(BorderStyle.THIN);
        normalCellStyle.setBorderLeft(BorderStyle.THIN);
        normalCellStyle.setBorderRight(BorderStyle.THIN);
        normalCellStyle.setBorderTop(BorderStyle.THIN);

        titleCellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        titleCellStyle.setFont(font);
        titleCellStyle.setBorderBottom(BorderStyle.THIN);
        titleCellStyle.setBorderLeft(BorderStyle.THIN);
        titleCellStyle.setBorderRight(BorderStyle.THIN);
        titleCellStyle.setBorderTop(BorderStyle.THIN);
        titleCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        titleCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
    }

    @Override
    public void downloadExamResultData(HttpServletResponse response, ExamMain main, List<Map<String, Object>> examResult, List<ExamTitle> titles) {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        String fileName=main.getExamName()+".xlsx";
        try {
            response.setHeader("Content-Disposition", "attachment;filename=\""+new String(fileName.getBytes("gb2312"),"ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Workbook workbook =  new XSSFWorkbook() ;
        createCellStyle(workbook);
//        int rowIndex=2;
        int rowIndexStart=2;
        int totalRowIndexStart=2;
        Map<String,Integer> rowIndexMap=new HashMap<>(100);
        String scoreTypeIndexs = main.getScoreTypeIndexs();
        String textTypeIndexs = main.getTextTypeIndexs();
        String[] scoreTypeIndexArr = scoreTypeIndexs.split(",");
        String[] textTypeIndexArr = textTypeIndexs.split(",");

        Sheet totalSheet = workbook.createSheet("总计");
        createSheetTitle(totalSheet,scoreTypeIndexArr,textTypeIndexArr,titles);
        for (Map<String, Object> detail:examResult){
            String companyName = detail.get("company_name").toString();
            Sheet currSheet = workbook.getSheet(companyName);
            if(null==currSheet){
                rowIndexMap.put(companyName,rowIndexStart);
                currSheet = workbook.createSheet(companyName);
                createSheetTitle(currSheet,scoreTypeIndexArr,textTypeIndexArr,titles);
            }
            String  scoreStr = detail.getOrDefault("score_array","").toString();
            String[] scoreArr = scoreStr.split(",");
            String  textStr = detail.getOrDefault("text_array","").toString();
            String[] textArr = textStr.split("#-hisense-#");
            int cellIndex=0;
            int totalCellIndex=0;
            Row currRow = currSheet.createRow(rowIndexMap.get(companyName));
            rowIndexMap.put(companyName,rowIndexMap.get(companyName)+1);
            currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("server_company_name","").toString());
            currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("server_name","").toString());
            currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("server_code","").toString());
            currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("server_type","").toString());
            currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("manager","").toString());
            currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("province","").toString());
            currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("city","").toString());
            currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("district","").toString());
            currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("company_name","").toString());
            currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("office","").toString());
            currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("enterprise_name","").toString());
            currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("enterprise_cis","").toString());
            for (int i = 0; i < scoreTypeIndexArr.length; i++) {
                if(i<scoreArr.length){
                    currRow.createCell(cellIndex++).setCellValue(scoreArr[i]);
                }else{
                    currRow.createCell(cellIndex++);
                }
            }
            for (int i = 0; i < textTypeIndexArr.length; i++) {
                if(i<textArr.length){
                    currRow.createCell(cellIndex++).setCellValue(textArr[i]);
                }else{
                    currRow.createCell(cellIndex++);
                }
            }
            currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("totle_score","").toString());
            currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("mean_score","").toString());
            for (int i = 0; i < cellIndex; i++) {
                currRow.getCell(i).setCellStyle(normalCellStyle);
            }

            Row totalRow = totalSheet.createRow(totalRowIndexStart++);
            totalRow.createCell(totalCellIndex++).setCellValue(detail.getOrDefault("server_company_name","").toString());
            totalRow.createCell(totalCellIndex++).setCellValue(detail.getOrDefault("server_name","").toString());
            totalRow.createCell(totalCellIndex++).setCellValue(detail.getOrDefault("server_code","").toString());
            totalRow.createCell(totalCellIndex++).setCellValue(detail.getOrDefault("server_type","").toString());
            totalRow.createCell(totalCellIndex++).setCellValue(detail.getOrDefault("manager","").toString());
            totalRow.createCell(totalCellIndex++).setCellValue(detail.getOrDefault("province","").toString());
            totalRow.createCell(totalCellIndex++).setCellValue(detail.getOrDefault("city","").toString());
            totalRow.createCell(totalCellIndex++).setCellValue(detail.getOrDefault("district","").toString());
            totalRow.createCell(totalCellIndex++).setCellValue(detail.getOrDefault("company_name","").toString());
            totalRow.createCell(totalCellIndex++).setCellValue(detail.getOrDefault("office","").toString());
            totalRow.createCell(totalCellIndex++).setCellValue(detail.getOrDefault("enterprise_name","").toString());
            totalRow.createCell(totalCellIndex++).setCellValue(detail.getOrDefault("enterprise_cis","").toString());
            for (int i = 0; i < scoreTypeIndexArr.length; i++) {
                if(i<scoreArr.length){
                    totalRow.createCell(totalCellIndex++).setCellValue(scoreArr[i]);
                }else{
                    totalRow.createCell(totalCellIndex++);
                }
            }
            for (int i = 0; i < textTypeIndexArr.length; i++) {
                if(i<textArr.length){
                    totalRow.createCell(totalCellIndex++).setCellValue(textArr[i]);
                }else{
                    totalRow.createCell(totalCellIndex++);
                }
            }
            totalRow.createCell(totalCellIndex++).setCellValue(detail.getOrDefault("totle_score","").toString());
            totalRow.createCell(totalCellIndex++).setCellValue(detail.getOrDefault("mean_score","").toString());
            for (int i = 0; i < totalCellIndex; i++) {
                totalRow.getCell(i).setCellStyle(normalCellStyle);
            }
        }

        try {
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void downloadExamProcessData(HttpServletResponse response, List<Map<String, Object>> examProcess) {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        String fileName="评价过程.xlsx";
        String sheetName="全部";
        try {
            response.setHeader("Content-Disposition", "attachment;filename=\""+new String(fileName.getBytes("gb2312"),"ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Workbook workbook =  new XSSFWorkbook() ;
        createCellStyle(workbook);
        int rowIndexStart=1;

        Map<String,Integer> rowIndexMap=new HashMap<>(100);
        if(!CollectionUtils.isEmpty(examProcess)){
            for (Map<String,Object> detail:examProcess){
                Sheet currSheet = workbook.getSheet(sheetName);
                if(null==currSheet){
                    rowIndexMap.put(sheetName,rowIndexStart);
                    currSheet = workbook.createSheet(sheetName);
                    createProcessSheetTitle(currSheet);
                }
                int cellIndex=0;
                Row currRow = currSheet.createRow(rowIndexMap.get(sheetName));
                rowIndexMap.put(sheetName,rowIndexMap.get(sheetName)+1);
                currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("company_name","").toString());
                currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("enterprise_cis","").toString());
                currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("enterprise_name","").toString());
                currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("pre_num","").toString());
                currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("post_num","").toString());
                currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("total_num","").toString());
                for (int i = 0; i < cellIndex; i++) {
                    currRow.getCell(i).setCellStyle(normalCellStyle);
                }
            }
            try {
                workbook.write(out);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void staticByServerCompany(HttpServletResponse response, List<Map<String, Object>> examResult) {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        String fileName="汇总.xlsx";
        String sheetName="汇总";
        try {
            response.setHeader("Content-Disposition", "attachment;filename=\""+new String(fileName.getBytes("gb2312"),"ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Workbook workbook =  new XSSFWorkbook() ;
        Sheet sheet = workbook.createSheet(sheetName);
        createCellStyle(workbook);
        long totalAllNum=0;
        long totalPostedNum=0;
        float totalScore=0.0f;
        int rowIndexStart=2;
        if(!CollectionUtils.isEmpty(examResult)&&sheet!=null){
            createStaticTitle(sheet);
            for (Map<String,Object> detail:examResult){
                int cellIndex=0;
                Row currRow = sheet.createRow(rowIndexStart++);
                currRow.createCell(cellIndex++).setCellValue(rowIndexStart-2);
                currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("server_company_name","").toString());
                long totalNum=(long)detail.getOrDefault("totalnum", 0);
                long postedNum=(long)detail.getOrDefault("postednum", 0);
                float avgScore=Float.parseFloat(detail.getOrDefault("avgscore", "0").toString());
                currRow.createCell(cellIndex++).setCellValue(totalNum);
                currRow.createCell(cellIndex++).setCellValue(postedNum);
                currRow.createCell(cellIndex++).setCellValue(avgScore);
                totalAllNum+=totalNum;
                totalPostedNum+=postedNum;
                totalScore+=postedNum*avgScore;
                for (int i = 0; i < cellIndex; i++) {
                    currRow.getCell(i).setCellStyle(normalCellStyle);
                }
            }
            Row currRow = sheet.createRow(rowIndexStart);
            int cellIndex=0;
            currRow.createCell(cellIndex++).setCellValue("");
            currRow.createCell(cellIndex++).setCellValue("总计");
            currRow.createCell(cellIndex++).setCellValue(totalAllNum);
            currRow.createCell(cellIndex++).setCellValue(totalPostedNum);
            if(totalPostedNum==0){
                currRow.createCell(cellIndex++).setCellValue(0);
            }else{
                currRow.createCell(cellIndex++).setCellValue(totalScore/totalPostedNum);
            }
            for (int i = 0; i < cellIndex; i++) {
                currRow.getCell(i).setCellStyle(normalCellStyle);
            }
            try {
                workbook.write(out);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    public void staticByServerCompany2(HttpServletResponse response, List<Map<String, Object>> examResult) {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        String fileName="汇总.xlsx";
        String sheetName="汇总";
        try {
            response.setHeader("Content-Disposition", "attachment;filename=\""+new String(fileName.getBytes("gb2312"),"ISO8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        OutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Workbook workbook =  new XSSFWorkbook() ;
        Sheet sheet = workbook.createSheet(sheetName);
        createCellStyle(workbook);
        long totalAllNum=0;
        long totalPostedNum=0;
        float totalScore=0.0f;
        int rowIndexStart=2;
        if(!CollectionUtils.isEmpty(examResult)&&sheet!=null){
            final String[] scoreArrays = examResult.get(0).get("scoreArray").toString().split(",");
            createStaticTitle2(sheet,scoreArrays.length);
            Map<String,ExamCollectionVO> companyCollection=new HashMap<>(100);
            for (Map<String,Object> detail:examResult) {
                final String companyName = detail.get("companyName").toString();
                if (companyCollection.containsKey(companyName)) {
                    companyCollection.get(companyName).addItem(detail);
                } else {
                    companyCollection.put(companyName, new ExamCollectionVO(detail));
                }
            }
            if(!CollectionUtils.isEmpty(companyCollection)) {
                for(ExamCollectionVO vo:companyCollection.values()){
                    int cellIndex=0;
                    Row currRow = sheet.createRow(rowIndexStart++);
                    currRow.createCell(cellIndex++).setCellValue(rowIndexStart-2);
                    currRow.createCell(cellIndex++).setCellValue(vo.getCompany());
                    currRow.createCell(cellIndex++).setCellValue(vo.getServerNum());
                    currRow.createCell(cellIndex++).setCellValue(vo.getCisNum());
                    for (String temp:vo.getMeanScoreByNoList()){
                        currRow.createCell(cellIndex++).setCellValue(temp);
                    }
                    final double sum = vo.getMeanScoreByNoList().stream().mapToDouble(s -> Double.valueOf(s)).sum();
                    currRow.createCell(cellIndex++).setCellValue(sum);
                }
            }
//            Row currRow = sheet.createRow(rowIndexStart);
//            int cellIndex=0;
//            currRow.createCell(cellIndex++).setCellValue("");
//            currRow.createCell(cellIndex++).setCellValue("总计");
//            currRow.createCell(cellIndex++).setCellValue(totalAllNum);
//            currRow.createCell(cellIndex++).setCellValue(totalPostedNum);
//            if(totalPostedNum==0){
//                currRow.createCell(cellIndex++).setCellValue(0);
//            }else{
//                currRow.createCell(cellIndex++).setCellValue(totalScore/totalPostedNum);
//            }
//            for (int i = 0; i < cellIndex; i++) {
//                currRow.getCell(i).setCellStyle(normalCellStyle);
//            }
            try {
                workbook.write(out);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void createStaticTitle2(Sheet sheet,int qNum) {
        Row newRow = sheet.createRow(0);
        for (int i = 0; i < 5+qNum; i++) {
            Cell cell = newRow.createCell(0);
            cell.setCellStyle(titleCellStyle);
        }
        int index=0;
        newRow.getCell(0).setCellValue("服务商评价得分");
        newRow = sheet.createRow(1);
        newRow.createCell(index++).setCellValue("序号");
        newRow.createCell(index++).setCellValue("分公司");
        newRow.createCell(index++).setCellValue("服务商数量");
        newRow.createCell(index++).setCellValue("评价商家数量");
        for (int i=0;i<qNum;i++){
            newRow.createCell(index++).setCellValue("题目"+(i+1)+"(平均分)");
        }
        newRow.createCell(index++).setCellValue("总分（平均）");
        for (int i = 0; i < index; i++) {
            newRow.getCell(i).setCellStyle(titleCellStyle);
            sheet.setColumnWidth(i, 20 * 256);
        }
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 4+qNum);
        sheet.addMergedRegion(region);
    }
    private void createStaticTitle(Sheet sheet) {
        Row newRow = sheet.createRow(0);
        for (int i = 0; i < 5; i++) {
            Cell cell = newRow.createCell(0);
            cell.setCellStyle(titleCellStyle);
        }
        newRow.getCell(0).setCellValue("服务商评价得分");
        newRow = sheet.createRow(1);
        newRow.createCell(0).setCellValue("序号");
        newRow.createCell(1).setCellValue("分公司");
        newRow.createCell(2).setCellValue("服务商数量");
        newRow.createCell(3).setCellValue("评价商家数量");
        newRow.createCell(4).setCellValue("平均分");
        for (int i = 0; i < 5; i++) {
            newRow.getCell(i).setCellStyle(titleCellStyle);
            sheet.setColumnWidth(i, 20 * 256);
        }
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 4);
        sheet.addMergedRegion(region);
    }

    private void createProcessSheetTitle(Sheet sheet){
        Row newRow = sheet.createRow(0);
        newRow.createCell(0).setCellValue("分公司");
        newRow.createCell(1).setCellValue("商家编码");
        newRow.createCell(2).setCellValue("商家名称");
        newRow.createCell(3).setCellValue("待评价");
        newRow.createCell(4).setCellValue("已评价");
        newRow.createCell(5).setCellValue("总计");
        for (int i = 0; i <= 5; i++) {
            newRow.getCell(i).setCellStyle(titleCellStyle);
            sheet.setColumnWidth(i, 10 * 256);
        }
        sheet.setColumnWidth(2, 40 * 256);
    }

    private void createSheetTitle(Sheet sheet, String[] scoreTypeIndexArr, String[] textTypeIndexArr, List<ExamTitle> titles) {
        for (int j = 0; j < LINE_START_ENTERPRISE; j++) {
            Row newRow = sheet.createRow(j);
            for (int i = 0; i < 14 + scoreTypeIndexArr.length+textTypeIndexArr.length; i++) {
                Cell cell = newRow.createCell(i);
                cell.setCellStyle(titleCellStyle);
                sheet.setColumnWidth(i, 20 * 256);
            }
        }
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 7);
        sheet.addMergedRegion(region);
        region = new CellRangeAddress(0, 0, 8, 11);
        sheet.addMergedRegion(region);
        region = new CellRangeAddress(0, 0, 12, 13+scoreTypeIndexArr.length+textTypeIndexArr.length);
        sheet.addMergedRegion(region);
        Row row1=sheet.getRow(0);
        row1.getCell(0).setCellValue("赛维服务商信息");
        row1.getCell(8).setCellValue("商贸商家信息");
        row1.getCell(12).setCellValue("商家评价内容");
        int cellIndex=0;
        Row row2 = sheet.getRow(1);
        row2.getCell(cellIndex++).setCellValue("所属分公司");
        row2.getCell(cellIndex++).setCellValue("服务商名称");
        row2.getCell(cellIndex++).setCellValue("服务商编号");
        row2.getCell(cellIndex++).setCellValue("服务商性质备注");
        row2.getCell(cellIndex++).setCellValue("服务商经理");
        row2.getCell(cellIndex++).setCellValue("办公-省");
        row2.getCell(cellIndex++).setCellValue("办公-市");
        row2.getCell(cellIndex++).setCellValue("办公-区");
        row2.getCell(cellIndex++).setCellValue("商贸分公司");
        row2.getCell(cellIndex++).setCellValue("办事处");
        row2.getCell(cellIndex++).setCellValue("商家名称");
        row2.getCell(cellIndex++).setCellValue("商家CIS系统编码");
        for (int i = 0; i <scoreTypeIndexArr.length ; i++) {
            for (ExamTitle title: titles){
                if(title.getTitleNo().equals("q".concat(scoreTypeIndexArr[i]))){
                    row2.getCell(cellIndex++).setCellValue(title.getTitleName());
                }
            }
        }
        for (int i = 0; i <textTypeIndexArr.length ; i++) {
            for (ExamTitle title: titles){
                if(title.getTitleNo().equals("q".concat(textTypeIndexArr[i]))){
                    row2.getCell(cellIndex++).setCellValue(title.getTitleName());
                }
            }
        }
        row2.getCell(cellIndex++).setCellValue("合计分值");
        row2.getCell(cellIndex++).setCellValue("最终得分（平均分）");
    }

    @Override
    public List<ExamInfo> getExamInfo(XsAccount xsAccount) {
        StringBuilder stringBuilder = new StringBuilder();
        List<ExamInfo> result=new ArrayList<>(1);
        List<ExamDetail> details=detailMapper.listExamDetailByCis(xsAccount.getCisCode());
//        Map<String, ExamDetail> soVOMap = details.stream().collect(Collectors.toMap(ExamDetail::getMainQid, Function.identity()));
        final List<String> mainQids=details.stream().map(ExamDetail::getMainQid).distinct().collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(mainQids)) {
            mainQids.forEach(qid -> {
                ExamInfo temp=new ExamInfo();
                temp.setId(qid);
                temp.setDetails(details.stream().filter(item -> item.getMainQid().equals(qid)).collect(Collectors.toList()));
                List<ExamDetail> detailsByQid=details.stream().filter(item -> item.getMainQid().equals(qid)).collect(Collectors.toList());
                detailsByQid.forEach(item->{
                    stringBuilder.setLength(0);
                    stringBuilder.append(item.getEnterpriseCis())
                            .append(",")
                            .append(item.getServerCode());
//                    item.setEnterpriseCis(Encryption.encrypByMD5(stringBuilder.toString()));
                    item.setEnterpriseCis((stringBuilder.toString()));

                });
                final List<ExamTitle> examTitles=titleMapper.selectByQid(qid);
                if (!CollectionUtils.isEmpty(examTitles)) {
                    temp.setDesc(examTitles);
                }
                result.add(temp);
            });
        }
        return result;
    }
}
