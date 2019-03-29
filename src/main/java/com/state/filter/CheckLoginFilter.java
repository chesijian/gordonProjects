package com.state.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.state.po.UserPo;
/**
 * 
 * @description
 * @author 大雄
 * @date 2016年8月2日下午4:02:25
 */
public class CheckLoginFilter  implements Filter {
	protected FilterConfig filterConfig = null;

	
	public void destroy() {
	}

	
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain filterChain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		String url = request.getServletPath();
		String requestType = request.getHeader("X-Requested-With");
		String contextPath = request.getContextPath();

		String uri = getUri(request);
		
		// 第一层过滤：筛选出不需要验证的资源(包括静态项目、不过滤列表以及js、css、图片登陆资源)
		if (isNoCheckResource(uri)) {
			filterChain.doFilter(request, response);
			return;
		}
		//System.out.println("---"+uri);
		if ("".equals(url)) {
			url += "/";
		}
		/**
		 * 除了一些js、css、图片资源 和 回调函数外 对其他请求均做过滤
		 */
		if (url.startsWith("/") ) {// 若访问后台资源
			//System.out.println("------1------");
			UserPo user = (UserPo)request.getSession().getAttribute("userInfo");
			//System.out.println("===="+request.getContextPath());
			if (user == null && !url.startsWith("/login") && !url.startsWith("/register") && !url.startsWith("/app")) {
				String path = request.getContextPath()+"/login";
				//System.out.println("----2--------");
				response.sendRedirect(path);
				return;
			}
			
			if(url.equals("/")){
				String path = request.getContextPath()+"/login";
				response.sendRedirect(path);
				return;
			}
		}
			

		filterChain.doFilter(request, response);
	}

	/**
	 * 筛选出不需要验证的资源<br/>
	 * 
	 * @param uri
	 * @return
	 */
	private boolean isNoCheckResource(String uri) {
		return uri.endsWith(".js") || uri.endsWith(".css")
				|| uri.endsWith(".jpg") || uri.endsWith(".jpeg")
				|| uri.endsWith(".gif") || uri.endsWith(".png")
				|| uri.endsWith(".dwr") || uri.endsWith(".htm")
				|| uri.endsWith(".xlsx") || uri.endsWith(".ws")
				|| uri.endsWith(".woff") || uri.endsWith(".ttf") || uri.endsWith(".json")
				|| (uri.indexOf("ws") > -1) || uri.contains("ChapterData")
				|| uri.contains("KnowledgePoint") || uri.contains("jiaocai")
				|| uri.contains("getBookAttrList");
	}

	/**
	 * 获取uri
	 */
	private String getUri(HttpServletRequest request) {
		return request.getServletPath()
				+ (request.getPathInfo() == null ? "" : request.getPathInfo());
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		// this.filterConfig = filterConfig;
	}

	
}
