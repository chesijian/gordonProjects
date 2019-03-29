package com.state.service.impl;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.state.annotation.LogWrite;
import com.state.controller.DeclareController;
import com.state.dao.DeclareExtraDaoI;
import com.state.dao.DeclareExtraEnDaoI;
import com.state.dao.IDeclareDao;
import com.state.dao.IDeclareDataDao;
import com.state.dao.DealResultDaoI;
import com.state.dao.TypeDao;
import com.state.enums.Enums_DeclareType;
import com.state.enums.Enums_SessionType;
import com.state.enums.Enums_SystemConst;
import com.state.exception.MsgException;
import com.state.po.AreaPo;
import com.state.po.DeclareDataPo;
import com.state.po.DeclareExtraEnPo;
import com.state.po.DeclareExtraPo;
import com.state.po.DeclarePo;
import com.state.po.ResultPo;
import com.state.po.TypePo;
import com.state.po.UserPo;
import com.state.security.Des3Util;
import com.state.service.AreaService;
import com.state.service.IDeclareService;
import com.state.service.PfLoggerServiceI;
import com.state.service.ServiceHelper;
import com.state.util.CommonUtil;
import com.state.util.DatUtil;
import com.state.util.DeclareUtil;
import com.state.util.FileManager;
import com.state.util.LoggerUtil;
import com.state.util.SessionUtil;
import com.state.util.SystemTools;
import com.state.util.sys.SystemConstUtil;

@Service
@Transactional
public class DeclareServiceImpl implements IDeclareService {

	@Autowired
	private IDeclareDao declareDao;

	@Autowired
	private IDeclareDataDao declareDataDao;

	@Autowired
	private DeclareExtraEnDaoI declareExtraEnDao;
	@Autowired
	private DeclareExtraDaoI declareExtraDao;

	@Autowired
	private TypeDao typeDao;

	/**
	 * 加解密
	 */
	private Des3Util desUtil = Des3Util.getInstance();

	/**
	 * 保存多区间，多报价
	 * 
	 * @throws MsgException
	 *             , Exception
	 */
	// @LogWrite(modelName="购售电申报单",operateName="保存数据")
	public DeclarePo saveDeclare(String id, String area, String drole, String startDate, String endDate, String time, String data) throws MsgException, Exception {
		UserPo user = SessionUtil.getUserPo();
		if (area == null) {
			area = user.getArea();
		}
		if (drole == null) {
			drole = user.getDrole();
		}
		String dcode = (String) SessionUtil.getAttribute(Enums_SessionType.DCODE.getValue());
		String sheetName = "";
		String operateStr = null;
		if (CommonUtil.ifEmpty(id)) {
			operateStr = "修改";
		} else {
			operateStr = "新增";
		}
		// 开始与结束日期区间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat namedf = new SimpleDateFormat("HHmmss");
		List<Date> lDate = new ArrayList<Date>();
		DeclarePo result = null;
		String sheet_name = null;
		try {
			try {
				if (startDate == null || startDate.equals("") || startDate.equals("undefined") || endDate == null || endDate.equals("") || startDate.equals("undefined")) {
					startDate = time;
					endDate = time;
				}
				if (startDate.indexOf("-") > -1) {
					Date dBegin = df.parse(startDate);
					Date dEnd = df.parse(endDate);
					lDate = findDates(dBegin, dEnd);
				} else {
					Date dBegin = sdf.parse(startDate);
					Date dEnd = sdf.parse(endDate);
					lDate = findDates(dBegin, dEnd);
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
				throw new MsgException("日期格式不对！");
			}
			// System.out.println("--------111-------");
			for (Date date : lDate) {
				// System.out.println("date==========="+sdf.format(date));

				DeclarePo declare = new DeclarePo();
				// 如果是新增
				if (!CommonUtil.ifEmpty(id)) {
					if (CommonUtil.ifEmpty(sheetName)) {
						if (drole.equals("sale")) {
							declare.setSheetName(sheetName + this.getLastSn(date, user.getDrole()));
						} else if (drole.equals("buy")) {
							declare.setSheetName(sheetName + this.getLastSn(date, user.getDrole()));
						}
					} else {
						declare.setSheetName(dcode + this.getLastSn(date, user.getDrole()));
						/*
						 * if (drole.equals("sale")) { declare.setSheetName("卖单"
						 * + this.getLastSn(date, user.getDrole())); } else if
						 * (drole.equals("buy")) { declare.setSheetName("买单" +
						 * this.getLastSn(date, user.getDrole())); }
						 */
					}
					declare.setMdate(sdf.format(date));// 交易日期
					declare.setStartDate(startDate);
					declare.setEndDate(endDate);
					declare.setArea(area);
					declare.setDtime(sdf.format(new Date()));// 当前时间
					declare.setMname(user.getMname());
					declare.setDrloe(drole);
					declare.setClearstate("0");

					declare.setDescr("填写");
					
					// System.out.println("======" +
					// CommonUtil.objToJson(declare));

					declareDao.insertDeclare(declare);

					clearLogForDecl(String.valueOf(declare.getId()), declare.getMdate(), SessionUtil.getUserPo().getId(), Enums_SystemConst.SIGN_TYPE_DECLARE_MODIFY.getValue());
					addLog(String.valueOf(declare.getId()), declare.getMdate(), SessionUtil.getUserPo().getId(), Enums_SystemConst.SIGN_TYPE_DECLARE_MODIFY.getValue());

					DeclareDataPo data3a = new DeclareDataPo();
					data3a.setId(declare.getId());
					data3a.setSumPrice(0d);
					data3a.setSumQ(0d);
					data3a.setDtype(Enums_DeclareType.ALLDAY.getValue());
					declareDataDao.insertDeclData(data3a);
					sheet_name = declare.getSheetName();
					String json = CommonUtil.objToJson(declare);
					ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), DeclareController.MODEL_NAME_, "新增申报单，单号：" + sheet_name, "getDeclare", json, json, true);

				} else {
					// declare.setId(Long.parseLong(id));
					declare = declareDao.getDeclares(Long.parseLong(id));
					clearLogForDecl(String.valueOf(id), declare.getMdate(), SessionUtil.getUserPo().getId(), Enums_SystemConst.SIGN_TYPE_DECLARE_MODIFY.getValue());
					addLog(String.valueOf(id), declare.getMdate(), SessionUtil.getUserPo().getId(), Enums_SystemConst.SIGN_TYPE_DECLARE_MODIFY.getValue());

				}
				declareExtraEnDao.delete(String.valueOf(declare.getId()));
				// System.out.println(CommonUtil.objToJson(declare));

				List<Map<String, String>> list = CommonUtil.jsonToListMapStr(data);
				DeclareExtraEnPo dpo = null;
				// System.out.println("======="+list.size());
				for (Map<String, String> map : list) {
					if (CommonUtil.ifEmpty(map.get("startTime")) && CommonUtil.ifEmpty(map.get("endTime"))) {
						String cdata = map.get("data");
						// System.out.println(cdata);
						List<Map<String, String>> clist = CommonUtil.jsonToListMapStr(cdata);
						for (Map<String, String> temp : clist) {

							dpo = new DeclareExtraEnPo();
							dpo.setId(CommonUtil.getUUID());
							dpo.setStartTime(map.get("startTime"));
							dpo.setEndTime(map.get("endTime"));
							dpo.setSheetUid(String.valueOf(declare.getId()));
							dpo.setPower(temp.get("power"));
							dpo.setPrice(temp.get("price"));
							// System.out.println("===="+CommonUtil.objToJson(dpo));
							insertExtraEn(dpo);
						}
					}
				}
			}

			// 说明是修改
			if (CommonUtil.ifEmpty(id)) {
				result = this.getDeclare(Long.parseLong(id));
				sheet_name = result.getSheetName();
				String[] params = { id, area, drole, startDate, endDate, time, data };

				ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), DeclareController.MODEL_NAME_, "修改申报单，单号：" + sheet_name, "getDeclare", CommonUtil.objToJson(params), CommonUtil.objToJson(result), true);

			} else {
				result = this.getLastDeclare(area, startDate);
			}
		} catch (MsgException me) {
			String[] params = { id, area, drole, startDate, endDate, time, data };
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), DeclareController.MODEL_NAME_, operateStr + "申报单，单号：" + sheet_name, "getDeclare", CommonUtil.objToJson(params), CommonUtil.objToJson(result), false);
			throw me;
		} catch (Exception e) {
			String[] params = { id, area, drole, startDate, endDate, time, data };
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), DeclareController.MODEL_NAME_, operateStr + "申报单，单号：" + sheet_name, "getDeclare", CommonUtil.objToJson(params), CommonUtil.objToJson(result), false);

			throw e;
		}
		// System.out.println(area+"========"+startDate);

		return result;
	}

	/**
	 * 插入电力电价表
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年10月12日下午4:52:12
	 * @param dpo
	 */
	private void insertExtraEn(DeclareExtraEnPo dpo) {
		dpo.setStartTime(desUtil.encode(dpo.getStartTime()));
		dpo.setEndTime(desUtil.encode(dpo.getEndTime()));
		dpo.setPower(desUtil.encode(dpo.getPower()));
		dpo.setPrice(desUtil.encode(dpo.getPrice()));
		// System.out.println(CommonUtil.objToJson(dpo));
		declareExtraEnDao.insert(dpo);
	}

	/**
	 * 处理导入
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月9日下午5:50:47
	 * @param id
	 * @param area
	 * @param drole
	 * @param startDate
	 * @param endDate
	 * @param time
	 * @param data
	 * @return
	 * @throws Exception
	 */
	// @LogWrite(modelName = "购售电申报单", operateName = "保存数据")
	public DeclarePo saveDeclare(String id, String mname,String area, String drole, String startDate, String endDate, String time, Integer status, List<DeclareExtraEnPo> data) throws Exception {
		UserPo user = SessionUtil.getUserPo();
		if (area == null) {
			area = user.getArea();
		}
		if (drole == null) {
			drole = user.getDrole();
		}
		String sheetName = "";
		String operateStr = null;
		if (CommonUtil.ifEmpty(id)) {
			operateStr = "修改";
		} else {
			operateStr = "导入新增";
		}

		// 开始与结束日期区间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat namedf = new SimpleDateFormat("HHmmss");
		List<Date> lDate = new ArrayList<Date>();
		DeclarePo result = null;
		String sheet_name = null;
		try {
			try {
				if (startDate == null || startDate.equals("") || startDate.equals("undefined") || endDate == null || endDate.equals("") || startDate.equals("undefined")) {
					startDate = time;
					endDate = time;
				}
				if (startDate.indexOf("-") > -1) {
					Date dBegin = df.parse(startDate);
					Date dEnd = df.parse(endDate);
					lDate = findDates(dBegin, dEnd);
				} else {
					Date dBegin = sdf.parse(startDate);
					Date dEnd = sdf.parse(endDate);
					lDate = findDates(dBegin, dEnd);
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
				throw new MsgException("日期格式不对！");
			}
			String dcode = ServiceHelper.getBean(AreaService.class).getDcodeByArea(area);
			// System.out.println("--------111-------");
			for (Date date : lDate) {
				// System.out.println("date==========="+sdf.format(date));

				DeclarePo declare = new DeclarePo();
				// 如果是新增
				if (!CommonUtil.ifEmpty(id)) {
					if (CommonUtil.ifEmpty(sheetName)) {
						if (drole.equals("sale")) {
							declare.setSheetName(sheetName + this.getLastSn(date, drole));
						} else if (drole.equals("buy")) {
							declare.setSheetName(sheetName + this.getLastSn(date, drole));
						}
					} else {
						if (drole.equals("sale")) {
							declare.setSheetName(dcode+ this.getLastSn(date, drole));
						} else if (drole.equals("buy")) {
							declare.setSheetName(dcode+ this.getLastSn(date, drole));
						}
					}
					declare.setStatus(status);
					declare.setMdate(sdf.format(date));// 交易日期
					declare.setStartDate(startDate);
					declare.setEndDate(endDate);
					declare.setArea(area);
					declare.setDtime(sdf.format(new Date()));// 当前时间
					declare.setMname(mname == null?user.getMname():mname);
					declare.setDrloe(drole);
					declare.setClearstate("0");
					
					declare.setDescr("导入");
					
					declareDao.insertDeclare(declare);

					DeclareDataPo data3a = new DeclareDataPo();
					data3a.setId(declare.getId());
					data3a.setSumPrice(0d);
					data3a.setSumQ(0d);
					data3a.setDtype(Enums_DeclareType.ALLDAY.getValue());
					declareDataDao.insertDeclData(data3a);

					sheet_name = declare.getSheetName();

					String json = CommonUtil.objToJson(declare);
					ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), DeclareController.MODEL_NAME_, "导入新增申报单，单号：" + sheet_name, "getDeclare", json, json, true);

				} else {
					declare.setId(Long.parseLong(id));
				}
				declareExtraEnDao.delete(String.valueOf(declare.getId()));
				// System.out.println(CommonUtil.objToJson(declare));
				if (data != null) {
					for (DeclareExtraEnPo dpo : data) {
						dpo.setId(CommonUtil.getUUID());
						dpo.setSheetUid(String.valueOf(declare.getId()));
						insertExtraEn(dpo);
					}
				}
			}
			result = this.getLastDeclare(area, startDate);

		} catch (MsgException me) {
			String[] params = { id, area, drole, startDate, endDate, time, CommonUtil.objToJson(data) };

			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), DeclareController.MODEL_NAME_, operateStr + "申报单，单号：" + sheet_name, "getDeclare", CommonUtil.objToJson(params), CommonUtil.objToJson(result), false);
			throw me;
		} catch (Exception e) {
			String[] params = { id, area, drole, startDate, endDate, time, CommonUtil.objToJson(data) };
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), DeclareController.MODEL_NAME_, operateStr + "申报单，单号：" + sheet_name, "getDeclare", CommonUtil.objToJson(params), CommonUtil.objToJson(result), false);

			throw e;
		}

		return result;
	}

	/**
	 * 解密查询报价曲线数据并进行数据转换
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年10月12日下午5:04:51
	 * @param sheetUid
	 * @param price
	 * @return
	 */
	public List<DeclareExtraPo> selectListBySheetUid(String sheetUid, Float price) {
		List<DeclareExtraEnPo> list = declareExtraEnDao.selectListBySheetUid(sheetUid, price);
		List<DeclareExtraPo> declareExtras = new ArrayList<DeclareExtraPo>();
		if (list != null && list.size() > 0) {
			for (DeclareExtraEnPo po : list) {
				DeclareExtraPo temp = new DeclareExtraPo();
				temp.setId(po.getId());
				temp.setSheetUid(po.getSheetUid());
				temp.setPower(Float.parseFloat(desUtil.decode(po.getPower())));
				temp.setPrice(Float.parseFloat(desUtil.decode(po.getPrice())));
				temp.setStartTime(desUtil.decode(po.getStartTime()));
				temp.setEndTime(desUtil.decode(po.getEndTime()));
				declareExtras.add(temp);
			}
		} else {
			declareExtras = declareExtraDao.selectListBySheetUid(sheetUid, price);
		}
		return declareExtras;
	}

	/**
	 * 获取未加密的电力电价曲线表数据
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年10月13日下午3:28:30
	 * @param sheetUid
	 * @param price
	 * @return
	 */
	public List<DeclareExtraPo> selectDeclareExtraListBySheetUid(String sheetUid, Float price) {
		List<DeclareExtraPo> declareExtras = declareExtraDao.selectListBySheetUid(sheetUid, price);
		return declareExtras;
	}

	/**
	 * 获取当个单子的电力曲线信息
	 */
	public Map<Float, Map<String, Float>> getDeclareExtraData(long id, Float price) {
		List<DeclareExtraPo> list = selectListBySheetUid(String.valueOf(id), price);
		Map<Float, Map<String, Float>> result = new TreeMap<Float, Map<String, Float>>(new Comparator<Float>() {
			/*
			 * int compare(Object o1, Object o2) 返回一个基本类型的整型， 返回负数表示：o1 小于o2，
			 * 返回0 表示：o1和o2相等， 返回正数表示：o1大于o2。
			 */
			public int compare(Float i1, Float i2) {
				// float i1 = Float.parseFloat(s1);
				// float i2 = Float.parseFloat(s2);

				// 指定排序器按照顺序排列
				return i2 > i1 ? -1 : i1 == i2 ? 0 : 1;
			}
		});
		// System.out.println(CommonUtil.objToJson(list));
		if (list != null) {
			Map<String, Float> temp = null;
			String startTime = null;
			String endTime = null;
			String hstr;
			for (DeclareExtraPo po : list) {
				// String priceKey = String.valueOf(po.getPrice());
				Float tempKey = null;
				Iterator<Float> it = result.keySet().iterator();

				Object value;
				while (it.hasNext()) {
					Float tkey = it.next();
					if (tkey != null) {
						if ((tkey - po.getPrice()) == 0) {
							tempKey = tkey;
						}
					}

				}
				if (tempKey != null) {
					temp = result.get(tempKey);

				} else {
					temp = new HashMap<String, Float>();
					for (int i = 1; i < 97; i++) {
						hstr = String.valueOf((100 + i));
						hstr = hstr.substring(1);
						hstr = "h" + hstr;
						temp.put(hstr, 0f);
					}
					result.put(po.getPrice(), temp);
				}
				startTime = po.getStartTime();
				endTime = po.getEndTime();
				for (int i = DeclareUtil.getIndex(startTime); i <= DeclareUtil.getIndex(endTime); i++) {
					hstr = String.valueOf((100 + i));
					hstr = hstr.substring(1);
					hstr = "h" + hstr;
					if (temp.containsKey(hstr)) {
						temp.put(hstr, temp.get(hstr) + po.getPower());
					} else {
						temp.put(hstr, po.getPower());
					}

				}
			}
		}

		return result;
	}

	public List<Map<String, Object>> getDeclareExtraData(long id) {
		List<DeclareExtraPo> list = selectListBySheetUid(String.valueOf(id), null);
		// System.out.println(CommonUtil.objToJson(list));
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		TreeMap<String, TreeMap<Float, Float>> map = new TreeMap<String, TreeMap<Float, Float>>(new Comparator<String>() {
			public int compare(String o1, String o2) {

				// 指定排序器按照顺序排列
				return o1.compareTo(o2);
			}
		});
		if (list != null) {
			TreeMap<Float, Float> temp = null;
			for (DeclareExtraPo po : list) {
				if (map.containsKey(po.getStartTime() + "," + po.getEndTime())) {
					map.get(po.getStartTime() + "," + po.getEndTime()).put(po.getPower(), po.getPrice());
				} else {
					temp = new TreeMap<Float, Float>(new Comparator<Float>() {
						/*
						 * int compare(Object o1, Object o2) 返回一个基本类型的整型，
						 * 返回负数表示：o1 小于o2， 返回0 表示：o1和o2相等， 返回正数表示：o1大于o2。
						 */
						public int compare(Float i1, Float i2) {

							// 指定排序器按照顺序排列
							return i2 > i1 ? -1 : i1 == i2 ? 0 : 1;
						}
					});
					temp.put(po.getPower(), po.getPrice());
					map.put(po.getStartTime() + "," + po.getEndTime(), temp);
				}
			}
			// System.out.println(CommonUtil.objToJson(map));
			Map<String, Object> t = null;
			List<Map<String, Float>> subData = null;
			Map<String, Float> tt = null;
			for (Map.Entry<String, TreeMap<Float, Float>> m : map.entrySet()) {
				String[] strs = m.getKey().split(",");
				t = new HashMap<String, Object>();
				t.put("startTime", strs[0]);
				t.put("endTime", strs[1]);
				subData = new ArrayList<Map<String, Float>>();
				// System.out.println(m.getKey());
				for (Map.Entry<Float, Float> mm : m.getValue().entrySet()) {
					tt = new HashMap<String, Float>();
					tt.put("power", mm.getKey());
					tt.put("price", mm.getValue());
					subData.add(tt);
				}
				t.put("data", subData);
				result.add(t);

			}
		}
		// System.out.println(CommonUtil.objToJson(result));
		return result;
	}

	/**
	 * 保存96值
	 */
	@LogWrite(modelName = "购售电申报单", operateName = "保存96值数据")
	public void saveDeclareData(String id, String data) {
		// TODO Auto-generated method stub
		DeclarePo po = declareDao.getDeclares(Long.parseLong(id));
		if (po == null) {
			throw new MsgException("该单子已经不存在！");
		}
		Map<String, Double> map = null;
		try {
			map = JSON.parseObject(data, new TypeReference<Map<String, Double>>() {
			});

		} catch (Exception e) {
			throw new MsgException("96值数据格式错误！");
		}
		if (map == null) {
			throw new MsgException("96值错误！");
		}
		DeclareDataPo declareData = declareDataDao.getDeclDataPoById(po.getId());
		if (declareData != null) {
			declareData.setId(po.getId());
			declareData.setSumPrice(declareData.getSumPrice());
			double sumQ = 0;
			double davg = 0;
			double sumNum = 0;
			TypePo typePo = getTimeType();
			for (int j = 1; j <= 96; j++) {
				String getAttributeMethodName = "getH" + (j < 10 ? "0" + j : j);
				Method getAttributeMethod = null;
				String setAttributeMethodName = "setH" + (j < 10 ? "0" + j : j);
				Method setAttributeMethod = null;
				try {
					getAttributeMethod = TypePo.class.getDeclaredMethod(getAttributeMethodName);
					setAttributeMethod = DeclareDataPo.class.getDeclaredMethod(setAttributeMethodName, Double.class);
					try {
						String corhr = (String) getAttributeMethod.invoke(typePo);
						// if(corhr.equals("谷")){
						setAttributeMethod.invoke(declareData, map.get("h" + CommonUtil.decimal(2, j)));
						// }
						sumNum = sumNum + map.get("h" + CommonUtil.decimal(2, j));
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
			declareData.setSumQ(sumNum / 4);
			declareData.setAveP(sumNum / 96);
			declareData.setDtype(Enums_DeclareType.ALLDAY.getValue());

			declareDataDao.updateDeclData(declareData);
		}
	}

	/**
	 * 根据日期和类型获取最新单号
	 * 
	 * @param date
	 * @param drloe
	 * @return
	 */
	private String getLastSn(Date date, String drloe) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMdd");
		String result = declareDao.getLastSn(sdf.format(date), drloe);
		// System.out.println("result========="+result);
		String sn = null;
		if (CommonUtil.ifEmpty(result)) {
			// sn = "卖单"+sdf.format(date)+"001";
			int num = Integer.parseInt(result.substring(result.length() - 2, result.length()));

			int count = 0;
			do {
				num++;
				sn = sdf.format(date) + CommonUtil.decimal(2, num);
				count = declareDao.getIsExtisSn(sn, drloe);

			} while (count != 0);
		} else {
			sn = sdf.format(date) + "01";
		}
		return sn;
	}

	// 区间日期方法
	public static List<Date> findDates(Date dBegin, Date dEnd) {
		List lDate = new ArrayList();
		lDate.add(dBegin);
		Calendar calBegin = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calBegin.setTime(dBegin);
		Calendar calEnd = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		calEnd.setTime(dEnd);
		// 测试此日期是否在指定日期之后
		while (dEnd.after(calBegin.getTime())) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			lDate.add(calBegin.getTime());
		}
		return lDate;
	}

	public boolean existsDeclare(Long id) {

		return declareDao.countDeclareById(id) > 0;
	}

	@LogWrite(modelName = "购售电申报单", operateName = "删除单据")
	public void deleteDeclare(Long id) {

		declareDataDao.deleteDeclareData(id);
		declareExtraDao.delete(String.valueOf(id));
		declareExtraEnDao.delete(String.valueOf(id));
		declareDao.deleteDeclare(id);
		declareDao.deleteLogBySheet(id);
	}

	public List<DeclarePo> getDeclares(String area, String time) {

		// Date tomrrow = new Date((new Date()).getTime()+1000*60*60*24);
		// return declareDao.selectDeclareByParam(area, DateUtil.format(tomrrow,
		// "yyyyMMdd"), null);
		return declareDao.selectDeclareByParam(area, time, null);
	}

	public DeclareDataPo getDeclareData(Long id, String dtype) {
		List<DeclareDataPo> list = declareDataDao.getDeclDataById(id, dtype);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@LogWrite(modelName = "购售电申报单", operateName = "修改数据")
	public void updateDeclare(DeclarePo declarePo) {
		// System.out.println("---------------");
		declareDao.updateDeclare(declarePo);

		List<DeclareDataPo> declareDatas = declarePo.getDeclareDatas();
		// System.out.println("===="+CommonUtil.objToJson(declarePo.getDeclareDatas()));
		for (DeclareDataPo declareDataPo : declareDatas) {
			declareDataDao.updateDeclData(declareDataPo);
		}
	}

	/**
	 * 查询时段类型
	 * 
	 * @return
	 */
	public TypePo getTimeType() {
		return typeDao.getType().get(0);
	}

	/**
	 * 统计买卖单数
	 * 
	 * @return
	 */
	public List<String> getSheetDeclNum(String time) {
		// TODO Auto-generated method stub
		return declareDao.getSheetDeclNum(time);
	}

	public List<DeclarePo> queryForList(String time, String area, Integer status) {
		return declareDao.queryForList(time, area, status);
	}

	public DeclareDataPo getDeclareSumData(List<Long> id) {

		List<DeclareDataPo> list = declareDataDao.getDeclDataByIds(id);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;

	}

	// @LogWrite(modelName="购售电申报单",operateName="提交单据")
	public void updateDeclareStatus(String ids) {
		String[] str = ids.split(",");
		long[] idArr = new long[str.length];
		int i = 0;
		for (String temp : str) {
			idArr[i++] = Long.parseLong(temp);
		}
		String submitId = SessionUtil.getAddName();
		Date submitTime = new Date();
		declareDao.updateDeclareStatus(idArr, 1, submitId, submitTime);
	}

	// @LogWrite(modelName="购售电申报单",operateName="查看单个单子")
	public DeclarePo getDeclare(long id) {
		// TODO Auto-generated method stub
		DeclarePo po = declareDao.getDeclares(id);

		return po;
	}

	/**
	 * 获取某个日期的最新单子
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年8月29日下午3:14:36
	 * @param mdate
	 * @return
	 */
	public DeclarePo getLastDeclare(String area, String mdate) {
		mdate = mdate.replace("-", "");
		return declareDao.getLastDeclare(area, mdate);
	}

	/**
	 * 解析获取单个单子，96个时段的报价曲线
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年8月25日下午4:20:01
	 * @param id
	 * @return
	 */
	public TreeMap<Integer, TreeMap<Float, Float>> getDeclareIntervalData(long id) {
		// System.out.println("==="+declareExtraDao);
		List<DeclareExtraPo> list = selectListBySheetUid(String.valueOf(id), null);
		// 20160920旧的
		TreeMap<Integer, TreeMap<Float, Float>> extraData = DeclareUtil.analysisDecExtraData(list, false, false);
		// 20160921
		// TreeMap<Integer, TreeMap<Float, Float>> extraData =
		// DeclareUtil.analysisDecExtraData(list, false, false);

		// Map<DeclarePo, TreeMap<Integer, TreeMap<Float, Float>>> decMap =
		// DeclareUtil.analysisDec(dslist);
		return extraData;
	}

	/**
	 * 解析获取单个单子，96个时段的报价曲线 新的
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月21日下午12:17:00
	 * @param id
	 * @return
	 */
	public Map<String, Object> getDeclareIntervalData1(long id) {
		// System.out.println("==="+declareExtraDao);
		List<DeclareExtraPo> list = selectListBySheetUid(String.valueOf(id), null);
		// 20160920旧的
		// TreeMap<Integer, TreeMap<Float, Float>> extraData =
		// DeclareUtil.analysisDecExtraData1(list, false, false);

		// System.out.println("----"+extraData1);

		// 20160921
		// TreeMap<Integer, TreeMap<Float, Float>> extraData =
		// DeclareUtil.analysisDecExtraData(list, false, false);
		// 20160922
		Map<String, Object> resultData = DeclareUtil.analysisDecExtraData1(list, false, false);
		/*
		 * 
		 * Object o = resultData.get("treeData"); if(o != null){ offerMap =
		 * (TreeMap<Integer,TreeMap<Float,Float>>)o; }
		 */
		// Map<DeclarePo, TreeMap<Integer, TreeMap<Float, Float>>> decMap =
		// DeclareUtil.analysisDec(dslist);
		return resultData;
	}

	
	@LogWrite(modelName = "购售电申报单", operateName = "导入数据")
	public void importDeclare(String filePath) throws Exception {
		// TODO Auto-generated method stub
		Vector<Vector<String>> data = DatUtil.readFile(new File(filePath), "<SheetData::国调 type=全数>", "</SheetData::国调>");
		Map<String, DeclarePo> declareMap = new HashMap<String, DeclarePo>();
		Map<String, Object> declareInfo = null;
		DeclarePo po = null;
		List<DeclareExtraEnPo> declareExtras = null;
		StringBuilder isExistSb = new StringBuilder();
		for (Vector<String> unit : data) {
			// 判断该单子是否存在
			if (declareMap.containsKey(unit.get(2))) {
				po = declareMap.get(unit.get(2));
				DeclareExtraEnPo dxpo = new DeclareExtraEnPo();
				dxpo.setStartTime(unit.get(7));
				dxpo.setEndTime(unit.get(8));
				dxpo.setPower((unit.get(9)));
				dxpo.setPrice((unit.get(9)));
				po.getDeclareExtraEns().add(dxpo);
			} else {
				// 创建申报单的info类
				// declareInfo = new HashMap<String,Object>();
				// 设置基本信息
				// declareInfo.put("sheetName", unit.get(2));
				po = new DeclarePo();
				po.setArea(unit.get(3));
				po.setMdate(unit.get(4));
				po.setDtime(unit.get(5));
				po.setDrloe(unit.get(6));
				declareExtras = new ArrayList<DeclareExtraEnPo>();
				DeclareExtraEnPo dxpo = new DeclareExtraEnPo();
				dxpo.setStartTime(unit.get(7));
				dxpo.setEndTime(unit.get(8));
				dxpo.setPower((unit.get(9)));
				dxpo.setPrice((unit.get(10)));
				declareExtras.add(dxpo);
				po.setDeclareExtraEns(declareExtras);
				declareMap.put(unit.get(2), po);
			}
		}
		// System.out.println(CommonUtil.objToJson(declareMap));

		for (Map.Entry<String, DeclarePo> entry : declareMap.entrySet()) {
			po = entry.getValue();
			// 保存到数据库
			this.saveDeclare(null,null, po.getArea(), po.getDrloe(), po.getMdate(), po.getMdate(), po.getDtime(), null, po.getDeclareExtraEns());

		}

	}

	/**
	 * 通过小邮件获取各个省调的上报数据
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月19日下午3:19:38
	 * @param path
	 * @param time
	 * @return
	 * @throws Exception
	 */
	public String readFile(String path, String mdate) throws Exception {
		File file = new File(path);
		File[] array = file.listFiles();
		StringBuilder result = new StringBuilder();
		String recvdSrc = SystemConstUtil.getMatchPath() + File.separator + "trans_data" + File.separator + "recvd" + File.separator;
		File f = new File(recvdSrc);
		if (!f.exists()) {
			f.mkdirs();
		}
		StringBuilder isExistSb = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			// System.out.println(array[i].getPath()+"==="+"CBPM_国调_result_" +
			// time + ".dat");
			String filePath = null;
			String fileName = array[i].getName();
			if (array[i].getName().startsWith("CBPM_BIDOFFER_") &&  fileName.contains(mdate)  && !fileName.contains("_SESSION") && fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).equals("dat")) {// 根据日期匹配文件
				filePath = array[i].getPath();

				String area = null;
				LoggerUtil.log(this.getClass().getName(), "++++正在解析文件++++" + filePath, 0);

				DeclarePo po = new DeclarePo();
				Vector<Vector<String>> data = DatUtil.readFile2(new File(filePath), "<SubmitInfo::", "</SubmitInfo::");
				if(data  != null){
					if(data.size()>2){
						po.setMname(data.get(1).get(3));
					}
				}
				
				data = DatUtil.readFile2(new File(filePath), "<BidOfferData::", "</BidOfferData::");
				Map<String, DeclarePo> declareMap = new HashMap<String, DeclarePo>();

				

				List<DeclareExtraEnPo> declareExtras = null;
				declareExtras = new ArrayList<DeclareExtraEnPo>();
				po.setDeclareExtraEns(declareExtras);
				for (Vector<String> unit : data) {
					// 判断该单子是否存在

					// 创建申报单的info类
					// declareInfo = new HashMap<String,Object>();
					// 设置基本信息
					// declareInfo.put("sheetName", unit.get(2));

					po.setArea(unit.get(2));
					area = po.getArea();
					po.setMdate(mdate);
					po.setDtime(CommonUtil.getSimpleDate());
					po.setStatus(1);
					po.setDrloe(unit.get(4).equals("买方") ? "buy" : "sale");

					DeclareExtraEnPo dxpo = new DeclareExtraEnPo();
					dxpo.setStartTime(unit.get(5));
					dxpo.setEndTime(unit.get(6));
					dxpo.setPower(unit.get(7));
					dxpo.setPrice(unit.get(8));
					declareExtras.add(dxpo);

					// declareMap.put(unit.get(2), po);
				}
				List<DeclarePo> oldData = declareDao.queryForListByMdate(mdate, area);
				
				if(oldData!= null && oldData.size()>0){
					isExistSb.append(",").append(po.getArea());
					// 把该文件移动到recvd目录
					FileManager.copyFile(filePath, recvdSrc + fileName);
					// 删除拷贝过来的文件
					FileManager.delete(filePath);
					continue;
				}else{
					// 保存到数据库
					this.saveDeclare(null,po.getMname(), po.getArea(), po.getDrloe(), po.getMdate(), po.getMdate(), po.getDtime(), 1, po.getDeclareExtraEns());
					result.append("," + po.getArea());
				}
				
				// 把该文件移动到recvd目录
				FileManager.copyFile(filePath, recvdSrc + fileName);
				// 删除拷贝过来的文件
				FileManager.delete(filePath);

			}
		}
		if (result.length() > 0) {
			result = result.deleteCharAt(0);
			result.insert(0, "成功导入了如下省："  );
		}
		String isExistStr = "";
		if(isExistSb.length()>0){
			String isStr = isExistSb.deleteCharAt(0).toString();
			if(result.length() > 0){
				isExistStr+="；但是发现"+isStr;
			}else{
				isExistStr+="没有导入任何数据；因为发现"+isStr;
			}
			
			isExistStr+="重复报价文件，未导入";
		}
		
		result.append(isExistStr);
		//System.out.println("11----"+isExistStr);
		//System.out.println("22----"+result.toString());
		return result.toString();
	}

	/**
	 * 通过日期获取所有交易单，并组装导出word的字符串
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月25日下午6:27:24
	 * @param time
	 * @return
	 */
	public Map<String, StringBuilder> getDelcateMap(String tempPath, String time) {
		Map<String, StringBuilder> sb = new HashMap<String, StringBuilder>();
		time = time.replaceAll("-", "");
		StringBuilder declareBuySb = new StringBuilder();
		StringBuilder declareSaleSb = new StringBuilder();
		sb.put("declareBuyData", declareBuySb);
		sb.put("declareSaleData", declareSaleSb);

		StringBuilder tempSb = null;
		int indexBuy = 0;
		int indexSale = 0;
		int indexTemp;
		List<DeclarePo> list = getListAndDataByMdate(time, null,null);
		if (list != null) {
			for (DeclarePo po : list) {
				if (!po.getArea().equals("四川")) {
					// break;
				}
				boolean isBuy = false;
				if (po.getDrloe().equals(Enums_DeclareType.BUY)) {
					isBuy = true;
					tempSb = declareBuySb;
					indexBuy++;
					indexTemp = indexBuy;
				} else {
					tempSb = declareSaleSb;
					indexSale++;
					indexTemp = indexSale;
				}
				Map<Integer, Integer> statusdata = new TreeMap<Integer, Integer>(new Comparator<Integer>() {
					/*
					 * int compare(Object o1, Object o2) 返回一个基本类型的整型， 返回负数表示：o1
					 * 小于o2， 返回0 表示：o1和o2相等， 返回正数表示：o1大于o2。
					 */
					public int compare(Integer i1, Integer i2) {

						// 指定排序器按照顺序排列
						return i2 > i1 ? -1 : i1 == i2 ? 0 : 1;
					}
				});

				TreeMap<Integer, TreeMap<Float, Float>> treeMap = DeclareUtil.analysisDecExtraData(po.getDeclareExtras(), isBuy, false);
				int offset = 1;

				// System.out.println(CommonUtil.objToJson(treeMap));
				int index = 0;
				TreeMap<Float, Float> prevEnt = null;
				TreeMap<Float, Float> nowEnt = null;
				// System.out.println("size==="+treeMap.size());
				// 遍历判断每个时段的电力电价曲线是否相同，相同的用同一个颜色
				for (Map.Entry<Integer, TreeMap<Float, Float>> intEnt : treeMap.entrySet()) {
					// System.out.println("index=="+index);
					index++;
					// System.out.println("======"+index);
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
				// System.out.println("treeMap==" +
				// CommonUtil.objToJson(treeMap));
				// System.out.println("statusdata==" +
				// CommonUtil.objToJson(statusdata));
				if (statusdata == null) {
					throw new MsgException("没有数据");
				}
				tempSb.append(indexTemp + ")." + po.getArea());
				int startIndex = 1;
				int endIndex;
				int intervalIndex = 1;
				int num = 0;
				for (Entry<Integer, Integer> entry : statusdata.entrySet()) {
					if (intervalIndex == 1) {
						// tempSb.append(" ");
						startIndex = 1;
					} else {
						// tempSb.append("\t\t ");
						int valuePre = statusdata.get(startIndex);
						int valueNow = entry.getValue();
						// System.out.println("intervalIndex:"+intervalIndex+"   startIndex:"+startIndex+"  valuePre:"+valuePre+"    valueNow:"+valueNow);

						if ((valuePre - valueNow) != 0) {

							endIndex = intervalIndex - 1;
							TreeMap<Float, Float> data = treeMap.get(startIndex);
							int i = 0;
							boolean isNull = true;
							StringBuilder tSb = new StringBuilder();
							for (Entry<Float, Float> extraEntry : data.entrySet()) {
								if (extraEntry.getKey() == 0 && extraEntry.getValue() == 0) {
									continue;
								}
								isNull = false;
								if (i == 0) {
									tSb.append("第" + startIndex + "时段到第" + endIndex + "时段:  电力:" + extraEntry.getValue() + "MW,电价:" + extraEntry.getKey() + "元/MWh").append(SystemTools._BR);
								} else {
									tSb.append("\t\t\t\t\t\t\t\t  电力:" + extraEntry.getValue() + "MW,电价:" + extraEntry.getKey() + "元/MWh").append(SystemTools._BR);
								}
								i++;
							}
							if (!isNull) {
								if (num == 0) {
									tempSb.append(" ");

								} else {
									tempSb.append("\t\t ");
								}
								tempSb.append(tSb);
								num++;
							}
							startIndex = intervalIndex;
						}
					}
					intervalIndex++;
				}
				if (startIndex < intervalIndex) {
					endIndex = intervalIndex - 1;
					TreeMap<Float, Float> data = treeMap.get(startIndex);
					int i = 0;
					boolean isNull = true;
					StringBuilder tSb = new StringBuilder();
					for (Entry<Float, Float> extraEntry : data.entrySet()) {
						if (extraEntry.getKey() == 0 && extraEntry.getValue() == 0) {
							continue;
						}
						isNull = false;
						if (i == 0) {
							tSb.append("第" + startIndex + "时段到第" + endIndex + "时段:  电力:" + extraEntry.getValue() + "MW,电价:" + extraEntry.getKey() + "元/MWh").append(SystemTools._BR);
						} else {
							tSb.append("\t\t\t\t\t\t\t\t  电力:" + extraEntry.getValue() + "MW,电价:" + extraEntry.getKey() + "元/MWh").append(SystemTools._BR);
						}
						i++;
					}
					if (!isNull) {
						if (num == 0) {
							tempSb.append(" ");

						} else {
							tempSb.append("\t\t ");
						}
						tempSb.append(tSb);
					}
				}

			}
		}

		return sb;
	}

	@LogWrite(modelName = "电子签名", operateName = "单据修改记录")
	public void addLog(String sheetId, String mdate, String userId, String type) {
		declareDao.addLog(sheetId, mdate, userId, type);
	}

	@LogWrite(modelName = "电子签名", operateName = "单据修改记录清除")
	public void clearLogForDecl(String sheetId, String mdate, String userId, String type) {
		declareDao.clearLogForDecl(sheetId, mdate, userId, type);
	}

	public List<String> getUserIdList(String sheetId) {
		return declareDao.getUserIdList(sheetId);
	}

	public static void main(String[] args) {
		String fileName = "CBPM_BIDOFFER_四川_20160915.dat";
		System.out.println(fileName.substring(14, 16));
	}

	
	@LogWrite(modelName = "电子签名", operateName = "发布记录")
	public void addLogForResult(String userId, String mdate, String type) {
		// TODO Auto-generated method stub
		declareDao.addLogForResult(userId, mdate, type);
	}

	
	@LogWrite(modelName = "电子签名", operateName = "发布记录清除")
	public void clearLogForResult(String userId, String mdate, String type) {
		// TODO Auto-generated method stub
		declareDao.clearLogForResult(userId, mdate, type);
	}

	/**
	 * 解密 获取申报单和电力电价曲线
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年10月12日下午5:19:15
	 * @param mdate
	 * @param aera
	 * @return
	 */
	public List<DeclarePo> getListAndDataByMdate(String mdate, String aera,Integer status) {
		List<DeclarePo> list = declareDao.getListAndDataByMdate(mdate, aera,status);
		if (list != null && list.size() > 0) {
			for (DeclarePo dpo : list) {
				List<DeclareExtraEnPo> ens = dpo.getDeclareExtraEns();
				List<DeclareExtraPo> declareExtras = new ArrayList<DeclareExtraPo>();
				if (ens != null && ens.size() > 0) {
					for (DeclareExtraEnPo po : ens) {
						DeclareExtraPo temp = new DeclareExtraPo();
						temp.setId(po.getId());
						temp.setSheetUid(po.getSheetUid());
						temp.setPower(Float.parseFloat(desUtil.decode(po.getPower())));
						temp.setPrice(Float.parseFloat(desUtil.decode(po.getPrice())));
						temp.setStartTime(desUtil.decode(po.getStartTime()));
						temp.setEndTime(desUtil.decode(po.getEndTime()));
						declareExtras.add(temp);
					}
					dpo.setDeclareExtras(declareExtras);
				} else {
					// System.out.println("---------");
					dpo.setDeclareExtras(selectDeclareExtraListBySheetUid(String.valueOf(dpo.getId()), null));
				}

			}
		}
		// System.out.println(list.size()+"---"+CommonUtil.objToJson(list));
		return list;
	}

	/**
	 * 导出周报组装获取申报数据
	 * @description
	 * @author 大雄
	 * @date 2016年10月18日下午4:24:53
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Map<String, Object> getListAndDataByMdateInterval(String startTime, String endTime) {
		List<DeclarePo> list = declareDao.getListAndDataByMdateInterval(startTime, endTime, null);
		Map<String, Map<String, List<DeclarePo>>> buyMap = new HashMap<String, Map<String, List<DeclarePo>>>();
		Map<String, Map<String, List<DeclarePo>>> saleMap = new HashMap<String, Map<String, List<DeclarePo>>>();
		Map<String, Double> sumElectricity = new HashMap<String, Double>();
		Map<String, Object> result = new HashMap<String, Object>();
		int buySum = 0;
		int saleSum = 0;
		if (list != null && list.size() > 0) {
			for (DeclarePo dpo : list) {
				List<DeclareExtraEnPo> ens = dpo.getDeclareExtraEns();
				List<DeclareExtraPo> declareExtras = new ArrayList<DeclareExtraPo>();
				if (ens != null && ens.size() > 0) {
					for (DeclareExtraEnPo po : ens) {
						DeclareExtraPo temp = new DeclareExtraPo();
						temp.setId(po.getId());
						temp.setSheetUid(po.getSheetUid());
						temp.setPower(Float.parseFloat(desUtil.decode(po.getPower())));
						temp.setPrice(Float.parseFloat(desUtil.decode(po.getPrice())));
						temp.setStartTime(desUtil.decode(po.getStartTime()));
						temp.setEndTime(desUtil.decode(po.getEndTime()));
						declareExtras.add(temp);
					}
					dpo.setDeclareExtras(declareExtras);
				} else {
					dpo.setDeclareExtras(selectDeclareExtraListBySheetUid(String.valueOf(dpo.getId()), null));
				}
				//System.out.println("dpo==="+CommonUtil.objToJson(dpo));
				Map<String, List<DeclarePo>> dayMap = null;
				List<DeclarePo> sheetList = null;
				// 如果是买方
				if ("buy".equals(dpo.getDrloe())) {
					buySum++;
					if (buyMap.containsKey(dpo.getArea())) {
						dayMap = buyMap.get(dpo.getArea());
					} else {
						dayMap = new HashMap<String, List<DeclarePo>>();
						buyMap.put(dpo.getArea(), dayMap);
					}
				} else {
					saleSum++;
					if (saleMap.containsKey(dpo.getArea())) {
						dayMap = saleMap.get(dpo.getArea());
					} else {
						dayMap = new HashMap<String, List<DeclarePo>>();
						saleMap.put(dpo.getArea(), dayMap);
					}
				}
				if (dayMap.containsKey(dpo.getMdate())) {
					sheetList = dayMap.get(dpo.getMdate());
				} else {
					sheetList = new ArrayList<DeclarePo>();
					dayMap.put(dpo.getMdate(), sheetList);
				}
				sheetList.add(dpo);
				// 计算每个单的总电量,总电力，总电价，平均电力，平均电量，平均电价
				dpo = DeclareUtil.calculate(dpo);
				
				// 统计总电量
				if (sumElectricity.containsKey(dpo.getDrloe())) {
					sumElectricity.put(dpo.getDrloe(), sumElectricity.get(dpo.getDrloe()) + dpo.getSumElectricity());
				} else {
					sumElectricity.put(dpo.getDrloe(), dpo.getSumElectricity());
				}
			}
		}
		result.put("buySumElectricity", sumElectricity.get("buy"));
		result.put("saleSumElectricity", sumElectricity.get("sale"));
		result.put("buySum",buySum);
		result.put("saleSum",saleSum);
		
		Map<String, Map<String, Double>> buyAvg = new HashMap<String, Map<String, Double>>();
		Map<String, Map<String, Double>> saleAvg = new HashMap<String, Map<String, Double>>();

		int areaLen = buyMap.size();
		
		double areaSumPower = 0;
		double areaSumPrice = 0;
		// 遍历买方计算平均电力和电价
		for (Entry<String, Map<String, List<DeclarePo>>> area : buyMap.entrySet()) {
			Map<String, List<DeclarePo>> dayMap = area.getValue();
			int dayLen = dayMap.size();
			double daySumPower = 0;
			double daySumPrice = 0;
			for (Entry<String, List<DeclarePo>> dayEnt : dayMap.entrySet()) {
				int sheetLen = dayEnt.getValue().size();
				double sumPower = 0;
				double sumPrice = 0;
				for (DeclarePo po : dayEnt.getValue()) {
					sumPower += po.getAvgPower();
					sumPrice += po.getAvgPrice();
				}
				daySumPower += (sheetLen == 0?0:sumPower / sheetLen);
				daySumPrice += (sheetLen == 0?0:sumPrice / sheetLen);
			}
			
			areaSumPower +=  (dayLen == 0?0:daySumPower / dayLen);
			areaSumPrice +=  (dayLen == 0?0:daySumPrice / dayLen);
			
			Map<String, Double> attrMap = new HashMap<String, Double>();
			attrMap.put("power",  dayLen == 0?0:Double.valueOf(CommonUtil.toFix(daySumPower / dayLen)));
			attrMap.put("price",  dayLen == 0?0:Double.valueOf(CommonUtil.toFix(daySumPrice / dayLen)));
			buyAvg.put(area.getKey(), attrMap);
		}

		result.put("buyAvgPower", areaLen==0?0:areaSumPower/areaLen);
		result.put("buyAvgPrice", areaLen==0?0:areaSumPrice/areaLen);
		result.put("buyAvgMap", buyAvg);
		
		areaLen = saleMap.size();
		areaSumPrice = 0;
		areaSumPower = 0;
		// 遍历卖方计算平均电力和电价
		for (Entry<String, Map<String, List<DeclarePo>>> area : saleMap.entrySet()) {
			Map<String, List<DeclarePo>> dayMap = area.getValue();
			int dayLen = dayMap.size();
			double daySumPower = 0;
			double daySumPrice = 0;
			for (Entry<String, List<DeclarePo>> dayEnt : dayMap.entrySet()) {
				int sheetLen = dayEnt.getValue().size();
				double sumPower = 0;
				double sumPrice = 0;
				for (DeclarePo po : dayEnt.getValue()) {
					sumPower += po.getAvgPower();
					sumPrice += po.getAvgPrice();
				}
				daySumPower += sheetLen == 0?0:sumPower / sheetLen;
				daySumPrice += sheetLen == 0?0:sumPrice / sheetLen;
			}
			
			areaSumPower +=  dayLen == 0?0:daySumPower / dayLen;
			areaSumPrice +=  dayLen == 0?0:daySumPrice / dayLen;
			
			Map<String, Double> attrMap = new HashMap<String, Double>();
			attrMap.put("power", dayLen == 0?0:Double.valueOf(CommonUtil.toFix(daySumPower / dayLen)));
			attrMap.put("price", dayLen == 0?0:Double.valueOf(CommonUtil.toFix(daySumPrice / dayLen)));
			saleAvg.put(area.getKey(), attrMap);
		}
		
		result.put("saleAvgPower", areaLen==0?0:areaSumPower/areaLen);
		result.put("saleAvgPrice", areaLen==0?0:areaSumPrice/areaLen);
		result.put("saleAvgMap", saleAvg);
		
		//System.out.println("========"+CommonUtil.objToJson(result));
		return result;
		
	}

	/**
	 * 根据日期获取各个区域的单子数量
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年10月12日下午6:21:18
	 * @param mdate
	 * @return
	 */
	public List<AreaPo> getAreaSheetCountByMdate(@Param("mdate") String mdate) {
		return declareDao.getAreaSheetCountByMdate(mdate);
	}
	/**
	 * 根据日期获取各个区域的单子状态
	 * @description
	 * @date 2016年10月12日下午6:21:18
	 * @param mdate
	 * @return
	 */
	public List<AreaPo> getAreaSheetStatusByMdate(@Param("mdate") String mdate){
		return declareDao.getAreaSheetStatusByMdate(mdate);
	}

	
	public List<DeclarePo> querySheetsByTime(String startTime, String endTime, Integer status) {
		return declareDao.querySheetsByTime(startTime, endTime, status);
	}

	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年10月14日下午5:59:03
	 * @param idList
	 * @return
	 */
	
	public Map<String, Double> getDeclareExtraDatasByIds(List<String> idList) {
		Map<String, Double> dataMap = new HashMap<String, Double>();
		String[] uids = new String[idList.size()];
		idList.toArray(uids);
		List<DeclareExtraPo> list = declareExtraDao.selectListBySheetUids(uids);
		// System.out.println("list===="+CommonUtil.objToJson(list));

		double power = 0;
		double price = 0;
		for (DeclareExtraPo po : list) {
			String startTime = po.getStartTime();
			String endTime = po.getEndTime();
			int cc = Integer.valueOf(endTime.substring(0, 2)) - Integer.valueOf(startTime.substring(0, 2));
			int dd = (Integer.valueOf(endTime.substring(3)) - Integer.valueOf(startTime.substring(3))) / 15 + 1;
			int sum = cc * 4 + dd;
			power += po.getPower() * sum;
			price += po.getPrice() * sum;

		}
		dataMap.put("power", power);
		dataMap.put("price", price);
		return dataMap;
	}

	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年11月24日下午5:40:00
	 * @param time
	 * @return
	 */
	public Long getDeclareIdByTimeAndArea(String time, String area) {
		return declareDao.getDeclareIdByTimeAndArea(time,area);
	}
}
