package com.state.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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
import com.jrsoft.util.process.ProcessUtils;
import com.jrsoft.util.process.ProcessUtils.ProcessStatus;
import com.state.enums.Enums_SystemConst;
import com.state.exception.MsgException;
import com.state.po.AreaPo;
import com.state.po.DeclareDataPo;
import com.state.po.DeclarePo;
import com.state.po.SdcostResultPo;
import com.state.po.TypePo;
import com.state.po.UserPo;
import com.state.po.extend.JsonMsg;
import com.state.service.AreaService;
import com.state.service.IDeclareService;
import com.state.service.IssueService;
import com.state.service.MatchService;
import com.state.service.PfLoggerServiceI;
import com.state.service.ServiceHelper;
import com.state.service.ShellExecuteServiceI;
import com.state.util.Attributes;
import com.state.util.AuthoritiesUtil;
import com.state.util.BillCountBean;
import com.state.util.CommonUtil;
import com.state.util.DeclareUtil;
import com.state.util.FileManager;
import com.state.util.LoggerUtil;
import com.state.util.MParentTree;
import com.state.util.MatchUtil;
import com.state.util.SessionUtil;
import com.state.util.SystemTools;
import com.state.util.TimeUtil;
import com.state.util.sys.SystemConstUtil;

@Controller
@RequestMapping("/declare")
public class DeclareController {
	private static final transient Logger log = Logger.getLogger(DeclareController.class);
	/**
	 * 用于日志记录
	 */
	public static final String MODEL_NAME_ = "购售电申报单";
	@Autowired
	private IDeclareService declareService;

	@Autowired
	private AreaService areaService;
	@Autowired
	private IssueService issueService;
	@Autowired
	private ServletContext servletContext;

	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 跳转申报页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/init")
	public String init(Model model) {
		log.info("@ init declare " + AuthoritiesUtil.isAllow_Menu_UserManager());
		TypePo timeType = declareService.getTimeType();
		timeType.countType();
		model.addAttribute("timeType", JSON.toJSON(timeType).toString());
		model.addAttribute("areaList", JSON.toJSON(areaService.getAllArea()).toString());
		model.addAttribute("jspType", "declare");
		//获取当前服务器时间
		model.addAttribute("serviceDate", TimeUtil.getStringDate());

		return "declare";
	}

	/**
	 * 更改交易日期
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/changeDate")
	public String changeDate(HttpServletRequest request, String time, String jspName) {
		UserPo pd = (UserPo) request.getSession().getAttribute("userInfo");
		StringBuilder sb = new StringBuilder(time);
		sb.insert(4, "-");
		sb.insert(7, "-");
		pd.setMatchDate(sb.toString());
		if ("1".equals(pd.getIslogin())) {

			request.getSession().setAttribute("userInfo", pd);
		}
		// System.out.println(jspName);
		return jspName;
	}

	/**
	 * 
	 * 获取申报单子列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/getDeclareList", method = RequestMethod.POST)
	public List<DeclarePo> getDeclare(HttpServletRequest request, String area, String time) {
		if (area == null || "".equals(area)) {
			UserPo user = (UserPo) request.getSession().getAttribute("userInfo");
			area = user.getArea();
		}
		return declareService.getDeclares(area, time);
	}

	/**
	 * 获取申报数据
	 * 
	 * @param request
	 * @param response
	 * @param id
	 *            单子id
	 * @param type
	 *            申报类型 全天，高峰，低谷
	 * @return
	 */
	@RequestMapping(value = "/getIssueCountMeg", method = RequestMethod.POST)
	
	public BillCountBean getIssueCountMeg(HttpServletRequest request, HttpServletResponse response, String time, String sheetName) {
		BillCountBean billCountMeg = new BillCountBean();
		List<BillCountBean> billCount = new ArrayList<BillCountBean>();
		List<SdcostResultPo> sdBill = issueService.geSdBillMeg(time);// 申报单汇总
		for (SdcostResultPo sdpo : sdBill) {
			BillCountBean bcb = new BillCountBean();
			bcb.setArea(sdpo.getArea());
			bcb.setSheetName(sdpo.getdSheet());
			bcb.setClear(sdpo.getClearQJ());
			bcb.setSumPrice(sdpo.getSdfy());
			bcb.setSumQ(sdpo.getSumQ());
			bcb.setFdfy(sdpo.getFdfy());
			billCount.add(bcb);
		}
		for (BillCountBean bc : billCount) {
			if (bc.getSheetName().endsWith(sheetName)) {
				billCountMeg = bc;
			}

		}
		return billCountMeg;
	}

	/**
	 * 获取申报单汇总数据
	 * 
	 * @param request
	 * @param response
	 * @param id
	 *            单子id
	 * @param type
	 *            申报类型 全天，高峰，低谷
	 * @return
	 */
	@RequestMapping(value = "/getDeclareData", method = RequestMethod.POST)
	public DeclareDataPo getDeclareData(HttpServletRequest request, HttpServletResponse response, Long id, String type) {
		DeclareDataPo po = declareService.getDeclareData(id, type);
		return po;
	}

	/**
	 * 获取申报单导出数据
	 * 
	 * @param request
	 * @param response
	 * @param id
	 *            单子id
	 * @param type
	 *            申报类型1a 全天，2a高峰，3a低谷
	 * @return
	 */
	@RequestMapping(value = "/getExportMsg", method = RequestMethod.POST)
	public Map getExportMsg(HttpServletRequest request, HttpServletResponse response, Long id) {
		Map map = new HashMap<String, Object>();
		BillCountBean billCountMeg = new BillCountBean();
		List<BillCountBean> billCount = new ArrayList<BillCountBean>();

		DeclareDataPo po = declareService.getDeclareData(id, "");
		DeclarePo dp = declareService.getDeclare(id);
		List<SdcostResultPo> sdBill = issueService.geSdBillMeg(dp.getMdate());// 申报单汇总
		for (SdcostResultPo sdpo : sdBill) {
			BillCountBean bcb = new BillCountBean();
			bcb.setArea(sdpo.getArea());
			bcb.setSheetName(sdpo.getdSheet());
			bcb.setClear(sdpo.getClearQJ());
			bcb.setSumPrice(sdpo.getSdfy());
			bcb.setSumQ(sdpo.getSumQ());
			bcb.setFdfy(sdpo.getFdfy());
			billCount.add(bcb);
		}
		for (BillCountBean bc : billCount) {
			if (bc.getSheetName().endsWith(dp.getSheetName())) {
				billCountMeg = bc;
			}

		}

		map.put("declare", dp);
		map.put("declareData", po);
		if (billCountMeg != null) {
			map.put("billCountMeg", billCountMeg);
		}
		return map;
	}

	/**
	 * 获取申报数据
	 * 
	 * @param request
	 * @param response
	 * @param id
	 *            单子id
	 * @return
	 */
	@RequestMapping(value = "/getDeclareSumData", method = RequestMethod.POST)
	public DeclareDataPo getDeclareSumData(HttpServletRequest request, HttpServletResponse response, String id, String type) {
		List<Long> list = new ArrayList<Long>();
		if (id.contains(",")) {
			String[] array = id.split(",");
			for (int i = 0; i < array.length; i++) {
				list.add(Long.parseLong(array[i]));
			}
		} else if (id.equals("")) {
			DeclareDataPo po = new DeclareDataPo();
			po = getPobyVector(po);
			return po;
		} else {
			list.add(Long.parseLong(id));
		}

		return declareService.getDeclareSumData(list);
	}

	

	/**
	 * 删除申报
	 * 
	 * @param classCode
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public JsonMsg deleteDeclare(String id) {
		String[] strs = id.split(",");
		// System.out.println("==========="+id);
		JsonMsg j = new JsonMsg();
		try {
			for (String str : strs) {
				/*
				 * if(!declareService.existsDeclare(Long.parseLong(str))){
				 * return "申报单不存在"; }
				 */
				DeclarePo po = declareService.getDeclare(Long.parseLong(str));
				if (po == null) {
					j.setMsg("申报单不存在");
					return j;
				} else {
					// System.out.println("==="+po.getStatus());
					if (po.getStatus() == 1) {
						// return "申报单已经提交不能删除！";
						j.setMsg("申报单已经提交不能删除！");
					} else {
						declareService.deleteDeclare(Long.parseLong(str));
						j.setMsg("删除成功！");
						j.setStatus(true);
						;
					}
				}

			}

		} catch (Exception e) {
			log.error("delete declare fail !", e);
			j.setMsg("删除失败！");
			j.setStatus(false);
			;
		} finally {
			return j;
		}

	}

	/**
	 * 更改申报
	 * 
	 * @param classCode
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateDeclare(Model model, HttpServletRequest request, HttpServletResponse response, String declarePo) {
		JSONObject bean = com.alibaba.fastjson.JSONObject.parseObject(declarePo);
		DeclarePo javaObject = JSONObject.toJavaObject(bean, DeclarePo.class);
		declareService.updateDeclare(javaObject);
		return "success";
	}

	@RequestMapping(value = "/saveDeclareData", method = RequestMethod.POST)
	public Object saveDeclareData(Model model, HttpServletRequest request, HttpServletResponse response, String id, String data) {
		JsonMsg j = new JsonMsg();
		try {
			declareService.saveDeclareData(id, data);
			j.setStatus(true);
		} catch (MsgException me) {
			j.setMsg(me.getMessage());
		} catch (Exception e) {
			j.setMsg("保存失败！");
			e.printStackTrace();
		} finally {
			return j;
			// return JSON.toJSONString(j);
		}

	}

	@RequestMapping(value = "/submitDeclare", method = RequestMethod.POST)
	public Object submitDeclare(Model model, HttpServletRequest request, HttpServletResponse response, String ids) {
		JsonMsg j = new JsonMsg();
		try {
			declareService.updateDeclareStatus(ids);
			j.setStatus(true);
		} catch (MsgException me) {
			j.setMsg(me.getMessage());
		} catch (Exception e) {
			j.setMsg("提交失败！");
			e.printStackTrace();
		} finally {
			return j;
			// return JSON.toJSONString(j);
		}

	}

	/**
	 * 获取申报数据
	 * 
	 * @param request
	 * @param response
	 * @param id
	 *            单子id
	 * @param type
	 *            申报类型 全天，高峰，低谷
	 * @return
	 */
	@RequestMapping(value = "/getTimeType", method = RequestMethod.POST)
	public TypePo getTimeType(HttpServletRequest request, HttpServletResponse response) {
		return declareService.getTimeType();
	}

	/**
	 * 获取申报数据
	 * 
	 * @param request
	 * @param response
	 * @param id
	 *            单子id
	 * @param type
	 *            申报类型 全天，高峰，低谷
	 * @return
	 */
	@RequestMapping(value = "/exportDeclarDoc", method = RequestMethod.GET)
	public void exportDeclarDoc(HttpServletRequest request, HttpServletResponse response, String paramid, String time) {
		String[] ids = paramid.split(",");
		String[] fils = new String[ids.length];
		String path = request.getSession().getServletContext().getRealPath("/bat/model.doc");
		String temppath = request.getSession().getServletContext().getRealPath("/bat/");

		for (int i = 0; i < fils.length; i++) {
			if (ids[i] != null && Integer.parseInt(ids[i]) > 0) {
				DeclareDataPo po = declareService.getDeclareData(Long.parseLong(ids[i]), "");
				DeclarePo dep = declareService.getDeclare(Long.parseLong(ids[i]));
				try {
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("area", dep.getArea());
					paramMap.put("user", dep.getMname());
					paramMap.put("sheetName", dep.getSheetName());
					paramMap.put("sumPrice", po.getSumPrice());
					paramMap.put("sumq", po.getSumQ());
					paramMap.put("ave_p", po.getAveP());
					// System.out.println(po.getDtype().getName());
					paramMap.put("type", po.getDtype().getName());
					paramMap.put("mDate", dep.getMdate());
					paramMap.put("descr", dep.getDescr());
					// paramMap.put("time", dep.getMdate());
					if (dep.getClearstate().trim().equals("1")) {
						List<BillCountBean> billCount = new ArrayList<BillCountBean>();
						List<SdcostResultPo> sdBill = issueService.geSdBillMeg(time);// 申报单汇总
						for (SdcostResultPo sdpo : sdBill) {
							BillCountBean bcb = new BillCountBean();
							bcb.setArea(sdpo.getArea());
							bcb.setSheetName(sdpo.getdSheet());
							bcb.setClear(sdpo.getClearQJ());
							bcb.setSumPrice(sdpo.getSdfy());
							bcb.setSumQ(sdpo.getSumQ());
							bcb.setFdfy(sdpo.getFdfy());
							billCount.add(bcb);
						}
						for (BillCountBean bc : billCount) {
							if (bc.getSheetName().endsWith(dep.getSheetName())) {
								paramMap.put("clear", bc.getClear());
								paramMap.put("fdfy", bc.getFdfy());
							}

						}
					}
					FileInputStream is = new FileInputStream(new File(path));
					POIFSFileSystem pfs = new POIFSFileSystem(is);
					HWPFDocument hwpf = new HWPFDocument(pfs);
					
					Range range = hwpf.getRange();
					Set<String> keys = paramMap.keySet();
					for (String key : keys) {
						Object value = paramMap.get(key);
						range.replaceText("${" + key + "}", value == null ? "" : String.valueOf(value));
						
					}
					String docName = dep.getSheetName() + ".doc";
					File file = new File(temppath + "//" + docName);
					FileOutputStream fos = new FileOutputStream(file);
					fils[i] = file.getAbsolutePath();
					// System.out.println(temppath);
					hwpf.write(fos);
					is.close();
					pfs.close();
					fos.close();

				} catch (Exception e) {
					e.printStackTrace();
				} finally {

				}
			}

		}

		try {
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");// 可以方便地修改日期格
			String tm = dateFormat.format(new Date());
			String zipname = tm + ".zip";
			FileManager.writeZip(fils, zipname, temppath);
			// 服务端生成zip文件
			InputStream in = in = new FileInputStream(temppath + "//" + zipname); // 获取文件的流
			OutputStream os = response.getOutputStream();
			int len = 0;
			byte buf[] = new byte[1024];// 缓存作用
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/octet-stream; charset=UTF-8");
			response.addHeader("Content-Disposition", "attachment; filename=\"" + new String(zipname.getBytes("GB2312"), "ISO8859-1") + "\";");//
			os = response.getOutputStream();// 输出流
			while ((len = in.read(buf)) > 0) // 切忌这后面不能加 分号 ”;“
			{
				os.write(buf, 0, len);// 向客户端输出，实际是把数据存放在response中，然后web服务器再去response中读取
			}
			in.close();
			os.close();
			deleteZip(temppath);// 导出后删除zip
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

	/**
	 * 递归查找文件
	 * 
	 * @param baseDirName
	 *            查找的文件夹路径
	 * @param targetFileName
	 *            需要查找的文件名
	 * @param fileList
	 *            查找到的文件集合
	 */
	/*
	 * public static void findFiles(String baseDirName, String targetFileName,
	 * List fileList) {
	 * 
	 * File baseDir = new File(baseDirName); // 创建一个File对象 if (!baseDir.exists()
	 * || !baseDir.isDirectory()) { // 判断目录是否存在 System.out.println("文件查找失败：" +
	 * baseDirName + "不是一个目录！"); } String tempName = null; //判断目录是否存在 File
	 * tempFile; File[] files = baseDir.listFiles(); for (int i = 0; i <
	 * files.length; i++) { tempFile = files[i]; if(tempFile.isDirectory()){
	 * findFiles(tempFile.getAbsolutePath(), targetFileName, fileList); }else
	 * if(tempFile.isFile()){ tempName = tempFile.getName();
	 * if(wildcardMatch(targetFileName, tempName)){ // 匹配成功，将文件名添加到结果集
	 * fileList.add(tempFile.getAbsoluteFile()); } } } }
	 */
	// 获取申报单树结构
	@ResponseBody
	@RequestMapping(value = "/getTreeList", method = RequestMethod.POST)
	public List<MParentTree> getAll(HttpServletRequest request, String time) throws ParseException {
		List<DeclarePo> declareList = null;
		List<MParentTree> listp = new ArrayList<MParentTree>();
		List<MParentTree> list = new ArrayList<MParentTree>();
		List<MParentTree> billList = new ArrayList<MParentTree>();
		UserPo user = (UserPo) request.getSession().getAttribute("userInfo");
		// System.out.println( );
		// String userName = user.getMname();
		String userName = user.getArea();

		MParentTree root = new MParentTree();
		root.setId("-1");
		root.setpId("-0");
		root.setText("");
		root.setState("opened");
		int index = 0;
		if (user.getArea().equals("国调")) {
			declareList = declareService.queryForList(time, "", null);
			Map<String, Map<String, List<DeclarePo>>> map = getTreeData(declareList, time);
			for (String key : map.keySet()) {
				index += 1;
				MParentTree parent = new MParentTree();
				parent.setId(root.getId() + CommonUtil.decimal(2, index));
				parent.setpId(root.getId());
				parent.setText(key.equals("buy") ? "买方" : "卖方");
				if(parent.getText().equals("买方")){
					parent.setIconCls("icon-buy");
				}else{
					parent.setIconCls("icon-sale");
				}
				Attributes attributes = new Attributes();
				attributes.setDrloe(key);
				parent.setAttributes(attributes);
				List<MParentTree> firstNodeList = new ArrayList<MParentTree>();
				int i = 0;
				for (String area : map.get(key).keySet()) {
					MParentTree firstNode = new MParentTree();
					
						DeclarePo po = map.get(key).get(area).get(0);//由于一个地区只有一个单子
						
						firstNode.setId(String.valueOf(po.getId()));
						firstNode.setpId(parent.getId());
						firstNode.setText(area);
						attributes = new Attributes();
						attributes.setDrloe(key);
						attributes.setId(3 + "");
						String mdate = format1.format(format.parse(po.getMdate()));
						attributes.setMdate(mdate);
						attributes.setRname(po.getRname());
						attributes.setStartDate(po.getStartDate());
						attributes.setEndDate(po.getEndDate());
						attributes.setDescr(po.getDescr());
						attributes.setStatus(po.getStatus());
						attributes.setAllowEdit(false);
						attributes.setArea(area);
						firstNode.setAttributes(attributes);
						firstNode.setChildren(null);
						
						
						firstNode.setIconCls(po.getStatus() == 1?"icon-ok":"icon-nok");
						firstNode.setState("opened");
						firstNodeList.add(firstNode);
				}
				parent.setChildren(firstNodeList);
				parent.setState("opened");
				list.add(parent);
			}

		} else {
			// System.out.println("=============");

			declareList = declareService.queryForList(time, userName, null);
			// System.out.println("============="+CommonUtil.objToJson(declareList));
			if (declareList!= null && declareList.size() > 0) {
				MParentTree AreaNode = new MParentTree();
				index += 1;
				DeclarePo po = declareList.get(0);
				
				
				AreaNode.setId(String.valueOf(po.getId()));
				AreaNode.setpId(root.getId());
				
				Attributes attributes = new Attributes();
				attributes.setDrloe(user.getDrole());
				attributes.setId(3 + "");
				String mdate = format1.format(format.parse(po.getMdate()));
				attributes.setMdate(mdate);
				attributes.setRname(po.getRname());
				attributes.setStartDate(po.getStartDate());
				attributes.setEndDate(po.getEndDate());
				attributes.setDescr(po.getDescr());
				attributes.setDrloe(po.getDrloe());
				attributes.setStatus(po.getStatus());
				attributes.setArea(po.getArea());
				attributes.setAllowEdit(AuthoritiesUtil.isAllow_De_Intput(po.getStatus()));
				AreaNode.setAttributes(attributes);
				AreaNode.setText(user.getMname());
				AreaNode.setState("opened");
				//AreaNode.setIconCls("icon-trans");
				
				AreaNode.setIconCls(po.getStatus() == 1?"icon-ok":"icon-nok");
				
				AreaNode.setChildren(null);
				list.add(AreaNode);
			}

		}
		//root.setChildren(list);
		//listp.add(root);
		// System.out.println(CommonUtil.objToJson(listp));
		return list;
	}

	private Map<String, Map<String, List<DeclarePo>>> getTreeData(List<DeclarePo> list, String time) {

		Map<String, Map<String, List<DeclarePo>>> map = new HashMap<String, Map<String, List<DeclarePo>>>();
		Map<String, List<String>> typeToAreaMap = new HashMap<String, List<String>>();
		Map<String, List<DeclarePo>> areaToBillMap = null;
		for (DeclarePo po : list) {
			// System.out.println("po.getArea()=============="+po.getArea());
			if (typeToAreaMap.containsKey(po.getDrloe())) {
				typeToAreaMap.get(po.getDrloe()).add(po.getArea());
			} else {
				List<String> alist = new ArrayList<String>();
				alist.add(po.getArea());
				typeToAreaMap.put(po.getDrloe(), alist);
			}

		}

		for (String key : typeToAreaMap.keySet()) {
			List<String> alist = typeToAreaMap.get(key);
			removeDuplicateList(alist);
			areaToBillMap = new LinkedHashMap<String, List<DeclarePo>>();
			for (String area : alist) {
				List<DeclarePo> billList = new ArrayList<DeclarePo>();
				for (DeclarePo po : list) {
					if (area.equals(po.getArea()) && po.getDrloe().equals(key)) {
						billList.add(po);
					}
				}
				areaToBillMap.put(area, billList);
			}
			map.put(key, areaToBillMap);
		}

		return map;
	}

	public void removeDuplicateList(List list) {
		for (int i = 0; i < list.size(); i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).equals(list.get(i))) {
					list.remove(j);
				}
			}
		}
	}

	/**
	 * 通过vector给对象属性赋值
	 * 
	 * @param lines
	 */
	public DeclareDataPo getPobyVector(DeclareDataPo po) {
		po.setAveP(0.0);
		po.setSumQ(0.0);
		po.setSumPrice(0.0);
		Field[] fields = DeclareDataPo.class.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().startsWith("h")) {
				field.setAccessible(true);
				try {

					String setAttributeMethodName = "set" + Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
					Method setAttributeMethod = DeclareDataPo.class.getDeclaredMethod(setAttributeMethodName, Double.class);
					setAttributeMethod.invoke(po, 0.0);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}

		return po;

	}

	/**
	 * 删除zip
	 */
	public void deleteZip(String path) {
		/*
		 * String[] sos = abpath.split("/"); String name = ss[ss.length - 1];
		 * String path = abpath.replace("/" + name, "");
		 */

		File file = new File(path);// 里面输入特定目录
		File temp = null;
		File[] filelist = file.listFiles();
		for (int i = 0; i < filelist.length; i++) {
			temp = filelist[i];
			if (temp.getName().endsWith("zip"))// 获得文件名，如果后缀为“”，这个你自己写，就删除文件
			{
				temp.delete();// 删除文件}
			}
		}
	}

	/**
	 * 复制申报数据
	 * @author 车斯剑
	 * @date 2016年11月24日下午5:26:26
	 * @param request
	 * @param response
	 * @param time
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/copyData")
	public String copyData(HttpServletRequest request, HttpServletResponse response, String time, String area){
		String ddate = time.replaceAll("-", "");
		List<Map<String, Object>> data = null;//new ArrayList<Map<String,Object>>();
		Long declareId = declareService.getDeclareIdByTimeAndArea(ddate,area);
		if (declareId != null) {
			data = declareService.getDeclareExtraData(declareId);
			
		}
		return CommonUtil.objToJson(data);
	}
	/**
	 * 新的界面可以增加设置多个时段，对应多个电力和电价
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年8月17日上午10:21:09
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/new1")
	public ModelAndView new1(HttpServletRequest request, HttpServletResponse response, Model model, boolean readOnly) {
		log.info("跳转到单据信息界面");
		String id = request.getParameter("id");
		Map<String, Object> data = null;
		String mdate = request.getParameter("mdate");
		Map<String, Object> map = new HashMap<String, Object>();
		if (CommonUtil.ifEmpty(id)) {
			// 编辑
			map.put("status", "edit");
			DeclarePo po = declareService.getDeclare(Long.parseLong(id));
			if (po != null) {
				data = new HashMap<String, Object>();
				data.put("startDate", po.getStartDate());
				data.put("endDate", po.getEndDate());
				data.put("time", po.getDtime());
				data.put("sheetName", po.getSheetName());
				data.put("id", po.getId());
				map.put("isShowDel", po.getStatus() == 1?false:true);
				if (po.getStatus() == 1 || SessionUtil.isState()) {
					map.put("readOnly", true);
				} else {
					map.put("readOnly", false);
				}

				data.put("user", po.getMname());
				data.put("area", po.getArea());

				List<Map<String, Object>> list = declareService.getDeclareExtraData(Long.parseLong(id));
				data.put("data", list);
				List<String> userIdList = declareService.getUserIdList(id);
				if(userIdList==null||userIdList.size()==0){
					map.put("userIdList", "");
				}else{
					map.put("userIdList", userIdList.get(0));
				}
				
			}
		} else {
			// 新增
			map.put("readOnly", false);
			
			data = new HashMap<String, Object>();
			data.put("startDate", mdate);
			data.put("endDate", mdate);
			data.put("time", mdate);
			UserPo user = SessionUtil.getUserPo();
			if (user != null) {
				data.put("user", user.getMname());
				data.put("area", user.getArea());
			}
			map.put("isShowDel",false);
			map.put("status", "new");

		}
		if (readOnly || SessionUtil.isState()) {
			map.put("readOnly", true);
		}
		/**/
		if(SessionUtil.isState()){
			//如果是国调，必须要发布之后才能查看
			//判断是否结果发布
			boolean isJGFB = MatchUtil.isClickButton(mdate, Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_JGFB);
			if(isJGFB){
				//表示报价可以查看
				map.put("priceStatus", 0);
			}else{
				map.put("priceStatus", 1);
			}
		}else{
			map.put("priceStatus", 0);
		}
		
		if(!Boolean.parseBoolean(String.valueOf(map.get("readOnly")))){
			map.put("readOnly",!AuthoritiesUtil.isAllow("De_Btn_Submit"));
		}
		//System.out.println(CommonUtil.objToJson(map));
		map.put("dataInfo", CommonUtil.objToJson(data));
		map.put("id", SessionUtil.getUserPo().getId());
		SessionUtil.getSession().setAttribute("index", "0");
		ModelAndView view = new ModelAndView("declare/declare1", map);
		return view;
	}

	/**
	 * 新的界面可以增加设置多个时段，对应多个电力和电价
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年8月17日上午10:21:09
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/chart")
	public ModelAndView chart(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("跳转到用户信息界面");
		String id = request.getParameter("id");
		// Map<String,Object> data = null;
		// Map<String, Object> map = new HashMap<String, Object>();
		/*
		 * if(CommonUtil.ifEmpty(id)){ //编辑
		 * 
		 * DeclarePo po = declareService.getDeclare(Long.parseLong(id)); if(po
		 * != null){ data = new HashMap<String, Object>();
		 * Map<Float,Map<String,Float>> result =
		 * declareService.getDeclareExtraData(Long.parseLong(id),null);
		 * //System.out.println(CommonUtil.objToJson(result)); data.put("data",
		 * result); }
		 * 
		 * 
		 * Map<Integer,Integer> statusdata = new HashMap<Integer,Integer>();
		 * TreeMap<Integer, TreeMap<Float, Float>> treeMap =
		 * declareService.getDeclareIntervalData(Long.parseLong(id)); int offset
		 * = 1;
		 * 
		 * //System.out.println(CommonUtil.objToJson(treeMap)); int index = 0;
		 * TreeMap<Float, Float> prevEnt = null; TreeMap<Float, Float> nowEnt =
		 * null; //System.out.println("size==="+treeMap.size());
		 * for(Map.Entry<Integer, TreeMap<Float, Float>> intEnt :
		 * treeMap.entrySet()){ //System.out.println("index=="+index); index++;
		 * //System.out.println("======"+index); if(index == 1){
		 * statusdata.put(index, offset); }else{ boolean isSame = false; int i =
		 * 1; nowEnt = intEnt.getValue();
		 * 
		 * for(i=1;i<index;i++){ //获取上一个比较 prevEnt = treeMap.get(i); boolean
		 * isSingleSame = DeclareUtil.isSingleSame(prevEnt, nowEnt);
		 * //System.out
		 * .println(CommonUtil.objToJson(nowEnt)+"=="+isSingleSame+"=="
		 * +CommonUtil.objToJson(prevEnt));
		 * 
		 * if(isSingleSame){ isSame = true; break; } } if(isSame){
		 * statusdata.put(index, statusdata.get(i)); }else{
		 * statusdata.put(index, ++offset); } }
		 * 
		 * } map.put("statusData", CommonUtil.objToJson(statusdata));
		 * map.put("treeData", treeMap); }
		 * 
		 * map.put("dataInfo", CommonUtil.objToJson(data));
		 */

		ModelAndView view = new ModelAndView("declare/declare_sheet", null);
		return view;
	}

	@RequestMapping(value = "/getDeclareInfo", method = RequestMethod.POST)
	public Map getDeclareInfo(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("跳转到getDeclareInfo界面");
		String id = request.getParameter("id");
		Map<String, Object> data = null;
		Map<String, Object> map = new HashMap<String, Object>();
		DeclarePo po = null;
		try {
			if (CommonUtil.ifEmpty(id)) {
				// 编辑

				po = declareService.getDeclare(Long.parseLong(id));
				if (po != null) {
					data = new HashMap<String, Object>();
					Map<Float, Map<String, Float>> result = declareService.getDeclareExtraData(Long.parseLong(id), null);
					//System.out.println(CommonUtil.objToJson(result));
					map.put("dataInfo", result);
					if(SessionUtil.isState()){
						boolean isJGFB = MatchUtil.isClickButton(po.getStartDate(), Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_JGFB);//ServiceHelper.getBean(MatchService.class).isMatchBtn(po.getStartDate(), Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_JGFB);
						//System.out.println(po.getStartDate()+"--"+isJGFB);
						//如果还没有结果发布，而且是国调
						if(!isJGFB){
							if(result != null){
								int index = 1;
								Map<String,Map<String, Float>> dataInfo = new HashMap<String, Map<String,Float>>();
								for(Entry<Float, Map<String, Float>> entry : result.entrySet()){
									dataInfo.put("电价"+index++, entry.getValue());
								}
								map.put("dataInfo", dataInfo);
							}
						}
					}
				}

				Map<Integer, Integer> statusdata = new HashMap<Integer, Integer>();
				//20160920之前
				
				Map<String,Object> resultData = declareService.getDeclareIntervalData1(Long.parseLong(id));
				TreeMap<Integer, TreeMap<Float, Float>> treeMap = null;
				if(resultData != null){
					Object o = resultData.get("treeData");
					if(o != null){
						treeMap = (TreeMap<Integer, TreeMap<Float, Float>>)o;
					}
					o = resultData.get("statusData");
					if(o != null){
						statusdata = (Map<Integer, Integer>)o;
					}
				}
				
				
				map.put("statusData", statusdata);
				map.put("treeData", treeMap);
				ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "查看申报单信息，单号：" + po.getSheetName(), "getDeclare", String.valueOf(id), CommonUtil.objToJson(po), true);
				// System.out.println(CommonUtil.objToJson(map.get("dataInfo")));
				// System.out.println(CommonUtil.objToJson(treeMap));
			}
		} catch (Exception e) {
			ServiceHelper.getBean(PfLoggerServiceI.class).log(this.getClass().getName(), MODEL_NAME_, "查看申报单", "getDeclare", String.valueOf(id), "查看失败", false);

			e.printStackTrace();
		}

		// System.out.println(id);

		return map;
	}

	/**
	 * 新的界面可以增加设置多个时段，对应多个电力和电价
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年8月17日上午10:21:09
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public JsonMsg save(HttpServletRequest request, HttpServletResponse response, String id, String startDate, String endDate, String time, String data) {
		log.info("跳转到单据修改");
		JsonMsg j = new JsonMsg();

		if(request.getSession().getAttribute("isSign")==null){
			j.setMsg("先签名后保存！");
			return j;
		}
		String flag = (String)request.getSession().getAttribute("isSign");
		if(flag.equals("false")){
			j.setMsg("先签名后保存！");
			return j;
		}
		try {

			DeclarePo po = declareService.saveDeclare(id, null, null, startDate, endDate, time, data);
			j.setStatus(true);
			if (po != null) {
				j.setMsg(String.valueOf(po.getId()));
			} else {
				j.setMsg(null);
			}
			// System.out.println("========"+po.getId());
			//======保存Log
			//String mdate = time.replaceAll("-", "");
			request.getSession().setAttribute("isSign", "false");
			SessionUtil.getSession().setAttribute("index", "0");
		} catch (MsgException ex) {
			j.setMsg(ex.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("保存失败！");
		}
		return j;
	}

	/**
	 * 显示单个单据96个时段的报价曲线
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年8月25日下午4:22:49
	 * @param request
	 * @param response
	 * @param id
	 * @param startDate
	 * @param endDate
	 * @param time
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/showSingleDecInfo", method = RequestMethod.GET)
	public ModelAndView showSingleDecInfo(HttpServletRequest request, HttpServletResponse response, long id) {
		log.info("跳转到单据修改");
		try {
			// 用来存储每个时段，他的变化值
			Map<Integer, Integer> data = new HashMap<Integer, Integer>();
			TreeMap<Integer, TreeMap<Float, Float>> treeMap = declareService.getDeclareIntervalData(id);
			int offset = 1;

			// System.out.println(CommonUtil.objToJson(treeMap));
			int index = 0;
			TreeMap<Float, Float> prevEnt = null;
			TreeMap<Float, Float> nowEnt = null;
			// System.out.println("size==="+treeMap.size());
			for (Map.Entry<Integer, TreeMap<Float, Float>> intEnt : treeMap.entrySet()) {
				// System.out.println("index=="+index);
				index++;
				// System.out.println("======"+index);
				if (index == 1) {
					data.put(index, offset);
				} else {
					boolean isSame = false;
					int i = 1;
					nowEnt = intEnt.getValue();

					for (i = 1; i < index; i++) {
						// 获取上一个比较
						prevEnt = treeMap.get(i);
						boolean isSingleSame = DeclareUtil.isSingleSame(prevEnt, nowEnt);
						// System.out.println(CommonUtil.objToJson(nowEnt)+"=="+isSingleSame+"=="+CommonUtil.objToJson(prevEnt));

						if (isSingleSame) {
							isSame = true;
							break;
						}
					}
					if (isSame) {
						data.put(index, data.get(i));
					} else {
						data.put(index, ++offset);
					}
				}

			}
			// System.out.println(CommonUtil.objToJson(data));

		} catch (MsgException ex) {

		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		// model.addAttribute("jspType", "issue");
		ModelAndView view = new ModelAndView("declare/declareInfo", map);
		return view;
	}

	@RequestMapping(value = "/copyDeclareData", method = RequestMethod.POST)
	@ResponseBody
	public JsonMsg copyDeclareData(HttpServletRequest request, String time, String mdate) {

		JsonMsg j = new JsonMsg();
		UserPo user = (UserPo) request.getSession().getAttribute("userInfo");
		String area = user.getArea();

		List<DeclarePo> declareList = declareService.queryForList(time, area, null);
		// System.out.println("===="+declareList.size()+"=="+time);
		try {
			if (declareList.size() > 0) {
				for (int i = 0; i < declareList.size(); i++) {
					// System.out.println(declareList.get(i).getId());
					List<Map<String, Object>> list = declareService.getDeclareExtraData(declareList.get(i).getId());
					String data = CommonUtil.objToJson(list);
					declareService.saveDeclare(null, null, null, mdate, mdate, time, data);
				}
				j.setStatus(true);

			} else {
				j.setStatus(false);
				j.setMsg("没有数据！");
			}

		} catch (MsgException e) {
			j.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("复制失败！");
		}

		return j;
	}

	/**
	 * 导入各省申报数据
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月19日下午2:55:36
	 * @param mdate
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/importFromInterface")
	public JsonMsg importFromInterface(String mdate) {
		log.info("@ importFromInterface ");
		// System.out.println("++++++++++++++开始调用C程序======>plan_copy2.sh++++++++++++");
		JsonMsg j = ServiceHelper.getBean(ShellExecuteServiceI.class).importDeclare(false,mdate);
		// System.out.println("++++++++++++++开始调用C程序======>insert_cbpm2.sh++++++++++++");
		return j;
	}

	/**
	 * 交易申报中申报监视中各区域申报单状态数据
	 * @description
	 * @author 大雄
	 * @date 2016年10月12日下午7:49:22
	 * @param request
	 * @param mdate
	 * @return
	 */
	@RequestMapping(value = "/getAreaSheet", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, List<Map<String,Integer>>>> getAreaSheet(HttpServletRequest request, String mdate) {
		//System.out.println("==="+mdate);
		List<AreaPo> list = declareService.getAreaSheetStatusByMdate(mdate);
		
		
		System.out.println(CommonUtil.objToJson(list));
		
		//Map<String,Map<String,Integer>> map = new HashMap<String, Map<String,Integer>>();
		Map<String,List<Map<String,Integer>>> result = new TreeMap<String,List<Map<String,Integer>>>();
		if(list != null){
			Map<String,Integer> temp = null;
			for(AreaPo po : list){
//				if(map.containsKey(po.getRegion())){
//					temp = map.get(po.getRegion());
//				}else{
//					temp = new HashMap<String, Integer>();
//					map.put(po.getRegion(), temp);
//				}
//				temp.put(po.getArea(), po.getCount()==0?1:0);
				List<Map<String,Integer>> li = null;
				if(result.containsKey(po.getRegion())){
					li = result.get(po.getRegion());
				}else{
					li = new ArrayList<Map<String,Integer>>();
					result.put(po.getRegion(), li);
				}
				Map<String,Integer> tempMap = new HashMap<String,Integer>();
				//count 为1是已提交，为2是未提交
				if(po.getIsSheet()==0){
					//未申报
					tempMap.put(po.getArea(), 0);
				}else{
					tempMap.put(po.getArea(), po.getCount()==null||po.getCount()==0?2:1);
				}
				li.add(tempMap);
			}
		}
		String[] regions = {"华北","华东","华中","东北","西北","西南"};
		List<Map<String, List<Map<String,Integer>>>> resultList = new ArrayList<Map<String, List<Map<String,Integer>>>>();
		for(String region : regions){
			Map<String, List<Map<String,Integer>>> temp = new HashMap<String, List<Map<String,Integer>>>();
			temp.put(region, result.get(region));
			resultList.add(temp);
		}
		System.out.println(CommonUtil.objToJson(resultList));
		return resultList;
	}

	
}
