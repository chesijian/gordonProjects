package com.state.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import com.state.annotation.LogWrite;
import com.state.po.TypePo;
import com.state.po.UserPo;




public interface ILoginService {
	
	/**
	 * 校验用户
	 * @param user,password
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public UserPo judgeUser(String user,String password) throws NoSuchAlgorithmException, UnsupportedEncodingException;
	
	/**
	 * 插入在线用户
	 * @description
	 * @author 大雄
	 * @date 2016年8月17日下午5:11:36
	 */
	public void insertOnlineUser();
	/**
	 * 注册检查
	 * @param user
	 * @return
	 */
	public UserPo containUser(String user);
	
	/**
	 * 注册用户
	 * @param user,password,area
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchAlgorithmException 
	 */
	
	public void saveUser(String userId,String user,String password,String area,String sf) throws NoSuchAlgorithmException, UnsupportedEncodingException;
	
	/**
	 * 查询已有权限的菜单
	 * @param user
	 */
	public List<Map<String,String>> selectBill(String user);
	
	/**
	 * 查询未审核用户
	 * @param user
	 */
	public List<UserPo> selectNopass();
	
	/**
	 * 查询已审核用户
	 * @param user
	 */
	public List<UserPo> selectPass();
	
	/**
	 * 审批用户
	 * @param user
	 */
	public void approve(String user);
	
	/**
	 * 给用户分配菜单
	 * @param user
	 */
	public void allotBill(String user);
	/**
	 * 给用户分配菜单
	 * @param user
	 */
	public int getNumByParam(String time);
	/**
	 * 查询类型时段数
	 * @param user
	 */
	public TypePo getCountByType();
	/**
	 * 更新用户
	 * @param user
	 */
	public void updateUser(String name,String state);

	/**
	 * 删除用户
	 * @description
	 * @author 大雄
	 * @date 2016年8月3日下午5:21:09
	 * @param id
	 */
	public void deleteUser(String id);

	/**
	 * 删除在线用户
	 * @description
	 * @author 大雄
	 * @date 2016年8月17日下午4:54:13
	 * @param userUid
	 * @param sessionId
	 */
	public void deleteOnlineUser(String userUid,String sessionId);
	
	/**
	 * 删除所有
	 * @description
	 * @author 大雄
	 * @date 2016年8月17日下午5:26:52
	 */
	public void deleteOnlineUser();

	public long getPssCount();


	public List<UserPo> selectPassCurrentPage(int currentPage,int limit);
}
