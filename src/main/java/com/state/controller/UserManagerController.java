package com.state.controller;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.state.exception.MsgException;
import com.state.listener.TomcatListener;
import com.state.po.UserPo;
import com.state.po.extend.DataGrid;
import com.state.po.extend.JsonMsg;
import com.state.po.org.OrgDepartPo;
import com.state.po.org.OrgDepartUserPo;
import com.state.po.org.OrgRolePo;
import com.state.po.org.OrgUserRolePo;
import com.state.security.EncryptUtil;
import com.state.service.ILoginService;
import com.state.service.UserServiceI;
import com.state.service.org.OrgDepartServiceI;
import com.state.service.org.OrgRoleServiceI;
import com.state.util.CommonUtil;
import com.state.util.SessionUtil;
import com.state.util.TimeUtil;
import com.state.util.UploadImageTool;

@Controller
@RequestMapping("/manager")
public class UserManagerController {
	// By：JinHang 2016年5月8日 17:08:48
	private static final transient Logger log = Logger.getLogger(DeclareController.class);

	@Autowired
	private ILoginService loginService;
	@Autowired
	private UserServiceI userService;

	@Autowired
	private OrgRoleServiceI orgRoleService;

	@Autowired
	private OrgDepartServiceI orgDeparteService;

	@RequestMapping(value = "/init")
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("跳转到用户管理界面");
		// List<UserPo> nopass = loginService.selectNopass();
		// List<UserPo> pass = loginService.selectPass();

		/*
		 * List<UserPo> pass =
		 * loginService.selectPassCurrentPage(currentPage-1); long totalCount
		 * =loginService.getPssCount(); long totalPage =
		 * Math.max((totalCount+10-1)/10, 1);
		 * 
		 * Map<String, Object> map = new HashMap<String, Object>();
		 * map.put("nopass", nopass); map.put("pass", pass);
		 * map.put("currentPage", currentPage); model.addAttribute("totalPage",
		 * totalPage);
		 */
		// model.addAttribute("jspType", "issue");
		// ModelAndView view = new ModelAndView("usermanager");
		//获取当前服务器时间
		model.addAttribute("serviceDate", TimeUtil.getStringDate());
		ModelAndView view = new ModelAndView("usermanager");
		return view;
	}

	@ResponseBody
	@RequestMapping(value = "/valiPassword", method = RequestMethod.POST)
	public Boolean valiPassword(HttpServletRequest request, HttpServletResponse response,String password, String id) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		UserPo userPo = userService.get(id);
		String aa = userPo.getMkey();
		String bb = EncryptUtil.md5AndSha(password);
		return aa.equals(bb);
	}
	
	@RequestMapping(value = "/upload")
	@ResponseBody
	public Map<String, Object> upload(HttpServletRequest request, HttpServletResponse response, String mName) {
		System.out.println("===================用户名：" + mName + "=====================");
		// try {
		// mName = new String(mName.getBytes("ISO-8859-1"),"UTF-8");
		// } catch (UnsupportedEncodingException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// System.out.println(mName);
		// System.out.println("+++++++++++"+TomcatListener.getWebAppPath()+"+++++++++++");
		String savePicPathTemp = TomcatListener.getWebAppPath() + File.separator + "driverPicTemp";

		// System.out.println("存放图片路径："+savePicPathTemp);
		// 转型为MultipartHttpRequest：
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获得文件：
		MultipartFile file = multipartRequest.getFile("autoGraph");
		// 获得文件名：
		String filename = file.getOriginalFilename();
		String[] array = filename.split("\\.");
		String result = "";
		Map<String, Object> map = new HashMap<String, Object>();
		if (array.length > 2) {
			result = "文件格式不正确！";
			map.put("status", false);
			map.put("fileName", "");
			map.put("msg", result);
			map.put("upLoadSucess", "false");
			return map;
		}
		InputStream input = null;
		try {
			input = file.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File savePath = new File(savePicPathTemp);
		if (!savePath.exists()) { // 文件夹
			savePath.mkdir();
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String userName = format.format(new Date());
		// 把上传的文件暂时存放在临时文件夹
		String tempPath = savePicPathTemp + File.separator + userName + "." + FilenameUtils.getExtension(filename);

		UploadImageTool.getUploadFileMap().put(mName, tempPath);// 记录临时存放图片的路径
																// 以便保存用户时使用
		try {
			FileOutputStream out = new FileOutputStream(tempPath);
			// 写入文件
			out.write(file.getBytes());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(filename+"++++");
		result = "上传成功！";
		ModelAndView modelAndView = new ModelAndView("goods/uploadSuccess");
		modelAndView.addObject("result", result);
		modelAndView.addObject("filename", filename);
		map.put("upLoadSucess", "true");
		map.put("status", true);
		map.put("fileName", filename);
		map.put("msg", result);
		return map;
	}

	@RequestMapping(value = "/getImage")
	@ResponseBody
	public JsonMsg getImage(HttpServletRequest request, HttpServletResponse response, String id, boolean sign, String mname) {
		// System.out.println(id+"++"+sign+"+++"+mname);
		JsonMsg j = new JsonMsg();
		String imgPath1 = "";
		if (id != "" && id != null) {
			UserPo user = userService.get(id);
			if (user == null) {
				return j;
			}
			System.out.println(TomcatListener.getIconPath());
			if (user.getAutoGraph() == null || user.getAutoGraph().equals("") || user.getAutoGraph().isEmpty()) {
				j.setMsg("请联系管理员上传您的签名！");
				return j;
			}
			imgPath1 = TomcatListener.getIconPath() + File.separator + user.getAutoGraph();
			response.setContentType("image/jpeg");
		} else {
			// try {
			// mname = new String(mname.getBytes("ISO-8859-1"),"UTF-8");
			// } catch (UnsupportedEncodingException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			imgPath1 = UploadImageTool.getUploadFileMap().get(mname);
			response.setContentType("image/jpeg");
		}
		if (imgPath1 == null) {
			return j;
		}
		// 判断该路径下的文件是否存在
		File file = new File(imgPath1);
		// System.out.println(imgPath1);
		OutputStream temps = null;
		System.out.println(file.exists() + "==========" + file.getAbsolutePath());
		if (file.exists()) {
			try {
				InputStream in = new FileInputStream(file);
				temps = new DataOutputStream(response.getOutputStream());
				byte[] b = new byte[2048];
				while ((in.read(b)) != -1) {
					temps.write(b);
					temps.flush();
				}
				in.close();
				temps.close();
				if (sign) {
					request.getSession().setAttribute("isSign", "true");
					request.getSession().setAttribute("isSignResult", "true");
					request.getSession().setAttribute("isDeclResult", "true");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return j;
	}

	@RequestMapping(value = "/getUserAutoGraphIsExist")
	@ResponseBody
	public JsonMsg getUserAutoGraphIsExist(HttpServletRequest request, HttpServletResponse response, String id) {
		JsonMsg j = new JsonMsg();
		UserPo user = userService.get(id);
		// System.out.println(TomcatListener.getIconPath());
		if (user.getAutoGraph() == null || user.getAutoGraph().equals("") || user.getAutoGraph().isEmpty()) {
			j.setMsg("请联系管理员上传您的签名！");
			return j;
		}
		return j;
	}

	/**
	 * 对用户编辑时根据id获取用户信息，并跳转到用户信息界面
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日下午3:49:03
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/editUser")
	public ModelAndView editUser(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("跳转到用户信息界面");
		String id = request.getParameter("id");

		UserPo user = userService.get(id);
		// 获取用户的角色
		List<OrgRolePo> roles = orgRoleService.selectListByUser(id);
		if (CommonUtil.ifEmpty_List(roles)) {
			user.setRole(roles.get(0).getRoleId());
		}
		// 获取用户的部门
		List<OrgDepartPo> departs = orgDeparteService.selectListByUser(id);
		if (CommonUtil.ifEmpty_List(departs)) {
			user.setDepart(departs.get(0).getDepartId());

		}
		// System.out.println("========="+CommonUtil.objToJson(user));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userInfo", CommonUtil.objToJson(user));
		map.put("user", user);
		map.put("status", "edit");
		map.put("upLoadSucess", "true");
		// model.addAttribute("jspType", "issue");
		ModelAndView view = new ModelAndView("org/user", map);
		return view;
	}

	/**
	 * 新增用户
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年8月9日下午3:49:03
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/newUser")
	public ModelAndView newUser(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("跳转到用户信息界面");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userInfo", "{}");
		map.put("status", "new");
		map.put("upLoadSucess", "false");
		// model.addAttribute("jspType", "issue");
		ModelAndView view = new ModelAndView("org/user", map);
		return view;
	}

	@RequestMapping(value = "/editPwd")
	public ModelAndView editPwd(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("跳转到用户信息界面");
		String id = request.getParameter("id");
		if (!CommonUtil.ifEmpty(id)) {
			id = SessionUtil.getUserPo().getId();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		// model.addAttribute("jspType", "issue");
		ModelAndView view = new ModelAndView("org/pwd", map);
		return view;
	}

	@ResponseBody
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public String updateUser(HttpServletRequest request, HttpServletResponse response, String id, String userId, String mname, String mkey, String drole, String area, String role, String depart) {
		log.info("跳转到用户修改");
		System.out.println("update===id===="+id);
		JsonMsg j = new JsonMsg();
		try {
			boolean isExtis = false;
			// 如果传了用户民和编号说明修改了
			if (CommonUtil.ifEmpty(userId) || CommonUtil.ifEmpty(mname)) {
				isExtis = userService.getIfExtis(userId, mname);
			}
			// System.out.println(isExtis);
			if (isExtis) {
				j.setMsg("该用户名或编号已经存在！");
			} else {
				UserPo user = new UserPo();
				if (CommonUtil.ifEmpty(userId)) {
					user.setUserId(userId);
				}
				if (CommonUtil.ifEmpty(mname)) {
					user.setMname(mname);
				}
				if (CommonUtil.ifEmpty(drole)) {
					user.setDrole(drole);
				}
				if (CommonUtil.ifEmpty(area)) {
					user.setArea(area);
					if (area.equals("国调")) {
						user.setDtype("国");
					} else {
						user.setDtype("省");
					}
				}
				if (CommonUtil.ifEmpty(mkey)) {
					user.setMkey(mkey);
				}
				if (mname == null) {
					mname = userService.get(id).getMname();
				}

				if (UploadImageTool.getUploadFileMap().containsKey(mname)) {
					String tempFile = UploadImageTool.getUploadFileMap().get(mname);
					String savePicPath = TomcatListener.getWebAppPath() + File.separator + "driverPic";
					UploadImageTool.copyFile(tempFile, savePicPath);// 把图片从临时目录拷贝到正式的路径
					File temp = new File(tempFile);
					String fileName = temp.getName();
					String[] array = fileName.split("\\.");
					// 创建缩略图
					String imageName = UploadImageTool.createIcon(savePicPath, array[0], array[1]);
					user.setAutoGraph(imageName);
					temp.delete();

				}

				if (CommonUtil.ifEmpty(id)) {
					user.setId(id);
					userService.update(user);
				} else {
					userService.insert(user);
				}
				if (CommonUtil.ifEmpty(role)) {
					OrgUserRolePo our = new OrgUserRolePo();
					our.setRoleId(role);
					our.setUserUid(user.getId());
					our.setUserId(user.getUserId());
					boolean flag = orgRoleService.insertUserRole(our);
					if (!flag) {
						throw new MsgException("无法为给用户授权！");
					}
				}
				// 添加部门
				if (CommonUtil.ifEmpty(depart)) {
					OrgDepartUserPo odu = new OrgDepartUserPo();
					odu.setUserId(user.getUserId());
					odu.setUserUid(user.getId());
					odu.setDepartId(depart);
					odu.setDepartUid(depart);

					boolean flag = orgDeparteService.insertDepartUser(odu);

					if (!flag) {
						throw new MsgException("无法为给用户添加部门！");
					}
				}
				j.setStatus(true);
				j.setMsg("修改成功!");
			}
		} catch (MsgException ex) {
			j.setMsg(ex.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			j.setMsg("修改失败！");
		}
		return CommonUtil.objToJson(j);
	}

	@ResponseBody
	@RequestMapping(value = "/approveUser", method = RequestMethod.POST)
	public String approveUser(String user) {
		log.info("审批用户及分配菜单权限");
		try {
			loginService.approve(user);// 审批
			loginService.allotBill(user);// 分配菜单权限
		} catch (Exception e) {
			log.error("审批失败", e);
			return "fail";
		}
		return "success";
	}

	@ResponseBody
	@RequestMapping(value = "/updateUserStatus", method = RequestMethod.POST)
	public String updateUserStatus(String name, String state) {
		loginService.updateUser(name, state);
		return "success";
	}

	@ResponseBody
	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	public String deleteUser(String id) {
		loginService.deleteUser(id);
		return "success";
	}

	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年9月21日下午5:23:50
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/usermanager")
	public ModelAndView initClear(Model model) {

		ModelAndView view = new ModelAndView("system/userManager", null);
		return view;
	}

	/**
	 * 分页
	 * 
	 * @author 车斯剑
	 * @date 2016年9月21日下午5:24:06
	 * @param request
	 * @param response
	 * @param model
	 * @param currentPage
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = "/changePage")
	@ResponseBody
	public Map<String, Object> changePage(HttpServletRequest request, HttpServletResponse response, Model model, int currentPage, int limit) {
		log.info("跳转到用户管理界面");
		List<UserPo> list = loginService.selectPassCurrentPage(currentPage - 1, limit);
		long totalCount = loginService.getPssCount();
		long totalPage = Math.max((totalCount + limit - 1) / limit, 1);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("currentPage", currentPage);
		map.put("totalPage", totalPage);
		return map;
	}
}
