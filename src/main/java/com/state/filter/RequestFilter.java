package com.state.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.state.util.CommonUtil;
import com.state.util.properties.JPropertiesUtil;

public class RequestFilter implements Filter {

	// 创建线程
	public static ThreadLocal<HttpServletRequest> threadLocal = new ThreadLocal<HttpServletRequest>();

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		HttpServletResponse response = (HttpServletResponse) arg1;
		HttpServletRequest request = (HttpServletRequest) arg0;
		
		//LoggerUtil.log("RequestFilter", request.getSession().getId(),0);
		if(!JPropertiesUtil.IS_ALLOW_EXTRA_REFERER){
			/**跨站点请求伪造。修复任务： 拒绝恶意请求**/
			//HTTP 头设置 Referer过滤  
			         /**/
			String referer = request.getHeader("Referer");   //REFRESH  
			String basePath = CommonUtil.getBaseUrl(request);
			//LoggerUtil.log(this.getClass().getName(),basePath+"====="+referer, 0);
			//System.out.println(basePath+"====="+referer);
			if(referer!=null && referer.startsWith("https://:")){
				if(referer.indexOf(basePath)<0){  
					request.getRequestDispatcher(basePath+"index.jsp").forward(request, response); 
				}
			}else{
				if(referer!=null && referer.indexOf(basePath)<0){           
					request.getRequestDispatcher(basePath+"index.jsp").forward(request, response);  
				} 
			}
			 
		}
		/** 会话cookie 中缺少HttpOnly 属性 **/
		// httponly是微软对cookie做的扩展,该值指定 Cookie 是否可通过客户端脚本访问,
		// 解决用户的cookie可能被盗用的问题,减少跨站脚本攻击
		//response.setHeader("Set-Cookie", "name=value; HttpOnly");
		// System.out.println("-----------"+request.getSession().getServletContext().getServerInfo());;
		/**/
		String serName = request.getSession().getServletContext().getServerInfo();
		Cookie[] cookies = ((HttpServletRequest) request).getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				//设置cookie只作用于安全的协议(https)
				cookie.setSecure(true);
				// tomcat7 支持该属性，tomcat6不支持
				if (serName.indexOf("Tomcat/7") > -1) {
					//cookie.setHttpOnly(true);
				}

			}
		}

		
		//LoggerUtil.log("RequestFilter", "==================",1);
		// 把request传入
		threadLocal.set((HttpServletRequest) arg0);
		arg2.doFilter(arg0, arg1);
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
