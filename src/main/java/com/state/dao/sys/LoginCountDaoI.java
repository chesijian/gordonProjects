package com.state.dao.sys;

import org.apache.ibatis.annotations.Param;

import com.state.enums.Enums_SystemConst;
import com.state.po.PfLoggerPo;
import com.state.po.sys.PfLoginCountPo;
import com.state.util.CommonUtil;
import com.state.util.properties.JPropertiesUtil;

/**
 * 
 * @description
 * @author 大雄
 * @date 2016年8月18日上午9:58:41
 */
public interface LoginCountDaoI {
	
	/**
	 * 查询
	 * @description
	 * @author 大雄
	 * @date 2016年8月18日上午10:14:56
	 * @param user
	 */
	public PfLoginCountPo selectOne(String user);
	
	/**
	 * 插入
	 * @description
	 * @author 大雄
	 * @date 2016年8月18日上午10:15:06
	 * @param po
	 */
	public void insert(PfLoginCountPo po);
	
	/**
	 * 更新
	 * @description
	 * @author 大雄
	 * @date 2016年8月18日上午10:15:12
	 * @param po
	 */
	public void update(PfLoginCountPo po);
	
	/**
	 * 删除
	 * @description
	 * @author 大雄
	 * @date 2016年8月18日上午10:15:21
	 * @param user
	 */
	public void delete(@Param("user")String user);
	
	/**
	 * 定时器删除超时
	 * @description
	 * @author 大雄
	 * @date 2016年8月18日上午10:15:28
	 */
	public void deleteOverTime(@Param("loginOverTime")Integer loginOverTime);
	
}
