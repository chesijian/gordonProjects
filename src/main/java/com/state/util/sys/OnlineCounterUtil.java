package com.state.util.sys;

import java.util.HashMap;
import java.util.Map;


import com.state.enums.Enums_SessionType;
import com.state.util.CommonUtil;
import com.state.util.SessionUtil;


/**
 * 
 * @author 大雄
 * @Description 统计在线用户
 * @Date 2015-2-13
 */
public class OnlineCounterUtil {
	private static long online = 0;
	private static Map<String, Integer> onLineNumber = new HashMap<String, Integer>();
	private static Map<String, Map<String, String>> onLineUser = new HashMap<String, Map<String, String>>();

	public static long getOnline() {
		return online;
	}

	public static void raise(Object companyId) {
		if (companyId != null) {
			if (onLineNumber.containsKey(companyId)) {
				Integer number = onLineNumber.get(companyId);
				if (number == null) {
					number = 0;
				}
				number++;
				onLineNumber.put(companyId.toString(), number);
			} else {
				Integer number = 1;
				onLineNumber.put(companyId.toString(), number);
			}
		}
		online++;
	}

	public static void reduce(Object companyId) {
		// System.out.println(companyId+"==raise=="+onLineNumber.get(companyId));
		if (companyId != null) {
			if (onLineNumber.containsKey(companyId)) {
				Integer number = onLineNumber.get(companyId);
				number--;
				if (number <= 0) {
					number = 1;
				}
				onLineNumber.put(String.valueOf(companyId), number);
			}
		}
		online--;
	}

	public static long getOnLineNumber() {
		Object companyId = SessionUtil.getAttribute(Enums_SessionType.AREA.getValue());
		if (companyId != null) {
			if(onLineUser.containsKey(companyId)){
				return onLineUser.get(companyId).size();
			}
		}
		if (online <= 0) {
			online = 1;
		}
		return online;
	}

	public static Map<String, String> getOnlineUser(String companyId) {
		return onLineUser.get(companyId);
	}

	/**
	 * 添加员工
	 * 
	 * @author 大雄
	 * @Title raise
	 * @Date 2015-8-20
	 * @Description TODO
	 * @param departId
	 * @param id
	 * @Return void
	 */
	public static void raise(Object companyId, String userUid, String SessionId) {
		// TODO Auto-generated method stub
		if (companyId != null) {
			Map<String, String> users = null;
			if (onLineUser.containsKey(companyId)) {
				users = onLineUser.get(companyId);
				users.put(SessionId, userUid);

			} else {
				users = new HashMap<String, String>();
				users.put(SessionId, userUid);
				onLineUser.put(String.valueOf(companyId), users);
			}

			onLineNumber.put(String.valueOf(companyId), users.size());
			online++;
		}

	}

	public static void reduce(Object companyId, String userUid, String sessionId) {
		if (companyId != null) {
			Map<String, String> users = null;
			if (onLineUser.containsKey(companyId)) {
				users = onLineUser.get(companyId);
				if (sessionId != null) {
					users.remove(sessionId);
					if (onLineNumber.containsKey(companyId)) {
						Integer number = onLineNumber.get(companyId);
						number--;
						if (number <= 0) {
							number = 1;
						}
						onLineNumber.put(String.valueOf(companyId), number);
					}
					online--;
				}
				

			}

		}

	}

	/**
	 * 根据公司编号和SessionId获取当前这个人是否在登陆状态
	 * 
	 * @author 大雄
	 * @Title getOnlineUser
	 * @Date 2015-9-9
	 * @Description TODO
	 * @param companyId
	 * @param sessionId
	 * @return
	 * @Return boolean
	 */
	public static boolean isOnline(String companyId, String sessionId) {
		if (companyId != null) {
			Map<String, String> users = null;
			if (onLineUser.containsKey(companyId)) {
				users = onLineUser.get(companyId);
				System.out.println(CommonUtil.objToJson(users));
				if (users.containsKey(sessionId)) {
					return true;
				}
			}
		}
		return false;
	}

}
