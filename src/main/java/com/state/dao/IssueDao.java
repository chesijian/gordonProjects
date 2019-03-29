package com.state.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.AreaReportBean;
import com.state.po.DeclarePo;
import com.state.po.LineReportBean;
import com.state.po.ResultPo;
import com.state.po.SdTotalCostPo;
import com.state.po.SdcostResultPo;
import com.state.po.TransTielinePo;

public interface IssueDao {

	/**
	 * 插入结果数据  
	 * @param ResultPo
	 * @return 影响行数
	 */
	public long insertResult(ResultPo resultPo);
	
	/**
	 * 删除指定日期的所有结果数据  
	 * @param 日期
	 * @return 影响行数
	 */
	public long deleteResultByDate(@Param("mdate")String mdate);
	/**
	 * 调用c结束之后读取结果，先删除，dtype可以传入多个值加逗号隔开
	 * @description
	 * @author 大雄
	 * @date 2016年8月24日下午5:22:58
	 * @param mdate
	 * @param dtype
	 * @return
	 */
	public long deleteResultByDateAndDtype(@Param("mdate")String mdate,@Param("dtypes")String[] dtypes,@Param("drloes")String[] drloes);
	
	/**
	 * 按地区查询结果中的所有单号
	 * @param param
	 * @return 所有单子名称
	 */
	public List<DeclarePo> selectSheetOfResultByArea(@Param("area")String area,@Param("mdate")String mdate);
	
	/**
	 * 根据参数查询结果数据不包括96
	 * @param param
	 * @return
	 */
	public List<ResultPo> selectResultByParam(@Param("area")String area,@Param("mdate")String mdate,
			@Param("mname")String mname, @Param("dtype")String dtype);


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
	 * 按照日期更新成交结果发布标志位
	 * @param mdate
	 * @param dprint
	 */
	public void updatePrint(@Param("mdate")String mdate, @Param("dprint")String dprint);
	
	public void updateTransTieLine(@Param("mdate")String mdate, @Param("state")String state);

	/**
	 * 根据申报单号、类型查找发布单
	 * @param dsheet
	 * @return
	 */
	public ResultPo getResultBySheetId(@Param("dsheet")String dsheet, @Param("time")String time);
	/**
	 * 更新电量及价格
	 * @param dsheet
	 * @return
	 */
	public void updateResultPo(ResultPo resultPo);
	/**
	 * 更新时段
	 * @param dsheet
	 * @return
	 */
	public void updateResultPoSd(ResultPo resultPo);
	/**
	 * 山东申报单汇总		
	 * @param dsheet
	 * @return
	 */	
	public List<SdcostResultPo> getSdBillList(@Param("mdate")String time);
	
	/**
	 * 山东地区费用汇总				
	 * @param dsheet
	 * @return
	 */	
	public List<SdTotalCostPo> getSdAreaList(@Param("mdate")String time);
	
	/**
	 * 插入山东申报单汇总		
	 * @param dsheet
	 * @return
	 */	
	public void addSdcostResultPo(SdcostResultPo sdcostResultPo);
	
	/**
	 * 插入山东地区费用汇总				
	 * @param dsheet
	 * @return
	 */	
	public void addSdTotalCostPo(SdTotalCostPo sdTotalCostPo);
	/**
	 * 插入山东地区费用汇总				
	 * @param dsheet
	 * @return
	 */	
	public DeclarePo getResultIdBySheetName(@Param("sheetName")String sheetName);


	public void updateSdBill(@Param("time")String time, @Param("state")String state);


	public void updateSdCost(@Param("time")String time, @Param("state")String state);


	public void delSdcostResultPo(@Param("time")String time,@Param("position")String position);


	public void delSdTotalCostPo(@Param("time")String time,@Param("position")String position);

	public String selectProgramInfo(@Param("str")String str);

	public List<DeclarePo> getAreaTree(@Param("time")String time);

	public ResultPo getResultByArea(@Param("area")String area, @Param("time")String time);

	public DeclarePo getDeclarePoById(@Param("sheetId")String sheetId);

	public void updateDeclare(@Param("mdate")String time, @Param("clearstate")String clearstate);

	public void updateLineLimit(@Param("mdate")String time, @Param("state")String state);

	public List getAreaReportByMonth(@Param("startTime")String startTime,@Param("endTime")String endTime);

	public List<AreaReportBean> getAreaReportByYear(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("start")int start, @Param("end")int end);

	public List<LineReportBean> getLineReportByYear(@Param("startTime")String startTime, @Param("endTime")String endTime);

	public List<LineReportBean> getLineReportByMonth(@Param("startTime")String startTime, @Param("endTime")String endTime);

	/**
	 * 获取cbpm_result组装送点侧和受电侧数据树
	 * @description
	 * @author 大雄
	 * @date 2016年8月27日上午11:20:41
	 * @param mdate
	 * @return
	 */
	public List<ResultPo> getTreeForResult(@Param("area")String area,@Param("mdate")String mdate,@Param("status")Integer status,@Param("dtype")String dtype);
	
	/**
	 * 发布界面单击左侧树获取出清结果
	 * @description
	 * @author 大雄
	 * @date 2016年8月27日下午1:41:22
	 * @param id
	 * @return
	 */
	public ResultPo getResultById(@Param("id")String id);

	public Integer getAreaReportByMonthCount(@Param("startTime")String startTime, @Param("endTime")String endTime);

	public List<AreaReportBean> getAreaReportByMonthPage(@Param("startTime")String startTime,
			@Param("endTime")String endTime, @Param("start")int start, @Param("end")int end);

	public Integer getAreaReportByYearCount(@Param("startTime")String startTime, @Param("endTime")String endTime);

	/**
	 * 获取联络线月份分页数据
	 * @param startTime
	 * @param endTime
	 * @param start
	 * @param end
	 * @return
	 */
	public List getLineReportByMonthPage(@Param("startTime")String startTime, @Param("endTime")String endTime,
			@Param("start")int start, @Param("end")int end);

	/**
	 * 获取联络线年度分页数据
	 * @param startTime
	 * @param endTime
	 * @param start
	 * @param end
	 * @return
	 */
	public List getLineReportByYearPage(@Param("startTime")String startTime, @Param("endTime")String endTime,
			@Param("start")int start, @Param("end")int end);

	public Integer getLineReportByMonthCount(@Param("startTime")String startTime, @Param("endTime")String endTime);

	public Integer getLineReportByYearCount(@Param("startTime")String startTime, @Param("endTime")String endTime);
	/**
	 * 导出日报表，获取各个地区电力电价送电侧，受电侧电力或者电价统计总和
	 * @description
	 * @author 大雄
	 * @date 2016年9月13日上午9:34:40
	 * @param mdate
	 * @param area
	 * @param dtype
	 * @return
	 */
	public List<ResultPo> getSumResultByMdate(@Param("mdate")String mdate,@Param("area")String area,@Param("dtype")String dtype);
	/**
	 * 获取周报一段时间内分省交易结果数据
	 * @author 车斯剑
	 * @date 2016年10月15日上午9:51:06
	 * @param area
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<ResultPo> getResultByTime(@Param("startTime")String startTime, @Param("endTime")String endTime);

	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年10月15日上午11:13:15
	 * @param ids
	 * @return
	 */
	public ResultPo getResultByIds(@Param("ids")String[] ids);

	/**
	 * 获取地区交易结果
	 * @author 车斯剑
	 * @date 2016年12月14日下午5:29:32
	 * @param area
	 * @param time
	 * @return
	 */
	public List<ResultPo> getResultForAreaByTime(@Param("area")String area, @Param("time")String time);

	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年12月15日上午11:08:46
	 * @param area
	 * @param time
	 * @return
	 */
	public List<String> getAreaForTradeByTime(@Param("time")String time);

	/**
	 * 获取执行结果左边树
	 * @author 车斯剑
	 * @date 2016年12月15日下午3:57:52
	 * @param time
	 * @param area
	 * @param status
	 * @return
	 */
	public List<TransTielinePo> getExecuteTree(@Param("time")String time, @Param("area")String area,@Param("status")Integer status);

	/**
	 * 根据Id获取执行结果数据
	 * @author 车斯剑
	 * @date 2016年12月15日下午4:43:30
	 * @param id
	 * @return
	 */
	public TransTielinePo getExecuteResultById(@Param("id")String id);

	/**
	 * 获取执行结果数据
	 * @author 车斯剑
	 * @date 2016年12月15日下午5:48:16
	 * @param time
	 * @param area
	 * @param lines
	 * @return
	 */
	public List<TransTielinePo> getExecuteDataForArea(@Param("time")String time, @Param("area")String area, @Param("lines")List<String> lines);

	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年12月19日下午4:37:34
	 * @param mdate
	 * @return
	 */
	public List<ResultPo> getTotalResultByTime(@Param("mdate")String mdate);
}
