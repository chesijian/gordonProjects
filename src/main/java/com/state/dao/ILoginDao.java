package com.state.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.state.po.TypePo;
import com.state.po.UserPo;

public interface ILoginDao {
	
	public UserPo judgeUser(@Param("user")String user,@Param("password")String password);
	
	public UserPo containUser(@Param("user")String user);

	public void saveUser(@Param("id")String id,@Param("userId")String userId,@Param("user")String user,@Param("password")String password,@Param("area")String area,@Param("sf")String sf);
	
	public List<Map<String,String>> selectBill(@Param("user")String user);
	
	public List<UserPo> selectNopass();

	public List<UserPo> selectPass();
	
	public void approve(@Param("user")String user);
	
	public void allotBill(@Param("user")String user,@Param("num")int num);

	public List<String> getNumUser(@Param("time")String time);

	public TypePo getCountByType();

	public void updateUser(@Param("name")String name,@Param("state")String state);

	public void deleteUser(@Param("id")String id);

	public long getPssCount();

	//DAO
	public List<UserPo> selectPassCurrentPage(@Param("currentPage")int currentPage,@Param("limit")int limit);




}
