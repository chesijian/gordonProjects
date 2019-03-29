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

import com.state.po.org.OrgDepartPo;
import com.state.service.org.OrgDepartServiceI;
import com.state.util.CommonUtil;
import com.state.util.TimeUtil;

@Controller
@RequestMapping("/org/orgDepart")
public class OrgDepartController {

	private static final transient Logger log = Logger
			.getLogger(OrgRoleController.class);
	
	@Autowired
	private OrgDepartServiceI orgDepartService;
	
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
	
	@ResponseBody
	@RequestMapping(value = "/getDeparts",method=RequestMethod.POST)
	public String getDeparts(String id) {
		
		List<OrgDepartPo>  list = orgDepartService.selectList(true,"1");
		//System.out.println(CommonUtil.objToJson(list));
		return CommonUtil.objToJson(list);
	}
}
