package com.state.dao.org;

import org.apache.ibatis.annotations.Param;

import com.state.po.org.OrgUserRolePo;

public interface OrgUserRoleDaoI {

	/**
	 * 新增
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日上午9:56:33
	 * @param po
	 */
	public void insert(OrgUserRolePo po) ;
	
	/**
	 * 根据用户唯一编号用逗号隔开
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日上午9:59:17
	 * @param userUids
	 */
	public void deleteByUser(@Param("userUids")String userUids) ;
	
	/**
	 * 根据角色唯一编号用逗号隔开
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日上午9:58:57
	 * @param userUids
	 */
	public void deleteByRole(@Param("roleUids")String roleUids) ;
}
