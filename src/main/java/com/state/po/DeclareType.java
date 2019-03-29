package com.state.po;

public enum DeclareType {
	
	FULL_DAY("1a","全天"),
	HIGH("2a","高峰"),
	LOW("3a","低谷");
	
	private String code;
	
	private String name;
	
	private DeclareType(String code,String name){
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static DeclareType parseByCode(String code){
		 if("1a".equals(code)){
			 return FULL_DAY;
		 }else if("2a".equals(code)){
			 return HIGH;
		 }else{
			 return LOW;
		 }
	}
	
	public static DeclareType parseByName(String name){
		if("全天".equals(name)){
			 return FULL_DAY;
		 }else if("高峰".equals(name)){
			 return HIGH;
		 }else{
			 return LOW;
		 }
	}
	

}
