package com.state.util;

import java.util.List;

public class MParentTree extends MChildrenTree {
	private List<MParentTree> children;

	public List<MParentTree> getChildren() {
		return children;
	}

	public void setChildren(List<MParentTree> children) {
		this.children = children;
	}
}
