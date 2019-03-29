package com.state.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jrsoft.util.process.ProcessUtils;
import com.jrsoft.util.process.ProcessUtils.ProcessStatus;
import com.state.enums.Enums_SystemConst;
import com.state.exception.MsgException;
import com.state.po.DeclareDataPo;
import com.state.po.LineDefinePo;
import com.state.po.LineLimitPo;
import com.state.po.PathResultPo;
import com.state.po.ResultPo;
import com.state.po.TypePo;
import com.state.po.extend.JsonMsg;
import com.state.po.sys.PfSysConfigPo;
import com.state.service.IDeclareService;
import com.state.service.ILineService;
import com.state.service.LineLimitService;
import com.state.service.MatchService;
import com.state.service.PfLoggerServiceI;
import com.state.service.ServiceHelper;
import com.state.service.ShellExecuteServiceI;
import com.state.service.SystemServiceI;
import com.state.util.CommonUtil;
import com.state.util.LoggerUtil;
import com.state.util.SessionUtil;
import com.state.util.SystemTools;
import com.state.util.TimeUtil;
import com.state.util.sys.SystemConstUtil;

@Controller
@RequestMapping("/lineLimit")
public class LineLimitController {
	private static final transient Logger log = Logger.getLogger(LineLimitController.class);
	public static final String MODEL_NAME_="限额管理";
	@Autowired
	private LineLimitService lineLimitService;

	@Autowired
	private IDeclareService declareService;

	@Autowired
	private ILineService lineService;

	@Autowired
	private MatchService matchService;

	/**
	 * 跳转联络线限额
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/init")
	public String init(Model model) {
		log.info("@ init limitLineR ");
		TypePo timeType = declareService.getTimeType();
		String limitType = lineLimitService.getConfigStr();
		timeType.countType();
		model.addAttribute("limitType", limitType);
		model.addAttribute("timeType", JSON.toJSON(timeType).toString());
		model.addAttribute("jspType", "limitLine");
		//获取当前服务器时间
		model.addAttribute("serviceDate", TimeUtil.getStringDate());
		return "limitLine";
	}

	/**
	 * 查询所有的联络线
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAllLine")
	public List<LineDefinePo> getAllLine(HttpServletRequest request, HttpServletResponse response) {
		log.info("@ getAllLine ");
		String area = SessionUtil.getArea();
		//String area = SessionUtil.getArea();
		
		//List<LineDefinePo> list = lineDefineDao.getAllLineByArea(area.equals("国调")?"":area);
		List<LineDefinePo> list = lineService.getAllLineByArea(area.equals("国调")?"":area);
		return list;
	}

	/**
	 * 查询指定类型的联络线限额
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getLineLimit")
	public LineLimitPo getLineLimit(String mcorhr, String dtype, String time, String state) {
		log.info("@ getLineLimit");

		return lineLimitService.selectLineLimitList(mcorhr, time, dtype, state);
	}

	/**
	 * 加载通道三种类型限额
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/loadChartData")
	public Map loadChartData(String mcorhr, String dtype, String time, String state) {
		log.info("@ getLineLimit");
		Map map = new HashMap<String, Object>();
		LineLimitPo po1 = lineLimitService.selectLineLimitList(mcorhr, time, "正向限额", state);
		LineLimitPo po2 = lineLimitService.selectLineLimitList(mcorhr, time, "反向限额", state);
		LineLimitPo po3 = lineLimitService.selectLineLimitList(mcorhr, time, "计划值", state);
		map.put("data", po1);
		map.put("data1", po2);
		map.put("data2", po3);
		return map;
	}

	/**
	 * 单击联络线获取该联络线的所有数据
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月19日上午9:36:49
	 * @param mcorhr
	 * @param dtype
	 * @param time
	 * @param state
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAllLimitLineData")
	public Map<String, Object> getAllLimitLineData(String mcorhr, String dtype, String time) {
		log.info("@ getLineLimit");
		String state = null;
		// 如果是非国调用户
		if (!SessionUtil.isState()) {
			state = "1";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		LineLimitPo beforeData = lineLimitService.selectLineLimitList(mcorhr, time, "日前计划值", state);
		LineLimitPo dayData = lineLimitService.selectLineLimitList(mcorhr, time, "日内计划值", state);
		LineLimitPo staticData = lineLimitService.selectLineLimitList(mcorhr, time, "静态正向限额", state);
		LineLimitPo dynamicData = lineLimitService.selectLineLimitList(mcorhr, time, "动态正向限额", state);
		LineLimitPo tradeData = lineLimitService.selectLineLimitList(mcorhr, time, "交易功率", state);

		if (beforeData == null) {
			beforeData = new LineLimitPo();
		}
		if (dayData == null) {
			dayData = new LineLimitPo();
		}
		if (dynamicData == null) {
			dynamicData = new LineLimitPo();
		}
		if (staticData == null) {
			staticData = new LineLimitPo();
		}
		if (tradeData == null) {
			tradeData = new LineLimitPo();
		}

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
						Object o1 = getAttributeMethod.invoke(minuendData);
						if (o1 != null) {
							value1 = (Double) o1;
						}
					}
					double value2 = 0;
					if (minuendData != null) {
						Object o2 =  getAttributeMethod.invoke(beforeData);
						if (o2 != null) {
							value2 = (Double) o2;
						}
					}
					double value = value1 - value2;
					if(value < 0){
						value = 0;
					}
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

		map.put("日前计划值", beforeData);
		map.put("日内计划值", dayData);
		map.put("静态正向限额", staticData);
		map.put("动态正向限额", dynamicData);
		map.put("交易功率", tradeData);
		map.put("可用容量", spaceData);
		return map;
	}

	/**
	 * 更新联络线
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateLineLimit")
	public String updateLineLimit(String time, String lineLimitStr) {
		log.info("@ updateLineLimit ");
		//System.out.println(time+"========" + lineLimitStr);

		JSONObject bean = com.alibaba.fastjson.JSONObject.parseObject(lineLimitStr);
		LineLimitPo lineLimit = JSONObject.toJavaObject(bean, LineLimitPo.class);
		if(lineLimit != null && !CommonUtil.ifEmpty(lineLimit.getMdate())){
			lineLimit.setMdate(time.replaceAll("-", ""));
		}
		//System.out.println(lineLimit.getMcorhr()+"==="+lineLimit.getMdate()+"==="+lineLimit.getDtype());
		LineLimitPo lp = lineLimitService.getLineLimit(lineLimit.getMcorhr(), lineLimit.getMdate(), lineLimit.getDtype());
		//System.out.println("lp----"+lp);
		if (lp != null) {
			lineLimitService.updateLineLimit(lineLimit);
		} else {
			lineLimitService.insertLineLimit(lineLimit);
		}

		return "success";
	}

	/**
	 * 获取上下限以及计划值
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getLineLimitAndPlan")
	public JsonMsg getLineLimitAndPlan(String time, String time1) {
		log.info("@ getLineLimitAndPlan ");
		return ServiceHelper.getBean(ShellExecuteServiceI.class).getLineLimitAndPlan(false,time);
	}

	/**
	 * 可用容量发布
	 * 导出交易功率到计划系统
	 * @description
	 * @author 大雄
	 * @date 2016年9月19日下午2:09:38
	 * @param time
	 * @param time1
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/exportToPlan")
	public JsonMsg exportToPlan(String time, String shortTime) {
		log.info("@ exportToPlan ");
		// System.out.println("++++++++++++++开始调用C程序======>plan_copy2.sh++++++++++++");
		return ServiceHelper.getBean(ShellExecuteServiceI.class).exportToPlan(false,time, shortTime);
		// System.out.println("++++++++++++++开始调用C程序======>insert_cbpm2.sh++++++++++++");

	}
	
	@RequestMapping(value = "/exportLimitData", method = RequestMethod.POST)
	@ResponseBody
	public String exportLimitData(HttpServletRequest request, HttpServletResponse response, String mcorhr, String time, String state) {

		// linux创建bat文件，为C提供数据文件
		LineLimitPo po3 = lineLimitService.selectLineLimitList(mcorhr, time, "计划值", state);
		String creatStr = matchService.createLimitFile(Enums_SystemConst.RESULT_FILE_PATH_WIN, mcorhr, time, po3);
		// System.out.println("po3==="+CommonUtil.objToJson(po3));
		// String creatStr =
		// matchService.createLimitFile(SystemConstUtil.getMatchPath()+File.separator+"data",mcorhr,
		// time, po3);
		// String fileName = path + File.separator +"data"+ File.separator+
		// Enums_SystemConst.RESULT_FILE_NAME + mdate + ".dat";
		// System.out.println("creatStr===="+creatStr);
		return creatStr;
	}
	
	/**
	 * 本地导出限额
	 * @author 车斯剑
	 * @date 2016年9月19日下午4:39:59
	 * @param request
	 * @param response
	 * @param startTime
	 * @param endTime
	 * @param str
	 * @param type
	 */
	@RequestMapping(value = "/localExportData", method = RequestMethod.GET)
	public void localExportData(HttpServletRequest request, HttpServletResponse response,String time) {

		try {
			
			time = time.replaceAll("-", "");
			String zipname = "CBPM_CORRIDOR_"+time+ ".dat";
			
			int len = 0;
			byte buf[] = new byte[1024];// 缓存作用
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/octet-stream; charset=UTF-8");
			response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(zipname.getBytes("GB2312"), "ISO8859-1") + "\";");//
			JsonMsg j = matchService.localExportMpathLimit(time);
			
			if(!j.getStatus()){
				throw new MsgException(j.getMsg());
			}
			//System.out.println("===========");
			//System.out.println(j.getMsg());
			OutputStream os = response.getOutputStream();
			Writer writer = new OutputStreamWriter(os);
			
			writer.write(j.getMsg());
			writer.flush();
			os.flush();
			os.close();
		} catch (MsgException e) {
			e.printStackTrace();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
			

	}

}
