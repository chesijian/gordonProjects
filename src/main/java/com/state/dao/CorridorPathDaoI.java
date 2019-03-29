package com.state.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.CorridorPathPo;

public interface CorridorPathDaoI {
	/**
	 * 根据通道定义Id 获取所有相关的所有通道
	 * @return 
	 */
	public List<CorridorPathPo> getAllCorridorPathByPathId(@Param("pathId")String pathId);
	
	public void insertCorridorPath(CorridorPathPo po);
	
	public void updateCorridorPath(CorridorPathPo po);
	
	public CorridorPathPo getCorridorPathById(@Param("id")String id);
	
	public void deleteCorridorPathById(@Param("id")String id);

	public void deleteCorridorPathByUpathId(@Param("uPathId")String uPathId);
	
	
}
