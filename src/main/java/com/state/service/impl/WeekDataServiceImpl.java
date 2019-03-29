package com.state.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.state.dao.WeekDataDao;
import com.state.po.WeekDataPo;
import com.state.service.IDeclareService;
import com.state.service.IWeekDataService;
import com.state.service.IssueService;
import com.state.service.LineLimitService;
import com.state.util.CommonUtil;
import com.state.util.TimeUtil;

@Service
public class WeekDataServiceImpl implements IWeekDataService {

	@Autowired
	private WeekDataDao weekDataDao;
	@Autowired
	private LineLimitService lineLimitService;
	@Autowired
	private IDeclareService declareService;
	@Autowired
	private IssueService issueService;
	
	
	public WeekDataPo getWeekDataByTimeAndWeek(String startTime, String endTime) {
		return weekDataDao.getWeekDataByTimeAndWeek(startTime,endTime);
	}

	
	public void insertWeekData(WeekDataPo po) {
		weekDataDao.insertWeekData(po);
	}


	public void updateWeekData(WeekDataPo po) {
		weekDataDao.updateWeekData(po);
	}


	/**
	 * 组织数据到WeekDataPo
	 * @author 车斯剑
	 * @date 2016年10月20日下午5:00:37
	 * @return
	 */
	public WeekDataPo getWeekData(String startTime,String endTime){
		WeekDataPo week = new WeekDataPo();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	    long endDays = 0;
	    long startDays =0;
		try {
			endDays = df.parse(endTime).getTime();
			startDays = df.parse(startTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int days = (int) ((endDays - startDays)/(1000 * 60 * 60 * 24)+1);
		week.setStartTime(startTime);
		week.setEndTime(endTime);
		week.setWeekNum(TimeUtil.getWeekInYear(endTime));
		week.setDays(days);
		//获取图片，及可用容量数据
		Map<String,Object> picAndAbleData = lineLimitService.getPictureAndAblePowerData(startTime,endTime);
		int lineSize = (Integer) picAndAbleData.get("lineSize");
		if(picAndAbleData!=null){
			week.setAblePower((Double)picAndAbleData.get("ablePower"));
			week.setAlitAvg(Double.valueOf(CommonUtil.toFix((Double)picAndAbleData.get("alitTotl")/(days*lineSize))));
			week.setPictureData(JSONObject.toJSONString(picAndAbleData.get("picData")));;
		}
		
		//获取申报数据
		Map<String,Object> declareData = declareService.getListAndDataByMdateInterval(startTime, endTime);
		
		if(declareData!=null){
			
				week.setBuySumElectricity(declareData.get("buySumElectricity") == null?0:(Double)declareData.get("buySumElectricity"));
			
			if(declareData.get("saleSumElectricity")!=null){
				week.setSaleSumElectricity(declareData.get("saleSumElectricity") == null?0:(Double)declareData.get("saleSumElectricity"));
			}
			week.setBuySum(declareData.get("buySum")==null?0:(Integer)declareData.get("buySum"));
			week.setSaleSum(declareData.get("saleSum")==null?0:(Integer)declareData.get("saleSum"));
			week.setBuyAvgPower(declareData.get("buyAvgPower")==null?0:Double.valueOf(CommonUtil.toFix((Double)declareData.get("buyAvgPower"))));
			week.setBuyAvgPrice(declareData.get("buyAvgPrice")==null?0:Double.valueOf(CommonUtil.toFix((Double)declareData.get("buyAvgPrice"))));
			week.setSaleAvgPower(declareData.get("saleAvgPower")==null?0:Double.valueOf(CommonUtil.toFix((Double)declareData.get("saleAvgPower"))));
			week.setSaleAvgPrice(declareData.get("saleAvgPrice")==null?0:Double.valueOf(CommonUtil.toFix((Double)declareData.get("saleAvgPrice"))));
			Map<String, Map<String, Double>> buyAvg = (Map<String, Map<String, Double>>) declareData.get("buyAvgMap");
			Map<String, Map<String, Double>> saleAvg = (Map<String, Map<String, Double>>) declareData.get("saleAvgMap");
			week.setBuyTable(JSONObject.toJSONString(buyAvg));
			week.setSaleTable(JSONObject.toJSONString(saleAvg));
		}
		//获取成交数据	
		Map<String, Object> dealData = issueService.getDealDatasByMdateInterval(startTime,endTime);
		if(dealData!=null){
			week.setDealPower(Double.valueOf(CommonUtil.toFix((Double)dealData.get("dealPower"))));
			week.setDealPrice(Double.valueOf(CommonUtil.toFix((Double)dealData.get("dealPrice"))));
			Map<String, Map<String, Double>> dealMap = (Map<String, Map<String, Double>>) dealData.get("DealData");
			week.setDealTable(JSONObject.toJSONString(dealMap));
		}
		
		//获取执行结果数据
		//Map<String,Map<String,Double>> carryData = lineLimitService.getCarryData(startTime,endTime);
		List<Map<String,Map<String,Double>>> carryData = lineLimitService.getCarryData(startTime,endTime);
		double buyExecutePower =0; //累计执行电量
		if(carryData!=null){
			Map<String,Map<String,Double>>  executeData = carryData.get(0);
			Map<String,Map<String,Double>>  biasData = carryData.get(1);
			if(carryData!=null && executeData != null){
				for(Entry<String,Map<String,Double>> entry : executeData.entrySet()){
					Map<String, Double> value = entry.getValue();
					buyExecutePower += value.get("price");
				}
			}
			week.setExecutePower(buyExecutePower);
			week.setBiasTable(JSONObject.toJSONString(executeData));
			week.setExecuteTable(JSONObject.toJSONString(biasData));
		}
		
		return week;
	}

	


}
