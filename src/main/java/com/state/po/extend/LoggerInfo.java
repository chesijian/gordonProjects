package com.state.po.extend;

import org.apache.log4j.Logger;

import com.state.util.CommonUtil;
import com.state.util.SessionUtil;

public class LoggerInfo {
	private static final String _BLANK = " "; 
	private Logger _logger;
	private String clazz;
	
	public LoggerInfo(){
		
	}

	public LoggerInfo(String clazz){
		//System.out.println("**************");
		_logger = Logger.getLogger(clazz);  
		this.clazz = clazz;
	}
	
	
	public void debug(Object paramObject) {
		_logger.debug(paramObject);
	}

	public void debug(Object paramObject, Throwable paramThrowable) {
		_logger.debug(paramObject, paramThrowable);
	}

	public void error(Object paramObject) {
		_logger.error(paramObject);
	}
	
	public void errorAll(Object paramObject) {
		StringBuilder sb = new StringBuilder();  
		sb.append(getMessageHead());
        sb.append(paramObject);
		_logger.error(sb.toString());
	}

	public void error(Object paramObject, Throwable paramThrowable) {
		_logger.error(paramObject, paramThrowable);
	}
	
	public void errorAll(Object paramObject, Throwable paramThrowable) {
		StringBuilder sb = new StringBuilder();  
		sb.append(getMessageHead());
        sb.append(paramObject);
		_logger.error(sb.toString(), paramThrowable);
	}

	
	public void info(Object paramObject) {
		_logger.info(paramObject);
	}
	
	public StringBuilder getMessageHead(){
		StringBuilder sb = new StringBuilder();  
		//sb.append("%3Cfont color=\"#993300\">");
        //sb.append("[").append(clazz).append("]").append(_BLANK);  
		sb.append("[").append(SessionUtil.getCurrentRequest()==null?"服务器":SessionUtil.getClientIp()).append("]").append(_BLANK);  
        sb.append("[").append(SessionUtil.getAddNameCn()==null?"服务器":SessionUtil.getAddNameCn()).append("]").append(_BLANK);  
        sb.append("[").append(CommonUtil.getDateTime()).append("]").append(_BLANK); 
        
        /*
        sb.append("&#60;/font&#62;");
        sb.append("&lt;font color=&quot;#993300&quot;&gt;&lt;strong&gt;");
        sb.append("[").append(clazz).append("]").append(_BLANK);  
        sb.append("[").append(SessionUtil.getCurrentRequest().getRemoteAddr()).append("]").append(_BLANK);  
        sb.append("[").append(SessionUtil.getAddName()).append("]").append(_BLANK);  
        sb.append("[").append(CommonUtil.getDateTime()).append("]").append(_BLANK); 
        sb.append("&lt;/strong&gt;&lt;");
        */
        return sb;
	}
	
	public void  infoAll(String paramObject){
		//System.out.println();
		StringBuilder sb = new StringBuilder();  
		sb.append(getMessageHead());
        sb.append(paramObject);
        
        //System.out.println("===="+_logger.getName());
        _logger.info(sb.toString());
	}

	public void info(Object paramObject, Throwable paramThrowable) {
		_logger.info(paramObject, paramThrowable);
	}
	
	public void infoAll(Object paramObject, Throwable paramThrowable) {
		StringBuilder sb = new StringBuilder();  
		sb.append(getMessageHead());
        sb.append(paramObject);
		_logger.info(sb.toString(), paramThrowable);
	}

	public void warn(Object paramObject) {
		StringBuilder sb = new StringBuilder();  
		sb.append(getMessageHead());
        sb.append(paramObject);
		_logger.warn(sb.toString());
	}

	public void warn(Object paramObject, Throwable paramThrowable) {
		StringBuilder sb = new StringBuilder();  
		sb.append(getMessageHead());
        sb.append(paramObject);
		_logger.warn(sb.toString(), paramThrowable);
	}
}
