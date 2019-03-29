package com.state.service.impl;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.state.dao.IssueDao;
import com.state.dao.SurveyDaoI;
import com.state.enums.Enums_DeclareType;
import com.state.enums.Enums_SystemConst;
import com.state.po.DeclareDataPo;
import com.state.po.DeclarePo;
import com.state.po.ResultPo;
import com.state.po.TypePo;
import com.state.service.IDeclareService;
import com.state.service.ServiceHelper;
import com.state.service.SurveyServiceI;
import com.state.util.CommonUtil;
import com.state.util.DeclareUtil;
import com.state.util.FileManager;
import com.state.util.LoggerUtil;
import com.state.util.sys.SystemConstUtil;

@Service
public class SurveyServiceImpl implements SurveyServiceI {

	@Autowired
	private IssueDao issueDao;
	@Autowired
	private SurveyDaoI surveyDao;
	
	public Map<String, Object> getData(String mdate) {
		// TODO Auto-generated method stub
		/******计算申报数据*******/
		Map<String,  List<DeclarePo>> buyMap = new HashMap<String, List<DeclarePo>>();
		Map<String, List<DeclarePo>> saleMap = new HashMap<String, List<DeclarePo>>();
		Map<String, Double> sumElectricity = new HashMap<String, Double>();
		Map<String, Object> result = new HashMap<String, Object>();
		int buySum = 0;
		int saleSum = 0;
		List<DeclarePo> declares = ServiceHelper.getBean(IDeclareService.class).getListAndDataByMdate(mdate, null,null);
		//System.out.println(mdate+"-------"+declares.size());
		if(CommonUtil.ifEmpty_List(declares)){
			for(DeclarePo dpo : declares){
				//dpo = DeclareUtil.calculate(dpo);
				
				List<DeclarePo> sheetList = null;
				// 如果是买方
				if ("buy".equals(dpo.getDrloe())) {
					buySum++;
					if (buyMap.containsKey(dpo.getArea())) {
						sheetList = buyMap.get(dpo.getArea());
					} else {
						sheetList = new ArrayList<DeclarePo>();
						buyMap.put(dpo.getArea(), sheetList);
					}
				} else {
					saleSum++;
					if (saleMap.containsKey(dpo.getArea())) {
						sheetList = saleMap.get(dpo.getArea());
					} else {
						sheetList = new ArrayList<DeclarePo>();
						saleMap.put(dpo.getArea(), sheetList);
					}
				}
				
				sheetList.add(dpo);
				// 计算每个单的总电量,总电力，总电价，平均电力，平均电量，平均电价
				dpo = DeclareUtil.calculate(dpo);
				//System.out.println("----------"+CommonUtil.objToJson(dpo));
				// 统计总电量
				if (sumElectricity.containsKey(dpo.getDrloe())) {
					sumElectricity.put(dpo.getDrloe(), sumElectricity.get(dpo.getDrloe()) + dpo.getSumElectricity());
				} else {
					sumElectricity.put(dpo.getDrloe(), dpo.getSumElectricity());
				}
			}
		}
		
		if(sumElectricity.get("buy") == null){
			sumElectricity.put("buy",0d);
		}
		if(sumElectricity.get("sale") == null){
			sumElectricity.put("sale",0d);
		}
		result.put("buySumElectricity", sumElectricity.get("buy"));
		result.put("saleSumElectricity", sumElectricity.get("sale"));
		//result.put("saleMap", saleMap);
		//result.put("buyMap", buyMap);
		//result.put("buySum",buySum);
		//result.put("saleSum",saleSum);
		int tableSize = 0;
		
		//System.out.println("-----"+CommonUtil.objToJson(sumElectricity));
		
		/******计算成交笔数*******/
		result.put("clearNum",0);
		String path = SystemConstUtil.getMatchPath()+File.separator+"clearData"+File.separator+mdate + ".json";
		File f = new File(path);
		if(f.exists()){
			String json = FileManager.readFile(path);
			if(CommonUtil.ifEmpty(json)){
				Map<String,Object> clearResult = CommonUtil.jsonToMapObj(json);
				if(clearResult != null){
					result.put("clearNum",clearResult.get("total"));
				}
			}
		}
		
		/******计算成交数据*******/
		
		Map<String,  ResultPo> buyClearMap = new HashMap<String, ResultPo>();
		Map<String, ResultPo> saleClearMap = new HashMap<String, ResultPo>();
		double buyClearElectricity = 0;
		double saleClearElectricity = 0;
		List<ResultPo> rpos = issueDao.getTotalResultByTime(mdate);
		if(CommonUtil.ifEmpty_List(rpos)){
			for (ResultPo po : rpos) {
				if(po.getSumQ()==null || po.getSumQ()==0 ){
					continue;
				}
				
				if(po.getDrole().equals("buy") && po.getSide().equals(Enums_DeclareType.DROLE_RECV)){
					buyClearElectricity += po.getSumQ();
					buyClearMap.put(po.getArea(), po);
				}else if(po.getDrole().equals("sale") && po.getSide().equals(Enums_DeclareType.DROLE_SEND)){
					saleClearElectricity += po.getSumQ();
					saleClearMap.put(po.getArea(), po);
				}
			}
		}
		
		//System.out.println("buyClearMap------"+CommonUtil.objToJson(buyClearMap));
		
		//申报和成交电量
		Vector<String> columnCategoriesData = new Vector<String>();
		Vector<Double> columnDeclareSeriesData = new Vector<Double>();
		Vector<Double> columnClearSeriesData = new Vector<Double>();
		
		result.put("columnCategoriesData", columnCategoriesData);
		result.put("columnDeclareSeriesData", columnDeclareSeriesData);
		result.put("columnClearSeriesData", columnClearSeriesData);
		
		double buyClearSumElectricity = 0;
		double saleClearSumElectricity = 0;
		
		
		Map<String,Object> buyDeclareTableMap = new HashMap<String, Object>();
		Vector<Vector<Object>> buyDeclareChartData = new Vector<Vector<Object>>();
		//购电申报电量表格
		result.put("buyDeclareTableData", buyDeclareTableMap);
		//购电申报电量饼状图
		result.put("buyDeclareChartData", buyDeclareChartData);
		
		Map<String,Object> buyClearTableData = new HashMap<String, Object>();
		Vector<Vector<Object>> buyClearChartData = new Vector<Vector<Object>>();
		//购电交易电量表格
		result.put("buyClearTableData", buyClearTableData);
		//购电交易电量饼状图
		result.put("buyClearChartData", buyClearChartData);
		
		if(sumElectricity!= null && sumElectricity.get("buy")>0 && buyMap != null && buyMap.size()>0){
			if(buyMap.size() > tableSize ){
				tableSize = buyMap.size();
			}
			int index = 1;
			int clearIndex = 1;
			
			double sumPercent = 0;
			double sumClearPercent = 0;
			for(Entry<String,  List<DeclarePo>> entry : buyMap.entrySet()){
				DeclarePo declarePo = null;
				if(CommonUtil.ifEmpty_List(entry.getValue())){
					//一个省只有一个单子
					declarePo = entry.getValue().get(0);
					columnCategoriesData.add(declarePo.getArea());
					String sumElectricityStr = CommonUtil.toFix(declarePo.getSumElectricity());
					columnDeclareSeriesData.add(Double.parseDouble(sumElectricityStr));
					buyDeclareTableMap.put(entry.getKey(), sumElectricityStr);
					if(index == buyMap.size()){
						
						Vector<Object> buySingleDeclareChartData = new Vector<Object>();
						buySingleDeclareChartData.add(declarePo.getArea());
						buySingleDeclareChartData.add(100-sumPercent);
						buyDeclareChartData.add(buySingleDeclareChartData);
					}else{
						double percent = declarePo.getSumElectricity()*100/sumElectricity.get("buy");
						double newPercent = Double.parseDouble( CommonUtil.toFix(percent));
						
						sumPercent+= newPercent;
						Vector<Object> buySingleDeclareChartData = new Vector<Object>();
						buySingleDeclareChartData.add(declarePo.getArea());
						buySingleDeclareChartData.add(newPercent);
						buyDeclareChartData.add(buySingleDeclareChartData);
					}

					index++;
				}
				
				ResultPo resultPo = buyClearMap.get(entry.getKey());
				String sumClearElectricityStr = "0";
				if(resultPo != null){
					//一个省只有一个单子
					buyClearSumElectricity+=resultPo.getSumQ()/4;
					sumClearElectricityStr = CommonUtil.toFix(resultPo.getSumQ()/4);
					buyClearTableData.put(entry.getKey(), sumClearElectricityStr);
					if(clearIndex == buyMap.size()){
						//System.out.println("-------"+sumClearPercent);
						Vector<Object> buySingleClearChartData = new Vector<Object>();
						buySingleClearChartData.add(resultPo.getArea());
						buySingleClearChartData.add(Double.parseDouble(CommonUtil.toFix((100-sumClearPercent))));
						buyClearChartData.add(buySingleClearChartData);
					}else{
						double percent = buyClearElectricity == 0?0:resultPo.getSumQ()*100/buyClearElectricity;
						double newPercent = Double.parseDouble( CommonUtil.toFix(percent));
						sumClearPercent+= newPercent;
						Vector<Object> buySingleClearChartData = new Vector<Object>();
						buySingleClearChartData.add(resultPo.getArea());
						buySingleClearChartData.add(newPercent);
						buyClearChartData.add(buySingleClearChartData);
					}
					clearIndex++;
				}
				columnClearSeriesData.add(Double.parseDouble(sumClearElectricityStr));
			}
		}
		
		
		//售电申报电量表格
		Map<String,Object> saleDeclareTableMap = new HashMap<String, Object>();
		Vector<Vector<Object>> saleDeclareChartData = new Vector<Vector<Object>>();
		//售电申报电量表格
		result.put("saleDeclareTableData", saleDeclareTableMap);
		//售电申报电量饼状图
		result.put("saleDeclareChartData", saleDeclareChartData);
		
		Map<String,Object> saleClearTableData = new HashMap<String, Object>();
		Vector<Vector<Object>> saleClearChartData = new Vector<Vector<Object>>();
		//售电交易电量表格
		result.put("saleClearTableData", saleClearTableData);
		//售电交易电量饼状图
		result.put("saleClearChartData", saleClearChartData);
		
		if(sumElectricity!= null  && sumElectricity.get("sale")>0 && saleMap != null && saleMap.size()>0){
			if(saleMap.size() > tableSize ){
				tableSize = saleMap.size();
			}
			int index = 1;
			int clearIndex = 1;
			
			double sumPercent = 0;
			double sumClearPercent = 0;
			
			for(Entry<String,  List<DeclarePo>> entry : saleMap.entrySet()){
				DeclarePo declarePo = null;
				if(CommonUtil.ifEmpty_List(entry.getValue())){
					//一个省只有一个单子
					declarePo = entry.getValue().get(0);
					
					columnCategoriesData.add(declarePo.getArea());
					String sumElectricityStr = CommonUtil.toFix(declarePo.getSumElectricity());
					columnDeclareSeriesData.add(Double.parseDouble(sumElectricityStr));
					saleDeclareTableMap.put(entry.getKey(), sumElectricityStr);
					if(index == saleMap.size()){
						Vector<Object> saleSingleDeclareChartData = new Vector<Object>();
						saleSingleDeclareChartData.add(declarePo.getArea());
						saleSingleDeclareChartData.add(100-sumPercent);
						saleDeclareChartData.add(saleSingleDeclareChartData);
					}else{
						double percent = declarePo.getSumElectricity()*100/sumElectricity.get("sale");
						double newPercent = Double.parseDouble(CommonUtil.toFix(percent));
						sumPercent+= newPercent;
						Vector<Object> saleSingleDeclareChartData = new Vector<Object>();
						saleSingleDeclareChartData.add(declarePo.getArea());
						saleSingleDeclareChartData.add(newPercent);
						saleDeclareChartData.add(saleSingleDeclareChartData);
					}
				}
				ResultPo resultPo = saleClearMap.get(entry.getKey());
				String sumClearElectricityStr = "0";
				
				if(resultPo != null){
					//一个省只有一个单子
					saleClearSumElectricity += resultPo.getSumQ()/4;
					sumClearElectricityStr = CommonUtil.toFix(resultPo.getSumQ()/4);
					saleClearTableData.put(entry.getKey(), sumClearElectricityStr);
					if(clearIndex == saleMap.size()){
						Vector<Object> saleSingleClearChartData = new Vector<Object>();
						saleSingleClearChartData.add(resultPo.getArea());
						//saleSingleClearChartData.add(100-sumClearPercent);
						saleSingleClearChartData.add(Double.parseDouble(CommonUtil.toFix((100-sumClearPercent))));
						
						saleClearChartData.add(saleSingleClearChartData);
					}else{
						double percent = saleClearElectricity == 0?0:resultPo.getSumQ()*100/saleClearElectricity;
						double newPercent = Double.parseDouble(CommonUtil.toFix(percent));
						sumClearPercent+= newPercent;
						Vector<Object> saleSingleClearChartData = new Vector<Object>();
						saleSingleClearChartData.add(resultPo.getArea());
						saleSingleClearChartData.add(newPercent);
						saleClearChartData.add(saleSingleClearChartData);
					}
					clearIndex++;
				}
				columnClearSeriesData.add(Double.parseDouble(sumClearElectricityStr));
			}
		}
		result.put("buyClearSumElectricity", CommonUtil.toFix(buyClearSumElectricity));
		result.put("saleClearSumElectricity", CommonUtil.toFix(saleClearSumElectricity));
		
		result.put("tableSize", tableSize);
		
		/******计算通道最大成交数*******/
		List<ResultPo> clearSumList = surveyDao.getSumList(mdate);
		if(CommonUtil.ifEmpty_List(clearSumList)){
			//交易通道最大成交电量
			result.put("pathMaxClearElectricity",CommonUtil.toFix(clearSumList.get(0).getSumQ()/4));
			result.put("pathMaxClearName", clearSumList.get(0).getCorridor());
		}
		List<ResultPo> clearIntervalList = surveyDao.getMaxInterval(mdate);
		if(CommonUtil.ifEmpty_List(clearIntervalList)){
			ResultPo clearIntervalPo = clearIntervalList.get(0);
			//System.out.println(clearIntervalList.size()+"---clearIntervalPo------"+CommonUtil.objToJson(clearIntervalList));
			double maxValue = 0;
			int maxInterval = -1;
			if(clearIntervalPo != null){
			maxValue = clearIntervalPo.getH01();
			maxInterval = 1;
			Method getAttributeMethod = null;
			for (int j = 2; j <= 96; j++) {
				String getAttributeMethodName = "getH" + (j < 10 ? "0" + j : j);
				try {
					getAttributeMethod = ResultPo.class.getDeclaredMethod(getAttributeMethodName);
					Object o = getAttributeMethod.invoke(clearIntervalPo);
					if(o != null){
						double val = (Double)o;
						if(val>maxValue){
							maxValue = val;
							maxInterval = j;
						}
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
			result.put("pathMaxIntervalPower",CommonUtil.toFix(maxValue));
			result.put("pathMaxInterval", maxInterval== -1?"":DeclareUtil.getInterval(maxInterval));
		}
		LoggerUtil.log("获取当日概况数据", CommonUtil.objToJson(result),0);
		return result;
	}

	

}
