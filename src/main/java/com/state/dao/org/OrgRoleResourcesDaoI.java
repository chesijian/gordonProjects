package com.state.dao.org;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.org.OrgRoleResourcesPo;

public interface OrgRoleResourcesDaoI {

	/**
	 * 新增
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日上午9:56:33
	 * @param po
	 */
	public void insert(OrgRoleResourcesPo po) ;
	
	
	
	/**
	 * 根据角色唯一编号用逗号隔开
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日上午9:58:57
	 * @param userUids
	 */
	public void deleteByRole(@Param("roleUid")String roleUid) ;
	
	/**
	 * 根据角色唯一编号获取角色权限
	 * @author 车斯剑
	 * @param roleUids
	 */
	public List<OrgRoleResourcesPo> selectListByRole(@Param("roleUid")String roleUid) ;


	/**
	 * 
	 * 插入多条数据(暂没用到)
	 * @author 车斯剑
	 */
	public void saveRoleResources(@Param("ids")long[] idArr, @Param("roleUid")String roleUid, @Param("addName")String addName,
			@Param("addNameCn")String addNameCn);
}
