package com.state.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.state.dao.CorridorPathDaoI;
import com.state.po.CorridorPathPo;
import com.state.service.CorridorPathServiceI;
@Service
public class CorridorPathServiceImpl implements CorridorPathServiceI{
	@Autowired
    private CorridorPathDaoI corridorPathDao;
	
	public List<CorridorPathPo> getAllCorridorPathByPathId(String pathId) {
		return corridorPathDao.getAllCorridorPathByPathId(pathId);
	}

	public void insertCorridorPath(CorridorPathPo po) {
		corridorPathDao.insertCorridorPath(po);
	}

	public void updateCorridorPath(CorridorPathPo po) {
		corridorPathDao.updateCorridorPath(po);
	}

	public CorridorPathPo getCorridorPathById(String id) {
		return corridorPathDao.getCorridorPathById(id);
	}

	public void deleteCorridorPathById(String id) {
		corridorPathDao.deleteCorridorPathById(id);
	}

	public void deleteCorridorPathByUpathId(String uPathId) {
		corridorPathDao.deleteCorridorPathByUpathId(uPathId);
	}
	
	
	
	

}
