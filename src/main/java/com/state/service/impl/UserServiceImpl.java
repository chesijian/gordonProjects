package com.state.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.state.annotation.LogWrite;
import com.state.dao.ILoginDao;
import com.state.dao.UserDaoI;
import com.state.po.UserPo;
import com.state.security.EncryptUtil;
import com.state.service.UserServiceI;
import com.state.util.CommonUtil;
import com.state.util.SessionUtil;

@Service
@Transactional
public class UserServiceImpl implements UserServiceI {

	@Autowired
	private UserDaoI userDao;
	
	
	public UserPo get(String id) {
		// TODO Auto-generated method stub
		return userDao.selectById(id);
	}

	
	public boolean getIfExtis(String userId, String userName) {
		// TODO Auto-generated method stub
		UserPo user = userDao.selectByUserIdOrUserName(userId,userName);
		if(user != null){
			return true;
		}
		return false;
	}

	@LogWrite(modelName="用户管理",operateName="修改用户")
	public void update(UserPo user) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		if(CommonUtil.ifEmpty(user.getMkey())){
			user.setMkey(EncryptUtil.md5AndSha(user.getMkey()));
		}
		// TODO Auto-generated method stub
		user.setLastModifier(SessionUtil.getAddName());
		user.setLastModifierCn(SessionUtil.getAddNameCn());
		user.setLastModified(new Date());
		//System.out.println("==============="+CommonUtil.objToJson(user));
		userDao.update(user);
	}

	@LogWrite(modelName="用户管理",operateName="新增用户")
	public void insert(UserPo user) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		if(CommonUtil.ifEmpty(user.getMkey())){
			user.setMkey(EncryptUtil.md5AndSha(user.getMkey()));
		}
		// TODO Auto-generated method stub
		if(user.getArea().equals("国调")){
			user.setDtype("国");
		}else{
			user.setDtype("省");
		}
		user.setId(CommonUtil.getUUID());
		user.setAddName(SessionUtil.getAddName());
		user.setAddNameCn(SessionUtil.getAddNameCn());
		user.setDocCreated(new Date());
		//System.out.println(userDao+"==============="+user);
		userDao.insert(user);
	}

}
