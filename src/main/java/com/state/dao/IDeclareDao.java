package com.state.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.AreaPo;
import com.state.po.DeclarePo;
import com.state.po.UserPo;

public interface IDeclareDao {

	/**
	 * 根据ID删除申报主表
	 * @param id
	 */
	public void deleteDeclare(Long id);

	/**
	 * 插入申报主表  
	 * @param declarePo
	 * @return 影响行数，取ID使用declarePo.getId()方法
	 */
	public long insertDeclare(DeclarePo declarePo);
	
	/**
	 * 根据ID查找申报主表是否存在
	 * @param id
	 * @return
	 */
	public int countDeclareById(Long id);
	
	/**
	 * 根据单主键获取单子的修改人记录
	 * @description
	 * @author 大雄
	 * @date 2016年9月26日下午6:08:51
	 * @param sheetId
	 * @return
	 */
	public List<UserPo> getUserGraPhPoListBySheetUid(String sheetId);
	
	/**
	 * 根据类型和日期获取的签名记录
	 * @description
	 * @author 大雄
	 * @date 2016年9月26日下午6:08:51
	 * @param sheetId
	 * @return
	 */
	public List<UserPo> getUserGraPhPoListByMdateAndType(@Param("mdate")String mdate,@Param("type")String type);
	
	
	/**
	 * 更新申报主表
	 * @param id
	 * @return
	 */
	public void updateDeclare (DeclarePo declarePo);
	
	/**
	 * 根据参数查询申报主表
	 * @param param
	 * @return
	 */
	public List<DeclarePo> selectDeclareByParam(@Param("area")String area,@Param("mdate")String mdate
			,@Param("mname")String mname);
	/**
	 * 统计买卖单数
	 * @param param
	 * @return
	 */
	public List<String> getSheetDeclNum(@Param("time")String time);
	
	/**
	 * 统计某个日期买方和卖方地区数量
	 * @description
	 * @author 大雄
	 * @date 2016年8月24日上午10:19:37
	 * @param time
	 * @return
	 */
	public List<String> getSheetDeclAreaNum(@Param("time")String time);
	
	
	/**
	 * 统计申报单
	 * @param status 
	 * @param param
	 * @return
	 */
	public List<DeclarePo> queryForList(@Param("time")String time,@Param("area")String area ,@Param("status")Integer status);
	
	
	/**
	 * 根据申报单名称查询ID
	 * @param param
	 * @return
	 */
	public String getSheetIdByName(@Param("name")String string);

	public List<DeclarePo> queryForListByMdate(@Param("time")String time, @Param("area")String area);
	
	/**
	 * 优化计算时获取所有单子及电力电价曲线
	 * 获取加密CBPM_SHEET_EXTRA
	 * @description
	 * @author 大雄
	 * @date 2016年8月24日上午11:43:34
	 * @param time
	 * @return
	 */
	public List<DeclarePo> getListAndExtraByMdate(@Param("time")String time,@Param("area")String area);
	/**
	 * 优化计算时获取所有单子及电力电价曲线
	 * type==0表示获取CBPM_SHEET_EXTRA,type==1表示获取加密
	 * @description
	 * @author 大雄
	 * @date 2016年8月24日上午11:43:34
	 * @param time
	 * @return
	 */
	public List<DeclarePo> getListAndDataByMdate(@Param("time")String time,@Param("area")String area,@Param("status")Integer status);
	
	
	/**
	 * 获取某一天的申请单的地区
	 * @description
	 * @author 大雄
	 * @date 2016年8月24日上午10:38:55
	 * @param time
	 * @return
	 */
	public List<DeclarePo> getAreaListByMdate(@Param("time")String time);
	
	/**
	 * 获取最新单号
	 * @param sn
	 * @return
	 */
	public String getLastSn(@Param("date")String date,@Param("drloe")String drloe);
	
	/**
	 * 获取是否存在单号
	 * @param sn
	 * @return
	 */
	public int getIsExtisSn(@Param("sn")String sn,@Param("drloe")String drloe);

	public DeclarePo getDeclares(@Param("id")long id);
	
	/**
	 * 根据时间排序获取某个区最新的单子
	 * @description
	 * @author 大雄
	 * @date 2016年8月29日下午3:19:59
	 * @param area
	 * @param mdate
	 * @return
	 */
	public DeclarePo getLastDeclare(@Param("area") String area,@Param("mdate") String mdate);

	public void updateDeclareStatus(@Param("ids")long[]ids,@Param("status")int status,@Param("submitId")String submitId,@Param("submitTime")Date submitTime);

    public void addLog(@Param("sheetId")String sheetId,@Param("mdate")String mdate,@Param("userId")String userId,@Param("type")String type);
    
    public void clearLogForDecl(@Param("sheetId")String sheetId,@Param("mdate")String mdate,@Param("userId")String userId,@Param("type")String type);
    
    public void addLogForResult(@Param("userId")String userId,@Param("mdate")String mdate,@Param("type")String type);
    
    public void clearLogForResult(@Param("userId")String userId,@Param("mdate")String mdate,@Param("type")String type);
    
	public List<String> getUserIdList(@Param("sheetId")String sheetId);
	
	public void deleteLogBySheet(@Param("sheetId")long sheetId);

	/**
	 * 根据日期获取各个区域的单子数量
	 * @description
	 * @author 大雄
	 * @date 2016年10月12日下午6:21:18
	 * @param mdate
	 * @return
	 */
	public List<AreaPo> getAreaSheetCountByMdate(@Param("mdate")String mdate);
	/**
	 * 根据日期获取各个区域的单子状态
	 * @description
	 * @date 2016年10月12日下午6:21:18
	 * @param mdate
	 * @return
	 */
	public List<AreaPo> getAreaSheetStatusByMdate(@Param("mdate") String mdate);
	
	/**
	 * 获取某一范围买卖单数据
	 * @author 车斯剑
	 * @date 2016年10月14日下午5:42:54
	 * @param startTime
	 * @param endTime
	 * @param status
	 * @return
	 */
	public List<DeclarePo> querySheetsByTime(@Param("startTime")String startTime, @Param("endTime")String endTime,
			@Param("status")Integer status);
	
	/**
	 * 根据时间区间获取申报单及申报曲线数据
	 * @description
	 * @author 大雄
	 * @date 2016年10月18日下午3:04:43
	 * @param startTime
	 * @param endTime
	 * @param status
	 * @return
	 */
	public List<DeclarePo> getListAndDataByMdateInterval(@Param("startTime")String startTime, @Param("endTime")String endTime,
			@Param("status")Integer status);

	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年11月24日下午5:40:25
	 * @param time
	 * @return
	 */
	public Long getDeclareIdByTimeAndArea(@Param("time")String time, @Param("area")String area);
}
