package com.state.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class CommonUtil {

	/**
	 * 判断一个对象是否为空
	 * @param o
	 * @return
	 */
	public static boolean ifEmpty(Object o){
		if(o == null || o.toString().trim().equals("")){
			return false;
		}
		else{
			return true;
		}
	}
	
	public static boolean ifEmpty_List(List o){
		if(o != null && o.size() > 0){
			return true;
		}
		return false;
	}
	
	/**
	 * 转化为两位数字比如 9-->09
	 * @author 大雄
	 * @Title decimal
	 * @Date 2015-1-6
	 * @Description 转化为两位数字比如 9-->09
	 * @Params @param offset 几位数字
	 * @Params @param shu
	 * @Params @return
	 * @Return String
	 */
	public static String decimal(int offset,int shu){
		DecimalFormat df = new DecimalFormat();
		StringBuffer sb = new StringBuffer();
		for(int i = 0;i<offset;i++){
			sb.append("0");
		}
		df.applyPattern(sb.toString());
		return df.format(shu);
	}
	
	/**
	 * 保留两位有效数字
	 * @description
	 * @author 大雄
	 * @date 2016年9月25日下午9:32:20
	 * @param shu
	 * @return
	 */
	public static String toFix(Object shu){
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		return decimalFormat.format(shu);
	}
	

	/**
	 * 获取当前时间
	 * @description
	 * @author 大雄
	 * @date 2016年8月2日下午4:11:52
	 * @return
	 */
	public static String getDateTime() {
		// TODO Auto-generated method stub
		return TimeUtil.getStringDate();
	}
	/**
	 * 返回yyyyMMdd
	 * @description
	 * @author 大雄
	 * @date 2016年8月24日下午5:03:31
	 * @return
	 */
	public static String getSimpleDate() {
		// TODO Auto-generated method stub
		return TimeUtil.getStringDateSimple();
	}
	
	public static String getUUID(){
		//用hibernate的UUID生成器
		
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 
	 * @author 大雄
	 * @Title getBaseUrl
	 * @Date 2015-1-8
	 * @Description 获取主机url
	 * @Params @return
	 * @Return String
	 */
	public static String getBaseUrl(){
		StringBuffer sb = new StringBuffer();
		sb.append(SessionUtil.getCurrentRequest().getScheme()+"://");
		sb.append(SessionUtil.getCurrentRequest().getServerName());
		if( SessionUtil.getCurrentRequest().getServerPort() != 0)
			sb.append(":"+SessionUtil.getCurrentRequest().getServerPort());
		sb.append(SessionUtil.getCurrentRequest().getContextPath());
		return sb.toString();
	}
	
	public static String getBaseUrl(HttpServletRequest request){
		StringBuffer sb = new StringBuffer();
		//System.out.println(request.getRequestURI());
		//System.out.println(request.getScheme() );
		sb.append(request.getScheme()+"://");
		sb.append(request.getServerName());
		if( request.getServerPort() != 0)
			sb.append(":"+request.getServerPort());
		sb.append(request.getContextPath());
		return sb.toString();
	}
	
	/**
	 * objToJson
	 * @author 大雄
	 * @Title objToJson
	 * @Date 2015-3-30
	 * @Description TODO
	 * @Params @param o
	 * @Params @return
	 * @Return String
	 */
	public static String objToJson(Object o){
		return JSON.toJSONString(o);
	}
	
	/**
	 * 转换json为Map<String,Object>
	 * @author 大雄
	 * @Title jsonToMap
	 * @Date 2015-3-30
	 * @Description TODO
	 * @Params @param json
	 * @Params @return
	 * @Return Map<String,Object>
	 */
	public static Map<String,Object> jsonToMapObj(String json){
		if(CommonUtil.ifEmpty(json)){
			return JSON.parseObject(json, new TypeReference<Map<String, Object>>(){});
		}else{
			return new HashMap<String, Object>();
		}
		
		
	}
	/**
	 * 转换json为List<Map<String,Object>>
	 * @author 大雄
	 * @Title jsonToListMapObj
	 * @Date 2015-3-30
	 * @Description TODO
	 * @Params @param json
	 * @Params @return
	 * @Return List<Map<String,Object>>
	 */
	public static List<Map<String,Object>> jsonToListMapObj(String json){
		if(CommonUtil.ifEmpty(json)){
			return JSON.parseObject(json, new TypeReference<List<Map<String, Object>>>(){});
		}else{
			return new ArrayList<Map<String,Object>>();
		}
	}
	
	/**
	 * 转换json为List<Map<String,String>>
	 * @author 大雄
	 * @Title jsonToListMapStr
	 * @Date 2015-3-30
	 * @Description TODO
	 * @Params @param json
	 * @Params @return
	 * @Return List<Map<String,String>>
	 */
	public static List<Map<String,String>> jsonToListMapStr(String json){
		if(CommonUtil.ifEmpty(json)){
			return JSON.parseObject(json, new TypeReference<List<Map<String, String>>>(){});
		}else{
			return new ArrayList<Map<String,String>>();
		}
	}
	
	/**
	 * 转换json为Map<String,String>
	 * @author 大雄
	 * @Title jsonToMapString
	 * @Date 2015-3-30
	 * @Description TODO
	 * @Params @param json
	 * @Params @return
	 * @Return Map<String,String>
	 */
	public static Map<String,String> jsonToMapStr(String json){
		if(CommonUtil.ifEmpty(json)){
			return JSON.parseObject(json, new TypeReference<Map<String, String>>(){});
		}else{
			return new HashMap<String, String>();
		}
	}
	/**
	 * json转换
	 * @description
	 * @author 大雄
	 * @date 2016年10月17日上午10:35:34
	 * @param json
	 * @return
	 */
	public static Map<String,Integer> jsonToMapInteger(String json){
		if(CommonUtil.ifEmpty(json)){
			return JSON.parseObject(json, new TypeReference<Map<String, Integer>>(){});
		}else{
			return new HashMap<String, Integer>();
		}
	}
}
