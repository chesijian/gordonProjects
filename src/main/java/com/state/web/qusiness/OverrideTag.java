package com.state.web.qusiness;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class OverrideTag extends BodyTagSupport {

	private static final long serialVersionUID = -2699727064669058213L;

	private String name;

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int doStartTag() throws JspException {
		return isOverride() ? SKIP_BODY : EVAL_BODY_AGAIN;
	}

	@Override
	public int doEndTag() throws JspException {
		if (isOverride()) {
			return EVAL_PAGE;
		}
		BodyContent b = getBodyContent();
		String varName = Utils.getOverrideVariableName(name);
		pageContext.setAttribute(varName, b.getString());
		return EVAL_PAGE;
	}

	private boolean isOverride() {
		String varName = Utils.getOverrideVariableName(name);
		return pageContext.getAttribute(varName) != null;
	}

}
