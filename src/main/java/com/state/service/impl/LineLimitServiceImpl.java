package com.state.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.state.dao.IssueDao;
import com.state.dao.LineDefineDao;
import com.state.dao.LineLimitDao;
import com.state.dao.TransTielineDao;
import com.state.enums.Enums_DeclareType;
import com.state.enums.Enums_SystemConst;
import com.state.po.LineDefinePo;
import com.state.po.LineLimitPo;
import com.state.po.TransTielinePo;
import com.state.po.TreeBean;
import com.state.po.sys.PfSysConfigPo;
import com.state.service.LineLimitService;
import com.state.service.ServiceHelper;
import com.state.service.SystemServiceI;
import com.state.util.CommonUtil;
import com.state.util.SessionUtil;
import com.state.util.TimeUtil;

@Service
@Transactional
public class LineLimitServiceImpl implements LineLimitService{
	
	@Autowired
	private LineLimitDao lineLimitDao;
	@Autowired
	private LineDefineDao lineDefineDao;
	@Autowired
	private TransTielineDao transTielineDao;	
	@Autowired
	private IssueDao issueDao;
	
	public void insertLineLimit(LineLimitPo lineLimitPo) {
		lineLimitDao.insertLineLimit(lineLimitPo);
	}

	public void updateLineLimit(LineLimitPo lineLimitPo) {
		lineLimitDao.updateLineLimit(lineLimitPo);
	}

	public LineLimitPo getLineLimit(String mcorhr, String mdate, String dtype) {
		return lineLimitDao.getLineLimit(mcorhr,mdate,dtype,"");
	}

	public LineLimitPo selectLineLimitList(String mcorhr, String time,String dtype,String state) {
		
		LineLimitPo po = lineLimitDao.getLineLimit(mcorhr,time,dtype,state);
		return po;
	}
	
	/**
	 * 获取通道交易结果树
	 * @author 车斯剑
	 * @date 2016年11月16日下午3:55:12
	 * @param area
	 * @param time
	 * @param status
	 * @return
	 */
	public List<TreeBean> getTree(String area, String time, Integer status){
		
		List<TransTielinePo> lineList = transTielineDao.getTree(time,area, status);
		List<TreeBean> list=new ArrayList<TreeBean>();
		TreeBean firstNode = null;
		TreeBean secondNode = null;
		Map<String, Map<String, Object>> tree = new HashMap<String, Map<String, Object>>();
		
		Map<String, Object> treeInfo = null;
		Map<String, Object> first = null;
		Map<String, Object> firstInfo = null;
		Map<String, Object> second = null;
		Map<String, Object> secondInfo = null;
		String id;
		int firstIndex = 0;
		boolean isOpen = false;
		if (CommonUtil.ifEmpty_List(lineList)) {
			for (TransTielinePo po : lineList) {
				
				if (tree.containsKey(po.getTielineName())) {
					if (SessionUtil.isState()) {
						isOpen = true;
					} else {
						isOpen = (firstIndex == 1 ? true : false);
					}
					isOpen = true;
					second = (Map<String, Object>) tree.get(po.getTielineName()).get("children");
					id = String.valueOf(tree.get(po.getTielineName()).get("id"));

					if (second.containsKey(po.getTransCorridorName())) {
						secondInfo = (Map<String, Object>) second.get(po.getTielineName());
						
					} else {

						secondInfo = new HashMap<String, Object>();
						second.put(po.getTransCorridorName(), secondInfo);
						secondInfo.put("id", po.getId());
						
						secondNode = new TreeBean(po.getId(), id, po.getTransCorridorName(), isOpen);
						list.add(secondNode);
					}
				} else {
					// 送电侧或者受电侧
					id = CommonUtil.decimal(2, ++firstIndex);
					id = "-" + id;
					treeInfo = new HashMap<String, Object>();
					first = new HashMap<String, Object>();
					firstInfo = new HashMap<String, Object>();
					second = new HashMap<String, Object>();
					secondInfo = new HashMap<String, Object>();

					tree.put(po.getTielineName(), treeInfo);
					treeInfo.put("id", id);
					treeInfo.put("children", second);

					if (SessionUtil.isState()) {
						isOpen = true;
					} else {
						isOpen = (firstIndex == 1 ? true : false);
					}
					isOpen = true;
					firstNode = new TreeBean(id, "0", po.getTielineName(), isOpen);
					list.add(firstNode);
					second.put(po.getTransCorridorName(), secondInfo);
					secondInfo.put("id", po.getId());
					secondNode = new TreeBean(po.getId(), id, po.getTransCorridorName(), isOpen);
					list.add(secondNode);
				}
			}
		}
		return list;
	}
	
	/**
     * 根据参数获取联络线电量
     * @param mcorhr
     * @param time
     * @return
     */
	public LineLimitPo getDLTJPo(String mcorhr,String time){
		return lineLimitDao.getDLTJPo(mcorhr, time);
	}
	
	public String getConfigStr() {
		// TODO Auto-generated method stub
		return lineLimitDao.getConfigStr();
	}
	/**
	 * 组织周报执行情况数据
	 * @author 车斯剑
	 * @date 2016年10月19日下午4:44:19
	 * @param startTime
	 * @param endTime
	 */
	public List<Map<String,Map<String,Double>>> getCarryData(String startTime,String endTime){
		List<Map<String,Map<String,Double>>> carryData = new ArrayList<Map<String,Map<String,Double>>>();
		List<LineDefinePo> lineList = lineDefineDao.getAllLine();
		String status = null;
		if (!SessionUtil.isState()) {
			status = "1";
		}
		Map<String,Map<String,Double>> mapData = new HashMap<String, Map<String,Double>>();
		Map<String,Map<String,Double>> biasMap = new HashMap<String, Map<String,Double>>();
		for (LineDefinePo po : lineList) {
			Map<String,Double> datas = new HashMap<String, Double>();
			Map<String,Double> biasDatas = new HashMap<String, Double>();
			double deal =0;
			double carry =0; 
			String start = startTime;
			do{
				//LineLimitPo tradeData = lineLimitDao.getLineLimit(po.getMcorhr(),start,Enums_DeclareType.DATATYPE_TRADING_POWER,status);
				TransTielinePo tradeData = transTielineDao.getTotalTransTielineByName(po.getMcorhr(),start);
				//LineLimitPo beforeData = lineLimitDao.getLineLimit(po.getMcorhr(), start, "日前计划值", "");
				List<String> mcorhrList = new ArrayList<String>();
				mcorhrList.add(po.getMcorhr());
				List<TransTielinePo> executeTielineList = issueDao.getExecuteDataForArea(start, null, mcorhrList);
				if(tradeData!=null){
					deal += tradeData.getSumQ()/4;
				}
				if(executeTielineList!=null && executeTielineList.size()>0){
					for(TransTielinePo executeTielinePo : executeTielineList){
						if(CommonUtil.ifEmpty(executeTielinePo.getTransCorridorName())){
							if(!executeTielinePo.getTransCorridorName().equals("汇总值")){
								carry += getSum(executeTielinePo)/4;
							}
						}
					}
					
				}
				
				start = TimeUtil.getNextDayDate(start, "yyyyMMdd");
			}while(Long.valueOf(start)<=Long.valueOf(endTime));
			
			double range = deal-carry;
			datas.put("power", Double.valueOf(CommonUtil.toFix(deal)));
			datas.put("price", Double.valueOf(CommonUtil.toFix(carry)));
			//datas.put("range", range);
			
			biasDatas.put("power", range);
			biasDatas.put("price", Double.valueOf(0));
			mapData.put(po.getMcorhr(), datas);
			biasMap.put(po.getMcorhr(), biasDatas);
		}
		carryData.add(mapData);
		carryData.add(biasMap);
		return carryData;
	}
	
	private double getSum(TransTielinePo transTielinePo) {
		// TODO Auto-generated method stub
		double spaceSum =0;
		for (int i = 1; i < 97; i++) {
			String getAttributeMethodName = "getH" + (i < 10 ? "0" + i : i);
			Method getAttributeMethod = null;
			try {
				getAttributeMethod = TransTielinePo.class.getDeclaredMethod(getAttributeMethodName);
				try {
					if(transTielinePo != null){
						spaceSum += Math.abs((Double) getAttributeMethod.invoke(transTielinePo)==null? 0:(Double) getAttributeMethod.invoke(transTielinePo));
						
					}
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
		return spaceSum;
	}

	/**
	 * 获取LineLimitPo 96值总和
	 * @author 车斯剑
	 * @date 2016年10月11日上午10:19:13
	 * @param data
	 * @return
	 */
	public double getSum(LineLimitPo  data){
		double spaceSum =0;
		for (int i = 1; i < 97; i++) {
			String getAttributeMethodName = "getH" + (i < 10 ? "0" + i : i);
			Method getAttributeMethod = null;
			try {
				getAttributeMethod = LineLimitPo.class.getDeclaredMethod(getAttributeMethodName);
				try {
					spaceSum += Math.abs((Double) getAttributeMethod.invoke(data)==null? 0:(Double) getAttributeMethod.invoke(data));
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
		return spaceSum;
	}
	
	/**
	 * 获取单个可用容量、日前预计划值
	 * @author 车斯剑
	 * @date 2016年10月12日上午9:59:46
	 * @param mcorhr
	 * @param dtype
	 * @param time
	 * @param state
	 * @return
	 */
	public Map<String,LineLimitPo> getSpaceData(String mcorhr, String dtype, String time, String state){
		Map<String,LineLimitPo> map = new HashMap<String, LineLimitPo>();
		LineLimitPo beforeData = lineLimitDao.getLineLimit(mcorhr, time, "日前计划值", state);
		LineLimitPo staticData = lineLimitDao.getLineLimit(mcorhr, time, "静态正向限额", state);
		LineLimitPo dynamicData = lineLimitDao.getLineLimit(mcorhr, time, "动态正向限额", state);
		
		// 判断是用哪个减 差值空间 = 静态限额/动态限额-日前计划
		PfSysConfigPo config = ServiceHelper.getBean(SystemServiceI.class).getSysConfig(Enums_SystemConst.SPACE_EXCUTE_TYPE);
		//boolean flag = true;
		LineLimitPo minuendData = null;
		if (config == null || config.getValue().equals("1")) {
			minuendData = staticData;
		} else {
			minuendData = dynamicData;
		}
		
		// 计算差值空间
		LineLimitPo spaceData = new LineLimitPo();
		spaceData.setDtype("可用容量");
		spaceData.setMcorhr(mcorhr);
		spaceData.setMdate(time);

		for (int i = 1; i < 97; i++) {
			String getAttributeMethodName = "getH" + (i < 10 ? "0" + i : i);
			String setAttributeMethodName = "setH" + (i < 10 ? "0" + i : i);
			Method getAttributeMethod = null;
			Method setAttributeMethod = null;
			try {
				getAttributeMethod = LineLimitPo.class.getDeclaredMethod(getAttributeMethodName);
				setAttributeMethod = LineLimitPo.class.getDeclaredMethod(setAttributeMethodName, Double.class);

				try {
					double value1 = 0;
					if (minuendData != null) {
						
						value1 = getAttributeMethod.invoke(minuendData) == null?0:(Double) getAttributeMethod.invoke(minuendData);
					}
					double value2 = 0;
					if (beforeData != null) {
						value2 = getAttributeMethod.invoke(beforeData) == null?0:(Double) getAttributeMethod.invoke(beforeData);
					}
					double value = value1 - value2;
					setAttributeMethod.invoke(spaceData, value);
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

		map.put("可用容量", spaceData);
		map.put("日前预计划", beforeData);
		map.put("静态正向限额", staticData);
		map.put("动态正向限额", dynamicData);
		return map;
	}

	/**
	 * 获取周报图片，及可用容量数据
	 * @author 车斯剑
	 * @date 2016年11月28日下午4:14:46
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Map<String, Object> getPictureAndAblePowerData(String startTime,String endTime) {
		
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> picData = new HashMap<String, Object>();
		List<String> times = new ArrayList<String>();
		
		List<Map<String,Object>> xDatas = new ArrayList<Map<String,Object>>();
		int startWeekNum = TimeUtil.getWeekInYear(startTime);
		int endWeekNum = TimeUtil.getWeekInYear(endTime);
		double alitTotl = 0; //总平均利用率
		double ablePower =0; //累计可用容量
		List<LineDefinePo> list = lineDefineDao.getAllLineByArea("");
		String limitType = getConfigStr();
		if(list != null && list.size()>0){
			for(LineDefinePo po : list){
				String initTime = startTime;
				List<Double> values = new ArrayList<Double>();
				Map<String,Object> map_list = new LinkedHashMap<String, Object>();
				do{
					if(Long.valueOf(initTime)>Long.valueOf(endTime)){
						break;
					}
					if(!times.contains(initTime)){
						if((endWeekNum-startWeekNum)==0){
							times.add(TimeUtil.getWeekDay(initTime,"yyyyMMdd"));
						}else{
							times.add(initTime);
						}
						
					}
					//日计划值
					double dataTotal = 0;
					//交易结果值
					double dataTrade = 0;
					//可用用量
					double dataAble = 0;
					double minuend =0; //通道限额值
					//得到实体类的所有属性
					LineLimitPo minuendPo= null;
					Map<String,LineLimitPo> map =getSpaceData(po.getMcorhr(), "差值空间", initTime, "");
					LineLimitPo ablePo = map.get("可用容量");
					LineLimitPo beforePo = map.get("日前预计划");
					//LineLimitPo tradeData = selectLineLimitList(po.getMcorhr(), initTime, Enums_DeclareType.DATATYPE_TRADING_POWER, "");
					//通道交易结果
					TransTielinePo tradeData = transTielineDao.getTotalTransTielineByName(po.getMcorhr(),initTime);
					if("0".equals(limitType)){
						minuendPo = map.get("动态正向限额");
					}else if("1".equals(limitType)){
						minuendPo = map.get("静态正向限额");
					}
					if(beforePo != null){
						dataTotal += getSum(beforePo);
					}
					if(ablePo != null){ //可用容量
						
						dataAble = getSum(ablePo);
						ablePower += dataAble/4;
					}
					if(tradeData != null){
						dataTrade += tradeData.getSumQ();
					}
					if(minuendPo!=null){
						minuend +=getSum(minuendPo);
					}
					//计算联络线路的平均利用率
					double avgVal = 0;
					if((new BigDecimal(minuend)).compareTo(BigDecimal.ZERO)!=0){
						avgVal = new BigDecimal(dataTotal+dataTrade).divide(new BigDecimal(minuend), 4, BigDecimal.ROUND_HALF_UP).doubleValue();
						//avgVal = (dataTotal+dataTrade)/minuend;
						alitTotl +=avgVal*100;
					}
					values.add(Double.valueOf(CommonUtil.toFix(avgVal*100)));
					initTime = TimeUtil.getNextDayDate(initTime, "yyyyMMdd");
				}while(Long.valueOf(initTime)<=Long.valueOf(endTime));
				map_list.put("data", values);
				map_list.put("name", po.getMcorhr());
				xDatas.add(map_list);
			}
		}
		picData.put("mainData", xDatas);
		picData.put("mainTime", times);
		
		result.put("picData", picData);
		result.put("alitTotl", alitTotl);
		result.put("ablePower", ablePower);
		result.put("lineSize", list.size());
		return result;
	}

	public TransTielinePo getTransTielineById(String id) {
		return transTielineDao.getTransTielineById(id);
	}
	/**
	 * 根据当前日期更新联络线数据的状态
	 * @param mdate
	 */
	public void updateLineLimitByMdate(String mdate){
		lineLimitDao.updateLineLimitByMdate(mdate);
	}
}
