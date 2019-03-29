package com.state.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.sys.PfOnlineUserPo;
/**
 * 
 * @description
 * @author 大雄
 * @date 2016年8月17日下午2:32:31
 */
public interface PfOnlineUserDaoI {

	public void insert(PfOnlineUserPo po) ;
	
	public List<PfOnlineUserPo> selectOnLineUser(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("start")int start,@Param("end")int end);

	public long getOnlineUserCount(@Param("startTime")String startTime, @Param("endTime")String endTime);

	public void delete(@Param("userUid")String userUid, @Param("sessionId")String sessionId);
	public void deleteAll();
	
}
