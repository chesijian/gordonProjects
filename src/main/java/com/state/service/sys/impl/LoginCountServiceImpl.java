package com.state.service.sys.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.state.dao.sys.LoginCountDaoI;
import com.state.enums.Enums_SystemConst;
import com.state.po.sys.PfLoginCountPo;
import com.state.service.sys.LoginCountServiceI;
import com.state.util.CommonUtil;
import com.state.util.SessionUtil;
import com.state.util.properties.JPropertiesUtil;
/**
 * 
 * @description
 * @author 大雄
 * @date 2016年8月18日上午10:12:47
 */
@Service
@Transactional
public class LoginCountServiceImpl implements LoginCountServiceI {
	//超时时间
		public static final int LOGIN_OVER_TIME;
		public static final int LOGIN_OVER_COUNT;
		static{
			String loginOverTime = JPropertiesUtil.getSysConfigValue(Enums_SystemConst.LOGIN_OVER_TIME.getValue());
			if(CommonUtil.ifEmpty(loginOverTime)){
				LOGIN_OVER_TIME = Integer.parseInt(loginOverTime);
			}else{
				LOGIN_OVER_TIME = 10;
			}
			String loginOverCount = JPropertiesUtil.getSysConfigValue(Enums_SystemConst.LOGIN_OVER_COUNT.getValue());
			if(CommonUtil.ifEmpty(loginOverCount)){
				LOGIN_OVER_COUNT = Integer.parseInt(loginOverCount);
			}else{
				LOGIN_OVER_COUNT = 5;
			}
		}
		
	@Autowired
	private LoginCountDaoI dao;
	
	public boolean getIfOverCount(String user) {
		// TODO Auto-generated method stub
		PfLoginCountPo po = dao.selectOne(user);
		if(po == null){
			po = new PfLoginCountPo();
			po.setId(CommonUtil.getUUID());
			po.setCount(1);
			po.setDocCreated(new Date());
			po.setIp(SessionUtil.getClientIp());
			po.setUserId(user);;
			dao.insert(po);
		}else{
			int count = po.getCount();
			//System.out.println("====="+count);
			if(count >= LOGIN_OVER_COUNT){
				return false;
			}else{
				po.setCount(count+1);
				po.setIp(SessionUtil.getClientIp());
				po.setLastModified(new Date());
				dao.update(po);
			}
		}
		return true;
	}


	public boolean delete(String user) {
		// TODO Auto-generated method stub
		dao.delete(user);
		return false;
	}


	public boolean remove(boolean flag) {
		// TODO Auto-generated method stub
		if(flag){
			dao.deleteOverTime(null);
		}else{
			dao.deleteOverTime(LOGIN_OVER_TIME);
		}
		
		return false;
	}

}
