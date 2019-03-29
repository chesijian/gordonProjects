package com.state.service;

import com.state.po.PfLoggerPo;
/**
 * 
 * @description
 * @author 大雄
 * @date 2016年9月9日下午4:52:45
 */
public interface PfLoggerServiceI {
	
	/**
	 * aop切面插入日志
	 * @description
	 * @author 大雄
	 * @date 2016年9月9日下午4:52:58
	 * @param po
	 */
	public void insert(PfLoggerPo po);
	/**
	 * 手动插入日志
	 * @description
	 * @author 大雄
	 * @date 2016年9月9日下午4:53:10
	 * @param className 操作的类
	 * @param modelName 操作的模块
	 * @param operNameCn 操作的方法
	 * @param operName 类的方法
	 * @param operParams 参数
	 * @param resultMsg 执行结果
	 * @param isSuccess 是否成功
	 */
	public void log(String className,String modelName,String operNameCn,String operName,String operParams,String resultMsg,boolean isSuccess);
		
	
}
