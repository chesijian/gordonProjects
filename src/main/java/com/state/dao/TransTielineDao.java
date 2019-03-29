package com.state.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.TransTielinePo;

/**
 * 
 * @author 车斯剑
 *
 */
public interface TransTielineDao {
	
	public List<TransTielinePo> getTree(@Param("dtime")String dtime,@Param("area")String area, @Param("status")Integer status);

	public TransTielinePo getTransTielineById(@Param("id")String id);

	public void insertTransTieline(TransTielinePo po);

	public void deleteResultByDate(@Param("mdate")String mdate);

	public List<TransTielinePo> getTransTielineList();

	public List<TransTielinePo> getTransTielineListByMdate(@Param("mdate")String mdate,@Param("area")String area);

	public TransTielinePo getTotalTransTielineByName(@Param("tielineName")String tielineName, @Param("mdate")String dtime);
	
	public List<TransTielinePo> getTradeData(@Param("dtime")String dtime,@Param("area")String area);

}
