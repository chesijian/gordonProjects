package com.state.service.org;

import java.util.List;

import com.state.po.org.OrgRoleResourcesPo;
/**
 * 
 * @author chesj
 *
 */
public interface OrgRoleResourcesServiceI {

	public List<OrgRoleResourcesPo> selectListByRole(String roleUid) ;

	public void saveRoleResources(String roleUid, String ids);

	public void insertRoleResources(List<OrgRoleResourcesPo> orrpList,String roleUid);

	public List<OrgRoleResourcesPo> selectListByRoles(String roleId);
}
