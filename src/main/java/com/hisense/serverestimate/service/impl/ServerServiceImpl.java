package com.hisense.serverestimate.service.impl;

import com.hisense.serverestimate.entity.BaseEnterprise;
import com.hisense.serverestimate.entity.BaseServer;
import com.hisense.serverestimate.entity.ServerEnterpriseRel;
import com.hisense.serverestimate.mapper.BaseEnterpriseMapper;
import com.hisense.serverestimate.mapper.BaseServerMapper;
import com.hisense.serverestimate.mapper.ServerEnterpriseRelMapper;
import com.hisense.serverestimate.service.ServerService;
import com.hisense.serverestimate.utils.HiStringUtil;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * @Author Huang.bingzhi
 * @Date 2019/6/20 19:36
 * @Version 1.0
 */
@Service
public class ServerServiceImpl implements ServerService {
    @Value("${baseinfo.startrow}")
    private int startRow;

    @Autowired
    private BaseServerMapper serverMapper;

    @Autowired
    private BaseEnterpriseMapper enterpriseMapper;

    @Autowired
    private ServerEnterpriseRelMapper serverEnterpriseRelMapper;

    @Override
    public boolean importServerEnterprise(MultipartFile dataFile) {
        Workbook workbook = null;
        try {
            workbook=StreamingReader.builder()
                    .rowCacheSize(100)
                    .bufferSize(4098)
                    .open(dataFile.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Sheet sheet = workbook.getSheetAt(0);
        int rowIndex = 0;
        Map<String, BaseServer> serverMap = new HashMap<>();
        Map<String, BaseEnterprise> enterpriseMap = new HashMap<>();
        List<ServerEnterpriseRel> serverEnterpriseRels = new ArrayList<>();
        for (Row row : sheet) {
            if (rowIndex < startRow) {
                rowIndex++;
                continue;
            }
            rowIndex++;
            BaseServer server = new BaseServer(HiStringUtil.getRandomUUID(),
                    getValue(row.getCell(2)), getValue(row.getCell(1)), getValue(row.getCell(0)),
                    getValue(row.getCell(3)), getValue(row.getCell(4)), getValue(row.getCell(5)),
                    getValue(row.getCell(6)), getValue(row.getCell(7)));
            BaseEnterprise enterprise = new BaseEnterprise(HiStringUtil.getRandomUUID(), getValue(row.getCell(11)),
                    getValue(row.getCell(10)), getValue(row.getCell(9)), getValue(row.getCell(8)), getValue(row.getCell(8)));

            if (!serverMap.containsKey(server.getServerCode())) {
                serverMap.put(server.getServerCode(), server);
            }
            if (!enterpriseMap.containsKey(enterprise.getCis())) {
                enterpriseMap.put(enterprise.getCis(), enterprise);
            }
            ServerEnterpriseRel serverEnterpriseRel = new ServerEnterpriseRel(HiStringUtil.getRandomUUID(), server.getServerCode(), enterprise.getCis());
            serverEnterpriseRels.add(serverEnterpriseRel);
            for (ServerEnterpriseRel temp : serverEnterpriseRels) {
                if (temp.getEnterpriseCis().equals(serverEnterpriseRel.getEnterpriseCis())
                        && temp.getServerCode().equals(serverEnterpriseRel.getServerCode())
                        && !temp.getId().equals(serverEnterpriseRel.getId())) {
                    serverEnterpriseRels.remove(temp);
                    break;
                }
            }

        }
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        enterpriseMapper.deleteAll();
        enterpriseMapper.insertAll(enterpriseMap.values());
        serverMapper.deleteAll();
        serverMapper.insertAll(serverMap.values());
        serverEnterpriseRelMapper.deleteAll();
        serverEnterpriseRelMapper.insertAll(serverEnterpriseRels);
        return true;
    }

    private String getValue(Cell xCell) {
        String result = "";
        if (xCell != null) {
            switch (xCell.getCellType()) {
                case _NONE:
                    break;
                case NUMERIC:
                    result = Double.valueOf(xCell.getNumericCellValue()).longValue() + "";
                    break;
                case STRING:
                    result = xCell.getStringCellValue();
                    break;
                case FORMULA:
                    break;
                case BLANK:
                    break;
                case BOOLEAN:
                    result = String.valueOf(xCell.getBooleanCellValue());
                    break;
                case ERROR:
                    break;
            }
        }
        return result;
    }
}
