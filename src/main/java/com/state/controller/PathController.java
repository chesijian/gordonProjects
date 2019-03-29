package com.state.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.state.po.CorridorPathPo;
import com.state.po.LineDefinePo;
import com.state.po.PathDefinePo;
import com.state.service.CorridorPathServiceI;
import com.state.service.ILineService;
import com.state.service.IPathService;
import com.state.util.CommonUtil;
import com.state.util.TimeUtil;

/**
 * 通道controller
 * @author 帅
 *
 */
@Controller
@RequestMapping("/path")
public class PathController {
	private static final transient Logger log = Logger
			.getLogger(PathController.class);

	@Autowired
	private ILineService lineService;
	
	@Autowired
	private IPathService pathService;
	
	@Autowired
	private CorridorPathServiceI corridorPathService;
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	
	/**
	 * 跳转页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/init")
	public String init(HttpServletRequest request,HttpServletResponse response,Model model) {
		log.info("@ init limitLine ");
		//获取当前服务器时间
		model.addAttribute("serviceDate", TimeUtil.getStringDate());
		request.setAttribute("jspType", "path");
		return "path";
	}
	
	/**
	 * 跳转报价申报
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/bidOffer")
	public String bidOffer(HttpServletRequest request,HttpServletResponse response) {
		log.info("@ init bidOffer ");
		request.setAttribute("jspType", "path");
		return "path/bidOffer";
	}
	
	/**
	 * 查询所有的通道
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAllPath")
	public List<PathDefinePo> getAllPath(HttpServletRequest request,HttpServletResponse response,String time) {
		log.info("@ init getAllPath ");
		time = time.replaceAll("-", "");
		List<PathDefinePo> list = pathService.getAllPath(time);
		return list;
	}
	
	/**
	 * 根据通道定义Id 获取所有通道列表
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getCorridorPathList")
	public List<CorridorPathPo> getCorridorPathList(HttpServletRequest request,HttpServletResponse response,String pathId) {
		log.info("@ init getAllPath ");
		List<CorridorPathPo> list = corridorPathService.getAllCorridorPathByPathId(pathId);
		return list;
	}
	
	/**
	 * 查询所有的通道
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAllLine")
	public List<LineDefinePo> getAllLine(HttpServletRequest request,HttpServletResponse response) {
		log.info("@ init getAllLine ");
		return lineService.getAllLine();
	}
	
	/**
	 * 添加通道
	 * @param pathDefine
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addPath")
	public String addPath(String pathDefine,String pathId,String time,String action){
		log.info(" add path");
		SimpleDateFormat formart = new SimpleDateFormat("yyyyMMdd");
		JSONObject bean = com.alibaba.fastjson.JSONObject.parseObject(pathDefine);
		PathDefinePo javaObject = JSONObject.toJavaObject(bean, PathDefinePo.class);
		time = time.replaceAll("-", "");
		int flag = 0;
		try {
			flag = formart.parse(time).compareTo(formart.parse(formart.format(new Date(new Date().getTime()+24*3600*1000))));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		try {
			if (action.equals("copy")&& CommonUtil.ifEmpty(pathId)) {
				// 判断通道是否存在
				List<PathDefinePo> pathList = pathService.selectPathByName(time, javaObject.getMpath());
				if (pathList.size() > 0) {
					return "当前通道已经存在";
				}
				// 先拷贝通道
				String newPathId = CommonUtil.getUUID();
				javaObject.setId(newPathId);
				javaObject.setStartDate(time);
				pathService.addPath(javaObject);
				// 拷贝通道路径
				List<CorridorPathPo> list = corridorPathService.getAllCorridorPathByPathId(pathId);
				for (CorridorPathPo po : list) {
					po.setId(CommonUtil.getUUID());
					po.setPathUid(newPathId);
					corridorPathService.insertCorridorPath(po);
				}

			} else {
				List<PathDefinePo> curryList = pathService.selectPathByName(time, javaObject.getMpath());
				if (curryList.size() > 0) {
					return "当前通道已经存在";
				}
				if (pathId == "") {// 添加通道
					if (flag == 0) {// 如果参数日期和当前系统日期一致 直接添加
						javaObject.setId(CommonUtil.getUUID());
						javaObject.setStartDate(time);
						pathService.addPath(javaObject);
					} else if (flag == -1) {// 如果在历史数据添加通道 startDate和endDate
											// 都等于time

						javaObject.setId(CommonUtil.getUUID());
						javaObject.setStartDate(time);
						javaObject.setEndDate(time);
						pathService.addPath(javaObject);
					}else{
						//如果传参时间大于当前时间
						javaObject.setId(CommonUtil.getUUID());
						javaObject.setStartDate(time);
						javaObject.setEndDate(null);
						pathService.addPath(javaObject);
					}

				} else {// 修改通道
					PathDefinePo po = pathService.getPathDefineById(pathId);
					String oldStartDate = po.getStartDate();
					String oldId = po.getId();
					time = time.replaceAll("-", "");
					if (flag == 0) {// 如果参数日期等于系统日期
						int flg = formart.parse(po.getStartDate()).compareTo(formart.parse(time));
						if (flg == 0) {// 则判断如果startDate=sysDate则直接修改
							javaObject.setId(pathId);
							javaObject.setId(po.getId());
							javaObject.setStartDate(po.getStartDate());
							pathService.updatePathDefine(javaObject);
						} else if (flg == -1) {
							// 拷贝数据，并且修改拷贝数据的startDate = sysDate,
							String newId = CommonUtil.getUUID();

							List<CorridorPathPo> list = corridorPathService.getAllCorridorPathByPathId(po.getId());
							if (list != null) {
								for (CorridorPathPo temp : list) {
									temp.setId(CommonUtil.getUUID());
									temp.setPathUid(newId);
									corridorPathService.insertCorridorPath(temp);
								}
							}
							javaObject.setId(newId);
							javaObject.setStartDate(time);
							//javaObject.setEndDate(time);
							pathService.addPath(javaObject);
							
							// 如果startDate<sysDate，修改当前数据的endDate = sysDate
							//po.setId(newId);
							po.setEndDate(time);
							pathService.updatePathDefine(po);
							
						}

					} else if (flag == -1) {// 如果切换到历史日期
						Date startDate = formart.parse(po.getStartDate());
						Date endDate = null;
						if (po.getEndDate() != null && !po.getEndDate().equals("")) {
							endDate = formart.parse(po.getEndDate());
						}
						Date curryDate = formart.parse(time);
						Date nextDate = formart.parse(getNextDateString(curryDate));
						
						System.out.println(curryDate+"---"+nextDate+"---"+startDate+"--"+endDate);
						if (startDate.compareTo(curryDate) == 0) {// 如果startDate==参数日期
							// 同时拷贝该记录,修改 拷贝数据的startDate = (time+1);
							String newId = CommonUtil.getUUID();
							List<CorridorPathPo> list = corridorPathService.getAllCorridorPathByPathId(po.getId());
							if (list != null) {
								for (CorridorPathPo temp : list) {
									temp.setId(CommonUtil.getUUID());
									temp.setPathUid(newId);
									corridorPathService.insertCorridorPath(temp);
								}
							}
							po.setId(newId);
							po.setStartDate(getNextDateString(curryDate));
							pathService.addPath(po);

							
							// 如果该记录startDate==11.6(time)，则，修改当前数据的endDate
							// =(time)，
							javaObject.setId(oldId);
							javaObject.setStartDate(oldStartDate);
							javaObject.setEndDate(time);
							pathService.updatePathDefine(javaObject);
							
						} else if (startDate.compareTo(curryDate) == -1 && endDate != null && endDate.compareTo(nextDate) == 0) {
							// 同时拷贝该记录,修改拷贝记录的startDate=11.6(time)
							String newId = CommonUtil.getUUID();

							List<CorridorPathPo> list = corridorPathService.getAllCorridorPathByPathId(po.getId());
							if (list != null) {
								for (CorridorPathPo temp : list) {
									temp.setId(CommonUtil.getUUID());
									temp.setPathUid(newId);
									corridorPathService.insertCorridorPath(temp);
								}
							}
							po.setId(newId);
							po.setStartDate(oldStartDate);
							po.setEndDate(time);
							pathService.addPath(po);

							
							// 如果该记录startDate<(time) and
							// endDate=(time+1),修改当前数据的endDate=(time)
							javaObject.setId(oldId);
							javaObject.setStartDate(time);
							javaObject.setEndDate(time);
							pathService.updatePathDefine(javaObject);
							
						} else if (startDate.compareTo(curryDate) == -1 && endDate != null && endDate.compareTo(nextDate) == 1) {
							// 同时修改拷贝两条记录
							// 修改拷贝记录一的startDate=(time),endDate=(time)
							String newId = CommonUtil.getUUID();
							List<CorridorPathPo> list = corridorPathService.getAllCorridorPathByPathId(po.getId());
							if (list != null) {
								for (CorridorPathPo temp : list) {
									temp.setId(CommonUtil.getUUID());
									temp.setPathUid(newId);
									corridorPathService.insertCorridorPath(temp);
								}
							}
							po.setId(newId);
							po.setStartDate(oldStartDate);
							po.setEndDate(time);
							pathService.addPath(po);
							// 修改拷贝记录二的startDate=(time+1),
							newId = CommonUtil.getUUID();

							list = corridorPathService.getAllCorridorPathByPathId(po.getId());
							if (list != null) {
								for (CorridorPathPo temp : list) {
									temp.setId(CommonUtil.getUUID());
									temp.setPathUid(newId);
									corridorPathService.insertCorridorPath(temp);
								}
							}
							po.setId(newId);
							po.setStartDate(formart.format(nextDate));
							pathService.addPath(po);
							
							// 如果该记录startDate<(time) and
							// endDate>(time+1),修改当前数据的endDate=11.6(time)
							javaObject.setId(oldId);
							javaObject.setStartDate(time);
							javaObject.setEndDate(time);
							pathService.updatePathDefine(javaObject);
							
						} else if (startDate.compareTo(curryDate) == -1 && po.getEndDate() == null) {
							// 如果该记录startDate<(time) and
							// 同时拷贝该记录,修改拷贝记录的startDate=11.6(time)

							String newId = CommonUtil.getUUID();
							List<CorridorPathPo> list = corridorPathService.getAllCorridorPathByPathId(oldStartDate);
							if (list != null) {
								for (CorridorPathPo temp : list) {
									temp.setId(CommonUtil.getUUID());
									temp.setPathUid(newId);
									corridorPathService.insertCorridorPath(temp);
								}
							}
							
							po.setId(newId);
							po.setStartDate(oldStartDate);
							po.setEndDate(time);
							pathService.addPath(po);
							
							// 修改拷贝记录二的startDate=(time+1),
							newId = CommonUtil.getUUID();

							//list = corridorPathService.getAllCorridorPathByPathId(oldStartDate);
							if (list != null) {
								for (CorridorPathPo temp : list) {
									temp.setId(CommonUtil.getUUID());
									temp.setPathUid(newId);
									corridorPathService.insertCorridorPath(temp);
								}
							}
							
							po.setId(newId);
							po.setStartDate(formart.format(nextDate));
							po.setEndDate(null);
							pathService.addPath(po);
							
							// endDate=null,修改当前数据的endDate=(time)
							javaObject.setId(oldId);
							javaObject.setStartDate(time);
							javaObject.setEndDate(time);
							pathService.updatePathDefine(javaObject);
							
						}
					}

				}
			}

		} catch (Exception e) {
			log.error("add pathDefine fail !", e);
			return "fail";
		}
		return "success";
	}
	
	/**
	 * 
	 * @param pathDefine
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addCorridorPath")
	public String addCorridorPath(String pathDefine,String id,String action,String newStr,String datas){
		log.info(" add path");
		JSONObject bean = com.alibaba.fastjson.JSONObject.parseObject(pathDefine);
		CorridorPathPo javaObject = JSONObject.toJavaObject(bean, CorridorPathPo.class);
		javaObject.setData(datas);
		try {
			if(action.equals("copy")){
				//CorridorPathPo po = corridorPathService.getCorridorPathById(id);
				//String oldString = getCorhrAndMdirectionString(po);
				String oldString = getOldCorhrAndMdiration(id);
				if(oldString.equals(newStr)){
					return "拷贝配置路径不能完全一致！";
				}
				javaObject.setId(CommonUtil.getUUID());
				corridorPathService.insertCorridorPath(javaObject);
			}else{
				if(id==""||id==null){
					String pathId = javaObject.getPathUid();
					List<CorridorPathPo> corList = corridorPathService.getAllCorridorPathByPathId(pathId);
					for(CorridorPathPo corPo:corList){
						String oldString = getCorhrAndMdirectionString(corPo);
						if (oldString.equals(newStr)) {
							return "新增配置路径已重复！";
						}
					}
					javaObject.setId(CommonUtil.getUUID());
					corridorPathService.insertCorridorPath(javaObject);
				}else{
					//先删除后增加
					corridorPathService.deleteCorridorPathById(id);
					javaObject.setId(id);
					corridorPathService.insertCorridorPath(javaObject);
				}
			}
			
		} catch (Exception e) {
			log.error("add pathDefine fail !",e);
			return "fail";
		}
		return "success";
	}

	
	/**
	 * 修改通道
	 * @param pathDefine
	 * @return
	 */
	@RequestMapping(value = "/editPath")
	@ResponseBody
	public PathDefinePo editPath(String pathId,Model model){
		log.info(" edit path");
		PathDefinePo pdo =pathService.getPathDefineById(pathId);
		return pdo;
	}
	
	/**
	 * 修改通道
	 * @param pathDefine
	 * @return
	 */
	@RequestMapping(value = "/editCorPath")
	@ResponseBody
	public CorridorPathPo editCorPath(String id,Model model){
		log.info(" edit path");
		CorridorPathPo pdo =corridorPathService.getCorridorPathById(id);
		return pdo;
	}

	/**
	 * 修改联络线
	 * @author 车斯剑
	 * @param pathDefine
	 * @return
	 */
	@RequestMapping(value = "/editLine")
	@ResponseBody
	public LineDefinePo editLine(String lineId,Model model){
		log.info(" edit line");
		LineDefinePo po =lineService.getLineDefineById(lineId);
		return po;
	}
	

	
	/**
	 * 添加联络线
	 * @param lineDefine
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/addLine")
	public String addLine(String lineDefine,String lineId){
		System.out.println(lineDefine);
		JSONObject bean = com.alibaba.fastjson.JSONObject.parseObject(lineDefine);
		LineDefinePo javaObject = JSONObject.toJavaObject(bean, LineDefinePo.class);
		if(lineId==""){
			if(lineService.existsLine(javaObject.getMcorhr())){
				return "联络线已经存在";
			}
			try {
				javaObject.setId(CommonUtil.getUUID());
				lineService.addLine(javaObject);
			} catch (Exception e) {
				log.error("add lineDefine fail !",e);
				return "fail";
			}
		}else{
			javaObject.setId(lineId);
			lineService.updateLineDefine(javaObject);
		}
		
		return "success";
	}
	
	/**
	 * 删除通道
	 * @param mpath
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deletePath")
	public String deletePath(String pathId,String time){
		SimpleDateFormat formart = new SimpleDateFormat("yyyyMMdd");
		try {
			PathDefinePo po = pathService.getPathDefineById(pathId);
			//如果当前日期等于系统日期
			if(formart.parse(time).compareTo(formart.parse(formart.format(new Date(new Date().getTime()+24*3600*1000))))==0){
				int flag = formart.parse(po.getStartDate()).compareTo(formart.parse(formart.format(new Date(new Date().getTime()+24*3600*1000))));
				if(flag==0){
					pathService.deletePath(pathId);
					corridorPathService.deleteCorridorPathByUpathId(pathId);
				}else if(flag==-1){
					po.setEndDate(formart.format(new Date(new Date().getTime()+24*3600*1000)));
					pathService.updatePathDefine(po);
				}
			}else if(formart.parse(time).compareTo(formart.parse(formart.format(new Date(new Date().getTime()+24*3600*1000))))==-1){
				//如果删除历史的通道数据
				Date startDate = formart.parse(po.getStartDate());
				Date curryDate = formart.parse(time);
				if(startDate.compareTo(curryDate)==0){//如果历史日期等于当前记录的startDate
					String newId = CommonUtil.getUUID();
					po.setId(newId);
					po.setStartDate(getNextDateString(curryDate));//先拷贝当前记录并更新startDate为参数日期的后一天，然后删除当前记录
					pathService.addPath(po);
					List<CorridorPathPo> list = corridorPathService.getAllCorridorPathByPathId(po.getId());
					if(list != null){
						for(CorridorPathPo temp:list){
							temp.setId(CommonUtil.getUUID());
							temp.setPathUid(newId);
							corridorPathService.insertCorridorPath(temp);
						}
					}
					pathService.deletePath(pathId);
					
				}else if(startDate.compareTo(curryDate)==-1){//如果历史日期小于当前记录的startDate
					//修改当前数据的endDate=参数日期
					po.setEndDate(time);
					pathService.updatePathDefine(po);
					
					//拷贝一条记录,修改拷贝记录的startDate为参数日期的下一天
					String newId = CommonUtil.getUUID();
					po.setId(newId);
					po.setStartDate(getNextDateString(curryDate));
					List<CorridorPathPo> list = corridorPathService.getAllCorridorPathByPathId(po.getId());
					if(list != null){
						for(CorridorPathPo temp:list){
							temp.setId(CommonUtil.getUUID());
							temp.setPathUid(newId);
							corridorPathService.insertCorridorPath(temp);
						}
					}
					pathService.addPath(po);
					
				}
			}else{
				//如果当前时间大于系统时间
				//如果当前时间大于系统时间
				Date startDate = formart.parse(po.getStartDate());
				Date endDate = null;
				if(po.getEndDate()!=null){
					endDate = formart.parse(po.getEndDate());
				}
				
				Date curryDate = formart.parse(time);
				if(startDate.compareTo(curryDate) == 0&&po.getEndDate()==null) {// 如果当前日期等于当前记录的startDate
					corridorPathService.deleteCorridorPathByUpathId(pathId);
					pathService.deletePath(pathId);
				}else if(startDate.compareTo(curryDate) == -1&&po.getEndDate()==null){//start<curry&&end==null
					po.setEndDate(getPreDateString(formart.parse(time)));
					pathService.updatePathDefine(po);
					
					String newId = CommonUtil.getUUID();
					List<CorridorPathPo> list = corridorPathService.getAllCorridorPathByPathId(po.getId());
					if (list != null) {
						for (CorridorPathPo temp : list) {
							temp.setId(CommonUtil.getUUID());
							temp.setPathUid(newId);
							corridorPathService.insertCorridorPath(temp);
						}
					}
					po.setId(newId);
					po.setStartDate(getNextDateString(formart.parse(time)));
					po.setEndDate(null);
					pathService.addPath(po);
				}else if(startDate.compareTo(curryDate) == 0 && po.getEndDate()!=null){
					if(endDate.compareTo(curryDate)==0){//start==curry==end
						corridorPathService.deleteCorridorPathByUpathId(pathId);
						pathService.deletePath(pathId);
					}else if(endDate.compareTo(curryDate)==1){//start==curry<end
						po.setStartDate(getNextDateString(formart.parse(time)));
						pathService.updatePathDefine(po);
					}
					
				}else if(startDate.compareTo(curryDate) == -1&&po.getEndDate()!=null){
					if(endDate.compareTo(curryDate)==0){ //start<curry=end
						po.setEndDate(getPreDateString(formart.parse(time)));
						pathService.updatePathDefine(po);
						
					}else if(endDate.compareTo(curryDate)==1){// start<curry<end
						
						po.setEndDate(getPreDateString(formart.parse(time)));
						pathService.updatePathDefine(po);//结束日期置为前一天
						
						String newId = CommonUtil.getUUID();
						List<CorridorPathPo> list = corridorPathService.getAllCorridorPathByPathId(po.getId());
						if (list != null) {
							for (CorridorPathPo temp : list) {
								temp.setId(CommonUtil.getUUID());
								temp.setPathUid(newId);
								corridorPathService.insertCorridorPath(temp);
							}
						}
						po.setId(newId);
						po.setStartDate(getNextDateString(formart.parse(time)));
						pathService.addPath(po);
					}
				}
			}
			
			
		} catch (Exception e) {
			log.error("delete pathDefine fail !",e);
			return "fail";
		}
		return "success";
	}
	
	/**
	 * @param mpath
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteCorPath")
	public String deleteCorPath(String id){
		
		try {
			corridorPathService.deleteCorridorPathById(id);
		} catch (Exception e) {
			log.error("delete pathDefine fail !",e);
			return "fail";
		}
		return "success";
	}
	
	/**
	 * 删除联络线
	 * @param mcorhr
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteLine")
	public String deleteLine(String mcorhr){
		
		if(!lineService.existsLine(mcorhr)){
			return "联络线不存在";
		}
		try {
			lineService.deleteLine(mcorhr);
		} catch (Exception e) {
			log.error("delete lineDefine fail !",e);
			return "fail";
		}
		return "success";
	}
	
	private String getOldCorhrAndMdiration(String id){
		CorridorPathPo po = corridorPathService.getCorridorPathById(id);
		String oldString = getCorhrAndMdirectionString(po);
		return oldString;
	}
	
	private String getCorhrAndMdirectionString(CorridorPathPo corriddorPo){
		StringBuffer buffer = new StringBuffer("");	
		if(corriddorPo.getCorhr1()!=null&&!corriddorPo.getCorhr1().equals("")&&!corriddorPo.getCorhr1().isEmpty()){
			buffer.append(corriddorPo.getCorhr1());
		}
		if(corriddorPo.getCorhr2()!=null&&!corriddorPo.getCorhr2().equals("")&&!corriddorPo.getCorhr2().isEmpty()){
			buffer.append(corriddorPo.getCorhr2());
		}
		if(corriddorPo.getCorhr3()!=null&&!corriddorPo.getCorhr3().equals("")&&!corriddorPo.getCorhr3().isEmpty()){
			buffer.append(corriddorPo.getCorhr3());
		}
		if(corriddorPo.getCorhr4()!=null&&!corriddorPo.getCorhr4().equals("")&&!corriddorPo.getCorhr4().isEmpty()){
			buffer.append(corriddorPo.getCorhr4());
		}
		if(corriddorPo.getCorhr5()!=null&&!corriddorPo.getCorhr5().equals("")&&!corriddorPo.getCorhr5().isEmpty()){
			buffer.append(corriddorPo.getCorhr5());
		}
		if(corriddorPo.getCorhr6()!=null&&!corriddorPo.getCorhr6().equals("")&&!corriddorPo.getCorhr6().isEmpty()){
			buffer.append(corriddorPo.getCorhr6());
		}
		if(corriddorPo.getCorhr7()!=null&&!corriddorPo.getCorhr7().equals("")&&!corriddorPo.getCorhr7().isEmpty()){
			buffer.append(corriddorPo.getCorhr7());
		}
		if(corriddorPo.getCorhr8()!=null&&!corriddorPo.getCorhr8().equals("")&&!corriddorPo.getCorhr8().isEmpty()){
			buffer.append(corriddorPo.getCorhr8());
		}
		if(corriddorPo.getCorhr9()!=null&&!corriddorPo.getCorhr9().equals("")&&!corriddorPo.getCorhr9().isEmpty()){
			buffer.append(corriddorPo.getCorhr9());
		}
		if(corriddorPo.getCorhr10()!=null&&!corriddorPo.getCorhr10().equals("")&&!corriddorPo.getCorhr10().isEmpty()){
			buffer.append(corriddorPo.getCorhr10());
		}
		if(corriddorPo.getMdirection()!=null&&!corriddorPo.getMdirection().equals("")&&!corriddorPo.getMdirection().isEmpty()){
			buffer.append(corriddorPo.getMdirection());
		}
		return buffer.toString();
				
	}
	
	private String getNextDateString(Date date){
		SimpleDateFormat formart = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(calendar.DAY_OF_YEAR, 1);
		return formart.format(calendar.getTime());
		
	}
	
	
	private String getPreDateString(Date date) {
		SimpleDateFormat formart = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(calendar.DAY_OF_YEAR, -1);
		return formart.format(calendar.getTime());

	}

}
