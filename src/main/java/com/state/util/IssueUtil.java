package com.state.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.state.enums.Enums_DeclareType;
import com.state.po.DeclarePo;
import com.state.po.ResultPo;
import com.state.po.TransTielinePo;

/**
 * 
 * @description
 * @author 大雄
 * @date 2016年9月25日下午8:43:00
 */
public class IssueUtil {
	
	public static Map<Integer,Integer> getStatusData(ResultPo po){
		
		Map<Integer,Integer> data = new TreeMap<Integer,Integer>(new Comparator<Integer>() {
			/*
			 * int compare(Object o1, Object o2) 返回一个基本类型的整型， 返回负数表示：o1
			 * 小于o2， 返回0 表示：o1和o2相等， 返回正数表示：o1大于o2。
			 */
			public int compare(Integer i1, Integer i2) {

				// 指定排序器按照顺序排列
				return i2 > i1 ? -1 : i1 == i2 ? 0 : 1;
			}
		});
		int i = 1;
		data.put(1, i);
		for (int j = 2; j <= 96; j++) {
			String getAttributeMethodName = "getH" + CommonUtil.decimal(2, j);
			Method getAttributeMethod = null;
			try {
				double value = 0d;
				getAttributeMethod = ResultPo.class.getDeclaredMethod(getAttributeMethodName);
				//System.out.println(po+"==="+getAttributeMethod);
				Object o = getAttributeMethod.invoke(po);
				if(o != null){
					value = (Double)o;
				}
				boolean isSample = false;
				int m;
				for (m = 1; m <j; m++) {
					String getAttributeMethodNameT = "getH" + CommonUtil.decimal(2, m);
					Method getAttributeMethodT = ResultPo.class.getDeclaredMethod(getAttributeMethodNameT);
					double temp = 0f;
					Object ot = getAttributeMethodT.invoke(po);
					if(o != null){
						temp = (Double)ot;
					}
					if((temp - value) == 0){
						isSample = true;
						break;
					}
				}
				if(isSample){
					data.put(j, data.get(m));
				}else{
					data.put(j, ++i);
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return data;
	}
	
	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年12月22日上午10:45:38
	 * @param resultValue
	 * @return
	 */
	public static Map<Integer,Integer> getStatusDataNew(Map<String, ResultPo> resultValue){
		
		Map<Integer,Integer> data = new TreeMap<Integer,Integer>(new Comparator<Integer>() {
			/*
			 * int compare(Object o1, Object o2) 返回一个基本类型的整型， 返回负数表示：o1
			 * 小于o2， 返回0 表示：o1和o2相等， 返回正数表示：o1大于o2。
			 */
			public int compare(Integer i1, Integer i2) {

				// 指定排序器按照顺序排列
				return i2 > i1 ? -1 : i1 == i2 ? 0 : 1;
			}
		});
		int i = 1;
		data.put(1, i);
		
		ResultPo power = resultValue.get("电力");
		ResultPo price = resultValue.get("电价");
		for (int j = 2; j <= 96; j++) {
			String getAttributeMethodName = "getH" + CommonUtil.decimal(2, j);
			Method getAttributeMethod = null;
			try {
				double value = 0d;
				double value2 = 0d;
				getAttributeMethod = ResultPo.class.getDeclaredMethod(getAttributeMethodName);
				//System.out.println(po+"==="+getAttributeMethod);
				Object o = getAttributeMethod.invoke(power);
				Object a = getAttributeMethod.invoke(price);
				if(o != null){
					value = (Double)o;
				}
				if(a != null){
					value2 = (Double)a;
				}
				boolean isSample = false;
				int m;
				for (m = 1; m <j; m++) {
					String getAttributeMethodNameT = "getH" + CommonUtil.decimal(2, m);
					Method getAttributeMethodT = ResultPo.class.getDeclaredMethod(getAttributeMethodNameT);
					double temp = 0f;
					double temp2 = 0f;
					Object ot = getAttributeMethodT.invoke(power);
					Object ot2 = getAttributeMethodT.invoke(price);
					if(ot != null){
						temp = (Double)ot;
					}
					if(ot2 != null){
						temp2 = (Double)ot2;
					}
					if((temp - value) == 0 && (temp2-value2)==0){
						isSample = true;
						break;
					}
				}
				if(isSample){
					data.put(j, data.get(m));
				}else{
					data.put(j, ++i);
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return data;
	}
	
	public static Map<Integer,Integer> getStatusDataForExecute(TransTielinePo po){
		
		Map<Integer,Integer> data = new TreeMap<Integer,Integer>(new Comparator<Integer>() {
			
			public int compare(Integer i1, Integer i2) {

				// 指定排序器按照顺序排列
				return i2 > i1 ? -1 : i1 == i2 ? 0 : 1;
			}
		});
		int i = 1;
		data.put(1, i);
		for (int j = 2; j <= 96; j++) {
			String getAttributeMethodName = "getH" + CommonUtil.decimal(2, j);
			Method getAttributeMethod = null;
			try {
				double value = 0d;
				getAttributeMethod = TransTielinePo.class.getDeclaredMethod(getAttributeMethodName);
				Object o = getAttributeMethod.invoke(po);
				if(o != null){
					value = (Double)o;
				}
				boolean isSample = false;
				int m;
				for (m = 1; m <j; m++) {
					String getAttributeMethodNameT = "getH" + CommonUtil.decimal(2, m);
					Method getAttributeMethodT = TransTielinePo.class.getDeclaredMethod(getAttributeMethodNameT);
					double temp = 0f;
					Object ot = getAttributeMethodT.invoke(po);
					if(o != null){
						temp = (Double)ot;
					}
					if((temp - value) == 0){
						isSample = true;
						break;
					}
				}
				if(isSample){
					data.put(j, data.get(m));
				}else{
					data.put(j, ++i);
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

		}
		return data;
	}
	
	/**
	 * 获取地区交易单成交结果数据
	 * @author 车斯剑
	 * @date 2016年12月16日上午9:57:01
	 * @param resultMap
	 * @return
	 */
	public static Map<String,Map<String, Map<String,Double>>> getdealTradeData(Map<String, Map<String, ResultPo>> resultMap){
		//StringBuilder dealStr = new StringBuilder();
		int startIndex = 1;
		int endIndex;
		int intervalIndex = 1;
		Map<String,Map<String, Map<String,Double>>>  dealMap = new HashMap<String, Map<String,Map<String,Double>>>();
		for (Entry<String, Map<String, ResultPo>> entry : resultMap.entrySet()) {
			//dealStr.append("<div class='headline'>"+entry.getKey()+"</div>");
			Map<String, Map<String,Double>> dataMap = new HashMap<String, Map<String,Double>>();
			dealMap.put(entry.getKey(), dataMap);
			Map<Integer, Integer> statusData = IssueUtil.getStatusDataNew(entry.getValue());
			System.out.println("statusData==="+CommonUtil.objToJson(statusData));
			for (Entry<String, ResultPo> ent : entry.getValue().entrySet()) {
				// 获取相邻时刻是否相等的状态
				ResultPo rpo = ent.getValue();
			
				//Map<Integer, Integer> statusData = IssueUtil.getStatusData(rpo);
				startIndex = 1;
				endIndex = 0;
				intervalIndex = 1;
				
				Map<String,Double> values = new HashMap<String, Double>();
				dataMap.put(rpo.getDtype(), values);
				for (Entry<Integer, Integer> statEntry : statusData.entrySet()) {
					if (intervalIndex == 1) {
						startIndex = 1;
					} else {
						int valuePre = statusData.get(startIndex);
						int valueNow = statEntry.getValue();

						if ((valuePre - valueNow) != 0) {
							endIndex = intervalIndex - 1;
							String getAttributeMethodName = "getH" + CommonUtil.decimal(2, startIndex);
							Method getAttributeMethod = null;
							double value = 0d;
							try {

								getAttributeMethod = ResultPo.class.getDeclaredMethod(getAttributeMethodName);
								Object o = getAttributeMethod.invoke(rpo);
								if (o != null) {
									value = (Double) o;
								}
								
								if (value != 0) {
									//dealStr.append(DeclareUtil.getInterval(startIndex) + "至" + DeclareUtil.getInterval(endIndex) + ": ");
									//dealStr.append(value + dtype);
									String timeStr = DeclareUtil.getInterval(startIndex) +"a"+DeclareUtil.getInterval(endIndex);
									values.put(timeStr, value);
								}
								
								startIndex = intervalIndex;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					intervalIndex++;
				}
				if (startIndex < intervalIndex) {
					endIndex = intervalIndex - 1;
					String getAttributeMethodName = "getH" + CommonUtil.decimal(2, startIndex);
					Method getAttributeMethod = null;
					double value = 0d;
					try {

						getAttributeMethod = ResultPo.class.getDeclaredMethod(getAttributeMethodName);
						Object o = getAttributeMethod.invoke(rpo);
						if (o != null) {
							value = (Double) o;
						}
						if (value != 0) {
							//dealStr.append(DeclareUtil.getInterval(startIndex) + "至" + DeclareUtil.getInterval(endIndex) + ": ");
							//dealStr.append(value + dtype);
							String timeStr = DeclareUtil.getInterval(startIndex) +"a"+DeclareUtil.getInterval(endIndex);
							values.put(timeStr, value);
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				//dealStr.append("</div>");
			}
		}
		
		return dealMap;
	}
	
	/**
	 * 获取地区交易单执行结果数据
	 * @author 车斯剑
	 * @date 2016年12月16日上午9:56:30
	 * @param executeData
	 * @return
	 */
	public static Map<String,Map<String,String>> getExecuteTradeData(List<TransTielinePo> executeData){
		//StringBuilder executeStr = new StringBuilder();
		Map<String,Map<String,String>> executeMap = new HashMap<String, Map<String,String>>();
		int startIndex = 1;
		int endIndex;
		int intervalIndex = 1;
		if (executeData != null) {
			// 获取相邻时刻是否相等的状态
			for (TransTielinePo executePo : executeData) {
				Map<Integer, Integer> statusData = IssueUtil.getStatusDataForExecute(executePo);
				startIndex = 1;
				endIndex = 0;
				intervalIndex = 1;
				//executeStr.append("<div class='headline'>"+executePo.getTransCorridorName()+"-"+executePo.getTielineName()+"</div>");
				//executeStr.append("<div>电量");
				Map<String,String> dataValue = new HashMap<String, String>();
				executeMap.put(executePo.getTransCorridorName()+"-"+executePo.getTielineName(), dataValue);
				for (Entry<Integer, Integer> entry : statusData.entrySet()) {
					if (intervalIndex == 1) {
						startIndex = 1;
					} else {
						int valuePre = statusData.get(startIndex);
						int valueNow = entry.getValue();

						if ((valuePre - valueNow) != 0) {
							endIndex = intervalIndex - 1;

							String getAttributeMethodName = "getH" + CommonUtil.decimal(2, startIndex);
							Method getAttributeMethod = null;
							double value = 0d;
							String valueStr = null;
							try {

								getAttributeMethod = TransTielinePo.class.getDeclaredMethod(getAttributeMethodName);
								Object o = getAttributeMethod.invoke(executePo);
								if (o != null) {
									value = (Double) o;
								}

								if (value != 0) {
									valueStr = CommonUtil.toFix(value / 4);
									dataValue.put(DeclareUtil.getInterval(startIndex) + "-" + DeclareUtil.getInterval(endIndex), valueStr);
								}
								//executeStr.append(DeclareUtil.getInterval(startIndex) + "至" + DeclareUtil.getInterval(endIndex) + ": ");
								//executeStr.append(valueStr + "MWh");
								
								startIndex = intervalIndex;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					intervalIndex++;
				}
				if (startIndex < intervalIndex) {
					endIndex = intervalIndex - 1;
					String getAttributeMethodName = "getH" + CommonUtil.decimal(2, startIndex);
					Method getAttributeMethod = null;
					double value = 0d;
					try {
						String valueStr = null;
						getAttributeMethod = TransTielinePo.class.getDeclaredMethod(getAttributeMethodName);
						Object o = getAttributeMethod.invoke(executePo);
						if (o != null) {
							value = (Double) o;
						}
						if (value != 0) {
							valueStr = CommonUtil.toFix(value / 4);
							//executeStr.append(DeclareUtil.getInterval(startIndex) + "至" + DeclareUtil.getInterval(endIndex) + ": ");
							//executeStr.append(valueStr + "MWh");
							dataValue.put(DeclareUtil.getInterval(startIndex) + "-" + DeclareUtil.getInterval(endIndex), valueStr);
						}
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				//executeStr.append("</div>");
			}
			
		}
		
		return executeMap;
	}
	
}
