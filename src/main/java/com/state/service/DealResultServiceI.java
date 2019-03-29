package com.state.service;

import java.util.List;

import com.state.po.ResultPo;
import com.state.po.TreeBean;

public interface DealResultServiceI {
	/**
	 * 发布页面左边获取发布信息树结构
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年8月27日上午11:24:49
	 * @param mdate
	 * @return
	 */
	public List<TreeBean> getTree(String area, String mdate, Integer status);

	/**
	 * 发布界面单击左侧树获取出清结果
	 * @description
	 * @author 大雄
	 * @date 2016年8月27日下午1:41:22
	 * @param id
	 * @return
	 */
	public ResultPo getResultById(String id);
	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年10月12日上午7:44:23
	 * @param string
	 * @param time
	 * @return
	 */
	public List<ResultPo> getTreeForResult(String string, String time);
	public List<ResultPo> getResultByTime(String area, String startTime,String endTime) ;
}
