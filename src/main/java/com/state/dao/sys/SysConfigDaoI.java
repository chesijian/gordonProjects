package com.state.dao.sys;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.sys.PfLoginCountPo;
import com.state.po.sys.PfSysConfigPo;

/**
 * 操作系统常量
 * @description
 * @author 大雄
 * @date 2016年8月18日上午9:58:41
 */
public interface SysConfigDaoI {
	
	/**
	 * 查询
	 * @description
	 * @author 大雄
	 * @date 2016年8月18日上午10:14:56
	 * @param key
	 */
	public PfSysConfigPo selectOne(String key);
	
	
	/**
	 * 查询
	 * @description
	 * @author 大雄
	 * @date 2016年8月18日上午10:14:56
	 * @param key
	 */
	public PfSysConfigPo selectOneByType(@Param("key")String key,@Param("mdate")String mdate);
	
	/**
	 * 根据类型查询
	 * @description
	 * @author 大雄
	 * @date 2016年8月18日上午10:14:56
	 * @param key
	 */
	public List<PfSysConfigPo> selectListByType(String type);
	
	/**
	 * 插入
	 * @description
	 * @author 大雄
	 * @date 2016年8月18日上午10:15:06
	 * @param po
	 */
	public void insert(PfLoginCountPo po);
	
	/**
	 * 插入常量
	 * @description
	 * @author 大雄
	 * @date 2016年10月14日上午11:29:30
	 * @param po
	 */
	public void insertSysConfig(PfSysConfigPo po);
	
	/**
	 * 修改变量
	 * @description
	 * @author 大雄
	 * @date 2016年10月14日上午11:29:39
	 * @param po
	 */
	public void updateSysConfig(PfSysConfigPo po);
}
