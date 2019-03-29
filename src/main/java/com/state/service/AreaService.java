package com.state.service;

import java.util.List;

import com.state.po.AreaPo;

public interface AreaService {
	
	public List<AreaPo> getAllArea();

	public String getDcodeByArea(String area);

	List<AreaPo> getAreaInfo();
}
