package com.state.service.org;

import java.util.List;

import com.state.po.org.OrgDepartPo;
import com.state.po.org.OrgDepartUserPo;

public interface OrgDepartServiceI {

	List<OrgDepartPo> selectList(boolean isState,String parentId);
	public boolean insertDepartUser(OrgDepartUserPo odu);
	List<OrgDepartPo> selectListByUser(String userId);

}
