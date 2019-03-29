package com.state.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.DeclareDataPo;

public interface IDeclareDataDao {
	/**
	 * 根据申报主表ID删除申报子表数据
	 * @param declareId
	 */
	public void deleteDeclareData(Long declareId);
	
	/**
	 * 增加申请子表
	 * @param declareDataDao
	 */
	public void insertDeclData(DeclareDataPo declareData);
	
	/**
	 * 更新申请子表
	 * @param declareDataDao
	 */
	public void updateDeclData(DeclareDataPo declareData);
	
	/**
	 * 根据参数查找申请子表
	 * @param id 申报主表ID
	 * @param dtype 申报子表类型
	 * @return 申报子表列表
	 */
	public List<DeclareDataPo> getDeclDataById(@Param("id")Long id,@Param("dtype")String dtype);
	
	public List<DeclareDataPo> getDeclDataByIds(List<Long> ids);
	/**
	 * 根据ID查找申请子表
	 */
	public DeclareDataPo getDeclDataPoById(@Param("id")Long id);
	
}
