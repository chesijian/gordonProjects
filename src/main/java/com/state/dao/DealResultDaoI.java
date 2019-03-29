package com.state.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.ResultPo;

public interface DealResultDaoI {
	
	/**
	 * 插入结果数据  
	 * @param ResultPo
	 * @return 影响行数
	 */
	public long insertResult(ResultPo resultPo);
	
	public void updateResult(ResultPo result);

	/**
	 * 根据参数查询结果数据包括96
	 * @description
	 * @author 大雄
	 * @date 2016年9月25日下午9:05:47
	 * @param area
	 * @param mdate
	 * @param status
	 * @return
	 */
	public List<ResultPo> selectResultListByParam(@Param("area")String area,@Param("mdate")String mdate,
			@Param("status")String status);
	/**
	 * 删除指定日期的所有结果数据  
	 * @param 日期
	 * @return 影响行数
	 */
	public long deleteResultByDate(@Param("mdate")String mdate);

	/**
	 * 按照日期更新成交结果发布标志位
	 * @param mdate
	 * @param dprint
	 */
	public void updatePrint(@Param("mdate")String mdate, @Param("dprint")String dprint);

	/**
	 * 获取cbpm_result组装送点侧和受电侧数据树
	 * @description
	 * @author 大雄
	 * @date 2016年8月27日上午11:20:41
	 * @param mdate
	 * @return
	 */
	public List<ResultPo> getTreeForResult(@Param("area")String area,@Param("mdate")String mdate,@Param("status")Integer status);

	public ResultPo getResultById(@Param("id")String id);
	
	
	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年10月18日下午3:01:17
	 * @param area
	 * @param startTime
	 * @param endTime
	 * @param status
	 * @return
	 */
	public List<ResultPo> getResultByTime(@Param("area")String area, @Param("startTime")String startTime,
			@Param("endTime")String endTime, @Param("status")Integer status);

}
