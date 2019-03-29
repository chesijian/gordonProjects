package com.state.controller;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
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

import com.state.enums.Enums_SessionType;
import com.state.po.AreaPo;
import com.state.po.TypePo;
import com.state.po.UserPo;
import com.state.po.extend.JsonMsg;
import com.state.po.org.OrgRolePo;
import com.state.po.org.OrgRoleResourcesPo;
import com.state.security.DesJsUtil;
import com.state.service.AreaService;
import com.state.service.ILoginService;
import com.state.service.org.OrgRoleResourcesServiceI;
import com.state.service.org.OrgRoleServiceI;
import com.state.service.sys.LoginCountServiceI;
import com.state.service.sys.impl.LoginCountServiceImpl;
import com.state.util.AuthoritiesUtil;
import com.state.util.CommonUtil;
import com.state.util.DateUtil;
import com.state.util.LoggerUtil;
import com.state.util.LoginUtil;
import com.state.util.SessionUtil;
import com.state.util.sys.OnlineCounterUtil;

@Controller
@RequestMapping("/login")
public class LoginController {
	// By：JinHang 2016年5月6日 20:13:46
	private static final transient Logger log = Logger.getLogger(DeclareController.class);

	@Autowired
	private ILoginService loginService;

	@Autowired
	private AreaService areaService;

	@Autowired
	private OrgRoleServiceI orgRoleService;

	@Autowired
	private LoginCountServiceI loginCountService;
	
	@Autowired
	private OrgRoleResourcesServiceI orgRoleResourcesService;

	@RequestMapping(value = "/login")
	public String login(HttpServletRequest request, HttpServletResponse response) {
		log.info("跳转到登录页面");
		return "login";
	}
	@RequestMapping(value = "/navigation")
	public String loginMenu(HttpServletRequest request, HttpServletResponse response) {
		log.info("跳转到登录后的菜单页面");
		return "navigation";
	}

	@RequestMapping(value = "/register")
	public String register(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("跳转到注册页面");
		List<AreaPo> allArea = areaService.getAllArea();
		model.addAttribute("allArea", allArea);
		return "register";
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		if (CommonUtil.ifEmpty(SessionUtil.getUserUid())) {
			loginService.deleteOnlineUser(SessionUtil.getUserUid(), null);
		} else {
			loginService.deleteOnlineUser(null, SessionUtil.getSessionId());
		}
		request.getSession().invalidate();
		log.info("退出登录");
		return "login";
	}

	@ResponseBody
	@RequestMapping(value = "/validLogin", method = RequestMethod.POST)
	public JsonMsg validLogin(HttpServletRequest request, String user, String password) {
		log.info("校验并返回结果");
		JsonMsg result = new JsonMsg();
		try {
			
			if(request.getSession() != null){
				request.getSession().invalidate();
			}
			
			// 验证是否为空
			if (!LoginUtil.isEmpty(result, user, password).getStatus()) {
				return result;
			}
			// 验证是否加密
			if (!LoginUtil.isCipher(result, user, password).getStatus()) {
				return result;
			}

			// 校验用户
			user = DesJsUtil.getInstance().decode(user);
			password = DesJsUtil.getInstance().decode(password);

			// 验证是否太长
			if (!LoginUtil.isTooLong(result, user, password).getStatus()) {
				return result;
			}
			// 验证是否包含危险字符
			if (!LoginUtil.isHasBadStr(result, user, password).getStatus()) {
				return result;
			}
			
			UserPo pd = null;
			try {
				//重设
				result.setStatus(false);
				// 登录失败次数超过
				if (!loginCountService.getIfOverCount(user)) {
					
					result.setMsg("您登录失败累计超过"+LoginCountServiceImpl.LOGIN_OVER_COUNT+"次，请" + LoginCountServiceImpl.LOGIN_OVER_TIME + "分钟后重新登录！");
					return result;
				}
				
				pd = loginService.judgeUser(user, password);
				// System.out.println(pd+"=="+user+"=="+password);
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (pd != null) {

				if ("1".equals(pd.getIslogin())) {
					// 删除登录次数记录表
					loginCountService.delete(user);
					
					List<OrgRolePo> roles = orgRoleService.selectListByUser(pd.getId());
					
					// 查询所有获取的角色
					if (CommonUtil.ifEmpty_List(roles)) {
						pd.setRole(roles.get(0).getRoleId());
					}
					
					List<OrgRoleResourcesPo> resources = orgRoleResourcesService.selectListByRoles(roles.get(0).getId());

					for(int i=0;i<resources.size();i++){
						request.getSession().setAttribute(resources.get(i).getResourceId(), "true");
					}

					String dcode =areaService.getDcodeByArea(pd.getArea());
					request.getSession().setAttribute("userInfo", pd);
					request.getSession().setAttribute("roles", roles);

					request.getSession().setAttribute(Enums_SessionType.DCODE.getValue(), dcode);
					request.getSession().setAttribute(Enums_SessionType.USERINFO.getValue(), pd);
					request.getSession().setAttribute(Enums_SessionType.ROLES.getValue(), roles);

					List<Map<String, String>> billList = loginService.selectBill(pd.getArea());
					// System.out.println(CommonUtil.objToJson(bill));
					// 菜单menu
					List<Map<String, String>> bill = new ArrayList<Map<String, String>>();
					for (Map<String, String> temp : billList) {
						if (temp.get("mname").equals("用户管理")) {
							if (AuthoritiesUtil.isAllow_Menu_UserManager()) {
								bill.add(temp);
							}
						} else {
							bill.add(temp);
						}
					}
					Date tomorrow = new Date((new Date()).getTime() + 1000 * 60 * 60 * 24);
					String matchDate = DateUtil.format(tomorrow, "yyyy-MM-dd");
					pd.setMatchDate(matchDate);
					TypePo tp = loginService.getCountByType();
					int a2 = 0;
					int a3 = 0;
					for (int j = 1; j <= 96; j++) {
						String getAttributeMethodName = "getH" + (j < 10 ? "0" + j : j);
						Method getAttributeMethod = null;
						try {
							getAttributeMethod = TypePo.class.getDeclaredMethod(getAttributeMethodName);
							try {
								String corhr = (String) getAttributeMethod.invoke(tp);
								if (corhr.equals("峰")) {
									a2++;
								} else if (corhr.equals("谷")) {
									a3++;
								}

							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						}

					}
					loginService.insertOnlineUser();
					LoggerUtil.log(this.getClass().getName(),SessionUtil.getAddNameCn()+"登陆，"+"当前用户数：" + OnlineCounterUtil.getOnLineNumber(),0);
					//log.info("当前用户数：" + OnlineCounterUtil.getOnLineNumber());
					request.getSession().setAttribute("bill", bill);
					request.getSession().setAttribute("a2", a2);
					request.getSession().setAttribute("a3", a3);
					result.setStatus(true);
					return result;
				} else {
					result.setMsg("您没有权限,请联系管理员！");
					return result;
				}
			} else {
				result.setMsg("用户名或密码错误！");
				return result;
				// return "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMsg("登陆失败！");
			return result;
		}
	}

	@ResponseBody
	@RequestMapping(value = "/containUser", method = RequestMethod.POST)
	public String containUser(String user) {
		log.info("注册时检查是否存在用户");
		// 注册检查用户
		UserPo pd = loginService.containUser(user);
		if (pd != null) {
			if ("0".equals(pd.getIslogin())) {
				// 未审批
				return "noapprove";
			} else {
				return "fail";// 用户已存在
			}
		}
		return "success";
	}

	@ResponseBody
	@RequestMapping(value = "/registerUser", method = RequestMethod.POST)
	public String registerUser(String user, String password, String area, String sf) {
		log.info("注册用户");
		try {
			loginService.saveUser(null, user, password, area, sf);
		} catch (Exception e) {
			log.error("注册失败", e);
			return "fail";
		}
		return "success";
	}

}
