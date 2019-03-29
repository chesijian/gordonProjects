package com.state.util;

import com.state.po.org.OrgRolePo;

/**
 * 
 * @description
 * @author 大雄
 * @date 2016年8月17日下午5:46:21
 */
public class AuthoritiesUtil {
	
	/**
	 * 菜单中用户管理的权限
	 * @description
	 * @author 大雄
	 * @date 2016年8月12日下午7:45:55
	 * @return
	 */
	public static boolean isAllow_Menu_UserManager(){
		if(SessionUtil.isAdmin()){
			return true;
		}
		
		if(SessionUtil.isState() && SessionUtil.isManger()){
			//System.out.println(SessionUtil.isManger());
			return true;
		}
		return false;
	}
	
	/**
	 * Declare页面中优化计算按钮的权限
	 * @description
	 * @author 大雄
	 * @date 2016年8月10日下午3:22:07
	 * @return
	 */
	public static boolean isAllow_De_Btn_Match(){
		
		if(SessionUtil.isAdmin()){
			return true;
		}
		if(SessionUtil.isState() && (SessionUtil.isManger() || SessionUtil.isOperater())){
			return true;
		}
		
		return false;
	}
	
	
	
	/**
	 * Declare页面中新增按钮的权限
	 * @description
	 * @author 大雄
	 * @date 2016年8月10日下午3:22:07
	 * @return
	 */
	public static boolean isAllow_De_Btn_Add(){
		if(SessionUtil.isAdmin()){
			return true;
		}
		if(!SessionUtil.isState() && (SessionUtil.isManger() || SessionUtil.isOperater())){
			return true;
		}
		return false;
	}
	/**
	 * Declare页面中修改按钮的权限
	 * @description
	 * @author 大雄
	 * @date 2016年8月10日下午3:22:07
	 * @return
	 */
	public static boolean isAllow_De_Btn_Edit(){
		if(SessionUtil.isAdmin()){
			return true;
		}
		if(!SessionUtil.isState() && (SessionUtil.isManger() || SessionUtil.isOperater())){
			return true;
		}
		return false;
	}
	/**
	 * Declare页面中删除按钮的权限
	 * @description
	 * @author 大雄
	 * @date 2016年8月10日下午3:22:07
	 * @return
	 */
	public static boolean isAllow_De_Btn_Delete(){
		if(SessionUtil.isAdmin()){
			return true;
		}
		if(!SessionUtil.isState() && (SessionUtil.isManger() || SessionUtil.isOperater())){
			return true;
		}
		return false;
	}
	
	/**
	 * Declare页面中提交按钮的权限
	 * @description
	 * @author 大雄
	 * @date 2016年8月13日下午4:40:22
	 * @return
	 */
	public static boolean isAllow_De_Btn_Submit(){
		if(SessionUtil.isAdmin()){
			return true;
		}
		if(!SessionUtil.isState() && (SessionUtil.isManger() || SessionUtil.isOperater())){
			return true;
		}
		return false;
	}
	
	/**
	 * 判断交易单是否允许修改96值
	 * @description
	 * @author 大雄
	 * @date 2016年8月12日下午7:47:49
	 * @param status
	 * @return
	 */
	public static boolean isAllow_De_Intput(int status){
		if(status == 1){
			//说明已经提交
			return false;
		}
		if(!SessionUtil.isState() && (SessionUtil.isManger() || SessionUtil.isOperater())){
			return true;
		}
		return false;
	}
	
	/**
	 * limitLine限额页面中删除按钮的权限
	 * @description
	 * @author 大雄
	 * @date 2016年8月10日下午3:22:07
	 * @return
	 */
	public static boolean isAllow_LineL_Input(){
		if(SessionUtil.isAdmin()){
			return true;
		}
		if(SessionUtil.isState() ){
			return true;
		}
		return false;
	}
	
	/**
	 * limitLine限额页面中获取和保存数据按钮的权限
	 * @description
	 * @author 大雄
	 * @date 2016年8月10日下午6:37:50
	 * @return
	 */
	public static boolean isAllow_LineL_Btn(){
		if(SessionUtil.isAdmin()){
			return true;
		}
		if(SessionUtil.isState() && (SessionUtil.isManger() || SessionUtil.isOperater())){
			return true;
		}
		return false;
	}
	
	/**
	 * limitLine限额页面中删除按钮的权限
	 * @description
	 * @author 大雄
	 * @date 2016年8月10日下午3:22:07
	 * @return
	 */
	public static boolean isAllow_LineL_Btn_Delete(){
		if(SessionUtil.isAdmin()){
			return true;
		}
		if(SessionUtil.isState() && (SessionUtil.isManger() || SessionUtil.isOperater())){
			return true;
		}
		return false;
	}
	/**
	 * limitLine限额页面中删除按钮的权限
	 * @description
	 * @author 大雄
	 * @date 2016年8月10日下午3:22:07
	 * @return
	 */
	public static boolean isAllow_LineL_Btn_Add(){
		if(SessionUtil.isAdmin()){
			return true;
		}
		if(SessionUtil.isState() && (SessionUtil.isManger() || SessionUtil.isOperater())){
			return true;
		}
		return false;
	}
	
	/**
	 * 发布页面出清审核和结算审核的按钮权限
	 * @description
	 * @author 大雄
	 * @date 2016年8月10日下午4:25:44
	 * @return
	 */
	public static boolean isAllow_Issue_Btn(){
		if(SessionUtil.isAdmin()){
			return true;
		}
		if(SessionUtil.isState() && (SessionUtil.isManger() || SessionUtil.isOperater())){
			return true;
		}
		return false;
	}
	
	/**
	 * @author 车斯剑
	 * @param resourcesId
	 * @return
	 */
	public static boolean isAllow(String resourcesId){
		if(SessionUtil.isAdmin()){
			return true;
		}
		if(SessionUtil.isAdminMatch()){
			return true;
		}
		if("true".equals(SessionUtil.getAttribute(resourcesId))){
			return true;
		}
		return false;
	}
	/**
	 * 得到当前用户角色的类型
	 * @return
	 */
	public static Integer getToleType(){
		Integer type = 0;
		if(SessionUtil.getRole() != null){
			OrgRolePo role = SessionUtil.getRole();
			if(CommonUtil.ifEmpty(role.getRoleId())){
				type = role.getType();
			}
		}
		return type;
	}
}
