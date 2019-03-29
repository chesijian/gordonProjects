package com.state.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.DeclarePo;

/**
 * 撮合dao
 * @author 帅
 *
 */
public interface MatchDao {

	/**
	 * 根据ID删除申报主表
	 * @param id
	 */
	public void deleteDeclare(Integer id);

	/**
	 * 插入申报主表
	 * @param declarePo
	 */
	public void insertDeclare(DeclarePo declarePo);
	
	/**
	 * 根据ID查找申报主表是否存在
	 * @param id
	 * @return
	 */
	public int countDeclareById(long id);
	
	/**
	 * 根据参数查询申报主表
	 * @param param
	 * @return
	 */
	public List<DeclarePo> selectDeclareByParam(@Param("area")String area,@Param("mdate")String mdate
			,@Param("mname")String mname);
}
