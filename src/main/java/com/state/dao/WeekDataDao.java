package com.state.dao;

import org.apache.ibatis.annotations.Param;

import com.state.po.WeekDataPo;

public interface WeekDataDao {
	
	public WeekDataPo getWeekDataByTimeAndWeek(@Param("startTime")String startTime, @Param("endTime")String endTime);

	public void insertWeekData(WeekDataPo po);

	public WeekDataPo getWeekDataByWeek(@Param("weekNum")int weekNum);

	public void updateWeekData(WeekDataPo po);

}
