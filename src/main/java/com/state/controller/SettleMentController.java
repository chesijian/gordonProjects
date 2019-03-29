package com.state.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
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
import com.alibaba.fastjson.TypeReference;
import com.state.enums.Enums_SystemConst;
import com.state.exception.MsgException;
import com.state.po.DeclareDataPo;
import com.state.po.DeclarePo;
import com.state.po.PathResultPo;
import com.state.po.ResultPo;
import com.state.po.SdTotalCostPo;
import com.state.po.SdcostResultPo;
import com.state.po.TreeBean;
import com.state.po.UserPo;
import com.state.po.extend.DataGrid;
import com.state.po.extend.JsonMsg;
import com.state.service.AreaService;
import com.state.service.IPathService;
import com.state.service.IssueService;
import com.state.service.MatchService;
import com.state.service.PfLoggerServiceI;
import com.state.service.ServiceHelper;
import com.state.util.AreaCountBean;
import com.state.util.AuthoritiesUtil;
import com.state.util.BillCountBean;
import com.state.util.CommonUtil;
import com.state.util.DatUtil;
import com.state.util.FileManager;
import com.state.util.LoggerUtil;
import com.state.util.SessionUtil;
import com.state.util.SystemTools;
import com.state.util.TimeUtil;
import com.state.util.office.ExportExcel;
import com.state.util.sys.SystemConstUtil;

@Controller
@RequestMapping("/settleMent")
public class SettleMentController {
	private static final transient Logger log = Logger.getLogger(SettleMentController.class);
	private static final String MODEL_NAME_ = "发布";
	@Autowired
	private IssueService issueService;
	@Autowired
	private IPathService pathService;

	@Autowired
	private AreaService areaService;
	@Autowired
	private MatchService matchService;

	/**
	 * 跳转发布页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/init")
	public String init(Model model) {
		log.info("@ init settleMent ");
		// System.out.println(CommonUtil.objToJson(areaService.getAllArea()));
		// model.addAttribute("areaList",
		// JSON.toJSON(areaService.getAllArea()).toString());
		//获取当前服务器时间
		model.addAttribute("serviceDate", TimeUtil.getStringDate());
		model.addAttribute("jspType", "settleMent");
		return "settleMent";
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
			// sb.insert(4,"-");
			// sb.insert(7,"-");
			time = sb.toString();
		}
		time = time.replaceAll("-", "");
		Map<String, Map<String, Map<String, Object>>> data = pathService.getResultByMdateAndDtype(time);
		Map<String, List<Vector<Map<String, Object>>>> result = new HashMap<String, List<Vector<Map<String, Object>>>>();
		List<Vector<Map<String, Object>>> list = null;
		int colors[] = { 20, 45, 75, 100 };
		int i = 0;
		for (Entry<String, Map<String, Map<String, Object>>> mpath : data.entrySet()) {

			if (mpath.getKey().equals("四川送山东")) {
				String[] areas = mpath.getKey().split("送");
				for (int j = 1; j < 97; j++) {
					String key = String.valueOf(j);
					if (result.containsKey(key)) {
						list = result.get(key);
					} else {
						list = new ArrayList<Vector<Map<String, Object>>>();
						result.put(key, list);
					}
					Vector<Map<String, Object>> line1 = new Vector<Map<String, Object>>();
					Vector<Map<String, Object>> line2 = new Vector<Map<String, Object>>();
					Map<String, Object> line1Start = new HashMap<String, Object>();
					Map<String, Object> line1End = new HashMap<String, Object>();
					Map<String, Object> line2Start = new HashMap<String, Object>();
					Map<String, Object> line2End = new HashMap<String, Object>();

					line1Start.put("name", areas[0]);
					line1Start.put("smoothness", 0.2);
					line1Start.put("path", mpath.getKey());
					line1Start.put("sendPower", data.get(mpath.getKey()).get(key).get("送端电力"));
					line1Start.put("recvPower", data.get(mpath.getKey()).get(key).get("受端电力"));

					line1End.put("name", "甘肃");
					line1End.put("value", colors[i]);

					line2Start.put("name", "甘肃");
					line2Start.put("smoothness", 0.2);
					line2Start.put("path", mpath.getKey());
					line2Start.put("sendPower", data.get(mpath.getKey()).get(key).get("送端电力"));
					line2Start.put("recvPower", data.get(mpath.getKey()).get(key).get("受端电力"));

					line2End.put("name", areas[1]);
					line2End.put("value", colors[i]);

					line1.add(line1Start);
					line1.add(line1End);
					line2.add(line2Start);
					line2.add(line2End);
					list.add(line1);
					list.add(line2);
				}
			} else {
				float smoothness = 0;
				if (mpath.getKey().equals("四川送上海")) {
					smoothness = 0.6f;
				} else if (mpath.getKey().equals("四川送江苏")) {
					smoothness = 0.3f;
				} else if (mpath.getKey().equals("四川送浙江")) {
					smoothness = 0.1f;
				}

				String[] areas = mpath.getKey().split("送");
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

					line1Start.put("name", areas[0]);
					line1Start.put("smoothness", smoothness);
					line1Start.put("path", mpath.getKey());
					line1Start.put("sendPower", data.get(mpath.getKey()).get(key).get("送端电力"));
					line1Start.put("recvPower", data.get(mpath.getKey()).get(key).get("受端电力"));

					line1End.put("name", areas[1]);
					line1End.put("value", colors[i]);

					line1.add(line1Start);
					line1.add(line1End);
					list.add(line1);
				}
			}
			i++;
		}

		Map<String, Object> map = new HashMap<String, Object>();
		// System.out.println(time+"===="+CommonUtil.objToJson(result));
		map.put("data", CommonUtil.objToJson(data));
		map.put("result", CommonUtil.objToJson(result));
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
	 * 根据申报单号、类型查找发布单
	 * 
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping(value = "/loadTree", method = RequestMethod.POST)
	public Object loadTree(HttpServletRequest request, String area, String time, String type) {
		// System.out.println("==="+type);
		List<TreeBean> list = null;
		Integer status = null;
		// 如果是非国调用户
		if (!SessionUtil.isState()) {
			status = 1;
		}
		// 地区送点侧受电侧出清数据
		if (type.equals("result")) {

			list = issueService.getTree(area, time, status);
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
		// System.out.println(CommonUtil.objToJson(list));
		// System.out.println(JSON.toJSON(list));
		// JSON.toJSON(list)

	}

	/**
	 * 
	 * 发布
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateIssue")
	public JsonMsg updateIssue(String time) {
		JsonMsg j = new JsonMsg();
		try {
			issueService.issue(time);
			j.setStatus(true);
			j.setMsg("发布成功！");
		} catch (Exception e) {
			j.setMsg("发布失败！");
		} finally {
			return j;
		}

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
	public Object getResultData(String id, String type) {
		Map map = new HashMap<String, Object>();
		// System.out.println(type);

		if (type.equals("result")) {
			ResultPo po = issueService.getResultById(id);
			return po;
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

	@RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
	public void exportExcel(HttpServletRequest request, HttpServletResponse response, String mdate) {

		try {
			// String path =
			// SessionUtil.getSession().getServletContext().getRealPath("js");
			// path = path+"/state/issue/data/"+mdate+".json";
			// String content = FileManager.readFile(path);
			// System.out.println(content);

			Map<String, String> dg1 = null;
			DataGrid dg = null;
			String path = SystemConstUtil.getMatchPath();

			String fileName = path + File.separator + "data" + File.separator + Enums_SystemConst.RESULT_FILE_NAME + mdate + ".dat";
			// List<Map<String, Object>> result = new ArrayList<Map<String,
			// Object>>();
			Vector<Vector<String>> data = null;
			try {
				data = DatUtil.readFile(new File(fileName), "<ClearDetail::国调 type=全数>", "</ClearDetail::国调 type=全数>");
				/*
				 * Map<String,Object> po = null; for (Vector<String> unit :
				 * data) { po = new HashMap<String,Object>(); po.put("Number",
				 * unit.get(1)); po.put("Interval", unit.get(2));
				 * po.put("BuyerArea", unit.get(3)); po.put("Buyername",
				 * unit.get(4)); po.put("Buyersection", unit.get(5));
				 * po.put("power", unit.get(6)); po.put("price", unit.get(7));
				 * po.put("SellerArea", unit.get(8)); po.put("Sellername",
				 * unit.get(9)); po.put("Sellersection", unit.get(10));
				 * po.put("leftpower", unit.get(11)); po.put("leftprice",
				 * unit.get(12)); po.put("Pricediff", unit.get(13));
				 * po.put("Corridorpower", unit.get(14)); po.put("clearpower",
				 * unit.get(15)); result.add(po); }
				 */
				// dg = CommonUtil.jsonToMapStr(content);
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
			String[] headers = { "序号", "时段数", "买单区域", "买单名称", "买单段数", "申报单剩余电力", "申报电价", "卖单区域", "卖单名称", "卖单段数", "申报单剩余电力", "申报电价", "价差", "通道剩余容量", "成交电力" };
			ExportExcel.exportDataToExcel("出清信息", headers, data, os);
			os.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	@RequestMapping(value = "/exportDailyExcel", method = RequestMethod.GET)
	public void exportDailyExcel(HttpServletRequest request, HttpServletResponse response, String mdate) {

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

			// 导出通道运行情况
			/*
			 * for(int i = 0;i<4;i++){ String key = lineArr[i];
			 * if(lineResult.get(key) != null){ HSSFCell cell2 =
			 * sheet.getRow(5+i).getCell(6);
			 * cell2.setCellValue(lineResult.get(pathArr[i]).get("送端上网费用"));
			 * HSSFCell cell3 = sheet.getRow(5+i).getCell(9);
			 * cell3.setCellValue(lineResult.get(key).get("省内输电费用"));
			 * 
			 * if(i == 3){ HSSFCell cell4 = sheet.getRow(5+i).getCell(13);
			 * cell4.
			 * setCellValue(lineResult.get(key).get("华中输电费用")+pathResult.get
			 * (pathArr[i]).get("西北输电费用")); HSSFCell cell5 =
			 * sheet.getRow(5+i).getCell(16);
			 * cell5.setCellValue(lineResult.get(key
			 * ).get("宁东输电费用")+pathResult.get(pathArr[i]).get("德宝输电费用"));
			 * 
			 * }else{ HSSFCell cell4 = sheet.getRow(5+i).getCell(13);
			 * cell4.setCellValue(0); HSSFCell cell5 =
			 * sheet.getRow(5+i).getCell(16);
			 * cell5.setCellValue(lineResult.get(pathArr[i]).get("跨区输电费用")); }
			 * HSSFCell cell6 = sheet.getRow(5+i).getCell(20);
			 * cell6.setCellValue(lineResult.get(pathArr[i]).get("从送端累加的总费用"));
			 * HSSFCell cell7 = sheet.getRow(5+i).getCell(23);
			 * cell7.setCellValue(lineResult.get(pathArr[i]).get("受端总费用")); } }
			 */

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
		return "";//matchService.createResultFile(Enums_SystemConst.RESULT_FILE_PATH_WIN, area, time, datas,null);
	}
	
	/**
	 * 本地导出
	 * @author 车斯剑
	 * @date 2016年9月19日下午4:39:59
	 * @param request
	 * @param response
	 * @param startTime
	 * @param endTime
	 * @param str
	 * @param type
	 */
	@RequestMapping(value = "/localExportData", method = RequestMethod.GET)
	public void localExportData(HttpServletRequest request, HttpServletResponse response, String id, String area,String time) {

		try {
			area =new String(area.getBytes("ISO8859-1"), "utf-8");
			List<ResultPo> datas = new ArrayList<ResultPo>();
			List<ResultPo> rpos = issueService.getTreeForResult(area, time);
			for (ResultPo rpo : rpos) {
				if(!"四川".equals(rpo.getArea())&&"送电侧".equals(rpo.getSide())){
					continue;
				}
				ResultPo po = issueService.getResultById(rpo.getId());
				datas.add(po);
			}
			
			String zipname = "CBPM_RESULT_" +area+"_"+time+ ".dat";
			OutputStream os = response.getOutputStream();
			int len = 0;
			byte buf[] = new byte[1024];// 缓存作用
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/octet-stream; charset=UTF-8");
			response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(zipname.getBytes("GB2312"), "ISO8859-1") + "\";");//
			
			Writer writer = new OutputStreamWriter(os);
			//matchService.createResultFile("", area, time, datas,writer);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
			

	}

	/**
	 * 通过小邮件导出发布通道剩余容量
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
			//生成通道剩余容量
			
			JsonMsg j1 = matchService.exportMpathLimit(mdate);
			if(!j1.getStatus()){
				throw new MsgException(j1.getMsg());
			}
			//生成各省调出清结果
			
			String[] areas = {"四川","江苏","上海","山东","浙江"};
			for(String area : areas){
				List<ResultPo> datas = new ArrayList<ResultPo>();
				List<ResultPo> rpos = issueService.getTreeForResult(area, time);

				for (ResultPo rpo : rpos) {
					if (!"四川".equals(rpo.getArea()) && "送电侧".equals(rpo.getSide())) {
						continue;
					}
					ResultPo po = issueService.getResultById(rpo.getId());
					datas.add(po);

				}
				//matchService.createResultFile(fileSrc, area, time, datas,null);
				
			}
			
			LoggerUtil.log(this.getClass().getName(), "++++++++++++++结束生成本地文件+++++++++++++", 0);
			
			LoggerUtil.log(this.getClass().getName(), "++++++++++++++开始调用C程序======>Send_trans_data.sh++++++++++++"+SystemConstUtil.getMatchPath() + File.separator + "bin" + File.separator + "Send_trans_data.sh", 0);
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
			//String fileSrc = SystemConstUtil.getMatchPath() + File.separator + "trans_data" + File.separator + "recv" + File.separator;
			
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
}
