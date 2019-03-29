package com.state.enums;

/**
 * 
 * @description
 * @author 大雄
 * @date 2016年8月17日下午4:25:10
 */
public enum Enums_SystemConst {
	COUNTRAY("国调"),
	ROLE_ADMIN("ROLE_ADMIN"),
	ROLE_MANAGER("ROLE_SYS_MANAGER"),
	ROLE_OPERATER("ROLE_OPERATER"),
	ROLE_READER("ROLE_READER"),
	LOGIN_OVER_TIME("loginOverTime"),//登陆超时时间
	LOGIN_OVER_COUNT("loginOverCount"),//登陆超时次数
	SIGN_TYPE_ISSUE("发布"),//发布的电子签名
	SIGN_TYPE_DECLARE_MODIFY("单据修改"),//单据修改的电子签名
	SIGN_TYPE_DEAL_MODIFY("执行结果修改");//执行结果修改
	
	public static String  RESULT_FILE_NAME = "CBPM_国调_result_";
	public static String  RESULT_LOG_FILE_NAME = "CBPM_";//CBPM_20160914.log
	public static String  RESULT_FILE_PATH_LINUX = "/home/d5000/guodiao/var/osa/cbpm";
	public static String  RESULT_FILE_PATH_LINUX_INTRA = "/home/d5000/guodiao/var/osa/intraday";
	public static String  RESULT_FILE_PATH_WIN_INTRA = "d:\\temp\\guodiao\\intraday";//相对路径，在tomcat目录下
	public static String  RESULT_FILE_PATH_WIN = "d:\\temp\\guodiao\\cbpm";//相对路径，在tomcat目录下
	public static String  RESULT_FILE_TEMP_PATH_LINUX = "/home/d5000/guodiao/var/osa/cbpm/temp";
	public static String  RESULT_FILE_TEMP_PATH_WIN = "d:\\temp\\guodiao\\cbpm\\temp";//相对路径，在tomcat目录下
	
	/**
	 * 常量表中流程时间类型
	 */
	public static String SYS_CONFIG_TYPE＿PROCESS_TIME ="PROCESS_TIME";
	/**
	 * 常量表中流程常量类型
	 */
	public static String SYS_CONFIG_TYPE＿PROCESS_CONFIG ="PROCESS_CONFIG";
	/**
	 * 常量表中流程常量类型中的值
	 */
	public static String SYS_CONFIG_TYPE＿PROCESS_CONFIG_KYRLFB ="可用容量发布";
	public static String SYS_CONFIG_TYPE＿PROCESS_CONFIG_JZJJ ="集中竞价";
	public static String SYS_CONFIG_TYPE＿PROCESS_CONFIG_JGFB ="结果发布";
	
	public static String SYS_CONFIG_TYPE＿SHEETCOST_SUM ="SHEETCOST_SUM";
	
	/**
	 * 限额计算方式
	 */
	public static String SPACE_EXCUTE_TYPE="限额计算方式";
	
	private String value;
	

	private Enums_SystemConst(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	
	
}
