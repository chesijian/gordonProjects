package com.state.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.LineDefinePo;

public interface LineDefineDao {

	/**
	 * 根据联络线名查询联络线
	 * @param lineLimitPo
	 */
	public LineDefinePo getLineDefine(String mcorhr);
	
	/**
	 * 获取所有的联络线
	 * @return
	 */
	public List<LineDefinePo> getAllLine();
	
	/**
	 * 获取所有的联络线
	 * @return
	 */
	public List<LineDefinePo> getAllLineByArea(@Param("area")String area);
	
	/**
	 * 根据联络线名查询联络线个数
	 * @param mcorhr
	 * @return
	 */
	public int countLineByMcorhr(String mcorhr);
	
	/**
	 * 插入联络线
	 * @param lineDefine
	 */
	public void insertLineDefine(LineDefinePo lineDefine);
	
	/**
	 * 删除联络线
	 * @param mcorhr
	 */
	public void deleteLineDefine(String mcorhr);
	/**
	 * 统计联络线
	 * @param mcorhr
	 */
	public int getLineNum();

	public LineDefinePo getLineDefineById(@Param("id")String Id);

	public void updateLineDefine(LineDefinePo po);

	
}
