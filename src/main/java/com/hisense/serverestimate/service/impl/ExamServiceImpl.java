package com.hisense.serverestimate.service.impl;

import com.hisense.serverestimate.entity.ExamMain;
import com.hisense.serverestimate.entity.ExamTitle;
import com.hisense.serverestimate.service.ExamService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @Author Huang.bingzhi
 * @Date 2019/6/26 22:16
 * @Version 1.0
 */
@Service
public class ExamServiceImpl implements ExamService {
    public static final int LINE_START_ENTERPRISE=2;

    @Override
    public void addExamDetail(ExamMain main) {

    }

    @Override
    public void downloadExamResultData(HttpServletResponse response, ExamMain main, List<Map<String, Object>> examResult, List<ExamTitle> titles) {
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        String fileName="sss.xlsx";
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
        int rowIndex=2;
        String scoreTypeIndexs = main.getScoreTypeIndexs();
        String[] scoreTypeIndexArr = scoreTypeIndexs.split(",");


        for (Map<String, Object> detail:examResult){
            if(detail.get("server_code").toString().equals("JMS1608010167")){
                System.out.println(detail);
            }
            String company_name = detail.get("company_name").toString();
            Sheet currSheet = workbook.getSheet(company_name);
            if(null==currSheet){
                currSheet = workbook.createSheet(company_name);
                createSheetTitle(currSheet,scoreTypeIndexArr,titles);
            }
            String  scoreStr = detail.getOrDefault("score_array","").toString();
            String[] scoreArr = scoreStr.split(",");
            int cellIndex=0;
            Row currRow = currSheet.createRow(rowIndex++);
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
                    cellIndex++;
                }
            }
            currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("totle_score","").toString());
            currRow.createCell(cellIndex++).setCellValue(detail.getOrDefault("mean_score","").toString());
        }

        try {
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createSheetTitle(Sheet sheet, String[] scoreTypeIndexArr, List<ExamTitle> titles) {
        for (int j = 0; j < LINE_START_ENTERPRISE; j++) {
            Row newRow = sheet.createRow(j);
            for (int i = 0; i < 14 + scoreTypeIndexArr.length; i++) {
                newRow.createCell(i);
            }
        }
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 7);
        sheet.addMergedRegion(region);
        region = new CellRangeAddress(0, 0, 8, 11);
        sheet.addMergedRegion(region);
        region = new CellRangeAddress(0, 0, 12, 14+scoreTypeIndexArr.length);
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
        row2.getCell(cellIndex++).setCellValue("合计分值");
        row2.getCell(cellIndex++).setCellValue("最终得分（平均分）");
    }
}
