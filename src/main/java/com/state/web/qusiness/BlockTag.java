package com.state.web.qusiness;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class BlockTag extends TagSupport{

	private static final long serialVersionUID = -2699727064669058213L;
	
	private String name;
	
	public void setName(String name){
		this.name = name;
	}
	
	@Override
	public int doStartTag() throws JspException {
		return getOverrideContent() == null ? EVAL_BODY_INCLUDE : SKIP_BODY;
	}
	
	@Override
	public int doEndTag() throws JspException {
		String overrideContent = getOverrideContent();
		if(null == overrideContent){
			return EVAL_PAGE;
		}
		try {
			pageContext.getOut().write(overrideContent);
		} catch (IOException e) {
			throw new JspException("tag output error",e);
		}
		return EVAL_PAGE;
	}
	
	private String getOverrideContent(){
		String varName = Utils.getOverrideVariableName(name);
		return (String)pageContext.getAttribute(varName);
	}

}
