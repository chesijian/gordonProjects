package com.state.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.DeclareDataPo;
import com.state.po.DeclareExtraPo;

public interface DeclareExtraDaoI {
	
	
	/**
	 * 插入
	 * @description
	 * @author 大雄
	 * @date 2016年8月16日下午5:19:49
	 * @param po
	 */
	public void insert(DeclareExtraPo po);
	
	/**
	 * 删除
	 * @description
	 * @author 大雄
	 * @date 2016年8月16日下午5:19:38
	 * @param declareData
	 */
	public void delete(@Param("sheetUid")String sheetUid);
	/**
	 * 查询
	 * @description
	 * @author 大雄
	 * @date 2016年8月16日下午5:20:34
	 * @param ids
	 * @return
	 */
	public List<DeclareExtraPo> selectListBySheetUid(@Param("sheetUid")String sheetUid,@Param("price")Float price);
	
	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年10月14日下午5:59:22
	 * @param uids
	 * @return
	 */
	public List<DeclareExtraPo> selectListBySheetUids(@Param("uids")String[] uids);
	
}
