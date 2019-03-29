package com.state.service.org.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.state.annotation.LogWrite;
import com.state.dao.org.OrgDepartDaoI;
import com.state.dao.org.OrgDepartUserDaoI;
import com.state.dao.org.OrgRoleResourcesDaoI;
import com.state.po.org.OrgDepartPo;
import com.state.po.org.OrgDepartUserPo;
import com.state.service.org.OrgDepartServiceI;
import com.state.util.SessionUtil;

/**
 * 处理部门的类
 * @author chesj
 *
 */
@Service
@Transactional
public class OrgDepartServiceImpl implements OrgDepartServiceI {

	@Autowired
	private OrgDepartDaoI orgDepartDao;
	@Autowired
	private OrgDepartUserDaoI orgDepartUserDao;
	@Autowired
	private OrgRoleResourcesDaoI orgRoleResourcesDao;
	
	
	public List<OrgDepartPo> selectList(boolean isState,String parentId) {
		// TODO Auto-generated method stub
		return orgDepartDao.selectList(isState,parentId);
	}
	
	public OrgDepartPo selectOneByDepartId(String departId){
		OrgDepartPo depart = orgDepartDao.selectOneByDepartId(departId);
		return depart;
	}

	
	@LogWrite(modelName="用户管理",operateName="新增部门")
	public boolean insertDepartUser(OrgDepartUserPo odu) {
		OrgDepartPo depart = selectOneByDepartId(odu.getDepartId());
		if(depart == null){
			return false;
		}else{
			//System.out.println("chehhha88888==="+odu.getDepartUid());
			orgDepartUserDao.deleteByDepart(odu.getDepartUid());
			
			odu.setDepartUid(depart.getId());
			
			odu.setAddName(SessionUtil.getAddName());
			odu.setAddNameCn(SessionUtil.getAddNameCn());
			odu.setDocCreated(new Date());
			orgDepartUserDao.insert(odu);
			return true;
		}
	}

	
	public List<OrgDepartPo> selectListByUser(String userId) {
		// TODO Auto-generated method stub
		
		return orgDepartDao.selectListByUser(userId);
	}

}
