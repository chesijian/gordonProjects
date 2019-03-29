package com.state.dao.org;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.org.OrgResourcesPo;

public interface OrgResourcesDaoI {

	List<OrgResourcesPo> selectResourcesListByPid(@Param("pid")String pid);

}
