package com.state.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.state.po.extend.LoggerInfo;
import com.state.util.properties.JProperties;

public class LoggerUtil {
	private static final LoggerInfo _LOGGER = new LoggerInfo("LoggerUtil.debug");

	

	/**
	 * 调试使用 flag0是info 1是error
	 * @author 大雄
	 * @Title log
	 * @Date 2015-5-18
	 * @Description TODO
	 * @Params @param title
	 * @Params @param msg
	 * @Params @param flag
	 * @Return void
	 */
	public static void log(String title,String msg, int flag) {
		LoggerInfo _log = new LoggerInfo(title);
		if (flag == 0) {
			_log.infoAll(msg);
		} else {
			_log.errorAll(msg);
		}
	}

	/**
	 * 调试使用 flag0是info 1是error
	 * 
	 * @author 大雄
	 * @Title log
	 * @Date 2015-4-15
	 * @Description TODO
	 * @Params @param msg
	 * @Params @param flag
	 * @Return void
	 */
	public static void log(String msg, int flag) {
		if (flag == 0) {
			_LOGGER.infoAll(msg);
		} else {
			_LOGGER.errorAll(msg);
		}
	}
}
