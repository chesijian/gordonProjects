package com.state.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.state.dao.IDeclareDao;
import com.state.dao.PfLoggerDaoI;
import com.state.po.PfLoggerPo;
import com.state.service.PfLoggerServiceI;
import com.state.util.CommonUtil;
import com.state.util.SessionUtil;

@Service
@Transactional
public class PfLoggerServiceImpl implements PfLoggerServiceI {

	@Autowired
	private PfLoggerDaoI dao;
	
	
	public void insert(PfLoggerPo po) {
		// TODO Auto-generated method stub
		po.setId(CommonUtil.getUUID());
		if(po.getAddName() == null){
			po.setAddName(SessionUtil.getAddName());
		}
		if(po.getAddNameCn() == null){
			po.setAddNameCn(SessionUtil.getAddNameCn());
		}
		po.setIp(SessionUtil.getClientIp());
		po.setDocCreated(new Date());
		dao.insert(po);
	}
	
	public void log(String className,String modelName,String operNameCn,String operName,String operParams,String resultMsg,boolean isSuccess){
		PfLoggerPo po = new PfLoggerPo();
		po.setClassName(className);
		po.setModelName(modelName);
		po.setOperName(operName);
		po.setOperNameCn(operNameCn);
		po.setOperParams(operParams);
		po.setOperResult(isSuccess?"success":"failure");
		po.setResultMsg(resultMsg);
		po.setId(CommonUtil.getUUID());
		if(po.getAddName() == null){
			if(CommonUtil.ifEmpty(SessionUtil.getAddName())){

				po.setAddName(SessionUtil.getAddName());
			}else{
				po.setAddName("server");
			}
		}
		if(po.getAddNameCn() == null){
			
			if(CommonUtil.ifEmpty(SessionUtil.getAddNameCn())){

				po.setAddNameCn(SessionUtil.getAddNameCn());
			}else{
				po.setAddNameCn("服务器");
			}
		}
		po.setIp(SessionUtil.getClientIp());
		po.setDocCreated(new Date());
		//System.out.println("======"+CommonUtil.objToJson(po));
		dao.insert(po);
	}
	

}
