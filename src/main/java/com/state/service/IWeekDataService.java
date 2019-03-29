package com.state.service;

import com.state.po.WeekDataPo;

public interface IWeekDataService {
	
	public WeekDataPo getWeekDataByTimeAndWeek(String startTime,String endTime);
	
	public void insertWeekData(WeekDataPo po);

	public void updateWeekData(WeekDataPo po);
	
	public WeekDataPo getWeekData(String startTime,String endTime);
}
