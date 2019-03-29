package com.state.service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.ibatis.annotations.Param;

import com.state.exception.MsgException;
import com.state.po.AreaPo;
import com.state.po.DeclareDataPo;
import com.state.po.DeclareExtraPo;
import com.state.po.DeclarePo;
import com.state.po.TypePo;
import com.state.po.UserPo;



/**
 * 申报service
 * @author 帅
 *
 */
public interface IDeclareService {
	
	/**
	 * 查询所有单子
	 * @param area
	 * @return
	 */
	public List<DeclarePo> getDeclares(String area,String time);
	
	/**
	 * 查询申报数据
	 * @param id
	 * @param dtype
	 * @return
	 */
	public DeclareDataPo getDeclareData(Long id,String dtype);
	
	/**
	 * 查询申报数据
	 * @param id
	 * @param dtype
	 * @return
	 */
	public DeclareDataPo getDeclareSumData(List<Long> id);
	
	public Map<Float,Map<String,Float>> getDeclareExtraData(long id,Float price);
	
	public List<Map<String,Object>> getDeclareExtraData(long id);
	
	/**
	 * 解析获取单个单子，96个时段的报价曲线
	 * @description
	 * @author 大雄
	 * @date 2016年8月25日下午4:20:01
	 * @param id
	 * @return
	 */
	public TreeMap<Integer, TreeMap<Float, Float>> getDeclareIntervalData(long id);
	/**
	 * 解析获取单个单子，96个时段的报价曲线 新的
	 * @description
	 * @author 大雄
	 * @date 2016年9月21日下午12:17:00
	 * @param id
	 * @return
	 */
	public Map<String,Object> getDeclareIntervalData1(long id);
	
	/**
	 * 是否存在申报单
	 * @param id
	 * @return
	 */
	public boolean existsDeclare(Long id);
	
	/**
	 * 删除申报单
	 * @param id
	 */
	public void deleteDeclare(Long id);
	
	/**
	 * 更新申报主表
	 * @param id
	 * @return
	 */
	public void updateDeclare (DeclarePo declarePo);

	/**
	 * 查询时段类型
	 * @return
	 */
	public TypePo getTimeType();
	/**
	 * 统计买卖单数
	 * @return
	 */
	public List<String> getSheetDeclNum(String time);
	
	public List<DeclarePo> queryForList(String time,String area,Integer status);
	
	public DeclarePo getDeclare(long parseLong);
	
	/**
	 * 获取某个日期的最新单子
	 * @description
	 * @author 大雄
	 * @date 2016年8月29日下午3:14:36
	 * @param mdate
	 * @return
	 */
	public DeclarePo getLastDeclare(String area ,String mdate);
	

	/**
	 * 保存96值
	 * @description
	 * @author 大雄
	 * @date 2016年8月12日下午8:43:30
	 * @param id
	 * @param data
	 */
	public void saveDeclareData(String id, String data);

	public void updateDeclareStatus(String ids);

	/**
	 * 保存申报单数据
	 * @description
	 * @author 大雄
	 * @date 2016年9月7日下午5:20:58
	 * @param id
	 * @param area
	 * @param drole
	 * @param startDate
	 * @param endDate
	 * @param time
	 * @param data
	 * @return
	 */
	public DeclarePo saveDeclare(String id, String area,String drole,String startDate, String endDate, String time, String data) throws MsgException,Exception; 
	/**
	 * 批量导入declare数据dat
	 * @description
	 * @author 大雄
	 * @date 2016年9月7日下午4:48:08
	 * @param string
	 */
	public void importDeclare(String filePath)throws Exception ;

	/**
	 * 通过小邮件获取各个省调的上报数据
	 * @description
	 * @author 大雄
	 * @date 2016年9月19日下午3:19:38
	 * @param path
	 * @param mdate
	 * @return
	 */
	public String readFile(String path,String mdate) throws Exception;

	/**
	 * 通过日期获取所有交易单，并组装导出word的字符串
	 * @description
	 * @author 大雄
	 * @date 2016年9月25日下午6:27:24
	 * @param time
	 * @return
	 */
	public Map<String, StringBuilder> getDelcateMap(String tempPath,String time);
	
    public void addLog(String sheetId,String mdate,String userId,String type);
    
    public void clearLogForDecl(String sheetId,String mdate,String userId,String type);
    
    public void addLogForResult(String userId,String mdate,String type);
    
    public void clearLogForResult(String userId,String mdate,String type);
	
    public List<String> getUserIdList(String sheetId);
    /**
     * 解密 获取申报单和电力电价曲线
     * @description
     * @author 大雄
     * @date 2016年10月12日下午5:19:15
     * @param mdate
     * @param aera
     * @return
     */
	public List<DeclarePo> getListAndDataByMdate(String mdate,String aera, Integer status);
	
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
	public List<AreaPo> getAreaSheetStatusByMdate(@Param("mdate")String mdate);
	
	/**
     * 获取某一范围数据
     * @author 车斯剑
     * @date 2016年10月14日下午5:40:07
     * @param startTime
     * @param endTime
     * @param i
     * @return
     */
	public List<DeclarePo> querySheetsByTime(String startTime, String endTime,Integer status);

	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年10月14日下午5:50:35
	 * @param uids
	 * @return
	 */
	public Map<String, Double> getDeclareExtraDatasByIds(List<String> uids);
	
	
	/**
	 * 导出周报组装获取申报数据
	 * @description
	 * @author 大雄
	 * @date 2016年10月18日下午4:24:53
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Map<String, Object> getListAndDataByMdateInterval(String startTime, String endTime) ;

	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年11月24日下午5:35:33
	 * @param time
	 * @return
	 */
	public Long getDeclareIdByTimeAndArea(String time, String area);
	
}
