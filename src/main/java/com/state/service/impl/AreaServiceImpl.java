package com.state.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.state.dao.AreaDao;
import com.state.po.AreaPo;
import com.state.service.AreaService;

@Service
public class AreaServiceImpl implements AreaService {

	@Autowired
	private AreaDao areaDao;
	
	public List<AreaPo> getAllArea() {
		
		return areaDao.getAllArea();
	}
	
	public String getDcodeByArea(String area) {
		return areaDao.getDcodeByArea(area);
	}

	public List<AreaPo> getAreaInfo() {
		return areaDao.getAreaInfo();
	}

}
