package com.state.util.properties;

import java.io.IOException;
import java.util.Properties;

/**
 * 
 * @author 大雄
 * @description 操作Properties文件的工具类
 * @date 2014-7-10
 */
public class JPropertiesUtil {
	public static final String CONFIG="config.properties";
	public static final String SYS_CONFIG="sys-config.properties";
	public static final String UPLOAD="upload.properties";
	public static final String BTN_TIME_CONFIG="time-config.properties";
	//是否过滤附件查看和下载权限
	public static final boolean IS_FILTER_ATTACHMENT;
	//是否允许跨域
	public static final boolean IS_ALLOW_EXTRA_REFERER;
	static{
		String isFilterAttachment = getSysConfigValue("isFilterAttachment");
		if(isFilterAttachment != null && isFilterAttachment.equals("1")){
			IS_FILTER_ATTACHMENT = true;
		}else{
			IS_FILTER_ATTACHMENT = false;
		}
		String isAllowExtraRefer = getSysConfigValue("isAllowExtraRefer");
		if(isAllowExtraRefer != null && isAllowExtraRefer.equals("0")){
			IS_ALLOW_EXTRA_REFERER = false;
		}else{
			IS_ALLOW_EXTRA_REFERER = true;
		}
	}
	/**
	 * 
	 * @author 大雄
	 * @Title getValue
	 * @Date 2014-7-10
	 * @Description 根据资源文件名，和键值获取value
	 * @Params @param propertiesFile
	 * @Params @param key
	 * @Params @return
	 * @Return String
	 */
	public static String getValue(String propertiesFile,String key){
		Properties p = new Properties();     
		try {
			p = JProperties.loadProperties(propertiesFile, JProperties.BY_CLASSLOADER);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		String fileDir = p.getProperty(key);
		
		return fileDir;
	}
	/**
	 * 获取sysConfig.properties中的值
	 * @author 大雄
	 * @Title getSysConfigValue
	 * @Date 2015-4-13
	 * @Description TODO
	 * @Params @param key
	 * @Params @return
	 * @Return String
	 */
	public static String getSysConfigValue(String key){
		return getValue(SYS_CONFIG,key);
	}
	
	public static String getConfigValue(String key){
		return getValue(CONFIG,key);
	}
}
