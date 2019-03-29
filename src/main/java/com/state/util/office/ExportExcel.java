package com.state.util.office;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.util.CellUtil;

import com.state.po.AreaReportBean;
import com.state.po.ResultPo;

/**
 * 导出Excel
 * 
 * @Title: ExportExcel.java
 * @author Lanxiaowei
 * @date 2012-7-26 下午4:47:15
 * @version V1.0
 */
public class ExportExcel<T> {
	private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
	private static final int LEFT_HIGH_RIGHT_LOW = 0;
	private static final int LEFT_LOW_RIGHT_HIGH = 1;

	/**
	 * @author: Lanxiaowei
	 * @date: 2012-7-27 上午9:16:54
	 * @Title: exportExcel
	 * @Description: TODO(导出Excel)
	 * @param dataset
	 *            数据集
	 * @param out
	 *            输出流
	 * @return void
	 * @throws
	 */
	public void exportExcel(Collection<T> dataset, OutputStream out) {
		exportExcel("sheet 1", null, dataset, out, DATE_FORMAT_PATTERN);
	}

	/**
	 * @author: Lanxiaowei
	 * @date: 2012-7-27 上午9:17:48
	 * @Title: exportExcel
	 * @Description: TODO(导出Excel)
	 * @param headers
	 *            表头
	 * @param dataset
	 *            数据集
	 * @param out
	 *            输出流
	 * @return void
	 * @throws
	 */
	public void exportExcel(String[] headers, Collection<T> dataset, OutputStream out) {
		exportExcel("sheet 1", headers, dataset, out, DATE_FORMAT_PATTERN);
	}

	/**
	 * @author: Lanxiaowei
	 * @date: 2012-7-27 上午9:18:56
	 * @Title: exportExcel
	 * @Description: TODO(导出Excel)
	 * @param headers
	 *            表头
	 * @param dataset
	 *            数据集
	 * @param out
	 *            输出流
	 * @param pattern
	 *            日期格式
	 * @return void
	 * @throws
	 */
	public void exportExcel(String[] headers, Collection<T> dataset, OutputStream out, String pattern) {
		exportExcel("sheet 1", headers, dataset, out, pattern);
	}

	/**
	 * @author: 大雄
	 * @date: 2016-8-30 上午9:13:18
	 * @Title: exportDataToExcel
	 * @Description: TODO(导出Excel)
	 * @param title
	 *            工作表标题
	 * @param headers
	 *            表头
	 * @param dataset
	 *            数据集
	 * @param out
	 *            输出流
	 * @param pattern
	 *            日期格式
	 * @return void
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public static void exportDataToExcel(String title, String[] headers, Collection<Map<String, Object>> dataset, OutputStream out, String pattern) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
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
		/*
		 * HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
		 * 0, 0, 0, (short) 4, 2, (short) 6, 5)); // 设置注释内容
		 * comment.setString(new HSSFRichTextString("可以在POI中添加注释！")); //
		 * 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容. comment.setAuthor("leno");
		 */

		// 遍历集合数据，产生数据行
		Iterator<Map<String, Object>> it = dataset.iterator();
		int index = 0;
		List<String> exportFieldTitle = new ArrayList<String>();

		HSSFRow row = null;
		Object value = null;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			Map<String, Object> t = (Map<String, Object>) it.next();
			int i = 0;
			for (Map.Entry<String, Object> ent : t.entrySet()) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style2);
				value = ent.getValue();
				if (value instanceof Date) {
					Date date = (Date) value;
					SimpleDateFormat sdf = new SimpleDateFormat(pattern);
					cell.setCellValue(sdf.format(date));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				} else if (value instanceof Float || value.getClass().equals("float")) {
					BigDecimal bd = new BigDecimal(value.toString());
					bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
					HSSFRichTextString richString = new HSSFRichTextString(bd.floatValue() + "");
					cell.setCellValue(richString);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				} else if (value instanceof Double || value.getClass().equals("double")) {
					BigDecimal bd = new BigDecimal(value.toString());
					bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
					HSSFRichTextString richString = new HSSFRichTextString(bd.doubleValue() + "");
					cell.setCellValue(richString);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				} else if (value instanceof byte[]) {
					// 有图片时，设置行高为60px;
					row.setHeightInPoints(60);
					// 设置图片所在列宽度为100px,注意这里单位的一个换算
					sheet.setColumnWidth(i, (short) (35.7 * 100));
					// sheet.autoSizeColumn(i);
					byte[] bsValue = (byte[]) value;
					HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6, index);
					// AnchorType anchorType = new
					anchor.setAnchorType(AnchorType.DONT_MOVE_DO_RESIZE);
					// anchor.setAnchorType(2);
					patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
				} else {
					// 其它数据类型都当作字符串简单处理
					HSSFRichTextString richString = new HSSFRichTextString(value.toString());
					cell.setCellValue(richString);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				}
				i++;
			}

		}

		if ((null != headers || headers.length != 0)) {
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

	public static void exportDataToExcel(String title, String[] headers, Vector<Vector<String>> data, OutputStream out) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
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
		/*
		 * HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
		 * 0, 0, 0, (short) 4, 2, (short) 6, 5)); // 设置注释内容
		 * comment.setString(new HSSFRichTextString("可以在POI中添加注释！")); //
		 * 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容. comment.setAuthor("leno");
		 */

		// 遍历集合数据，产生数据行
		int index = 0;
		List<String> exportFieldTitle = new ArrayList<String>();

		HSSFRow row = null;
		for (Vector<String> unit : data) {
			row = sheet.createRow(++index);
			for(int i = 0;i<unit.size()-1;i++){
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style2);
				// 其它数据类型都当作字符串简单处理
				HSSFRichTextString richString = new HSSFRichTextString(unit.get(i+1));
				cell.setCellValue(richString);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			}
			

		}

		if ((null != headers || headers.length != 0)) {
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

	public void exportDailyExcel(String[] headers, Map<String,List<ResultPo>> dataset, OutputStream out) {
		exportDailyExcel("sheet 1", null, dataset, out, DATE_FORMAT_PATTERN);
	}
	/**
	 * 导出日报DATE_FORMAT_PATTERN
	 * @author 车斯剑
	 * @date 2016年10月23日上午9:52:17
	 * @param title
	 * @param headers
	 * @param dataset
	 * @param out
	 * @param pattern
	 */
	public void exportDailyExcel(String title, String[] headers, Map<String,List<ResultPo>> dailyData, OutputStream out, String pattern) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
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
		// 表头标题样式
		HSSFFont headfont = workbook.createFont();
        headfont.setFontName("宋体");
        headfont.setFontHeightInPoints((short) 22);// 字体大小
        HSSFCellStyle headstyle = workbook.createCellStyle();
        headstyle.setFont(headfont);
        headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
        headstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
        headstyle.setLocked(true);  

		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		
		List<ResultPo> salePowers = dailyData.get("送端电力出清结果");
		List<ResultPo> salePrices = dailyData.get("送端电价出清结果");// 四川送段电力出清集合
		List<ResultPo> saleEachPower = dailyData.get("送端电力出清结果详细");// 四川送段电价出清集合
		List<ResultPo> buyPowers = dailyData.get("受端电力出清结果");// 四川送江苏送端电力出清集合
		List<ResultPo> buyPrices = dailyData.get("受端电价出清结果");// 四川送江苏受端电力出清集合
		
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
        titleCell1.setCellValue("2016-09-20 星期二");
		// 遍历集合数据，产生数据行
		Iterator<T> it = (Iterator<T>) salePowers.iterator();
		int index = 4;
		List<String> exportFieldTitle = new ArrayList<String>();

		HSSFRow row = null;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			T t = (T) it.next();
			Field[] fields = t.getClass().getDeclaredFields();
			for (short i = 0; i < fields.length; i++) {
				Field field = fields[i];

				ExcelAnnotation ea = field.getAnnotation(ExcelAnnotation.class);
				// 若没有添加注解
				/*if (null == ea) {
					continue;
				}*/
				field.setAccessible(true);
				String fieldName = field.getName();
				//exportFieldTitle.add(fieldName);
				if (index == 5) {
					exportFieldTitle.add(fieldName);
				}
				String getMethodName = null;
				if (field.getType().equals(Boolean.class) || field.getType().toString().equals("boolean")) {
					getMethodName = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				} else {
					getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				}

				try {
					Class tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					if(value==null){
						value="";
					}

					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(style2);
					if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						cell.setCellValue(sdf.format(date));
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					} else if (value instanceof Float || value.getClass().equals("float")) {
						BigDecimal bd = new BigDecimal(value.toString());
						bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
						HSSFRichTextString richString = new HSSFRichTextString(bd.floatValue() + "");
						cell.setCellValue(richString);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					} else if (value instanceof Double || value.getClass().equals("double")) {
						BigDecimal bd = new BigDecimal(value.toString());
						bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
						HSSFRichTextString richString = new HSSFRichTextString(bd.doubleValue() + "");
						cell.setCellValue(richString);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					} else if (value instanceof byte[]) {
						// 有图片时，设置行高为60px;
						row.setHeightInPoints(60);
						// 设置图片所在列宽度为100px,注意这里单位的一个换算
						sheet.setColumnWidth(i, (short) (35.7 * 100));
						// sheet.autoSizeColumn(i);
						byte[] bsValue = (byte[]) value;
						HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6, index);
						// AnchorType anchorType = new
						anchor.setAnchorType(AnchorType.DONT_MOVE_DO_RESIZE);
						// anchor.setAnchorType(2);
						patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
					} else {
						// 其它数据类型都当作字符串简单处理
						HSSFRichTextString richString = new HSSFRichTextString(value.toString());
						cell.setCellValue(richString);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}

		if ((null == headers || headers.length == 0)) {
			if (exportFieldTitle.size() != 0) {
				// 产生表格标题行
				row = sheet.createRow(4);
				for (short i = 0; i < exportFieldTitle.size(); i++) {
					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(style);
					HSSFRichTextString text = new HSSFRichTextString(exportFieldTitle.get(i));
					cell.setCellValue(text);

					// 画表头斜线
					if (i == 0) {
						drawSlash(patriarch, i);
					}
				}
			}
		} else {
			// 产生表格标题行
			row = sheet.createRow(4);
			for (short i = 0; i < headers.length; i++) {
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style);
				HSSFRichTextString text = new HSSFRichTextString(headers[i]);
				cell.setCellValue(text);

				// 画表头斜线
				/*if (i == 0) {
					drawSlash(patriarch, i);
				}*/
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
	 * 
	 * @author 车斯剑
	 * @date 2016年10月23日下午10:39:54
	 * @param title
	 * @param headers
	 * @param dataset
	 * @param out
	 * @param pattern
	 */
	public void exportAreaExcel(String[] headers, List<AreaReportBean> dataset, OutputStream out) {
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
	
	public void exportExcel(String title, String[] headers, Collection<T> dataset, OutputStream out, String pattern) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
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
		/*
		 * HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
		 * 0, 0, 0, (short) 4, 2, (short) 6, 5)); // 设置注释内容
		 * comment.setString(new HSSFRichTextString("可以在POI中添加注释！")); //
		 * 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容. comment.setAuthor("leno");
		 */

		// 遍历集合数据，产生数据行
		Iterator<T> it = dataset.iterator();
		int index = 0;
		List<String> exportFieldTitle = new ArrayList<String>();

		HSSFRow row = null;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			T t = (T) it.next();
			Field[] fields = t.getClass().getDeclaredFields();
			for (short i = 0; i < fields.length; i++) {
				Field field = fields[i];

				field.setAccessible(true);
				String fieldName = field.getName();
				if (index == 1) {
					exportFieldTitle.add(fieldName);
				}
				String getMethodName = null;
				if (field.getType().equals(Boolean.class) || field.getType().toString().equals("boolean")) {
					getMethodName = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				} else {
					getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				}

				try {
					Class tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					if(value==null){
						value="";
					}

					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(style2);
					if (value instanceof Date) {
						Date date = (Date) value;
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						cell.setCellValue(sdf.format(date));
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					} else if (value instanceof Float || value.getClass().equals("float")) {
						BigDecimal bd = new BigDecimal(value.toString());
						bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
						HSSFRichTextString richString = new HSSFRichTextString(bd.floatValue() + "");
						cell.setCellValue(richString);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					} else if (value instanceof Double || value.getClass().equals("double")) {
						BigDecimal bd = new BigDecimal(value.toString());
						bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
						HSSFRichTextString richString = new HSSFRichTextString(bd.doubleValue() + "");
						cell.setCellValue(richString);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					} else if (value instanceof byte[]) {
						// 有图片时，设置行高为60px;
						row.setHeightInPoints(60);
						// 设置图片所在列宽度为100px,注意这里单位的一个换算
						sheet.setColumnWidth(i, (short) (35.7 * 100));
						// sheet.autoSizeColumn(i);
						byte[] bsValue = (byte[]) value;
						HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6, index);
						// AnchorType anchorType = new
						anchor.setAnchorType(AnchorType.DONT_MOVE_DO_RESIZE);
						// anchor.setAnchorType(2);
						patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
					} else {
						// 其它数据类型都当作字符串简单处理
						HSSFRichTextString richString = new HSSFRichTextString(value.toString());
						cell.setCellValue(richString);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}

		if ((null == headers || headers.length == 0)) {
			if (exportFieldTitle.size() != 0) {
				// 产生表格标题行
				row = sheet.createRow(0);
				for (short i = 0; i < exportFieldTitle.size(); i++) {
					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(style);
					HSSFRichTextString text = new HSSFRichTextString(exportFieldTitle.get(i));
					cell.setCellValue(text);

					// 画表头斜线
					if (i == 0) {
						drawSlash(patriarch, i);
					}
				}
			}
		} else {
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

	public static void main(String[] args) {
		List<Map<String, Object>> dataset = new ArrayList<Map<String, Object>>();
		String[] headers = { "学        号", "姓名", "性别", "年龄" };
		Map<String, Object> s1 = new HashMap<String, Object>();
		s1.put("学号", 1);
		s1.put("姓名", "张三");
		s1.put("性别", "男");
		s1.put("年龄", "11");
		Map<String, Object> s2 = new HashMap<String, Object>();
		s2.put("学号", 2);
		s2.put("姓名", "李四");
		s2.put("性别", "女");
		s2.put("年龄", "22");

		dataset.add(s1);
		dataset.add(s2);
		OutputStream out;
		try {
			out = new FileOutputStream("C://a.xls");
			ExportExcel.exportDataToExcel("学生表", headers, dataset, out, null);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * // 测试学生 ExportExcel<Student> ex = new ExportExcel<Student>();
		 * String[] headers = { "学        号", "姓名", "年龄", "性别", "出生日期" };
		 * List<Student> dataset = new ArrayList<Student>(); dataset.add(new
		 * Student(new Long(1), "张三", 20, true, new Date())); dataset.add(new
		 * Student(new Long(2), "李四", 24, false, new Date())); dataset.add(new
		 * Student(new Long(3), "王五", 22, true, new Date())); // 测试图书
		 * ExportExcel<Book> ex2 = new ExportExcel<Book>(); String[] headers2 =
		 * { "图书编号", "图书名称", "图书作者", "图书价格", "图书ISBN", "图书出版社", "封面图片" };
		 * List<Book> dataset2 = new ArrayList<Book>(); try {
		 * BufferedInputStream bis = new BufferedInputStream(new
		 * FileInputStream("C:/aaa.jpg")); byte[] buf = inputStreamToByte(bis);
		 * 
		 * dataset2.add(new Book(new Long(1), "jsp", "leno", 300.33f, "1234567",
		 * "清华出版社", buf)); dataset2.add(new Book(new Long(2), "java编程思想",
		 * "brucl", 300.33f, "1234567", "阳光出版社", buf)); dataset2.add(new
		 * Book(new Long(3), "DOM艺术", "lenotang", 300.33f, "1234567", "清华出版社",
		 * buf)); dataset2.add(new Book(new Long(4), "c++经典", "leno", 400.33f,
		 * "1234567", "清华出版社", buf)); dataset2.add(new Book(new Long(5), "c#入门",
		 * "leno", 300.33f, "1234567", "汤春秀出版社", buf));
		 * 
		 * OutputStream out = new FileOutputStream("C://a.xls"); OutputStream
		 * out2 = new FileOutputStream("C://b.xls"); ex.exportExcel(headers,
		 * dataset, out); ex2.exportExcel(headers2, dataset2, out2);
		 * out.close(); System.out.println("excel导出成功！"); } catch
		 * (FileNotFoundException e) { e.printStackTrace(); } catch (IOException
		 * e) { e.printStackTrace(); }
		 */
	}

}
