package com.state.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.LineLimitPo;
import com.state.po.PathDefinePo;
import com.state.po.PathResultPo;
import com.state.po.ResultPo;

public interface IPathResultDao {

	/**
	 * 查询通道结果列表（不指定类型）
	 * @param mdate 日期
	 * @param mpath 通道名称
	 * @return 通道结果列表
	 */
	public List<PathResultPo> getPathResultList(@Param("mdate")String mdate,@Param("mpath")String mpath);
	
	/**
	 * 根据参数查询通道结果
	 * @param mdate 日期
	 * @param mpath 通道名称
	 * @param dtype 通道结果类型
	 * @return 通道结果
	 */
	public PathResultPo getPathResult(@Param("mdate")String mdate,@Param("mpath")String mpath,@Param("dtype")String dtype);
	
	/**
	 * 插入通道结果
	 * @param pathResultPo
	 */
	public void insertPathResult(PathResultPo pathResultPo);

	/**
	 * 更新通道结果
	 * @param pathResultPo
	 */
	public void updatePathResult(PathResultPo pathResultPo);
	
	/**
	 * 根据通道名称删除通道所有相关结果
	 * @param String 
	 */
	public void deletePathResultByName(String  mpath);
	
	/**
	 * 按照日期删除通道结果
	 * @param String 
	 */
	public void deletePathResultByDate(String mdate);
	
	/**
	 * 调用c结束之后读取结果，先删除，dtype可以传入多个值加逗号隔开
	 * @description
	 * @author 大雄
	 * @date 2016年8月24日下午5:22:58
	 * @param mdate
	 * @param dtype
	 * @return
	 */
	public long deleteResultByDateAndDtype(@Param("mdate")String mdate,@Param("dtypes")String[] dtypes);
	

	/**
	 * 按照日期更新通道结果发布标志位
	 * @param mdate
	 * @param dprint
	 */
	public void updatePrint(@Param("mdate")String mdate, @Param("dprint")String dprint);
	/**
	 * 通道结果集
	 */
	public List<PathResultPo> getResultList(@Param("time")String time);
	/**
	 * 删除已存在的交易功率
	 */
	public void delPathResult(@Param("mdate")String time, @Param("dtype")String dtype);
/**
 * 发布界面获取通道及类型的树结构
 * @description
 * @author 大雄
 * @date 2016年8月27日下午2:54:39
 * @param mdate
 * @param status 
 * @return
 */
	public List<PathResultPo> getTreeForResult(@Param("mdate")String mdate, @Param("status")Integer status);
	
	/**
	 * 导出日报表统计各个通道的，各种费用的综合
	 * @description
	 * @author 大雄
	 * @date 2016年9月12日下午5:14:09
	 * @param mdate
	 * @return
	 */
	public List<PathResultPo> getSumResultByMdate(String mdate);
	
	/**
	 * 获取单个通道单个类型数据
	 * @description
	 * @author 大雄
	 * @date 2016年8月27日下午2:54:55
	 * @param id
	 * @return
	 */
	public PathResultPo getResultById(@Param("id")String id);

	/**
	 * 根据日期和类型获取数据
	 * @description
	 * @author 大雄
	 * @date 2016年9月17日下午5:17:42
	 * @param mdate
	 * @param dtypes
	 * @return
	 */
	public List<PathResultPo> getResultByMdateAndDtype(@Param("mdate")String mdate,@Param("dtypes")String[] dtypes);

	
	
}
