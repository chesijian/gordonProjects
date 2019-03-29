package com.state.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.state.po.UserPo;

public interface UserServiceI {
	public UserPo get(String id);

	/**
	 * 判断该用户编号和用户名是否存在
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日下午5:07:46
	 * @param userId
	 * @param userName
	 * @return
	 */
	public boolean getIfExtis(String userId, String userName);

	/**
	 * 修改用户
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日下午5:18:33
	 * @param user
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public void update(UserPo user) throws NoSuchAlgorithmException, UnsupportedEncodingException;

	/**
	 * 新增用户
	 * @description
	 * @author 大雄
	 * @date 2016年8月10日下午4:30:13
	 * @param user
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public void insert(UserPo user)throws NoSuchAlgorithmException, UnsupportedEncodingException;
}
