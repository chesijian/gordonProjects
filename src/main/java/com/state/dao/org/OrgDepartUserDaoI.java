package com.state.dao.org;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.org.OrgDepartPo;
import com.state.po.org.OrgDepartUserPo;

public interface OrgDepartUserDaoI {

	/**
	 * 新增
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日上午9:56:33
	 * @param po
	 */
	public void insert(OrgDepartUserPo po) ;
	
	/**
	 * 根据用户唯一编号用逗号隔开
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日上午9:59:17
	 * @param userUids
	 */
	public void deleteByUser(@Param("userUids")String userUids) ;
	
	/**
	 * 根据部门唯一编号用逗号隔开
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日上午9:58:57
	 * @param userUids
	 */
	public void deleteByDepart(@Param("departUids")String departUids) ;

	/**
	 * 
	 * @author 车斯剑
	 * @param userId
	 * @return
	 */
	public List<OrgDepartPo> selectListByUser(String userId);
}
