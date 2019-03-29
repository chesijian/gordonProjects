package com.state.service;

import java.util.List;
import java.util.Map;

import com.state.po.LineLimitPo;
import com.state.po.TransTielinePo;
import com.state.po.TreeBean;



/**
 * 联络线service
 * @author 帅
 *
 */
public interface LineLimitService {
	
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
	public LineLimitPo getLineLimit(String mcorhr,
			String mdate,String dtype);
	
	/**
	 * 根据参数查找联络线限额列表
	 * @param mcorhr
	 * @param dtype
	 * @return 联络线限额列表
	 */
	public LineLimitPo selectLineLimitList(String mcorhr,String time,String dtype,String state);

	/**
	 * 获取通道交易结果树
	 * @author 车斯剑
	 * @date 2016年11月16日下午3:55:12
	 * @param area
	 * @param time
	 * @param status
	 * @return
	 */
	public List<TreeBean> getTree(String area, String time, Integer status);

	
	 /**
     * 根据参数获取联络线电量
     * @param mcorhr
     * @param time
     * @return
     */
	public LineLimitPo getDLTJPo(String mcorhr,String time);

	public String getConfigStr();
	/**
	 * 导出周报组织执行电量数据
	 * @author 车斯剑
	 * @date 2016年10月19日下午6:05:26
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String,Map<String,Double>>> getCarryData(String startTime,String endTime);
	
	/**
	 * 获取LineLimitPo 96值总和
	 * @author 车斯剑
	 * @date 2016年10月11日上午10:19:13
	 * @param data
	 * @return
	 */
	public double getSum(LineLimitPo  data);
	/**
	 * 获取单个可用容量、日前预计划值
	 * @author 车斯剑
	 * @date 2016年10月12日上午9:59:46
	 * @param mcorhr
	 * @param dtype
	 * @param time
	 * @param state
	 * @return
	 */
	public Map<String,LineLimitPo> getSpaceData(String mcorhr, String dtype, String time, String state);
	/**
	 * 获取图片数据,可用容量，平均利用率
	 * @author 车斯剑
	 * @date 2016年10月20日下午1:37:36
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Map<String,Object> getPictureAndAblePowerData(String startTime,String endTime);

	/**
	 * 根据Id查找通道结果
	 * @author 车斯剑
	 * @date 2016年11月16日下午8:35:22
	 * @param id
	 * @return
	 */
	public TransTielinePo getTransTielineById(String id);
	/**
	 * 根据当前日期更新联络线数据的状态
	 * @param mdate
	 */
	public void updateLineLimitByMdate(String mdate);
}
