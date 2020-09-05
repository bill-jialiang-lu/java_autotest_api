package com.lemon.utils;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.lemon.pojo.CaseInfo;
import com.lemon.pojo.TestInfo;
import com.lemon.pojo.WriteBackData;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class ExcelUtils {

    public static void main(String[] args) {
        List<CaseInfo> res1 = read(0,1,CaseInfo.class);
        for (CaseInfo caseInfo : res1) {
            System.out.println(caseInfo);
        }

        System.out.println("================");
        List<TestInfo> res2 = read(1,1,TestInfo.class);
        for (TestInfo testInfo : res2) {
            System.out.println(testInfo);
        }
    }

    //批量回写存储list集合
    public static List<WriteBackData> wbdList = new ArrayList<>();

    /**
     * Excel数据读取，返回指定对象
     * @param sheetIndex        开始sheet索引
     * @param sheetNum          sheet个数
     * @param clazz             excel映射类字节对象
     * @return
     */
    public static List read(int sheetIndex, int sheetNum, Class clazz) {
        //easypoi
        try {
            FileInputStream fis = new FileInputStream(Constants.EXCEL_PATH);
            ImportParams params = new ImportParams();
            params.setStartSheetIndex(sheetIndex);
            params.setSheetNum(sheetNum);

            List res = ExcelImportUtil.importExcel(fis, clazz, params);
            System.out.println(res);
            fis.close();

            return  res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void batchWrite() throws Exception {
        FileInputStream fis = new FileInputStream(Constants.EXCEL_PATH);
        Workbook sheets = WorkbookFactory.create(fis);
        int sheetIndex;
        int rowNum;
        int cellNum;
        String content;
        for (WriteBackData writeBackData : wbdList) {
            sheetIndex = writeBackData.getSheetIndex();
            rowNum = writeBackData.getRowNum();
            cellNum = writeBackData.getCellNum();
            content = writeBackData.getContent();
            Sheet sheetAt = sheets.getSheetAt(sheetIndex);
            Row row = sheetAt.getRow(rowNum);
            Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellValue(content);
        }
        FileOutputStream fos = new FileOutputStream(Constants.EXCEL_PATH);
        sheets.write(fos);
        fis.close();
        fos.close();

    }

}
