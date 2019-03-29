package com.state.listener;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.state.util.LoggerUtil;
import com.state.util.SessionUtil;
/**
 * 
 * @description
 * @author 大雄
 * @date 2016年8月17日下午5:23:29
 */
public class TomcatListener implements HttpSessionListener {
	public static String iconPath;
	public static String webAppPath;
	
	public void sessionCreated(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		
		LoggerUtil.log(this.getClass().getName(),"服务器启动" , 0);
		

	}

	
	public void sessionDestroyed(HttpSessionEvent arg0) {
		// TODO Auto-generated method stub
		LoggerUtil.log(this.getClass().getName(),"服务器关闭了" , 0);
	}
	
	public static String getWebAppPath() {
		HttpServletRequest  request = SessionUtil.getCurrentRequest();
		HttpSession session = request.getSession();    
		ServletContext  application  = session.getServletContext();    
		String serverRealPath = application.getRealPath("/");
		File projectPath = new File(serverRealPath);
		webAppPath = projectPath.getParent();
		iconPath = webAppPath+File.separator+"driverPicTemp";
		File iconFile = new File(iconPath);
		if(!iconFile.exists()){
			iconFile.mkdir();
		}
		return webAppPath;
	}

	

	public static String getIconPath() {
		getWebAppPath();
		return iconPath;
	}

	

	

}
