package com.state.util;

import com.state.po.extend.JsonMsg;

/**
 * 登陆的帮助类
 * @description
 * @author 大雄
 * @date 2016年8月18日上午10:48:31
 */
public class LoginUtil {

	/**
	 * 验证是否为空
	 * @description
	 * @author 大雄
	 * @date 2016年8月18日上午10:52:36
	 * @param j
	 * @param user
	 * @param password
	 * @return
	 */
	public static synchronized JsonMsg isEmpty(JsonMsg j ,String user,String password){
		if (!CommonUtil.ifEmpty(user) || !CommonUtil.ifEmpty(password)) {
			j.setStatus(false);
			j.setMsg("您输入不能为空，请重新输入！");
			return j;
		}else{
			j.setStatus(true);
		}
		return j;
	}
	
	/**
	 * 验证是否加密
	 * @description
	 * @author 大雄
	 * @date 2016年8月18日上午10:54:38
	 * @param j
	 * @param user
	 * @param password
	 * @return
	 */
	public static synchronized JsonMsg isCipher(JsonMsg j ,String user,String password){
		if (CharFilterUtil.ifHasBadChar(user) || CharFilterUtil.ifHasBadChar(password)) {
			j.setStatus(false);
			j.setMsg("您输入的信息没有加密，请重新输入！");
			return j;
		}else{
			j.setStatus(true);
		}
		return j;
	}
	
	/**
	 * 验证用户名和密码是否太长
	 * @description
	 * @author 大雄
	 * @date 2016年8月18日上午10:57:40
	 * @param j
	 * @param user
	 * @param password
	 * @return
	 */
	public static synchronized JsonMsg isTooLong(JsonMsg j ,String user,String password){
		if (user.length() > 30 || password.length() > 25) {
			j.setStatus(false);
			j.setMsg("您输入用户名或密码太长，请重新输入！");
			return j;
		}else{
			j.setStatus(true);
		}
		return j;
	}
	
	/**
	 * 验证是否有危险字符
	 * @description
	 * @author 大雄
	 * @date 2016年8月18日上午10:58:04
	 * @param j
	 * @param user
	 * @param password
	 * @return
	 */
	public static synchronized JsonMsg isHasBadStr(JsonMsg j ,String user,String password){
		if (CharFilterUtil.ifHasBadChar(user) || CharFilterUtil.ifHasBadChar(password)) {
			j.setStatus(false);
			j.setMsg("您输入的信息有错误字符，请重新输入！");
			return j;
		}
		if (password.equals("A%22+or+isnull%281%2F0%29+%2F*")) {
			j.setStatus(false);
			j.setMsg("您输入的信息有错误字符，请重新输入！");
			return j;
		}
		j.setStatus(true);
		return j;
	}
}
