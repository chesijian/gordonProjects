package com.state.util.office;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFSimpleShape;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;

import com.state.po.AreaReportBean;
import com.state.po.ResultPo;
import com.state.util.CommonUtil;
import com.state.util.ResultUtil;

/**
 * 导出Excel
 * 
 * @Title: ExportExcel.java
 * @author Lanxiaowei
 * @date 2012-7-26 下午4:47:15
 * @version V1.0
 */
public class ExportExcelUtil {
	private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
	private static final int LEFT_HIGH_RIGHT_LOW = 0;
	private static final int LEFT_LOW_RIGHT_HIGH = 1;


	/**
	 * 导出日报
	 * @author 车斯剑
	 * @date 2016年10月23日上午9:52:17
	 * @param title
	 * @param headers
	 * @param dataset
	 * @param out
	 * @param pattern
	 */
	public static void exportDailyExcel(String dailyDate, String[] headers, Map<String,List<ResultPo>> dailyData, OutputStream out) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet("sheet 1");
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 10);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);// 设置样式
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = workbook.createFont();// 生成一个字体
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) 14);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);// 把字体应用到当前的样式
		
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.WHITE.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont font2 = workbook.createFont();// 生成另一个字体
		font2.setColor(HSSFColor.BLACK.index);
		font2.setFontHeightInPoints((short) 12);
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		style2.setFont(font2);// 把字体应用到当前的样式
		
		// 标题样式
		HSSFFont headfont = workbook.createFont();
        headfont.setFontName("宋体");
        headfont.setFontHeightInPoints((short) 13);// 字体大小
        headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle headstyle = workbook.createCellStyle();
        headstyle.setFont(headfont);
        headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
        headstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
        headstyle.setLocked(true);  
        // 表头标题样式
 		HSSFFont tableTitlefont = workbook.createFont();
 		tableTitlefont.setFontName("宋体");
 		tableTitlefont.setFontHeightInPoints((short) 11);// 字体大小
	    HSSFCellStyle tableTitleStyle = workbook.createCellStyle();
	    tableTitleStyle.setFont(tableTitlefont);
	    tableTitleStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 左
	    tableTitleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
	    tableTitleStyle.setLocked(true); 

		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		List<ResultPo> salePowers = null;
		List<ResultPo> salePrices = null;
		List<ResultPo> saleEachPower = null;
		List<ResultPo> buyPowers = null;
		List<ResultPo> buyPrices = null;
		if(CommonUtil.ifEmpty(dailyData)){
			salePowers = dailyData.get("送端电力出清结果");
			salePrices = dailyData.get("送端电价出清结果");// 四川送段电力出清集合
			saleEachPower = dailyData.get("送端电力出清结果详细");// 四川送段电价出清集合
			buyPowers = dailyData.get("受端电力出清结果");// 四川送江苏送端电力出清集合
			buyPrices = dailyData.get("受端电价出清结果");// 四川送江苏受端电力出清集合
		}
		
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 25));
        HSSFRow titleRow = sheet.createRow(0);
        //titleRow.setHeight((short) 0x349);
        titleRow.setHeight((short) 0x200);
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle(headstyle);
        titleCell.setCellValue("可再生能源跨省区消纳现货市场运行日报");
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 25));
        HSSFRow titleRow1 = sheet.createRow(1);
        HSSFCell titleCell1 = titleRow1.createCell(0);
        titleCell1.setCellStyle(headstyle);
        titleCell1.setCellValue(dailyDate);
        
        List<String> exportFieldTitle = new ArrayList<String>();
        exportFieldTitle.add("出清时段");
        for(int i=0; i<24; i++){
        	exportFieldTitle.add(""+i);
        }
		// 遍历集合数据，产生数据行
        int a =5;
        a =insertData(sheet,salePowers,exportFieldTitle,"一、送端电力出清结果总体情况（单位：兆瓦）",tableTitleStyle,style,style2,a,"sale");
        a =insertData(sheet,salePrices,exportFieldTitle,"二、送端电价出清结果情况（单位：元）",tableTitleStyle,style,style2,a,"sale");
        a =insertData(sheet,saleEachPower,exportFieldTitle,"三、送端电力出清结果详细情况（单位：兆瓦）",tableTitleStyle,style,style2,a,"buy");
        a =insertData(sheet,buyPowers,exportFieldTitle,"四、受端电力出清结果情况（单位：兆瓦）",tableTitleStyle,style,style2,a,"buy2");
        a =insertData(sheet,buyPrices,exportFieldTitle,"五、受端电价出清结果情况（单位：元）",tableTitleStyle,style,style2,a,"buy2");

		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 插入表格
	 * @author 车斯剑
	 * @date 2016年10月26日下午2:10:43
	 * @param sheet
	 * @param salePowers
	 * @param exportFieldTitle
	 * @param titleStr
	 * @param tableTitleStyle
	 * @param style
	 * @param style2
	 * @param a
	 */
	public static int insertData(HSSFSheet sheet,List<ResultPo> salePowers,List<String> exportFieldTitle,
			String titleStr,HSSFCellStyle tableTitleStyle,HSSFCellStyle style,HSSFCellStyle style2, int a,String type){
		// 产生表格标题行
		sheet.addMergedRegion(new CellRangeAddress(a-2, a-2, 0, 24));
        HSSFRow titleRow1 = sheet.createRow(a-2);
        HSSFCell titleCell1 = titleRow1.createCell(0);
        titleCell1.setCellStyle(tableTitleStyle);
        titleCell1.setCellValue(titleStr);
		
		HSSFRow row = null;
		// 产生表头
		if (exportFieldTitle.size() != 0) {
			row = sheet.createRow(a-1);
			HSSFCell fristCell = row.createCell(0);
			fristCell.setCellStyle(style);
			HSSFRichTextString fristText = new HSSFRichTextString(exportFieldTitle.get(0));
			fristCell.setCellValue(fristText);
			sheet.addMergedRegion(new CellRangeAddress( a-1, a-1,0,1));
			for (short i = 1; i < exportFieldTitle.size(); i++) {
				HSSFCell cell = row.createCell(i+1);
				cell.setCellStyle(style);
				HSSFRichTextString text = new HSSFRichTextString(exportFieldTitle.get(i));
				cell.setCellValue(text);

			}
		}
		//插入数据
		if(salePowers!=null){
			for (ResultPo po : salePowers) {
				sheet.addMergedRegion(new CellRangeAddress(a, a+3,0,0));
				//int index = 1;
				for(int i=0; i<4; i++){
					row = sheet.createRow(i+a);
					HSSFCell bengin1 = row.createCell(0);
					bengin1.setCellStyle(style2);
					/*if("sale".equals(type)){
						bengin1.setCellValue("四川送出");
					}else if("buy".equals(type)){
						bengin1.setCellValue("四川送"+po.getArea());
					}else{
						bengin1.setCellValue(po.getArea()+"受四川");
					}*/
					bengin1.setCellValue(po.getCorridor());
					bengin1.setCellType(HSSFCell.CELL_TYPE_STRING);
					
					HSSFCell bengin = row.createCell(1);
					bengin.setCellStyle(style2);
					bengin.setCellValue(i*15);
					bengin.setCellType(HSSFCell.CELL_TYPE_STRING);
					for(int j=0; j<24; j++){
						HSSFCell cell = row.createCell(j+2);
						cell.setCellStyle(style2);
						Double value = ResultUtil.getResultPoValue(po, i+(j*4)+1);
						BigDecimal bd = new BigDecimal(value.toString());
						bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
						HSSFRichTextString richString = new HSSFRichTextString(bd.doubleValue() + "");
						cell.setCellValue(richString);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						//index++;
					}
				}
				
				a+=4;
			}
		}
		return a+5;
	}
	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年10月23日下午10:39:54
	 * @param title
	 * @param headers
	 * @param dataset
	 * @param out
	 * @param pattern
	 */
	public static void exportAreaExcel(String[] headers, List<AreaReportBean> dataset, OutputStream out) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet("sheet 1");
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置样式
		style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) 14);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.WHITE.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setColor(HSSFColor.BLACK.index);
		font2.setFontHeightInPoints((short) 12);
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);

		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		
		int index = 0;
		List<String> exportFieldTitle = new ArrayList<String>();

		HSSFRow row = null;
		
		for (AreaReportBean areaReportBean : dataset) {
			index++;
			row = sheet.createRow(index);
			
			HSSFCell areaCell = row.createCell(0);
			areaCell.setCellStyle(style2);
			HSSFRichTextString areaString = new HSSFRichTextString(areaReportBean.getArea());
			areaCell.setCellValue(areaString);
			areaCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			
			HSSFCell sumqCell = row.createCell(1);
			sumqCell.setCellStyle(style2);
			BigDecimal bd = new BigDecimal(areaReportBean.getSumq());
			bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
			HSSFRichTextString richString = new HSSFRichTextString(bd.doubleValue() + "");
			sumqCell.setCellValue(richString);
			sumqCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			
			HSSFCell cqqCell = row.createCell(2);
			cqqCell.setCellStyle(style2);
			BigDecimal bd2 = new BigDecimal(areaReportBean.getCqq());
			bd2 = bd2.setScale(2, BigDecimal.ROUND_HALF_UP);
			HSSFRichTextString cqqString = new HSSFRichTextString(bd2.doubleValue() + "");
			cqqCell.setCellValue(cqqString);
			cqqCell.setCellType(HSSFCell.CELL_TYPE_STRING);
			
		}

		// 产生表格标题行
		row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);

			// 画表头斜线
			if (i == 0) {
				drawSlash(patriarch, i);
			}
		}

		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @author: Lanxiaowei
	 * @date: 2012-7-27 下午3:40:39
	 * @Title: drawSlash
	 * @Description: TODO(画斜线)
	 * @param patriarch
	 *            画图管理器
	 * @param rowIndex
	 *            在第几行画斜线，从0开始计算
	 * @param colIndex
	 *            在第几列画斜线，从0开始计算
	 * @param rowSpan
	 *            跨几行，从1开始计算
	 * @param cloSpan
	 *            跨几列，从1开始计算
	 * @param direction
	 *            斜线方向，默认左高右低
	 * @return void
	 * @throws
	 */
	public static void drawSlash(HSSFPatriarch patriarch, int rowIndex, int colIndex, int rowSpan, int colSpan, int direction) {
		HSSFClientAnchor anchor = new HSSFClientAnchor();
		if (direction == LEFT_HIGH_RIGHT_LOW) {
			// 左高右低
			anchor.setAnchor((short) (colIndex), rowIndex, 0, 0, (short) ((colIndex + 1) * colSpan), (rowIndex + 1) * rowSpan, 0, 0);
		} else {
			// 左低右高
			anchor.setAnchor((short) (colIndex), (rowIndex + 1) * rowSpan, 0, 0, (short) ((colIndex + 1) * colSpan), rowIndex, 0, 0);
		}
		HSSFSimpleShape shape = patriarch.createSimpleShape(anchor);
		shape.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
		shape.setLineStyleColor(2);
	}

	/**
	 * @author: Lanxiaowei
	 * @date: 2012-7-27 下午3:47:59
	 * @Title: drawSlash
	 * @Description: TODO(画斜线[重载],斜线方向默认左高右低)
	 * @param patriarch
	 *            画图管理器
	 * @param colIndex
	 *            在第几列画斜线，从0开始计算
	 * @return void
	 * @throws
	 */
	private static void drawSlash(HSSFPatriarch patriarch, int colIndex) {
		drawSlash(patriarch, 0, colIndex, 1, 1, LEFT_HIGH_RIGHT_LOW);
	}

	/**
	 * @author: Lanxiaowei
	 * @date: 2012-7-27 上午9:28:14
	 * @Title: InputStreamToByte
	 * @Description: TODO(输入流转换成byte[])
	 * @param is
	 *            输入流
	 * @return byte[] 字节数组
	 * @throws IOException
	 */
	private static byte[] inputStreamToByte(InputStream is) throws IOException {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = is.read()) != -1) {
			bytestream.write(ch);
		}
		byte imgdata[] = bytestream.toByteArray();
		bytestream.close();
		return imgdata;
	}

}
