package com.state.dao;

import java.util.List;

import com.state.po.ResultPo;

public interface SurveyDaoI {
	/**
	 * 获取最大通道交易量
	 * @description
	 * @author 大雄
	 * @date 2016年12月18日下午3:44:10
	 * @param mdate
	 * @return
	 */
	public List<ResultPo> getSumList(String mdate);
	/**
	 * 获取最大交易量的时刻
	 * @description
	 * @author 大雄
	 * @date 2016年12月18日下午3:44:23
	 * @param mdate
	 * @return
	 */
	public List<ResultPo> getMaxInterval(String mdate);
}
