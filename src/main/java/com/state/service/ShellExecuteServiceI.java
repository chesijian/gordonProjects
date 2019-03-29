package com.state.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.state.po.extend.JsonMsg;

public interface ShellExecuteServiceI {
	/**
	 * 调用脚本导入送点侧报价数据
	 * @description
	 * @author 大雄
	 * @date 2016年12月21日下午3:32:43
	 * @param mdate
	 * @return
	 */
	public JsonMsg importDeclare(boolean isAuto,String mdate);
	/**
	 * 第一次调用获取联络线和计划与限额
	 * @description
	 * @author 大雄
	 * @date 2016年12月21日下午3:36:26
	 * @param mdate
	 * @return
	 */
	public JsonMsg getLineLimitAndPlan(boolean isAuto,String mdate);
	
	/**
	 * 可用容量发布
	 * @description
	 * @author 大雄
	 * @date 2016年12月21日下午3:45:04
	 * @param time
	 * @param shortTime
	 * @return
	 */
	public JsonMsg exportToPlan(boolean isAuto,String time, String shortTime);
	/**
	 * 撮合出清
	 * @description
	 * @author 大雄
	 * @date 2016年12月21日下午3:55:42
	 * @param shortTime
	 * @param time
	 * @param dtype
	 * @return
	 */
	public Map<String,Object> match(boolean isAuto, String shortTime, String time, String dtype);
	
	/**
	 * 结果发布
	 * @description
	 * @author 大雄
	 * @date 2016年12月21日下午4:05:45
	 * @param time
	 * @return
	 */
	public JsonMsg updateIssue(boolean isAuto,HttpServletRequest request,String time);
	/**
	 * 获取执行结果数据
	 * @description
	 * @author 大雄
	 * @date 2016年12月21日下午4:24:30
	 * @param isAuto
	 * @param time
	 * @return
	 */
	public JsonMsg getDealResultFromInterface(boolean isAuto,String time);
		
}
