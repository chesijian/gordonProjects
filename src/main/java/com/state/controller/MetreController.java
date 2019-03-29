package com.state.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import com.state.enums.Enums_SystemConst;
import com.state.exception.MsgException;
import com.state.po.DeclareDataPo;
import com.state.po.LineDefinePo;
import com.state.po.LineLimitPo;
import com.state.po.PathResultPo;
import com.state.po.TypePo;
import com.state.po.extend.JsonMsg;
import com.state.po.sys.PfSysConfigPo;
import com.state.service.IDeclareService;
import com.state.service.ILineService;
import com.state.service.LineLimitService;
import com.state.service.MatchService;
import com.state.service.PfLoggerServiceI;
import com.state.service.ServiceHelper;
import com.state.service.SystemServiceI;
import com.state.util.CommonUtil;
import com.state.util.LoggerUtil;
import com.state.util.SessionUtil;
import com.state.util.TimeUtil;
import com.state.util.sys.SystemConstUtil;

@Controller
@RequestMapping("/metre")
public class MetreController {
	private static final transient Logger log = Logger.getLogger(MetreController.class);
	public static final String MODEL_NAME_="计量";
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
		log.info("@ init metreR ");
		TypePo timeType = declareService.getTimeType();
		timeType.countType();
		model.addAttribute("timeType", JSON.toJSON(timeType).toString());
		model.addAttribute("jspType", "metre");
		//获取当前服务器时间
		model.addAttribute("serviceDate", TimeUtil.getStringDate());
		return "metre";
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
		log.info("@ getLineLimit=======");
		//LoggerUtil.log("==========="+mcorhr, 0);
		System.out.println("MetreController"+mcorhr);
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
	public Map<String, Object> getAllLimitLineData(String mcorhr, String dtype, String time, String state) {
		log.info("@ getLineLimit");
		Map<String, Object> map = new HashMap<String, Object>();
		LineLimitPo dayData = lineLimitService.getDLTJPo(mcorhr, time);
		
		if (dayData == null) {
			dayData = new LineLimitPo();
		}

		map.put("日内计划值", dayData);
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

		JSONObject bean = com.alibaba.fastjson.JSONObject.parseObject(lineLimitStr);
		LineLimitPo lineLimit = JSONObject.toJavaObject(bean, LineLimitPo.class);
		LineLimitPo lp = lineLimitService.getLineLimit(lineLimit.getMcorhr(), lineLimit.getMdate(), lineLimit.getDtype());
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
		// System.out.println("++++++++++++++开始调用C程序======>plan_copy2.sh++++++++++++");
		JsonMsg j = new JsonMsg();
		try {
			LoggerUtil.log(this.getClass().getName(), "++++++++++++++开始调用C程序======>dltj_copyto_cbpm.sh++++++++++++"+SystemConstUtil.getMatchPath() + File.separator + "bin" + File.separator + "./dltj_copyto_cbpm.sh " + time, 0);
			Process process;
			try {
				process = Runtime.getRuntime().exec(SystemConstUtil.getMatchPath() + File.separator + "bin" + File.separator + "./dltj_copyto_cbpm.sh " + time);
				try {
					process.waitFor();
					process.exitValue();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new MsgException("调用C程序获取失败！");
			}
			LoggerUtil.log(this.getClass().getName(), "已经调用了C,现在开始判断是否调用成功!", 0);
			
			boolean flag = false;
			int index = 1;
			while (!flag) {
				if (index == 5) {
					break;
				}
				String state = matchService.selectProgramInfo("电量获取");
				if (state.equals("成功")) {
					flag = true;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				index++;
			}
			if(flag){
				j.setStatus(true);
				j.setMsg("获取成功！");
			}else{
				throw new MsgException("获取失败!");
			}
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "获取电量数据", "getLineLimitDLTJ",time+","+time1, null, true);
			
			// System.out.println("++++++++++++++C程序======>plan_copy2.sh执行完毕++++++++++++");
			LoggerUtil.log(this.getClass().getName(), "++++++++++++++C程序======>plan_copyto_cbpm.sh执行完毕++++++++++++", 0);
			
		} catch (MsgException e) {
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "获取电量数据", "getLineLimitDLTJ",time+","+time1, e.getMessage(), false);
			
			j.setMsg(e.getMessage());
		} catch (Exception e) {
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "获取电量数据", "getLineLimitDLTJ",time+","+time1, e.getMessage(), false);
			
			j.setMsg("获取失败！" + e.getMessage());
		} finally {
			return j;
		}
		// System.out.println("++++++++++++++开始调用C程序======>insert_cbpm2.sh++++++++++++");

	}

	/**
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
	public JsonMsg exportToPlan(String time, String time1) {
		log.info("@ exportToPlan ");
		// System.out.println("++++++++++++++开始调用C程序======>plan_copy2.sh++++++++++++");
		JsonMsg j = new JsonMsg();
		try {
			LoggerUtil.log(this.getClass().getName(), "++++++++++++++开始调用C程序======>cbpm_copyto_plan.sh++++++++++++", 0);
			Process process;
			try {
				process = Runtime.getRuntime().exec(SystemConstUtil.getMatchPath() + File.separator + "bin" + File.separator + "./cbpm_copyto_plan.sh " + time);
				try {
					process.waitFor();
					process.exitValue();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new MsgException("调用C程序获取失败！");
			}

			boolean flag = false;
			int index = 1;
			while (!flag) {
				if (index == 5) {
					break;
				}
				String state = matchService.selectProgramInfo("计划导入");
				if (state.equals("成功")) {
					flag = true;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				index++;
			}
			if(flag){
				ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "交易功率导出至计划", "exportToPlan",time+","+time1, null, false);
				
				j.setStatus(true);
				j.setMsg("导入成功！");
			}else{
				throw new MsgException("导入失败！");
			}
			// System.out.println("++++++++++++++C程序======>plan_copy2.sh执行完毕++++++++++++");
			LoggerUtil.log(this.getClass().getName(), "++++++++++++++C程序======>cbpm_copyto_plan.sh执行完毕++++++++++++", 0);
			
		} catch (MsgException e) {
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "交易功率导出至计划", "exportToPlan",time+","+time1, e.getMessage(), false);
			
			j.setMsg(e.getMessage());
		} catch (Exception e) {
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "交易功率导出至计划", "exportToPlan",time+","+time1, e.getMessage(), false);
			
			j.setMsg("导入失败！" + e.getMessage());
		} finally {
			return j;
		}
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
	@RequestMapping(value = "/measure")
	public String measure(Model model) {
		log.info("@ init metreR ");
		TypePo timeType = declareService.getTimeType();
		timeType.countType();
		model.addAttribute("timeType", JSON.toJSON(timeType).toString());
		model.addAttribute("jspType", "metre");
		return "measure";
	}
}

