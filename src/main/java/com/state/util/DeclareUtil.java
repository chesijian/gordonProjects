package com.state.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.aspectj.weaver.ArrayAnnotationValue;

import com.state.enums.Enums_DeclareType;
import com.state.po.DeclareExtraPo;
import com.state.po.DeclarePo;

/**
 * 
 * @description
 * @author 大雄
 * @date 2016年8月17日下午5:45:50
 */
public class DeclareUtil {
	/**
	 * 获取某个时间属于哪个时间段
	 * @description
	 * @author 大雄
	 * @date 2016年8月24日下午2:00:46
	 * @param time
	 * @return
	 */
	public static int getIndex(String time){
		String[] strs = time.split(":");
		int i = Integer.parseInt(strs[0])*4;
		i += Integer.parseInt(strs[1])/15;
		return i;
	}
	/**
	 * 根据时段获取时间
	 * @description
	 * @author 大雄
	 * @date 2016年9月26日上午10:36:08
	 * @param time
	 * @return
	 */
	public static String getInterval(int index){
		if(index<1){
			return null;
		}else{
			int hour = index/4;
			int minute = index%4*15;
			
			return CommonUtil.decimal(2, hour)+":"+CommonUtil.decimal(2, minute);
		}
	}
	public static void main(String[] args) {
		String time = "01:00";
		System.out.println(getIndex(time));
		int index = 9;
		System.out.println(getInterval(index));
	}
	
	/**
	 * 根据申报单报价区间，解析数据
	 * @description
	 * @author 大雄
	 * @date 2016年8月24日下午2:02:23
	 */
	public static Map<DeclarePo,TreeMap<Integer,TreeMap<Float,Float>>> analysisDec(List<DeclarePo> dslist){
		//Map<String,TreeMap<Integer,TreeMap<Float,Float>>> result = new HashMap<String,TreeMap<Integer,TreeMap<Float,Float>>>();
		//Map<String,Map<String,Object>> result = new HashMap<String,Map<String,Object>>();
		Map<DeclarePo,TreeMap<Integer,TreeMap<Float,Float>>> result = new HashMap<DeclarePo,TreeMap<Integer,TreeMap<Float,Float>>>();
		
		if(CommonUtil.ifEmpty_List(dslist)){
			List<DeclareExtraPo> data = null;
			TreeMap<Integer,TreeMap<Float,Float>> offerMap = null;
			//TreeMap<Float,Float> curve = null;
			//Map<String,Object> offerMap = null;
			for(DeclarePo po : dslist){
				boolean isBuy = false;
				if(po.getDrloe().equals(Enums_DeclareType.BUY)){
					isBuy = true;
				}
				data = po.getDeclareExtras();
				if(CommonUtil.ifEmpty_List(data)){
					Map<String,Object> resultData = analysisDecExtraData1(data, isBuy,false);
					Object o = resultData.get("treeData");
					if(o != null){
						offerMap = (TreeMap<Integer,TreeMap<Float,Float>>)o;
					}
					
					result.put(po, offerMap);
				}
				
			}
		}
		return result;
	}
	
	/**
	 * 解析单个单据的96时段报价曲线
	 * @description
	 * @author 大雄
	 * @date 2016年8月25日下午4:17:16
	 * @param data
	 * @param isBuy 如果是true则按照价格升序，如果是卖方则按照价格降序
	 * @param isReverse 是否反转 ，如果是 ，则map的key是电力，如果是否，key是电价
	 * @return
	 */
	public static TreeMap<Integer,TreeMap<Float,Float>> analysisDecExtraData(List<DeclareExtraPo> data,boolean isBuy,boolean isReverse){
		TreeMap<Integer,TreeMap<Float,Float>> offerMap = null;
		TreeMap<Float,Float> curve = null;
		String startTime,endTime;
		offerMap = new TreeMap<Integer,TreeMap<Float,Float>>(new Comparator<Integer>() {
			/*
			 * int compare(Object o1, Object o2) 返回一个基本类型的整型，
			 * 返回负数表示：o1 小于o2， 返回0 表示：o1和o2相等， 返回正数表示：o1大于o2。
			 */
			public int compare(Integer i1, Integer i2) {

				// 指定排序器按照顺序排列
				return i2 > i1  ? -1 : i1 == i2 ? 0 : 1;
			}
		});
		
		//offerMap = new HashMap<String,Object>();
		for(DeclareExtraPo epo : data){
			startTime = epo.getStartTime();
			endTime  = epo.getEndTime();
			for(int i = DeclareUtil.getIndex(startTime);i<=DeclareUtil.getIndex(endTime);i++){
				if(offerMap.containsKey(i)){
					curve = offerMap.get(i);
				}else{
					//如果是买方根据价格降序
					if(isBuy){
						curve = new TreeMap<Float,Float>(new Comparator<Float>() {
							public int compare(Float i1, Float i2) {
								// 指定排序器按照顺序排列
								return i2 > i1  ? -1 : i1 == i2 ? 0 : 1;
							}
						});
					}else{
						//如果是卖方则价格升序
						curve = new TreeMap<Float,Float>(new Comparator<Float>() {
							public int compare(Float i1, Float i2) {
								// 指定排序器按照顺序排列
								return i1 > i2  ? -1 : i1 == i2 ? 0 : 1;
							}
						});
					}
					
					offerMap.put(i, curve);
				}
				if(isReverse){
					curve.put( epo.getPower(),epo.getPrice());
				}else{
					Float tempKey = null;
					for(Entry<Float,Float> t : curve.entrySet()){
						if((t.getKey() -epo.getPrice()) == 0 ){
							tempKey = t.getKey();
						}
					}
					if(tempKey != null){
						curve.put( tempKey,epo.getPower()+curve.get(tempKey));
					}else{
						curve.put( epo.getPrice(),epo.getPower());
						
					}
				}
				
			}
		}
		//遍历所有时段，如果有些时段没有报价，默认曲线段数为1，电力和报价默认0
		for(int i = 1;i<97;i++){
			if(!offerMap.containsKey(i)){
				 curve = new TreeMap<Float,Float>();
				 curve.put(0f, 0f);
				offerMap.put(i, curve);
			}
		}
		return offerMap;
	}
	
	/**
	 * 对于多个电力和电价重复的要合并相同电价要合并,并且计算状态，如果有时段有报价则状态未0，如果没有报价则为1
	 * @description
	 * @author 大雄
	 * @date 2016年9月21日下午12:08:03
	 * @param data
	 * @param isBuy
	 * @param isReverse
	 * @return
	 */
	public static Map<String,Object> analysisDecExtraData1(List<DeclareExtraPo> data,boolean isBuy,boolean isReverse){
		TreeMap<Integer,TreeMap<Float,Float>> offerMap = null;
		TreeMap<Float,Float> curve = null;
		String startTime,endTime;
		offerMap = new TreeMap<Integer,TreeMap<Float,Float>>(new Comparator<Integer>() {
			/*
			 * int compare(Object o1, Object o2) 返回一个基本类型的整型，
			 * 返回负数表示：o1 小于o2， 返回0 表示：o1和o2相等， 返回正数表示：o1大于o2。
			 */
			public int compare(Integer i1, Integer i2) {
				//Double i1 = Double.parseDouble(s1);
				//Double i2 = Double.parseDouble(s2);
				

				// 指定排序器按照顺序排列
				return i2 > i1  ? -1 : i1 == i2 ? 0 : 1;
			}
		});
		Map<Integer,Integer> statusData = new HashMap<Integer,Integer>();
		//offerMap = new HashMap<String,Object>();
		for(DeclareExtraPo epo : data){
			startTime = epo.getStartTime();
			endTime  = epo.getEndTime();
			for(int i = DeclareUtil.getIndex(startTime);i<=DeclareUtil.getIndex(endTime);i++){
				//String key = String.valueOf(i);
				//System.out.println("i==="+i);
				statusData.put(i, 1);
				if(offerMap.containsKey(i)){
					curve = offerMap.get(i);
				}else{
					//如果是买方根据价格降序
					if(isBuy){
						curve = new TreeMap<Float,Float>(new Comparator<Float>() {
							public int compare(Float i1, Float i2) {
								//Double i1 = Double.parseDouble(s1);
								//Double i2 = Double.parseDouble(s2);
								// 指定排序器按照顺序排列
								return i2 > i1  ? -1 : i1 == i2 ? 0 : 1;
							}
						});
					}else{
						//如果是卖方则价格升序
						curve = new TreeMap<Float,Float>(new Comparator<Float>() {
							public int compare(Float i1, Float i2) {
								//Double i1 = Double.parseDouble(s1);
								//Double i2 = Double.parseDouble(s2);
								// 指定排序器按照顺序排列
								return i1 > i2  ? -1 : i1 == i2 ? 0 : 1;
							}
						});
					}
					offerMap.put(i, curve);
				}
				
				if(isReverse){
					//String subKey = String.valueOf(epo.getPower());
					curve.put( epo.getPower(),epo.getPrice());
				}else{
					//String subKey = String.valueOf(epo.getPrice());
					//if(i == 1){
						//System.out.println(epo.getPrice()+"==="+curve.containsKey(epo.getPrice())+"=="+CommonUtil.objToJson(curve));
					//}
					Float tempKey = null;
					for(Entry<Float,Float> t : curve.entrySet()){
						if((t.getKey() -epo.getPrice()) == 0 ){
							tempKey = t.getKey();
						}
					}
					if(tempKey != null){
						curve.put( tempKey,epo.getPower()+curve.get(tempKey));
					}else{
						curve.put( epo.getPrice(),epo.getPower());
						
					}
					
				}
				
			}
		}
		//遍历所有时段，如果有些时段没有报价，默认曲线段数为1，电力和报价默认0
		for(int i = 1;i<97;i++){
			//String key = String.valueOf(i);
			if(!offerMap.containsKey(i)){
				 curve = new TreeMap<Float,Float>();
				 curve.put(0f, 0f);
				offerMap.put(i, curve);
				statusData.put(i, 2);
			}
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("treeData", offerMap);
		result.put("statusData", statusData);
		
		return result;
	}
	
	/**
	 * 比较两个时段的电力和电价是否完全相同
	 * @description
	 * @author 大雄
	 * @date 2016年9月9日下午5:12:27
	 * @param prevEnt
	 * @param nowEnt
	 * @return
	 */
	public static boolean isSingleSame(TreeMap<Float, Float> prevEnt ,TreeMap<Float, Float> nowEnt){
    	//遍历当前时段的报价，比较上一个时段的报价，看是否相同
    	float nowPower,nowPrice,prevPower,prevPrice;
		if(prevEnt.size() == nowEnt.size()){
			boolean isKeySame = false;
			for(Map.Entry<Float, Float > offerEnt : nowEnt.entrySet()){
				nowPower = offerEnt.getKey();
				nowPrice = offerEnt.getValue();
				//System.out.println(nowPower+"==nowEnt=="+nowPrice);
				boolean isSingleSame = true;
				
				//System.out.println(offerEnt.getValue()+"=="+offerEnt.getKey()+"=="+prevEnt.get(offerEnt.getKey().floatValue()));
				for(Map.Entry<Float, Float > preOfferEnt : prevEnt.entrySet()){
					prevPower = preOfferEnt.getKey();
					prevPrice = preOfferEnt.getValue();
					//System.out.println(prevPower+"==prevEnt=="+prevPrice);
					//System.out.println((nowPower-prevPower)+"=="+(nowPrice-prevPrice));
					//如果key相等
					if((nowPower-prevPower) == 0){
						isKeySame = true;
						//如果可以相等，值不懂，则肯定不相同
						if((nowPrice-prevPrice) != 0){
							return false;
						}
					}
					
				};
				//System.out.println("isSingleSame==="+isSingleSame);
				if(isKeySame == false){
					return false;
				}
			};
			
			return true;
		}else{
			return false;
		}
    }
	
	/**
	 * 比较两个时段的电力和电价是否完全相同
	 * @description
	 * @author 大雄
	 * @date 2016年9月9日下午5:12:27
	 * @param prevEnt
	 * @param nowEnt
	 * @return
	 */
	public static boolean isSingleSame1(TreeMap<String, Float> prevEnt ,TreeMap<String, Float> nowEnt){
    	//遍历当前时段的报价，比较上一个时段的报价，看是否相同
		String nowPrice,prevPrice;
    	float nowPower,prevPower;
		if(prevEnt.size() == nowEnt.size()){
			boolean isKeySame = false;
			for(Map.Entry<String, Float > offerEnt : nowEnt.entrySet()){
				nowPrice = offerEnt.getKey();
				nowPower = offerEnt.getValue();
				//System.out.println(nowPower+"==nowEnt=="+nowPrice);
				boolean isSingleSame = true;
				
				//System.out.println(offerEnt.getValue()+"=="+offerEnt.getKey()+"=="+prevEnt.get(offerEnt.getKey().floatValue()));
				for(Map.Entry<String, Float > preOfferEnt : prevEnt.entrySet()){
					prevPrice = preOfferEnt.getKey();
					prevPower = preOfferEnt.getValue();
					//System.out.println(prevPower+"==prevEnt=="+prevPrice);
					//System.out.println((nowPower-prevPower)+"=="+(nowPrice-prevPrice));
					//如果key相等
					if(nowPrice.equals(prevPrice)){
						isKeySame = true;
						//如果可以相等，值不懂，则肯定不相同
						if((nowPower-nowPower) != 0){
							return false;
						}
					}
					
				};
				//System.out.println("isSingleSame==="+isSingleSame);
				if(isKeySame == false){
					return false;
				}
			};
			
			return true;
		}else{
			return false;
		}
    }
	
	/**
	 * 获取当天对应的交易日期
	 * @description
	 * @author 大雄
	 * @date 2016年10月17日上午10:58:21
	 * @return
	 */
	public static String getMdate(){
		Date nowDate = new Date();
		Date preDate = TimeUtil.getNextDay(nowDate);
		return TimeUtil.dateToStr(preDate);
	}
	
	/**
	 * 计算每个单的总电量,总电力，总电价，平均电力，平均电量，平均电价
	 * @description
	 * @author 大雄
	 * @date 2016年10月18日下午3:25:18
	 * @param po
	 * @return
	 */
	public static DeclarePo calculate(DeclarePo po){
		//System.out.println("--------"+po.getArea());
		if(CommonUtil.ifEmpty_List(po.getDeclareExtras())){
			for(DeclareExtraPo temp : po.getDeclareExtras()){
				int startIndex = getIndex(temp.getStartTime());
				int endIndex = getIndex(temp.getEndTime());
				//System.out.println(po.getSumPower()+"====="+(endIndex-startIndex+1)+"==="+temp.getPower());
				po.setSumPower(po.getSumPower()+(endIndex-startIndex+1)*temp.getPower());
				po.setSumPrice(po.getSumPrice()+((endIndex-startIndex+1)*temp.getPower()*temp.getPrice())/4);
			}
		}
		po.setSumElectricity(po.getSumPower()/4);
		
		po.setAvgPower(po.getSumElectricity()/24);
		po.setAvgElectricity(po.getAvgPower()/4);
		po.setAvgPrice(po.getSumPrice()/po.getSumElectricity());
		return po;
	}
	
	/**
	 * 获取地区交易单申报数据
	 * @author 车斯剑
	 * @date 2016年12月16日上午9:59:53
	 * @param po
	 * @return
	 */
	public static Map<String,List<Map<String, Float>>> getDeclareTradeData(DeclarePo po){
		Map<Integer, Integer> statusdata = new TreeMap<Integer, Integer>(new Comparator<Integer>() {
			
			public int compare(Integer i1, Integer i2) {
				// 指定排序器按照顺序排列
				return i2 > i1 ? -1 : i1 == i2 ? 0 : 1;
			}
		});
		boolean isBuy = false;
		if (po.getDrloe().equals(Enums_DeclareType.BUY)) {
			isBuy = true;

		}
		TreeMap<Integer, TreeMap<Float, Float>> treeMap = DeclareUtil.analysisDecExtraData(po.getDeclareExtras(), isBuy, false);
		int offset = 1;
		int index = 0;
		TreeMap<Float, Float> prevEnt = null;
		TreeMap<Float, Float> nowEnt = null;
		// 遍历判断每个时段的电力电价曲线是否相同，相同的用同一个颜色
		for (Map.Entry<Integer, TreeMap<Float, Float>> intEnt : treeMap.entrySet()) {
			index++;
			if (index == 1) {
				statusdata.put(index, offset);
			} else {
				boolean isSame = false;
				int i = 1;
				nowEnt = intEnt.getValue();

				for (i = 1; i < index; i++) {
					// 获取上一个比较
					prevEnt = treeMap.get(i);
					boolean isSingleSame = DeclareUtil.isSingleSame(prevEnt, nowEnt);
					if (isSingleSame) {
						isSame = true;
						break;
					}
				}
				if (isSame) {
					statusdata.put(index, statusdata.get(i));
				} else {
					statusdata.put(index, ++offset);
				}
			}
		}
	
		//StringBuilder tSb = new StringBuilder();
		Map<String,List<Map<String, Float>>> declareDataMap = new HashMap<String,List<Map<String, Float>>>();
		int startIndex = 1;
		int endIndex;
		int intervalIndex = 1;
		for (Entry<Integer, Integer> entry : statusdata.entrySet()) {
			if (intervalIndex == 1) {
				startIndex = 1;
			} else {
				int valuePre = statusdata.get(startIndex);
				int valueNow = entry.getValue();
				if ((valuePre - valueNow) != 0) {
					endIndex = intervalIndex - 1;
					TreeMap<Float, Float> data = treeMap.get(startIndex);
					
					boolean isNull = true;
					//StringBuilder dataStr = new StringBuilder();
					Map<String, Float> dataMap = null;
					List<Map<String, Float>> dataList = new ArrayList<Map<String,Float>>();
					for (Entry<Float, Float> extraEntry : data.entrySet()) {
						if (extraEntry.getKey() == 0 && extraEntry.getValue() == 0) {
							continue;
						}
						isNull = false;
						dataMap = new HashMap<String, Float>();
						dataMap.put("电力",extraEntry.getValue());
						dataMap.put("电价",extraEntry.getKey());
						dataList.add(dataMap);
						//dataStr.append("电力:" + extraEntry.getValue() + "MW,电价:" + extraEntry.getKey() + "元/MWh;");
					}
					if (!isNull) {
						//tSb.append(DeclareUtil.getInterval(startIndex) + "至" + DeclareUtil.getInterval(endIndex) + ": ");
						//tSb.append(dataStr.toString());
						String timeStr = DeclareUtil.getInterval(startIndex) + "a" + DeclareUtil.getInterval(endIndex);
						declareDataMap.put(timeStr, dataList);
					}
					startIndex = intervalIndex;
				}
			}
			intervalIndex++;
		}
		if (startIndex < intervalIndex) {
			endIndex = intervalIndex - 1;
			TreeMap<Float, Float> data = treeMap.get(startIndex);
			boolean isNull = true;
			//StringBuilder dataStr = new StringBuilder();
			Map<String, Float> dataMap = null;
			List<Map<String, Float>> dataList = new ArrayList<Map<String,Float>>();
			for (Entry<Float, Float> extraEntry : data.entrySet()) {
				if (extraEntry.getKey() == 0 && extraEntry.getValue() == 0) {
					continue;
				}
				isNull = false;
				dataMap = new HashMap<String, Float>();
				dataMap.put("电力",extraEntry.getValue());
				dataMap.put("电价",extraEntry.getKey());
				dataList.add(dataMap);
				//dataStr.append("电力:" + extraEntry.getValue() + "MW,电价:" + extraEntry.getKey() + "元/MWh;");

			}
			if (!isNull) {
				//tSb.append(DeclareUtil.getInterval(startIndex) + "至" + DeclareUtil.getInterval(endIndex) + ": ");
				//tSb.append(dataStr.toString());
				String timeStr = DeclareUtil.getInterval(startIndex) + "a" + DeclareUtil.getInterval(endIndex);
				declareDataMap.put(timeStr, dataList);
			}
			
		}
		return declareDataMap;
	}
	
}
