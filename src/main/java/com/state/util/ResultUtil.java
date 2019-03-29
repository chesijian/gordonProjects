package com.state.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.state.po.ResultPo;

public class ResultUtil {
	 
 	  
	/**
	 * 根据i获取96值数据
	 * 
	 * @author 车斯剑
	 * @date 2016年9月21日下午3:32:20
	 * @param po
	 * @param i
	 * @return
	 */
	public static Double getResultPoValue(ResultPo po, int i) {
		String getAttributeMethodName = "getH" + (i < 10 ? "0" + i : i);
		Method getAttributeMethod = null;
		try {
			getAttributeMethod = ResultPo.class.getDeclaredMethod(getAttributeMethodName);
			return (Double) getAttributeMethod.invoke(po);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 合并ResultPo
	 * 
	 * @author 车斯剑
	 * @date 2016年9月21日上午11:02:35
	 * @param datas
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static ResultPo combineResultPo(List<ResultPo> datas) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		ResultPo resultPo = new ResultPo();
		for (int i = 1; i < 97; i++) {
			String getAttributeMethodName = "getH" + (i < 10 ? "0" + i : i);
			String setAttributeMethodName = "setH" + (i < 10 ? "0" + i : i);
			Method getAttributeMethod = null;
			Method setAttributeMethod = null;
			Double min = 0.00;
			try {
				getAttributeMethod = ResultPo.class.getDeclaredMethod(getAttributeMethodName);
				setAttributeMethod = ResultPo.class.getDeclaredMethod(setAttributeMethodName, Double.class);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (datas.size() > 0) {
				// resultPo = new ResultPo();
				for (ResultPo po : datas) {

					try {
						min += (Double) getAttributeMethod.invoke(po);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				setAttributeMethod.invoke(resultPo, min);
			}

		}
		return resultPo;
	}
	
	/**
	 * 获取每个通道的平均电力、平均电价、累计成交电量
	 * @author 车斯剑
	 * @date 2016年12月3日上午9:47:37
	 * @param data
	 * @return
	 */
	public static Map<String, Double> getResultValueMap(Map<String,ResultPo>  data){
		Map<String, Double> result = new HashMap<String, Double>();
		double powerAvg =0; //平均电力
		double sumQuantity =0; //成交电量
		double sumPrice = 0; //电价总和
		double priceAvg = 0; //平均电价
		double dealPower =0; //累计成交电量
		double dealPrice =0; //累计成交电价
		ResultPo powerResult = data.get("电力");
		ResultPo priceResult = data.get("电价");
		
		powerAvg = powerResult.getSumQ()/4/24;
		sumQuantity =powerResult.getSumQ()/4;
		if("sale".equals(powerResult.getDrole())){
			dealPower = sumQuantity;
		}
		if("sale".equals(priceResult.getDrole())){
			
		}
		for (int i = 1; i < 97; i++) {
			String getAttributeMethodName = "getH" + (i < 10 ? "0" + i : i);
			Method getAttributeMethod = null;
			try {
				getAttributeMethod = ResultPo.class.getDeclaredMethod(getAttributeMethodName);
				try {
					double power = (Double) getAttributeMethod.invoke(powerResult)==null? 0:(Double) getAttributeMethod.invoke(powerResult);
					double price = (Double) getAttributeMethod.invoke(priceResult)==null? 0:(Double) getAttributeMethod.invoke(priceResult);
					sumPrice += power*price;
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		priceAvg = (powerResult.getSumQ() == 0? 0:sumPrice/powerResult.getSumQ());
		result.put("powerAvg", powerAvg);
		result.put("priceAvg", priceAvg);
		result.put("sumQuantity", sumQuantity);
		result.put("dealPower", dealPower);
		return result;
	}
	
}
