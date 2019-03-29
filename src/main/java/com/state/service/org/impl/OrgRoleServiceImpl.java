package com.state.service.org.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.state.annotation.LogWrite;
import com.state.dao.org.OrgRoleDaoI;
import com.state.dao.org.OrgRoleResourcesDaoI;
import com.state.dao.org.OrgUserRoleDaoI;
import com.state.po.org.OrgRolePo;
import com.state.po.org.OrgUserRolePo;
import com.state.service.org.OrgRoleServiceI;
import com.state.util.SessionUtil;
/**
 * 处理角色的类
 * @description
 * @author 大雄
 * @date 2016年8月9日下午9:16:33
 */
@Service
@Transactional
public class OrgRoleServiceImpl implements OrgRoleServiceI {

	@Autowired
	private OrgRoleDaoI orgRoleDao;
	@Autowired
	private OrgUserRoleDaoI orgUserRoleDao;
	@Autowired
	private OrgRoleResourcesDaoI orgRoleResourcesDao;
	
	
	public List<OrgRolePo> selectListByIsSys(int isSys,String type) {
		// TODO Auto-generated method stub
		return orgRoleDao.selectListByIsSys(isSys,type);
	}

	
	public OrgRolePo selectOneByRoleId(String roleId){
		OrgRolePo role = orgRoleDao.selectOneByRoleId(roleId);
		return role;
	}
	
	public List<OrgRolePo> selectListByUser(String userUid){
		return orgRoleDao.selectListByUser(userUid);
	}
	
	
	@LogWrite(modelName="用户管理",operateName="新增角色")
	public boolean insertUserRole(OrgUserRolePo our){
		OrgRolePo role = selectOneByRoleId(our.getRoleId());
		if(role == null){
			return false;
		}else{
			
			orgUserRoleDao.deleteByUser(our.getUserUid());
			our.setRoleUid(role.getId());
			our.setAddName(SessionUtil.getAddName());
			our.setAddNameCn(SessionUtil.getAddNameCn());
			our.setDocCreated(new Date());
			orgUserRoleDao.insert(our);
			return true;
		}
		
	}


	
	public List<OrgRolePo> selectList(int isSys) {
		// TODO Auto-generated method stub
		return orgRoleDao.selectList(isSys);
	}
}
