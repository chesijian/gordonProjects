package com.state.util.office;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.PositionInParagraph;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.TextSegement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import com.state.util.CommonUtil;
import com.state.util.SessionUtil;

public class MyWordUtil {

	public static String regex = "\\$\\{(.*?)\\}";
	
	public static void export(List<Map<String, Object>> datas) throws InvalidFormatException, IOException {
		
		String tempPath = SessionUtil.getCurrentRequest().getSession().getServletContext().getRealPath("/bat");
		Map<String, Object> data = datas.get(0);
		// 买方数据
		List<Map<String, Object>> buyData = (List<Map<String, Object>>) data.get("buyData");
		// 卖方数据
		List<Map<String, Object>> saleData = (List<Map<String, Object>>) data.get("saleData");
		// 执行结果数据
		List<Map<String, Object>> dealData = (List<Map<String, Object>>) data.get("dealData");
		
		String area = String.valueOf(data.get("area"));  //地区
		String drole = String.valueOf(data.get("drole")); //买卖类型
		System.out.println("buyData==="+CommonUtil.objToJson(buyData));
		Map<String, Object> tableDatas = new HashMap<String, Object>();
		if("buy".equals(drole)){
			tableDatas.put("title1", "购电方："+area);
			tableDatas.put("title2", "售电方");
			tableDatas.put("table1", buyData);
		}else{
			tableDatas.put("title1", "售电方："+area);
			tableDatas.put("title2", "购电方");
			tableDatas.put("table1", saleData);
		}

		//设置word生成路径
		String filePath = "D:\\temp" + File.separator + "guodiao";
		
		File fileP = new File(filePath);
		if (!fileP.exists()) {
			fileP.mkdirs();
		}
		XWPFParagraph contentP = null;
		//XWPFDocument doc = PoiWordUtil.openDocument(tempPath + File.separator + "tradeModel.docx");
		XWPFDocument doc = openDocument("D:\\tradeModel.docx");
		List<XWPFParagraph> paragraphList = doc.getParagraphs();
		
		if (paragraphList != null && paragraphList.size() > 0) {
			for (XWPFParagraph paragraph : paragraphList) {
				//System.out.println("paragraph.getText()===="+paragraph.getText());
				List<XWPFRun> runs = paragraph.getRuns();
				//System.out.println("MyWordUtil.regex===="+MyWordUtil.regex);
				Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
				Matcher matcher = pattern.matcher(paragraph.getText());
				
				int startPosition = 0;
				while (matcher.find(startPosition)) {
					String key = matcher.group(1);
					String find = matcher.group(0);
					//System.out.println(find + "=====replaceStr======" + key);
					Object valueObj = data.get(key);
					String value = valueObj == null ? "" : String.valueOf(valueObj);
					
					/*if (key.equals("content")) {
						contentP = paragraph;
						
					} */
					TextSegement found = paragraph.searchText(find, new PositionInParagraph());
					if (found != null) {
						// 判断查找内容是否在同一个Run标签中
						if (found.getBeginRun() == found.getEndRun()) {
							XWPFRun run = runs.get(found.getBeginRun());
							String runText = run.getText(run.getTextPosition());
							String replaced = runText.replace(find, value);
							run.setText(replaced, 0);
						} else {
							// 存在多个Run标签
							StringBuilder sb = new StringBuilder();
							for (int runPos = found.getBeginRun(); runPos <= found.getEndRun(); runPos++) {
								XWPFRun run = runs.get(runPos);
								sb.append(run.getText((run.getTextPosition())));
							}
							String connectedRuns = sb.toString();
							String replaced = connectedRuns.replace(find, value);
							XWPFRun firstRun = runs.get(found.getBeginRun());
							firstRun.setText(replaced, 0);
							// 删除后边的run标签
							for (int runPos = found.getBeginRun() + 1; runPos <= found.getEndRun(); runPos++) {
								// 清空其他标签内容
								XWPFRun partNext = runs.get(runPos);
								partNext.setText("", 0);
							}
						}
					}
					startPosition = matcher.end();
				}
			}
		}
		System.out.println("tableDatas==="+CommonUtil.objToJson(tableDatas));
		//MyWordUtil.replaceInTable(doc,tableDatas);
		String[] titles = {"省份","起止时段","成交电量（MWH）",""};
		XWPFParagraph newP = null;
		newP = getContent(doc);
		MyWordUtil.insertTable(doc, newP, null, new String[] { "购电方：安徽"});
		MyWordUtil.insertTable(doc, newP, (List<Map<String, Object>>)tableDatas.get("table1"), titles);
		MyWordUtil.insertTable(doc, newP, null, new String[] { "售电方：四川"});
		MyWordUtil.insertTable(doc, newP, (List<Map<String, Object>>)tableDatas.get("table1"), titles);
		MyWordUtil.insertTable(doc, newP, null, new String[] { "执行结果"});
		MyWordUtil.insertTable(doc, newP, (List<Map<String, Object>>)tableDatas.get("table1"), titles);
		String fileName = filePath  + File.separator + "交易单测试" + ".docx";
		File f = new File(fileName);
		FileOutputStream fopts = new FileOutputStream(f);
		doc.write(fopts);
		fopts.flush();
		fopts.close();
		doc.close();
	}
	
	/**
	 * 获取正文段落
	 * @author 大雄
	 * @Title getContent
	 * @Date 2016-11-17
	 * @Description TODO
	 * @param doc
	 * @return
	 * @Return XWPFParagraph
	 */
	public static XWPFParagraph getContent(XWPFDocument doc){
		XWPFParagraph newP = doc.createParagraph();
		newP.setAlignment(ParagraphAlignment.LEFT);
		newP.setStyle("正文");
		return newP;
	}
	
	

	/**
	 * 插入表格
	 * 
	 * @author 车斯剑
	 * @date 2016年10月17日下午10:13:54
	 * @param doc
	 * @param para
	 * @param saleData
	 * @param titles
	 */
	public static void insertTable(XWPFDocument doc, XWPFParagraph para, List<Map<String, Object>> data, Object[] titles) {
		/**/
		CTPPr pr = para.getCTP().getPPr() != null ? para.getCTP().getPPr() : para.getCTP().addNewPPr();
		CTSpacing spacing = pr.addNewSpacing();
		//spacing.setLine(new BigInteger("360"));
		spacing.setLine(new BigInteger("0"));
		spacing.setLineRule(STLineSpacingRule.AUTO);
		para.setAlignment(ParagraphAlignment.LEFT);

		XWPFTable xTable = doc.createTable();
		xTable.setStyleID("1");
		//表格边框
		if(data!=null){
			CTTblBorders borders=xTable.getCTTbl().getTblPr().addNewTblBorders();

			CTBorder hBorder=borders.addNewInsideH();
			hBorder.setVal(STBorder.Enum.forString("none"));
			hBorder.setSz(BigInteger.ONE);
			hBorder.setColor("FFFFFF");
			
			CTBorder vBorder=borders.addNewInsideV();
			vBorder.setVal(STBorder.Enum.forString("none"));
			vBorder.setSz(BigInteger.ONE);
			vBorder.setColor("FFFFFF");
			
			/*CTBorder lBorder=borders.addNewLeft();
			lBorder.setVal(STBorder.Enum.forString("none"));
			lBorder.setSz(new BigInteger("1"));
			lBorder.setColor("3399FF");
			
			CTBorder rBorder=borders.addNewRight();
			rBorder.setVal(STBorder.Enum.forString("none"));
			rBorder.setSz(new BigInteger("1"));
			rBorder.setColor("F2B11F");*/
			
			/*CTBorder tBorder=borders.addNewTop();
			tBorder.setVal(STBorder.Enum.forString("none"));
			tBorder.setSz(new BigInteger("1"));
			tBorder.setColor("C3599D");*/
			
			/*CTBorder bBorder=borders.addNewBottom();
			bBorder.setVal(STBorder.Enum.forString("none"));
			bBorder.setSz(new BigInteger("1"));
			bBorder.setColor("F7E415");*/
		}
		
		/**/
		CTTbl ttbl = xTable.getCTTbl();
		CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();
		CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();
		tblWidth.setW(new BigInteger("8600"));
		tblWidth.setType(STTblWidth.DXA);

		XWPFTableRow row = xTable.getRow(0);
		row.setHeight(500);
		XWPFTableCell cell = null;
		for (int m = 0; m < titles.length; m++) {
			if (m == 0) {
				cell = row.getCell(0);
			} else {
				cell = row.createCell();
			}
			XWPFParagraph xp = cell.addParagraph();
			xp.setVerticalAlignment(TextAlignment.CENTER);
			xp.setAlignment(ParagraphAlignment.LEFT);
			xp.setStyle("table");
			XWPFRun temp = xp.createRun();
			temp.setText(titles[m] == null ? "" : String.valueOf(titles[m]));
			temp.setFontFamily("仿宋");
			temp.setFontSize(14);
			cell.removeParagraph(0);
			//cell.setColor("CCCCCC");
			cell.setVerticalAlignment(XWPFVertAlign.CENTER);

		}
		if(data!=null){
			for (Map<String, Object> rowData : data) {
				row = xTable.createRow();
				row.setHeight(480);
				for (int j = 0; j < titles.length; j++) {
					cell = row.getCell(j);
					XWPFParagraph xp = cell.addParagraph();
					xp.setVerticalAlignment(TextAlignment.CENTER);
					xp.setAlignment(ParagraphAlignment.LEFT);
					xp.setStyle("table");
					XWPFRun temp = xp.createRun();
					if(j==0){
						temp.setText(String.valueOf(rowData.get("province")));
					}else if(j==1){
						temp.setText(String.valueOf(rowData.get("time")));
					}else if(j==2){
						temp.setText(String.valueOf(rowData.get("clearElectricity")));
					}else{
						temp.setText("");
					}
					temp.setFontFamily("仿宋");
					temp.setFontSize(14);
					cell.removeParagraph(0);
					cell.setVerticalAlignment(XWPFVertAlign.CENTER);
				}
			}
		}
		
	}

	public static XWPFDocument openDocument(String filePath) {
		XWPFDocument xdoc = null;
		InputStream is = null;
		try {
			is = new FileInputStream(filePath);
			xdoc = new CustomXWPFDocument(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return xdoc;
	}

	public static List<Map<String, Object>> getTestData() {
		List<Map<String, Object>> datas = new ArrayList<Map<String,Object>>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("area", "安徽");
		data.put("drole", "buy");
		data.put("number", "2016-00901");
		data.put("date", "2016年12月23日");
		
		List<Map<String, Object>> buyData = new ArrayList<Map<String,Object>>();
		Map<String, Object> buys = new HashMap<String, Object>();
		buys.put("province", "安徽");
		buys.put("time", "09:00-12:00");
		buys.put("clearElectricity", "230");
		buyData.add(buys);
		data.put("buyData", buyData);
		
		List<Map<String, Object>> saleData = new ArrayList<Map<String,Object>>();
		Map<String, Object> sales = new HashMap<String, Object>();
		sales.put("province", "甘肃");
		sales.put("time", "09:00-12:00");
		sales.put("clearElectricity", "230");
		saleData.add(sales);
		data.put("buyData", saleData);
		
		datas.add(data);
		return datas;
	}
	
	
}
