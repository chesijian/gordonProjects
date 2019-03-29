package com.state.listener;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.state.enums.Enums_SessionType;
import com.state.po.UserPo;
import com.state.service.ILoginService;
import com.state.service.ServiceHelper;
import com.state.util.LoggerUtil;
import com.state.util.sys.OnlineCounterUtil;
/**
 * session创建监听
 * @description
 * @author 大雄
 * @date 2016年8月17日下午4:17:29
 */
public class SessionListener implements HttpSessionListener {

	
	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub

	}

	
	public void sessionDestroyed(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		LoggerUtil.log(this.getClass().getName(), "=========sessionDestroyed========",0);
		
		HttpSession session = arg0.getSession();
		ServletContext ctx=session.getServletContext();
		
		ILoginService onlineUserService = ServiceHelper.getBean(ILoginService.class);
		Object userObj = session.getAttribute(Enums_SessionType.USERINFO.getValue());
		
		String userUid = null;
		String area = null;
		UserPo user = null;
		if(user != null){
			user = (UserPo)userObj;
			userUid = user.getId();
			area = user.getArea();
			OnlineCounterUtil.reduce(area, userUid, session.getId());
		}
		onlineUserService.deleteOnlineUser(null, String.valueOf(session.getId()));
		session.removeAttribute(Enums_SessionType.USERINFO.getValue());
		session.invalidate();
	}

}
