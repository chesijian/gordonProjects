package com.state.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.state.dao.IDeclareDao;
import com.state.dao.IssueDao;
import com.state.dao.TransTielineDao;
import com.state.enums.Enums_SystemConst;
import com.state.po.AreaReportBean;
import com.state.po.DeclarePo;
import com.state.po.LineReportBean;
import com.state.po.ResultPo;
import com.state.po.TransTielinePo;
import com.state.po.UserPo;
import com.state.service.IDeclareService;
import com.state.service.ILineService;
import com.state.service.IssueService;
import com.state.service.ServiceHelper;
import com.state.util.CommonUtil;
import com.state.util.DeclareUtil;
import com.state.util.IssueUtil;
import com.state.util.SessionUtil;
import com.state.util.TimeUtil;
import com.state.util.office.ExportExcel;

@Controller
@RequestMapping("/report")
public class ReportController {
	private static final transient Logger log = Logger
			.getLogger(DeclareController.class);
	@Autowired
	private IssueService issueService;
	@Autowired
	private IDeclareService declareService;
	@Autowired
	private ILineService lineService;
	
	/**
	 * 跳转统计页面,默认展示年度统计
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/init")
	public String init(Model model,String startTime,String endTime,String type) {
		log.info("@ init declare ");
		/*List<AreaReportBean> areaList=new ArrayList<AreaReportBean>();
		if(startTime!=null){
			startTime=startTime.replace("-", "");
		}
		if(endTime!=null){
			endTime=endTime.replace("-", "");
		}
		areaList=issueService.getAreaReportByYear(startTime,endTime);*/
		model.addAttribute("type", "year");
		model.addAttribute("str", "area");
		model.addAttribute("jspType", "report");
		//获取当前服务器时间
		model.addAttribute("serviceDate", TimeUtil.getStringDate());
		return "report1";
	}
	/**
	 * 获取统计数据
	 * @author 车斯剑
	 * @date 2016年10月10日上午11:11:30
	 * @param startTime
	 * @param endTime
	 * @param type
	 * @param str
	 * @param pageIndex
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = "/getData")
	@ResponseBody
	public Map<String, Object> getData1(String startTime,String endTime,String type,String str,int pageIndex,int limit) {
		log.info("@ init reportArea ");
		
		List<AreaReportBean> list=new ArrayList<AreaReportBean>();
		Integer totalCount =1;
		Map<String, Object> map=new HashMap<String, Object>();
		if(startTime!=null){
			startTime=startTime.replace("-", "");
		}
		if(endTime!=null){
			endTime=endTime.replace("-", "");
		}
		list=issueService.getAreaReportByYear(startTime,endTime,(pageIndex-1)*limit, pageIndex*limit);
		totalCount=issueService.getAreaReportByYearCount(startTime,endTime);
		
		if(totalCount==null){
			totalCount=0;
		}
		long totalPage = Math.max((totalCount+limit-1)/limit, 1);
		map.put("str", str);
		map.put("list", list);
		map.put("totalPage",totalPage);
		return map;
	}
	/**
	 * Excel导出
	 * @param request
	 * @param response
	 * @param mdate
	 */
	@RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
	public void exportExcel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime,String str,String type) {

		if(startTime!=null){
			startTime=startTime.replace("-", "");
		}
		if(endTime!=null){
			endTime=endTime.replace("-", "");
		}
		if(str.equals("area")){
			ExportExcel<AreaReportBean> ex = new ExportExcel<AreaReportBean>();
			String[] headers = { "地区", "交易电量", "成交笔数" };
			List<AreaReportBean> dataset = new ArrayList<AreaReportBean>();
			if(type!=null && type.equals("month")){
				dataset=issueService.getAreaReportByMonth(startTime,endTime);
			}else{
				dataset=issueService.getAreaReportByYear(startTime,endTime,0,10000);
			}
			try {
				String zipname = "地区信息.xls";
				OutputStream os = response.getOutputStream();
				int len = 0;
				byte buf[] = new byte[1024];// 缓存作用
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/octet-stream; charset=UTF-8");
				response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(zipname.getBytes("GB2312"), "ISO8859-1") + "\";");//
				ex.exportAreaExcel(headers, dataset, os);
				os.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(str.equals("line")){
			ExportExcel<LineReportBean> ex = new ExportExcel<LineReportBean>();
			String[] headers = { "联络线名称", "交易电量", "上限电量", "下限电量", "计划电量", "剩余能力","利用率", "说明" };
			List<LineReportBean> dataset = new ArrayList<LineReportBean>();
			if(type!=null && type.equals("month")){
				dataset=issueService.getLineReportByMonth(startTime,endTime);
				
			}else{
				dataset=issueService.getLineReportByYear(startTime,endTime);
			}
			try {
				String zipname = "联络线信息-.xls";
				OutputStream os = response.getOutputStream();
				int len = 0;
				byte buf[] = new byte[1024];// 缓存作用
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/octet-stream; charset=UTF-8");
				response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(zipname.getBytes("GB2312"), "ISO8859-1") + "\";");
				ex.exportExcel(headers,dataset, os);
				os.close();
				System.out.println("excel导出成功！");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * 跳转到交易单展示界面
	 * @author 车斯剑
	 * @date 2016年12月13日下午5:48:44
	 * @param model
	 * @param mdate
	 * @return
	 */
	@RequestMapping(value = "/tradeMap")
	public ModelAndView initTrade(Model model,String mdate) {
		log.info("@ init initTrade ");
		
		ModelAndView view = new ModelAndView("issue/trade", null);
		return view;
	}
	
	/**
	 * 获取交易单展示左边树
	 * @author 车斯剑
	 * @date 2016年12月13日下午5:48:40
	 * @param request
	 * @param area
	 * @param time
	 * @return
	 */
	@RequestMapping(value = "/loadTradeTree", method = RequestMethod.POST)
	public List<String> loadDailyTree(HttpServletRequest request, String time) {
		List<String> result = issueService.getAreaForTradeByTime(time);
		return result;
	}

	/**
	 * 获取交易单数据
	 * @author 车斯剑
	 * @date 2016年12月13日下午7:27:53
	 * @param request
	 * @param response
	 * @param time
	 * @return
	 */
	@RequestMapping(value = "/getTradeDatas", method = RequestMethod.POST)
	public Map<String,Object> getTradeDatas(String ddate) {
		
		Map<String,Object> result = new HashMap<String, Object>();
		//日期时间
		String dateStr =""; 
		Date d = TimeUtil.strToDate(ddate);
		try {
			dateStr = TimeUtil.toStrDateFromUtilDateByFormat(d, "yyyy年MM月dd日");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String time = ddate.replaceAll("-", "");
		String area = null;
		if(!SessionUtil.isState()){
			//如果不是国调则导出对应的省
			area = SessionUtil.getArea();
		}
		List<DeclarePo> pos = declareService.getListAndDataByMdate(time, area,null);
		List<ResultPo> list = ServiceHelper.getBean(IssueDao.class).selectResultListByParam(area, time, null);
		List<TransTielinePo> pathData = ServiceHelper.getBean(TransTielineDao.class).getTradeData(time,area);
		List<TransTielinePo> executeData = ServiceHelper.getBean(IssueDao.class).getExecuteDataForArea(time,"",null);
		/*if (pos == null || pos.size() == 0) {
			throw new MsgException("没有申报数据");
		}
		if (list == null || list.size() == 0) {
			throw new MsgException("没有交易结果数据");
		}
		if (executeData == null || executeData.size() == 0) {
			throw new MsgException("没有执行结果数据");
		}*/
		//封装交易结果数据
		Map<String, Map<String, Map<String, ResultPo>>> resultMap = new HashMap<String, Map<String, Map<String, ResultPo>>>();
		Map<String, Map<String, ResultPo>> areaResultM = null;
		Map<String, ResultPo> resultM = null;
		
		Map<String, ResultPo> salePrice = new HashMap<String, ResultPo>();
		for (ResultPo po : list) {
			if("sale".equals(po.getDrole()) && "电价".equals(po.getDtype()) && "汇总值".equals(po.getCorridor())){
				salePrice.put(po.getArea(), po);
			}
		}
		for (ResultPo po : list) {
			if (po.getSumQ()==0 && !"汇总值".equals(po.getCorridor())) {
				continue;
			}
			if(!"汇总值".equals(po.getCorridor())){
				if (resultMap.containsKey(po.getArea())) {
					areaResultM = resultMap.get(po.getArea());
				} else {
					areaResultM = new HashMap<String, Map<String,ResultPo>>();
					resultMap.put(po.getArea(), areaResultM);
					
				}
				if(areaResultM.containsKey(po.getCorridor())){
					resultM = areaResultM.get(po.getCorridor());
				}else{
					resultM = new HashMap<String, ResultPo>();
					areaResultM.put(po.getCorridor(), resultM);
				}
				
				if(salePrice.containsKey(po.getArea())){
					resultM.put("电价", salePrice.get(po.getArea()));
					resultM.put(po.getDtype(), po);
					
				}else{
					resultM.put(po.getDtype(), po);
				}
			}
			
		
		}
		//System.out.println("resultMap==="+CommonUtil.objToJson(resultMap));
		
		//计算结果
		List<Map<String,List<Map<String, Float>>>> declareData = new ArrayList<Map<String,List<Map<String,Float>>>>();
		List<Map<String,Map<String, Map<String,Double>>>> dealData = new ArrayList<Map<String,Map<String,Map<String,Double>>>>();
		Map<String,List<Map<String, Float>>>  declareMap = null;
		Map<String,Map<String, Map<String,Double>>> dealMap = null;
		List<String> areas = new ArrayList<String>();
		List<String> userIds = new ArrayList<String>();
		//通道、执行结果数据
		List<Map<String,Map<String,String>>> executeDatas = new ArrayList<Map<String,Map<String,String>>>();
		List<Map<String,Map<String,String>>> pathDatas = new ArrayList<Map<String,Map<String,String>>>();
		Map<String,Map<String,String>> exceteMap =null;
		Map<String,Map<String,String>> pathMap =null;
		for (DeclarePo po : pos) {
			declareMap = DeclareUtil.getDeclareTradeData(po);
			declareData.add(declareMap);
			//获取签名
			List<String> userIdList = declareService.getUserIdList(String.valueOf(po.getId()));
			if(userIdList==null||userIdList.size()==0){
				userIds.add("");
			}else{
				userIds.add(userIdList.get(0));
			}
			areas.add(po.getArea());
			//成交结果数据
			areaResultM = resultMap.get(po.getArea());
			if(areaResultM!=null){
				dealMap = IssueUtil.getdealTradeData(areaResultM);
				dealData.add(dealMap);
			}else{
				dealData.add(null);
			}
						
		}
		List<TransTielinePo> lineExecuteDatas = null;
		List<TransTielinePo> lineDatas = null;
		Map<String,List<TransTielinePo>> linePathMap = new HashMap<String, List<TransTielinePo>>();
		Map<String,List<TransTielinePo>> executeMap = new HashMap<String, List<TransTielinePo>>();
		List<String> linePaths = new ArrayList<String>();
		List<String> lineExecutes = new ArrayList<String>();
		
		//System.out.println("executeData======"+CommonUtil.objToJson(executeData));
		for (TransTielinePo tielinePo : executeData) {
			if(!executeMap.isEmpty() && executeMap.containsKey(tielinePo.getTielineName())){
				lineExecuteDatas = executeMap.get(tielinePo.getTielineName());
			}else{
				
				lineExecuteDatas = new ArrayList<TransTielinePo>();
				executeMap.put(tielinePo.getTielineName(), lineExecuteDatas);
			}
			lineExecuteDatas.add(tielinePo);
		}
		
		for (TransTielinePo tielinePo : pathData) {
			if(linePathMap.containsKey(tielinePo.getTielineName())){
				lineDatas = linePathMap.get(tielinePo.getTielineName());
				
			}else{
				lineDatas = new ArrayList<TransTielinePo>();
				linePathMap.put(tielinePo.getTielineName(), lineDatas);
			}
			lineDatas.add(tielinePo);
		}
		//计算
		for (Entry<String,List<TransTielinePo>> entry : linePathMap.entrySet()) {
			pathMap =IssueUtil.getExecuteTradeData(entry.getValue());
			pathDatas.add(pathMap);
			linePaths.add(entry.getKey());
		}
		for (Entry<String,List<TransTielinePo>> entry : executeMap.entrySet()) {
			exceteMap =IssueUtil.getExecuteTradeData(entry.getValue());
			executeDatas.add(exceteMap);
			lineExecutes.add(entry.getKey());
		}
		//获取出清发布的签名
		String issuerId = "";
		List<UserPo> issueUserGraList = ServiceHelper.getBean(IDeclareDao.class).getUserGraPhPoListByMdateAndType(time, Enums_SystemConst.SIGN_TYPE_ISSUE.getValue());
		if(issueUserGraList!=null && issueUserGraList.size()>0){
			issuerId = issueUserGraList.get(0).getId();
		}
		
		
		result.put("dateStr", dateStr);
		result.put("areas", areas);
		result.put("userIds", userIds);
		result.put("declareData", declareData);
		result.put("dealData", dealData);
		result.put("linePaths", linePaths);
		result.put("lineExecutes", lineExecutes);
		result.put("pathData", pathDatas);
		result.put("executeData", executeDatas);
		result.put("issuerId", issuerId);
		return result;
		
	}
}
