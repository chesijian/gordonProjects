package com.state.service.org;

import java.util.List;

import com.state.po.org.OrgResourcesPo;

public interface OrgResourcesServiceI {

	List<OrgResourcesPo> selectResourcesListByPid(String pid);

}
