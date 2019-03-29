package com.state.dao.org;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.org.OrgDepartPo;

public interface OrgDepartDaoI {

	/**
	 * 新增
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日上午9:56:33
	 * @param po
	 */
	public void insert(OrgDepartPo po) ;
	/**
	 * 更新
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日上午9:56:41
	 * @param po
	 */
	public void update(OrgDepartPo po) ;
	/**
	 * 根据id删除
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日上午9:56:47
	 * @param po
	 */
	public void delete(OrgDepartPo po) ;
	
	/**
	 * 根据id获取角色
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日下午10:27:17
	 * @param id
	 * @return
	 */
	public OrgDepartPo selectOneByDepartId(@Param("departId")String departId);
	
	/**
	 * 如果是国调，则获取各个处室，如果不是国调，则返回对应的省调下面的部门
	 * @description
	 * @author 大雄
	 * @date 2016年8月23日下午3:58:54
	 * @param isState 是否国调
	 * @param parentId 主键
	 * @return
	 */
	public List<OrgDepartPo> selectList(@Param("isState")boolean isState,@Param("parentId")String parentId);
	/**
	 * 获取用户所在部门
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日下午11:13:35
	 * @param userUid
	 * @return
	 */
	public List<OrgDepartPo> selectListByUser(@Param("userUid")String userUid);
}
