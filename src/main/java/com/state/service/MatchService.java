package com.state.service;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import com.state.exception.MsgException;
import com.state.po.LineLimitPo;
import com.state.po.PathResultPo;
import com.state.po.ResultPo;
import com.state.po.extend.JsonMsg;



/**
 * 申报service
 * @author 帅
 *
 */
public interface MatchService {
	
	/**
	 * 根据通道名、类型查询通道结果
	 * @param mpath
	 * @param dtype
	 * @return
	 */
	public PathResultPo getPathResult(String mpath, String dtype,String time);

	/**
	 * 一键发布
	 */
	public void issue();
    /**
     * 创建bat文件
     */
	public String createFile(String path,String time,String dtype);
	/**
     * 读取bat文件
     */
	public String readFile(String path,String time) throws MsgException;
	/**
     * 查询bat文件是否成功
     */
	public String selectProgramInfo(String string);
	/**
     * 根据时间读取联络线限额
     */
	public List<LineLimitPo> getLineLimitList(String time);
	/**
     * 判断申报单类型是否一致
     */
	public String getCheckDtype(String time);
	/**
	 * 调用c之后解析result数据，把clearDetail数据解析成json并在jsp/issue/data下创建json文件
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年8月29日下午4:46:45
	 * @param dtime
	 * @param mdate
	 * @param fileName
	 */
	public void createClearDetailJsonFile(String dtime, String mdate, String fileName);
	public String createLimitFile(String path,String mcorhr, String time, LineLimitPo po3);
	public String createDeclareFile(String path, String area, String time, List<Map<String, Object>> data);
	
	/**
	 * 导出Result数据,并生成.eh头文件
	 * @author 车斯剑
	 * @date 2016年9月19日下午4:20:39
	 * @param path
	 * @param area
	 * @param time
	 * @param datas
	 * @return
	 */
	public String createResultFile(String path, String time,String mdate) ;

	/**
	 * 根据小邮件导出通道剩余容量
	 * @description
	 * @author 大雄
	 * @date 2016年9月20日下午4:29:01
	 */
	public JsonMsg exportMpathLimit(String time);
	/**
	 * 根据小邮件导出通道剩余容量
	 * @description
	 * @author 大雄
	 * @date 2016年9月20日下午4:29:01
	 */
	public JsonMsg localExportMpathLimit(String time);
	/**
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年11月24日上午11:06:28
	 * @param path
	 * @param shortTime 20161111
	 * @param time 2016-11-11
	 * @return
	 * @throws IOException
	 */
	public String createLimitLineFile(String path, String shortTime, String time) throws IOException;
	
}
