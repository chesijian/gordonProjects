package com.state.service;

import java.util.List;
import java.util.Map;

import com.state.po.PathDefinePo;
import com.state.po.PathResultPo;
import com.state.po.TreeBean;

public interface IPathService {

	/**
	 * 获取所有的通道定义数据
	 * @return
	 */
	public List<PathDefinePo> getAllPath(String time);

	/**
	 * 根据通道名判断通道是否存在
	 * @param mpath
	 * @return
	 */
	public boolean existsPath(String id);

	/**
	 * 添加通道定义
	 * @param pathDefine
	 */
	public void addPath(PathDefinePo pathDefine);
	
	/**
	 * 删除通道
	 * @param mpath
	 */
	public void deletePath(String mpath);
	/**
	 * 统计通道个数
	 * @param mpath
	 */
	public int getPathNum();

	/**
	 * 发布界面获取交易通道各种类型数据
	 * @description
	 * @author 大雄
	 * @date 2016年8月27日下午2:49:24
	 * @param area
	 * @param time
	 * @param status 如果是1表示已经发布数据，否则就是未发布的
	 * @return
	 */
	public List<TreeBean> getTree(String mdate,Integer status);
	/**
	 * 发布界面获取交易通道各种类型数据
	 * @description
	 * @author 大雄
	 * @date 2016年8月27日下午2:49:24
	 * @param area
	 * @param time
	 * @return
	 */
	public PathResultPo getResultById(String id);

	/**
	 * 根据名称获取
	 * @author chesj
	 * @param mpath
	 * @return
	 */
	public PathDefinePo getPathDefineById(String pathId);

	public void updatePathDefine(PathDefinePo pdp);

	/**
	 * 地图展示获取的数据
	 * @description
	 * @author 大雄
	 * @date 2016年9月17日下午6:08:08
	 * @param mdate
	 * @return
	 */
	public Map<String,Map<String,Map<String,Object>>> getResultByMdateAndDtype(String mdate);

	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年11月16日下午10:26:49
	 * @param time
	 * @param mpath
	 * @return
	 */
	public List<PathDefinePo> selectPathByName(String time, String mpath);

	
}
