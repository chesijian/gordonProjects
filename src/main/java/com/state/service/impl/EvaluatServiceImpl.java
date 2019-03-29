package com.state.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.state.dao.IEvaluateDao;
import com.state.dao.IPathResultDao;
import com.state.dao.IssueDao;
import com.state.po.DeclareDataPo;
import com.state.po.DeclarePo;
import com.state.po.DeclareType;
import com.state.po.LineLimitPo;
import com.state.po.PathDefinePo;
import com.state.po.PathResultPo;
import com.state.po.ResultPo;
import com.state.service.IEvaluateService;
import com.state.util.CommonUtil;
import com.state.util.DateUtil;
import com.state.util.LoggerUtil;
import com.state.util.ReflectGetValueUtil;

@Service
@Transactional
public class EvaluatServiceImpl implements IEvaluateService {
	/**
	 * 撮合计算核心程序
	 */
	@Autowired
	private IEvaluateDao evaluateDao;
	@Autowired
	private IPathResultDao pathResultDao;
	@Autowired
	private IssueDao issueDao;

	private List<LineLimitPo> ZXLimitList = new ArrayList<LineLimitPo>();// 正向限额

	private List<LineLimitPo> FXLimitList = new ArrayList<LineLimitPo>();// 反向限额

	// private String date = "20160509";

	private final String ZX_LIMIT = "正向限额";

	private final String FX_LIMIT = "反向限额";

	private String userId = "";

	private static DecimalFormat decimalFormat = new DecimalFormat("0.00");

	public String computeFunticon(String userId, String time) {
		// Date tomorrow=new Date((new Date()).getTime()+1000*60*60*24);
		// date = DateUtil.format(tomorrow, "yyyyMMdd");
		this.userId = userId;
		// 卖方每个子单的申报量
		List<DeclareDataPo> everyBillSBofSaleList = evaluateDao.selectEveryDeclareValueBySale(time);
		//System.out.println("==1=="+CommonUtil.objToJson(everyBillSBofSaleList));
		Vector<Vector<String>> everyBillSBofSaleVector = ReflectGetValueUtil.getValueByField(everyBillSBofSaleList);
		//System.out.println("==2=="+CommonUtil.objToJson(everyBillSBofSaleVector));
		// 每个省每个子单申报量
		List<DeclareDataPo> everyProviceBillBuyList = evaluateDao.selectEveryProviceDecValueBySale(time);
		Vector<Vector<String>> everyProviceBillBuyOfProviceVector = ReflectGetValueUtil.getValueByField(everyProviceBillBuyList);

		// 卖方（四川）所有子单求和
		List<DeclareDataPo> saleSumList = evaluateDao.selectDeclareValueBySale(time);
		//Vector<Double> saleSumVector = ReflectGetValueUtil.getValue(saleSumList.get(0));

		// 买方（4个省）子单分别求和
		Map<String, Vector<Double>> buyMap = new HashMap<String, Vector<Double>>();
		List<ResultPo> buyOrderlist = evaluateDao.selectDeclareValueByBuy(time);
		for (ResultPo dataPo : buyOrderlist) {
			Vector<Double> vec = ReflectGetValueUtil.getValue(dataPo);
			// System.out.println(vec);
			buyMap.put(dataPo.getArea(), vec);
		}

		// 联络线96点限额计算96点通道限额
		List<PathDefinePo> pathList = evaluateDao.getPathList();
		pathResultDao.deletePathResultByDate(time);// 删除计算日所有通道的限额 --->先删后插
		for (PathDefinePo pathPo : pathList) {
			// 多个联络线，对应多个方向
			String mdirection ="";// pathPo.getMdirection();
			// 物理联络线
			String corhrName = "";
			String direction = "";
			
			for (int i = 1; i <= mdirection.length(); i++) {
				direction = String.valueOf(mdirection.charAt(i - 1));
				// 获取物理联络线
				corhrName = getCorhrName(i, pathPo);
				//获取通道限额
				getPathLimit(corhrName, direction, pathPo, time);
			}
			getFinalPathLimit(ZXLimitList, pathPo.getMpath(), this.ZX_LIMIT, time);// 通道正向限额
			getFinalPathLimit(FXLimitList, pathPo.getMpath(), this.FX_LIMIT, time);// 通道反向限额
			ZXLimitList.clear();
			FXLimitList.clear();
		}
		
		issueDao.deleteResultByDate(time);// 先清空计算日所有单子成交量
		//insertToDatabase(everyBillSBofSaleVector, time);
		//insertToDatabase(everyProviceBillBuyOfProviceVector, time);
		// getPathResultByDeclResult();//交易功率
		return "funSuccess";
	}

	private void insertToDatabase(Vector<Vector<String>> everyBillSBofSaleVector, String time) {
		//System.out.println(CommonUtil.objToJson(everyBillSBofSaleVector));
		for (Vector<String> vec : everyBillSBofSaleVector) {
			String type = vec.remove(1);
			DeclareType mytype = Enum.valueOf(DeclareType.class, type);
			type = mytype.getCode();
			//System.out.println("======"+type);
			Long id = Long.parseLong(vec.remove(0));
			DeclarePo po = evaluateDao.getDeclarePoById(id, time);

			ResultPo resultPo = new ResultPo();
			resultPo.setMdate(po.getMdate());
			resultPo.setDtime(DateUtil.format(new Date(), "yyyyMMdd HH:mm:ss"));
			resultPo.setArea(po.getArea());
			resultPo.setMname(userId);
			resultPo.setSide(po.getDrloe());
			resultPo.setDsheet(id + "");
			resultPo.setDtype(type);
			resultPo.setSumPrice(0.0);
			for (int i = 1; i < vec.size(); i++) {
				String setAttributeMethodName = "setH" + (i < 10 ? "0" + i : i);
				Method setAttributeMethod = null;
				try {
					setAttributeMethod = ResultPo.class.getDeclaredMethod(setAttributeMethodName, Double.class);
					// setAttributeMethod.invoke(resultPo,Double.parseDouble(vec.get(i)));
					setAttributeMethod.invoke(resultPo, 0.0);
				} catch (SecurityException e) {

					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}

			}

			issueDao.insertResult(resultPo);
		}

	}

	/**
	 * 计算通道限额入库
	 * 
	 * @param LimitList
	 * @param pathName
	 * @param type
	 */
	private void getFinalPathLimit(List<LineLimitPo> LimitList, String pathName, String type, String time) {
		PathResultPo po = LineLimitPo.compareLittle(LimitList);
		po.setId(CommonUtil.getUUID());
		po.setMpath(pathName);
		po.setMdate(time);
		po.setDtype(type);
		pathResultDao.insertPathResult(po);

	}

	private void getPathLimit(String corhrName, String direction, PathDefinePo pathPo, String date) {

		List<LineLimitPo> listLimit = evaluateDao.getCorhrByCorhr(corhrName, date);
		// List<LineLimitPo> listLimit = evaluateDao.getCorhrByCorhr(corhrName);
		// 正向
		LineLimitPo ZXlimitVo = null;
		// 计划
		LineLimitPo planlimitVo = null;
		// 反向
		LineLimitPo FXlimitVo = null;
		for (LineLimitPo limitPo : listLimit) {
			String corhrType = limitPo.getDtype();
			if (corhrType.equals("动态正向限额")) {
				ZXlimitVo = limitPo;

			} else if (corhrType.equals("动态方向限额")) {
				FXlimitVo = limitPo;

			} else if (corhrType.equals("日前计划值")) {
				planlimitVo = limitPo;
			}

		}

		if(ZXlimitVo == null){
			ZXlimitVo = new LineLimitPo();
		}
		if(FXlimitVo == null){
			FXlimitVo = new LineLimitPo();
		}
		if(planlimitVo == null){
			planlimitVo = new LineLimitPo();
		}
		// if(direction.equals("+")){
		// 通道正向限额--->加号时正向限额-计划值

		List<LineLimitPo> listTemp = new ArrayList<LineLimitPo>();
		listTemp.add(planlimitVo);
		listTemp.add(ZXlimitVo);
		LineLimitPo po = getMutilPoCompute(listTemp, ZX_LIMIT);
		ZXLimitList.add(po);
		listTemp.clear();
		listTemp.add(planlimitVo);
		listTemp.add(FXlimitVo);
		LineLimitPo po1 = getMutilPoCompute(listTemp, FX_LIMIT);
		FXLimitList.add(po1);
		// }else if(direction.equals("-")){
		// //通道正向限额---->减号时计划值减反向限额
		// List<LineLimitPo> listTemp = new ArrayList<LineLimitPo>();
		// listTemp.add(planlimitVo);
		// listTemp.add(FXlimitVo);
		// LineLimitPo po = getMutilPoCompute(listTemp, ZX_LIMIT);
		// ZXLimitList.add(po);
		// //通道反向限额---->减号时 正向限额-计划值
		// listTemp.clear();
		// listTemp.add(ZXlimitVo);
		// listTemp.add(planlimitVo);
		// po = getMutilPoCompute(listTemp, FX_LIMIT);
		// FXLimitList.add(po);
		// }

	}

	/**
	 * 获取卖方通道限额之和
	 */
	/*
	 * private double[] getSalePathSumLimit(String time){ List<PathResultPo>
	 * resultList = new ArrayList<PathResultPo>(); List<DeclarePo> areaList =
	 * evaluateDao.getSaleAreaList(time); for(DeclarePo po:areaList){ String
	 * area = po.getArea(); List<PathDefinePo> pathPoOfSaleList =
	 * evaluateDao.getPathDefinePoListByArea(area); for(PathDefinePo
	 * pathPoOfSale:pathPoOfSaleList){ String mpath = pathPoOfSale.getMpath();
	 * String startArea = pathPoOfSale.getStartArea(); String endArea =
	 * pathPoOfSale.getEndArea(); String type = "";
	 * if(area.equals(startArea)){//卖方地区为首端就取通道正向限额 type = "正向限额"; PathResultPo
	 * resultPo = pathResultDao.getPathResult(date, mpath,type);
	 * if(resultPo!=null){ resultList.add(resultPo); };
	 * 
	 * }else if(area.equals(endArea)){//为末端就用反向限额的相反数 type = "反向限额";
	 * PathResultPo resultPo = pathResultDao.getPathResult(date, mpath,type);
	 * resultPo = getOppositeValueOfPathResult(resultPo); if(resultPo!=null){
	 * resultList.add(resultPo); } }
	 * 
	 * } } return getSumbyList(resultList); }
	 */

	/**
	 * 获取各买方通道限额
	 */
	/*
	 * private Map<String,double[]> getEveryBuyPathSumLimit(String time){
	 * Map<String,double[]> buyAreaMap = new HashMap<String, double[]>();
	 * List<PathResultPo> resultList = null; List<DeclarePo> areaList =
	 * evaluateDao.getBuyAreaList(time); for(DeclarePo po:areaList){ String area
	 * = po.getArea(); resultList = new ArrayList<PathResultPo>();
	 * List<PathDefinePo> pathPoOfSaleList =
	 * evaluateDao.getPathDefinePoListByArea(area); for(PathDefinePo
	 * pathPoOfSale:pathPoOfSaleList){ String mpath = pathPoOfSale.getMpath();
	 * String startArea = pathPoOfSale.getStartArea(); String endArea =
	 * pathPoOfSale.getEndArea(); String type = "";
	 * if(area.equals(startArea)){//买方地区为首端就取通道反向限额的相反数 type = "反向限额";
	 * PathResultPo resultPo = pathResultDao.getPathResult(date, mpath,type);
	 * resultPo = getOppositeValueOfPathResult(resultPo); if(resultPo!=null){
	 * resultList.add(resultPo); }
	 * 
	 * }else if(area.equals(endArea)){//为末端就用正向限额 type = "正向限额"; PathResultPo
	 * resultPo = pathResultDao.getPathResult(date, mpath,type);
	 * if(resultPo!=null){ resultList.add(resultPo); } }
	 * 
	 * } buyAreaMap.put(area, getSumbyList(resultList)); }
	 * 
	 * return buyAreaMap; }
	 */

	private double[] getSumbyList(List<PathResultPo> resultList) {
		Vector<Vector<Double>> vector = getValueByField(resultList);
		double[] resultSumArray = new double[96];
		for (Vector<Double> vec : vector) {
			for (int i = 0; i < vec.size(); i++) {
				resultSumArray[i] += vec.get(i);
			}
		}
		return resultSumArray;
	}

	/*
	 * private void getPathResultByDeclResult(String time){
	 * pathResultDao.deletePathResultByDate(time); List<PathDefinePo> pathList =
	 * evaluateDao.getPathList(); for(PathDefinePo po:pathList){ String mend =
	 * po.getEndArea(); PathResultPo resultPo =
	 * evaluateDao.getDeclResultSumByArea(mend);
	 * resultPo.setMpath(po.getMpath()); resultPo.setDtype("交易功率");
	 * resultPo.setMdate(time); pathResultDao.insertPathResult(resultPo);
	 * 
	 * } }
	 */

	/**
	 * 获取物理联络线
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年8月23日下午5:28:40
	 * @param index
	 * @param pathPo
	 * @return
	 */
	private String getCorhrName(Integer index, PathDefinePo pathPo) {
		String corhrName = "";
		/*switch (index) {
		case 1:
			corhrName = pathPo.getCorhr1();
			break;
		case 2:
			corhrName = pathPo.getCorhr2();
			break;
		case 3:
			corhrName = pathPo.getCorhr3();
			break;
		case 4:
			corhrName = pathPo.getCorhr4();
			break;
		case 5:
			corhrName = pathPo.getCorhr5();
			break;
		case 6:
			corhrName = pathPo.getCorhr6();
			break;
		case 7:
			corhrName = pathPo.getCorhr7();
			break;
		case 8:
			corhrName = pathPo.getCorhr8();
			break;
		case 9:
			corhrName = pathPo.getCorhr9();
			break;
		case 10:
			corhrName = pathPo.getCorhr10();
			break;

		default:
			corhrName = "";
			break;
		}*/
		return corhrName;
	}

	/**
	 * 反射获取各个属性的值
	 * 
	 * @param lines
	 */
	public Vector<Vector<Double>> getValueByField(List<PathResultPo> lines) {
		Vector<Vector<Double>> vec = new Vector<Vector<Double>>();
		Vector<Double> unitVector = null;
		if (null == lines || lines.isEmpty()) {
			// return null;
		}
		Field[] fields = PathResultPo.class.getDeclaredFields();

		for (int i = 0; i < lines.size(); i++) {
			unitVector = new Vector<Double>();
			for (Field field : fields) {
				if (field.getName().startsWith("h")) {
					field.setAccessible(true);
					try {
						PathResultPo lineLimitPo = lines.get(i);
						Field lineField = lineLimitPo.getClass().getDeclaredField(field.getName());
						lineField.setAccessible(true);
						Double hh = (Double) lineField.get(lineLimitPo);
						unitVector.add(hh);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					}
				}
			}
			vec.add(unitVector);
		}
		return vec;

	}

	/**
	 * 反射获取各个属性的相反值
	 * 
	 * @param lines
	 */
	public PathResultPo getOppositeValueOfPathResult(PathResultPo po) {

		Field[] fields = PathResultPo.class.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().startsWith("h")) {
				field.setAccessible(true);
				try {

					Field lineField = po.getClass().getDeclaredField(field.getName());
					lineField.setAccessible(true);
					Double hh = Double.parseDouble(lineField.get(po) == null ? "0" : lineField.get(po).toString());
					hh = Double.parseDouble("-" + hh);
					String setAttributeMethodName = "set" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
					Method setAttributeMethod = PathResultPo.class.getDeclaredMethod(setAttributeMethodName, Double.class);
					setAttributeMethod.invoke(po, hh);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return po;

	}

	/**
	 * 通过vector给对象属性赋值
	 * 
	 * @param lines
	 */
	public ResultPo getPobyVector(ResultPo po, Vector<String> vector) {

		Field[] fields = ResultPo.class.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().startsWith("h")) {
				field.setAccessible(true);
				try {

					String setAttributeMethodName = "set" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
					Method setAttributeMethod = ResultPo.class.getDeclaredMethod(setAttributeMethodName, Double.class);
					setAttributeMethod.invoke(po, 0.0);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}

		return po;

	}

	/**
	 * 两个对象每个点的值相减（如果 反向限额为正取反）
	 * 
	 * @param lines
	 * @param type
	 * @return
	 */
	public LineLimitPo getMutilPoCompute(List<LineLimitPo> lines, String type) {
		if (null == lines || lines.isEmpty()) {
			return null;
		}
		LineLimitPo lineLimitPo = null;
		LineLimitPo firstPo = lines.get(0);
		Field[] fields = LineLimitPo.class.getDeclaredFields();
		//LoggerUtil.log(this.getClass().getName(), "lines=="+CommonUtil.objToJson(lines), 0);
		for (Field field : fields) {
			if (field.getName().startsWith("h")) {
				field.setAccessible(true);
				try {
					//LoggerUtil.log(this.getClass().getName(), "firstPo=="+firstPo, 0);
					Field lineField1 = firstPo.getClass().getDeclaredField(field.getName());
					lineField1.setAccessible(true);
					double plan = (Double) lineField1.get(firstPo);// 计划值

					lineLimitPo = lines.get(1);
					Field lineField = lineLimitPo.getClass().getDeclaredField(field.getName());
					lineField.setAccessible(true);
					double limit = (Double) lineField.get(lineLimitPo);// 上限or下限

					String setAttributeMethodName = "set" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
					Method setAttributeMethod = LineLimitPo.class.getDeclaredMethod(setAttributeMethodName, double.class);
					if (type.equals(FX_LIMIT)) {
						// =============通道反向限额==================
						if (plan >= 0) {// 计划值为正,撮合反向剩余力=限额反向剩余力
							setAttributeMethod.invoke(lineLimitPo, limit);// 反向限额
						}
						if (plan < 0) {// 计划值为负，撮合反向剩余力=限额反向剩余力-计划值
							setAttributeMethod.invoke(lineLimitPo, limit - plan);// 反向限额
						}
					} else {
						// =============通道正向限额==================
						if (plan >= 0) {// 计划值为正，撮合正向剩余力=限额正向剩余力-计划值
							setAttributeMethod.invoke(lineLimitPo, limit - plan);// 反向限额
						}
						if (plan < 0) {// 计划值为负，撮合正向剩余力=限额正向剩余力
							setAttributeMethod.invoke(lineLimitPo, limit);// 反向限额
						}
					}

				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
			}
		}

		return lineLimitPo;
	}

}
