package com.state.service;

import java.util.List;

import com.state.po.CorridorPathPo;


public interface CorridorPathServiceI {
	/**
	 * 根据通道定义Id 获取所有相关的所有通道
	 * @return 
	 */
	public List<CorridorPathPo> getAllCorridorPathByPathId(String pathId);
	
    public void insertCorridorPath(CorridorPathPo po);
	
	public void updateCorridorPath(CorridorPathPo po);
	
	public CorridorPathPo getCorridorPathById(String id);
	
	public void deleteCorridorPathById(String id);

	public void deleteCorridorPathByUpathId(String pathId);
}
