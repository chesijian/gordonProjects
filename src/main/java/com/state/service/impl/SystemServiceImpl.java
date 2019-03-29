package com.state.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.helpers.OnlyOnceErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.state.dao.PfLoggerDaoI;
import com.state.dao.PfOnlineUserDaoI;
import com.state.dao.sys.SysConfigDaoI;
import com.state.enums.Enums_SessionType;
import com.state.enums.Enums_SystemConst;
import com.state.po.PfLoggerPo;
import com.state.po.sys.PfOnlineUserPo;
import com.state.po.sys.PfSysConfigPo;
import com.state.service.SystemServiceI;
import com.state.util.CommonUtil;
import com.state.util.DeclareUtil;
import com.state.util.SessionUtil;
import com.state.util.TimeUtil;
import com.state.util.sys.SystemConstUtil;
/**
 * 
 * @description
 * @author 大雄
 * @date 2016年8月17日上午11:35:55
 */
@Service
@Transactional
public class SystemServiceImpl implements SystemServiceI {

	@Autowired
	private PfOnlineUserDaoI onlineUserDao;
	@Autowired
	private PfLoggerDaoI logDao;
	@Autowired
	private SysConfigDaoI sysConfigDao;
	
	
	public List<PfOnlineUserPo> getOnlineUser(String startTime, String endTime, int start, int end) {
		// TODO Auto-generated method stub
		//System.out.println(CommonUtil.objToJson(onlineUserDao.selectOnLineUser(startTime, endTime, start, end)));
		return onlineUserDao.selectOnLineUser(startTime, endTime, start, end);
	}
	
	public long getOnlineUserCount(String startTime, String endTime) {
		//System.out.println(startTime +"==="+onlineUserDao.getOnlineUserCount(startTime, endTime));
		return onlineUserDao.getOnlineUserCount(startTime, endTime);
	}

	public long getLogCount(String startTime, String endTime){
		return logDao.getLogCount(startTime, endTime);
		
	}

	public List<PfLoggerPo> getLog(String startTime, String endTime, int start, int end){
		return logDao.selectLog(startTime, endTime, start, end);
		
	}
	/**
	 * 获取配置常量
	 * @description
	 * @author 大雄
	 * @date 2016年9月19日下午2:24:32
	 * @param key
	 * @return
	 */
	public PfSysConfigPo getSysConfig(String key){
		return sysConfigDao.selectOne(key);
	}
	/**
	 * 获取配置常量
	 * @description
	 * @author 大雄
	 * @date 2016年9月19日下午2:24:32
	 * @param key
	 * @return
	 */
	public PfSysConfigPo selectOneByType(String key, String mdate){
		return sysConfigDao.selectOneByType(key, mdate);
	}
	
	/**
	 * 定时判断流程按钮状态
	 * @description
	 * @author 大雄
	 * @throws ParseException 
	 * @date 2016年10月8日下午9:21:05
	 */
	public void updateProcessBtnStatus() throws ParseException{
		Date date = TimeUtil.getNextDay(new Date(System.currentTimeMillis()));
		String tommowTime = new SimpleDateFormat("yyyy-MM-dd").format(date);
		PfSysConfigPo processTime = sysConfigDao.selectOneByType(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_TIME,null);
		PfSysConfigPo processConfig = sysConfigDao.selectOneByType(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG,tommowTime);
		SystemConstUtil.updateProcessBtnStatus(processTime,processConfig,tommowTime);
	}
	/**
	 * 定时判断流程按钮状态
	 * @description
	 * @author 大雄
	 * @throws ParseException 
	 * @date 2016年10月8日下午9:21:05
	 */
	public void updateProcessBtnStatus(String mdate) throws ParseException{
		PfSysConfigPo processTime = sysConfigDao.selectOneByType(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_TIME,null);
		PfSysConfigPo processConfig = sysConfigDao.selectOneByType(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG,mdate);
		SystemConstUtil.updateProcessBtnStatus(processTime,processConfig,mdate);
	}
	/**
	 * 点击可用容量发布或者竞价出清或者结果发布的时候修改processConfig
	 * @description
	 * @author 大雄
	 * @date 2016年10月17日上午10:54:42
	 * @param key
	 * @throws ParseException
	 */
	public void updateProcessConfig(String key,String mdate) throws ParseException {
		//获取当前交易日期
		//String mdate = DeclareUtil.getMdate();
		PfSysConfigPo processConfig = sysConfigDao.selectOneByType(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG,mdate);
		//System.out.println("------"+mdate);
		Map<String,Integer> map = null;
		if(processConfig == null){
			processConfig = new PfSysConfigPo();
			map = new HashMap<String, Integer>();
			map.put(key, 1);
			processConfig.setId(CommonUtil.getUUID());
			processConfig.setAddName(SessionUtil.getAddName());
			processConfig.setAddNameCn(SessionUtil.getAddNameCn());
			processConfig.setDocCreated(new Date());
			processConfig.setType(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG);
			processConfig.setKey(mdate);
			processConfig.setValue(CommonUtil.objToJson(map));
			sysConfigDao.insertSysConfig(processConfig);
		}else{
			if(mdate.equals(processConfig.getKey())){
				String config = processConfig.getValue();
				map = CommonUtil.jsonToMapInteger(config);
			}else{
				processConfig.setKey(mdate);
				map = new HashMap<String, Integer>();
			}
			map.put(key, 1);
			processConfig.setValue(CommonUtil.objToJson(map));
			sysConfigDao.updateSysConfig(processConfig);
			
		}
		updateProcessBtnStatus();
	}
	
}
