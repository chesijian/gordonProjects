package com.state.dao.org;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.org.OrgRolePo;

public interface OrgRoleDaoI {

	/**
	 * 新增
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日上午9:56:33
	 * @param po
	 */
	public void insert(OrgRolePo po) ;
	/**
	 * 更新
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日上午9:56:41
	 * @param po
	 */
	public void update(OrgRolePo po) ;
	/**
	 * 根据id删除
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日上午9:56:47
	 * @param po
	 */
	public void delete(OrgRolePo po) ;
	
	/**
	 * 根据id获取角色
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日下午10:27:17
	 * @param id
	 * @return
	 */
	public OrgRolePo selectOneByRoleId(@Param("roleId")String roleId);
	
	/**
	 * 根据是否系统角色获取角色列表
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日下午9:27:49
	 * @param isSys
	 * @return
	 */
	public List<OrgRolePo> selectListByIsSys(@Param("isSys")int isSys,@Param("type")String type);
	/**
	 * 获取用户所拥有的角色
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日下午11:13:35
	 * @param userUid
	 * @return
	 */
	public List<OrgRolePo> selectListByUser(@Param("userUid")String userUid);
	public List<OrgRolePo> selectList(@Param("isSys")int isSys);
}
