package com.state.util.office;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableCell.XWPFVertAlign;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.state.enums.Enums_DeclareType;
import com.state.exception.MsgException;
import com.state.po.DeclarePo;
import com.state.po.ResultPo;
import com.state.po.TransTielinePo;
import com.state.po.UserPo;
import com.state.po.WeekDataPo;
import com.state.util.CommonUtil;
import com.state.util.DeclareUtil;
import com.state.util.FileManager;
import com.state.util.IssueUtil;
import com.state.util.LoggerUtil;
import com.state.util.SystemTools;
import com.state.util.sys.SystemConstUtil;

public class WordUtil {

	private static final int TITLE_FONT_SIZE = 22;

	private static final String TITLE_FONT_FAMILLY = "黑体";
	private static final int SUBTITLE_FONT_SIZE = 20;
	private static final String CONTENT_FONT_FAMILLY = "仿宋";
	private static final int CONTENT_FONT_SIZE = 13;

	//签名图片长度
	public static final int SIGN_PIC_WIDTH = 100;
	//签名图片高度
	public static final int SIGN_PIC_HEIGHT = 50;
	
	/**
	 * 插入表格
	 * @author 车斯剑
	 * @date 2016年10月17日下午10:13:54
	 * @param doc
	 * @param para
	 * @param saleData
	 * @param titles
	 */
	public static void insertTable(XWPFDocument doc, XWPFParagraph para, Map<String,Map<String,Double>> saleData,String[] titles){
		CTPPr pr = para.getCTP().getPPr() != null ? para.getCTP().getPPr() : para.getCTP().addNewPPr();  
        CTSpacing spacing =pr.addNewSpacing();  
        spacing.setLine(new BigInteger("360"));  
        spacing.setLineRule(STLineSpacingRule.AUTO); 
	    para.setAlignment(ParagraphAlignment.CENTER);  
	    int size =saleData.size();
	    XWPFTable xTable = doc.createTable(size+1, titles.length);
	    CTTbl ttbl = xTable.getCTTbl();  
        CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();  
        CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();  
        tblWidth.setW(new BigInteger("8400"));  
        tblWidth.setType(STTblWidth.DXA);  
       
        xTable.getRow(0).setHeight(480); 
        
        for(int m =0;m<titles.length;m++){
        	XWPFTableCell cell = xTable.getRow(0).getCell(m);
        	XWPFParagraph xp = cell.addParagraph();
	        xp.setVerticalAlignment(TextAlignment.CENTER);
	        xp.setAlignment(ParagraphAlignment.CENTER);
	        XWPFRun temp = xp.createRun();
	        temp.setText(titles[m]);  
	        temp.setFontFamily(CONTENT_FONT_FAMILLY);
	        temp.setFontSize(CONTENT_FONT_SIZE);
	        cell.removeParagraph(0);
	        cell.setColor("CCCCCC");  
	        cell.setVerticalAlignment(XWPFVertAlign.CENTER);  
	       
        }
        int y=1;
        for (Entry<String,Map<String,Double>> entry : saleData.entrySet()) {
        	xTable.getRow(y).setHeight(480); 
        	Map<String, Double> value = entry.getValue();
        	for (int j = 0; j < titles.length; j++) { 
        		XWPFTableCell cell = xTable.getRow(y).getCell(j);
            	XWPFParagraph xp = cell.addParagraph();
		        xp.setVerticalAlignment(TextAlignment.CENTER);
		        xp.setAlignment(ParagraphAlignment.CENTER);
		        XWPFRun temp = xp.createRun();
		        if(j==0){
		        	temp.setText(entry.getKey()); 
		        }else if(j==1){
		        	temp.setText(CommonUtil.toFix(value.get("power"))); 
		        }else if(j==2){
		        	temp.setText(CommonUtil.toFix(value.get("price"))); 
		        }else{
		        	temp.setText(CommonUtil.toFix(value.get("quantity"))); 
		        }
		        temp.setFontFamily("仿宋");
		        temp.setFontSize(13);
		        cell.removeParagraph(0);
		        cell.setVerticalAlignment(XWPFVertAlign.CENTER);
        	}
        	y++;
		}
	}
	/**
	 * 生成周报临时文件2
	 * @author 车斯剑
	 * @date 2016年10月20日下午8:53:27
	 * @param weekData
	 */
	public static void exportWeekReportByWeekData(String tempPath, String fileName,WeekDataPo weekData) {
		FileInputStream is = null;
		POIFSFileSystem pfs = null;
		OutputStream os = null;
		
		try {
			
			XWPFDocument doc = new XWPFDocument();
			// 创建一个段落
			XWPFParagraph para = doc.createParagraph();
			para.setAlignment(ParagraphAlignment.CENTER);
			// 一个XWPFRun代表具有相同属性的一个区域
			// 设置标题
			XWPFRun run = para.createRun();
			run.setBold(true);
			// run.setSubscript(VerticalAlign.);
			run.setFontFamily(TITLE_FONT_FAMILLY);
			run.setFontSize(TITLE_FONT_SIZE);
			run.setText(CommonUtil.getSimpleDate().substring(0,4)+"年富余可再生能源跨省区现货市场");
			
			para = doc.createParagraph();
			para.setAlignment(ParagraphAlignment.CENTER);
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(TITLE_FONT_FAMILLY);
			run.setFontSize(SUBTITLE_FONT_SIZE);
			run.setText("运行情况第"+weekData.getWeekNum()+"周周报");
			para = doc.createParagraph();
			para.setAlignment(ParagraphAlignment.CENTER);
			run = para.createRun();
			//run.setBold(true);
			run.setFontFamily(TITLE_FONT_FAMILLY);
			run.setFontSize(SUBTITLE_FONT_SIZE);
			run.setText("("+weekData.getStartTime()+"—"+weekData.getEndTime()+")");

			// 设置内容content
			para = doc.createParagraph();
			para.setSpacingBefore(0);
			para.setSpacingAfter(0);
			para.setSpacingAfterLines(0);
			para.setSpacingBeforeLines(0);
			// para.setAlignment(ParagraphAlignment.CENTER);
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("一、跨区现货市场总体运行情况 ");
			para = doc.createParagraph();
			run = para.createRun();
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			//run.setText("第周，跨区现货市场启动运行天数7天，跨区通道累计可用电量"+ablePower+"MWh，买方累计交易申报电量"+buyPower+"MWh，卖方累计交易申报电量"+salePower+"MWh，累计交易成交电量"+dealPower+"MWh，累计交易执行电量?MWh（模拟运行），跨区通道平均利用率?%。各跨区通道利用率如下图所示：");
			run.setText("第"+weekData.getWeekNum()+"周，跨区现货市场启动运行天数"+weekData.getDays()+"天，跨区通道累计可用电量"+weekData.getAblePower()+"MWh，买方累计交易申报电量"+weekData.getBuySumElectricity()+"MWh，卖方累计交易申报电量"+weekData.getSaleSumElectricity()+"MWh，累计交易成交电量"+weekData.getDealPower()+"MWh，累计交易执行电量"+weekData.getExecutePower()+"MWh（模拟运行），跨区通道平均利用率"+CommonUtil.toFix(weekData.getAlitAvg())+"%。各跨区通道利用率如下图所示：");
			
			// 插入图片
			para = doc.createParagraph();
			para.setAlignment(ParagraphAlignment.RIGHT);
			run = para.createRun();
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("${channelImage}");
			
			//二、跨区现货市场交易报价情况
			para = doc.createParagraph();
			// para.setAlignment(ParagraphAlignment.CENTER);
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("二、跨区现货市场交易报价情况");

			para = doc.createParagraph();
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("1. 送端交易报价情况");
			para = doc.createParagraph();
			run = para.createRun();
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("第"+weekData.getWeekNum()+"周，累计受理送端交易单"+weekData.getSaleSum()+"单，平均申报电力"+CommonUtil.toFix(weekData.getSaleAvgPower())+"MW，平均申报电价 "+CommonUtil.toFix(weekData.getSaleAvgPrice())+"元/MWh。各送端交易成员报价情况如下表所示：");
			
			//插入卖方电量、电价表格
			String[] titleArr = {"省份","平均电力（MW） ","平均电价（元/MWh） "};
			//insertTable(doc,para,saleData,titleArr);
			Map<String, Map<String, Double>>  saleTable = JSON.parseObject(weekData.getSaleTable(), new TypeReference<Map<String, Map<String, Double>>>(){});
			insertTable(doc,para,saleTable,titleArr);
			
	        //2.受端交易报价情况
	        para = doc.createParagraph();
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("2.受端交易报价情况");
			para = doc.createParagraph();
			run = para.createRun();
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("第"+weekData.getWeekNum()+"周，累计受理受端交易单"+weekData.getBuySum()+"单，平均申报电力"+CommonUtil.toFix(weekData.getBuyAvgPower())+"MW，平均申报电价 "+CommonUtil.toFix(weekData.getBuyAvgPrice())+"元/MWh。各送端交易成员报价情况如下表所示：");
			
			//插入买方电量、电价表格
			Map<String, Map<String, Double>>  buyTable = JSON.parseObject(weekData.getBuyTable(), new TypeReference<Map<String, Map<String, Double>>>(){});
			insertTable(doc,para,buyTable,titleArr);
	        //-----------------------------------------------------------------------
			para = doc.createParagraph();
			para.setSpacingBefore(380);
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("三、跨区现货市场交易成交情况");
			
			para = doc.createParagraph();
			para.setAlignment(ParagraphAlignment.LEFT);
			run = para.createRun();
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			
			run.setText("第"+weekData.getWeekNum()+"周，累计成交电量"+CommonUtil.toFix(weekData.getDealPower())+"MWh，平均出清电价 "+CommonUtil.toFix(weekData.getDealPrice())+"元/MWh，各交易通道详细成交情况如下表所示：");
			
			//插入交易通道详细成交情况表格
			String[] dealTitleArr = {"区域","平均出清电力（MW） ","平均出清电价 （元/MWh）","累计成交电量（MWh） "};
			Map<String, Map<String, Double>>  dealTable = JSON.parseObject(weekData.getDealTable(), new TypeReference<Map<String, Map<String, Double>>>(){});
			insertTable(doc,para,dealTable,dealTitleArr);
	        
	        //4=========================================
	        para = doc.createParagraph();
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("四、跨区现货市场交易执行情况");

			para = doc.createParagraph();
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("1. 交易执行总体情况");
			para = doc.createParagraph();
			run = para.createRun();
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("第"+weekData.getWeekNum()+"周，累计执行交易电量"+weekData.getExecutePower()+"MWh，交易执行完成率0%，原因是模拟运行，未实际投运。各通道执行情况如表所示：");
			
			//插入各通道执行情况表格
			String[] titleArr4 = {"区域","累计成交电量（MWh） ","累计执行电量（MWh）"};
			Map<String, Map<String, Double>>  executeTable = JSON.parseObject(weekData.getBiasTable(), new TypeReference<Map<String, Map<String, Double>>>(){});
			insertTable(doc,para,executeTable,titleArr4);
	        
	        //2.执行情况偏差分析
	        para = doc.createParagraph();
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("2.执行情况偏差分析");
			
			//插入偏差分析表格
			//String[] titleArr5 = {"区域","偏差量（MWh） ","偏差原因 "};
			Map<String, Map<String, Double>>  abisTable = JSON.parseObject(weekData.getExecuteTable(), new TypeReference<Map<String, Map<String, Double>>>(){});
			//insertTable(doc,para,executeTable,titleArr4);
			
			CTPPr pr = para.getCTP().getPPr() != null ? para.getCTP().getPPr() : para.getCTP().addNewPPr();  
	        CTSpacing spacing =pr.addNewSpacing();  
	        spacing.setLine(new BigInteger("360"));  
	        spacing.setLineRule(STLineSpacingRule.AUTO); 
		    para.setAlignment(ParagraphAlignment.CENTER);  
		    int size =abisTable.size();
		    XWPFTable carryTable = doc.createTable(size+1, 3);
		    CTTbl ttbl = carryTable.getCTTbl();  
	        CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();  
	        CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();  
	        tblWidth.setW(new BigInteger("8400"));  
	        tblWidth.setType(STTblWidth.DXA);  
	        
	        carryTable.getRow(0).setHeight(480); 
	        String[] titleArr5 = {"区域","偏差量（MWh） ","偏差原因 "};
	        for(int m =0;m<3;m++){
	        	XWPFTableCell cell = carryTable.getRow(0).getCell(m);
	        	XWPFParagraph xp = cell.addParagraph();
		        xp.setVerticalAlignment(TextAlignment.CENTER);
		        xp.setAlignment(ParagraphAlignment.CENTER);
		        XWPFRun temp = xp.createRun();
		        temp.setText(titleArr5[m]);  
		        temp.setFontFamily(CONTENT_FONT_FAMILLY);
		        temp.setFontSize(CONTENT_FONT_SIZE);
		        cell.removeParagraph(0);
		        cell.setColor("CCCCCC");  
		        cell.setVerticalAlignment(XWPFVertAlign.CENTER);  
		       
	        }
	        int b=1;
	        for (Entry<String,Map<String,Double>> entry : abisTable.entrySet()) {
	        	carryTable.getRow(b).setHeight(480); 
	        	for (int j = 0; j < 3; j++) { 
	        		XWPFTableCell cell = carryTable.getRow(b).getCell(j);
	            	XWPFParagraph xp = cell.addParagraph();
			        xp.setVerticalAlignment(TextAlignment.CENTER);
			        xp.setAlignment(ParagraphAlignment.CENTER);
			        XWPFRun temp = xp.createRun();
			        if(j==0){
			        	temp.setText(entry.getKey()); 
			        }else if(j==1){
			        	temp.setText(String.valueOf(entry.getValue().get("power"))); 
			        }else{
			        	temp.setText("模拟运行，未执行"); 
			        }
			        temp.setFontFamily(CONTENT_FONT_FAMILLY);
			        temp.setFontSize(CONTENT_FONT_SIZE);
			        cell.removeParagraph(0);
			        cell.setVerticalAlignment(XWPFVertAlign.CENTER);
	        	}
	        	b++;
			}
	        //end====================================
	        
	        String tempFileNamePath = tempPath + File.separator + fileName;
	        String _tempFileNamePath = tempPath + File.separator +"_"+ fileName;

			File file = new File(_tempFileNamePath);
			FileOutputStream fos = new FileOutputStream(file);
			doc.write(fos);
			// hwpf.write(fos);
			fos.flush();
			doc.close();
			/** 打开word2007的文件设置图片 */
			OPCPackage opc = POIXMLDocument.openPackage(_tempFileNamePath);
			CustomXWPFDocument document = new CustomXWPFDocument(opc);
			
			// 处理段落
			XWPFParagraph channelParagraph = null;
			List<XWPFParagraph> paragraphList = document.getParagraphs();
			if (paragraphList != null && paragraphList.size() > 0) {
				for (XWPFParagraph paragraph : paragraphList) {
					List<XWPFRun> runs = paragraph.getRuns();
					for (XWPFRun r : runs) {
						String text = r.getText(0);
						if (text != null) {
							if (text.indexOf("${channelImage}") > -1) {
								r.setText("", 0);
								channelParagraph = paragraph;
							}
						}
					}
				}
			}
			String iconPath = SystemConstUtil.getTempPath() + File.separator + "channelRatio.png";
			if(channelParagraph != null){
				int picType = document.PICTURE_TYPE_PNG;
				
				File pictureF = new File(iconPath);
				if(pictureF.exists()){
					FileInputStream imageIns = new FileInputStream(pictureF);
					document.addPictureData(imageIns, picType);
					document.createPicture(channelParagraph, document.getAllPictures().size() - 1, 600, 300, "");
					if (imageIns != null) {
						imageIns.close();
					}
				}
			}
			FileOutputStream fopts = new FileOutputStream(tempFileNamePath);
			document.write(fopts);
			fopts.flush();
			fopts.close();
			document.close();
			
			FileManager.delete(_tempFileNamePath);
			FileManager.delete(iconPath);
			System.out.println("---------成功--------");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (pfs != null) {
					pfs.close();
				}
				if (os != null) {
					os.close();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 生成周报临时文件
	 * @author 车斯剑
	 * @date 2016年10月18日上午10:18:00
	 * @param tempPath
	 * @param fileName
	 * @param dealData
	 * @param data 
	 */
	public static void exportWeekReport(String tempPath, String fileName, Map<String,Object> dealData, Map<String, Object> declareData,
			Map<String,Map<String,Double>> carryData) {
		FileInputStream is = null;
		POIFSFileSystem pfs = null;
		OutputStream os = null;
		
		Map<String,Map<String,Double>> buyDealData = (Map<String, Map<String, Double>>) dealData.get("DealData");
		
		double buyExecutePower =0; //累计执行电量
		if(carryData != null){
			for(Entry<String,Map<String,Double>> entry : carryData.entrySet()){
				Map<String, Double> value = entry.getValue();
				buyExecutePower += value.get("price");
			}
		}
		
		double ablePower = (Double) dealData.get("dataAble"); //可用容量
		double dealPower = (Double) dealData.get("dealPower"); //交易成交电量
		double dealPrice = (Double) dealData.get("dealPrice"); //出清电价
		int days = (Integer) dealData.get("days");
		int week = (Integer) dealData.get("week");
		double alitAvg = (Double) dealData.get("alitAvg"); 
		
		Map<String, Object> paramMap = new HashMap<String, Object>();//全部数据集合
		paramMap.put("buyPower", CommonUtil.toFix(declareData.get("buySumElectricity")));  //买方累计交易申报电量
		paramMap.put("salePower", CommonUtil.toFix(declareData.get("saleSumElectricity")));  //卖方累计交易申报电量
		paramMap.put("dealPower", dealData.get("dealPower"));
		paramMap.put("salePowerAvg", CommonUtil.toFix(declareData.get("saleAvgPower")));
		paramMap.put("salePriceAvg", CommonUtil.toFix(declareData.get("saleAvgPrice")));
		paramMap.put("buyProwerAvg", CommonUtil.toFix(declareData.get("buyAvgPower")));
		paramMap.put("buyPriceAvg", CommonUtil.toFix(declareData.get("buyAvgPrice")));
		
		try {
			
			XWPFDocument doc = new XWPFDocument();
			// 创建一个段落
			XWPFParagraph para = doc.createParagraph();
			para.setAlignment(ParagraphAlignment.CENTER);
			// 一个XWPFRun代表具有相同属性的一个区域
			// 设置标题
			XWPFRun run = para.createRun();
			run.setBold(true);
			// run.setSubscript(VerticalAlign.);
			run.setFontFamily(TITLE_FONT_FAMILLY);
			run.setFontSize(TITLE_FONT_SIZE);
			run.setText(CommonUtil.getSimpleDate().substring(0,4)+"年富余可再生能源跨省区现货市场");
			
			// run.setColor("FF0000");
			para = doc.createParagraph();
			para.setAlignment(ParagraphAlignment.CENTER);
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(TITLE_FONT_FAMILLY);
			run.setFontSize(SUBTITLE_FONT_SIZE);
			run.setText("运行情况第"+week+"周周报");

			// 设置内容content
			para = doc.createParagraph();
			para.setSpacingBefore(0);
			para.setSpacingAfter(0);
			para.setSpacingAfterLines(0);
			para.setSpacingBeforeLines(0);
			// para.setAlignment(ParagraphAlignment.CENTER);
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("一、跨区现货市场总体运行情况 ");
			para = doc.createParagraph();
			run = para.createRun();
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			//run.setText("第周，跨区现货市场启动运行天数7天，跨区通道累计可用电量"+ablePower+"MWh，买方累计交易申报电量"+buyPower+"MWh，卖方累计交易申报电量"+salePower+"MWh，累计交易成交电量"+dealPower+"MWh，累计交易执行电量?MWh（模拟运行），跨区通道平均利用率?%。各跨区通道利用率如下图所示：");
			run.setText("第"+week+"周，跨区现货市场启动运行天数"+days+"天，跨区通道累计可用电量"+ablePower+"MWh，买方累计交易申报电量"+paramMap.get("buyPower")+"MWh，卖方累计交易申报电量"+paramMap.get("salePower")+"MWh，累计交易成交电量"+dealPower+"MWh，累计交易执行电量"+buyExecutePower+"MWh（模拟运行），跨区通道平均利用率"+CommonUtil.toFix(alitAvg)+"%。各跨区通道利用率如下图所示：");
			
			// 插入图片
			para = doc.createParagraph();
			para.setAlignment(ParagraphAlignment.RIGHT);
			run = para.createRun();
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("${channelImage}");
			
			//二、跨区现货市场交易报价情况
			para = doc.createParagraph();
			// para.setAlignment(ParagraphAlignment.CENTER);
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("二、跨区现货市场交易报价情况");

			para = doc.createParagraph();
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("1. 送端交易报价情况");
			para = doc.createParagraph();
			run = para.createRun();
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("第"+week+"周，累计受理送端交易单"+declareData.get("saleSum")+"单，平均申报电力"+paramMap.get("salePowerAvg")+"MW，平均申报电价 "+paramMap.get("salePriceAvg")+"元/MWh。各送端交易成员报价情况如下表所示：");
			
			//插入卖方电量、电价表格
			String[] titleArr = {"省份","平均电力 ","平均电价 "};
			//insertTable(doc,para,saleData,titleArr);
			insertTable(doc,para,(Map<String, Map<String, Double>>)declareData.get("saleAvgMap"),titleArr);
	        //2.受端交易报价情况
	        para = doc.createParagraph();
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("2.受端交易报价情况");
			para = doc.createParagraph();
			run = para.createRun();
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("第"+week+"周，累计受理受端交易单"+declareData.get("buySum")+"单，平均申报电力"+paramMap.get("buyProwerAvg")+"MW，平均申报电价 "+paramMap.get("buyPriceAvg")+"元/MWh。各送端交易成员报价情况如下表所示：");
			
			//插入买方电量、电价表格
			insertTable(doc,para,(Map<String, Map<String, Double>>)declareData.get("buyAvgMap"),titleArr);
	        //-----------------------------------------------------------------------
			para = doc.createParagraph();
			para.setSpacingBefore(380);
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("三、跨区现货市场交易成交情况");
			
			para = doc.createParagraph();
			para.setAlignment(ParagraphAlignment.LEFT);
			run = para.createRun();
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			
			run.setText("第"+week+"周，累计成交电量"+CommonUtil.toFix(dealData.get("dealPower"))+"MWh，平均出清电价 "+CommonUtil.toFix(dealPrice)+"元/MWh，各交易通道详细成交情况如下表所示：");
			
			//插入交易通道详细成交情况表格
			String[] dealTitleArr = {"区域","平均出清电力 ","平均出清电价 ","累计成交电量 "};
			insertTable(doc,para,buyDealData,dealTitleArr);
	        
	        //4=========================================
	        para = doc.createParagraph();
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("四、跨区现货市场交易执行情况");

			para = doc.createParagraph();
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("1. 交易执行总体情况");
			para = doc.createParagraph();
			run = para.createRun();
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("第"+week+"周，累计执行交易电量"+buyExecutePower+"MWh，交易执行完成率0%，原因是模拟运行，未实际投运。各通道执行情况如表所示：");
			
			//插入各通道执行情况表格
			String[] titleArr4 = {"区域","累计成交电量（MWh） ","累计执行电量（MWh）"};
			insertTable(doc,para,carryData,titleArr4);
	        
	        //2.执行情况偏差分析
	        para = doc.createParagraph();
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("2.执行情况偏差分析");
			
			//插入偏差分析表格
			CTPPr pr = para.getCTP().getPPr() != null ? para.getCTP().getPPr() : para.getCTP().addNewPPr();  
	        CTSpacing spacing =pr.addNewSpacing();  
	        spacing.setLine(new BigInteger("360"));  
	        spacing.setLineRule(STLineSpacingRule.AUTO); 
		    para.setAlignment(ParagraphAlignment.CENTER);  
		    int size =carryData.size();
		    XWPFTable carryTable = doc.createTable(size+1, 3);
		    CTTbl ttbl = carryTable.getCTTbl();  
	        CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();  
	        CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();  
	        tblWidth.setW(new BigInteger("8400"));  
	        tblWidth.setType(STTblWidth.DXA);  
	        
	        carryTable.getRow(0).setHeight(480); 
	        String[] titleArr5 = {"区域","偏差量（MWh） ","偏差原因 "};
	        for(int m =0;m<3;m++){
	        	XWPFTableCell cell = carryTable.getRow(0).getCell(m);
	        	XWPFParagraph xp = cell.addParagraph();
		        xp.setVerticalAlignment(TextAlignment.CENTER);
		        xp.setAlignment(ParagraphAlignment.CENTER);
		        XWPFRun temp = xp.createRun();
		        temp.setText(titleArr5[m]);  
		        temp.setFontFamily(CONTENT_FONT_FAMILLY);
		        temp.setFontSize(CONTENT_FONT_SIZE);
		        cell.removeParagraph(0);
		        cell.setColor("CCCCCC");  
		        cell.setVerticalAlignment(XWPFVertAlign.CENTER);  
		       
	        }
	        int b=1;
	        for (Entry<String,Map<String,Double>> entry : carryData.entrySet()) {
	        	carryTable.getRow(b).setHeight(480); 
	        	for (int j = 0; j < 3; j++) { 
	        		XWPFTableCell cell = carryTable.getRow(b).getCell(j);
	            	XWPFParagraph xp = cell.addParagraph();
			        xp.setVerticalAlignment(TextAlignment.CENTER);
			        xp.setAlignment(ParagraphAlignment.CENTER);
			        XWPFRun temp = xp.createRun();
			        if(j==0){
			        	temp.setText(entry.getKey()); 
			        }else if(j==1){
			        	temp.setText(String.valueOf(entry.getValue().get("range"))); 
			        }else{
			        	temp.setText("模拟运行，未执行"); 
			        }
			        temp.setFontFamily(CONTENT_FONT_FAMILLY);
			        temp.setFontSize(CONTENT_FONT_SIZE);
			        cell.removeParagraph(0);
			        cell.setVerticalAlignment(XWPFVertAlign.CENTER);
	        	}
	        	b++;
			}
	        //end====================================
	        
	        String tempFileNamePath = tempPath + File.separator + fileName;
	        String _tempFileNamePath = tempPath + File.separator +"_"+ fileName;

			File file = new File(_tempFileNamePath);
			FileOutputStream fos = new FileOutputStream(file);
			doc.write(fos);
			// hwpf.write(fos);
			fos.flush();
			doc.close();
			/** 打开word2007的文件设置图片 */
			OPCPackage opc = POIXMLDocument.openPackage(_tempFileNamePath);
			CustomXWPFDocument document = new CustomXWPFDocument(opc);
			
			// 处理段落
			XWPFParagraph channelParagraph = null;
			List<XWPFParagraph> paragraphList = document.getParagraphs();
			if (paragraphList != null && paragraphList.size() > 0) {
				for (XWPFParagraph paragraph : paragraphList) {
					List<XWPFRun> runs = paragraph.getRuns();
					for (XWPFRun r : runs) {
						String text = r.getText(0);
						if (text != null) {
							if (text.indexOf("${channelImage}") > -1) {
								r.setText("", 0);
								channelParagraph = paragraph;
							}
						}
					}
				}
			}
			String iconPath = SystemConstUtil.getTempPath() + File.separator + "channelRatio.png";
			if(channelParagraph != null){
				int picType = document.PICTURE_TYPE_PNG;
				
				File pictureF = new File(iconPath);
				if(pictureF.exists()){
					FileInputStream imageIns = new FileInputStream(pictureF);
					document.addPictureData(imageIns, picType);
					document.createPicture(channelParagraph, document.getAllPictures().size() - 1, 600, 300, "");
					if (imageIns != null) {
						imageIns.close();
					}
				}
			}
			FileOutputStream fopts = new FileOutputStream(tempFileNamePath);
			document.write(fopts);
			fopts.flush();
			fopts.close();
			document.close();
			
			FileManager.delete(_tempFileNamePath);
			FileManager.delete(iconPath);
			System.out.println("---------成功--------");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (pfs != null) {
					pfs.close();
				}
				if (os != null) {
					os.close();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * 插入表格
	 * @author 车斯剑
	 * @date 2016年10月17日下午10:13:54
	 * @param doc
	 * @param para
	 * @param saleData
	 * @param titles
	 */
	public static void insertTradeTable(XWPFDocument doc, XWPFParagraph para, Map<String,Map<String,Double>> saleData,String[] titles){
		CTPPr pr = para.getCTP().getPPr() != null ? para.getCTP().getPPr() : para.getCTP().addNewPPr();  
        CTSpacing spacing =pr.addNewSpacing();  
        spacing.setLine(new BigInteger("360"));  
        spacing.setLineRule(STLineSpacingRule.AUTO); 
	    para.setAlignment(ParagraphAlignment.CENTER);  
	    int size =saleData.size();
	    XWPFTable xTable = doc.createTable(size+1, titles.length);
	    CTTbl ttbl = xTable.getCTTbl();  
        CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();  
        CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();  
        tblWidth.setW(new BigInteger("8400"));  
        tblWidth.setType(STTblWidth.DXA);  
       
        xTable.getRow(0).setHeight(480); 
        
        for(int m =0;m<titles.length;m++){
        	XWPFTableCell cell = xTable.getRow(0).getCell(m);
        	XWPFParagraph xp = cell.addParagraph();
	        xp.setVerticalAlignment(TextAlignment.CENTER);
	        xp.setAlignment(ParagraphAlignment.CENTER);
	        XWPFRun temp = xp.createRun();
	        temp.setText(titles[m]);  
	        temp.setFontFamily(CONTENT_FONT_FAMILLY);
	        temp.setFontSize(CONTENT_FONT_SIZE);
	        cell.removeParagraph(0);
	        cell.setColor("CCCCCC");  
	        cell.setVerticalAlignment(XWPFVertAlign.CENTER);  
	       
        }
        int y=1;
        for (Entry<String,Map<String,Double>> entry : saleData.entrySet()) {
        	xTable.getRow(y).setHeight(480); 
        	Map<String, Double> value = entry.getValue();
        	for (int j = 0; j < titles.length; j++) { 
        		XWPFTableCell cell = xTable.getRow(y).getCell(j);
            	XWPFParagraph xp = cell.addParagraph();
		        xp.setVerticalAlignment(TextAlignment.CENTER);
		        xp.setAlignment(ParagraphAlignment.CENTER);
		        XWPFRun temp = xp.createRun();
		        if(j==0){
		        	temp.setText(entry.getKey()); 
		        }else if(j==1){
		        	temp.setText(CommonUtil.toFix(value.get("power"))); 
		        }else if(j==2){
		        	temp.setText(CommonUtil.toFix(value.get("price"))); 
		        }else{
		        	temp.setText(CommonUtil.toFix(value.get("quantity"))); 
		        }
		        temp.setFontFamily("仿宋");
		        temp.setFontSize(13);
		        cell.removeParagraph(0);
		        cell.setVerticalAlignment(XWPFVertAlign.CENTER);
        	}
        	y++;
		}
	}
	/**
	 * 导出交易单
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月26日下午7:44:18
	 * @param tempPath
	 *            临时路径
	 * @param dateStr
	 *            日期
	 * @param area
	 *            区域
	 * @param po
	 *            订单
	 * @param resultM
	 *            成交数据
	 * @param dealResultPo
	 *            交易数据
	 * @param userList
	 *            签名    
	 * @param issueUserGraList 发布签名
	 * 
	 * @param dealUserGraList 执行结果签名
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String exportDoc(List<UserPo> userIds, List<String> areas,String tempPath, String dateStr, List<Map<String, List<Map<String, Float>>>> declareData,
			List<Map<String, Map<String, Map<String, Double>>>> dealData, List<Map<String, Map<String, String>>> pathDatas,
			List<Map<String, Map<String, String>>> executeDatas, List<UserPo> userList, List<UserPo> issueUserGraList, List<UserPo> dealUserGraList) {

		String fileName = "富余可再生能源跨省区现货市场" + dateStr + "交易单.docx";
		LoggerUtil.log("开始导出交易单", "导出文件----" + fileName, 0);
		FileOutputStream fos = null;
		try {
			
			//XWPFDocument doc = new XWPFDocument();
			InputStream is = new FileInputStream("c:/test.doc");
			XWPFDocument doc = new CustomXWPFDocument(is);
			// 创建一个段落
			XWPFParagraph para = doc.createParagraph();
			para.setAlignment(ParagraphAlignment.CENTER);
			// 一个XWPFRun代表具有相同属性的一个区域
			// 设置标题
			XWPFRun run = para.createRun();
			run.setBold(true);
			// run.setSubscript(VerticalAlign.);
			run.setFontFamily(TITLE_FONT_FAMILLY);
			run.setFontSize(TITLE_FONT_SIZE);
			run.setText("富余可再生能源跨省区现货市场");
			
			para = doc.createParagraph();
			para.setAlignment(ParagraphAlignment.CENTER);
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(TITLE_FONT_FAMILLY);
			run.setFontSize(SUBTITLE_FONT_SIZE);
			run.setText(dateStr + "交易单");

			// 设置内容content
			para = doc.createParagraph();
			para.setSpacingBefore(0);
			para.setSpacingAfter(0);
			para.setSpacingAfterLines(0);
			para.setSpacingBeforeLines(0);
			para.setAlignment(ParagraphAlignment.LEFT);
			// para.setAlignment(ParagraphAlignment.CENTER);
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("一、申报数据");

			String[] titleArr = {"开始时段","结束时段 ","电力（MW）","电价（元/MWh）"};
			int indexNum = 0;
			for (Map<String, List<Map<String, Float>>> map : declareData) {
			//for (int i =0; i<declareData.size(); i++) {
				int size =0;
				for (Entry<String, List<Map<String, Float>>> entry : map.entrySet()) {
					size +=entry.getValue().size();
				}
				para = doc.createParagraph();
				para.setSpacingBefore(0);
				para.setSpacingAfter(0);
				para.setSpacingAfterLines(0);
				para.setSpacingBeforeLines(0);
				para.setAlignment(ParagraphAlignment.LEFT);
				run = para.createRun();
				run.setBold(true);
				run.setFontFamily(CONTENT_FONT_FAMILLY);
				run.setFontSize(CONTENT_FONT_SIZE);
				run.setText((indexNum+1)+"、"+areas.get(indexNum));
				
				CTPPr pr = para.getCTP().getPPr() != null ? para.getCTP().getPPr() : para.getCTP().addNewPPr();  
		        CTSpacing spacing =pr.addNewSpacing();  
		        spacing.setLine(new BigInteger("360"));  
		        spacing.setLineRule(STLineSpacingRule.AUTO); 
			    para.setAlignment(ParagraphAlignment.LEFT);  
			    //int size =saleData.size();
			    XWPFTable xTable = doc.createTable(size+1, titleArr.length);
			    CTTbl ttbl = xTable.getCTTbl();  
		        CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();  
		        CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();  
		        tblWidth.setW(new BigInteger("8400"));  
		        tblWidth.setType(STTblWidth.DXA);  
		       
		        xTable.getRow(0).setHeight(480); 
		        
		        for(int m =0;m<titleArr.length;m++){
		        	XWPFTableCell cell = xTable.getRow(0).getCell(m);
		        	XWPFParagraph xp = cell.addParagraph();
			        xp.setVerticalAlignment(TextAlignment.CENTER);
			        xp.setAlignment(ParagraphAlignment.CENTER);
			        XWPFRun temp = xp.createRun();
			        temp.setText(titleArr[m]);  
			        temp.setFontFamily(CONTENT_FONT_FAMILLY);
			        temp.setFontSize(CONTENT_FONT_SIZE);
			        cell.removeParagraph(0);
			        cell.setColor("CCCCCC");  
			        cell.setVerticalAlignment(XWPFVertAlign.CENTER);  
			       
		        }
		        int y=1;
		        for (Entry<String, List<Map<String, Float>>> entry : map.entrySet()) {
		        	String timeStrs =entry.getKey();
		        	String[] timeAra = timeStrs.split("a");
		        	xTable.getRow(y).setHeight(480); 
		        	List<Map<String, Float>> timeData = entry.getValue();
		        	for (Map<String, Float> value : timeData) {
		        		for (int j = 0; j < titleArr.length; j++) { 
			        		XWPFTableCell cell = xTable.getRow(y).getCell(j);
			            	XWPFParagraph xp = cell.addParagraph();
					        xp.setVerticalAlignment(TextAlignment.CENTER);
					        xp.setAlignment(ParagraphAlignment.CENTER);
					        XWPFRun temp = xp.createRun();
					        if(j==0){
					        	temp.setText(timeAra[0]); 
					        }else if(j==1){
					        	temp.setText(timeAra[1]); 
					        }else if(j==2){
					        	temp.setText(CommonUtil.toFix(value.get("电力"))); 
					        }else{
					        	temp.setText(CommonUtil.toFix(value.get("电价"))); 
					        }
					        temp.setFontFamily("仿宋");
					        temp.setFontSize(13);
					        cell.removeParagraph(0);
					        cell.setVerticalAlignment(XWPFVertAlign.CENTER);
			        	}
			        	y++;
					}
		        	
				}
		        para = doc.createParagraph();
				para.setAlignment(ParagraphAlignment.LEFT);
				if(userIds != null && userIds.size()>0){
					UserPo user = userIds.get(indexNum);
					System.out.println("user.getAutoGraph()==="+user.getAutoGraph());
					int picType = 0;
					if (CommonUtil.ifEmpty(user.getAutoGraph())) {
						if (user.getAutoGraph().indexOf(".bmp") > -1) {
							picType = doc.PICTURE_TYPE_BMP;
						} else if (user.getAutoGraph().indexOf(".png") > -1) {
							picType = doc.PICTURE_TYPE_PNG;
						}
						String iconPath = SystemConstUtil.getIconPath() + File.separator + user.getAutoGraph();
						LoggerUtil.log("导出交易单交易结果签章", iconPath, 0);
						File pictureF = new File(iconPath);
						if(pictureF.exists()){
							CustomXWPFDocument cdoc = (CustomXWPFDocument) doc;
							FileInputStream imageIns = new FileInputStream(pictureF);
							cdoc.addPictureData(imageIns, picType);
							cdoc.createPicture(para, doc.getAllPictures().size() - 1, SIGN_PIC_WIDTH, SIGN_PIC_HEIGHT, "");
							if (imageIns != null) {
								imageIns.close();
							}
						}
						
					}
				}
				indexNum++;
			}
			
			para = doc.createParagraph();
			// para.setAlignment(ParagraphAlignment.CENTER);
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("二、成交数据");

			String[] titleArr2 = {"成分","开始时段","结束时段 ","电力（MW）","电价（元/MWh）"};
			if(areas != null && dealData != null && areas.size()>0 ){
				int index = 0;
				int a = 0;
				for(String area : areas){
					Map<String, Map<String, Map<String, Double>>> data = dealData.get(a++);
					if(data == null){
						continue;
					}
					
					index++;
					//System.out.println("999999999999");
					para = doc.createParagraph();
					para.setSpacingBefore(0);
					para.setSpacingAfter(0);
					para.setSpacingAfterLines(0);
					para.setSpacingBeforeLines(0);
					para.setAlignment(ParagraphAlignment.LEFT);
					run = para.createRun();
					run.setBold(true);
					run.setFontFamily(CONTENT_FONT_FAMILLY);
					run.setFontSize(CONTENT_FONT_SIZE);
					run.setText(index+"、"+area);
					
					//创建表格
					CTPPr pr = para.getCTP().getPPr() != null ? para.getCTP().getPPr() : para.getCTP().addNewPPr();
					CTSpacing spacing = pr.addNewSpacing();
					spacing.setLine(new BigInteger("360"));
					spacing.setLineRule(STLineSpacingRule.AUTO);
					para.setAlignment(ParagraphAlignment.LEFT);

					XWPFTable xTable = doc.createTable();
					CTTbl ttbl = xTable.getCTTbl();
					CTTblPr tblPr = ttbl.getTblPr() == null ? ttbl.addNewTblPr() : ttbl.getTblPr();
					CTTblWidth tblWidth = tblPr.isSetTblW() ? tblPr.getTblW() : tblPr.addNewTblW();
					tblWidth.setW(new BigInteger("8400"));
					tblWidth.setType(STTblWidth.DXA);

					// xTable.getRow(0).setHeight(480);
					XWPFTableRow row = xTable.getRow(0);
					row.setHeight(480);
					XWPFTableCell cell = null;
					for (int m = 0; m < titleArr2.length; m++) {
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
						temp.setText(titleArr2[m] == null ? "" : String.valueOf(titleArr2[m]));
						// temp.setFontFamily(CONTENT_FONT_FAMILLY);
						// temp.setFontSize(CONTENT_FONT_SIZE);
						cell.removeParagraph(0);
						cell.setColor("CCCCCC");
						cell.setVerticalAlignment(XWPFVertAlign.CENTER);

					}
					//遍历成分
					for(Entry<String, Map<String, Map<String, Double>>> pathEntry : data.entrySet()){
						String pathStr = pathEntry.getKey();
						Map<String, Double> powerMap = pathEntry.getValue().get("电力");
						Map<String, Double> priceMap = pathEntry.getValue().get("电价");
						if(powerMap != null && priceMap != null){
							for(Entry<String, Double> entry : powerMap.entrySet()){
								String[] times = entry.getKey().split("a");
								String power = entry.getValue()== null?"":CommonUtil.toFix(entry.getValue());
								String price = priceMap.get(entry.getKey()) == null?"":CommonUtil.toFix(priceMap.get(entry.getKey()));
								row = xTable.createRow();
								row.setHeight(480);
								for (int j = 0; j < titleArr2.length; j++) {
									cell = row.getCell(j);
									XWPFParagraph xp = cell.addParagraph();
									xp.setVerticalAlignment(TextAlignment.CENTER);
									xp.setAlignment(ParagraphAlignment.CENTER);
									xp.setStyle("table");
									XWPFRun temp = xp.createRun();
									if(j == 0){
										temp.setText(pathStr);
									}else if(j == 1){
										temp.setText(times[0]);
									}else if(j == 2){
										temp.setText(times[1]);
									}else if(j == 3){
										temp.setText(power);
									}else if(j == 4){
										temp.setText(price);
									}
									cell.removeParagraph(0);
									cell.setVerticalAlignment(XWPFVertAlign.CENTER);
								}
							}
						}
						
					}
					para = doc.createParagraph();
					para.setAlignment(ParagraphAlignment.LEFT);
					if(issueUserGraList != null && issueUserGraList.size()>0){
						UserPo user = issueUserGraList.get(0);
						int picType = 0;
						if (CommonUtil.ifEmpty(user.getAutoGraph())) {
							if (user.getAutoGraph().indexOf(".bmp") > -1) {
								picType = doc.PICTURE_TYPE_BMP;
							} else if (user.getAutoGraph().indexOf(".png") > -1) {
								picType = doc.PICTURE_TYPE_PNG;
							}
							String iconPath = SystemConstUtil.getIconPath() + File.separator + user.getAutoGraph();
							LoggerUtil.log("导出交易单交易结果签章", iconPath, 0);
							File pictureF = new File(iconPath);
							if(pictureF.exists()){
								CustomXWPFDocument cdoc = (CustomXWPFDocument) doc;
								FileInputStream imageIns = new FileInputStream(pictureF);
								cdoc.addPictureData(imageIns, picType);
								cdoc.createPicture(para, doc.getAllPictures().size() - 1, SIGN_PIC_WIDTH, SIGN_PIC_HEIGHT, "");
								if (imageIns != null) {
									imageIns.close();
								}
							}
							
						}
					}
				}
			}
			//System.out.println("----------"+CommonUtil.objToJson(dealData));
			para = doc.createParagraph();
			para.setSpacingBefore(0);
			para.setSpacingAfter(0);
			para.setSpacingAfterLines(0);
			para.setSpacingBeforeLines(0);
			para.setAlignment(ParagraphAlignment.LEFT);
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("三、通道成交结果");
			if(CommonUtil.ifEmpty_List(pathDatas)){
				int index = 0;
				for(Map<String, Map<String, String>> lineMap : pathDatas){
					if(!lineMap.isEmpty()){
						String keyStr = null;
						for(String key : lineMap.keySet()){
							keyStr = key;
							break;
						}
						index++;
						String[] keyArr = keyStr.split("-");
						String lineName = keyArr[1];
						para = doc.createParagraph();
						para.setSpacingBefore(0);
						para.setSpacingAfter(0);
						para.setSpacingAfterLines(0);
						para.setSpacingBeforeLines(0);
						para.setAlignment(ParagraphAlignment.LEFT);
						run = para.createRun();
						run.setBold(true);
						run.setFontFamily(CONTENT_FONT_FAMILLY);
						run.setFontSize(CONTENT_FONT_SIZE);
						run.setText(index+"、"+lineName);
						createTable(doc.createParagraph(),doc,lineMap);
					}
				}
			}
			
			para = doc.createParagraph();
			para.setSpacingBefore(0);
			para.setSpacingAfter(0);
			para.setSpacingAfterLines(0);
			para.setSpacingBeforeLines(0);
			para.setAlignment(ParagraphAlignment.LEFT);
			run = para.createRun();
			run.setBold(true);
			run.setFontFamily(CONTENT_FONT_FAMILLY);
			run.setFontSize(CONTENT_FONT_SIZE);
			run.setText("四、执行结果");
			//System.out.println("-----------"+CommonUtil.objToJson(executeDatas));
			if(CommonUtil.ifEmpty_List(executeDatas)){
				int index = 0;
				for(Map<String, Map<String, String>> lineMap : executeDatas){
					if(!lineMap.isEmpty()){
						String keyStr = null;
						for(String key : lineMap.keySet()){
							keyStr = key;
							break;
						}
						index++;
						String[] keyArr = keyStr.split("-");
						String lineName = keyArr[1];
						para = doc.createParagraph();
						para.setSpacingBefore(0);
						para.setSpacingAfter(0);
						para.setSpacingAfterLines(0);
						para.setSpacingBeforeLines(0);
						para.setAlignment(ParagraphAlignment.LEFT);
						run = para.createRun();
						run.setBold(true);
						run.setFontFamily(CONTENT_FONT_FAMILLY);
						run.setFontSize(CONTENT_FONT_SIZE);
						run.setText(index+"、"+lineName);
						createTable(doc.createParagraph(),doc,lineMap);
					}
				}
			}

			
			if (dealUserGraList != null && dealUserGraList.size() > 0) {
				para = doc.createParagraph();
				para.setAlignment(ParagraphAlignment.RIGHT);
				run = para.createRun();
				run.setFontFamily(CONTENT_FONT_FAMILLY);
				run.setFontSize(CONTENT_FONT_SIZE);
				run.setText("${dealImage}");
			}
			
			
			//用来存储临时文件
			String _tempFileNamePath = tempPath + File.separator + fileName;
			if (CommonUtil.ifEmpty_List(userList) || CommonUtil.ifEmpty_List(issueUserGraList) || CommonUtil.ifEmpty_List(dealUserGraList)) {
				//System.out.println("========================");
				String tempFileNamePath = tempPath + File.separator + "_" + fileName;
				File file = new File(tempFileNamePath);
				fos = new FileOutputStream(file);
				doc.write(fos);
				// hwpf.write(fos);
				fos.flush();
				if (fos != null) {
					fos.close();
				}
				doc.close();

				//** 打开word2007的文件设置签章图片 *//*
				OPCPackage opc = POIXMLDocument.openPackage(tempFileNamePath);
				CustomXWPFDocument document = new CustomXWPFDocument(opc);
				
				// XWPFParagraph paragraph = document.createParagraph();
				// 处理段落
				
				XWPFParagraph declareParagraph = null;
				XWPFParagraph issueParagraph = null;
				XWPFParagraph dealParagraph = null;
				List<XWPFParagraph> paragraphList = document.getParagraphs();
				if (paragraphList != null && paragraphList.size() > 0) {
					for (XWPFParagraph paragraph : paragraphList) {
						List<XWPFRun> runs = paragraph.getRuns();
						for (XWPFRun r : runs) {
							String text = r.getText(0);
							// System.out.println("-------"+text);
							if (text != null) {
								if (text.indexOf("${declareImage}") > -1) {
									r.setText("", 0);
									declareParagraph = paragraph;
								}else if (text.indexOf("${issueImage}") > -1) {
									r.setText("", 0);
									issueParagraph = paragraph;
								}else if (text.indexOf("${dealImage}") > -1) {
									r.setText("", 0);
									dealParagraph = paragraph;
								}
							}
						}
					}
				}
				UserPo user = null;
				// 循环获取签名图片
				if(declareParagraph != null && CommonUtil.ifEmpty(userList)){
					user = userList.get(0);
					int picType = 0;
					if (CommonUtil.ifEmpty(user.getAutoGraph())) {
						if (user.getAutoGraph().indexOf(".bmp") > -1) {
							picType = document.PICTURE_TYPE_BMP;
						} else if (user.getAutoGraph().indexOf(".png") > -1) {
							picType = document.PICTURE_TYPE_PNG;
						}
						String iconPath = SystemConstUtil.getIconPath() + File.separator + user.getAutoGraph();
						LoggerUtil.log("导出交易单申报数据签章", iconPath, 0);
						File pictureF = new File(iconPath);
						if(pictureF.exists()){
							FileInputStream imageIns = new FileInputStream(pictureF);
							document.addPictureData(imageIns, picType);
							document.createPicture(declareParagraph, document.getAllPictures().size() - 1, SIGN_PIC_WIDTH, SIGN_PIC_HEIGHT, "");
							if (imageIns != null) {
								imageIns.close();
							}
						}
						
					}
				}
				
				if(issueParagraph != null && CommonUtil.ifEmpty(issueUserGraList)){
					user = issueUserGraList.get(0);
					int picType = 0;
					if (CommonUtil.ifEmpty(user.getAutoGraph())) {
						if (user.getAutoGraph().indexOf(".bmp") > -1) {
							picType = document.PICTURE_TYPE_BMP;
						} else if (user.getAutoGraph().indexOf(".png") > -1) {
							picType = document.PICTURE_TYPE_PNG;
						}
						String iconPath = SystemConstUtil.getIconPath() + File.separator + user.getAutoGraph();
						LoggerUtil.log("导出交易单交易结果签章", iconPath, 0);
						File pictureF = new File(iconPath);
						if(pictureF.exists()){
							FileInputStream imageIns = new FileInputStream(pictureF);
							document.addPictureData(imageIns, picType);
							document.createPicture(issueParagraph, document.getAllPictures().size() - 1, SIGN_PIC_WIDTH, SIGN_PIC_HEIGHT, "");
							if (imageIns != null) {
								imageIns.close();
							}
						}
						
					}
				}

				if(dealParagraph != null && CommonUtil.ifEmpty(dealUserGraList)){
					user = dealUserGraList.get(0);
					int picType = 0;
					if (CommonUtil.ifEmpty(user.getAutoGraph())) {
						if (user.getAutoGraph().indexOf(".bmp") > -1) {
							picType = document.PICTURE_TYPE_BMP;
						} else if (user.getAutoGraph().indexOf(".png") > -1) {
							picType = document.PICTURE_TYPE_PNG;
						}
						String iconPath = SystemConstUtil.getIconPath() + File.separator + user.getAutoGraph();
						LoggerUtil.log("导出交易单执行结果签章", iconPath, 0);
						File pictureF = new File(iconPath);
						if(pictureF.exists()){
							FileInputStream imageIns = new FileInputStream(pictureF);
							document.addPictureData(imageIns, picType);
							document.createPicture(dealParagraph, document.getAllPictures().size() - 1, SIGN_PIC_WIDTH, SIGN_PIC_HEIGHT, "");
							if (imageIns != null) {
								imageIns.close();
							}
						}else{
							//System.out.println("图片不存在");
						}
						
					}
				}
				
				FileOutputStream fopts = new FileOutputStream(_tempFileNamePath);
				document.write(fopts);
				fopts.flush();
				fopts.close();
				document.close();
				FileManager.delete(tempFileNamePath);
			} else {
				//String tempFileNamePath = tempPath + File.separator + "temp_"+ fileName;
				File file = new File(_tempFileNamePath);
				fos = new FileOutputStream(file);
				doc.write(fos);
				// hwpf.write(fos);
				fos.flush();
				if (fos != null) {
					fos.close();
				}
				doc.close();
			}
			
			
			LoggerUtil.log("结束导出交易单", "导出文件----" + tempPath + File.separator + fileName, 0);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fileName;
	}
	private static void createTable(XWPFParagraph para, XWPFDocument doc, Map<String, Map<String, String>> lineMap) {
		// TODO Auto-generated method stub
		CTPPr pr = para.getCTP().getPPr() != null ? para.getCTP().getPPr() : para.getCTP().addNewPPr();
		CTSpacing spacing = pr.addNewSpacing();
		spacing.setLine(new BigInteger("360"));
		spacing.setLineRule(STLineSpacingRule.AUTO);
		para.setAlignment(ParagraphAlignment.LEFT);
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
		String[] titles = {"直流","成分","开始时段","结束时段 ","电量（MWh）"};
		
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
		
		for(Entry<String, Map<String, String>> entry: lineMap.entrySet()){
			String[] keyArr = entry.getKey().split("-");
			String lineName = keyArr[1];
			for(Entry <String, String>  data: entry.getValue().entrySet()){
				row = xTable.createRow();
				row.setHeight(480);
				String[] times = data.getKey().split("-");
				for (int j = 0; j < titles.length; j++) {
					cell = row.getCell(j);
					XWPFParagraph xp = cell.addParagraph();
					xp.setVerticalAlignment(TextAlignment.CENTER);
					xp.setAlignment(ParagraphAlignment.CENTER);
					xp.setStyle("table");
					XWPFRun temp = xp.createRun();
					if(j == 0){
						temp.setText(lineName);
					}else if(j == 1){
						temp.setText(keyArr[0]);
					}else if(j == 2){
						temp.setText(times[0]);
					}else if(j == 3){
						temp.setText(times[1]);
					}else if(j == 4){
						temp.setText(data.getValue());
					}
					cell.removeParagraph(0);
					cell.setVerticalAlignment(XWPFVertAlign.CENTER);
				}
			}
			
		}
	}
	
	
}
