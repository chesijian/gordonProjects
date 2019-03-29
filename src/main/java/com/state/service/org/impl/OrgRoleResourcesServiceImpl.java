package com.state.service.org.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.state.dao.org.OrgRoleResourcesDaoI;
import com.state.po.org.OrgRoleResourcesPo;
import com.state.service.org.OrgRoleResourcesServiceI;
import com.state.util.SessionUtil;

/**
 * 
 * @author chesj
 *
 */
@Service
@Transactional
public class OrgRoleResourcesServiceImpl implements OrgRoleResourcesServiceI {

	@Autowired
	private OrgRoleResourcesDaoI orgRoleResourcesDao;
	
	
	public List<OrgRoleResourcesPo> selectListByRole(String roleUid) {
		// TODO Auto-generated method stub
		return orgRoleResourcesDao.selectListByRole(roleUid);
	}

	/**
	 * 批量插入(暂没用到)
	 */
	
	public void saveRoleResources(String roleUid, String ids) {
		String[] str = ids.split(",");
		long[] idArr = new long[str.length];
		int i = 0;
		for (String temp : str) {
			idArr[i++] = Long.parseLong(temp);
		}
		String addName=SessionUtil.getAddName();
		String addNameCn=SessionUtil.getAddNameCn();
		orgRoleResourcesDao.saveRoleResources(idArr, roleUid,addName,addNameCn);
		
	}

	
	public void insertRoleResources(List<OrgRoleResourcesPo> orrpList,String roleUid) {
		// TODO Auto-generated method stub
		orgRoleResourcesDao.deleteByRole(roleUid);
		for(int i=0;i<orrpList.size();i++){
			//orrpList.get(i);
			orgRoleResourcesDao.insert(orrpList.get(i));
		}
	}

	
	public List<OrgRoleResourcesPo> selectListByRoles(String roleUid) {
		// TODO Auto-generated method stub
		return orgRoleResourcesDao.selectListByRole(roleUid);
	}

}
