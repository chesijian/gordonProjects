package com.state.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.state.annotation.LogWrite;
import com.state.dao.AreaDao;
import com.state.dao.DealResultDaoI;
import com.state.dao.IDeclareDao;
import com.state.dao.IDeclareDataDao;
import com.state.dao.ILoginDao;
import com.state.dao.IPathDefineDao;
import com.state.dao.IPathResultDao;
import com.state.dao.IssueDao;
import com.state.dao.LineDefineDao;
import com.state.dao.LineLimitDao;
import com.state.dao.MatchDao;
import com.state.dao.TransTielineDao;
import com.state.dao.TypeDao;
import com.state.dao.sys.SysConfigDaoI;
import com.state.enums.Enums_DeclareType;
import com.state.enums.Enums_SystemConst;
import com.state.exception.MsgException;
import com.state.po.AreaPo;
import com.state.po.CorridorPathPo;
import com.state.po.DeclareDataPo;
import com.state.po.DeclarePo;
import com.state.po.LineDefinePo;
import com.state.po.LineLimitPo;
import com.state.po.PathDefinePo;
import com.state.po.PathResultPo;
import com.state.po.ResultPo;
import com.state.po.TransTielinePo;
import com.state.po.TypePo;
import com.state.po.extend.DataGrid;
import com.state.po.extend.JsonMsg;
import com.state.po.sys.PfSysConfigPo;
import com.state.service.IDeclareService;
import com.state.service.MatchService;
import com.state.service.ServiceHelper;
import com.state.service.SystemServiceI;
import com.state.util.CommonUtil;
import com.state.util.DatUtil;
import com.state.util.DateUtil;
import com.state.util.DeclareUtil;
import com.state.util.FileManager;
import com.state.util.LoggerUtil;
import com.state.util.MatchUtil;
import com.state.util.SessionUtil;
import com.state.util.SystemTools;
import com.state.util.TimeUtil;
import com.state.util.sys.SystemConstUtil;

@Service
@Transactional
public class MatchServiceImpl implements MatchService {
	@Autowired
	private ILoginDao loginDao;
	@Autowired
	private MatchDao matchDao;
	@Autowired
	private IPathResultDao pathResultDao;
	@Autowired
	private IssueDao issueDao;
	@Autowired
	private IDeclareDao declareDao;
	@Autowired
	private IDeclareDataDao declareDataDao;
	@Autowired
	private IPathDefineDao pathDefineDao;
	@Autowired
	private LineDefineDao lineDefineDao;
	@Autowired
	private LineLimitDao lineLimitDao;
	@Autowired
	private TypeDao typeDao;
	@Autowired
	private DealResultDaoI dealResultDao;
	@Autowired
	private IDeclareService declareService;
	@Autowired
	private TransTielineDao transTielineDao;
	@Autowired
	private SysConfigDaoI sysConfigDao;
	@Autowired
	private AreaDao areaDao;

	/**
	 * 根据通道名、类型查询通道结果
	 * 
	 * @param mpath
	 * @param dtype
	 * @return
	 */
	public PathResultPo getPathResult(String mpath, String dtype, String time) {
		// Date tomorrow=new Date((new Date()).getTime()+1000*60*60*24);
		// return pathResultDao.getPathResult(DateUtil.format(tomorrow,
		// "yyyyMMdd"), mpath, dtype);
		return pathResultDao.getPathResult(time, mpath, dtype);
	}

	/**
	 * 一键发布
	 */
	@LogWrite(modelName = "发布管理", operateName = "发布")
	public void issue() {
		Date tomorrow = new Date((new Date()).getTime() + 1000 * 60 * 60 * 24);
		pathResultDao.updatePrint(DateUtil.format(tomorrow, "yyyyMMdd"), "1");
		issueDao.updatePrint(DateUtil.format(tomorrow, "yyyyMMdd"), "1");
	}

	/**
	 * 判断申报单类型是否一致
	 */
	public String getCheckDtype(String time) {
		List<DeclarePo> dslist = declareDao.queryForListByMdate(time, "");// 遍历申报单
		String dtype = "";
		String typestr = "";
		if (dslist.size() > 0) {
			DeclareDataPo declareDataPo = declareDataDao.getDeclDataPoById(dslist.get(0).getId());// 取第一条申报单类型
			if (declareDataPo != null) {
				if (declareDataPo.getDtype().getName().equals("全天")) {
					typestr = "全天";
					dtype = "1";
				} else if (declareDataPo.getDtype().getName().equals("高峰")) {
					typestr = "高峰";
					dtype = "2";
				} else if (declareDataPo.getDtype().getName().equals("低谷")) {
					typestr = "低谷";
					dtype = "3";
				}
			}
			for (int i = 0; i < dslist.size(); i++) {// 遍历申报单，与第一条申报单对比
				if (!(declareDataDao.getDeclDataPoById(dslist.get(i).getId()).getDtype().getName().equals(typestr))) {// 判断申报单类型是否一致
					dtype = "0";
					break;
				}
			}
		}
		return dtype;
	}

	/**
	 * 创建bat文件，并写入数据 * @param path
	 * 
	 * @param time
	 */
	@LogWrite(modelName = "优化计算", operateName = "为调用c程序写入数据")
	public String createFile(String path, String time, String dtype11) {
		String forward = "creatSuccess";
		Date tomorrow = new Date((new Date()).getTime());
		String sysTime = DateUtil.format(tomorrow, "yyyyMMdd_hh:MM:ss");
		List<String> ulist = declareDao.getSheetDeclAreaNum(time);// 统计买卖成员数
		String NumOffer = ulist.get(0);// 买方数量
		String NumBid = ulist.get(1);// 卖方数量
		// 获取申请日内买卖单的数量
		List<String> dlist = declareDao.getSheetDeclNum(time);// 统计买卖单数
		String NumOfferSheet = dlist.get(0);// 买单数
		String NumBidSheet = dlist.get(1);// 卖单数
		int NumCorridor = pathDefineDao.getPathNum();// 通道个数
		int NumTieline = lineDefineDao.getLineNum();// 联络线个数
		int NumInterval = 96;
		// 获取申请单日内的地区
		List<DeclarePo> areaList = declareDao.getAreaListByMdate(time);// 获取当天交易的地区
		// 遍历申报单
		List<DeclarePo> dslist = declareService.getListAndDataByMdate(time, null,null);// 遍历申报单

		List<LineLimitPo> limitList = lineLimitDao.getLineLimitList(time);// 联络线限额---排除交易功率
		// 遍历得到联络线map
		Map<String, Map<String, LineLimitPo>> limitMap = MatchUtil.getLineMap(limitList);
		Map<String, Object> regionMap = MatchUtil.getAreaAndRegion();// 得到地区和区域的map
		Map<String, Object> lineLossMap = MatchUtil.getLineLimitBySession();// 得到联络线和网损的关系
		List<PathDefinePo> pathList = pathDefineDao.getPathListAndCorridor(time);// 得到通道和联络线

		NumCorridor = pathList.size();
		int NumCorridorPath = 0;// 通道路径个数
		for (int i = 0; i < pathList.size(); i++) {
			List<CorridorPathPo> listCorr = pathList.get(i).getCorridorPaths();
			NumCorridorPath += listCorr.size();
		}

		List<PathResultPo> resultList = pathResultDao.getResultList(time);// 撮合结果集
		List<TypePo> typeList = typeDao.getType();// 时段类型结果集

		String fileName = "CBPM_国调_" + time + ".dat";
		String resultName = "CBPM_国调_result_" + time + ".dat";
		LoggerUtil.log(this.getClass().getName(), path + File.separator + fileName, 0);
		File rfile = new File(path, resultName);
		if (rfile.exists()) {
			rfile.delete();
		}
		// \r Mac; \n Unix/Linux; \r\n Windows
		String _NStr = System.getProperty("line.separator");
		LoggerUtil.log(this.getClass().getName(), "换行符=" + _NStr, 0);
		String _N = "\r\n";// "\r\n"
		File file = new File(path, fileName);
		String absolutePath = "";
		if (!file.exists()) {// 文件不存在，则创建
			try {
				file.createNewFile();
				absolutePath = file.getAbsolutePath();
			} catch (IOException e) {
				e.printStackTrace();
				forward = "creatFail";
			}
		} else {// 文件存在，删除后重建
			file.delete();
			file = new File(path, fileName);
			try {
				file.createNewFile();
				absolutePath = file.getAbsolutePath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (areaList.size() > 0 && pathList.size() > 0 && dslist.size() > 0 && limitList.size() > 0) {
			StringBuffer sbf = new StringBuffer();

			Map<String, Object> tielineMap = new HashMap<String, Object>();
			StringBuffer tielineSb = new StringBuffer();
			int tieLineIndex = 0;
			for (int i = 0; i < pathList.size(); i++) {
				PathDefinePo pathPo = pathList.get(i);
				String startArea = pathPo.getStartArea();
				String startRegion = "";
				String endArea = pathPo.getEndArea();
				String endRegion = "";
				if (regionMap.containsKey(startArea)) {
					startRegion = regionMap.get(startArea) + "";
				}
				if (regionMap.containsKey(endArea)) {
					endRegion = regionMap.get(endArea) + "";
				}
				List<CorridorPathPo> listCorr = pathList.get(i).getCorridorPaths();
				for (CorridorPathPo corriPo : listCorr) {
					// Field[] fields =
					// IntraCorridorPathPo.class.getDeclaredFields();
					for (int j = 1; j < 11; j++) {
						String getAttributeMethodName = "getCorhr" + j;
						Method getAttributeMethod = null;
						try {
							getAttributeMethod = CorridorPathPo.class.getDeclaredMethod(getAttributeMethodName);
							try {
								String corhr = (String) getAttributeMethod.invoke(corriPo);
								if (corhr == null) {
									break;
								}
								String val = "";
								String TielineTariff = "";
								String TielineLoss = "";
								if (lineLossMap.containsKey(corhr)) {
									val = lineLossMap.get(corhr) + "";
									if (val.indexOf("`") > -1) {
										if (val.split("`")[0] != null) {
											TielineLoss = val.split("`")[0];
										}
										if (val.split("`")[1] != null) {
											TielineTariff = val.split("`")[1];
										}
									}
								}
								if (!CommonUtil.ifEmpty(TielineLoss) || TielineLoss.equals("null")) {
									TielineLoss = "0";
								}
								if (!CommonUtil.ifEmpty(TielineTariff) || TielineTariff.equals("null")) {
									TielineTariff = "0";
								}
								if (!tielineMap.containsKey(corhr)) {
									tielineSb.append("#   " + (++tieLineIndex) + "  " + corhr + "	\"\"	" + startArea + "  " + endArea + "  " + startRegion + "  " + endRegion + "  " + TielineTariff + "  " + TielineLoss + " " + _N);
									tielineMap.put(corhr, null);
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
			StringBuffer corridorSb = new StringBuffer();
			int corridorNum = 0;
			for (int m = 0; m < pathList.size(); m++) {
				PathDefinePo obj1 = pathList.get(m);
				if (obj1.getCorridorPaths() != null && obj1.getCorridorPaths().size() > 0) {

					corridorSb.append("# " + (++corridorNum) + "  " + obj1.getMpath() + "	\"\"	" + obj1.getStartArea() + "  " + obj1.getEndArea() + "  " + obj1.getCorridorPaths().size() + "  " + obj1.getPriNum() + "  " + obj1.getIareaTariff() + "  " + obj1.getJareaTariff() + "  " + obj1.getJproTariff() + "  " + obj1.getIproTariff() + "" + _N);
				}
			}
			sbf.append("<!	Entity=国调	type=全数	time='" + sysTime + "'	!>" + _N + _N + "<ParamSet::国调 type=全数>" + _N + "@	Number	Param_ename	Param_cname	setting	note" + _N + "//	序号	参数英文名	参数中文名	设定	注释" + _N + "#	1	Date	日期	" + time + "	\"\" " + _N + "#	2	NumBuyer	买方成员数	" + NumOffer + "	\"\"" + _N + "#	3	NumSeller	卖方成员数	" + NumBid + "	\"\"" + _N + "#	4	NumOfferSheet	买单数	" + NumOfferSheet + "	\"\"" + _N + "#	5	NumBidSheet	卖单数	" + NumBidSheet + "	\"\"" + _N + "#	6	NumCorridor	通道个数	" + corridorNum + "	\"\"" + _N + "#	7	NumCorridorPath	通道路径个数 	" + NumCorridorPath + "	\"\"" + _N + "#	8	NumInterval	时段数	96	\"\"" + _N + "#	9	NumTieline	联络线个数	" + tieLineIndex + "	\"\"" + _N + "</ParamSet::国调>" + _N + _N + "<BuyerSeller::国调 type=全数>" + _N + "@	Number	Area	Attribute" + _N + "//	序号	地区	属性 " + _N);
			for (int i = 0; i < areaList.size(); i++) {// 遍历地区用户

				String mname = areaList.get(i).getArea();
				String drole = areaList.get(i).getDrloe();
				if (drole.equals("buy")) {
					sbf.append("#	" + (i + 1) + "	" + mname + "	买方" + _N);
				} else if (drole.equals("sale")) {
					sbf.append("#	" + (i + 1) + "	" + mname + "	卖方" + _N);
				}
			}

			sbf.append("</BuyerSeller::国调>" + _N + _N);
			// 申报单
			sbf.append("<BidOffer::国调 type=全数>" + _N + "@	Number	Area	Cname	Ename	IntervalNum	Attribute	Submitter	CommitTime" + _N + "//	序号	地区	单子中文名称	单子英文名称	时段数	属性 	提交人	提交时间" + _N);
			for (int i = 0; i < dslist.size(); i++) {// 遍历申报单
				String area = dslist.get(i).getArea();
				String sheetName = dslist.get(i).getSheetName();
				String drole = dslist.get(i).getDrloe();

				String ofby = "";
				if (drole.equals("buy")) {
					ofby = "offer";
				} else if (drole.equals("sale")) {
					ofby = "bid";
				}

				sbf.append("#	" + (i + 1) + "	" + area + "	" + sheetName + "	Sheet1	" + NumInterval + "	" + ofby + "	" + area + "	\"\"" + _N);
			}
			sbf.append("</BidOffer::国调>" + _N + _N);
			Map<DeclarePo, TreeMap<Integer, TreeMap<Float, Float>>> decMap = DeclareUtil.analysisDec(dslist);
			// 具体申报单数据，按照时段，统计每个时段报价曲线段数
			StringBuilder bidOfferBaseSb = new StringBuilder();
			// 具体申报单数据，按照时段，列出每个时段，每个电力和电价，卖方按电价降序，买方按电价升序
			StringBuilder bidOfferCurveSb = new StringBuilder();
			bidOfferBaseSb.append("<BidOfferBase::国调 type=全数>" + _N + "@	Number	Area	Cname	IntervalNum	CurveNum" + _N + "//	序号	地区	单子中文名称	时段序号ID 	曲线段数" + _N);
			bidOfferCurveSb.append("<BidOfferCurve::国调 type=全数>" + _N + "@	Number	Area	Cname	IntervalID	CurveID	Power	Price" + _N + "//	序号	地区	单子中文名称	时段序号ID	报价序号ID	分段起始出力	分段价格" + _N);

			int baseIndex = 0, curveIndex = 0;
			for (Map.Entry<DeclarePo, TreeMap<Integer, TreeMap<Float, Float>>> decEnt : decMap.entrySet()) {
				// 解析每个单的每个时段，没有报价的时段，默认是0
				for (Map.Entry<Integer, TreeMap<Float, Float>> intervalEnt : decEnt.getValue().entrySet()) {
					bidOfferBaseSb.append("#	" + (++baseIndex) + "	" + decEnt.getKey().getArea() + "	" + decEnt.getKey().getSheetName() + "	" + intervalEnt.getKey() + "	" + intervalEnt.getValue().size() + _N);
					int curvePowerIndex = 0;
					// 解析每个时段的每个电力报价
					for (Map.Entry<Float, Float> curve : intervalEnt.getValue().entrySet()) {
						bidOfferCurveSb.append("#	" + (++curveIndex) + "	" + decEnt.getKey().getArea() + "	" + decEnt.getKey().getSheetName() + "	" + intervalEnt.getKey() + "	" + (++curvePowerIndex) + "	" + curve.getValue() + "	" + curve.getKey() + _N);

					}
				}
			}
			bidOfferBaseSb.append("</BidOfferBase::国调>" + _N + _N);
			sbf.append(bidOfferBaseSb);
			bidOfferCurveSb.append("</BidOfferCurve::国调>" + _N + _N);
			sbf.append(bidOfferCurveSb);

			sbf.append("<Tieline::国调 type=全数>" + _N + "@	Number	TielineCName	TielineEName	IPro	JPro	IArea	JArea	TielineTariff	TielineLoss" + _N + "//	序号	联络线中文名	联络线英文名	首端省份	末端省份	首端区域	末端区域	联络线输电费	联络线网损" + _N);
			int lineIndex = 0;
			sbf.append(tielineSb);
			sbf.append("</Tieline::国调>" + _N + _N + "<TielineLimit::国调 type=全数>" + _N + " @	Number	TielineCName	TielineEName	Prd	LimitPos	LimitNeg	SchedulePower" + _N + "//	序号	联络线中文名	联络线英文名	时段数	正向限额	反向限额	计划值" + _N);
			// 联络线限额
			int num = 1;
			//判断是用动态正向限额还是用静态正向限额
			PfSysConfigPo config = ServiceHelper.getBean(SystemServiceI.class).getSysConfig(Enums_SystemConst.SPACE_EXCUTE_TYPE);
			List<Double> zx = new ArrayList<Double>();
			List<Double> fx = new ArrayList<Double>();
			List<Double> jh = new ArrayList<Double>();
			for (String key : tielineMap.keySet()) {
				Map<String, LineLimitPo> lineMap = limitMap.get(key);
				
				if (lineMap == null) {
					lineMap = new HashMap<String, LineLimitPo>();
					if (config == null || config.getValue().equals("1")) {
						lineMap.put("静态正向限额", new LineLimitPo());
						lineMap.put("静态反向限额", new LineLimitPo());
					}else{
						lineMap.put("动态正向限额", new LineLimitPo());
						lineMap.put("动态反向限额", new LineLimitPo());
					}
					lineMap.put("日前计划值", new LineLimitPo());

				}
				//System.out.println("---------"+CommonUtil.objToJson(lineMap));
				if (lineMap != null && lineMap.size() > 0) {
					for (int j = 1; j <= 96; j++) { // 原来startIndex,endIndex
						Double zxVal = 0.0;
						Double fxVal = 0.0;
						Double jhVal = 0.0;
						for (String dtype : lineMap.keySet()) {
							LineLimitPo liPo = lineMap.get(dtype);
							String getAttributeMethodName = "getH" + (j < 10 ? "0" + j : j);
							Method getAttributeMethod = null;
							Object value = null;
							try {
								getAttributeMethod = LineLimitPo.class.getDeclaredMethod(getAttributeMethodName);
								try {
									if(liPo != null){
										value = getAttributeMethod.invoke(liPo);
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
							if (config == null || config.getValue().equals("1")) {
								if (dtype.equals("静态正向限额")) {
									if (value != null) {
										zxVal = Double.valueOf(value.toString());
									}
								}
								if (dtype.equals("静态反向限额")) {
									if (value != null) {
										fxVal = Double.parseDouble(value.toString());
									}
								}
							}else{
								if (dtype.equals("动态正向限额")) {
									if (value != null) {
										zxVal = Double.valueOf(value.toString());
									}
								}
								if (dtype.equals("动态反向限额")) {
									if (value != null) {
										fxVal = Double.parseDouble(value.toString());
									}
								}
							}
							if (dtype.equals("日前计划值")) {
								if (value != null) {
									jhVal = Math.abs(Double.parseDouble(value.toString()));
								}
							}
							if(jhVal>zxVal){
								jhVal = zxVal;
							}
						}
						sbf.append("#	" + (num) + "	" + key + "	\"\"	" + j + "	" + zxVal + "	" + fxVal + "	" + jhVal + "" + _N);
						num++;
					}
				}
			}
			sbf.append("</TielineLimit::国调>" + _N + _N + "<Corridor::国调 type=全数>" + _N + " @	Number	CorridorCName	CorridorCNameEName	IPro	JPro	PathNum	PriNum	IAreaTariff	JAreaTariff	IProTariff	JProTariff " + _N + "//	序号	通道中文名	通道英文名	首端省份	末端省份	通道路径个数	优先级	首端区域输电费	末端区域输电费	首端省内输电费	末端省内输电费" + _N);

			sbf.append(corridorSb);
			sbf.append("</Corridor::国调>" + _N + _N + "<CorridorPath::国调 type=全数>" + _N + " @	Number	CorridorCName	CorridorCNameEName	PathNo	PathPri	PathDef priceratioA priceratioB Trans_loss Data " + _N + "//	序号	通道中文名	通道英文名	通道路径序号	通道路径优先级	通道路径定义  折价系数A 折价系数B 跨区网损  数据 " + _N);
			int xhIndex = 0;
			for (int i = 0; i < pathList.size(); i++) {
				PathDefinePo obj = pathList.get(i);
				List<CorridorPathPo> list = obj.getCorridorPaths();
				if (list.size() > 0) {
					for (int j = 0; j < list.size(); j++) {
						CorridorPathPo corriObj = list.get(j);
						String mdirection = corriObj.getMdirection();
						String pathMdirect = "";
						int pathLength = 0;
						// Field[] fields = IntraCorridorPathPo.class.getDeclaredFields();
						for (int k = 1; k < 11; k++) {
							String getAttributeMethodName = "getCorhr" + k;
							Method getAttributeMethod = null;
							try {
								getAttributeMethod = CorridorPathPo.class.getDeclaredMethod(getAttributeMethodName);
								try {
									String corhr = (String) getAttributeMethod.invoke(corriObj);
									if (corhr != null) {
										pathLength++;
										pathMdirect += mdirection.substring(k - 1, k) + corhr;
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
						/*String corriData = "";
						if(corriObj.getData()!=null){
							corriData = corriObj.getData();
						}*/
						String corriData = "";
						List<Map<String, Object>> listMapObj = CommonUtil.jsonToListMapObj(corriObj.getData());;
						if(corriObj.getData()==null && pathLength >0){
							for(int a =0; a<pathLength;a++){
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("tielineTariff", "0");
								map.put("netLoss", "0");
								listMapObj.add(map);
							}
							corriData = CommonUtil.objToJson(listMapObj);
						}else if(corriObj.getData()!=null && pathLength!=listMapObj.size()){
							for(int a =0; a< pathLength-listMapObj.size(); a++){
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("tielineTariff", "0");
								map.put("netLoss", "0");
								listMapObj.add(map);
							}
							corriData = CommonUtil.objToJson(listMapObj);
						}else{
							corriData = corriObj.getData();
						}
						sbf.append("#" + "  " + (++xhIndex) + "  " + obj.getMpath() + "	\"\"	" + corriObj.getSort() + "  " + corriObj.getPathPri() + "  " + pathMdirect + "  " + corriObj.getPriceRatioA() + "  " + corriObj.getPriceRatioB() + "  " + corriObj.getTransLoss() + "  " +corriData + ""+ _N);
					}
				}
			}
			sbf.append("</CorridorPath::国调>" + _N + _N);
			sbf.append("<!	Entity=国调	type=全数	End	!>");
			appendMethodB(absolutePath, sbf);
		}

		return forward;
	}

	/**
	 * 导入数据
	 */
	@LogWrite(modelName = "优化计算", operateName = "解析脚本返回数据")
	public String readFile(String path, String time) throws MsgException {
		String filePath = "";
		File file = new File(path);
		File[] array = file.listFiles();

		for (int i = 0; i < array.length; i++) {
			if (array[i].getPath().contains(Enums_SystemConst.RESULT_FILE_NAME + time + ".dat")) {// 根据日期匹配文件
				filePath = array[i].getPath();
			}
		}
		LoggerUtil.log(this.getClass().getName(), "filePath==" + filePath, 0);
		String dtime = CommonUtil.getSimpleDate();

		File f = new File(filePath);
		if (!f.exists()) {
			throw new MsgException("没有生成撮合结果文件!");
		}

		// 插入出清结果
		insertCbpmResult(dtime, time, filePath);
		// 插入执行结果
		// insertDealResult(dtime, time, filePath);
		// 解析插入通道结果
		insertPathResult(dtime, time, filePath);
		// 插入出清费用数据
		//insertSheetCostResult(dtime, time, filePath);
		// 获取出清详情，转换json创建文件
		createClearDetailJsonFile(dtime, time, filePath);
		createSheetCostSumMap(dtime,time,filePath); 
		return "readSuccess";
	}

	/**
	 * 解析出清电力和出清电价，结果插入CBPM_RESULT
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年8月24日下午5:17:00
	 * @param dtime
	 * @param mdate
	 */

	public void insertCbpmResult(String dtime, String mdate, String fileName) {
		// 解析出清电力
		Vector<Vector<String>> data = DatUtil.readFile(new File(fileName), "<ClearPower::国调 type=全数>", "</ClearPower::国调 type=全数>");
		// 先删除旧的数据
		issueDao.deleteResultByDateAndDtype(mdate, new String[] { Enums_DeclareType.DATATYPE_POWER, Enums_DeclareType.DATATYPE_PRICE }, null);
		String setAttributeMethodName = null;
		Method setAttributeMethod = null;
		ResultPo po = null;
		for (Vector<String> unit : data) {
			po = new ResultPo();
			po.setId(CommonUtil.getUUID());
			po.setArea(unit.get(2));// 地区
			po.setDtime(dtime);
			po.setMdate(mdate);
			po.setMname(SessionUtil.getAddNameCn());
			po.setDrole(unit.get(3));// 买卖类型 DROLE
			po.setCorridor(unit.get(4));
			po.setSide(unit.get(5)); // 送电侧；受电侧
			po.setDtype(Enums_DeclareType.DATATYPE_POWER);// 成交数据类型 DTYPE
															// VARCHAR(20) 电力；电价
			po.setDprint("0");// 未发布
			for (int i = 1; i < 97; i++) {
				setAttributeMethodName = "setH" + CommonUtil.decimal(2, i);
				try {
					setAttributeMethod = ResultPo.class.getDeclaredMethod(setAttributeMethodName, Double.class);
					setAttributeMethod.invoke(po, Double.parseDouble(unit.get(5 + i)));
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
			issueDao.insertResult(po);
		}
		// 解析出清电价
		data = DatUtil.readFile(new File(fileName), "<ClearPrice::国调 type=全数>", "</ClearPrice::国调 type=全数>");
		for (Vector<String> unit : data) {
			po = new ResultPo();
			po.setId(CommonUtil.getUUID());
			po.setArea(unit.get(2));// 地区
			po.setDtime(dtime);
			po.setMdate(mdate);
			po.setMname(SessionUtil.getAddNameCn());
			po.setDrole(unit.get(3));// 买卖类型 DROLE 送电侧；受电侧
			po.setCorridor(unit.get(4));
			// po.setSide(unit.get(5)); //送电侧；受电侧
			po.setDtype(Enums_DeclareType.DATATYPE_PRICE);// 成交数据类型 DTYPE
															// VARCHAR(20) 电力；电价
			po.setDprint("0");// 未发布
			for (int i = 1; i < 97; i++) {
				setAttributeMethodName = "setH" + CommonUtil.decimal(2, i);
				try {
					setAttributeMethod = ResultPo.class.getDeclaredMethod(setAttributeMethodName, Double.class);
					setAttributeMethod.invoke(po, Double.parseDouble(unit.get(4 + i)));
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
			issueDao.insertResult(po);
		}

	}

	/**
	 * 解析出清电力和出清电价，结果插入CBPM_RESULT
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年8月24日下午5:17:00
	 * @param dtime
	 * @param mdate
	 */

	public void insertDealResult(String dtime, String mdate, String fileName) {
		// 解析出清电力
		Vector<Vector<String>> data = DatUtil.readFile(new File(fileName), "<ClearPower::国调 type=全数>", "</ClearPower::国调 type=全数>");
		// 先删除旧的数据
		dealResultDao.deleteResultByDate(mdate);
		// issueDao.deleteResultByDateAndDtype(mdate, new String[] {
		// Enums_DeclareType.DATATYPE_POWER, Enums_DeclareType.DATATYPE_PRICE },
		// new String[] { Enums_DeclareType.DROLE_SEND,
		// Enums_DeclareType.DROLE_RECV });
		String setAttributeMethodName = null;
		Method setAttributeMethod = null;
		ResultPo po = null;
		for (Vector<String> unit : data) {
			po = new ResultPo();
			po.setId(CommonUtil.getUUID());
			po.setArea(unit.get(2));// 地区
			po.setDtime(dtime);
			po.setMdate(mdate);
			po.setMname(SessionUtil.getAddNameCn());
			po.setDrole(unit.get(4));// 买卖类型 DROLE
			po.setSheetName(unit.get(3));
			po.setDtype(Enums_DeclareType.DATATYPE_POWER);// 成交数据类型 DTYPE
															// VARCHAR(20) 电力；电价
			po.setDprint("0");// 未发布
			for (int i = 1; i < 97; i++) {
				setAttributeMethodName = "setH" + CommonUtil.decimal(2, i);
				try {
					setAttributeMethod = ResultPo.class.getDeclaredMethod(setAttributeMethodName, Double.class);
					setAttributeMethod.invoke(po, Double.parseDouble(unit.get(5 + i)));
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
			dealResultDao.insertResult(po);
			// issueDao.insertResult(po);
			// System.out.println(CommonUtil.objToJson(po));
		}
		// 解析出清电价
		data = DatUtil.readFile(new File(fileName), "<ClearPrice::国调 type=全数>", "</ClearPrice::国调 type=全数>");
		for (Vector<String> unit : data) {
			po = new ResultPo();
			po.setId(CommonUtil.getUUID());
			po.setArea(unit.get(2));// 地区
			po.setDtime(dtime);
			po.setMdate(mdate);
			po.setMname(SessionUtil.getAddNameCn());
			po.setDrole(unit.get(4));// 买卖类型 DROLE
			po.setSheetName(unit.get(3));
			po.setDtype(Enums_DeclareType.DATATYPE_PRICE);// 成交数据类型 DTYPE
															// VARCHAR(20) 电力；电价
			po.setDprint("0");// 未发布
			for (int i = 1; i < 97; i++) {
				setAttributeMethodName = "setH" + CommonUtil.decimal(2, i);
				try {
					setAttributeMethod = ResultPo.class.getDeclaredMethod(setAttributeMethodName, Double.class);
					setAttributeMethod.invoke(po, Double.parseDouble(unit.get(4 + i)));
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
			dealResultDao.insertResult(po);
			// System.out.println(CommonUtil.objToJson(po));
		}

	}

	public static final String[] ARR_DTYPES = { "送端电力", "受端电力", "送端上网费用", "省内输电费用", "跨区输电费用", "从送端累加的总费用", "受端总费用", "华中输电费用", "德宝输电费用", "西北输电费用", "宁东输电费用" };

	/**
	 * 解析通道信息，结果插入CBPM_TRANSTIELINE
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年8月24日下午5:17:00
	 * @param dtime
	 * @param mdate
	 */
	public void insertPathResult(String dtime, String mdate, String fileName) {
		// 解析出清电力
		// 先执行删除
		transTielineDao.deleteResultByDate(mdate);
		// 解析SheetCost插入通道结果
		Vector<Vector<String>> data = DatUtil.readFile(new File(fileName), "<TransTieline::国调 type=全数>", "</TransTieline::国调 type=全数>");
		String setAttributeMethodName = null;
		Method setAttributeMethod = null;
		TransTielinePo po = null;
		for (Vector<String> unit : data) {
			po = new TransTielinePo();
			po.setId(CommonUtil.getUUID());
			po.setMdate(mdate);
			po.setTielineName(unit.get(2));
			po.setTransCorridorName(unit.get(3));
			int x = 1;
			for (int i = 1; i <= 96; i++) {
				setAttributeMethodName = "setH" + CommonUtil.decimal(2, i);
				try {
					setAttributeMethod = TransTielinePo.class.getDeclaredMethod(setAttributeMethodName, Double.class);
					setAttributeMethod.invoke(po, Double.parseDouble(unit.get(3 + x)));
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
				x++;
			}
			transTielineDao.insertTransTieline(po);
		}
	}

	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年12月6日上午11:04:58
	 * @param dtime
	 * @param mdate
	 * @param fileName
	 */
	public void insertSheetCostResult(String dtime, String mdate, String fileName) {
		// 解析出清电力
		// 先执行删除交易功率
		lineLimitDao.delLineLimitPo(mdate, Enums_DeclareType.DATATYPE_TRADING_POWER);
		Vector<Vector<String>> data = DatUtil.readFile(new File(fileName), "<SheetCost::国调 type=全数>", "</SheetCost::国调 type=全数>");
		String setAttributeMethodName = null;
		Method setAttributeMethod = null;
		LineLimitPo po = null;
		for (Vector<String> unit : data) {
			po = new LineLimitPo();
			po.setMdate(mdate);
			po.setDtype(Enums_DeclareType.DATATYPE_TRADING_POWER);// 交易功率
			po.setMcorhr(unit.get(2));
			for (int i = 1; i < 97; i++) {
				setAttributeMethodName = "setH" + CommonUtil.decimal(2, i);
				try {
					setAttributeMethod = LineLimitPo.class.getDeclaredMethod(setAttributeMethodName, double.class);
					setAttributeMethod.invoke(po, Double.parseDouble(unit.get(2 + i)));
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
			lineLimitDao.insertLineLimit(po);
			// System.out.println(CommonUtil.objToJson(po));
		}

	}

	/**
	 * 调用c之后解析result数据，把clearDetail数据解析成json并在jsp/issue/data下创建json文件
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年8月29日下午4:46:45
	 * @param dtime
	 * @param mdate
	 * @param fileName
	 */
	public void createClearDetailJsonFile(String dtime, String mdate, String fileName) {
		// 解析出清电力
		Vector<Vector<String>> data = DatUtil.readFile(new File(fileName), "<ClearResult::国调 type=全数>", "</ClearResult::国调 type=全数>");
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> po = null;
		for (Vector<String> unit : data) {
			po = new HashMap<String, Object>();
			po.put("Number", unit.get(1));
			po.put("Interval", unit.get(2));
			po.put("SellerArea", unit.get(3));
			po.put("Sellername", unit.get(4));
			po.put("Sellersection", unit.get(5));
			po.put("power_i", unit.get(6));
			po.put("price_i", unit.get(7));
			po.put("clearpower_i", unit.get(8));
			po.put("clearprice_i", unit.get(9));
			po.put("BuyerArea", unit.get(10));
			po.put("Buyername", unit.get(11));
			po.put("Buyersection", unit.get(12));
			po.put("power_j", unit.get(13));
			po.put("price_j", unit.get(14));
			po.put("trans_loss", unit.get(15));
			po.put("tanspower_j", unit.get(16));
			po.put("clearpower_j", unit.get(17));
			po.put("clearprice_j", unit.get(18));
			
			po.put("Pricediff", unit.get(19));
			po.put("SellCost", unit.get(20));
			po.put("TranCost", unit.get(21));
			po.put("LossCost", unit.get(22));
			po.put("BuyCost", unit.get(23));
			po.put("LineCost", unit.get(24));
			po.put("LineLossCost", unit.get(25));
			po.put("LinePower", unit.get(26));
			po.put("Trans_Path", unit.get(27));
			po.put("Corridorpower", unit.get(28));
			po.put("Pri", unit.get(29));

			result.add(po);
		}
		DataGrid dg = new DataGrid();
		dg.setTotal(result.size());
		dg.setRows(result);
		// String path =
		// SessionUtil.getSession().getServletContext().getRealPath("js");
		// path = path + "/state/issue/data/" + mdate + ".json";
		String path = SystemConstUtil.getMatchPath() + File.separator + "clearData";
		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}
		// System.out.println("--------"+f.getAbsolutePath());
		path += File.separator + mdate + ".json";
		FileManager.writeToFile(path, CommonUtil.objToJson(dg), true);

	}

	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年12月6日下午9:15:24
	 * @param dtime
	 * @param mdate
	 * @param fileName
	 */
	public void createSheetCostSumMap(String dtime, String mdate, String fileName) {
		
		Vector<Vector<String>> data = DatUtil.readFile(new File(fileName), "<SheetCostSum::国调 type=全数>", "</SheetCostSum::国调 type=全数>");
		Map<String, Object> map = null;
		for (Vector<String> unit : data) {
			map = new HashMap<String, Object>();
			map.put("SellCost", unit.get(2));
			map.put("TranCost", unit.get(3));
			map.put("STSum", unit.get(4));
			map.put("BuyCost", unit.get(5));
		}
		
		PfSysConfigPo processConfig = sysConfigDao.selectOneByType(Enums_SystemConst.SYS_CONFIG_TYPE＿SHEETCOST_SUM,"SheetCostSum_"+mdate);
		
		if(processConfig == null){
			processConfig = new PfSysConfigPo();
			processConfig.setId(CommonUtil.getUUID());
			processConfig.setAddName(SessionUtil.getAddName());
			processConfig.setAddNameCn(SessionUtil.getAddNameCn());
			processConfig.setDocCreated(new Date());
			processConfig.setType(Enums_SystemConst.SYS_CONFIG_TYPE＿SHEETCOST_SUM);
			processConfig.setKey("SheetCostSum_"+mdate);
			processConfig.setValue(CommonUtil.objToJson(map));
			sysConfigDao.insertSysConfig(processConfig);
		}else{
			
			processConfig.setValue(CommonUtil.objToJson(map));
			sysConfigDao.updateSysConfig(processConfig);
			
		}

	}
	/**
	 * 追加文件：使用FileWriter---写入
	 */
	public static void appendMethodB(String fileName, StringBuffer content) {
		try {
			if (SystemTools.isLinux()) {
				FileWriter writer = new FileWriter(fileName, true);
				writer.write(content.toString());
				writer.close();
			} else {

				OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(fileName, true), "gbk");
				osw.write(content.toString());
				osw.close();

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 功能：Java读取txt文件的内容 步骤：1：先获得文件句柄 2：获得文件句柄当做是输入一个字节码流，需要对这个输入流进行读取
	 * 3：读取到输入流后，需要读取生成字节流 4：一行一行的输出。readline()。 备注：需要考虑的是异常情况
	 * 
	 * @param filePath
	 */
	public static ArrayList<String> readTxtFile(String filePath) {
		ArrayList<String> readList = new ArrayList<String>();
		try {
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);

				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					readList.add(lineTxt);
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return readList;
	}

	/**
	 * 查询bat文件是否生成
	 */
	public String selectProgramInfo(String str) {
		return issueDao.selectProgramInfo(str);
	}

	public List<LineLimitPo> getLineLimitList(String time) {
		return lineLimitDao.getLineLimitList(time);// 联络线限额
	}

	/**
	 * 创建通道限额计划数据bat文件
	 * 
	 * @param time
	 */
	@LogWrite(modelName = "导出计划数据", operateName = "导出通道计划数据")
	public String createLimitFile(String path, String mcorhr, String time, LineLimitPo po) {
		String forward = "creatSuccess";
		Date tomorrow = new Date((new Date()).getTime());
		String sysTime = DateUtil.format(tomorrow, "yyyyMMdd_hh:MM:ss");

		String fileName = "CBPM_CORRIDOR_" + time + ".dat";

		LoggerUtil.log(this.getClass().getName(), path + fileName, 0);

		File file = new File(path, fileName);
		String absolutePath = "";
		if (!file.exists()) {// 文件不存在，则创建
			try {
				file.createNewFile();
				absolutePath = file.getAbsolutePath();
			} catch (IOException e) {
				e.printStackTrace();
				forward = "creatFail";
			}
		} else {// 文件存在，删除后重建
			// System.out.println(fileName + "---------------导出文件存在，执行删除");
			file.delete();
			file = new File(path, fileName);
			try {
				file.createNewFile();
				absolutePath = file.getAbsolutePath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (po != null) {
			StringBuffer sbf = new StringBuffer();
			sbf.append("<!	Entity=国调	type=全数	time='" + sysTime + "'	!>" + "\r\n" + "\r\n" + "<Corridor::国调 type=全数>" + "\r\n" + "@	Number	CORRIDOR	MDATE	H01		H02  H03	H04 	H05 	H06	  H07	 H08	 H09	 H10	 H11	 H12	H13  H14	H15	 H16	H17  	H18	  H19	H20 	H21 	H22 	H23		H24		H25 	H26		H27 	H28 	H29 	H30 	H31 	H32 	H33 	H34 	H35 	H36 	H37 	H38 	H39 	H40 	H41 	H42 	H43 	H44 	H45 	H46 	H47 	H48 	H49 	H50 	" + "H51 	H52 	H53 	H54 	H55 	H56 	H57 	H58 	H59 	H60 	H61 	H62 	H63 	H64 	H65 	H66		H67 	H68		H69 	H70 	H71 	H72 	H73 	H74 	H75 	H76 	H77		H78		H79		H80		H81		H82 	H83 	H84 	H85 	H86		H87		H88		H89		H90		H91		H92		H93 	H94		H95		H96" + "\r\n"
					+ "//	序号 	通道名称 	  交易日期		00:15	00:30	00:45	01:00	01:15	01:30	01:45	02:00	02:15	02:30	02:45	03:00	03:15	03:30	03:45	04:00	04:15	04:30	04:45	05:00	05:15	05:30	05:45	06:00	06:15	06:30	06:45	07:00	07:15	07:30	07:45	08:00   08:15   08:30  08:45  09:00  09:15  09:30  09:45  10:00  10:15  10:30  10:45  11:00  11:15  11:30  11:45  12:00  " + "12:15	12:30	12:45	13:00	13:15	13:30	13:45	14:00	14:15	14:30	14:45	15:00	15:15	15:30	15:45	16:00	16:15	16:30	16:45	17:00	17:15	17:30	17:45	18:00	18:15	18:30	18:45	19:00	19:15	19:30	19:45	20:00   20:15   20:30  20:45  21:00  21:15  21:30  21:45  22:00  22:15  22:30  22:45  23:00  23:15  23:30  23:45  24:00" + "\r\n");

			sbf.append("#	1	" + mcorhr + "	" + time + "	" + po.getH01() + "	" + po.getH02() + "	" + po.getH03() + "	" + po.getH04() + "	" + po.getH05() + "	" + po.getH06() + "	" + po.getH07() + "	" + po.getH08() + "	" + po.getH09() + "	" + po.getH10() + "	" + po.getH11() + "	" + po.getH12() + "	" + po.getH13() + "	" + po.getH14() + "	" + po.getH15() + "	" + po.getH16() + "	" + po.getH17() + "	" + po.getH18() + "	" + po.getH19() + "	" + po.getH20() + "	" + po.getH21() + "	" + po.getH22() + "	" + po.getH23() + "	" + po.getH24() + "	" + po.getH25() + "		" + po.getH26() + "	" + po.getH27() + "	" + po.getH28() + "	" + po.getH29() + "	" + po.getH30() + "	" + po.getH31() + "	" + po.getH32() + "	" + po.getH33() + "	" + po.getH34() + "	" + po.getH35() + "	" + po.getH36() + "	" + po.getH37() + "	"
					+ po.getH38() + "	" + po.getH39() + "	" + po.getH40() + "	" + po.getH41() + "	" + po.getH42() + "	" + po.getH43() + "	" + po.getH44() + "	" + po.getH45() + "	" + po.getH46() + "	" + po.getH47() + "	" + po.getH48() + "	" + po.getH49() + "	" + po.getH50() + "	" + po.getH51() + "	" + po.getH52() + "	" + po.getH53() + "	" + po.getH54() + "		" + po.getH55() + "	" + po.getH56() + "	" + po.getH57() + "	" + po.getH58() + "	" + po.getH59() + "	" + po.getH60() + "	" + po.getH61() + "	" + po.getH62() + "	" + po.getH63() + "	" + po.getH64() + "	" + po.getH65() + "	" + po.getH66() + "	" + po.getH67() + "	" + po.getH68() + "	" + po.getH69() + "	" + po.getH70() + "	" + po.getH71() + "	" + po.getH72() + "	" + po.getH73() + "	" + po.getH74() + "	" + po.getH75() + "	" + po.getH76()
					+ "	" + po.getH77() + "	" + po.getH78() + "	" + po.getH79() + "	" + po.getH80() + "\r\n");
			sbf.append("</Corridor::国调>" + "\r\n" + "\r\n");
			sbf.append("<!	Entity=国调	type=全数	End	!>");
			appendMethodB(absolutePath, sbf);

		}

		return forward;
	}

	/**
	 * 
	 */
	public String createDeclareFile(String path, String area, String time, List<Map<String, Object>> data) {
		String forward = "creatSuccess";
		Date tomorrow = new Date((new Date()).getTime());
		String sysTime = DateUtil.format(tomorrow, "yyyyMMdd_hh:MM:ss");

		String fileName = "CBPM_BIDOFFER_" + area + "_" + time + ".dat";
		LoggerUtil.log(this.getClass().getName(), path + fileName, 0);

		File file = new File(path, fileName);
		String absolutePath = "";
		if (!file.exists()) {// 文件不存在，则创建
			try {
				file.createNewFile();
				absolutePath = file.getAbsolutePath();
			} catch (IOException e) {
				e.printStackTrace();
				forward = "creatFail";
			}
		} else {// 文件存在，删除后重建
			// System.out.println(fileName + "---------------导出文件存在，执行删除");
			file.delete();
			file = new File(path, fileName);
			try {
				file.createNewFile();
				absolutePath = file.getAbsolutePath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (data != null) {
			StringBuffer sbf = new StringBuffer();
			sbf.append("<!	Entity=" + area + "	type=全数	time='" + sysTime + "'	!>" + "\r\n" + "\r\n" + "<BidOfferData::国调 type=全数>" + "\r\n" + "@	Number	AREA	MDATE	DRLOE	START_TIME   END_TIME	POWER 	PRICE" + "\r\n" + "//	序号 	省份 	  交易日期		买卖类型	     开始时段	结束时段	  电力	价格" + "\r\n");

			for (int i = 0; i < data.size(); i++) {// 遍历地区用户

				String startTime = (String) data.get(i).get("startTime");
				String endTime = (String) data.get(i).get("endTime");
				List<Map<String, Object>> binData = (List<Map<String, Object>>) data.get(i).get("data");
				for (int j = 0; j < binData.size(); j++) {
					sbf.append("#	" + (i + 1) + "	   " + area + "  " + time + "	卖方		" + startTime + "		" + endTime + "		" + binData.get(j).get("power") + "		" + binData.get(j).get("price") + "\r\n");
				}
			}
			sbf.append("</BidOfferData::" + area + ">" + "\r\n" + "\r\n");
			appendMethodB(absolutePath, sbf);

		}

		return forward;
	}

	/**
	 * 导出Result数据,并生成.eh头文件
	 * 
	 * @author 车斯剑
	 * @date 2016年9月19日下午4:20:39
	 * @param path
	 * @param area
	 * @param time
	 * @param datas
	 * @return
	 */
	public String createResultFile(String path, String time, String mdate) {
		// 其中mdate的时间格式为 2016-11-10，time的时间格式为 20161110
		String forward = "creatSuccess";
		//System.out.println(time+"-------"+mdate);
		StringBuffer sessionBuffer = new StringBuffer();
		StringBuffer timeBuffer = new StringBuffer();
		for (int i = 1; i <= 96; i++) {
			sessionBuffer.append("H" + (i < 10 ? "0" + i : i) + "  ");
			timeBuffer.append(DeclareUtil.getInterval(i) + "  ");
		}
		List<DeclarePo> areaList = declareDao.getAreaListByMdate(time);
		//System.out.println(time+"-----"+session+"----"+areaList.size());
		String _N = "\r\n";// "\r\n"
		if (areaList != null && areaList.size() > 0) {
			for (DeclarePo areaPo : areaList) {
				String key = areaPo.getArea();
				String fileName = "CBPM_RESULT_" + key + "_" + time + ".dat";
				String ehFileName = "CBPM_RESULT_" + key + "_" + time + ".eh";
				File f = new File(path);
				if (!f.exists()) {
					f.mkdirs();
				}
				File fi = new File(path, fileName);
				String absolutePath = "";
				if (!fi.exists()) {// 文件不存在，则创建
					try {
						fi.createNewFile();
						absolutePath = fi.getAbsolutePath();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {// 文件存在，删除后重建
					// System.out.println(fileName +
					// "---------------导出文件存在，执行删除");
					fi.delete();
					fi = new File(path, fileName);
					try {
						fi.createNewFile();
						absolutePath = fi.getAbsolutePath();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				File ehFile = new File(path, ehFileName);
				String ehAbsolutePath = "";
				StringBuffer ensbf = new StringBuffer();
				if (!ehFile.exists()) {// 文件不存在，则创建
					try {
						ehFile.createNewFile();
						ehAbsolutePath = ehFile.getAbsolutePath();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {// 文件存在，删除后重建
					ehFile.delete();
					ehFile = new File(path, ehFileName);
					try {
						ehFile.createNewFile();
						ehAbsolutePath = ehFile.getAbsolutePath();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				int num = 1;
				StringBuffer sbf = new StringBuffer();
				Date tomorrow = new Date((new Date()).getTime());
				String sysTime = DateUtil.format(tomorrow, "yyyyMMdd_hh:MM:ss");
				sbf.append("<!	Entity=" + key + "	type=全数	time='" + sysTime + "'	!>" + _N + _N + "<ClearPower::	" + key + "	type=全数>" + _N + "@	NUMBER	AREA	MDATE	DRLOE	CORRIDOR	SIDE	");
				sbf.append(sessionBuffer + " " + _N);
				sbf.append("//	序号	省份	交易日期	买卖类型	交易对象	端侧  ");
				sbf.append(timeBuffer + "	" + _N);
				// 电力
				List<ResultPo> dlList = issueDao.getTreeForResult(key, time, null, "电力");
				// 电价
				List<ResultPo> djList = issueDao.getTreeForResult(key, time, null, "电价");
				for (int j = 0; j < dlList.size(); j++) {
					ResultPo obj = dlList.get(j);
					sbf.append("#	" + (num++) + "	" + key + "	" + mdate + "	" + "	" + (obj.getDrole().equals("buy")?"买方":"卖方") + "	" + obj.getCorridor() + "	" + obj.getSide() + "	");
					// 96值字符串
					for (int i = 1; i <= 96; i++) {
						String getAttributeMethodName = "getH" + (i < 10 ? "0" + i : i);
						Method getAttributeMethod = null;
						try {
							getAttributeMethod = ResultPo.class.getDeclaredMethod(getAttributeMethodName);
							try {
								Object value1 = getAttributeMethod.invoke(obj);
								sbf.append("	" + (value1 == null ? 0 : value1));
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
					sbf.append("  " + _N);
				}
				sbf.append("</ClearPower::	" + key + "	type=全数>" + _N + _N);
				sbf.append("<ClearPrice::	" + key + "	type=全数>" + _N + "@	Number	AREA	MDATE	DROLE	CORRIDOR ");
				sbf.append(sessionBuffer + " " + _N);
				sbf.append("//  序号	省份	交易日期	 买卖类型	交易对象	");
				sbf.append(timeBuffer + " " + _N);
				int index = 1;
				for (int j = 0; j < djList.size(); j++) {
					ResultPo obj = djList.get(j);
					sbf.append("#	" + (index++) + "	" + key + "	" + mdate + "	" + (obj.getDrole().equals("buy")?"买方":"卖方") + "	" + obj.getCorridor() + "	");
					// 96值字符串
					for (int i = 1; i <= 96; i++) {
						String getAttributeMethodName = "getH" + (i < 10 ? "0" + i : i);
						Method getAttributeMethod = null;
						try {
							getAttributeMethod = ResultPo.class.getDeclaredMethod(getAttributeMethodName);
							try {
								Object value1 = getAttributeMethod.invoke(obj);
								sbf.append("	" + (value1 == null ? 0 : value1));
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
					sbf.append("	" + _N);
				}
				sbf.append("</ClearPrice::	" + key + "	type=全数>" + _N + _N);
				sbf.append("<TransTieline::	" + key + "	type=全数>" + _N + "@	Number	TIELINENAME	MDATE	CORRIDOR ");
				sbf.append(sessionBuffer + " " + _N);
				sbf.append("//	序号	联络线名称	交易日期 	交易对象 ");
				sbf.append(timeBuffer + " " + _N);
				int index1 = 1;
				List<TransTielinePo> tieLineList = transTielineDao.getTransTielineListByMdate(time, key);
				for (int j = 0; j < tieLineList.size(); j++) {
					TransTielinePo obj = tieLineList.get(j);
					//System.out.println(key+"---------"+obj.getTransCorridorName());
					if( !(obj.getTransCorridorName().equals("汇总值") ||  obj.getTransCorridorName().indexOf(key)>-1)){
						continue;
					}
					//System.out.println("=============");
					sbf.append("#	" + (index1++) + "	" + obj.getTielineName() + "	" + mdate + "	" + obj.getTransCorridorName() + "	");
					// 96值字符串
					for (int i = 1; i <= 96; i++) {
						String getAttributeMethodName = "getH" + (i < 10 ? "0" + i : i);
						Method getAttributeMethod = null;
						try {
							getAttributeMethod = TransTielinePo.class.getDeclaredMethod(getAttributeMethodName);
							try {
								Object value1 = getAttributeMethod.invoke(obj);
								sbf.append("	" + (value1 == null ? 0 : value1));
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
					sbf.append("	" + _N);
				}
				sbf.append("</TransTieline::	" + key + "	type=全数>" + _N + _N);
				// 头文件
				String sysEHTime = DateUtil.format(tomorrow, "yyyyMMdd hh:mm:ss");
				ensbf.append("<CBPM::传输说明	time='" + sysEHTime + "'>" + "\r\n" + "@#顺序 属性名 属性值" + "\r\n" + "#0 标识 " + ehFileName + "\r\n" + "#1 发送地址 国调2.CBPM \r\n#2 接收地址 " + key + "2.CBPM \r\n#3 传输类型 文件 \r\n#4 内容 '' \r\n#5 文件 '" + fileName + "'\r\n</CBPM::传输说明>");
				appendMethodB(ehAbsolutePath, ensbf);
				appendMethodB(absolutePath, sbf);
			}
		}
		return forward;
	}

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
		/*
		 * switch (index) { case 1: corhrName = pathPo.getCorhr1(); break; case
		 * 2: corhrName = pathPo.getCorhr2(); break; case 3: corhrName =
		 * pathPo.getCorhr3(); break; case 4: corhrName = pathPo.getCorhr4();
		 * break; case 5: corhrName = pathPo.getCorhr5(); break; case 6:
		 * corhrName = pathPo.getCorhr6(); break; case 7: corhrName =
		 * pathPo.getCorhr7(); break; case 8: corhrName = pathPo.getCorhr8();
		 * break; case 9: corhrName = pathPo.getCorhr9(); break; case 10:
		 * corhrName = pathPo.getCorhr10(); break;
		 * 
		 * default: corhrName = ""; break; }
		 */
		return corhrName;
	}

	/**
	 * 根据小邮件导出通道剩余容量
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月20日下午4:29:01
	 */
	public JsonMsg exportMpathLimit(String time) {

		time = time.replaceAll("-", "");
		List<PathDefinePo> pathList = pathDefineDao.selectPathALL(time);// 遍历通道
		List<LineDefinePo> lineList = lineDefineDao.getAllLine();// 遍历联络线
		List<LineLimitPo> limitList = lineLimitDao.getLineLimitList(time);// 联络线限额---排除交易功率
		JsonMsg j = new JsonMsg();
		Map<String, Map<String, LineLimitPo>> linelimitMap = new HashMap<String, Map<String, LineLimitPo>>();
		if (limitList != null) {
			Map<String, LineLimitPo> temp = null;
			for (LineLimitPo po : limitList) {
				// 如果包含该联络线
				if (linelimitMap.containsKey(po.getMcorhr())) {
					temp = linelimitMap.get(po.getMcorhr());
				} else {
					temp = new HashMap<String, LineLimitPo>();
					linelimitMap.put(po.getMcorhr(), temp);
				}
				temp.put(po.getDtype(), po);
			}
		}

		// 判断是用哪个减 差值空间 = 静态限额/动态限额-日前计划
		PfSysConfigPo config = ServiceHelper.getBean(SystemServiceI.class).getSysConfig(Enums_SystemConst.SPACE_EXCUTE_TYPE);
		String cacuteType = "动态正向限额";
		if (config == null || config.getValue().equals("1")) {
			cacuteType = "静态正向限额";
		}

		// 计算各个联络线的差值空间
		for (Entry<String, Map<String, LineLimitPo>> mcohrEnt : linelimitMap.entrySet()) {
			LineLimitPo minuendPo = mcohrEnt.getValue().get(cacuteType);
			LineLimitPo beforDayPo = mcohrEnt.getValue().get("日前计划值");
			if (mcohrEnt.getKey().equals(""))
				// System.out.println("minuendPo"+CommonUtil.objToJson(minuendPo));
				// System.out.println("beforDayPo"+CommonUtil.objToJson(beforDayPo));

				if (minuendPo == null) {
					minuendPo = new LineLimitPo();
				}
			if (beforDayPo == null) {
				beforDayPo = new LineLimitPo();
			}
			LineLimitPo spacePo = new LineLimitPo();
			mcohrEnt.getValue().put("差值空间", spacePo);
			// 计算差值空间
			for (int i = 1; i < 97; i++) {
				String getAttributeMethodName = "getH" + (i < 10 ? "0" + i : i);
				String setAttributeMethodName = "setH" + (i < 10 ? "0" + i : i);
				Method getAttributeMethod = null;
				Method setAttributeMethod = null;
				try {
					getAttributeMethod = LineLimitPo.class.getDeclaredMethod(getAttributeMethodName);
					setAttributeMethod = LineLimitPo.class.getDeclaredMethod(setAttributeMethodName, double.class);

					try {
						double value1 = 0;
						if (minuendPo != null) {
							value1 = (Double) getAttributeMethod.invoke(minuendPo);
						}
						double value2 = 0;
						if (beforDayPo != null) {
							value2 = (Double) getAttributeMethod.invoke(beforDayPo);
						}
						double value = value1 - value2;
						setAttributeMethod.invoke(spacePo, value);
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
		}
		if (linelimitMap == null || linelimitMap.size() == 0) {
			j.setMsg("没有联络线！");
			return j;
		}
		String fileSrc = SystemConstUtil.getMatchPath() + File.separator + "trans_data" + File.separator + "send" + File.separator;
		// 计算各通道限额
		if (pathList != null && pathList.size() > 0) {
			for (PathDefinePo pathPo : pathList) {
				// 多个联络线，对应多个方向
				String mdirection = ""; // pathPo.getMdirection();
				// 计算最后的差值空间
				LineLimitPo po = new LineLimitPo();
				for (int m = 1; m <= mdirection.length(); m++) {
					// 获取物理联络线
					String corhrName = getCorhrName(m, pathPo);
					LineLimitPo t = linelimitMap.get(corhrName).get("差值空间");
					// System.out.println(corhrName+"=="+CommonUtil.objToJson(t));
					if (t != null) {

						for (int i = 1; i < 97; i++) {
							String getAttributeMethodName = "getH" + (i < 10 ? "0" + i : i);
							String setAttributeMethodName = "setH" + (i < 10 ? "0" + i : i);
							Method getAttributeMethod = null;
							Method setAttributeMethod = null;
							try {
								getAttributeMethod = LineLimitPo.class.getDeclaredMethod(getAttributeMethodName);
								setAttributeMethod = LineLimitPo.class.getDeclaredMethod(setAttributeMethodName, double.class);

								try {
									double value1 = 0;
									value1 = (Double) getAttributeMethod.invoke(po);

									double value2 = 0;
									value2 = (Double) getAttributeMethod.invoke(t);
									if (m == 1) {
										setAttributeMethod.invoke(po, value2);
									} else {
										if (value2 < value1) {
											setAttributeMethod.invoke(po, value2);
										}
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
					}
				}
				// 创建导出文件
				StringBuffer sbf = new StringBuffer();
				sbf.append("<!	Entity=国调	type=全数	time='" + CommonUtil.getDateTime() + "'	!>" + "\r\n" + "\r\n" + "<Corridor::国调 type=全数>" + "\r\n" + "@	Number	CORRIDOR	MDATE	H01		H02  H03	H04 	H05 	H06	  H07	 H08	 H09	 H10	 H11	 H12	H13  H14	H15	 H16	H17  	H18	  H19	H20 	H21 	H22 	H23		H24		H25 	H26		H27 	H28 	H29 	H30 	H31 	H32 	H33 	H34 	H35 	H36 	H37 	H38 	H39 	H40 	H41 	H42 	H43 	H44 	H45 	H46 	H47 	H48 	H49 	H50 	" + "H51 	H52 	H53 	H54 	H55 	H56 	H57 	H58 	H59 	H60 	H61 	H62 	H63 	H64 	H65 	H66		H67 	H68		H69 	H70 	H71 	H72 	H73 	H74 	H75 	H76 	H77		H78		H79		H80		H81		H82 	H83 	H84 	H85 	H86		H87		H88		H89		H90		H91		H92		H93 	H94		H95		H96" + "\r\n"
						+ "//	序号 	通道名称 	  交易日期		00:15	00:30	00:45	01:00	01:15	01:30	01:45	02:00	02:15	02:30	02:45	03:00	03:15	03:30	03:45	04:00	04:15	04:30	04:45	05:00	05:15	05:30	05:45	06:00	06:15	06:30	06:45	07:00	07:15	07:30	07:45	08:00   08:15   08:30  08:45  09:00  09:15  09:30  09:45  10:00  10:15  10:30  10:45  11:00  11:15  11:30  11:45  12:00  " + "12:15	12:30	12:45	13:00	13:15	13:30	13:45	14:00	14:15	14:30	14:45	15:00	15:15	15:30	15:45	16:00	16:15	16:30	16:45	17:00	17:15	17:30	17:45	18:00	18:15	18:30	18:45	19:00	19:15	19:30	19:45	20:00   20:15   20:30  20:45  21:00  21:15  21:30  21:45  22:00  22:15  22:30  22:45  23:00  23:15  23:30  23:45  24:00" + "\r\n");

				/*
				 * sbf.append("#	1	" + pathPo.getMpath() + "	" + time + "	" +
				 * po.getH01() + "	" + po.getH02() + "	" + po.getH03() + "	" +
				 * po.getH04() + "	" + po.getH05() + "	" + po.getH06() + "	" +
				 * po.getH07() + "	" + po.getH08() + "	" + po.getH09() + "	" +
				 * po.getH10() + "	" + po.getH11() + "	" + po.getH12() + "	" +
				 * po.getH13() + "	" + po.getH14() + "	" + po.getH15() + "	" +
				 * po.getH16() + "	" + po.getH17() + "	" + po.getH18() + "	" +
				 * po.getH19() + "	" + po.getH20() + "	" + po.getH21() + "	" +
				 * po.getH22() + "	" + po.getH23() + "	" + po.getH24() + "	" +
				 * po.getH25() + "		" + po.getH26() + "	" + po.getH27() + "	" +
				 * po.getH28() + "	" + po.getH29() + "	" + po.getH30() + "	" +
				 * po.getH31() + "	" + po.getH32() + "	" + po.getH33() + "	" +
				 * po.getH34() + "	" + po.getH35() + "	" + po.getH36() + "	" +
				 * po.getH37() + "	" + po.getH38() + "	" + po.getH39() + "	" +
				 * po.getH40() + "	" + po.getH41() + "	" + po.getH42() + "	" +
				 * po.getH43() + "	" + po.getH44() + "	" + po.getH45() + "	" +
				 * po.getH46() + "	" + po.getH47() + "	" + po.getH48() + "	" +
				 * po.getH49() + "	" + po.getH50() + "	" + po.getH51() + "	" +
				 * po.getH52() + "	" + po.getH53() + "	" + po.getH54() + "		" +
				 * po.getH55() + "	" + po.getH56() + "	" + po.getH57() + "	" +
				 * po.getH58() + "	" + po.getH59() + "	" + po.getH60() + "	" +
				 * po.getH61() + "	" + po.getH62() + "	" + po.getH63() + "	" +
				 * po.getH64() + "	" + po.getH65() + "	" + po.getH66() + "	" +
				 * po.getH67() + "	" + po.getH68() + "	" + po.getH69() + "	" +
				 * po.getH70() + "	" + po.getH71() + "	" + po.getH72() + "	" +
				 * po.getH73() + "	" + po.getH74() + "	" + po.getH75() + "	" +
				 * po.getH76() + "	" + po.getH77() + "	" + po.getH78() + "	" +
				 * po.getH79() + "	" + po.getH80()+ "	" + po.getH81()+ "	" +
				 * po.getH82()+ "	" + po.getH83()+ "	" + po.getH84()+ "	" +
				 * po.getH85()+ "	" + po.getH86()+ "	" + po.getH87()+ "	" +
				 * po.getH88()+ "	" + po.getH89()+ "	" + po.getH90()+ "	" +
				 * po.getH91()+ "	" + po.getH92()+ "	" + po.getH93()+ "	" +
				 * po.getH94()+ "	" + po.getH95()+ "	" + po.getH96() + "\r\n");
				 */
				sbf.append("#	1	" + pathPo.getMpath() + "	" + time);
				for (int i = 1; i < 97; i++) {
					String getAttributeMethodName = "getH" + (i < 10 ? "0" + i : i);
					Method getAttributeMethod = null;
					try {
						getAttributeMethod = LineLimitPo.class.getDeclaredMethod(getAttributeMethodName);

						try {
							Object value1 = getAttributeMethod.invoke(po);
							sbf.append("	" + value1);
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
				sbf.append("\r\n");
				sbf.append("</Corridor::国调>" + "\r\n" + "\r\n");
				sbf.append("<!	Entity=国调	type=全数	End	!>");

				String fileName = "CBPM_CORRIDOR_" + pathPo.getMpath() + "_" + time + ".dat";

				LoggerUtil.log(this.getClass().getName(), fileSrc + fileName, 0);

				File file = new File(fileSrc, fileName);
				String absolutePath = "";
				if (!file.exists()) {// 文件不存在，则创建
					try {
						file.createNewFile();
						absolutePath = file.getAbsolutePath();
					} catch (IOException e) {
						e.printStackTrace();
						j.setMsg("无法创建文件！");
					}
				} else {// 文件存在，删除后重建
					// System.out.println(fileName +
					// "---------------导出文件存在，执行删除");
					file.delete();
					file = new File(fileSrc, fileName);
					try {
						file.createNewFile();
						absolutePath = file.getAbsolutePath();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				appendMethodB(absolutePath, sbf);
				// FileManager.writeToFile(absolutePath, sbf.toString(), true);

				// 生成头文件
				String ehFileName = "CBPM_CORRIDOR_" + pathPo.getMpath() + "_" + time + ".eh";
				String ehAbsolutePath = "";
				LoggerUtil.log(this.getClass().getName(), fileSrc + ehFileName, 0);

				File ehFile = new File(fileSrc, ehFileName);
				StringBuffer ensbf = new StringBuffer();
				if (!ehFile.exists()) {// 文件不存在，则创建
					try {
						ehFile.createNewFile();
						ehAbsolutePath = ehFile.getAbsolutePath();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {// 文件存在，删除后重建
					// System.out.println(fileName +
					// "---------------导出文件存在，执行删除");
					ehFile.delete();
					ehFile = new File(fileSrc, fileName);
					try {
						ehFile.createNewFile();
						ehAbsolutePath = ehFile.getAbsolutePath();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				String mpath = pathPo.getMpath();
				String[] receArea = mpath.split("送");
				ensbf.append("<CBPM::传输说明 time='" + CommonUtil.getDateTime() + "'	!>" + "\r\n" + "@#顺序 属性名 属性值" + "\r\n" + "#0 标识 " + ehFileName + "\r\n" + "#1 发送地址 国调2.CBPM \r\n#2 接收地址 " + receArea[0] + "2.CBPM " + receArea[1] + "2.CBPM \r\n#3 传输类型 文件 \r\n#4 内容 '' \r\n#5 文件 '" + fileName + "'\r\n</CBPM::传输说明>");
				appendMethodB(ehAbsolutePath, ensbf);
				// FileManager.writeToFile(ehAbsolutePath, ensbf.toString(),
				// true);
			}
		}
		j.setStatus(true);
		return j;
	}

	/**
	 * 根据小邮件导出通道剩余容量
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月20日下午4:29:01
	 */
	public JsonMsg localExportMpathLimit(String time) {

		time = time.replaceAll("-", "");

		List<PathDefinePo> pathList = pathDefineDao.selectPathALL(time);// 遍历通道
		List<LineDefinePo> lineList = lineDefineDao.getAllLine();// 遍历联络线
		List<LineLimitPo> limitList = lineLimitDao.getLineLimitList(time);// 联络线限额---排除交易功率
		JsonMsg j = new JsonMsg();
		Map<String, Map<String, LineLimitPo>> linelimitMap = new HashMap<String, Map<String, LineLimitPo>>();
		if (limitList != null) {
			Map<String, LineLimitPo> temp = null;
			for (LineLimitPo po : limitList) {
				// 如果包含该联络线
				if (linelimitMap.containsKey(po.getMcorhr())) {
					temp = linelimitMap.get(po.getMcorhr());
				} else {
					temp = new HashMap<String, LineLimitPo>();
					linelimitMap.put(po.getMcorhr(), temp);
				}
				temp.put(po.getDtype(), po);
			}
		}

		// 判断是用哪个减 差值空间 = 静态限额/动态限额-日前计划
		PfSysConfigPo config = ServiceHelper.getBean(SystemServiceI.class).getSysConfig(Enums_SystemConst.SPACE_EXCUTE_TYPE);
		String cacuteType = "动态正向限额";
		if (config == null || config.getValue().equals("1")) {
			cacuteType = "静态正向限额";
		}

		// 计算各个联络线的差值空间
		for (Entry<String, Map<String, LineLimitPo>> mcohrEnt : linelimitMap.entrySet()) {
			LineLimitPo minuendPo = mcohrEnt.getValue().get(cacuteType);
			LineLimitPo beforDayPo = mcohrEnt.getValue().get("日前计划值");
			if (mcohrEnt.getKey().equals(""))
				// System.out.println("minuendPo"+CommonUtil.objToJson(minuendPo));
				// System.out.println("beforDayPo"+CommonUtil.objToJson(beforDayPo));

				if (minuendPo == null) {
					minuendPo = new LineLimitPo();
				}
			if (beforDayPo == null) {
				beforDayPo = new LineLimitPo();
			}
			LineLimitPo spacePo = new LineLimitPo();
			mcohrEnt.getValue().put("差值空间", spacePo);
			// 计算差值空间
			for (int i = 1; i < 97; i++) {
				String getAttributeMethodName = "getH" + (i < 10 ? "0" + i : i);
				String setAttributeMethodName = "setH" + (i < 10 ? "0" + i : i);
				Method getAttributeMethod = null;
				Method setAttributeMethod = null;
				try {
					getAttributeMethod = LineLimitPo.class.getDeclaredMethod(getAttributeMethodName);
					setAttributeMethod = LineLimitPo.class.getDeclaredMethod(setAttributeMethodName, double.class);

					try {
						double value1 = 0;
						if (minuendPo != null) {
							value1 = (Double) getAttributeMethod.invoke(minuendPo);
						}
						double value2 = 0;
						if (beforDayPo != null) {
							value2 = (Double) getAttributeMethod.invoke(beforDayPo);
						}
						double value = value1 - value2;
						setAttributeMethod.invoke(spacePo, value);
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
		}

		// String fileSrc = SystemConstUtil.getMatchPath() + File.separator +
		// "trans_data" + File.separator + "send" + File.separator;
		// 创建导出文件
		StringBuffer sbf = new StringBuffer();
		sbf.append("<!	Entity=国调	type=全数	time='" + CommonUtil.getDateTime() + "'	!>" + "\r\n" + "\r\n" + "<Corridor::国调 type=全数>" + "\r\n" + "@	Number	CORRIDOR	MDATE	H01		H02  H03	H04 	H05 	H06	  H07	 H08	 H09	 H10	 H11	 H12	H13  H14	H15	 H16	H17  	H18	  H19	H20 	H21 	H22 	H23		H24		H25 	H26		H27 	H28 	H29 	H30 	H31 	H32 	H33 	H34 	H35 	H36 	H37 	H38 	H39 	H40 	H41 	H42 	H43 	H44 	H45 	H46 	H47 	H48 	H49 	H50 	" + "H51 	H52 	H53 	H54 	H55 	H56 	H57 	H58 	H59 	H60 	H61 	H62 	H63 	H64 	H65 	H66		H67 	H68		H69 	H70 	H71 	H72 	H73 	H74 	H75 	H76 	H77		H78		H79		H80		H81		H82 	H83 	H84 	H85 	H86		H87		H88		H89		H90		H91		H92		H93 	H94		H95		H96" + "\r\n"
				+ "//	序号 	通道名称 	  交易日期		00:15	00:30	00:45	01:00	01:15	01:30	01:45	02:00	02:15	02:30	02:45	03:00	03:15	03:30	03:45	04:00	04:15	04:30	04:45	05:00	05:15	05:30	05:45	06:00	06:15	06:30	06:45	07:00	07:15	07:30	07:45	08:00   08:15   08:30  08:45  09:00  09:15  09:30  09:45  10:00  10:15  10:30  10:45  11:00  11:15  11:30  11:45  12:00  " + "12:15	12:30	12:45	13:00	13:15	13:30	13:45	14:00	14:15	14:30	14:45	15:00	15:15	15:30	15:45	16:00	16:15	16:30	16:45	17:00	17:15	17:30	17:45	18:00	18:15	18:30	18:45	19:00	19:15	19:30	19:45	20:00   20:15   20:30  20:45  21:00  21:15  21:30  21:45  22:00  22:15  22:30  22:45  23:00  23:15  23:30  23:45  24:00" + "\r\n");

		if (linelimitMap == null || linelimitMap.size() == 0) {
			throw new MsgException("今日没有限额！");
		}

		// 计算各通道限额
		if (pathList != null && pathList.size() > 0) {
			for (PathDefinePo pathPo : pathList) {
				// 多个联络线，对应多个方向
				String mdirection = "";// pathPo.getMdirection();
				// 计算最后的差值空间
				LineLimitPo po = new LineLimitPo();
				for (int m = 1; m <= mdirection.length(); m++) {
					// 获取物理联络线
					String corhrName = getCorhrName(m, pathPo);
					LineLimitPo t = linelimitMap.get(corhrName).get("差值空间");
					// System.out.println(corhrName+"=="+CommonUtil.objToJson(t));
					if (t != null) {

						for (int i = 1; i < 97; i++) {
							String getAttributeMethodName = "getH" + (i < 10 ? "0" + i : i);
							String setAttributeMethodName = "setH" + (i < 10 ? "0" + i : i);
							Method getAttributeMethod = null;
							Method setAttributeMethod = null;
							try {
								getAttributeMethod = LineLimitPo.class.getDeclaredMethod(getAttributeMethodName);
								setAttributeMethod = LineLimitPo.class.getDeclaredMethod(setAttributeMethodName, double.class);

								try {
									double value1 = 0;
									value1 = (Double) getAttributeMethod.invoke(po);

									double value2 = 0;
									value2 = (Double) getAttributeMethod.invoke(t);
									if (m == 1) {
										setAttributeMethod.invoke(po, value2);
									} else {
										if (value2 < value1) {
											setAttributeMethod.invoke(po, value2);
										}
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
					}
				}

				sbf.append("#	1	" + pathPo.getMpath() + "	" + time + "	" + po.getH01() + "	" + po.getH02() + "	" + po.getH03() + "	" + po.getH04() + "	" + po.getH05() + "	" + po.getH06() + "	" + po.getH07() + "	" + po.getH08() + "	" + po.getH09() + "	" + po.getH10() + "	" + po.getH11() + "	" + po.getH12() + "	" + po.getH13() + "	" + po.getH14() + "	" + po.getH15() + "	" + po.getH16() + "	" + po.getH17() + "	" + po.getH18() + "	" + po.getH19() + "	" + po.getH20() + "	" + po.getH21() + "	" + po.getH22() + "	" + po.getH23() + "	" + po.getH24() + "	" + po.getH25() + "		" + po.getH26() + "	" + po.getH27() + "	" + po.getH28() + "	" + po.getH29() + "	" + po.getH30() + "	" + po.getH31() + "	" + po.getH32() + "	" + po.getH33() + "	" + po.getH34() + "	" + po.getH35() + "	" + po.getH36() + "	"
						+ po.getH37() + "	" + po.getH38() + "	" + po.getH39() + "	" + po.getH40() + "	" + po.getH41() + "	" + po.getH42() + "	" + po.getH43() + "	" + po.getH44() + "	" + po.getH45() + "	" + po.getH46() + "	" + po.getH47() + "	" + po.getH48() + "	" + po.getH49() + "	" + po.getH50() + "	" + po.getH51() + "	" + po.getH52() + "	" + po.getH53() + "	" + po.getH54() + "		" + po.getH55() + "	" + po.getH56() + "	" + po.getH57() + "	" + po.getH58() + "	" + po.getH59() + "	" + po.getH60() + "	" + po.getH61() + "	" + po.getH62() + "	" + po.getH63() + "	" + po.getH64() + "	" + po.getH65() + "	" + po.getH66() + "	" + po.getH67() + "	" + po.getH68() + "	" + po.getH69() + "	" + po.getH70() + "	" + po.getH71() + "	" + po.getH72() + "	" + po.getH73() + "	" + po.getH74() + "	" + po.getH75()
						+ "	" + po.getH76() + "	" + po.getH77() + "	" + po.getH78() + "	" + po.getH79() + "	" + po.getH80() + "\r\n");

			}
		}
		sbf.append("</Corridor::国调>" + "\r\n" + "\r\n");
		sbf.append("<!	Entity=国调	type=全数	End	!>");

		j.setStatus(true);
		j.setMsg(sbf.toString());
		return j;
	}

	/**
	 * @throws IOException 
	 * 
	 */
	public String createLimitLineFile(String path, String shortTime, String time) throws IOException {
		// 其中time1的时间格式为 2016-11-10，time的时间格式为 20161110
		String forward = "creatSuccess";
		// System.out.println("-----"+time+"-------"+time1);
		StringBuffer sessionBuffer = new StringBuffer();
		StringBuffer timeBuffer = new StringBuffer();
		for (int i = 1; i <= 96; i++) {
			sessionBuffer.append("H" + (i < 10 ? "0" + i : i) + "  ");
			timeBuffer.append(DeclareUtil.getInterval(i) + "  ");
		}

		// 判断是用哪个减 差值空间 = 静态限额/动态限额-日前计划
		PfSysConfigPo config = ServiceHelper.getBean(SystemServiceI.class).getSysConfig(Enums_SystemConst.SPACE_EXCUTE_TYPE);
		String cacuteType = "动态正向限额";
		if (config == null || config.getValue().equals("1")) {
			cacuteType = "静态正向限额";
		}
		System.out.println("--------"+path);
		//List<LineDefinePo> list = lineDefineDao.getAllLineByArea("");
		//System.out.println("------------"+list.size());
		//List<LineDefinePo> list = lineDefineDao.getAllLine();
		//Map<String, List<LineDefinePo>> lineMap = MatchUtil.getLineDefine(list);
		//List<DeclarePo> areaList = declareDao.getAreaListByMdate(shortTime);
		List<AreaPo> areaList = areaDao.getAllArea();
		String _N = "\r\n";// "\r\n"
		if (areaList != null && areaList.size() > 0) {
			for (AreaPo areaPo : areaList) {
				String key = areaPo.getArea();
				LoggerUtil.log("省份------"+key,0);
				String datFileName = "CBPM_CORRIDOR_" + key + "_" + shortTime + ".dat";
				String ehFileName = "CBPM_CORRIDOR_" + key + "_" + shortTime + ".eh";
				File datFile = new File(path+datFileName);
				if(datFile.exists()){
					datFile.delete();
				}else{
					datFile.createNewFile();
				}
				File ehFile = new File(path+ehFileName);
				if(ehFile.exists()){
					ehFile.delete();
				}else{
					ehFile.createNewFile();
				}
				int num = 1;
				StringBuffer corridorBuffer = new StringBuffer();
				Date tomorrow = new Date((new Date()).getTime());
				String sysTime = DateUtil.format(tomorrow, "yyyyMMdd_hh:MM:ss");
				corridorBuffer.append("<!	Entity=国调	type=全数	time='" + sysTime + "'	!>" + _N + _N + "<Corridor:: 国调 type=全数>" + _N + "@	NUMBER	CORRIDOR  MDATE	");
				corridorBuffer.append(sessionBuffer + " " + _N);
				corridorBuffer.append("//  序号  通道名称  交易日期   ");
				corridorBuffer.append(timeBuffer + " " + _N);
				List<LineDefinePo> lineList = lineDefineDao.getAllLineByArea(key);
				for (LineDefinePo lineObj : lineList) {

					LineLimitPo dayData = lineLimitDao.getLineLimit(lineObj.getMcorhr(), shortTime, "日前计划值", "");
					LineLimitPo staticData = lineLimitDao.getLineLimit(lineObj.getMcorhr(), shortTime, cacuteType, "");
					if (dayData == null) {
						dayData = new LineLimitPo();
					}
					if (staticData == null) {
						staticData = new LineLimitPo();
					}
					// 96值字符串
					StringBuffer valBuffer = new StringBuffer();
					for (int i = 1; i <= 96; i++) {
						String getAttributeMethodName = "getH" + (i < 10 ? "0" + i : i);
						Method getAttributeMethod = null;
						try {
							getAttributeMethod = LineLimitPo.class.getDeclaredMethod(getAttributeMethodName);
							try {
								double value1 = 0;
								if (staticData != null) {
									Object o = getAttributeMethod.invoke(staticData);
									if(o != null){
										value1 = (Double) o;
										value1 = Math.abs(value1);
									}
								}
								double value2 = 0;
								if (dayData != null) {
									Object o = getAttributeMethod.invoke(dayData);
									if(o != null){
										value2 = (Double) o;
										value2 = Math.abs(value2);
									}
								}
								if(value1<value2){
									value2 = value1;
								}
								double value = value1 - value2;
								if(value<0){
									value = 0;
								}
								valBuffer.append(value + "  ");
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
					corridorBuffer.append("#  " + (num++) + "  " + lineObj.getMcorhr() + "  " + time + "  ");
					corridorBuffer.append(valBuffer + "  " + _N);
				}
				corridorBuffer.append("</Corridor::国调>" + _N + _N);
				LoggerUtil.log(datFile.getAbsolutePath()+"---", 0);
				appendMethodB(datFile.getAbsolutePath(), corridorBuffer);

				StringBuffer ensbf = new StringBuffer();
				LoggerUtil.log(ehFile.getAbsolutePath()+"---", 0);
				ensbf.append("<CBPM::传输说明 time='"+TimeUtil.getNowOfDateByFormat("yyyyMMdd hh:mm:ss")+ "'>"+ "\r\n" +"@#顺序 属性名 属性值"+ "\r\n"+"#0 标识 "+ehFileName+ "\r\n"+"#1 发送地址 国调2.CBPM \r\n#2 接收地址 "+key+"2.CBPM \r\n#3 传输类型 文件 \r\n#4 内容 '' \r\n#5 文件 '"+datFileName+"'\r\n</CBPM::传输说明>");
				appendMethodB(ehFile.getAbsolutePath(), ensbf);
				
			}
		}
		return forward;
	}

}
