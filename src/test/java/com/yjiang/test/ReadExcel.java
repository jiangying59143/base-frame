package com.yjiang.test;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReadExcel {
    public static void main(String[] args) throws Exception {
        List<Map<String,String>> list1 = readFile("D:/系统导出.xlsx", 0);
        List<Map<String, String>> list2 = readFile("D:/建档立卡最新.xlsx", 0);
        List<Map<String, String>> l2 = new ArrayList<>();
        List<Map<String, String>> l1 = new ArrayList<>();
        for (Map<String, String> map2 : list2) {
            boolean flag = false;
            for (Map<String, String> map1 : list1) {
                String  sfzh = map1.get("身份证号码").startsWith("'")? map1.get("身份证号码").substring(1) : map1.get("身份证号码");
                if(sfzh.equals(map2.get("身份证号"))){
                    flag = true;
                    break;
                }
            }
            if(!flag){
                l2.add(map2);
            }
        }
        for (Map<String, String> map1 : list1) {
            boolean flag = false;
            for (Map<String, String> map2 : list2) {
                String  sfzh = map1.get("身份证号码").startsWith("'")? map1.get("身份证号码").substring(1) : map1.get("身份证号码");
                if(sfzh.equals(map2.get("身份证号"))){
                    flag = true;
                    break;
                }
            }
            if(!flag){
                l1.add(map1);
            }
        }
        printList(l1, "D:/建档导出比较.xlsx","导出不在建档",  "户主姓名", "成员姓名", "身份证号码", "建档单位");
        printList(l2, "D:/建档导出比较.xlsx","建档不在导出", "姓名", "身份证号", "行政村");
/*
        List<Map<String, String>> l3 = new ArrayList<>();
        List<Map<String, String>> l4 = new ArrayList<>();

        for (Map<String, String> map1 : l1) {
            for (Map<String, String> map2 : l2) {
                if(map1.get("姓名").equals(map2.get("姓名"))){
                    l3.add(map1);
                    map2.put("贫困户属性", map1.get("贫困户\n" +
                            "属性"));
                    map2.put("行政村", map1.get("行政村"));
                    l4.add(map2);
                }
            }
        }
        printList(l3, l4, "名字相同");

        for (Map<String, String> map2 : list2) {
            boolean flag = false;
            for (Map<String, String> map1 : list1) {
                if((map1.get("姓名").equals(map2.get("姓名")) && map1.get("身份证号").contains(map2.get("身份证号"))) || map2.containsKey("贫困户属性")){
                    map2.put("行政村", map1.get("行政村"));
                    map2.put("贫困户属性", map1.get("贫困户\n" +
                            "属性"));
                    flag = true;
                    break;
                }
            }
            if(!flag){
                map2.put("贫困户属性", "");
            }
        }

        printList(list2, null, "贫困户属性", "行政村", "贫困户属性");*/
    }

    public static List<Map<String,String>> readFile(String fileName, int sheetNum) {
        Workbook wb =null;
        Sheet sheet = null;
        Row row = null;
        List<Map<String,String>> list = null;
        String cellData = null;
        wb = getWorkbook(fileName);
        if(wb != null){
            //用来存放表中数据
            list = new ArrayList<Map<String,String>>();
            //获取第一个sheet
            sheet = wb.getSheetAt(sheetNum);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            //获取第一行
            row = sheet.getRow(0);
            //获取最大列数
            int colnum = row.getPhysicalNumberOfCells();
            List<String> columns = new ArrayList<>();
            for (int i = 0; i<=rownum; i++) {
                if(i==0){
                    for (int j=0;j<colnum;j++){
                        columns.add((String) getCellFormatValue(row.getCell(j)));
                    }
                }else {
                    Map<String, String> map = new LinkedHashMap<String, String>();
                    row = sheet.getRow(i);
                    if (row != null) {
                        for (int j = 0; j < colnum; j++) {
                            cellData = (String) getCellFormatValue(row.getCell(j));
                            map.put(columns.get(j), cellData);
                        }
                    } else {
                        break;
                    }
                    list.add(map);
                }
            }
        }

        return list;

    }

    public static void printList(List<Map<String,String>> list, List<Map<String,String>> list2, String name, String... s) throws Exception {
        //遍历解析出来的list
        String filePath = "D:/导出建档比较数据.xlsx";
        File file = new File(filePath);
        if(!file.exists()){
            createExcel(filePath);
        }
        Workbook wb = getWorkbook(filePath);
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        Sheet sheet = wb.getSheet("Sheet1");
        if(sheet != null){
            wb.setSheetName(0, name);
        }else{
            sheet = wb.createSheet(name);
        }
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = list.get(i);
            Row row = sheet.createRow(i);
            if(list2 != null){
                for (Map<String, String> map2 : list2) {
                    if(map.get("姓名").equals(map2.get("姓名"))){
                        row.createCell(0).setCellValue(map.get("姓名"));
                        row.createCell(1).setCellValue(map.get("身份证号"));
                        row.createCell(2).setCellValue(map2.get("姓名"));
                        row.createCell(3).setCellValue(map2.get("身份证号"));
                        break;
                    }
                }
            }else {
                row.createCell(0).setCellValue(map.get("姓名"));
                row.createCell(1).setCellValue(map.get("身份证号"));
                if(map.containsKey("贫困户\n" +"属性")){
                    row.createCell(2).setCellValue(map.get("贫困户\n" +"属性"));
                }
                if(s != null){
                    for (int i1 = 1; i1 <= s.length; i1++) {
                        row.createCell(1+i1).setCellValue(map.get(s[i1-1]));
                    }
                }
            }
        }
        wb.write(fileOutputStream);
    }

    public static void printList(List<Map<String,String>> list,String filePath, String sheetName, String... s) throws Exception {
        //遍历解析出来的list
        Workbook wb;
        File file = new File(filePath);
        if(!file.exists()){
            createExcel(filePath);
            wb = getWorkbook(filePath);
        }else{
            wb = getWorkbook(filePath);
            if(wb.getSheet(sheetName) != null) {
                file.delete();
                createExcel(filePath);
                wb = getWorkbook(filePath);
            }
        }

        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        Sheet sheet = wb.getSheet("Sheet1");
        if(sheet != null){
            wb.setSheetName(0, sheetName);
        }else{
            sheet = wb.createSheet(sheetName);
        }

        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = list.get(i);
            Row row = sheet.createRow(i);

            if(s != null){
                for (int i1 = 0; i1 < s.length; i1++) {
                    row.createCell(i1).setCellValue(map.get(s[i1]));
                }
            }
        }
        wb.write(fileOutputStream);
    }
    //读取excel
    public static Workbook getWorkbook(String filePath){
        Workbook wb = null;
        if(filePath==null){
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if(".xls".equals(extString)){
                return wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(extString)){
                return wb = new XSSFWorkbook(is);
            }else{
                return wb = null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }

    public static void createExcel(String path) throws Exception {
        String extString = path.substring(path.lastIndexOf("."));
        //创建Excel文件对象
        Workbook wb = null;
        if(".xls".equals(extString)){
            wb = new HSSFWorkbook();
        }else if(".xlsx".equals(extString)){
            wb = new XSSFWorkbook();
        }else{
            return;
        }
        //用文件对象创建sheet对象  
        Sheet sheet = wb.createSheet("Sheet1");
        //输出Excel文件
        FileOutputStream output = new FileOutputStream(path);
        wb.write(output);
        output.flush();
    }

    public static Object getCellFormatValue(Cell cell){
        Object cellValue = null;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
                case Cell.CELL_TYPE_NUMERIC:{
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA:{
                    //判断cell是否为日期格式
                    if(DateUtil.isCellDateFormatted(cell)){
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    }else{
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:{
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }
}
