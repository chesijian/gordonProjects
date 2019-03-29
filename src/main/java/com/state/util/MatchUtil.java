package com.state.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.state.enums.Enums_SystemConst;
import com.state.po.AreaPo;
import com.state.po.CorridorPathPo;
import com.state.po.LineDefinePo;
import com.state.po.LineLimitPo;
import com.state.po.PathDefinePo;
import com.state.po.sys.PfSysConfigPo;
import com.state.service.AreaService;
import com.state.service.ILineService;
import com.state.service.ServiceHelper;
import com.state.service.SystemServiceI;

public class MatchUtil {
	private static List<AreaPo> areaList;
	private static List<LineDefinePo> lineList;

	public static List<AreaPo> getAreaList() {
		return areaList;
	}
	public static synchronized Map<String,Object> getAreaAndRegion(){
		Map<String,Object> map = new HashMap<String,Object>();
		if(areaList == null){
			areaList = ServiceHelper.getBean(AreaService.class).getAreaInfo();
		}
		if(areaList != null && areaList.size()>0){
			for(AreaPo area : areaList){
				map.put(area.getArea(), area.getRegion());
			}
		}
		return map;
	}
	public static synchronized Map<String,Object> getLineLimitBySession(){
		Map<String,Object> map = new HashMap<String,Object>();
		if(lineList == null){
			lineList = ServiceHelper.getBean(ILineService.class).getAllLine();
		}
		if(lineList != null && lineList.size()>0){
			for(LineDefinePo obj : lineList){
				map.put(obj.getMcorhr(), obj.getRate()+"`"+obj.getTielinetariff());
			}
		}
		return map;
	}
	public static Map<String,Map<String,LineLimitPo>> getLineMap(List<LineLimitPo> limitList){
		//System.out.println("---------"+CommonUtil.objToJson(limitList));
		Map<String,Map<String,LineLimitPo>> map = new HashMap<String, Map<String,LineLimitPo>>();
		Map<String,LineLimitPo> objMap = null;
		
		for(LineLimitPo obj : limitList){
			if(!map.containsKey(obj.getMcorhr())){
				objMap = new HashMap<String, LineLimitPo>();
				map.put(obj.getMcorhr(), objMap);
			}else{
				objMap = map.get(obj.getMcorhr());
			}
			if(!objMap.containsKey(obj.getDtype())){
				objMap.put(obj.getDtype(), obj);
			}
		}
		return map;
	}
	/**
	 * 通过所有可用通道及其路径得到下面所有的联络线名称
	 * @param pathList
	 * @return
	 */
	public static Map<String,Object> getKYPathLine(List<PathDefinePo> pathList){
		Map<String,Object> map = new HashMap<String, Object>();
		for(int i=0;i<pathList.size();i++){
			List<CorridorPathPo> listCorr = pathList.get(i).getCorridorPaths();
			for(CorridorPathPo corriPo : listCorr){
				for(int j=1;j<11;j++){
					String getAttributeMethodName = "getCorhr" + j;
					Method getAttributeMethod = null;
					try {
						getAttributeMethod = CorridorPathPo.class.getDeclaredMethod(getAttributeMethodName);
						try {
							String corhr = (String) getAttributeMethod.invoke(corriPo);
							if(corhr != null && !map.containsKey(corhr)){
								map.put(corhr, corriPo);
							}
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						}
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return map;
	}
	public static Map<String, List<LineDefinePo>> getLineDefine(List<LineDefinePo> list) {
		Map<String,List<LineDefinePo>> map = new HashMap<String, List<LineDefinePo>>();
		List<LineDefinePo> listLine = null;
		if(list != null && list.size()>0){
			for(LineDefinePo obj : list){
				if(!map.containsKey(obj.getStartArea())){
					listLine = new ArrayList<LineDefinePo>();
					listLine.add(obj);
					map.put(obj.getStartArea(), listLine);
				}else{
					listLine = map.get(obj.getStartArea());
					listLine.add(obj);
				}
			}
			for(LineDefinePo obj : list){
				if(!map.containsKey(obj.getEndArea())){
					listLine = new ArrayList<LineDefinePo>();
					listLine.add(obj);
					map.put(obj.getEndArea(), listLine);
				}else{
					listLine = map.get(obj.getEndArea());
					listLine.add(obj);
				}
			}
		}
		return map;
	}
	/**
	 * 判断按钮是否已经点击
	 * @param time 时间，例如：2016-12-15
	 * @param btnName 按钮名字
	 * @return 已经点击返回true，反之返回false
	 */
	public static boolean isClickButton(String time, String btnName){
		PfSysConfigPo processConfig = ServiceHelper.getBean(SystemServiceI.class).selectOneByType(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG, time);
		if(processConfig != null){
			//String mdate = processConfig.getKey();
			String config = processConfig.getValue();
			if(CommonUtil.ifEmpty(config)){
				Map<String, Integer> map = CommonUtil.jsonToMapInteger(config);
				if(map.containsKey(btnName) && map.get(btnName) == 1){
					return true;
				}
			}
		}
		return false;
	}
}
