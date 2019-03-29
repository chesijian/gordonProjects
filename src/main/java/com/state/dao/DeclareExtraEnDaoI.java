package com.state.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.state.po.DeclareDataPo;
import com.state.po.DeclareExtraEnPo;
import com.state.po.DeclareExtraPo;

/**
 * 对加密表的处理
 * @description
 * @author 大雄
 * @date 2016年10月12日下午4:37:45
 */
public interface DeclareExtraEnDaoI {
	
	
	/**
	 * 插入
	 * @description
	 * @author 大雄
	 * @date 2016年8月16日下午5:19:49
	 * @param po
	 */
	public void insert(DeclareExtraEnPo po);
	
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
	public List<DeclareExtraEnPo> selectListBySheetUid(@Param("sheetUid")String sheetUid,@Param("price")Float price);
	
}
