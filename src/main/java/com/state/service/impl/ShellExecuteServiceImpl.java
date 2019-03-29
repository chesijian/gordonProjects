package com.state.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jrsoft.util.process.ProcessUtils;
import com.jrsoft.util.process.ProcessUtils.ProcessStatus;
import com.state.enums.Enums_SystemConst;
import com.state.exception.MsgException;
import com.state.po.LineLimitPo;
import com.state.po.UserPo;
import com.state.po.extend.JsonMsg;
import com.state.service.IDeclareService;
import com.state.service.ILineService;
import com.state.service.IssueService;
import com.state.service.LineLimitService;
import com.state.service.MatchService;
import com.state.service.PfLoggerServiceI;
import com.state.service.ServiceHelper;
import com.state.service.ShellExecuteServiceI;
import com.state.service.SystemServiceI;
import com.state.util.CommonUtil;
import com.state.util.FileManager;
import com.state.util.LoggerUtil;
import com.state.util.SessionUtil;
import com.state.util.SystemTools;
import com.state.util.sys.SystemConstUtil;

@Service
@Transactional
public class ShellExecuteServiceImpl implements ShellExecuteServiceI {

	@Autowired
	private IDeclareService declareService;
	@Autowired
	private MatchService matchService;
	@Autowired
	private LineLimitService  lineLimitService;
	@Autowired
	private IssueService issueService;
	/**
	 * 调用脚本导入送点侧报价数据
	 * @description
	 * @author 大雄
	 * @date 2016年12月21日下午3:32:43
	 * @param mdate
	 * @return
	 */
	public JsonMsg importDeclare(boolean isAuto,String mdate){
		String MODEL_NAME_ =  isAuto?"购售电申报单-自动程序":"购售电申报单";
		JsonMsg j = new JsonMsg();
		try {
			LoggerUtil.log(this.getClass().getName(), "++++++++++++++开始调用C程序======>Get_trans_data.sh++++++++++++"+SystemConstUtil.getMatchPath() + File.separator + "bin" + File.separator + "Get_trans_data.sh", 0);
			if(SystemTools.isLinux()){
				try {
					ProcessStatus ps = ProcessUtils.execute(20000,null, SystemConstUtil.getMatchPath() + File.separator + "bin" + File.separator + "Get_trans_data.sh" );
				} catch (IOException e) {
					e.printStackTrace();
					throw new MsgException("调用C程序获取失败！");
				}
			}
			LoggerUtil.log(this.getClass().getName(), "++++++++脚本触发结束++++++++", 0);

			boolean flag = false;
			int index = 1;
			int size = 0;
			String simpleMdate = mdate.replaceAll("-", "");
			String fileSrc = SystemConstUtil.getMatchPath() + File.separator + "trans_data" + File.separator + "recv" + File.separator;

			while (!flag) {
				if (index == 5) {
					break;
				}
				LoggerUtil.log(this.getClass().getName(), "++++++++判断是否至少有两个文件导入++++++++" + fileSrc, 0);

				File file = new File(fileSrc);
				File[] array = file.listFiles();

				// CBPM_BIDOFFER_四川_20160915.dat
				for (int i = 0; i < array.length; i++) {
					if (array[i].getName().startsWith("CBPM_BIDOFFER_") && array[i].getName().contains(simpleMdate) && !array[i].getName().contains("_SESSION")  && array[i].getName().substring(array[i].getName().lastIndexOf(".") + 1, array[i].getName().length()).equals("dat")) {// 根据日期匹配文件
						size++;
					}
				}
				if (size >= 1) {
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
			if (size < 1) {
				throw new MsgException("无申报单!");
			}
			String result = declareService.readFile(fileSrc, simpleMdate);
			if (CommonUtil.ifEmpty(result)) {
				ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "导入各省调申报数据", "importFromInterface", mdate, result, true);
				
				j.setStatus(true);
				j.setMsg(result);
			} else {
				throw new MsgException("无申报数据!");
			}

			// System.out.println("++++++++++++++C程序======>plan_copy2.sh执行完毕++++++++++++");
			LoggerUtil.log(this.getClass().getName(), "++++++++++++++C程序======>Get_trans_data.sh执行完毕++++++++++++", 0);

		} catch (MsgException e) {
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "导入各省调申报数据", "importFromInterface", mdate, e.getMessage(), false);
			e.printStackTrace();
			j.setMsg(e.getMessage());
		} catch (Exception e) {
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "导入各省调申报数据", "importFromInterface", mdate, e.getMessage(), false);
			e.printStackTrace();
			j.setMsg("导入失败！" + e.getMessage());
		} finally {
			return j;
		}
	}
	
	/**
	 * 第一次调用获取联络线和计划与限额
	 * @description
	 * @author 大雄
	 * @date 2016年12月21日下午3:36:26
	 * @param time
	 * @param time1
	 * @return
	 */
	public JsonMsg getLineLimitAndPlan(boolean isAuto,String mdate){
		String MODEL_NAME_ =  isAuto?"信息公告-自动程序":"信息公告";
		//System.out.println(isAuto+"-----------"+MODEL_NAME_);
		JsonMsg j = new JsonMsg();
		try {
			LoggerUtil.log(this.getClass().getName(), "++++++++++++++开始调用C程序======>plan_copyto_cbpm.sh++++++++++++"+SystemConstUtil.getMatchPath() + File.separator + "bin" + File.separator + "plan_copyto_cbpm.sh " + mdate.replaceAll("-", ""), 0);
			
			if (SystemTools.isLinux()) {
				try {
					ProcessStatus ps = ProcessUtils.execute(20000,null, SystemConstUtil.getMatchPath() + File.separator  + "bin"+ File.separator+ "plan_copyto_cbpm.sh" , mdate.replaceAll("-", ""));
					
				} catch (IOException e) {
					e.printStackTrace();
					throw new MsgException("调用C程序获取失败！");
				}
			}
			LoggerUtil.log(this.getClass().getName(), "已经调用了C,现在开始判断是否调用成功!", 0);
			
			boolean flag = false;
			int index = 1;
			while (!flag) {
				if (index == 5) {
					break;
				}
				String state = matchService.selectProgramInfo("计划读取");
				LoggerUtil.log("计划读取", "state---"+state, 0);
				
				if (state!= null && state.equals("成功")) {
					flag = true;
					break;
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
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "获取计划与限额数据", "getLineLimitAndPlan",mdate, null, true);
			
			LoggerUtil.log(this.getClass().getName(), "++++++++++++++C程序======>plan_copyto_cbpm.sh执行完毕++++++++++++", 0);
			
		} catch (MsgException e) {
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "获取计划与限额数据", "getLineLimitAndPlan",mdate, e.getMessage(), false);
			e.printStackTrace();
			j.setMsg(e.getMessage());
		} catch (Exception e) {
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "获取计划与限额数据", "getLineLimitAndPlan",mdate, e.getMessage(), false);
			e.printStackTrace();
			j.setMsg("获取失败！" + e.getMessage());
		} finally {
			return j;
		}
		
	}
	
	/**
	 * 可用容量发布
	 * @description
	 * @author 大雄
	 * @date 2016年12月21日下午3:45:04
	 * @param time
	 * @param shortTime
	 * @return
	 */
	public JsonMsg exportToPlan(boolean isAuto,String time, String shortTime){
		String MODEL_NAME_= isAuto?"信息公告-自动程序":"信息公告";
		JsonMsg j = new JsonMsg();
		try {
			
			String mdate = time;
			String fileSrc = SystemConstUtil.getMatchPath() + File.separator + "trans_data" + File.separator + "send" + File.separator;

			LoggerUtil.log(this.getClass().getName(), "++++++++++++++开始生成本地文件+++++++++++++", 0);
			
			matchService.createLimitLineFile(fileSrc, shortTime,time);
			
			LoggerUtil.log(this.getClass().getName(), "++++++++++++++结束生成本地文件+++++++++++++", 0);
			
			LoggerUtil.log(this.getClass().getName(), "++++++++++++++开始调用C程序======>Send_trans_data.sh++++++++++++" + SystemConstUtil.getMatchPath() + File.separator + "bin" + File.separator + "Send_trans_data.sh", 0);
			Process process;
			if(SystemTools.isLinux()){
			try {
				ProcessStatus ps = ProcessUtils.execute(20000,null, SystemConstUtil.getMatchPath() + File.separator + "bin" + File.separator + "Send_trans_data.sh" );
				
			} catch (IOException e) {
				e.printStackTrace();
				throw new MsgException("调用C程序获取失败！");
			}
			}
			LoggerUtil.log(this.getClass().getName(), "++++++++脚本触发结束++++++++", 0);

			boolean flag = false;
			int index = 1;
			int size = 0;
			String simpleMdate = mdate.replaceAll("-", "");
			
			while (!flag) {
				size = 0;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (index == 5) {
					break;
				}
				
				File file = new File(fileSrc);
				File[] array = file.listFiles();

				for (int i = 0; i < array.length; i++) {
					if (array[i].getName().startsWith("CBPM_CORRIDOR_") && array[i].getName().contains(simpleMdate)) {// 根据日期匹配文件
						size = 1;
						break;
					}
				}
				LoggerUtil.log(this.getClass().getName(), "++++++++判断是否还有今天的文件没有发完++++++++" + size, 0);

				if (size == 0) {
					flag = true;
					break;
				}

				
				index++;
			}
			if(flag){
				ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "可用容量发布", "exportToPlan",time, null, true);
				//修改点击按钮的状态
				ServiceHelper.getBean(SystemServiceI.class).updateProcessConfig(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_KYRLFB,time);
				//可用容量发布成功后改变数据的状态
				lineLimitService.updateLineLimitByMdate(shortTime);
				
				j.setStatus(true);
				j.setMsg("可用容量发布成功！");
			}else{
				throw new MsgException("可用容量发布失败！");
			}
			
			LoggerUtil.log(this.getClass().getName(), "++++++++++++++C程序======>Send_trans_data.sh执行完毕++++++++++++", 0);
			
			
		} catch (MsgException e) {
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "可用容量发布", "exportToPlan",time+","+shortTime, e.getMessage(), false);
			//System.out.println("======="+e.getMessage());
			e.printStackTrace();
			j.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "可用容量发布", "exportToPlan",time+","+shortTime, e.getMessage(), false);
			j.setMsg("可用容量发布失败！" + e.getMessage());
		} finally {
			return j;
		}
	}
	
	/**
	 * 撮合出清
	 * @description
	 * @author 大雄
	 * @date 2016年12月21日下午3:55:42
	 * @param time
	 * @param time1
	 * @param dtype
	 * @return
	 */
	public Map<String,Object> match(boolean isAuto, String shortTime, String time, String dtype){
		String MODEL_NAME_ = isAuto?"撮合出清-自动程序":"撮合出清";
		Map<String,Object> j = new HashMap<String,Object>();
		String logFilePath = SystemConstUtil.getMatchPath() + File.separator + "log";
		String logFileName = logFilePath + File.separator + "match_" + time + ".log";
		
		try {
			// 修改点击按钮的状态集中竞价
			ServiceHelper.getBean(SystemServiceI.class).updateProcessConfig(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_JZJJ, time);
			List<LineLimitPo> limitList = matchService.getLineLimitList(shortTime);// 联络线限额
			if (limitList.size() > 0) {// 联络线限额存在则开始计算
				String funStr = "funSuccess";
				if (funStr.equals("funSuccess")) {
					String creatStr = matchService.createFile(SystemConstUtil.getMatchPath() + File.separator + "data", shortTime, dtype);// linux创建bat文件，为C提供数据文件
					if (creatStr.equals("creatSuccess")) {
						LoggerUtil.log(this.getClass().getName(), "++++++++++++++开始调用C程序++++++++++++", 0);
					
						File logFileDir = new File(logFilePath);
						if (!logFileDir.exists()) {
							logFileDir.mkdirs();
						}
						try {
							if (SystemTools.isLinux()) {
								LoggerUtil.log("cmd==" + SystemConstUtil.getMatchPath() + File.separator + "cbpm_control.sh " + SystemConstUtil.getMatchPath() + File.separator + "data" + File.separator + " " + shortTime, 0);
								ProcessStatus ps = ProcessUtils.execute(30000, logFileName, SystemConstUtil.getMatchPath() + File.separator + "bin" + File.separator + "cbpm_control.sh", SystemConstUtil.getMatchPath() + File.separator + "data" + File.separator, shortTime);

							} else {
								LoggerUtil.log("cmd==" + SystemConstUtil.getMatchPath() + File.separator + "cross_trade.exe " + SystemConstUtil.getMatchPath() + File.separator + "data" + File.separator + " " + shortTime, 0);

								ProcessStatus ps = ProcessUtils.execute(30000, logFileName, SystemConstUtil.getMatchPath() + File.separator + "cross_trade.exe", SystemConstUtil.getMatchPath() + File.separator + "data" + File.separator, shortTime);
								System.out.println("isSuccess-----------"+ps.isSuccess);
								if (!ps.isSuccess) {
									throw new MsgException("数据有误，无法出清!");
								}
							}
						} catch (MsgException e1) {
							throw e1;
						}catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						LoggerUtil.log(this.getClass().getName(), "++++++++++++++已开始执行C程序++++++++++++", 0);

						// System.out.println("=============已开始执行C程序==================");
						boolean flag = false;
						boolean errorFlag = false;
						int index = 0;
						while (!flag) {// 轮询生成装填
							if (index == 3) {
								LoggerUtil.log(this.getClass().getName(), "++++++++++++++后台C程序出错++++++++++++", 0);
								// System.out.println("===============后台C程序出错=================");
								flag = true;
								errorFlag = true;
							}
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if (SystemTools.isLinux()) {

								File logFile = new File(SystemConstUtil.getMatchPath() + File.separator + "data" + File.separator + "CBPM_" + shortTime + ".log");
								LoggerUtil.log(this.getClass().getName(), "logFile==========" + logFile.getAbsolutePath(), 0);

								if (logFile.exists()) {
									String resultStr = FileManager.readFile(logFile.getAbsolutePath());
									LoggerUtil.log(this.getClass().getName(), "logResult==========" + resultStr, 0);

									if (resultStr != null && resultStr.equals("Success")) {
										flag = true;
									} else if (resultStr != null && resultStr.equals("Failure")) {
										errorFlag = true;
										break;
									}
								} else {
									errorFlag = true;
								}
							} else {
								flag = true;
							}

							index++;
						}
						if (errorFlag) {
							throw new MsgException("解析结果数据文件错误");
						} else {
							LoggerUtil.log(this.getClass().getName(), "==============开始解析C程序计算结果文件+++++++++++++++", 0);
							// System.out.println("==============开始解析C程序计算结果文件+++++++++++++++");
							// linux读取C返回的数据文件
							String readStr = matchService.readFile(SystemConstUtil.getMatchPath() + File.separator + "data", shortTime);

							LoggerUtil.log(this.getClass().getName(), "==============" + readStr + "+++++++++++++++", 0);
							// System.out.println("=============="+readStr+"+++++++++++++++");
							if (readStr.equals("readSuccess")) {
								j.put("status",true);
								//String issueDataLog = FileManager.readRandomAccess(logFileName);
								//issueDataLog = issueDataLog.replaceAll("\r\n", "<br>");
								//j.setMsg(issueDataLog);
								// j.setMsg("撮合成功！");
								ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "撮合出清", "match", time, "撮合成功！", true);
								
							} else {
								throw new MsgException("解析结果数据文件错误");
							}
						}

					} else {
						// forward = "creatFail";// 导出数据文件错误
						throw new MsgException("导出数据文件错误");
					}
				} else {
					// forward = "funFail";// 计算正反向剩余力出现错误
					throw new MsgException("计算正反向剩余力出现错误");
				}
			} else {
				// forward = "lineLimitFail";// 计算正反向剩余力出现错误,联络线限额数据不存在
				throw new MsgException("联络线限额数据不存在");
			}
		} catch (MsgException e) {
			//System.out.println("-------------12312312---------------------------");
			e.printStackTrace();
			j.put("msg",e.getMessage());
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "撮合出清", "match", time, e.getMessage(), false);
			
		} catch (Exception e) {
			j.put("msg","撮合失败！");
			e.printStackTrace();
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "撮合出清", "match", time, e.getMessage(), false);
			
		} finally {
			String issueDataLog = FileManager.readRandomAccess(logFileName);
			issueDataLog = issueDataLog.replaceAll("\r\n", "<br>");
			j.put("log", issueDataLog);
			return j;
		}

	}
	
	/**
	 * 结果发布
	 * @description
	 * @author 大雄
	 * @date 2016年12月21日下午4:05:45
	 * @param time
	 * @return
	 */
	public JsonMsg updateIssue(boolean isAuto,HttpServletRequest request,String time){
		String MODEL_NAME_ = isAuto?"结果发布-自动程序":"结果发布";
		JsonMsg j = new JsonMsg();
		if(!isAuto){
			if(request.getSession().getAttribute("isSignResult")==null){
				j.setMsg("先签名后保存！");
				return j;
			}
			String str = (String)request.getSession().getAttribute("isSignResult");
			if(str.equals("false")){
				j.setMsg("先签名后保存！");
				return j;
			}
		}
		try {
			String time1 = time.replaceAll("-", "");

			String mdate = time;
			
			String fileSrc = SystemConstUtil.getMatchPath() + File.separator + "trans_data" + File.separator + "send" + File.separator;
			LoggerUtil.log(this.getClass().getName(), "++++++++++++++开始生成本地文件+++++++++++++", 0);
			

			matchService.createResultFile(fileSrc,time1,mdate);
			LoggerUtil.log(this.getClass().getName(), "++++++++++++++结束生成本地文件+++++++++++++", 0);

			LoggerUtil.log(this.getClass().getName(), "++++++++++++++开始调用C程序======>Send_trans_data.sh++++++++++++" + SystemConstUtil.getMatchPath() + File.separator + "bin" + File.separator + "Send_trans_data.sh", 0);
			Process process;
			if(SystemTools.isLinux()){
				try {
					ProcessStatus ps = ProcessUtils.execute(20000, null, SystemConstUtil.getMatchPath() + File.separator + "bin" + File.separator + "Send_trans_data.sh");

				} catch (IOException e) {
					e.printStackTrace();
					throw new MsgException("调用C程序获取失败！");
				}
			}
			
			LoggerUtil.log(this.getClass().getName(), "++++++++脚本触发结束++++++++", 0);

			boolean flag = false;
			int index = 1;
			int size = 0;
			String simpleMdate = mdate.replaceAll("-", "");
			
			while (!flag) {
				if (index == 5) {
					break;
				}
				LoggerUtil.log(this.getClass().getName(), "++++++++判断是否还有今天的文件没有发完++++++++" + fileSrc, 0);

				File file = new File(fileSrc);
				File[] array = file.listFiles();

				for (int i = 0; i < array.length; i++) {
					if (array[i].getName().contains(simpleMdate)) {// 根据日期匹配文件
						size = 1;
						break;
					}
				}
				if (size == 0) {
					flag = true;
					break;
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				index++;
			}

			LoggerUtil.log(this.getClass().getName(), "++++++++++++++C程序======>Send_trans_data.sh执行完毕++++++++++++", 0);

			LoggerUtil.log(this.getClass().getName(), "++++++++++++++开始调用C程序======>cbpm_copyto_plan.sh++++++++++++" + time, 0);

			if(SystemTools.isLinux()){
				try {
					ProcessStatus ps = ProcessUtils.execute(20000, null, SystemConstUtil.getMatchPath() + File.separator + "bin" + File.separator + "cbpm_copyto_plan.sh" , time.replaceAll("-", ""));

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new MsgException("调用C程序获取失败！");
				}
			}
			
			LoggerUtil.log(this.getClass().getName(), "++++++++++++++脚本调用已经触发======>cbpm_copyto_plan.sh++++++++++++", 0);

			flag = false;
			index = 1;
			while (!flag) {
				if (index == 5) {
					break;
				}
				
				String state = matchService.selectProgramInfo("日前跨区交易拷贝到计划");
				LoggerUtil.log("日前跨区交易拷贝到计划", "state---"+state, 0);
				if(CommonUtil.ifEmpty(state)){
					if(state.equals("成功")) {
						flag = true; 
						break;
					}
				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				index++;
			}
			if (flag) {
				ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "日前跨区交易拷贝到计划", "exportToPlan", time, "发布成功！", true);
				
				j.setStatus(true);
				j.setMsg("发布成功！");
			} else {
				if(SystemTools.isLinux()){

					throw new MsgException("发布失败！");
				}
			}
			LoggerUtil.log(this.getClass().getName(), ("++++++++++++++C程序======>cbpm_copyto_plan.sh执行完毕++++++++++++"), 0);

			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "导出出清信息给省调", "exportInterface", mdate, "导出出清信息给省调成功", true);

		    
		    //修改点击按钮的状态结果发布
			ServiceHelper.getBean(SystemServiceI.class).updateProcessConfig(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_JGFB,time);
			
			j.setStatus(true);
			j.setMsg("发布成功！");
			//issueService.issue(time1);
			issueService.updateIssueStatus(time1);
			if(!isAuto){
				declareService.clearLogForResult(SessionUtil.getUserPo().getId(), time,Enums_SystemConst.SIGN_TYPE_ISSUE.getValue());
				declareService.addLogForResult(SessionUtil.getUserPo().getId(), time,Enums_SystemConst.SIGN_TYPE_ISSUE.getValue());
				request.getSession().setAttribute("isSignResult", "false");
			}
		} catch (MsgException e) {
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "导出出清信息给省调", "exportInterface", time, e.getMessage(), false);
			e.printStackTrace();
			j.setMsg(e.getMessage());
		} catch (Exception e) {
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "导出出清信息给省调", "exportInterface", time, e.getMessage(), false);
			e.printStackTrace();
			j.setMsg("发布失败！");
		} finally {
			return j;
		}
	}

	/**
	 * 获取执行结果数据
	 * @description
	 * @author 大雄
	 * @date 2016年12月21日下午4:24:30
	 * @param isAuto
	 * @param mdate
	 * @return
	 */
	public JsonMsg getDealResultFromInterface(boolean isAuto, String mdate) {
		// TODO Auto-generated method stub
		String MODEL_NAME_ = isAuto?"执行结果-自动程序":"执行结果";
		JsonMsg j = new JsonMsg();
		
		try {
			
			LoggerUtil.log(this.getClass().getName(), "++++++++++++++开始调用C程序======>plan2_copyto_cbpm.sh++++++++++++" + SystemConstUtil.getMatchPath() + File.separator + "bin" + File.separator + "plan2_copyto_cbpm.sh "+mdate.replaceAll("-", ""), 0);
			
			if(SystemTools.isLinux()){
				try {
					ProcessStatus ps = ProcessUtils.execute(20000, null, SystemConstUtil.getMatchPath() + File.separator + "bin" + File.separator + "plan2_copyto_cbpm.sh",mdate.replaceAll("-", ""));

					
				} catch (IOException e) {
					e.printStackTrace();
					throw new MsgException("调用C程序获取失败！");
				}
			}
			
			
			
			LoggerUtil.log(this.getClass().getName(), "++++++++++++++脚本调用，已经触发开始判断是否导入成功======>plan2_copyto_cbpm.sh++++++++++++", 0);

			boolean flag = false;
			int index = 1;
			while (!flag) {
				if (index == 5) {
					break;
				}
			
				 String state = matchService.selectProgramInfo("日前计划二次导入"); 
				 LoggerUtil.log("日前计划二次导入", "state---"+state, 0);
				 if(CommonUtil.ifEmpty(state)){
					 if (state.equals("成功")) { 
						 flag = true;
						 break; 
					}
				 }
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				index++;
			}
			if (flag) {
				
				j.setStatus(true);
				j.setMsg("导入成功！");
			} else {
				if(SystemTools.isLinux()){

					throw new MsgException("导入失败！");
				}
			}
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "日前计划二次导入", "getPlan", mdate, "导入成功！", true);
			
			j.setStatus(true);
			j.setMsg("导入成功！");
		} catch (MsgException e) {
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "日前计划二次导入", "getPlan", mdate, e.getMessage(), false);
			e.printStackTrace();
			j.setMsg(e.getMessage());
		} catch (Exception e) {
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "日前计划二次导入", "getPlan", mdate, e.getMessage(), false);
			e.printStackTrace();
			j.setMsg("导入失败！");
		} finally {
			return j;
		}
	}
}
