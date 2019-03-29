package com.state.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.state.util.LoggerUtil;

/**
 * 解决标识符未更新的问题
 * @author 大雄
 * @Description TODO
 * @Date 2016-7-30
 */
public class NewSessionFilter  implements Filter{
	 private String url;  
	    
	    public static final String NEW_SESSION_INDICATOR = NewSessionFilter.class.getName();  
	    public static void newSession(){  
	       // HttpSession session = (HttpSession) SecurityUtils.getSubject().getSession(true);  
	       // session.setAttribute(NEW_SESSION_INDICATOR, true);  
	    } 
	     
	  
	    public void destroy() {  
	        //System.out.println("NewSessionFilter destory");  
	    }  
	  
	    public void doFilter(ServletRequest request, ServletResponse response,  
	            FilterChain chain) throws IOException, ServletException {  
	        //System.out.println("NewSessionFilter doFilter");  
	          
	        if (request instanceof HttpServletRequest) {  
	            HttpServletRequest httpRequest = (HttpServletRequest) request;  
	              
	            //取的url相对地址  
	            String url = httpRequest.getRequestURI();    
	            //System.out.println(url);    
	            if (httpRequest.getSession() != null) {  
	                //System.out.println("NewSessionFilter doFilter httpRequest.getSession().getId()"+ httpRequest.getSession().getId());  
	                //--------复制 session到临时变量  
	                HttpSession session = httpRequest.getSession();  

	                //LoggerUtil.log(this.getClass().getName(),"OldSessionId--"+session.getId(), 0); 
	                HashMap old = new HashMap();  
	                Enumeration keys = (Enumeration) session.getAttributeNames();  
	                  
	                while (keys.hasMoreElements()){  
	                    String key = (String) keys.nextElement();  
	                    if (!NEW_SESSION_INDICATOR.equals(key)){  
	                        old.put(key, session.getAttribute(key));  
	                        session.removeAttribute(key);  
	                    }  
	                }  
	                //LoggerUtil.log(this.getClass().getName(),"--"+httpRequest.getMethod().equals("POST"), 0);
	  	              
	                if (httpRequest.getMethod().equals("POST") && httpRequest.getSession() != null   
	                        && !httpRequest.getSession().isNew() && httpRequest.getRequestURI().endsWith(url)){  
	                	//LoggerUtil.log(this.getClass().getName(),"---d--", 0);
	  	              
	                	
	                	session.invalidate(); 
	                	if (httpRequest.getCookies()!=null) { 
		                	   Cookie cookie = httpRequest.getCookies()[0];     // 获取cookie 
		                	   cookie.setMaxAge(0);                    // 让cookie过期 
		                	} 
	                    session=httpRequest.getSession(true);  

	                    //LoggerUtil.log(this.getClass().getName(),"NewSessionId--"+session.getId(), 0);
	                }  
	                  
	                //-----------------复制session  
	                for (Iterator it = old.entrySet().iterator(); it.hasNext();) {  
	                    Map.Entry entry = (Entry) it.next();  
	                    session.setAttribute((String) entry.getKey(), entry.getValue());  
	                }  
	            }  
	        }  
	          
	        chain.doFilter(request, response);  
	        //System.out.println("NewSessionFilter doFilter end");  
	    }  
	  
	    public void init(FilterConfig filterConfig) throws ServletException {  
	       //System.out.println("NewSessionFilter init");  
	        //System.out.println("NewSessionFilter init end");  
	    }

		
}
