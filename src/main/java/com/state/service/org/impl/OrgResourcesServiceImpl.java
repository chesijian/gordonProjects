package com.state.service.org.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.state.dao.org.OrgResourcesDaoI;
import com.state.po.org.OrgResourcesPo;
import com.state.service.org.OrgResourcesServiceI;

/**
 * 出来权限的类
 * @author chesj
 *
 */
@Service
@Transactional
public class OrgResourcesServiceImpl implements OrgResourcesServiceI {

	@Autowired
	private OrgResourcesDaoI orgResourcesDao;
	
	
	public List<OrgResourcesPo> selectResourcesListByPid(String pid) {
		// TODO Auto-generated method stub
		return orgResourcesDao.selectResourcesListByPid(pid);
	}

}
