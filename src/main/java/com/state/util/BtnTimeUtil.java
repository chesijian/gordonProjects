package com.state.util;

import com.state.util.properties.JPropertiesUtil;

public class BtnTimeUtil {
	//自动执行获取计划和限额的时间
	public static String IMPORT_LIMIT_LINE = "";
	//信息公告的截止时间
	public static String LINE_LIMIT_DATE = "";
	//交易申报的截止日期
	public static String DECLARE_DATE = "";
	//自动执行导入申报单的时间
	public static String IMPORT_DECLARE_DATE = "";
	//集中竞价的截止日期
	public static String MATCH_DATE = "";
	//交易结果发布的截止日期
	public static String ISSUE_DATE = "";
	//执行结果的截止日期
	public static String ISSUE_RESULT_DATE = "";
	
	static{
		IMPORT_LIMIT_LINE = JPropertiesUtil.getValue(JPropertiesUtil.BTN_TIME_CONFIG, "importLinelimit");
		LINE_LIMIT_DATE = JPropertiesUtil.getValue(JPropertiesUtil.BTN_TIME_CONFIG, "linelimitdate");
		DECLARE_DATE = JPropertiesUtil.getValue(JPropertiesUtil.BTN_TIME_CONFIG, "declaredate");
		IMPORT_DECLARE_DATE = JPropertiesUtil.getValue(JPropertiesUtil.BTN_TIME_CONFIG, "importDeclareDate");
		MATCH_DATE = JPropertiesUtil.getValue(JPropertiesUtil.BTN_TIME_CONFIG, "matchdate");
		ISSUE_DATE = JPropertiesUtil.getValue(JPropertiesUtil.BTN_TIME_CONFIG, "issuedate");
		ISSUE_RESULT_DATE = JPropertiesUtil.getValue(JPropertiesUtil.BTN_TIME_CONFIG, "issueResultDate");
	}
	
	
}
