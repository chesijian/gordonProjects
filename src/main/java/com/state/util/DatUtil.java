package com.state.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.state.enums.Enums_DeclareType;
import com.state.po.LineLimitPo;
import com.state.po.PathResultPo;
import com.state.po.ResultPo;
import com.state.service.impl.DeclareServiceImpl;

public class DatUtil {
	public static String FILE_NAME = "d:/temp/CBPM_国调_result_20160830.dat";
	public static float H_RANGE = 0.02f;
	public static float W_RANGE = 0.03f;

	public static void main(String[] args) {
		System.out.println("===========start============");
		//getResult(CommonUtil.getSimpleDate(), "20160816");
		//getPathResult(CommonUtil.getSimpleDate(), "20160816");
		//getLineLimit(CommonUtil.getSimpleDate(), "20160826");
		//getClearDetail(CommonUtil.getSimpleDate(), "20160816");
		//readFile();
		String fileName = "D:\\temp\\guodiao\\CBPM_BIDOFFER_江苏_20160920.dat";
		//readFile2(new File(fileName), "<BidOfferData::", "</BidOfferData");
		fileName = "D:\\temp\\CBPM_国调_result_20161223.dat";
		
		Vector<Vector<String>>  data = readFile3(new File(fileName), "<ClearResult::国调 type=全数>", "#	2	1	四川");
		System.out.println(CommonUtil.objToJson(data));
		System.out.println(data.get(0).size());
		System.out.println(data.get(1).size());
		System.out.println(data.get(2).size());
		
		
		System.out.println("===========end============");
	}
	
	public static void readFile(){
		Vector<Vector<String>> data = readFile(new File("D:/申购单数据格式.dat"), "<SheetData::国调 type=全数>", "</SheetData::国调>");
		String setAttributeMethodName = null;
		Method setAttributeMethod = null;
		ResultPo po = null;
		List<Map<String, Object>> declareData = new ArrayList<Map<String, Object>>();
		Map<String, Object> declareMap = new HashMap<String, Object>();
		DeclareServiceImpl declareService = new DeclareServiceImpl();
		for (Vector<String> unit : data) {
			String sheetName =unit.get(2); //SHEET_NAME_
			String area = unit.get(3); //地区
			String endDate =unit.get(4);  //计划日期(mdate)
			String startDate =unit.get(5);//开始时间(dtime)
			String startTime = unit.get(7); //开始时段
			String endTime = unit.get(8); //结束时段
			String power = unit.get(9); //电力
			String price = unit.get(10); //价格
			
			//Map<String, Object> declareMap = new HashMap<String, Object>();
			Map<String,Object> timeMap = new HashMap<String, Object>();
			Map<String,Object> powerMap = new HashMap<String, Object>();
			timeMap.put("startTime", startTime);
			timeMap.put("endTime", endTime);
			powerMap.put("power", power);
			powerMap.put("price", price);
			for (Map<String, Object> m : declareData)  
		    {  
		      for (Object k : m.values())  
		      {  
		        if(k==sheetName){
		        	
		        }
		      }  
		  
		    }  
					
			if(declareMap.containsValue(sheetName)){
				List<Map<String,Object>> timedata = new ArrayList<Map<String,Object>>();
				List<Map<String,Object>> powerData = new ArrayList<Map<String,Object>>();
				
				powerData.add(powerMap);
				
				timeMap.put("data", powerData);
				timedata.add(timeMap);
				declareMap.put("data", timedata);

			}else{
				declareMap.put("sheetName", sheetName);
				declareMap.put("area", area);
				declareMap.put("endDate", endDate);
				declareMap.put("startDate", startDate);
				
				timeMap.put("startTime", startTime);
				timeMap.put("endTime", endTime);
				powerMap.put("power", power);
				powerMap.put("price", price);
				//powerData.add(powerMap);
				
				//timeMap.put("data", powerData);
				//timedata.add(timeMap);
				//declareMap.put("data", timedata);
			}
			
			

		}
		Iterator<Map.Entry<String, Object>> it = declareMap.entrySet().iterator();
		while (it.hasNext()) {
			//declareService.saveDeclare(null, declareData.get("startDate"), declareData.get("endDate"), time, declareData.get("data"));
		}
	}

	/**
	 * 解析出清电力和出清电价，结果插入CBPM_RESULT
	 * @description
	 * @author 大雄
	 * @date 2016年8月24日下午5:17:00
	 * @param dtime
	 * @param mdate
	 */
	public static void getResult(String dtime, String mdate) {
		//解析出清电力
		Vector<Vector<String>> data = readFile(new File(FILE_NAME), "<ClearPower::国调 type=全数>", "</ClearPower::国调 type=全数>");
		String setAttributeMethodName = null;
		Method setAttributeMethod = null;
		ResultPo po = null;
		for (Vector<String> unit : data) {
			po = new ResultPo();
			po.setArea(unit.get(2));// 地区
			po.setDtime(dtime);
			po.setMdate(mdate);
			po.setDrole(unit.get(3));//买卖类型	DROLE 送电侧；受电侧
			po.setDtype(Enums_DeclareType.DATATYPE_POWER);//成交数据类型	DTYPE	VARCHAR(20)	电力；电价
			po.setDprint("0");//未发布
			for (int i = 1; i < 97; i++) {
				setAttributeMethodName = "setH" + CommonUtil.decimal(2, i);
				try {
					setAttributeMethod = ResultPo.class.getDeclaredMethod(setAttributeMethodName, Double.class);
					setAttributeMethod.invoke(po, Double.parseDouble(unit.get(3 + 1)));
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
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
			System.out.println(CommonUtil.objToJson(po));
		}
		//解析出清电价
		data = readFile(new File(FILE_NAME), "<ClearPrice::国调 type=全数>", "</ClearPrice::国调 type=全数>");
		for (Vector<String> unit : data) {
			po = new ResultPo();
			po.setArea(unit.get(2));// 地区
			po.setDtime(dtime);
			po.setMdate(mdate);
			po.setDrole(unit.get(3));//买卖类型	DROLE 送电侧；受电侧
			po.setDtype(Enums_DeclareType.DATATYPE_PRICE);//成交数据类型	DTYPE	VARCHAR(20)	电力；电价
			po.setDprint("0");//未发布
			for (int i = 1; i < 97; i++) {
				setAttributeMethodName = "setH" + CommonUtil.decimal(2, i);
				try {
					setAttributeMethod = ResultPo.class.getDeclaredMethod(setAttributeMethodName, Double.class);
					setAttributeMethod.invoke(po, Double.parseDouble(unit.get(3 + 1)));
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
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
			System.out.println(CommonUtil.objToJson(po));
		}
		
		
	}
	
	/**
	 * 解析通道结果集，结果插入CBPM_PATH_RESULT
	 * @description
	 * @author 大雄
	 * @date 2016年8月24日下午5:27:40
	 * @param dtime
	 * @param mdate
	 */
	public static void getPathResult(String dtime, String mdate) {
		//解析SheetCost插入通道结果
		Vector<Vector<String>> data = readFile(new File(FILE_NAME), "<SheetCost::国调 type=全数>", "</SheetCost::国调 type=全数>");
		String setAttributeMethodName = null;
		Method setAttributeMethod = null;
		Map<String,Map<String,Map<Integer,Double>>> result = new HashMap<String,Map<String,Map<Integer,Double>>>();
		PathResultPo po = null;
		Map<String,Map<Integer,Double>> dtypeMap = null;
		for (Vector<String> unit : data) {
			if(result.containsKey(unit.get(2))){
				dtypeMap = result.get(unit.get(2));
			}else{
				dtypeMap = new HashMap<String,Map<Integer,Double>>();
				dtypeMap.put("送端电力", new HashMap<Integer,Double>());
				dtypeMap.put("受端电力", new HashMap<Integer,Double>());
				dtypeMap.put("送端上网费用", new HashMap<Integer,Double>());
				dtypeMap.put("省内输电费用", new HashMap<Integer,Double>());
				dtypeMap.put("跨区输电费用", new HashMap<Integer,Double>());
				dtypeMap.put("从送端累加的总费用", new HashMap<Integer,Double>());
				dtypeMap.put("受端总费用", new HashMap<Integer,Double>());
				result.put(unit.get(2), dtypeMap);
			}
			dtypeMap.get("送端电力").put(Integer.parseInt(unit.get(3)), Double.parseDouble(unit.get(4)));
			dtypeMap.get("受端电力").put(Integer.parseInt(unit.get(3)), Double.parseDouble(unit.get(5)));
			dtypeMap.get("送端上网费用").put(Integer.parseInt(unit.get(3)), Double.parseDouble(unit.get(6)));
			dtypeMap.get("省内输电费用").put(Integer.parseInt(unit.get(3)), Double.parseDouble(unit.get(7)));
			dtypeMap.get("跨区输电费用").put(Integer.parseInt(unit.get(3)), Double.parseDouble(unit.get(8)));
			dtypeMap.get("从送端累加的总费用").put(Integer.parseInt(unit.get(3)), Double.parseDouble(unit.get(9)));
			dtypeMap.get("受端总费用").put(Integer.parseInt(unit.get(3)), Double.parseDouble(unit.get(10)));
			//System.out.println(unit);
		}
		//遍历解析结果插入CBPM_PATH_RESULT
		for(Map.Entry<String,Map<String,Map<Integer,Double>>> pathEnt : result.entrySet()){
			for(Map.Entry<String,Map<Integer,Double>> dtypeEnt : pathEnt.getValue().entrySet()){
				po = new PathResultPo();
				po.setMpath(pathEnt.getKey());
				po.setMdate(mdate);
				po.setDtype(dtypeEnt.getKey());//成交数据类型	DTYPE	VARCHAR(20)	电力；电价
				po.setDprint("0");//未发布
				for(Map.Entry<Integer,Double> ent : dtypeEnt.getValue().entrySet()){
					setAttributeMethodName = "setH" + CommonUtil.decimal(2, ent.getKey());
					try {
						setAttributeMethod = PathResultPo.class.getDeclaredMethod(setAttributeMethodName, Double.class);
						setAttributeMethod.invoke(po, ent.getValue());
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
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
				//System.out.println(CommonUtil.objToJson(po));
			}
		}
		
		//计算SDSheetCost结果插入通道结果表
		data = readFile(new File(FILE_NAME), "<SDSheetCost::国调 type=全数>", "</SDSheetCost::国调 type=全数>");
		result = new HashMap<String,Map<String,Map<Integer,Double>>>();
		for (Vector<String> unit : data) {
			if(result.containsKey(unit.get(2))){
				dtypeMap = result.get(unit.get(2));
			}else{
				dtypeMap = new HashMap<String,Map<Integer,Double>>();
				dtypeMap.put("送端电力", new HashMap<Integer,Double>());
				dtypeMap.put("受端电力", new HashMap<Integer,Double>());
				dtypeMap.put("送端上网费用", new HashMap<Integer,Double>());
				dtypeMap.put("省内输电费用", new HashMap<Integer,Double>());
				dtypeMap.put("华中输电费用", new HashMap<Integer,Double>());
				dtypeMap.put("德宝输电费用", new HashMap<Integer,Double>());
				dtypeMap.put("西北输电费用", new HashMap<Integer,Double>());
				dtypeMap.put("宁东输电费用", new HashMap<Integer,Double>());
				dtypeMap.put("从送端累加的总费用", new HashMap<Integer,Double>());
				dtypeMap.put("受端总费用", new HashMap<Integer,Double>());
				result.put(unit.get(2), dtypeMap);
			}
			dtypeMap.get("送端电力").put(Integer.parseInt(unit.get(3)), Double.parseDouble(unit.get(4)));
			dtypeMap.get("受端电力").put(Integer.parseInt(unit.get(3)), Double.parseDouble(unit.get(5)));
			dtypeMap.get("送端上网费用").put(Integer.parseInt(unit.get(3)), Double.parseDouble(unit.get(6)));
			dtypeMap.get("省内输电费用").put(Integer.parseInt(unit.get(3)), Double.parseDouble(unit.get(7)));
			dtypeMap.get("华中输电费用").put(Integer.parseInt(unit.get(3)), Double.parseDouble(unit.get(8)));
			dtypeMap.get("德宝输电费用").put(Integer.parseInt(unit.get(3)), Double.parseDouble(unit.get(9)));
			dtypeMap.get("西北输电费用").put(Integer.parseInt(unit.get(3)), Double.parseDouble(unit.get(10)));
			dtypeMap.get("宁东输电费用").put(Integer.parseInt(unit.get(3)), Double.parseDouble(unit.get(11)));
			dtypeMap.get("从送端累加的总费用").put(Integer.parseInt(unit.get(3)), Double.parseDouble(unit.get(12)));
			dtypeMap.get("受端总费用").put(Integer.parseInt(unit.get(3)), Double.parseDouble(unit.get(13)));
			
			//System.out.println(unit);
		}
		//遍历解析结果插入CBPM_PATH_RESULT
		for(Map.Entry<String,Map<String,Map<Integer,Double>>> pathEnt : result.entrySet()){
			for(Map.Entry<String,Map<Integer,Double>> dtypeEnt : pathEnt.getValue().entrySet()){
				po = new PathResultPo();
				po.setMpath(pathEnt.getKey());
				po.setMdate(mdate);
				po.setDtype(dtypeEnt.getKey());//成交数据类型	DTYPE	VARCHAR(20)	电力；电价
				po.setDprint("0");//未发布
				for(Map.Entry<Integer,Double> ent : dtypeEnt.getValue().entrySet()){
					setAttributeMethodName = "setH" + CommonUtil.decimal(2, ent.getKey());
					try {
						setAttributeMethod = PathResultPo.class.getDeclaredMethod(setAttributeMethodName, Double.class);
						setAttributeMethod.invoke(po, ent.getValue());
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
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
				System.out.println(CommonUtil.objToJson(po));
			}
		}
		
		//System.out.println(CommonUtil.objToJson(result));
		
	}
	
	
	/**
	 * 解析限额，结果插入CBPM_LINE_LIMIT
	 * @description
	 * @author 大雄
	 * @date 2016年8月24日下午5:17:00
	 * @param dtime
	 * @param mdate
	 */
	public static void getLineLimit(String dtime, String mdate) {
		//解析出清电力
		Vector<Vector<String>> data = readFile(new File(FILE_NAME), "<TransTieline::国调 type=全数>", "</TransTieline::国调 type=全数>");
		String setAttributeMethodName = null;
		Method setAttributeMethod = null;
		LineLimitPo po = null;
		for (Vector<String> unit : data) {
			po = new LineLimitPo();
			po.setMdate(mdate);
			po.setDtype(Enums_DeclareType.DATATYPE_TRADING_POWER);//交易功率
			po.setMcorhr(unit.get(2));
			for (int i = 1; i < 97; i++) {
				setAttributeMethodName = "setH" + CommonUtil.decimal(2, i);
				try {
					setAttributeMethod = LineLimitPo.class.getDeclaredMethod(setAttributeMethodName, double.class);
					setAttributeMethod.invoke(po, Double.parseDouble(unit.get(2 + 1)));
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
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
			System.out.println(CommonUtil.objToJson(po));
		}
	}
	
	
	/**
	 * 解析限额，结果插入CBPM_LINE_LIMIT
	 * @description
	 * @author 大雄
	 * @date 2016年8月24日下午5:17:00
	 * @param dtime
	 * @param mdate
	 */
	public static void getClearDetail(String dtime, String mdate) {
		//解析出清电力
		Vector<Vector<String>> data = readFile(new File(FILE_NAME), "<ClearDetail::国调 type=全数>", "</ClearDetail::国调 type=全数>");
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		Map<String,Object> po = null;
		for (Vector<String> unit : data) {
			po = new HashMap<String,Object>();
			po.put("Number", unit.get(1));
			po.put("Interval", unit.get(2));
			po.put("BuyerArea", unit.get(3));
			po.put("Buyername", unit.get(4));
			po.put("Buyersection", unit.get(5));
			po.put("power", unit.get(6));
			po.put("price", unit.get(7));
			po.put("SellerArea", unit.get(8));
			po.put("Sellername", unit.get(9));
			po.put("Sellersection", unit.get(10));
			po.put("power", unit.get(11));
			po.put("price", unit.get(12));
			po.put("Pricediff", unit.get(13));
			po.put("Corridorpower", unit.get(14));
			po.put("clearpower", unit.get(15));
			result.add(po);
			System.out.println(CommonUtil.objToJson(po));
		}
	}
	
	

	/**
	 * 读取txt数据
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年8月24日下午4:47:17
	 * @param file
	 * @param start
	 * @param end
	 * @return
	 */
	public static Vector<Vector<String>> readFile(File file, String start, String end) {
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		Vector<String> unitData = null;
		FileInputStream input = null;
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String encoding = "GBK";
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(input, encoding);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader bufferReader = new BufferedReader(reader);
		String line = "";
		try {
			line = bufferReader.readLine();
			// System.out.println(line);
			while (line != null && !line.contains(start)) {
				line = bufferReader.readLine();

			}
			line = bufferReader.readLine();
			line = bufferReader.readLine();
			while (line != null && !line.equals(end)) {
				line = bufferReader.readLine();
				//System.out.println(line);
				String[] array = line.split("\t");
				unitData = new Vector<String>();
				for (int i = 0; i < array.length; i++) {
					unitData.add(array[i]);
				}
				data.add(unitData);
			}
			if (data.size() > 0) {
				data.remove(data.size() - 1);
			}

			bufferReader.close();
			reader.close();
			input.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return data;
	}
	
	/**
	 * 修改判断start和end都是contains,并且可以处理不规则的数据格式，比如多个空格和多个/t
	 * @description
	 * @author 大雄
	 * @date 2016年9月19日下午6:21:59
	 * @param file
	 * @param start
	 * @param end
	 * @return
	 */
	public static Vector<Vector<String>> readFile2(File file, String start, String end) {
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		Vector<String> unitData = null;
		FileInputStream input = null;
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String encoding = "GBK";
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(input, encoding);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader bufferReader = new BufferedReader(reader);
		String line = "";
		try {
			line = bufferReader.readLine();
			// System.out.println(line);
			while (line != null && !line.contains(start)) {
				line = bufferReader.readLine();

			}
			line = bufferReader.readLine();
			line = bufferReader.readLine();
			while (line != null && !line.contains(end)) {
				line = bufferReader.readLine();
				
				//System.out.println(line);
				LoggerUtil.log(DatUtil.class.getName(), "读内容："+line,0);
				//String[] array = line.split("\t");
				unitData = new Vector<String>();
				//for (int i = 0; i < array.length; i++) {
					//unitData.add(array[i]);
				//}
				int startIndex = 0;
				int len = line.length();
				for(int i = 0; i < line.length(); i++) {
					char c = line.charAt(i);
					//判断是否是空格或者tab
					if(Character.isSpace(c) || c == 9){
						if(i!=0){
							//判断上一个字符是否是空格或者tab
							char preC = line.charAt(i-1);
							if(Character.isSpace(preC) || preC == 9){
								startIndex = i;
							}else{
								unitData.add(line.substring(startIndex==0?0:(startIndex+1),i));
								startIndex = i;
							}
						}
					}
					
		        }
				if(startIndex < len-1){
					String last = line.substring(startIndex+1,len);
					if(CommonUtil.ifEmpty(last.trim())){
						unitData.add(last);
					}
				}
				//System.out.println(CommonUtil.objToJson(unitData));
				LoggerUtil.log(DatUtil.class.getName(), "得内容："+CommonUtil.objToJson(unitData),0);
				
				data.add(unitData);
			}
			if (data.size() > 0) {
				data.remove(data.size() - 1);
			}

			bufferReader.close();
			reader.close();
			input.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return data;
	}
	
	public static Vector<Vector<String>> readFile3(File file, String start, String end) {
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		Vector<String> unitData = null;
		FileInputStream input = null;
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String encoding = "GBK";
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(input, encoding);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader bufferReader = new BufferedReader(reader);
		String line = "";
		try {
			line = bufferReader.readLine();
			// System.out.println(line);
			while (line != null && !line.contains(start)) {
				line = bufferReader.readLine();

			}
			//line = bufferReader.readLine();
			//line = bufferReader.readLine();
			while (line != null && !line.equals(end)) {
				line = bufferReader.readLine();
				if(CommonUtil.ifEmpty(line)){
					unitData = new Vector<String>();
					//for (int i = 0; i < array.length; i++) {
						//unitData.add(array[i]);
					//}
					int startIndex = 0;
					int len = line.length();
					for(int i = 0; i < line.length(); i++) {
						char c = line.charAt(i);
						//判断是否是空格或者tab
						if(Character.isSpace(c) || c == 9){
							if(i!=0){
								//判断上一个字符是否是空格或者tab
								char preC = line.charAt(i-1);
								if(Character.isSpace(preC) || preC == 9){
									startIndex = i;
								}else{
									unitData.add(line.substring(startIndex==0?0:(startIndex+1),i));
									startIndex = i;
								}
							}
						}
						
			        }
					if(startIndex < len-1){
						String last = line.substring(startIndex+1,len);
						if(CommonUtil.ifEmpty(last.trim())){
							unitData.add(last);
						}
					}
					data.add(unitData);
				}
				//System.out.println(line);
				
			}
			if (data.size() > 0) {
				data.remove(data.size() - 1);
			}

			bufferReader.close();
			reader.close();
			input.close();

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return data;
	}
}
