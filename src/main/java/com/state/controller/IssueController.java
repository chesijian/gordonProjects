package com.state.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.state.enums.Enums_SessionType;
import com.state.enums.Enums_SystemConst;
import com.state.exception.MsgException;
import com.state.po.DeclarePo;
import com.state.po.LineDefinePo;
import com.state.po.PathResultPo;
import com.state.po.ResultPo;
import com.state.po.SdTotalCostPo;
import com.state.po.SdcostResultPo;
import com.state.po.TransTielinePo;
import com.state.po.TreeBean;
import com.state.po.UserPo;
import com.state.po.WeekDataPo;
import com.state.po.extend.JsonMsg;
import com.state.po.sys.PfSysConfigPo;
import com.state.service.AreaService;
import com.state.service.DealResultServiceI;
import com.state.service.IDeclareService;
import com.state.service.ILineService;
import com.state.service.IPathService;
import com.state.service.IWeekDataService;
import com.state.service.IssueService;
import com.state.service.LineLimitService;
import com.state.service.MatchService;
import com.state.service.PfLoggerServiceI;
import com.state.service.ServiceHelper;
import com.state.service.ShellExecuteServiceI;
import com.state.service.SurveyServiceI;
import com.state.service.SystemServiceI;
import com.state.util.AreaCountBean;
import com.state.util.BillCountBean;
import com.state.util.CommonUtil;
import com.state.util.DatUtil;
import com.state.util.FileManager;
import com.state.util.JFreeChartUtil;
import com.state.util.LoggerUtil;
import com.state.util.ResultUtil;
import com.state.util.SessionUtil;
import com.state.util.TimeUtil;
import com.state.util.office.ExportExcel;
import com.state.util.office.ExportExcelUtil;
import com.state.util.office.MyWordUtil;
import com.state.util.office.WordUtil;
import com.state.util.sys.SystemConstUtil;

@Controller
@RequestMapping("/issue")
public class IssueController {
	private static final transient Logger log = Logger.getLogger(IssueController.class);
	private static final String MODEL_NAME_ = "发布";
	@Autowired
	private DealResultServiceI dealResultService;
	@Autowired
	private IssueService issueService;
	@Autowired
	private IPathService pathService;
	@Autowired
	private LineLimitService lineLimitService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private MatchService matchService;
	@Autowired
	private IDeclareService declareService;
	@Autowired
	private ILineService lineService;
	
	@Autowired
	private IWeekDataService weekDataService;
	@Autowired
	private SystemServiceI systemService;
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 跳转发布页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/init")
	public String init(Model model) {
		log.info("@ init issue ");
		
		Object o = SessionUtil.getAttribute(Enums_SessionType.IS_MATCH.getValue());
		boolean isMatch = false;
		if (o != null) {
			isMatch = (Boolean) o;
		}
		SessionUtil.setAttribute(Enums_SessionType.IS_MATCH.getValue(), false);
		
		// model.addAttribute("areaList", JSON.toJSON(areaService.getAllArea()).toString());
		model.addAttribute("jspType", "issue");
		model.addAttribute("isMatch", isMatch);
		//获取当前服务器时间
		model.addAttribute("serviceDate", TimeUtil.getStringDate());
		return "issue";
	}

	/**
	 * 获取SheetCostSum值
	 * @author 车斯剑
	 * @date 2016年12月7日上午9:37:31
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getSheetCostSum")
	@ResponseBody
	public Map<String, Object> getSheetCostSum(String time) {
		
		Map<String, Object> sheetMap = null;
		PfSysConfigPo sysConfig = systemService.getSysConfig("SheetCostSum_"+time);
		if(sysConfig != null){
			String value = sysConfig.getValue();
			if(CommonUtil.ifEmpty(value)){
				sheetMap = CommonUtil.jsonToMapObj(value);
			}
		}
		
		return sheetMap;
	}
	
	/**
	 * 获取clear信息
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年8月29日下午11:02:53
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/initClear")
	public ModelAndView initClear(Model model) {
		log.info("@ init initClear ");
		ModelAndView view = new ModelAndView("issue/clear", null);
		return view;
	}

	/**
	 * 地图展示通道信息
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月17日下午12:12:09
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/initMap")
	public ModelAndView initMap(Model model) {
		log.info("@ init initMap ");
		UserPo info = SessionUtil.getUserPo();
		String time = CommonUtil.getSimpleDate();
		StringBuilder sb = new StringBuilder(time);
		if (info != null) {
			time = info.getMatchDate();
		} else {
			time = sb.toString();
		}
		time = time.replaceAll("-", "");
		
		Map<String, Map<String, Map<String, Object>>> data = pathService.getResultByMdateAndDtype(time);
		Map<String, List<Vector<Map<String, Object>>>> result = new HashMap<String, List<Vector<Map<String, Object>>>>();
		List<Vector<Map<String, Object>>> list = null;
		int colors[] = { 10,25, 40, 55,70,85, 100 };
		
		int i = 0;
		List<Map<String,Object>> areas = new ArrayList<Map<String,Object>>();
		List<String> areaList = new ArrayList<String>();
		Map<String,Object> areaMap = null;
		for (Entry<String, Map<String, Map<String, Object>>> mpath : data.entrySet()) {

			float smoothness = 0.5f;
			if (mpath.getKey().equals("复奉直流")) {
				smoothness = 0.6f;
			} else if (mpath.getKey().equals("锦苏直流")) {
				smoothness = 0.3f;
			} else if (mpath.getKey().equals("宾金直流")) {
				smoothness = 0.1f;
			}

			LineDefinePo lineDefinePo = lineService.getLineDefine(mpath.getKey());
			if(lineDefinePo==null){
				continue;
			}
			if(!areaList.contains(lineDefinePo.getStartArea())){
				areaMap =new HashMap<String, Object>();
				areaList.add(lineDefinePo.getStartArea());
				areaMap.put("name", lineDefinePo.getStartArea());
				areaMap.put("value", colors[i%7]);
				areas.add(areaMap);
				i++;
			}
			if(!areaList.contains(lineDefinePo.getEndArea())){
				areaMap =new HashMap<String, Object>();
				areaList.add(lineDefinePo.getEndArea());
				areaMap.put("name", lineDefinePo.getEndArea());
				areaMap.put("value",colors[i%7]);
				areas.add(areaMap);
				i++;
			}
			
			for (int j = 1; j < 97; j++) {
				String key = String.valueOf(j);
				if (result.containsKey(key)) {
					list = result.get(key);
				} else {
					list = new ArrayList<Vector<Map<String, Object>>>();
					result.put(key, list);
				}
				Vector<Map<String, Object>> line1 = new Vector<Map<String, Object>>();
				Map<String, Object> line1Start = new HashMap<String, Object>();
				Map<String, Object> line1End = new HashMap<String, Object>();

				if(!CommonUtil.ifEmpty(data.get(mpath.getKey()).get(key).get("汇总值"))){
					continue;
				}
				Map<String,Object> temp = data.get(mpath.getKey()).get(key);
				
				line1Start.put("name", lineDefinePo.getStartArea());
				line1Start.put("smoothness", smoothness);
				line1Start.put("path", mpath.getKey());
				line1Start.put("mcorhrTemp",temp);
				
				line1End.put("name", lineDefinePo.getEndArea());
				line1End.put("value", colors[i%7]);

				line1.add(line1Start);
				line1.add(line1End);
				list.add(line1);
			}
			//i++;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", CommonUtil.objToJson(data));
		map.put("result", CommonUtil.objToJson(result));
		map.put("areas", CommonUtil.objToJson(areas));
		ModelAndView view = new ModelAndView("issue/map", map);
		return view;
	}

	/**
	 * 获取发布单列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getResultList", method = RequestMethod.POST)
	public List<DeclarePo> getResultList(HttpServletRequest request, String area, String time) {

		if (area == null || "".equals(area)) {
			UserPo user = (UserPo) request.getSession().getAttribute("userInfo");
			area = user.getArea();
		}
		return issueService.getResultNameList(area, time);
	}

	/**
	 * 根据地区获取发布单数据汇总
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getResultByArea", method = RequestMethod.POST)
	public Map getResultByArea(String area, String time) {
		Map map = new HashMap<String, Object>();
		ResultPo r = issueService.getResultByArea(area, time);
		List<AreaCountBean> priceCount = new ArrayList<AreaCountBean>();
		List<SdTotalCostPo> sdPrice = issueService.getSdAreaMeg(time);// 费用汇总
		for (SdTotalCostPo sdcp : sdPrice) {
			if (sdcp != null) {
				if (sdcp.getArea().equals(area)) {
					AreaCountBean acb = new AreaCountBean();
					acb.setArea(sdcp.getArea());
					acb.setFdfy(sdcp.getFdfy());
					acb.setClear(sdcp.getClearQJ());
					acb.setSumPrice(sdcp.getSdfy());
					acb.setSumQ(r.getSumQ());
					acb.setDtype(r.getDtype());
					priceCount.add(acb);
					map.put("areaPrice", acb);
				}
			}
		}
		map.put("areaData", r);
		return map;
	}
	
	/**
	 * 发布结果页面
	 * @param request
	 * @param response
	 * @param model
	 * @param readOnly
	 * @return
	 */
	@RequestMapping(value = "/issueResult")
	public ModelAndView issueResult(HttpServletRequest request, HttpServletResponse response, String time) {
		log.info("跳转到结果发布界面");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", SessionUtil.getUserPo().getId());
		map.put("time", time);
		request.getSession().setAttribute("isSignResult", "false");
		SessionUtil.getSession().setAttribute("index", "0");
		//获取当前服务器时间
		map.put("serviceDate", TimeUtil.getStringDate());
		ModelAndView view = new ModelAndView("issue/issueDialog", map);
		
		return view;
	}
	
	/**
	 * 执行结果保存页面
	 * @param request
	 * @param response
	 * @param model
	 * @param readOnly
	 * @return
	 */
	@RequestMapping(value = "/declResult")
	public ModelAndView declResult(HttpServletRequest request, HttpServletResponse response, String time) {
		log.info("跳转到结果发布界面");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", SessionUtil.getUserPo().getId());
		map.put("time", time);
		request.getSession().setAttribute("isDeclResult", "false");
		SessionUtil.getSession().setAttribute("index", "0");
		ModelAndView view = new ModelAndView("issue/declResultDialog", map);
		
		return view;
	}

	/**
	 * 根据申报单号、类型查找发布单
	 * 
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping(value = "/loadTree", method = RequestMethod.POST)
	public Object loadTree(HttpServletRequest request, String area, String time, String type) {
		List<TreeBean> list = null;
		Integer status = null;
		// 如果是非国调用户
		if (!SessionUtil.isState()) {
			status = 1;
		}
		// 地区送点侧受电侧出清数据
		if (type.equals("result")) {

			list = issueService.getTree(area, time, status);

			/**/
			Collections.sort(list, new Comparator<TreeBean>() {
				public int compare(TreeBean arg0, TreeBean arg1) {
					
					if(arg0.getSort()!=null && arg1.getSort()!=null){
						return arg0.getSort()-arg1.getSort();
					}else{
						return arg0.getName().compareTo(arg1.getName());
					}
					
				}
			});


		}else if (type.equals("dealResult")) {
			list = dealResultService.getTree(area, time, status);
			if(list != null){
				Collections.sort(list, new Comparator<TreeBean>() {
					public int compare(TreeBean arg0, TreeBean arg1) {
						if (arg0.getName().equals("四川")) {
							return -1;
						} else if (arg0.getName().equals("上海")) {
							return 1;
						} else {
							return arg0.getName().compareTo(arg1.getName());
						}
					}
				});
			}else{
				list = new ArrayList<TreeBean>();
			}
			
		} else if (type.equals("lineLimit")) {
			if(SessionUtil.isState()){
				area = null;
			}else{
				status=1;
			}
			//System.out.println("-----"+status);
			list = lineLimitService.getTree(area, time, status);
		} else {
			// pathResult通道出清
			list = pathService.getTree(time, status);
		}
		// if(list == null){
		// list = new ArrayList<TreeBean>();
		// }
		// System.out.println("======="+JSON.toJSON(list));
		/*
		 * List treeNameList=new ArrayList();
		 * //System.out.println("=========="); String
		 * path=request.getSession().getServletContext().getRealPath("");
		 * List<DeclarePo> sheetList=issueService.getAreaTree(time);
		 * //System.out.println(CommonUtil.objToJson(sheetList)); String
		 * parentId=""; int num=1; if(area.equals("国调")){//国调用户显示所有 list.add(new
		 * TreeBean("1","0","全部汇总",true)); list.add(new
		 * TreeBean("11","1","申报单汇总")); list.add(new TreeBean("13","1","费用汇总"));
		 * list.add(new TreeBean("2","0","各省份汇总",true)); list.add(new
		 * TreeBean("3","2","买方",true)); list.add(new
		 * TreeBean("4","2","卖方",true)); for(int i=0;i<sheetList.size();i++){
		 * if(!treeNameList.contains(sheetList.get(i).getArea()) &&
		 * !treeNameList.contains(sheetList.get(i).getMname())){
		 * if(sheetList.get(i).getDrloe().equals("buy")){ list.add(new
		 * TreeBean("3"+(num),"3",sheetList.get(i).getArea()));
		 * parentId="3"+(num); list.add(new
		 * TreeBean("3"+(num+1),parentId,sheetList
		 * .get(i).getMname(),false,path+"/css/img/tree_file.gif"));
		 * treeNameList.add(sheetList.get(i).getArea());
		 * treeNameList.add(sheetList.get(i).getMname()); }else
		 * if(sheetList.get(i).getDrloe().equals("sale")){ list.add(new
		 * TreeBean("4"+(num),"4",sheetList.get(i).getArea()));
		 * parentId="4"+(num); list.add(new
		 * TreeBean("4"+(num+1),parentId,sheetList
		 * .get(i).getMname(),false,path+"/css/img/tree_file.gif"));
		 * treeNameList.add(sheetList.get(i).getArea());
		 * treeNameList.add(sheetList.get(i).getMname()); }
		 * 
		 * }else{ list.add(new
		 * TreeBean("3"+(num),parentId,sheetList.get(i).getMname
		 * (),false,path+"/css/img/tree_file.gif"));
		 * treeNameList.add(sheetList.get(i).getMname()); } num++; }
		 * }else{//省级用户只展示当前用户 for(int i=0;i<sheetList.size();i++){
		 * if(sheetList.get(i).getArea().equals(area)){//取出当前用户数据
		 * if(!treeNameList.contains(sheetList.get(i).getArea()) &&
		 * !treeNameList.contains(sheetList.get(i).getMname())){ list.add(new
		 * TreeBean("2"+(num),"2",sheetList.get(i).getArea(),true));
		 * parentId="2"+(num); list.add(new
		 * TreeBean("2"+(num+1),parentId,sheetList.get(i).getMname()));
		 * treeNameList.add(sheetList.get(i).getArea());
		 * treeNameList.add(sheetList.get(i).getMname()); }else{ list.add(new
		 * TreeBean("2"+(num),parentId,sheetList.get(i).getMname()));
		 * treeNameList.add(sheetList.get(i).getMname()); } num++; }
		 * 
		 * } }
		 */

		if (list == null) {
			return "[]";
		} else {
			return JSON.toJSON(list);
		}
		// JSON.toJSON(list)

	}
	
	/**
	 * 通过调用c程序获取执行结果数据
	 * @description
	 * @author 大雄
	 * @date 2016年12月20日上午9:42:36
	 * @param request
	 * @param response
	 * @param time
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getDealResultFromInterface")
	public JsonMsg getDealResultFromInterface(HttpServletRequest request,HttpServletResponse response,String time) {
		log.info("通过调用c程序获取执行结果数据");
		return ServiceHelper.getBean(ShellExecuteServiceI.class).getDealResultFromInterface(false, time);
		
			//return j;
		

	}

	/**
	 * 
	 * 发布
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateIssue")
	public JsonMsg updateIssue(HttpServletRequest request,HttpServletResponse response,String time) {
		log.info("结果发布");
			//return j;
		return ServiceHelper.getBean(ShellExecuteServiceI.class).updateIssue(false, request, time);
		

	}

	/**
	 * 根据申报单号、类型查找发布单
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getResult", method = RequestMethod.POST)
	public Map getResult(String dsheet, String time) {
		Map map = new HashMap<String, Object>();
		ResultPo r = issueService.getResultBySheetId(dsheet, time);
		if (r != null) {
			DeclarePo dp = issueService.getDeclarePoById(r.getDsheet());
			List<BillCountBean> billCount = new ArrayList<BillCountBean>();
			List<SdcostResultPo> sdBill = issueService.geSdBillMeg(time);// 申报单汇总
			for (SdcostResultPo sdpo : sdBill) {
				BillCountBean bcb = new BillCountBean();
				bcb.setArea(sdpo.getArea());
				bcb.setSheetName(sdpo.getdSheet());
				bcb.setClear(sdpo.getClearQJ());
				bcb.setSumPrice(sdpo.getSdfy());
				bcb.setFdfy(sdpo.getFdfy());
				bcb.setSumQ(sdpo.getSumQ());
				if (r.getDtype().equals("1a")) {
					bcb.setDtype("全天");
				} else if (r.getDtype().equals("2a")) {
					bcb.setDtype("高峰");
				} else if (r.getDtype().equals("3a")) {
					bcb.setDtype("低谷");
				}
				billCount.add(bcb);
			}
			for (BillCountBean bb : billCount) {
				// System.out.println(bb.getSheetName()+"======"+dp.getSheetName());
				if (bb.getSheetName().equals(dp.getSheetName())) {
					map.put("count", bb);
				}
			}
			map.put("state", dp.getClearstate().trim());
			map.put("detail", r);
		}
		// System.out.println(map);
		return map;
	}

	/**
	 * 发布界面单击左侧id获取result集
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年8月27日下午1:41:03
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getResultData", method = RequestMethod.POST)
	public Object getResultData(String id, String time, String type) {
		Map map = new HashMap<String, Object>();
		 //System.out.println(type);

		if (type.equals("result")) {
			ResultPo po = issueService.getResultById(id);
			return po;
		} else if (type.equals("dealResult")) {
			ResultPo po = dealResultService.getResultById(id);
			return po;
		} else if (type.equals("lineLimit")) {
			String status = null;
			if (!SessionUtil.isState()) {
				status = "1";
			}
			time = time.replaceAll("-", "");
			//LineLimitPo tradeData = lineLimitService.selectLineLimitList(id, time, Enums_DeclareType.DATATYPE_TRADING_POWER, status);
			TransTielinePo tradeData = lineLimitService.getTransTielineById(id);
			return tradeData;
		} else {
			// pathResult通道出清
			PathResultPo po = pathService.getResultById(id);
			return po;
		}
	}

	/**
	 * 申报单汇总
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getBillCountMeg", method = RequestMethod.POST)
	public List<BillCountBean> getBillCountMeg(String time) {
		List<BillCountBean> billCount = new ArrayList<BillCountBean>();
		List<SdcostResultPo> sdBill = issueService.geSdBillMeg(time);// 申报单汇总
		for (SdcostResultPo sdpo : sdBill) {
			BillCountBean bcb = new BillCountBean();
			bcb.setArea(sdpo.getArea());
			bcb.setSheetName(sdpo.getdSheet());
			bcb.setClear(sdpo.getClearQJ());
			bcb.setSumPrice(sdpo.getSdfy());
			bcb.setFdfy(sdpo.getFdfy());
			bcb.setSumQ(sdpo.getSumQ());
			billCount.add(bcb);
		}
		return billCount;
	}

	/**
	 * 费用汇总
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getPriceCountMeg", method = RequestMethod.POST)
	public List<AreaCountBean> getPriceCountMeg(String time) {
		List<AreaCountBean> priceCount = new ArrayList<AreaCountBean>();
		List<SdTotalCostPo> sdPrice = issueService.getSdAreaMeg(time);// 费用汇总
		for (SdTotalCostPo sdcp : sdPrice) {
			AreaCountBean acb = new AreaCountBean();
			acb.setArea(sdcp.getArea());
			acb.setFdfy(sdcp.getFdfy());
			acb.setClear(sdcp.getClearQJ());
			acb.setSumPrice(sdcp.getSdfy());
			priceCount.add(acb);
		}
		return priceCount;
	}

	/**
	 * 导出出清明细
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
	public void exportExcel(HttpServletRequest request, HttpServletResponse response, String mdate) {

		try {
			String path = SystemConstUtil.getMatchPath();
			String fileName = path + File.separator + "data" + File.separator + Enums_SystemConst.RESULT_FILE_NAME + mdate + ".dat";
			Vector<Vector<String>> data = null;
			try {
				data = DatUtil.readFile(new File(fileName), "<ClearResult::国调 type=全数>", "</ClearResult::国调 type=全数>");
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new MsgException("读取数据出错！");
			}
			String zipname = "出清结果" + mdate + ".xls";
			OutputStream os = response.getOutputStream();
			int len = 0;
			byte buf[] = new byte[1024];// 缓存作用
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/octet-stream; charset=UTF-8");
			response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(zipname.getBytes("GB2312"), "ISO8859-1") + "\";");//
			String[] headers = { "序号", "时段数", "卖单区域", "卖单名称", "卖单段数", "申报单剩余电力", "申报电价", "送端出清电力","送端出清电价","买单区域", "买单名称", "买单段数", "申报剩余电力", "申报电价", 
					"网损","受端电力","受端出清电力","受端出清电价","价差", "送端费用","输电费用","受端费用","分段费用","分段电力", "通道路径", "通道剩余容量","优先级" };
			ExportExcel.exportDataToExcel("出清信息", headers, data, os);
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据模板导出日报excel
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月12日下午1:54:53
	 * @param request
	 * @param response
	 * @param mdate
	 */
	@RequestMapping(value = "/exportDailyExcel1", method = RequestMethod.GET)
	public void exportDailyExcel1(HttpServletRequest request, HttpServletResponse response, String mdate) {

		String modelPath = request.getSession().getServletContext().getRealPath("/bat/dailyModel.xls");
		// String
		// temppath=request.getSession().getServletContext().getRealPath("/bat/");
		String time = mdate + "";
		mdate = mdate.replaceAll("-", "");
		OutputStream os = null;
		try {
			// 获取申报信息
			Map<String, Map<String, String>> sheetResult = issueService.getExcelDeclareData(mdate);
			// System.out.println(CommonUtil.objToJson(sheetResult));
			// 获取通道费用
			Map<String, Map<String, Double>> pathResult = issueService.getExcelPathResultData(mdate);
			// Map<String, Map<String, Double>> lineResult =
			// issueService.getExcelLineLimitResultData(mdate);
			// 获取每个地区的成交电量
			Map<String, Double> result = issueService.getExcelResultData(mdate);
			// System.out.println(CommonUtil.objToJson(sheetResult));
			// System.out.println(CommonUtil.objToJson(pathResult));
			// System.out.println(CommonUtil.objToJson(result));
			// excel模板路径
			File fi = new File(modelPath);
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fi));
			String[] areaArr = { "四川", "江苏", "上海", "浙江", "山东" };
			String[] pathArr = { "四川送江苏", "四川送上海", "四川送浙江", "四川送山东" };
			// String[] lineArr = {"锦苏直流","复奉直流","宾金直流","德宝直流","银东直流"};
			// 读取excel模板
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			// 读取了模板内所有sheet内容
			HSSFSheet sheet = wb.getSheetAt(0);
			// 在相应的单元格进行赋值
			// HSSFCell cell = sheet.getRow(6).getCell(6);
			// cell.setCellValue("测试");
			// 设置申报信息
			// DecimalFormat decimalFormat = new
			// DecimalFormat("#,##0.00");//格式化设置
			DecimalFormat decimalFormat = new DecimalFormat("0.00");// 格式化设置

			String area = null;
			for (int i = 0; i < 5; i++) {
				if (sheetResult.get(areaArr[i]) != null) {
					area = areaArr[i];
					// HSSFCell cell2 = sheet.getRow(5+i).getCell(10);
					// cell2.setCellValue(sheetResult.get(areaArr[i]).get("maxPrice"));
					// HSSFCell cell3 = sheet.getRow(5+i).getCell(16);
					// cell3.setCellValue(sheetResult.get(areaArr[i]).get("minPrice"));
					HSSFCell cell1 = sheet.getRow(5 + i).getCell(10);
					String ss = decimalFormat.format(Double.parseDouble(sheetResult.get(area).get("sumPower")) / 4);
					// System.out.println(double1);
					cell1.setCellValue(ss);
					HSSFCell cell2 = sheet.getRow(5 + i).getCell(19);
					ss = decimalFormat.format(result.get(area) == null ? 0 : result.get(area) / 4);
					// System.out.println(double1);
					cell2.setCellValue(ss);

				}
			}

			// 设置成交情况
			int rowIndex = 12;
			for (int i = 0; i < 4; i++) {
				String key = pathArr[i];
				Map<String, Double> item = pathResult.get(key);
				double sdSwf = (item.get("送端上网费用") == null ? 0 : item.get("送端上网费用"));
				double snSdf = (item.get("省内输电费用") == null ? 0 : item.get("省内输电费用"));
				double hzSdf = (item.get("华中输电费用") == null ? 0 : item.get("华中输电费用"));
				double xbSdf = (item.get("西北输电费用") == null ? 0 : item.get("西北输电费用"));
				double ndSdf = (item.get("宁东输电费用") == null ? 0 : item.get("宁东输电费用"));
				double dbSdf = (item.get("德宝输电费用") == null ? 0 : item.get("德宝输电费用"));
				double kqSdf = (item.get("跨区输电费用") == null ? 0 : item.get("跨区输电费用"));
				double ljfy = (item.get("从送端累加的总费用") == null ? 0 : item.get("从送端累加的总费用"));
				double sum = (item.get("受端总费用") == null ? 0 : item.get("受端总费用"));
				if (pathResult.get(key) != null) {
					HSSFCell cell2 = sheet.getRow(rowIndex + i).getCell(6);
					cell2.setCellValue(sdSwf);
					HSSFCell cell3 = sheet.getRow(rowIndex + i).getCell(9);
					cell3.setCellValue(snSdf);

					if (i == 3) {
						HSSFCell cell4 = sheet.getRow(rowIndex + i).getCell(13);
						cell4.setCellValue(hzSdf + xbSdf);
						HSSFCell cell5 = sheet.getRow(rowIndex + i).getCell(16);
						cell5.setCellValue(ndSdf + dbSdf);

					} else {
						HSSFCell cell4 = sheet.getRow(rowIndex + i).getCell(13);
						cell4.setCellValue(0);
						HSSFCell cell5 = sheet.getRow(rowIndex + i).getCell(16);
						cell5.setCellValue(kqSdf);
					}
					HSSFCell cell6 = sheet.getRow(rowIndex + i).getCell(20);
					cell6.setCellValue(ljfy);
					HSSFCell cell7 = sheet.getRow(rowIndex + i).getCell(23);
					cell7.setCellValue(sum);
				}
			}

			
			HSSFCell cell2 = sheet.getRow(2).getCell(2);
			cell2.setCellValue(time + " " + TimeUtil.getWeekOfDate(time));
			HSSFCell cell3 = sheet.getRow(16).getCell(2);
			cell3.setCellValue("  制作人： " + SessionUtil.getAddNameCn() + "                校核人：                 制作日期：" + time);

			String fileName = "可再生能源跨省区消纳现货市场模拟运行日报-" + mdate + ".xls";
			// System.out.println(fileName);
			// response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel");
			// response.setHeader("content-disposition",
			// "attachment;filename=11.xls");
			// response.setContentType("application/octet-stream; charset=UTF-8");
			response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GB2312"), "ISO8859-1") + "\";");//
			// os = response.getOutputStream();//输出流
			// fs.close();
			os = response.getOutputStream();
			wb.write(os);
			os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (os != null)
					os.close();
			} catch (IOException e) {
			}
		}

	}

	/**
	 * 发布中通过小邮件导出到各省调系统
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月19日下午11:53:58
	 * @param id
	 * @param area
	 * @param time
	 * @return
	 */
	@RequestMapping(value = "/exportData", method = RequestMethod.POST)
	@ResponseBody
	public String exportData(String id, String area, String time) {

		List<ResultPo> datas = new ArrayList<ResultPo>();
		List<ResultPo> rpos = issueService.getTreeForResult(area, time);

		for (ResultPo rpo : rpos) {
			if (!"四川".equals(rpo.getArea()) && "送电侧".equals(rpo.getSide())) {
				continue;
			}
			ResultPo po = issueService.getResultById(rpo.getId());
			datas.add(po);

		}
		//return matchService.createResultFile(Enums_SystemConst.RESULT_FILE_PATH_WIN, area, time, datas, null);
		return matchService.createResultFile(Enums_SystemConst.RESULT_FILE_PATH_WIN,time,time);
	}

	/**
	 * 本地导出
	 * 
	 * @author 车斯剑
	 * @date 2016年9月19日下午4:39:59
	 * @param request
	 * @param response
	 * @param startTime
	 * @param endTime
	 * @param str
	 * @param type
	 */
	@RequestMapping(value = "/localExportData1", method = RequestMethod.GET)
	public void localExportData1(HttpServletRequest request, HttpServletResponse response, String id, String area, String time) {

		try {
			if (!CommonUtil.ifEmpty(area)) {
				area = SessionUtil.getArea();
			}
			List<ResultPo> datas = new ArrayList<ResultPo>();
			List<ResultPo> rpos = issueService.getTreeForResult(area, time);
			for (ResultPo rpo : rpos) {
				if (!"四川".equals(rpo.getArea()) && "送电侧".equals(rpo.getSide())) {
					continue;
				}
				ResultPo po = issueService.getResultById(rpo.getId());
				datas.add(po);
			}

			String zipname = "CBPM_RESULT_" + area + "_" + time + ".dat";
			OutputStream os = response.getOutputStream();
			int len = 0;
			byte buf[] = new byte[1024];// 缓存作用
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/octet-stream; charset=UTF-8");
			response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(zipname.getBytes("GB2312"), "ISO8859-1") + "\";");//

			Writer writer = new OutputStreamWriter(os);
			//matchService.createResultFile("", area, time, datas, writer);
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 通过小邮件导出发布通道剩余容量
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月20日下午4:41:56
	 * @param mdate
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/exportInterface")
	public JsonMsg exportInterface(String time) {
		log.info("@ exportInterface ");
		String mdate = time;
		// System.out.println("++++++++++++++开始调用C程序======>plan_copy2.sh++++++++++++");
		JsonMsg j = new JsonMsg();
		String fileSrc = SystemConstUtil.getMatchPath() + File.separator + "trans_data" + File.separator + "send" + File.separator;

		try {
			LoggerUtil.log(this.getClass().getName(), "++++++++++++++开始生成本地文件+++++++++++++", 0);
			// 生成通道剩余容量

			JsonMsg j1 = matchService.exportMpathLimit(mdate);
			if (!j1.getStatus()) {
				throw new MsgException(j1.getMsg());
			}
			// 生成各省调出清结果

			String[] areas = { "四川", "江苏", "上海", "山东", "浙江" };
			for (String area : areas) {
				List<ResultPo> datas = new ArrayList<ResultPo>();
				List<ResultPo> rpos = issueService.getTreeForResult(area, time);

				for (ResultPo rpo : rpos) {
					if (!"四川".equals(rpo.getArea()) && "送电侧".equals(rpo.getSide())) {
						continue;
					}
					ResultPo po = issueService.getResultById(rpo.getId());
					datas.add(po);

				}
				//matchService.createResultFile(fileSrc, area, time, datas, null);

			}

			LoggerUtil.log(this.getClass().getName(), "++++++++++++++结束生成本地文件+++++++++++++", 0);

			LoggerUtil.log(this.getClass().getName(), "++++++++++++++开始调用C程序======>Send_trans_data.sh++++++++++++" + SystemConstUtil.getMatchPath() + File.separator + "bin" + File.separator + "Send_trans_data.sh", 0);
			Process process;
			try {
				process = Runtime.getRuntime().exec(SystemConstUtil.getMatchPath() + File.separator + "bin" + File.separator + "./Send_trans_data.sh");
				try {
					process.waitFor();
					process.exitValue();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new MsgException("调用C程序获取失败！");
			}
			LoggerUtil.log(this.getClass().getName(), "++++++++脚本触发结束++++++++", 0);

			boolean flag = false;
			int index = 1;
			int size = 0;
			String simpleMdate = mdate.replaceAll("-", "");

			while (!flag) {
				if (index == 5) {
					break;
				}
				LoggerUtil.log(this.getClass().getName(), "++++++++判断是否还有今天的文件没有发完++++++++" + fileSrc, 0);

				File file = new File(fileSrc);
				File[] array = file.listFiles();

				for (int i = 0; i < array.length; i++) {
					if (array[i].getName().contains(simpleMdate)) {// 根据日期匹配文件
						size = 1;
						break;
					}
				}
				if (size == 0) {
					flag = true;
					break;
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				index++;
			}
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "导出出清信息给省调", "exportInterface", mdate, "成功", true);
			j.setMsg("导出成功！");

			// System.out.println("++++++++++++++C程序======>plan_copy2.sh执行完毕++++++++++++");
			LoggerUtil.log(this.getClass().getName(), "++++++++++++++C程序======>Send_trans_data.sh执行完毕++++++++++++", 0);

		} catch (MsgException e) {
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "导出出清信息给省调", "exportInterface", mdate, e.getMessage(), false);

			j.setMsg(e.getMessage());
		} catch (Exception e) {
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "导出出清信息给省调", "exportInterface", mdate, e.getMessage(), false);

			j.setMsg("导出失败！" + e.getMessage());
		} finally {
			return j;
		}
		// System.out.println("++++++++++++++开始调用C程序======>insert_cbpm2.sh++++++++++++");

	}
	/**
	 * 加载日报展示左边树
	 * @author 车斯剑
	 * @date 2016年10月19日下午8:06:26
	 * @param request
	 * @param area
	 * @param time
	 * @return
	 */
	@RequestMapping(value = "/loadDailyTree", method = RequestMethod.POST)
	public Object loadDailyTree(HttpServletRequest request, String area, String time) {
		List<TreeBean> list = null;
		Integer status = null;
		// 如果是非国调用户
		if (!SessionUtil.isState()) {
			status = 1;
		}
		list = issueService.getDailyTree(area, time, status);
		if (list == null) {
			return "[]";
		} else {
			return JSON.toJSON(list);
		}
	}

	
	
	/**
	 * 
	 * 本地导出
	 * @description
	 * @author 车斯剑
	 * @date 2016年9月21日上午10:02:53
	 * @param request
	 * @param response
	 * @param mdate
	 */
	@RequestMapping(value = "/localExportData", method = RequestMethod.GET)
	public void localExportData(HttpServletRequest request, HttpServletResponse response, String mdate, String area) {

		String modelPath = request.getSession().getServletContext().getRealPath("/bat/dailyModel_SD.xls");
		if (SessionUtil.isState()) {
			modelPath = request.getSession().getServletContext().getRealPath("/bat/dailyModel_GD.xls");
		}
		try {
			//area = new String(area.getBytes("ISO8859-1"), "utf-8");
			area = URLDecoder.decode(area, "utf-8");
		} catch (UnsupportedEncodingException e2) {

			e2.printStackTrace();
		}
		if(!CommonUtil.ifEmpty(area)){
			area = SessionUtil.getArea();
		}
		// temppath=request.getSession().getServletContext().getRealPath("/bat/");
		String time = mdate + "";
		mdate = mdate.replaceAll("-", "");
		OutputStream os = null;
		try {
			List<ResultPo> rpos = issueService.getTreeForResult(area, mdate);
			//System.out.println(rpos.size()+"=="+area+"=="+mdate);
			List<ResultPo> scdlDatas = new ArrayList<ResultPo>();// 四川送段电力出清集合
			List<ResultPo> scdjDatas = new ArrayList<ResultPo>();// 四川送段电价出清集合

			List<ResultPo> acceptJsDlList = new ArrayList<ResultPo>();// 四川送江苏受端电力出清集合
			List<ResultPo> acceptShDlList = new ArrayList<ResultPo>();// 四川送上海受端电力出清集合
			List<ResultPo> acceptZjDlList = new ArrayList<ResultPo>();// 四川送浙江受端电力出清集合
			List<ResultPo> acceptSdDlList = new ArrayList<ResultPo>();// 四川送山东受端电力出清集合

			List<ResultPo> acceptJsDjList = new ArrayList<ResultPo>();// 四川送江苏受端电价出清集合
			List<ResultPo> acceptShDjList = new ArrayList<ResultPo>();// 四川送上海受端电价出清集合
			List<ResultPo> acceptZjDjList = new ArrayList<ResultPo>();// 四川送浙江受端电价出清集合
			List<ResultPo> acceptSdDjList = new ArrayList<ResultPo>();// 四川送山东受端电价出清集合
			
			for (ResultPo rpo : rpos) {
				if ("四川".equals(rpo.getArea()) && "电力".equals(rpo.getDtype())) {
					ResultPo po = issueService.getResultById(rpo.getId());
					scdlDatas.add(po);
				} else if ("四川".equals(rpo.getArea()) && "电价".equals(rpo.getDtype())) {
					ResultPo po = issueService.getResultById(rpo.getId());
					scdjDatas.add(po);
				} else if ("江苏".equals(rpo.getArea()) && "受电侧".equals(rpo.getSide()) && "电力".equals(rpo.getDtype())) {
					ResultPo po = issueService.getResultById(rpo.getId());
					acceptJsDlList.add(po);
				} else if ("上海".equals(rpo.getArea()) && "受电侧".equals(rpo.getSide()) && "电力".equals(rpo.getDtype())) {
					ResultPo po = issueService.getResultById(rpo.getId());
					acceptShDlList.add(po);
				} else if ("浙江".equals(rpo.getArea()) && "受电侧".equals(rpo.getSide()) && "电力".equals(rpo.getDtype())) {
					ResultPo po = issueService.getResultById(rpo.getId());
					acceptZjDlList.add(po);
				} else if ("山东".equals(rpo.getArea()) && "受电侧".equals(rpo.getSide()) && "电力".equals(rpo.getDtype())) {
					ResultPo po = issueService.getResultById(rpo.getId());
					acceptSdDlList.add(po);
				} else if ("江苏".equals(rpo.getArea()) && "受电侧".equals(rpo.getSide()) && "电价".equals(rpo.getDtype())) {
					ResultPo po = issueService.getResultById(rpo.getId());
					acceptJsDjList.add(po);
				} else if ("上海".equals(rpo.getArea()) && "受电侧".equals(rpo.getSide()) && "电价".equals(rpo.getDtype())) {
					ResultPo po = issueService.getResultById(rpo.getId());
					acceptShDjList.add(po);
				} else if ("浙江".equals(rpo.getArea()) && "受电侧".equals(rpo.getSide()) && "电价".equals(rpo.getDtype())) {
					ResultPo po = issueService.getResultById(rpo.getId());
					acceptZjDjList.add(po);
				} else if ("山东".equals(rpo.getArea()) && "受电侧".equals(rpo.getSide()) && "电价".equals(rpo.getDtype())) {
					ResultPo po = issueService.getResultById(rpo.getId());
					acceptSdDjList.add(po);
				}

			}

			ResultPo scdlResult = null;
			ResultPo scdjResult = null;

			// 受端电力
			ResultPo acceptJsDlResult = null;
			ResultPo acceptShDlResult = null;
			ResultPo acceptZjDlResult = null;
			ResultPo acceptSdDlResult = null;
			// 受端电价
			ResultPo acceptJsDjResult = null;
			ResultPo acceptShDjResult = null;
			ResultPo acceptZjDjResult = null;
			ResultPo acceptSdDjResult = null;
			try {
				if (scdlDatas.size() > 0) {
					scdlResult = ResultUtil.combineResultPo(scdlDatas);// 获取四川送段电力出清信息
				}
				if (scdjDatas.size() > 0) {
					scdjResult = ResultUtil.combineResultPo(scdjDatas);// 获取四川送段电价出清信息
				}

				if (acceptJsDlList.size() > 0) {
					acceptJsDlResult = ResultUtil.combineResultPo(acceptJsDlList);
				}
				if (acceptShDlList.size() > 0) {
					acceptShDlResult = ResultUtil.combineResultPo(acceptShDlList);
				}
				if (acceptZjDlList.size() > 0) {
					acceptZjDlResult = ResultUtil.combineResultPo(acceptZjDlList);
				}
				if (acceptSdDlList.size() > 0) {
					acceptSdDlResult = ResultUtil.combineResultPo(acceptSdDlList);
				}

				// 受端电价
				if (acceptJsDjList.size() > 0) {
					acceptJsDjResult = ResultUtil.combineResultPo(acceptJsDjList);
				}
				if (acceptShDjList.size() > 0) {
					acceptShDjResult = ResultUtil.combineResultPo(acceptShDjList);
				}
				if (acceptZjDjList.size() > 0) {
					acceptZjDjResult = ResultUtil.combineResultPo(acceptZjDjList);
				}
				if (acceptSdDjList.size() > 0) {
					acceptSdDjResult = ResultUtil.combineResultPo(acceptSdDjList);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			// excel模板路径
			File fi = new File(modelPath);
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fi));
			// 读取excel模板
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			// 读取了模板内所有sheet内容
			HSSFSheet sheet = wb.getSheetAt(0);

			// 设置申报信息
			// DecimalFormat decimalFormat = new
			// DecimalFormat("#,##0.00");//格式化设置
			DecimalFormat decimalFormat = new DecimalFormat("0.00");// 格式化设置

			int y = 1;
			if (SessionUtil.isState()) {
				for (int i = 0; i < 24; i++) {
					for (int j = 0; j < 4; j++) {
						if (scdlResult != null) {
							HSSFCell cell1 = sheet.getRow(5 + j).getCell(2 + i);
							Double ss = ResultUtil.getResultPoValue(scdlResult, y);
							cell1.setCellValue(ss);
						}
						if (scdjResult != null) {
							HSSFCell cell1 = sheet.getRow(14 + j).getCell(2 + i);
							Double ss = ResultUtil.getResultPoValue(scdjResult, y);
							cell1.setCellValue(ss);
						}

						// 受端电力出清情况
						if (acceptJsDlResult != null) {
							HSSFCell cell1 = sheet.getRow(23 + j).getCell(2 + i);
							Double ss = ResultUtil.getResultPoValue(acceptJsDlResult, y);
							cell1.setCellValue(ss);
						}
						if (acceptShDlResult != null) {
							HSSFCell cell1 = sheet.getRow(41 + j).getCell(2 + i);
							Double ss = ResultUtil.getResultPoValue(acceptShDlResult, y);
							cell1.setCellValue(ss);
						}
						if (acceptZjDlResult != null) {
							HSSFCell cell1 = sheet.getRow(59 + j).getCell(2 + i);
							Double ss = ResultUtil.getResultPoValue(acceptZjDlResult, y);
							cell1.setCellValue(ss);
						}
						if (acceptSdDlResult != null) {
							HSSFCell cell1 = sheet.getRow(77 + j).getCell(2 + i);
							Double ss = ResultUtil.getResultPoValue(acceptSdDlResult, y);
							cell1.setCellValue(ss);
						}
						// 受端电价出清情况
						if (acceptJsDjResult != null) {
							HSSFCell cell1 = sheet.getRow(32 + j).getCell(2 + i);
							Double ss = ResultUtil.getResultPoValue(acceptJsDjResult, y);
							cell1.setCellValue(ss);
						}
						if (acceptShDjResult != null) {
							HSSFCell cell1 = sheet.getRow(50 + j).getCell(2 + i);
							Double ss = ResultUtil.getResultPoValue(acceptShDjResult, y);
							cell1.setCellValue(ss);
						}
						if (acceptZjDjResult != null) {
							HSSFCell cell1 = sheet.getRow(68 + j).getCell(2 + i);
							Double ss = ResultUtil.getResultPoValue(acceptZjDjResult, y);
							cell1.setCellValue(ss);
						}
						if (acceptSdDjResult != null) {
							HSSFCell cell1 = sheet.getRow(86 + j).getCell(2 + i);
							Double ss = ResultUtil.getResultPoValue(acceptSdDjResult, y);
							cell1.setCellValue(ss);
						}
						y++;
					}
				}
			} else {
				for (int i = 0; i < 24; i++) {
					for (int j = 0; j < 4; j++) {
						if (scdlResult != null) {
							HSSFCell cell1 = sheet.getRow(5 + j).getCell(2 + i);
							Double ss = ResultUtil.getResultPoValue(scdlResult, y);
							cell1.setCellValue(ss);
						}
						if (scdjResult != null) {
							HSSFCell cell1 = sheet.getRow(14 + j).getCell(2 + i);
							Double ss = ResultUtil.getResultPoValue(scdjResult, y);
							cell1.setCellValue(ss);
						}

						// 受端电力出清情况
						if (acceptJsDlResult != null) {
							HSSFCell cell1 = sheet.getRow(5 + j).getCell(2 + i);
							Double ss = ResultUtil.getResultPoValue(acceptJsDlResult, y);
							cell1.setCellValue(ss);
						}
						if (acceptShDlResult != null) {
							HSSFCell cell1 = sheet.getRow(5 + j).getCell(2 + i);
							Double ss = ResultUtil.getResultPoValue(acceptShDlResult, y);
							cell1.setCellValue(ss);
						}
						if (acceptZjDlResult != null) {
							HSSFCell cell1 = sheet.getRow(5).getCell(2 + i);
							Double ss = ResultUtil.getResultPoValue(acceptZjDlResult, y);
							cell1.setCellValue(ss);
						}
						if (acceptSdDlResult != null) {
							HSSFCell cell1 = sheet.getRow(5 + j).getCell(2 + i);
							Double ss = ResultUtil.getResultPoValue(acceptSdDlResult, y);
							cell1.setCellValue(ss);
						}
						// 受端电价出清情况
						if (acceptJsDjResult != null) {
							HSSFCell cell1 = sheet.getRow(14 + j).getCell(2 + i);
							Double ss = ResultUtil.getResultPoValue(acceptJsDjResult, y);
							cell1.setCellValue(ss);
						}
						if (acceptShDjResult != null) {
							HSSFCell cell1 = sheet.getRow(14 + j).getCell(2 + i);
							Double ss = ResultUtil.getResultPoValue(acceptShDjResult, y);
							cell1.setCellValue(ss);
						}
						if (acceptZjDjResult != null) {
							HSSFCell cell1 = sheet.getRow(14 + j).getCell(2 + i);
							Double ss = ResultUtil.getResultPoValue(acceptZjDjResult, y);
							cell1.setCellValue(ss);
						}
						if (acceptSdDjResult != null) {
							HSSFCell cell1 = sheet.getRow(14 + j).getCell(2 + i);
							Double ss = ResultUtil.getResultPoValue(acceptSdDjResult, y);
							cell1.setCellValue(ss);
						}
						y++;
					}
				}
			}

			HSSFCell cell2 = sheet.getRow(1).getCell(0);
			cell2.setCellValue(time + " " + TimeUtil.getWeekOfDate(time));

			HSSFCell cell3 = sheet.getRow(19).getCell(0);
			if (SessionUtil.isState()) {
				cell3 = sheet.getRow(91).getCell(0);
			} else {
				HSSFCell cell4 = sheet.getRow(5).getCell(0);
				HSSFCell cell5 = sheet.getRow(14).getCell(0);
				cell4.setCellValue(SessionUtil.getAddNameCn());
				cell5.setCellValue(SessionUtil.getAddNameCn());
			}
			cell3.setCellValue("  制作人： 国调中心                校核人：国调中心                 制作日期：" + time);

			String fileName = "可再生能源跨省区消纳现货市场出清结果-" + mdate + ".xls";
			// response.setCharacterEncoding("UTF-8");
			response.setContentType("application/vnd.ms-excel");
			// response.setHeader("content-disposition","attachment;filename=11.xls");
			// response.setContentType("application/octet-stream; charset=UTF-8");
			response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GB2312"), "ISO8859-1") + "\";");//
			// os = response.getOutputStream();//输出流
			// fs.close();
			os = response.getOutputStream();
			wb.write(os);
			os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (os != null)
					os.close();
			} catch (IOException e) {
			}
		}

	}

	/**
	 * 动态导出日报excel
	 * 
	 * @description
	 * @author 车斯剑
	 * @date 2016年9月21日上午10:02:53
	 * @param request
	 * @param response
	 * @param mdate
	 */
	@RequestMapping(value = "/exportDailyExcel", method = RequestMethod.GET)
	public void exportDailyExcel(HttpServletRequest request, HttpServletResponse response, String mdate, String area) {

		try {
			area = new String(area.getBytes("ISO8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		String time = mdate;
		String week ="";
		try {
			week = TimeUtil.getWeekOfDate(time);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		String dailyDate =time+"  "+week;
		mdate = mdate.replaceAll("-", "");
		OutputStream os = null;
		try {
			Integer status = null;
			// 如果是非国调用户
			if (!SessionUtil.isState()) {
				status = 1;
			}
			Map<String,List<ResultPo>> dailyData = issueService.getDailyResults(area, mdate,status);
			String fileName = "可再生能源跨省区消纳现货市场运行日报-" + mdate + ".xls";
			int len = 0;
			byte buf[] = new byte[1024];// 缓存作用
			//response.setContentType("application/vnd.ms-excel");
			response.setContentType("application/octet-stream; charset=UTF-8");
			response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GB2312"), "ISO8859-1") + "\";");//
			os = response.getOutputStream();
			ExportExcelUtil.exportDailyExcel(dailyDate, null, dailyData, os);
			
			//wb.write(os);
			//os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null)
					os.close();
			} catch (IOException e) {
			}
		}

	}

	/**
	 * 导出每天的交易单
	 * @description
	 * @author 大雄
	 * @date 2016年9月25日下午5:49:44
	 * @param request
	 * @param response
	 * @param time
	 */
	@RequestMapping(value = "/exportDailyDoc1", method = RequestMethod.GET)
	public void exportDailyDoc1(HttpServletRequest request, HttpServletResponse response, String time) {

		String modelPath = request.getSession().getServletContext().getRealPath("/bat/daily.doc");
		String tampPath = request.getSession().getServletContext().getRealPath("/temp");
		FileInputStream is = null;
		POIFSFileSystem pfs = null;
		OutputStream os = null;
		String _BR = "\r\n";
		try {
			Date d = TimeUtil.strToDate(time);
			String dateStr = TimeUtil.toStrDateFromUtilDateByFormat(d, "yyyy年MM月dd日");
			Map<String, String> paramMap = new HashMap<String, String>();
			is = new FileInputStream(new File(modelPath));
			pfs = new POIFSFileSystem(is);
			HWPFDocument hwpf = new HWPFDocument(pfs);
			Range range = hwpf.getRange();
			paramMap.put("date", dateStr);
			Map<String,StringBuilder> delcateMap = declareService.getDelcateMap(tampPath,time);
			Map<String,StringBuilder> issueMap = issueService.getResultMap(time);
			//System.out.println(CommonUtil.objToJson(issueMap));
			StringBuilder issueSb = new StringBuilder();
			int indexTemp = 1;
			for(Entry<String,StringBuilder> entry : issueMap.entrySet()){
				issueSb.append((indexTemp++) + ")." + entry.getKey()).append(entry.getValue());
			}
			//StringBuilder declareSb = new StringBuilder();
			//declareSb.append("1).四川  第1时段到第10时段 电力:200MW,电价:100MWh").append(_BR);
			//declareSb.append("\t\t  第11时段到第20时段 电力是200MW,电价是100MWh").append(_BR);
			paramMap.put("declareSaleData", delcateMap.get("declareSaleData").toString());
			paramMap.put("declareBuyData", delcateMap.get("declareBuyData").toString());
			paramMap.put("issureData", issueSb.toString());
			
			Set<String> keys = paramMap.keySet();
			for (String key : keys) {
				Object value = paramMap.get(key);
				range.replaceText("${" + key + "}", value == null ? "" : String.valueOf(value));
			}

			//int len = 0;
			//byte buf[] = new byte[1024];// 缓存作用
			String fileName = "富余可再生能源跨省区现货市场"+dateStr+"交易单.doc";
			//System.out.println(fileName);
			//response.setCharacterEncoding("UTF-8");
			//response.setContentType("application/octet-stream; charset=UTF-8");
			response.setContentType("application/vnd.ms-word");
			response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GB2312"), "ISO8859-1") + "\";");//
			os = response.getOutputStream();
			hwpf.write(os);
			os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (pfs != null) {
					pfs.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 导出每天的交易单
	 * @description
	 * @author 大雄
	 * @date 2016年9月25日下午5:49:44
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getExportDailyDoc", method = RequestMethod.POST)
	public Map<String,Object> getExportDailyDoc(HttpServletRequest request, HttpServletResponse response, String time) {
		Map<String,Object> map = new HashMap<String,Object>();
		String tempPath = SystemConstUtil.getTempPath();
		String fileNames = null;
		map.put("status", false);
		try {
			File f = new File(tempPath);
			if(!f.exists()){
				f.mkdirs();
			}
			Date d = TimeUtil.strToDate(time);
			String dateStr = TimeUtil.toStrDateFromUtilDateByFormat(d, "yyyy年MM月dd日");
			String area = null;
			if(!SessionUtil.isState()){
				//如果不是国调则导出对应的省
				area = SessionUtil.getArea();
			}
			List<Map<String, Object>> datas = MyWordUtil.getTestData();
			MyWordUtil.export(datas);
			fileNames = issueService.exportDailyFile(area,time.replaceAll("-", ""),dateStr,tempPath);
			map.put("status", true);
			map.put("msg", fileNames);
		} catch (MsgException e) {
			//System.out.println("========="+e.getMessage());
			map.put("msg", e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			map.put("msg", "导出失败");
			e.printStackTrace();
		} finally {
			return map;
		}
	}
	
	/**
	 * 弹出下载框
	 * @description
	 * @author 大雄
	 * @date 2016年9月26日下午5:02:52
	 * @param request
	 * @param response
	 * @param fileName
	 * @return
	 */
	@RequestMapping(value = "/exportDailyDoc", method = RequestMethod.GET)
	public List<String> exportDailyDoc(HttpServletRequest request, HttpServletResponse response, String fileName) {

		String tempPath = SystemConstUtil.getTempPath();
		
		InputStream in = null;
		OutputStream os = null;
		String filePath = null;
		try {
			fileName = java.net.URLDecoder.decode(fileName,"UTF-8"); 
			response.setContentType("application/vnd.ms-word");
			
			response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GB2312"), "ISO8859-1") + "\";");//
			filePath = tempPath+File.separator+fileName;
			in = new FileInputStream(filePath); // 获取文件的流
			os = response.getOutputStream();
			int len = 0;
			byte buf[] = new byte[1024];// 缓存作用
			while ((len = in.read(buf)) > 0) // 切忌这后面不能加 分号 ”;“
			{
				os.write(buf, 0, len);// 向客户端输出，实际是把数据存放在response中，然后web服务器再去response中读取
			}
			in.close();
			os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (os != null) {
					os.close();
				}
				FileManager.delete(filePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateResult")
	public JsonMsg updateResult(HttpServletRequest request,HttpServletResponse response, String resultStr,String time) {
		log.info("@ updateResult ");
		//System.out.println("========" + resultStr);
		JsonMsg j = new JsonMsg();
		if(request.getSession().getAttribute("isDeclResult")==null){
			j.setMsg("先签名后保存！");
			return j;
		}
		String str = (String)request.getSession().getAttribute("isDeclResult");
		if(str.equals("false")){
			j.setMsg("先签名后保存！");
			return j;
		}
		JSONObject bean = com.alibaba.fastjson.JSONObject.parseObject(resultStr);
		ResultPo result = JSONObject.toJavaObject(bean, ResultPo.class);
		String mdate = time.replaceAll("-", "");
		declareService.clearLogForDecl(result.getId(),mdate, SessionUtil.getUserPo().getId(),Enums_SystemConst.SIGN_TYPE_DEAL_MODIFY.getValue());
		declareService.addLog(result.getId(), mdate,SessionUtil.getUserPo().getId(),Enums_SystemConst.SIGN_TYPE_DEAL_MODIFY.getValue());
		//System.out.println("result===="+CommonUtil.objToJson(result));
		//LineLimitPo lp = lineLimitService.getLineLimit(lineLimit.getMcorhr(), lineLimit.getMdate(), lineLimit.getDtype());
		//System.out.println();
		issueService.updateResult(result);
		j.setMsg("保存成功！");
		j.setStatus(true);
		return j;
	}
	
	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年10月8日下午11:45:26
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/result")
	public String issueResult(Model model) {
		log.info("@ init issueResult");
		//获取当前服务器时间
		model.addAttribute("serviceDate", TimeUtil.getStringDate());
		model.addAttribute("jspType", "result");
		return "issueResult";
	}

	/**
	 * 获取出请过程的json数据
	 * @description
	 * @author 大雄
	 * @date 2016年10月9日下午5:04:31
	 * @param mdate
	 */
	@RequestMapping(value = "/getJson", method = RequestMethod.GET)
	public void getJson(HttpServletRequest request,HttpServletResponse response,String mdate){
		InputStream in = null;
		BufferedOutputStream bout = null;
		try {
			//matchService.createClearDetailJsonFile(mdate, mdate, "D:/temp/guodiao/cbpm/data/CBPM_国调_result_20160911.dat");
			String path = SystemConstUtil.getMatchPath()+File.separator+"clearData"+File.separator+mdate + ".json";
			in = new FileInputStream(new File(path));
			if (in == null) {
				throw new MsgException("该文件不存在");
			}
			
		
			bout = new BufferedOutputStream(response.getOutputStream());
			byte[] b = new byte[1024];
			int n = 0;

			while ((n = in.read(b)) != -1) {
				bout.write(b, 0, n);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
				try {
					if (in != null) {
						in.close();
						in = null;
					}
					if (bout != null) {
						bout.close();
						bout = null;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
	
	/**
	 * 日报展示
	 * @author 车斯剑
	 * @date 2016年10月16日下午6:10:55
	 * @param model
	 * @param mdate
	 * @return
	 */
	@RequestMapping(value = "/dailyMap")
	public ModelAndView initDaily(Model model,String mdate) {
		log.info("@ init initDaily ");
		
		ModelAndView view = new ModelAndView("issue/daily", null);
		return view;
	}
	
	/**
	 * 导出周报界面
	 * @author 车斯剑
	 * @date 2016年10月13日上午9:08:05
	 * @param request
	 * @param response
	 * @param time
	 * @return
	 */
	@RequestMapping(value = "/exportWeekData")
	public ModelAndView exportWeekData(HttpServletRequest request, HttpServletResponse response) {
		log.info("跳转到导出周报界面");
		Map<String, Object> map = new HashMap<String, Object>();
		ModelAndView view = new ModelAndView("issue/weekData", map);
		return view;
	}
	
	/*@RequestMapping(value="/exportword")
	public void exportword(HttpServletRequest request, HttpServletResponse response){
		String svgCode = request.getParameter("svg");
		System.out.println("svgCode===="+svgCode);
		try {
			SvgPngConverter.convertToPng(svgCode, "d:\\temp\\222.png");
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (TranscoderException e1) {
			e1.printStackTrace();
		}  
	}*/
	/**
	 * 导出周报
	 * @author 车斯剑
	 * @date 2016年10月13日上午10:51:34
	 * @param request
	 * @param response
	 * @param fileName
	 * @return
	 */
	@RequestMapping(value = "/exportWeekReport", method = RequestMethod.GET)
	public List<String> exportWeekReport(HttpServletRequest request, HttpServletResponse response, String startTime,String endTime) {

		
		String tempPath = SystemConstUtil.getTempPath();
		int startWeekNum = TimeUtil.getWeekInYear(startTime);
		int endWeekNum = TimeUtil.getWeekInYear(endTime);
		String fileName = "富余可再生能源跨省区现货市场运行情况周报(第"+endWeekNum+"周).docx"; 
		WeekDataPo week =null;
		
		WeekDataPo weekData = weekDataService.getWeekDataByTimeAndWeek(startTime, endTime);
		if(weekData!=null){
			week=weekData;
		}else{
			week = weekDataService.getWeekData(startTime,endTime);
			if((endWeekNum-startWeekNum)==0 && week.getDays()==7){
				week.setId(CommonUtil.getUUID());
				weekDataService.insertWeekData(week);
			}
		}
		//新生成图片方法
		Map<String,Object>  picData = JSON.parseObject(week.getPictureData(), new TypeReference<Map<String,Object>>(){});
		//Map<String,Object> picData = (Map<String, Object>) lineLimitService.getPictureAndAblePowerData(startTime,endTime).get("picData");
		JFreeChartUtil.createImage(tempPath+File.separator+"channelRatio.png", picData);
		WordUtil.exportWeekReportByWeekData(tempPath,fileName,week);
		
		/**/
		InputStream in = null;
		OutputStream os = null;
		String filePath = null;
		try {
			
			response.setContentType("application/vnd.ms-word");
			response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GB2312"), "ISO8859-1") + "\";");//
			filePath = tempPath+File.separator+fileName;
			in = new FileInputStream(filePath); // 获取文件的流
			os = response.getOutputStream();
			int len = 0;
			byte buf[] = new byte[1024];// 缓存作用
			while ((len = in.read(buf)) > 0) // 切忌这后面不能加 分号 ”;“
			{
				os.write(buf, 0, len);// 向客户端输出，实际是把数据存放在response中，然后web服务器再去response中读取
			}
			in.close();
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (os != null) {
					os.close();
				}
				FileManager.delete(filePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return null;
	}
	/**
	 * 获取展示周报数据
	 * @author 车斯剑
	 * @date 2016年10月20日上午12:15:28
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getWeekData")
	public WeekDataPo getWeekData(HttpServletRequest request, String startTime, String endTime,String type) {
		
		startTime = startTime.replaceAll("-", "");
		endTime = endTime.replaceAll("-", "");
		
		int endWeekNum = TimeUtil.getWeekInYear(endTime);
		int startWeekNum = TimeUtil.getWeekInYear(startTime);
		WeekDataPo week = null;
		WeekDataPo weekData = weekDataService.getWeekDataByTimeAndWeek(startTime, endTime);
		if(!"new".equals(type) && weekData!=null){
			week=weekData;
		}else if("new".equals(type) && weekData!=null){
			week = weekDataService.getWeekData(startTime,endTime);
			week.setId(weekData.getId());
			weekDataService.updateWeekData(week);
			
		}else{
			week = weekDataService.getWeekData(startTime,endTime);
			if((endWeekNum-startWeekNum)==0 && week.getDays()==7){
				week.setId(CommonUtil.getUUID());
				weekDataService.insertWeekData(week);
			}
		}
		return week;
	}

	
	
	/**
	 * 导出申报情况
	 * @author 车斯剑
	 * @date 2016年10月13日上午10:51:34
	 * @param request
	 * @param response
	 * @param fileName
	 * @return
	 */
	@RequestMapping(value = "/exportWeekReportExcel", method = RequestMethod.GET)
	public List<String> exportWeekReportExcel(HttpServletRequest request, HttpServletResponse response, String startTime,String endTime) {
		
		
		/**/
		InputStream in = null;
		OutputStream os = null;
		String filePath = null;
		try {
			startTime = startTime.replaceAll("-", "");
			endTime = endTime.replaceAll("-", "");
			List<DeclarePo> list = declareService.querySheetsByTime(startTime, endTime, null);
			Map<String,Map<String,Integer>> map = new TreeMap<String,Map<String,Integer>>(new Comparator<String>() {

				public int compare(String o1, String o2) {
					// TODO Auto-generated method stub
					return o1.compareTo(o2);
				}
			});
			if(list != null){
				Map<String,Integer> temp = null;
				for(DeclarePo po : list){
					if(map.containsKey(po.getMdate())){
						temp = map.get(po.getMdate());
					}else{
						temp = new HashMap<String, Integer>();
						map.put(po.getMdate(), temp);
					}
					temp.put(po.getArea(), po.getStatus());
				}
			}
			String modelPath = request.getSession().getServletContext().getRealPath("/bat/weekModel.xls");
			File fi = new File(modelPath);
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fi));
			String[] areaArr = { "四川", "江苏", "上海", "浙江", "山东","安徽" };
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			// 读取了模板内所有sheet内容
			HSSFSheet sheet = wb.getSheetAt(0);
			
			int rowIndex = 1;
			for(Entry<String,Map<String,Integer>> entry:map.entrySet()){
				
				int index = rowIndex;
				for(String area : areaArr){
					HSSFRow row = sheet.createRow(index++);
					HSSFCell cell = row.createCell(1);
					
					cell.setCellValue(format1.format(format.parse(entry.getKey())));
					HSSFCell cell2 = row.createCell(2);
					cell2.setCellValue(area);
					HSSFCell cell3 = row.createCell(3);
					cell3.setCellValue(entry.getValue().get(area) == null?"未申报":(entry.getValue().get(area)==0?"未提交":"已提交"));
				}
				rowIndex = rowIndex +7;
			}
			int endWeekNum = TimeUtil.getWeekInYear(endTime);
			String fileName = "富余可再生能源跨省区现货市场运行情况周报(第"+endWeekNum+"周).xls";
			
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(fileName.getBytes("GB2312"), "ISO8859-1") + "\";");//
			
			os = response.getOutputStream();
			
			wb.write(os);
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (os != null) {
					os.close();
				}
				//FileManager.delete(filePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
	/**
	 * 获取执行结果左边树
	 * @author 车斯剑
	 * @date 2016年12月15日下午3:51:58
	 * @param request
	 * @param area
	 * @param time
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/loadExecuteTree", method = RequestMethod.POST)
	public List<TreeBean> loadExecuteTree(HttpServletRequest request, String area, String time) {
		List<TreeBean> list = null;
		Integer status = null;
		
		if(SessionUtil.isState()){
			area = null;
		}else{
			status=1;
		}
		list = issueService.getExecuteTree(area, time, status);
		if (list == null) {
			return new ArrayList<TreeBean>();
		} else {
			return list;
		}
	}
	
	/**
	 * 发布界面单击左侧id获取result集
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年8月27日下午1:41:03
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getExecuteResultData", method = RequestMethod.POST)
	public Object getExecuteResultData(String id) {
		
		TransTielinePo tradeData = issueService.getExecuteResultById(id);
		return tradeData;
	}
	
	/**
	 * 获取交易结果中的概况汇总数据
	 * @description
	 * @author 大雄
	 * @date 2016年12月18日下午2:52:16
	 * @param mdate
	 * @return
	 */
	@RequestMapping(value = "/getSurveyData", method = RequestMethod.POST)
	public Map getSurveyData(String mdate) {
		Map<String,Object> data = ServiceHelper.getBean(SurveyServiceI.class).getData(mdate);
		return data;
	}
}
