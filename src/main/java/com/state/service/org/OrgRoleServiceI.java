package com.state.service.org;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.org.OrgRolePo;
import com.state.po.org.OrgUserRolePo;

public interface OrgRoleServiceI {
	
	public List<OrgRolePo> selectListByIsSys(int isSys,String type);
	public List<OrgRolePo> selectList(int isSys);
	
	public OrgRolePo selectOneByRoleId(String roleId);
	
	public List<OrgRolePo> selectListByUser(String userUid);
	
	/**
	 * 保存角色
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日下午10:35:10
	 * @param our
	 * @return
	 */
	public boolean insertUserRole(OrgUserRolePo our);
	
}
