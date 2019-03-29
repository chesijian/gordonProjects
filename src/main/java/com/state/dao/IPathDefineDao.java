package com.state.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.PathDefinePo;

public interface IPathDefineDao {

	/**
	 * 查询所有的通道
	 * @return 通道列表
	 */
	public List<PathDefinePo> selectPathALL(@Param("time")String time);
		
	/**
	 * 更新通道定义
	 * @param pathDefinePo
	 */
	public void updatePathDefine(PathDefinePo pathDefinePo);
	
	/**
	 * 删除通道定义
	 * @param mpath
	 */
	public void deletePathDefine(@Param("pathId")String pathId);
	
	/**
	 * 插入通道定义
	 * @param pathDefinePo
	 */
	public void insertPathDefine(PathDefinePo pathDefinePo);

	/**
	 * 根据通道名称查询通道个数
	 * @param mpath
	 * @return
	 */
	public int countPathByName(@Param("id")String id);
	/**
	 * 统计通道个数
	 * @return
	 */
	public int getPathNum();

	/**
	 * 根据id查询通道
	 * @param mpath
	 * @return 通道
	 */
	public PathDefinePo getPathDefineById(@Param("id")String id);

	/**
	 * 
	 * @author 车斯剑
	 * @param time 
	 * @date 2016年11月10日下午2:38:23
	 * @return
	 */
	public List<PathDefinePo> getPathListAndCorridor(@Param("time")String time);

	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年11月16日下午10:29:20
	 * @param time
	 * @param mpath
	 * @return
	 */
	public List<PathDefinePo> selectPathByName(@Param("time")String time, @Param("pathName")String pathName);

	
}
