package com.state.service.sys;

/**
 * 
 * @description
 * @author 大雄
 * @date 2016年8月18日上午10:09:39
 */
public interface LoginCountServiceI {
	/**
	 * 判断用户是否超时
	 * @description
	 * @author 大雄
	 * @date 2016年8月18日上午10:10:07
	 * @param user
	 * @return
	 */
	public boolean getIfOverCount(String user);
	
	/**
	 * 删除
	 * @description
	 * @author 大雄
	 * @date 2016年8月18日上午10:11:11
	 * @param user
	 * @return
	 */
	public boolean delete(String user);
	
	/**
	 * flag = true,删除所有，flag = false表示只删除超时的
	 * 定时器定时删除超时的
	 * @description
	 * @author 大雄
	 * @date 2016年8月18日上午10:11:22
	 * @return
	 */
	public boolean remove(boolean flag);
}
