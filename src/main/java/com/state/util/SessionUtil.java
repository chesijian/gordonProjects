package com.state.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.state.enums.Enums_DeclareType;
import com.state.enums.Enums_SessionType;
import com.state.enums.Enums_SystemConst;
import com.state.filter.RequestFilter;
import com.state.po.UserPo;
import com.state.po.org.OrgRolePo;

/**
 * 
 * @description
 * @author 大雄
 * @date 2016年8月2日下午4:10:23
 */
public class SessionUtil {

	/**
	 * 获取当前请求的request
	 * @description
	 * @author 大雄
	 * @date 2016年8月2日下午4:11:05
	 * @return
	 */
	public static HttpServletRequest getCurrentRequest() {
		try{
			return RequestFilter.threadLocal.get();
		}catch(Exception e){
			return null;
		}
		
	}
	
	/**
	 * 获取客户端的IP地址
	 * 
	 * @author 大雄
	 * @Title getClientIp
	 * @Date 2015-4-13
	 * @Description TODO
	 * @Params @return
	 * @Return String
	 */
	public static String getClientIp() {
		if (null != SessionUtil.getCurrentRequest()) {
			return SessionUtil.getCurrentRequest().getRemoteAddr();
		}
		return "";
	}
	
	/**
	 * 获取请求的完整url
	 * 
	 * @author 大雄
	 * @Title getRequestUrl
	 * @Date 2015-9-1
	 * @Description TODO
	 * @return
	 * @Return String
	 */
	public static String getRequestUrl() {
		// TODO Auto-generated method stub
		HttpServletRequest request = getCurrentRequest();
		if (request != null) {
			if (request.getQueryString() != null) {
				return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + request.getServletPath() + "?" + request.getQueryString();

			} else {
				return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + request.getServletPath();

			}

		} else {
			return "";
		}
	}
	
	/**
	 * 获取当前人拥有的角色
	 * @description
	 * @author 大雄
	 * @date 2016年8月10日上午9:29:57
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<OrgRolePo> getRoles(){
		if(getCurrentRequest() == null){
			return null;
		}else{
			Object o = getCurrentRequest().getSession().getAttribute(Enums_SessionType.ROLES.getValue());
			
			if(o == null){
				return null;
			}else{
				return (List<OrgRolePo>)o;
			}
		}
	}
	
	
	/**
	 * 获取当前人的角色
	 * @description
	 * @author 大雄
	 * @date 2016年8月10日上午9:31:11
	 * @return
	 */
	public static OrgRolePo getRole(){
		if(getRoles() == null){
			return null;
		}else{
			return getRoles().get(0);
		}
	}

	/**
	 * 获取当前登录人
	 * @description
	 * @author 大雄
	 * @date 2016年8月2日下午4:20:00
	 * @return
	 */
	public static String getAddNameCn() {
		// TODO Auto-generated method stub
		if(getUserPo() == null){
			return null;
		}
		return getUserPo().getMname();
	}
	
	public static String getAddName() {
		// TODO Auto-generated method stub
		if(getUserPo() == null){
			return null;
		}
		return getUserPo().getUserId();
	}
	
	
	
	/**
	 * 获取当前登录人
	 * @description
	 * @author 大雄
	 * @date 2016年8月2日下午4:21:16
	 * @return
	 */
	public static UserPo getUserPo(){
		Object o = getAttribute(Enums_SessionType.USERINFO.getValue());
		if(o == null){
			return null;
		}else{
				return (UserPo)o;
		}
	}
	
	/**
	 * 获取当前用户id
	 * @description
	 * @author 大雄
	 * @date 2016年8月17日下午5:06:50
	 * @return
	 */
	public static String getUserUid(){
		if(getUserPo() == null){
			return null;
		}else{
			return getUserPo().getId();
		}
	}
	
	
	/**
	 * 获取session
	 * @description
	 * @author 大雄
	 * @date 2016年8月17日下午4:10:02
	 * @return
	 */
	public static HttpSession getSession(){
		if(getCurrentRequest() == null){
			return null;
		}else{
			return getCurrentRequest().getSession();
		}
	}
	
	/**
	 * 获取会话id
	 * @description
	 * @author 大雄
	 * @date 2016年8月17日下午4:12:15
	 * @return
	 */
	public static String getSessionId(){
		if(getSession() == null){
			return null;
		}else{
			return getSession().getId();
		}
	}
	
	
	/**
	 * 判断当前用户是否是超级管理员
	 * @description
	 * @author 大雄
	 * @date 2016年8月10日上午11:26:55
	 * @return
	 */
	public static boolean isAdmin(){
		if(getRole() != null){
			OrgRolePo role = getRole();
			if(CommonUtil.ifEmpty(role.getRoleId())){
				if(role.getRoleId().equals(Enums_SystemConst.ROLE_ADMIN.getValue())){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 是否是管理员
	 * @description
	 * @author 大雄
	 * @date 2016年8月10日上午11:29:05
	 * @return
	 */
	public static boolean isManger(){
		if(getRole() != null){
			OrgRolePo role = getRole();
			if(CommonUtil.ifEmpty(role.getRoleId())){
				if(role.getRoleId().equals(Enums_SystemConst.ROLE_MANAGER.getValue())){
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 是否操作者
	 * @description
	 * @author 大雄
	 * @date 2016年8月10日上午11:30:27
	 * @return
	 */
	public static boolean isOperater(){
		if(getRole() != null){
			OrgRolePo role = getRole();
			if(CommonUtil.ifEmpty(role.getRoleId())){
				if(role.getRoleId().equals(Enums_SystemConst.ROLE_OPERATER.getValue())){
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 是否读者
	 * @description
	 * @author 大雄
	 * @date 2016年8月10日上午11:30:35
	 * @return
	 */
	public static boolean isReader(){
		if(getRole() != null){
			OrgRolePo role = getRole();
			if(CommonUtil.ifEmpty(role.getRoleId())){
				if(role.getRoleId().equals(Enums_SystemConst.ROLE_READER.getValue())){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 获取当前用户类型
	 * @description
	 * @author 大雄
	 * @date 2016年8月10日下午3:23:13
	 * @return
	 */
	public static String getDrole(){
		if(getUserPo() != null){
			return getUserPo().getDrole();
		}
		return null;
			
	}
	/**
	 * 获取当前用户的区域
	 * @description
	 * @author 大雄
	 * @date 2016年8月10日下午3:46:44
	 * @return
	 */
	public static String getArea(){
		if(getUserPo() != null){
			return getUserPo().getArea();
		}
		return null;
	}
	
	/**
	 * 是否国调用户
	 * @description
	 * @author 大雄
	 * @date 2016年8月10日下午3:50:05
	 * @return
	 */
	public static boolean isState(){
		if(CommonUtil.ifEmpty(getArea())){
			//如果是国调
			if(getArea().equals(Enums_SystemConst.COUNTRAY.getValue())){
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @author 大雄
	 * @Title getAttribute
	 * @Date 2014-8-19
	 * @Description 获取session中存储的值
	 * @Params  key
	 * @Params 
	 * @Return Object
	 */
	public static Object getAttribute(String key) {
		if(getSession() == null){
			return null;
		}
		return getSession().getAttribute(key);
		
	}
	
	/**
	 * 
	 * @author 大雄
	 * @Title addAttribute
	 * @Date 2014-8-19
	 * @Description 设置session中的值
	 * @Params @param key
	 * @Params @param value
	 * @Return void
	 */
	public static boolean setAttribute(String key, Object value) {
		// System.out.println(key+"---------------------"+value);
		if(getSession() == null){
			return false;
		}
		getSession().setAttribute(key,value);
		return true;
	}
	/**
	 * 判断当前用户是否是管理员
	 * @description
	 * @author 大雄
	 * @date 2016年8月10日上午11:26:55
	 * @return
	 */
	public static boolean isAdminMatch(){
		if(getRole() != null){
			OrgRolePo role = getRole();
			if(CommonUtil.ifEmpty(role.getRoleId())){
				if(role.getType() != 0 && role.getType() != 1){
					return true;
				}
			}
		}
		return false;
	}
}
