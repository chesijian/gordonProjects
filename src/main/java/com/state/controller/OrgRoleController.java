package com.state.controller;


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
import com.state.po.UserPo;
import com.state.po.extend.JsonMsg;
import com.state.po.org.OrgRolePo;
import com.state.service.ILoginService;
import com.state.service.UserServiceI;
import com.state.service.org.OrgRoleServiceI;
import com.state.util.CommonUtil;
import com.state.util.SessionUtil;
import com.state.util.TimeUtil;

@Controller
@RequestMapping("/org/orgRole")
public class OrgRoleController {
	//By：JinHang 2016年5月8日 17:08:48
	private static final transient Logger log = Logger
			.getLogger(OrgRoleController.class);
	
	
	@Autowired
	private OrgRoleServiceI orgRoleService;
	
	@RequestMapping(value = "/init")
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response,Model model) {
		log.info("跳转到用户管理界面");
		Map<String, Object> map = new HashMap<String, Object>();
		//获取当前服务器时间
		model.addAttribute("serviceDate", TimeUtil.getStringDate());
		//model.addAttribute("jspType", "issue");
		ModelAndView view = new ModelAndView("usermanager",map);
		return view;
	}
	
	/**
	 * 对用户编辑时根据id获取角色信息，并跳转到用户信息界面
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日下午3:49:03
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/editRole")
	public ModelAndView editRole(HttpServletRequest request, HttpServletResponse response,Model model) {
		log.info("跳转到用户信息界面");
		String id = request.getParameter("id");
		
		Map<String, Object> map = new HashMap<String, Object>();
		//map.put("userInfo", CommonUtil.objToJson(user));
		//map.put("user", user);
		map.put("status", "edit1");
		//model.addAttribute("jspType", "issue");
		ModelAndView view = new ModelAndView("org/user",map);
		return view;
	}
	
	@ResponseBody
	@RequestMapping(value = "/updateRole",method=RequestMethod.POST)
	public String updateRole(HttpServletRequest request, HttpServletResponse response,String id,String userId,String mname,String drole,String area,String role) {
		log.info("跳转到用户修改");
		JsonMsg j = new JsonMsg();
		try{
			boolean isExtis = false;
			//如果传了用户民和编号说明修改了
			if(CommonUtil.ifEmpty(userId) || CommonUtil.ifEmpty(mname)){
				//isExtis = userService.getIfExtis(userId,mname);
			}
			//System.out.println(isExtis);
			if(isExtis){
				j.setMsg("该用户名或编号已经存在！");
			}else{
				UserPo user = new UserPo();
				if(CommonUtil.ifEmpty(userId)){
					user.setUserId(userId);
				}
				if(CommonUtil.ifEmpty(mname)){
					user.setMname(mname);
				}
				if(CommonUtil.ifEmpty(drole)){
					user.setDrole(drole);
				}
				if(CommonUtil.ifEmpty(area)){
					user.setArea(area);
					if(area.equals("国调")){
						user.setDtype("国");
					}else{
						user.setDtype("省");
					}
				}
				if(CommonUtil.ifEmpty(role)){
					
				}
				user.setId(id);
				//userService.update(user);
				j.setStatus(true);
				j.setMsg("修改成功!");
			}
		}catch(MsgException ex){
			j.setMsg(ex.getMessage());
		}catch(Exception e){
			e.printStackTrace();
			j.setMsg("修改失败！");
		}
		return CommonUtil.objToJson(j);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/deleteRole",method=RequestMethod.POST)
	public String deleteRole(String id) {
		//loginService.deleteUser(id);
		return "success";
	}
	
	@ResponseBody
	@RequestMapping(value = "/getRoles",method=RequestMethod.POST)
	public String getRoles(String type) {
		//loginService.deleteUser(id);
		List<OrgRolePo> list = orgRoleService.selectListByIsSys(0,type);
		//System.out.println(CommonUtil.objToJson(list));
		return CommonUtil.objToJson(list);
	}
}
