package com.state.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.PfLoggerPo;
/**
 * 
 * @description
 * @author 大雄
 * @date 2016年8月17日下午2:32:24
 */
public interface PfLoggerDaoI {

	public void insert(PfLoggerPo po) ;

	
	public List<PfLoggerPo> selectLog(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("start")int start,@Param("end")int end);

	public long getLogCount(@Param("startTime")String startTime, @Param("endTime")String endTime);
	
}
