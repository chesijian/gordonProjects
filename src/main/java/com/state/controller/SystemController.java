package com.state.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.state.exception.MsgException;
import com.state.po.extend.DataGrid;
import com.state.po.extend.JsonMsg;
import com.state.po.org.OrgResourcesPo;
import com.state.po.org.OrgRolePo;
import com.state.po.org.OrgRoleResourcesPo;
import com.state.service.SystemServiceI;
import com.state.service.org.OrgResourcesServiceI;
import com.state.service.org.OrgRoleResourcesServiceI;
import com.state.service.org.OrgRoleServiceI;
import com.state.util.Attributes;
import com.state.util.AuthoritiesUtil;
import com.state.util.BtnTimeUtil;
import com.state.util.CommonUtil;
import com.state.util.DeclareUtil;
import com.state.util.MParentTree;
import com.state.util.SessionUtil;
import com.state.util.TimeUtil;
import com.state.util.sys.SystemConstUtil;

@Controller
@RequestMapping("/system")
public class SystemController {
	private static final transient Logger log = Logger
			.getLogger(SystemController.class);

	@Autowired
	private SystemServiceI systemService;
	@Autowired
	private OrgRoleResourcesServiceI roleResourcesService;
	@Autowired
	private OrgResourcesServiceI resourcesService;
	
	@Autowired
	private OrgRoleServiceI orgRoleService;
	/**
	 * 跳转发布页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/init")
	public ModelAndView init(Model model) {
		log.info("@ init system ");
		Map<String, Object> map = new HashMap<String, Object>();	
		List<OrgRolePo> list = orgRoleService.selectList(0);
			
		map.put("dataInfo", CommonUtil.objToJson(list));
		//model.addAttribute("areaList", JSON.toJSON(areaService.getAllArea()).toString());
		model.addAttribute("jspType", "system");
		//获取当前服务器时间
		map.put("serviceDate", TimeUtil.getStringDate());
		ModelAndView view = new ModelAndView("system/index",map);
		return view;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getLoginData", method = RequestMethod.POST)
	public DataGrid getLoginData(HttpServletRequest request, HttpServletResponse response,String startTime,String endTime,int pageIndex,int limit){
		DataGrid dg = new DataGrid();
		if(!CommonUtil.ifEmpty(startTime)){
			//System.out.println("=1=");
			startTime = null;
		}
		if(!CommonUtil.ifEmpty(endTime)){
			//System.out.println("=2=");
			endTime = null;
		}
		dg.setTotal(systemService.getOnlineUserCount(startTime, endTime));
		dg.setRows(systemService.getOnlineUser(startTime, endTime, pageIndex, pageIndex+limit));
		return dg;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getLogData", method = RequestMethod.POST)
	public DataGrid getLogData(HttpServletRequest request, HttpServletResponse response,String startTime,String endTime,int pageIndex,int limit){
		DataGrid dg = new DataGrid();
		if(!CommonUtil.ifEmpty(startTime)){
			//System.out.println("=1=");
			startTime = null;
		}
		if(!CommonUtil.ifEmpty(endTime)){
			//System.out.println("=2=");
			endTime = null;
		}
		dg.setTotal(systemService.getLogCount(startTime, endTime));
		dg.setRows(systemService.getLog(startTime, endTime, pageIndex, pageIndex+limit));
		return dg;
	}
	
	/**
	 * 根据角色id获取角色权限
	 * @param request
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value = "/getRoleResourcesList", method = RequestMethod.POST)
	public String getRoleResourcesList(HttpServletRequest request,String roleUid) throws ParseException {
		 
		List<OrgRoleResourcesPo> roleResourcesList = roleResourcesService.selectListByRole(roleUid);
		//System.out.println("roleResourcesList==="+CommonUtil.objToJson(roleResourcesList));
		return CommonUtil.objToJson(roleResourcesList);
	}
	
	@ResponseBody
	@RequestMapping(value = "/saveRoleResources", method = RequestMethod.POST)
	public Object saveRoleResources(HttpServletRequest request,String ids,String resourcesIds,String roleUid) throws ParseException {
		 
		JsonMsg j = new JsonMsg();
		
		/*
		if(!(SessionUtil.isAdmin() || (SessionUtil.isManger()&& SessionUtil.isState()))){
			j.setStatus(false);
			j.setMsg("对不起您没有权限操作!");
			return j;
		}
		*/
		
		System.out.println("------33-------");
		String[] str = resourcesIds.split(",");
		String[] resourcesUids = ids.split(",");
		System.out.println("------11-------");
		List<OrgRoleResourcesPo> orrpList = new ArrayList<OrgRoleResourcesPo>();
		//int i = 0;
		for(int i=0;i<str.length;i++){
			OrgRoleResourcesPo rrp = new OrgRoleResourcesPo();
			rrp.setAddName(SessionUtil.getAddName());
			rrp.setAddNameCn(SessionUtil.getAddNameCn());
			rrp.setDocCreated(new Date());
			rrp.setId(CommonUtil.getUUID());
			rrp.setResourceId(str[i]);
			rrp.setRoleUid(roleUid);
			rrp.setResourceUid(resourcesUids[i]);
			orrpList.add(rrp);
		}
		/*for (String temp : str) {
			OrgRoleResourcesPo rrp = new OrgRoleResourcesPo();
			rrp.setAddName(SessionUtil.getAddName());
			rrp.setAddNameCn(SessionUtil.getAddNameCn());
			rrp.setDocCreated(new Date());
			rrp.setId(CommonUtil.getUUID());
			rrp.setResourceId(temp);
			rrp.setRoleUid(roleUid);
			orrpList.add(rrp);
		}*/
		try{
			System.out.println("------ww-------");
			roleResourcesService.insertRoleResources(orrpList,roleUid);
			System.out.println("------22-------");
			j.setStatus(true);
		}catch(MsgException me){
			j.setMsg(me.getMessage());
			me.printStackTrace();
		}catch(Exception e){
			j.setMsg("提交失败！");
			e.printStackTrace();
		}finally{
			//System.out.println("6668888===="+CommonUtil.objToJson(j));
			return j;
		}
	}
	
	// 获取权限树结构
	@ResponseBody
	@RequestMapping(value = "/getResourcesList", method = RequestMethod.POST)
	public List<MParentTree> getResourcesList(HttpServletRequest request,String time) throws ParseException {
//		Map<String,List<MParentTree>> map = new HashMap<String, List<MParentTree>>();
//		 List<MParentTree> list_rq = getChildrenList("root");
//		 List<MParentTree> list_rn = getChildrenList("root_in");
//		 map.put("rqData", list_rq);
//		 map.put("rnData", list_rn);
		 return getChildrenList("root");

	}
	//================================================
	protected List<MParentTree> getChildrenList(String pid){
		 List<OrgResourcesPo> resourcesList = resourcesService.selectResourcesListByPid(pid);
		 List<MParentTree> childrenList = new ArrayList<MParentTree>();
		 if(resourcesList.size()>0){
			 //MParentTree AreaNode = new MParentTree();
			 
			 for(OrgResourcesPo po:resourcesList){
				 MParentTree rootNode = new MParentTree();
				 rootNode.setBill(true);					 
				 rootNode.setId(String.valueOf(po.getId()));
				 rootNode.setpId(String.valueOf(po.getPid()));

				 Attributes attributes = new Attributes();
				 attributes.setRname(po.getResourceId());
				 rootNode.setText(po.getName());
				 rootNode.setState("open");
				 rootNode.setAttributes(attributes);
				 childrenList.add(rootNode);
				 rootNode.setChildren(getChildrenList(po.getId()));				
				 //rootList.add(rootNode);
				 
			 }
			 //AreaNode.setChildren(getChildrenList(po.getId()));
			 //AreaNode.setChildren(rootList);
			 //list.add(rootList);	 
		 }
		return childrenList;
	}

	
	/**
	 * 获取流程按钮的状态
	 * @description
	 * @author 大雄
	 * @date 2016年10月8日下午9:40:33
	 * @param request
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value = "/getProcessBtnStatus", method = RequestMethod.POST)
	public Map<String,Object> getProcessBtnStatus(HttpServletRequest request,String time) throws ParseException {
		systemService.updateProcessBtnStatus(time);
		
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Map<String,String>> map_data = new HashMap<String, Map<String,String>>();
		Map<String,Integer> map_status = SystemConstUtil.getProcessBtnStatus(time);
		Map<String,String> map1 = new HashMap<String, String>();
		Map<String,String> map2 = new HashMap<String, String>();
		Map<String,String> map3 = new HashMap<String, String>();
		Map<String,String> map4 = new HashMap<String, String>();
		Map<String,String> map5 = new HashMap<String, String>();
		String node1 = BtnTimeUtil.LINE_LIMIT_DATE;
		String node2 = BtnTimeUtil.DECLARE_DATE;
		String node3 = BtnTimeUtil.MATCH_DATE;
		String node4 = BtnTimeUtil.ISSUE_DATE;
		String node5 = BtnTimeUtil.ISSUE_RESULT_DATE;
		map1.put("time", node1);
		map2.put("time", node2);
		map3.put("time", node3);
		map4.put("time", node4);
		map5.put("time", node5);
		if(!map_status.isEmpty() && map_status.size()>0){
			for(String key : map_status.keySet()){
				if(key.equals("node1")){
					map1.put("status", map_status.get(key)+"");
				}else if(key.equals("node2")){
					map2.put("status", map_status.get(key)+"");
				}else if(key.equals("node3")){
					map3.put("status", map_status.get(key)+"");
				}else if(key.equals("node4")){
					map4.put("status", map_status.get(key)+"");
				}else if(key.equals("node5")){
					map5.put("status", map_status.get(key)+"");
				}
			}
		}
		map_data.put("node1", map1);
		map_data.put("node2", map2);
		map_data.put("node3", map3);
		map_data.put("node4", map4);
		map_data.put("node5", map5);
		map.put("status", map_status);
		map.put("data", map_data);
		return map;
		
		

	}
	
}
