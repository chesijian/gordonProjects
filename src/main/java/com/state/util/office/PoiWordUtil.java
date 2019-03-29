package com.state.util.office;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
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
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import com.state.util.SessionUtil;
import com.state.util.properties.JPropertiesUtil;

public class PoiWordUtil {

	public static String regex = "\\$\\{(.*?)\\}";

	public static int PIC_WIDTH = 550;
	public static int PIC_HEIGHT = 400;
	
	
	/**
	 * 存放word的路径
	 */
	private static String SRCPATH;
	static {
		SRCPATH = JPropertiesUtil.getValue("plan.properties", "srcPath");
	}

	
	
	public static void main(String[] args) {
		System.out.println("-------------start--------------");

		try {

			Map<String, Object> data = new HashMap<String, Object>();//datas.get(0);
			// 买方数据
			List<Map<String, Object>> buyData = (List<Map<String, Object>>) data.get("buyData");
			// 卖方数据
			List<Map<String, Object>> saleData = (List<Map<String, Object>>) data.get("saleData");
			// 执行结果数据
			List<Map<String, Object>> dealData = (List<Map<String, Object>>) data.get("dealData");
			
			String area = String.valueOf(data.get("area"));  //地区
			String drole = String.valueOf(data.get("drole")); //买卖类型
			String number = String.valueOf(data.get("number")); //编号
			String date = String.valueOf(data.get("date"));   //日期
			String signPic = String.valueOf(data.get("signPic")); //签名

			Map<String, WordPic> picData = getPicData();
			XWPFParagraph contentP = null;
			XWPFParagraph contrastP = null;
			XWPFDocument doc = openDocument("d:/tradeModel.docx");
			List<XWPFParagraph> paragraphList = doc.getParagraphs();
			if (paragraphList != null && paragraphList.size() > 0) {
				for (XWPFParagraph paragraph : paragraphList) {
					List<XWPFRun> runs = paragraph.getRuns();
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(paragraph.getText());
					int startPosition = 0;
					while (matcher.find(startPosition)) {
						// String match = matcher.group();
						// startPosition = matcher.end();
						String key = matcher.group(1);
						String find = matcher.group(0);
						System.out.println(key+"===========88888======"+find);
						Object valueObj = "520";//mainData.get(key);
						String value = valueObj == null ? "" : String.valueOf(valueObj);
						if (key.startsWith("Pic_")) {
							WordPic pic = picData.get(key);
							if (pic != null) {

								CustomXWPFDocument cdoc = (CustomXWPFDocument) doc;
								replacePic(cdoc, paragraph, pic.getIn(), pic.getType(), pic.getWidth(), pic.getHeight());
								
								value = "";
							}

						}
						// 方案内容
						if (key.equals("content")) {
							contentP = paragraph;

						} else if (key.equals("contrast")) {
							// 对比数据
							contrastP = paragraph;

						}
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

			int pos = doc.getPosOfParagraph(contentP);
			XmlCursor cursor = doc.getDocument().getBody().getPArray(pos).newCursor();

			int index = 0;
			// 遍历方案
			Map<String, Object> scenarioAttr = null;
			XWPFParagraph newP = null;
			/*for (Entry<String, Map<String, Object>> scenario : scenarioData.entrySet()) {
				cursor = doc.getDocument().getBody().getPArray(pos).newCursor();
				newP = doc.insertNewParagraph(cursor);
				newP.setStyle("3");
				XWPFRun run = newP.createRun();
				run.setText("6.2." + (++index) + " " + scenario.getKey(), 0);
				scenarioAttr = scenario.getValue();

				cursor = doc.getDocument().getBody().getPArray(++pos).newCursor();
				newP = doc.insertNewParagraph(cursor);
				newP.setAlignment(ParagraphAlignment.LEFT);
				newP.setStyle("正文");
				run = newP.createRun();
				run.setText("    线路数" + scenarioAttr.get("线路数") + "条，线路总长度" + scenarioAttr.get("总长度") + "km，总负荷" + scenarioAttr.get("总负荷") + "KVA。", 0);

				// 插入线路型号
				Vector<Vector<Object>> translineTypeData = (Vector<Vector<Object>>) scenarioAttr.get("translineTypeData");
				if(translineTypeData != null && translineTypeData.size() > 0){
				cursor = doc.getDocument().getBody().getPArray(++pos).newCursor();
				newP = doc.insertNewParagraph(cursor);
				newP.setAlignment(ParagraphAlignment.LEFT);
				newP.setStyle("正文");
				newP.createRun().setText("线路型号：");
				insertTable(doc, newP, ++pos, translineTypeData, new String[] { "设备名", "数据", "实际计算" });
				}

				++pos;
			}

			
			List<String> headerList = new ArrayList<String>();

			// 对比母线电压指标
			Vector<Vector<Object>> busContrastData = (Vector<Vector<Object>>) contrastData.get("母线电压指标");
			if (busContrastData != null && busContrastData.size() > 0) {
				cursor = doc.getDocument().getBody().getPArray(pos).newCursor();
				newP = doc.insertNewParagraph(cursor);
				newP.setAlignment(ParagraphAlignment.LEFT);
				newP.setStyle("正文");
				newP.createRun().setText("母线电压指标比较：");
				headerList.clear();
				headerList.add("设备名");
				headerList.add("潮流电压(%)");
				headerList.addAll(scenarioList);
				insertTable(doc, newP, ++pos, busContrastData, headerList.toArray());
			}*/

			
			String filePath = "D:\\temp" + File.separator + "guodiao";
			//String filePath = SRCPATH + File.separator + projPath + File.separator + station;
			
			File fileP = new File(filePath);
			if (!fileP.exists()) {
				fileP.mkdirs();
			}
			String fileName = filePath + File.separator + "交易单测试" + ".docx";
			// System.out.println("----------"+fileName);
			File f = new File(fileName);

			FileOutputStream fopts = new FileOutputStream(f);
			doc.write(fopts);
			fopts.flush();
			fopts.close();
			doc.close();

			/*if (f.exists()) {
				String outFile = filePath + File.separator + projName + ".pdf";
				OfficeUtil.getOfficeUtilInstance().office2Pdf(fileName, outFile, ".docx");
			} else {
				throw new MessageException("无法生成word!");
			}*/

		} catch (java.io.IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} 
		System.out.println("-------------end--------------");

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

	/**
	 * 返回主数据
	 * 
	 * @author 大雄
	 * @Title getMainData
	 * @Date 2016-11-14
	 * @Description TODO
	 * @return
	 * @Return Map<String,String>
	 */
	public static Map<String, String> getMainData() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("Voltage", "0.4KV");
		data.put("lintType", "LGJ-25²/LGJ-35²");
		data.put("MaxVoltage", "380V");
		data.put("VoltagePercent", "96.88");
		data.put("Line", "关北台区");

		return data;
	}

	/**
	 * 替换图片
	 * 
	 * @author 大雄
	 * @Title replacePic
	 * @Date 2016-11-14
	 * @Description TODO
	 * @param doc
	 * @param paragraph
	 * @param in
	 * @param picType
	 * @param width
	 * @param height
	 * @throws InvalidFormatException
	 * @throws java.io.IOException
	 * @Return void
	 */
	public static void replacePic(CustomXWPFDocument doc, XWPFParagraph paragraph, InputStream in, int picType, int width, int height) throws InvalidFormatException, java.io.IOException {

		if (in != null) {
			//System.out.println("-----------22---------");
			// FileInputStream imageIns = new FileInputStream(pictureF);
			doc.addPictureData(in, picType);
			doc.createPicture(paragraph, doc.getAllPictures().size() - 1, width, height, "");
			if (in != null) {
				in.close();
			}
		}
	}

	/**
	 * 获取图片数据
	 * 
	 * @author 大雄
	 * @Title getPicData
	 * @Date 2016-11-14
	 * @Description TODO
	 * @return
	 * @throws FileNotFoundException
	 * @Return Map<String,WordPic>
	 */
	public static Map<String, WordPic> getPicData() throws FileNotFoundException {
		Map<String, WordPic> data = new HashMap<String, WordPic>();
		String templatePath =  SRCPATH + File.separator+"template"+File.separator;
		WordPic p1 = new WordPic("pic1-1.png", new FileInputStream(templatePath+"pic1-1.png"), XWPFDocument.PICTURE_TYPE_PNG, 550, 400);
		//WordPic p1 = new WordPic("基本方案.emf", new FileInputStream("c:/基本方案.emf"), XWPFDocument.PICTURE_TYPE_EMF, 550, 400);
		
		WordPic p2 = new WordPic("pic1-2.png", new FileInputStream(templatePath+"pic1-2.png"), XWPFDocument.PICTURE_TYPE_PNG, 550, 400);

		data.put("Pic_pic-1-1", p1);
		data.put("Pic_pic-1-2", p2);

		return data;
	}

	public static Vector<Vector<String>> getTableData() throws FileNotFoundException {
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		Vector<String> row1 = new Vector<String>();
		row1.add("1-1");
		row1.add("模型");
		row1.add("LGJ-25");
		data.add(row1);

		Vector<String> row2 = new Vector<String>();
		row2.add("1-2");
		row2.add("模型");
		row2.add("LGJ-35");
		data.add(row2);

		Vector<String> row3 = new Vector<String>();
		row3.add("1-3");
		row3.add("模型");
		row3.add("LGJ-50");
		data.add(row3);

		Vector<String> row4 = new Vector<String>();
		row4.add("1-3");
		row4.add("模型");
		row4.add("LGJ-120");
		data.add(row4);
		return data;
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
	public static void insertTable(XWPFDocument doc, XWPFParagraph para, int pos, Vector<Vector<Object>> data, Object[] titles) {
		/**/
		CTPPr pr = para.getCTP().getPPr() != null ? para.getCTP().getPPr() : para.getCTP().addNewPPr();
		CTSpacing spacing = pr.addNewSpacing();
		spacing.setLine(new BigInteger("360"));
		spacing.setLineRule(STLineSpacingRule.AUTO);
		para.setAlignment(ParagraphAlignment.LEFT);

		// int size =data.size();

		// XWPFTable xTable = doc.createTable(size+1, titles.length);
		// doc.insertTable(doc.getPosOfParagraph(para)+2, xTable);
		//XmlCursor cursor = doc.getDocument().getBody().getPArray(pos).newCursor();
		//XWPFTable xTable = doc.insertNewTbl(cursor);
		XWPFTable xTable = doc.createTable();
		//xTable.setStyleID("1");
		/**/
		CTTbl ttbl = xTable.getCTTbl();
		CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();
		CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();
		tblWidth.setW(new BigInteger("8400"));
		tblWidth.setType(STTblWidth.DXA);

		// xTable.getRow(0).setHeight(480);
		XWPFTableRow row = xTable.getRow(0);
		row.setHeight(480);
		XWPFTableCell cell = null;
		for (int m = 0; m < titles.length; m++) {
			if (m == 0) {
				cell = row.getCell(0);
			} else {
				cell = row.createCell();
			}
			XWPFParagraph xp = cell.addParagraph();
			xp.setVerticalAlignment(TextAlignment.CENTER);
			xp.setAlignment(ParagraphAlignment.CENTER);
			xp.setStyle("table");
			XWPFRun temp = xp.createRun();
			temp.setText(titles[m] == null ? "" : String.valueOf(titles[m]));
			// temp.setFontFamily(CONTENT_FONT_FAMILLY);
			// temp.setFontSize(CONTENT_FONT_SIZE);
			cell.removeParagraph(0);
			cell.setColor("CCCCCC");
			cell.setVerticalAlignment(XWPFVertAlign.CENTER);

		}

		int y = 1;
		for (Vector<Object> rowData : data) {
			row = xTable.createRow();
			row.setHeight(480);

			for (int j = 0; j < rowData.size(); j++) {
				cell = row.getCell(j);
				XWPFParagraph xp = cell.addParagraph();
				xp.setVerticalAlignment(TextAlignment.CENTER);
				xp.setAlignment(ParagraphAlignment.CENTER);
				xp.setStyle("table");
				XWPFRun temp = xp.createRun();
				temp.setText(rowData.get(j) == null ? "" : String.valueOf(rowData.get(j)));
				//temp.setFontFamily("仿宋");
				//temp.setFontSize(13);
				cell.removeParagraph(0);
				cell.setVerticalAlignment(XWPFVertAlign.CENTER);
			}
			y++;
		}
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
		String number = String.valueOf(data.get("number")); //编号
		String date = String.valueOf(data.get("date"));   //日期
		String signPic = String.valueOf(data.get("signPic")); //签名

		String path = String.valueOf(data.get("path"));
		String station = String.valueOf(data.get("station"));
		String projName = String.valueOf(data.get("projName"));
		String projPath = String.valueOf(data.get("projPath"));
		int level = Integer.parseInt(String.valueOf(data.get("level")));

		//设置word生成路径
		String filePath = "D:\\temp" + File.separator + "guodiao";
		
		filePath=filePath+File.separator + "交易单测试.docx";
		File fileP = new File(filePath);
		if (!fileP.exists()) {
			fileP.mkdirs();
		}
		
		Map<String, WordPic> picData = PoiWordUtil.getPicData();
		XWPFParagraph contentP = null;
		//XWPFDocument doc = PoiWordUtil.openDocument(tempPath + File.separator + "tradeModel.docx");
		XWPFDocument doc = openDocument("d:/tradeModel.docx");
		List<XWPFParagraph> paragraphList = doc.getParagraphs();
		
		if (paragraphList != null && paragraphList.size() > 0) {
			for (XWPFParagraph paragraph : paragraphList) {
				List<XWPFRun> runs = paragraph.getRuns();
				Pattern pattern = Pattern.compile(PoiWordUtil.regex);
				Matcher matcher = pattern.matcher(paragraph.getText());
				int startPosition = 0;
				while (matcher.find(startPosition)) {
					// String match = matcher.group();
					// startPosition = matcher.end();
					String key = matcher.group(1);
					String find = matcher.group(0);
					System.out.println(find + "=====replaceStr======" + key);
					Object valueObj = "520";//mainData.get(key);
					String value = valueObj == null ? "" : String.valueOf(valueObj);
					if (key.startsWith("Pic_")) {
						WordPic pic = picData.get(key);
						if (pic != null) {
							// System.out.println("----------22---------");

							CustomXWPFDocument cdoc = (CustomXWPFDocument) doc;
							replacePic(cdoc, paragraph, pic.getIn(), pic.getType(), pic.getWidth(), pic.getHeight());
							// startPosition = matcher.end();
							// continue;
							value = "";
						}

					}
					// 方案内容
					if (key.equals("content")) {
						contentP = paragraph;
						//contentStyle = contentP.getStyle();
						
					} 
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
		
		
		int pos = doc.getPosOfParagraph(contentP);
		//XmlCursor cursor = doc.getDocument().getBody().getPArray(pos).newCursor();
		doc.removeBodyElement(pos);
		int index = 0;
		// 遍历方案
		Map<String, Object> scenarioAttr = null;
		XWPFParagraph newP = null;
		for (String key : scenarioList) {
			// LoggerUtil.log("------------"+scenario.getKey(), 0);
			//cursor = doc.getDocument().getBody().getPArray(pos).newCursor();
			newP = doc.createParagraph();
			newP.setStyle("3");
			XWPFRun run = newP.createRun();
			run.setText("6.2." + (++index) + " " + key, 0);
			scenarioAttr = scenarioData.get(key);

			//cursor = doc.getDocument().getBody().getPArray(pos).newCursor();
			newP = getContent(doc);
			run = newP.createRun();
			run.setText("线路数" + scenarioAttr.get("线路数") + "条，线路总长度" + scenarioAttr.get("总长度") + "km，总负荷" + scenarioAttr.get("总负荷") + "KVA。", 0);

			//插入接线图
			
			String emfPath = filePath+File.separator+key+".emf";
			File emfFile = new File(emfPath);
			if(emfFile.exists()){
				CustomXWPFDocument cdoc = (CustomXWPFDocument) doc;
				newP = getContent(doc);
				WordPic picture = new WordPic(emfPath, new FileInputStream(emfPath), XWPFDocument.PICTURE_TYPE_EMF, PIC_WIDTH, PIC_HEIGHT);
				replacePic(cdoc, newP, picture.getIn(), picture.getType(), picture.getWidth(), picture.getHeight());
			}
				
			
			// 插入线路型号
			Vector<Vector<Object>> translineTypeData = (Vector<Vector<Object>>) scenarioAttr.get("translineTypeData");
			if (translineTypeData != null && translineTypeData.size() != 0) {
				newP = getContent(doc);
				newP.createRun().setText("线路型号：");
				// System.out.println(scenario.getKey()+"------------"+CommonUtil.objToJson(translineTypeData));
				PoiWordUtil.insertTable(doc, newP, ++pos, translineTypeData, new String[] { "设备名", "数据", key });
			}

			//获取低于93%的母线
			List<String> belowData = (List<String>)scenarioAttr.get("belowData");
			if(belowData != null && belowData.size()>0){
				newP = getContent(doc);
				newP.createRun().setText("低电压母线：(<93%)：");
				newP = getContent(doc);
				XWPFRun r = newP.createRun();
				
				StringBuilder sb = new StringBuilder();
				for(String str : belowData){
					sb.append(",").append(str);
				}
				sb.deleteCharAt(0);
				sb.append("。");
				r.setText(sb.toString());
			}
			// ++pos;
		}
		
		//cursor = doc.getDocument().getBody().getPArray(pos).newCursor();
		newP = doc.createParagraph();
		newP.setAlignment(ParagraphAlignment.LEFT);
		newP.setStyle("2");
		newP.createRun().setText("6.3方案对比");
		// 对比数据
		// pos = doc.getPosOfParagraph(contrastP);
		List<String> headerList = new ArrayList<String>();
		// 对比线路型号
		Vector<Vector<Object>> lineTypeContrastData = (Vector<Vector<Object>>) contrastData.get("线路型号");
		if (lineTypeContrastData != null && lineTypeContrastData.size() > 0) {
			newP = getContent(doc);
			newP.createRun().setText("线路型号比较：");
			headerList.clear();
			headerList.add("设备名");
			headerList.add("数据");
			headerList.addAll(scenarioList);
			PoiWordUtil.insertTable(doc, newP, ++pos, lineTypeContrastData, headerList.toArray());
		}		
		
		newP = doc.createParagraph();
		newP.setAlignment(ParagraphAlignment.LEFT);
		newP.setStyle("2");
		newP.createRun().setText("6.4结论");
		
		
		String fileName = filePath  + File.separator + "交易单测试" + ".docx";
		File f = new File(fileName);

		FileOutputStream fopts = new FileOutputStream(f);
		doc.write(fopts);
		fopts.flush();
		fopts.close();
		doc.close();

		/*if (f.exists()) {
			String outFile = filePath + File.separator + "交易单测试" + ".pdf";
			OfficeUtil.getOfficeUtilInstance().office2Pdf(fileName, outFile, ".docx");
		} else {
			throw new Exception("无法生成word!");
		}*/
	}

}

/**
 * word中图片类的属性
 * 
 * @author 大雄
 * @Description TODO
 * @Date 2016-11-14
 */
class WordPic {
	private String fileName;
	private InputStream in;
	private int type;

	private int width;
	private int height;

	public WordPic(String fileName, InputStream in, int type) {
		super();
		this.fileName = fileName;
		this.in = in;
		this.type = type;
	}

	public WordPic(String fileName, InputStream in, int type, int width, int height) {
		super();
		this.fileName = fileName;
		this.in = in;
		this.type = type;
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public InputStream getIn() {
		return in;
	}

	public void setIn(InputStream in) {
		this.in = in;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
