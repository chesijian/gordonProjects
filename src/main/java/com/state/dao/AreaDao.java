package com.state.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.AreaPo;

public interface AreaDao {
	
	public List<AreaPo> getAllArea();

	public String getDcodeByArea(@Param("area")String area);
	public String getDroleByArea(@Param("area")String area);

	public List<AreaPo> getAreaInfo();

}
