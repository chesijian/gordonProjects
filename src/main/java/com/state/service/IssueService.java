package com.state.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.state.exception.MsgException;
import com.state.po.AreaReportBean;
import com.state.po.DeclarePo;
import com.state.po.LineReportBean;
import com.state.po.ResultPo;
import com.state.po.SdTotalCostPo;
import com.state.po.SdcostResultPo;
import com.state.po.TransTielinePo;
import com.state.po.TreeBean;



/**
 * 发布service
 * @author 帅
 *
 */
public interface IssueService {
	
	/**
	 * 根据地区获取发布单列表
	 * @param area
	 * @return
	 */
	public List<DeclarePo> getResultNameList(String area,String time);

	/**
	 * 根据申报单号、类型查找发布单
	 * @param dsheet
	 * @param dtype 
	 * @return
	 */
	public ResultPo getResultBySheetId(String dsheet, String dtype);
	
	
	/**
     * 山东单子汇总
     * @param time
     * @return
     */
	public List<SdcostResultPo> geSdBillMeg(String time);
	
    /**
     * 山东费用汇总
     * @param time
     * @return
     */
	public List<SdTotalCostPo> getSdAreaMeg(String time);
	/**
	 * 审核更新状态
	 * @return
	 */
	public void issue(String time);
	/**
	 * 根据时间获取树结构
	 * @param request
	 * @param response
	 * @return
	 */
	public List<DeclarePo> getAreaTree(String time);
	/**
	 * 根据地区获取发布单数据汇总
	 * @param request
	 * @param response
	 * @return
	 */
	public ResultPo getResultByArea(String area, String time);
	/**
	 * 根据结果集中的ID获取单子
	 * @param request
	 * @param response
	 * @return
	 */
	public DeclarePo getDeclarePoById(String sheetId);
	/**
	 * 根据月度获取地区统计信息
	 * @param request
	 * @param response
	 * @return
	 */
	public List<AreaReportBean> getAreaReportByMonth(String startTime,String endTime);
	/**
	 * 根据年度获取地区统计信息
	 * @param request
	 * @param response
	 * @return
	 */
	public List<AreaReportBean> getAreaReportByYear(String startTime, String endTime,int start, int end);
	/**
	 * 根据年度获取联络线统计信息
	 * @param request
	 * @param response
	 * @return
	 */
	public List<LineReportBean> getLineReportByYear(String startTime, String endTime);
	/**
	 * 根据月度获取联络线统计信息
	 * @param request
	 * @param response
	 * @return
	 */
	public List<LineReportBean> getLineReportByMonth(String startTime,
			String endTime);

	/**
	 * 发布页面左边获取树结构
	 * @description
	 * @author 大雄
	 * @date 2016年8月27日上午11:24:49
	 * @param mdate
	 * @param status 如果是1表示发布之后的，否则未发布
	 * @return
	 */
	public List<TreeBean> getTree(String area,String mdate,Integer status);

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
	 * 后来添加方法
	 * @author 车斯剑
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Integer getAreaReportByMonthCount(String startTime, String endTime);
	public List<AreaReportBean> getAreaReportByMonthPage(String startTime,String endTime,int start, int end);

	public Integer getAreaReportByYearCount(String startTime, String endTime);

	/**
	 * 
	 * @author 获取联络线记录月份分页数据
	 * @param startTime
	 * @param endTime
	 * @param pageIndex
	 * @param i
	 * @return
	 */
	public List getLineReportByMonthPage(String startTime, String endTime,
			int pageIndex, int i);
	/**
	 * 
	 * @author 获取联络线记录年度分页数据
	 * @param startTime
	 * @param endTime
	 * @param pageIndex
	 * @param i
	 * @return
	 */
	public List getLineReportByYearPage(String startTime, String endTime,
			int pageIndex, int i);

	/**
	 * 获取联络线记录月份总记录数
	 * @author 车斯剑
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Integer getLineReportByMonthCount(String startTime, String endTime);

	public Integer getLineReportByYearCount(String startTime, String endTime);
	/**
	 * 导出日报时获取竞价申报单数据
	 * @description
	 * @author 大雄
	 * @date 2016年9月12日下午4:16:45
	 * @param mdate
	 * @return
	 */
	public Map<String, Map<String, String>> getExcelDeclareData(String mdate);
	/**
	 * 导出日报时获取成交情况数据
	 * @description
	 * @author 大雄
	 * @date 2016年9月12日下午4:16:45
	 * @param mdate
	 * @return
	 */
	public Map<String, Map<String, Double>> getExcelPathResultData(String mdate);
	
	/**
	 * 导出日报时获取通道运行情况数据
	 * @description
	 * @author 大雄
	 * @date 2016年9月12日下午4:16:45
	 * @param mdate
	 * @return
	 */
	public Map<String, Map<String, Double>> getExcelLineLimitResultData(String mdate);
	
	/**
	 * 导出日报时获取各个地区成交总电量
	 * @description
	 * @author 大雄
	 * @date 2016年9月13日上午9:41:37
	 * @param mdate
	 * @return
	 */
	public Map<String,  Double> getExcelResultData(String mdate);
	
	
	public List<ResultPo> getTreeForResult(String area, String mdate);
	
	
	/**
	 * 通过日期获取所有当前时间的交易结果字符串，导出到word
	 * @description
	 * @author 大雄
	 * @date 2016年9月25日下午8:34:45
	 * @param time
	 * @return
	 */
	public Map<String,StringBuilder> getResultMap(String time);

	/**
	 * 更新成交记录
	 * @author 车斯剑
	 * @param result
	 */
	public void updateResult(ResultPo result);

	/**
	 * 根据日期导出日报，如有是国调，则area is null，导出所有省，每个省一份word打包成zip
	 * 如果是省调则只导出word
	 * @description
	 * @author 大雄
	 * @date 2016年9月26日下午2:12:24
	 * @param area
	 * @param mdate
	 * @param dateStr
	 * @param tempPath
	 * @return
	 */
	public String exportDailyFile(String area,String mdate,String dateStr,String tempPath) throws IOException,MsgException;
	

	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年10月15日上午10:14:40
	 * @param list
	 * @return
	 */
	public double getResultByIds(List<String> list);

	/**
	 * 获取日报左边树
	 * @author 车斯剑
	 * @date 2016年10月17日下午2:02:00
	 * @param area
	 * @param time
	 * @param status
	 * @return
	 */
	public List<TreeBean> getDailyTree(String area, String time, Integer status);

	/**
	 * 导出周报组装获取成交数据
	 * @author 车斯剑
	 * @date 2016年10月19日上午9:31:13
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Map<String, Object> getDealDatasByMdateInterval(String startTime,
			String endTime);
	
	/**
	 * 获取日报数据
	 * @author 车斯剑
	 * @date 2016年10月23日上午10:27:10
	 * @param area
	 * @param mdate
	 * @return
	 */
	public Map<String, List<ResultPo>> getDailyResults(String area, String mdate,Integer status);
	/**
	 * 发布成功后更新数据状态
	 * @param mdate
	 */
	public void updateIssueStatus(String mdate);

	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年12月14日上午9:31:46
	 * @param area
	 * @param time
	 * @return
	 */
	public List<ResultPo> getResultForAreaByTime(String area, String time);

	/**
	 * 获取交易单地区
	 * @author 车斯剑
	 * @date 2016年12月15日上午11:05:29
	 * @param area
	 * @param time
	 * @return
	 */
	public List<String> getAreaForTradeByTime(String time);

	/**
	 * 获取执行结果左边树
	 * @author 车斯剑
	 * @date 2016年12月15日下午3:54:37
	 * @param area
	 * @param time
	 * @param status
	 * @return
	 */
	public List<TreeBean> getExecuteTree(String area, String time,Integer status);

	/**
	 * 根据Id获取执行结果数据
	 * @author 车斯剑
	 * @date 2016年12月15日下午4:41:39
	 * @param id
	 * @return
	 */
	public TransTielinePo getExecuteResultById(String id);

	/**
	 * 获取地区执行结果数据
	 * @author 车斯剑
	 * @date 2016年12月15日下午5:45:34
	 * @param time
	 * @param area
	 * @param lines
	 * @return
	 */
	public List<TransTielinePo> getExecuteDataForArea(String time, String area,List<String> lines);
}