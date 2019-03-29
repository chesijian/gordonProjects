package com.state.dao;


import org.apache.ibatis.annotations.Param;

import com.state.po.UserPo;

public interface UserDaoI {
	
	
	public UserPo selectById(@Param("id")String id);

	public UserPo selectByUserIdOrUserName(@Param("userId")String userId, @Param("userName")String userName);

	public void update(UserPo user);

	public void insert(UserPo user);



}
