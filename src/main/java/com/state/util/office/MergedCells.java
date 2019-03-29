package com.state.util.office;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

public class MergedCells {  
    /** 测试使用的POI版本是3.1 
      * @param args 
      */  
      public static void main(String[] args) throws IOException {     
             HSSFWorkbook wb = new HSSFWorkbook();     
             HSSFSheet sheet = wb.createSheet("new sheet");     
   
             HSSFRow row = sheet.createRow(1);     
             HSSFCell cell = row.createCell(1);     
             cell.setCellValue("This is a test of merging");     
               
             //1.生成字体对象  
             HSSFFont font = wb.createFont();  
             font.setFontHeightInPoints((short) 10);  
             font.setFontName("新宋体");  
             font.setColor(HSSFColor.BLUE.index);  
             font.setBoldweight((short) 0.8);  
             //2.生成样式对象  
             HSSFCellStyle style = wb.createCellStyle();  
             style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  
             style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);  
             style.setFont(font); //调用字体样式对象  
             style.setWrapText(true);  
                            //增加表格边框的样式 例子  
                            style.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);  
                        style.setBorderLeft(HSSFCellStyle.BORDER_DOUBLE);  
                        style.setTopBorderColor(HSSFColor.GOLD.index);  
                        style.setLeftBorderColor(HSSFColor.PLUM.index);  
   
             //3.单元格应用样式  
             cell.setCellStyle(style);  
               
   
               
             //新版用法 3.8版  
            sheet.addMergedRegion(new CellRangeAddress(     
                    1, //first row (0-based)  from 行     
                    4, //last row  (0-based)  to 行     
                    0, //first column (0-based) from 列     
                    0  //last column  (0-based)  to 列     
            ));     
             //表示合并B2,B3  
            /* sheet.addMergedRegion(new Region(     
                      1, //first row (0-based)       
                     (short)1, //first column  (0-based)       
                      2, //last row (0-based)    
                     (short)1  //last column  (0-based)       
              )); */    
               
             //合并叠加  表示合并B3 B4。但是B3已经和B2合并了，所以，变成B2:B4合并了  
             /*sheet.addMergedRegion(new Region(     
                      2, //first row (0-based)       
                     (short)1, //first column  (0-based)       
                      3, //last row (0-based)    
                     (short)1  //last column  (0-based)       
              )); */   
               
             //一下代码表示在D4 cell 插入一段字符串  
             HSSFRow row2 = sheet.createRow(3);  
             HSSFCell cell2 = row2.createCell((short)3);   
             cell2.setCellValue("this is a very very very long string , please check me out.");  
                       //cell2.setCellValue(new HSSFRichTextString("我是单元格！"));  
   
   
              // Write the output to a file     
              FileOutputStream fileOut = new FileOutputStream("D:/workbook.xls");     
              wb.write(fileOut);     
              fileOut.close();     
          }     
 }  
