package com.state.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.state.po.LineDefinePo;

public interface ILineService {

	/**
	 * 获取所有的联络线定义数据
	 * @return
	 */
	public List<LineDefinePo> getAllLine();
	
	/**
	 * 根据联络线名判断联络线是否存在
	 * @param mcorhr
	 * @return
	 */
	public boolean existsLine(String mcorhr);
	
	/**
	 * 添加联络线
	 * @param lineDefine
	 */
	public void addLine(LineDefinePo lineDefine);

	/**
	 * 删除联络线
	 * @param mcorhr
	 */
	public void deleteLine(String mcorhr);
	/**
	 * 统计联络线个数
	 * @param mcorhr
	 */
	public int getLineNum();

	public List<LineDefinePo> getAllLineByArea(String area);

	/**
	 * @author 车斯剑
	 * @param lineId
	 * @return
	 */
	public LineDefinePo getLineDefineById(String lineId);

	public void updateLineDefine(LineDefinePo bean);

	public LineDefinePo getLineDefine(String key);


	
	
}
