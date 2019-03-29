package com.state.service;

import java.text.ParseException;
import java.util.List;

import com.state.po.PfLoggerPo;
import com.state.po.sys.PfOnlineUserPo;
import com.state.po.sys.PfSysConfigPo;

public interface SystemServiceI {

	public List<PfOnlineUserPo>  getOnlineUser(String startTime,String endTime,int start,int end);

	public long getOnlineUserCount(String startTime, String endTime);

	public long getLogCount(String startTime, String endTime);

	public List<PfLoggerPo> getLog(String startTime, String endTime , int start, int end);
	
	/**
	 * 获取配置常量
	 * @description
	 * @author 大雄
	 * @date 2016年9月19日下午2:24:32
	 * @param key
	 * @return
	 */
	public PfSysConfigPo getSysConfig(String key);
	/**
	 * 获取配置常量
	 * @description
	 * @author 大雄
	 * @date 2016年9月19日下午2:24:32
	 * @param key
	 * @return
	 */
	public PfSysConfigPo selectOneByType(String key, String mdate);
	/**
	 * 定时判断流程按钮状态
	 * @description
	 * @author 大雄
	 * @date 2016年10月8日下午9:21:05
	 */
	public void updateProcessBtnStatus() throws ParseException;
	/**
	 * 定时判断流程按钮状态
	 * @description
	 * @author 大雄
	 * @date 2016年10月8日下午9:21:05
	 */
	public void updateProcessBtnStatus(String mdate) throws ParseException;
	/**
	 * 点击可用容量发布或者竞价出清或者结果发布的时候修改processConfig
	 * @description
	 * @author 大雄
	 * @date 2016年10月17日上午10:54:42
	 * @param key
	 * @throws ParseException
	 */
	public void updateProcessConfig(String key,String mdate)  throws ParseException;
	
}
