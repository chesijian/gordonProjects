package com.state.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.LineDefinePo;
import com.state.po.LineLimitPo;
import com.state.po.PathResultPo;

public interface LineLimitDao {

	/**
	 * 增加联络线限额
	 * @param lineLimitPo
	 */
	public void insertLineLimit(LineLimitPo lineLimitPo);

	/**
	 * 更新联络线限额
	 * @param lineLimitPo 
	 */
	public void updateLineLimit(LineLimitPo lineLimitPo);
	
	/**
	 * 根据参数查找联络线限额
	 * @param mcorhr
	 * @param mdate
	 * @param dtype
	 * @return 联络线限额
	 */
	public LineLimitPo getLineLimit(@Param("mcorhr")String mcorhr,@Param("time")String time,@Param("dtype")String dtype,@Param("state")String state);
	
	/**
	 * 根据参数查找联络线限额列表
	 * @param mcorhr
	 * @param mdate
	 * @return 联络线限额列表
	 */
	public List<LineLimitPo> selectLineLimitList(@Param("mcorhr")String mcorhr,@Param("mdate")String mdate);
	/**
	 * 查找联络线限额列表
	 * @return 联络线限额列表
	 */
	public List<LineLimitPo> getLineLimitList(@Param("mdate")String mdate);
	/**
	 * 根据时间和类型删除联络线限额
	 * @return 
	 */
	public void delLineLimitPo(@Param("mdate")String time, @Param("dtype")String dtype);
	
	/**
	 * 导出日报表统计各个联络线的跨区通道运行情况，电力统计
	 * @description
	 * @author 大雄
	 * @date 2016年9月12日下午5:14:09
	 * @param mdate
	 * @return
	 */
	public List<LineLimitPo> getSumResultByMdate(String mdate);
	
	/**
	 * 根据参数查找联络线电量
	 * @param mcorhr
	 * @param mdate
	 * @param dtype
	 * @return 联络线限额
	 */
	public LineLimitPo getDLTJPo(@Param("mcorhr")String mcorhr,@Param("time")String time);

	public String getConfigStr();
	/**
	 * 根据当前日期更新联络线数据的状态
	 * @param mdate
	 */
	public void updateLineLimitByMdate(@Param("mdate")String mdate);
}
