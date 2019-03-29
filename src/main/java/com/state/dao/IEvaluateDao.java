package com.state.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.DeclareDataPo;
import com.state.po.DeclarePo;
import com.state.po.LineLimitPo;
import com.state.po.PathDefinePo;
import com.state.po.PathResultPo;
import com.state.po.ResultPo;

public interface IEvaluateDao {
	
	public List<DeclareDataPo> selectDeclareValueBySale(String date);
	
	public List<ResultPo> selectDeclareValueByBuy(String date);
	
	public List<PathDefinePo> getPathList();
	
	public List<LineLimitPo> getCorhrByCorhr(@Param("corhrName") String corhrName,@Param("mdate") String mdate);
	
	public List<PathDefinePo> getPathDefinePoListByArea(String areaName);
	
	public List<DeclarePo> getSaleAreaList(String date);
	
	public List<DeclarePo> getBuyAreaList(String date);
	
	public List<DeclareDataPo> selectEveryDeclareValueBySale(String date);
	
	public List<DeclareDataPo> selectEveryProviceDecValueBySale(String date);
	
	public DeclarePo getDeclarePoById(@Param("id") Long id,@Param("date") String date);
	
	public DeclareDataPo getDeclareDataPoById(Long id);
	
	public PathResultPo getDeclResultSumByArea(String area);
	
	
	

}
