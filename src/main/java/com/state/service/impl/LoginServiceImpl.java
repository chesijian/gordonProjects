package com.state.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.state.annotation.LogWrite;
import com.state.dao.ILoginDao;
import com.state.dao.PfOnlineUserDaoI;
import com.state.dao.org.OrgUserRoleDaoI;
import com.state.po.TypePo;
import com.state.po.UserPo;
import com.state.po.sys.PfOnlineUserPo;
import com.state.security.EncryptUtil;
import com.state.service.ILoginService;
import com.state.util.CommonUtil;
import com.state.util.SessionUtil;
import com.state.util.sys.OnlineCounterUtil;

@Service
@Transactional
public class LoginServiceImpl implements ILoginService {

	@Autowired
	private ILoginDao loginDao;
	@Autowired
	private PfOnlineUserDaoI onlineUserDao;
	@Autowired
	private OrgUserRoleDaoI orgUserRoleDao;

	@LogWrite(modelName = "用户管理", operateName = "登录")
	public UserPo judgeUser(String user, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		// System.out.println("22222");
		password = EncryptUtil.md5AndSha(password);
		UserPo u = loginDao.judgeUser(user, password);

		return u;
	}

	public void insertOnlineUser() {
		// 保存登陆日志信息
		UserPo u = SessionUtil.getUserPo();
		if (u != null) {
			PfOnlineUserPo ou = new PfOnlineUserPo();
			ou.setId(CommonUtil.getUUID());
			ou.setIp(SessionUtil.getClientIp());
			ou.setType(0);
			ou.setSeId(SessionUtil.getSessionId());
			ou.setUserName(u.getMname());
			ou.setUserUid(u.getId());
			ou.setUserId(u.getUserId());
			ou.setDocCreated(new Date());
			onlineUserDao.insert(ou);
			OnlineCounterUtil.raise(u.getArea(), u.getId(), SessionUtil.getSessionId());
		}
	}

	public UserPo containUser(String user) {
		return loginDao.containUser(user);
	}

	@LogWrite(modelName = "用户管理", operateName = "添加用户")
	public void saveUser(String userId, String user, String password, String area, String sf) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		password = EncryptUtil.md5AndSha(password);
		loginDao.saveUser(CommonUtil.getUUID(), userId, user, password, area, sf);
	}

	public List<Map<String, String>> selectBill(String user) {
		return loginDao.selectBill(user);
	}

	public List<UserPo> selectNopass() {
		return loginDao.selectNopass();
	}

	public List<UserPo> selectPass() {
		return loginDao.selectPass();
	}

	public void approve(String user) {
		loginDao.approve(user);
	}

	public void allotBill(String user) {
		for (int i = 1; i <= 3; i++) {
			loginDao.allotBill(user, i);
		}
	}

	public int getNumByParam(String time) {
		// TODO Auto-generated method stub
		return 0;
	}

	public TypePo getCountByType() {
		return loginDao.getCountByType();
	}

	@LogWrite(modelName = "用户管理", operateName = "修改用户")
	public void updateUser(String name, String state) {
		loginDao.updateUser(name, state);
	}

	@LogWrite(modelName = "用户管理", operateName = "删除用户")
	public void deleteUser(String id) {
		orgUserRoleDao.deleteByUser(id);
		loginDao.deleteUser(id);
	}

	@LogWrite(modelName = "用户管理", operateName = "退出登陆")
	public void deleteOnlineUser(String userUid, String sessionId) {
		OnlineCounterUtil.reduce(SessionUtil.getArea(), userUid, sessionId);
		onlineUserDao.delete(userUid, sessionId);
	}
	
	/**
	 * 删除所有
	 * @description
	 * @author 大雄
	 * @date 2016年8月17日下午5:26:52
	 */
	public void deleteOnlineUser(){
		onlineUserDao.deleteAll();
	}

	
	public long getPssCount() {
		// TODO Auto-generated method stub
		return loginDao.getPssCount();
	}


	
	
	public List<UserPo> selectPassCurrentPage(int currentPage,int limit) {
		// TODO Auto-generated method stub
		return loginDao.selectPassCurrentPage(currentPage,limit);
	}
	
}


