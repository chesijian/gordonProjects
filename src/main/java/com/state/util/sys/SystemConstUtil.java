package com.state.util.sys;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.state.enums.Enums_SessionType;
import com.state.enums.Enums_SystemConst;
import com.state.listener.TomcatListener;
import com.state.po.sys.PfSysConfigPo;
import com.state.util.BtnTimeUtil;
import com.state.util.CommonUtil;
import com.state.util.DeclareUtil;
import com.state.util.MatchUtil;
import com.state.util.SystemTools;
import com.state.util.TimeUtil;

/**
 * 系统常量
 * 
 * @description
 * @author 大雄
 * @date 2016年9月9日下午3:25:23
 */
public class SystemConstUtil {

	/**
	 * 流程按钮的状态
	 */
	private static Map<String, Integer> PROCESS_BTN_STATUS = new HashMap<String, Integer>();

	/**
	 * 调用c程序优化计算的时候，获取c程序的路径和数据结果的路径
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月9日下午3:21:16
	 * @return
	 */
	public static String getMatchPath() {
		if (SystemTools.isLinux()) {
			return Enums_SystemConst.RESULT_FILE_PATH_LINUX;
		} else {
			return Enums_SystemConst.RESULT_FILE_PATH_WIN;
		}
	}
	/**
	 * 获取日内
	 * @description
	 * @author 大雄
	 * @date 2016年11月10日下午2:51:35
	 * @return
	 */
	public static String getIntraMatchPath() {
		if (SystemTools.isLinux()) {
			return Enums_SystemConst.RESULT_FILE_PATH_LINUX_INTRA;
		} else {
			return Enums_SystemConst.RESULT_FILE_PATH_WIN_INTRA;
		}
	}

	/**
	 * 获取临时目录
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月26日下午2:39:49
	 * @return
	 */
	public static String getTempPath() {
		if (SystemTools.isLinux()) {
			return Enums_SystemConst.RESULT_FILE_TEMP_PATH_LINUX;
		} else {
			return Enums_SystemConst.RESULT_FILE_TEMP_PATH_WIN;
		}
	}

	/**
	 * 获取签名的路径
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年9月26日下午7:53:47
	 * @return
	 */
	public static String getIconPath() {
		return TomcatListener.getIconPath();

	}

	/**
	 * 判断当前时间是否在time之前
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年10月13日下午4:18:25
	 * @param time
	 * @return
	 */
	public static boolean isBeforeTime(String time) {
		String now = TimeUtil.getTimeShort();
		String[] nowStr = now.split(":");
		String hour_n = nowStr[0];
		String minute_n = nowStr[1];
		String[] timeStr = time.split(":");
		String hour_t = timeStr[0];
		String minute_t = timeStr[1];
		if (Integer.parseInt(hour_n) < Integer.parseInt(hour_t)) {
			return true;
		} else if (Integer.parseInt(hour_n) == Integer.parseInt(hour_t)) {
			if (Integer.parseInt(minute_n) < Integer.parseInt(minute_t)) {
				return true;
			}
		}
		return false;
	}

	
	
	

	public static Map<String, Integer> getProcessBtnStatus(String time) {
		// System.out.println("---"+CommonUtil.objToJson(PROCESS_BTN_STATUS));
		//String mdate = DeclareUtil.getMdate();
//		if(time.equals(mdate)){
			return PROCESS_BTN_STATUS;
//		}else{
//		Map<String,Integer> map = new HashMap<String, Integer>();
//		for(int i = 1;i<6;i++){
//			if(!map.containsKey("node"+i)){
//				map.put("node"+i, 3);
//			}
//		}
//		return map;
//		}
	}

	/**
	 * 定时更新按钮状态
	 * 
	 * @description
	 * @author 大雄
	 * @date 2016年10月8日下午9:17:48
	 * @param btns
	 * @throws ParseException
	 */
	public static void updateProcessBtnStatus(PfSysConfigPo processTime, PfSysConfigPo processConfig,String mdate) {
		//获取下一天的时间
		String time = DeclareUtil.getMdate();
		//System.out.println(mdate);
		PROCESS_BTN_STATUS.clear();
		if(time.equals(mdate)){
			//获取系统的当前时间，例如：11：30
			//String currentDate = TimeUtil.getTimeShort();
			//当前系统时间在11：00之前
			if(isBeforeTime(BtnTimeUtil.LINE_LIMIT_DATE)){
				PROCESS_BTN_STATUS.put("node1", 2);
			}else{
				if(isBeforeTime(BtnTimeUtil.DECLARE_DATE)){
					PROCESS_BTN_STATUS.put("node1", 3);
					PROCESS_BTN_STATUS.put("node2", 2);
				}else{
					if(isBeforeTime(BtnTimeUtil.MATCH_DATE)){
						PROCESS_BTN_STATUS.put("node1", 3);
						PROCESS_BTN_STATUS.put("node2", 3);
						PROCESS_BTN_STATUS.put("node3", 2);
					}else{
						if(isBeforeTime(BtnTimeUtil.ISSUE_DATE)){
							PROCESS_BTN_STATUS.put("node1", 3);
							PROCESS_BTN_STATUS.put("node2", 3);
							PROCESS_BTN_STATUS.put("node3", 3);
							PROCESS_BTN_STATUS.put("node4", 2);
						}else{
							PROCESS_BTN_STATUS.put("node1", 3);
							PROCESS_BTN_STATUS.put("node2", 3);
							PROCESS_BTN_STATUS.put("node3", 3);
							PROCESS_BTN_STATUS.put("node4", 3);
							PROCESS_BTN_STATUS.put("node5", 2);
						}
					}
				}
			}
			if(processConfig != null){
				String date = processConfig.getKey();
				//if(date.equals(mdate)){
					String config = processConfig.getValue();
					if(CommonUtil.ifEmpty(config)){
						Map<String,Integer> map = CommonUtil.jsonToMapInteger(config);
						//判断是否已经点击了可用容量发布
						if(map.containsKey(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_KYRLFB)&&map.get(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_KYRLFB) ==1){
							PROCESS_BTN_STATUS.put("node1", 3);
							if(!PROCESS_BTN_STATUS.containsKey("node2")||PROCESS_BTN_STATUS.get("node2")==1){
								PROCESS_BTN_STATUS.put("node2", 2);
							}
						}
						//点击了集中竞价
						if(map.containsKey(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_JZJJ)&&map.get(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_JZJJ) ==1){
							//PROCESS_BTN_STATUS.put("node1", 3);
							PROCESS_BTN_STATUS.put("node2", 3);
							PROCESS_BTN_STATUS.put("node3", 3);
							if(!PROCESS_BTN_STATUS.containsKey("node4")||PROCESS_BTN_STATUS.get("node4")==1){
								PROCESS_BTN_STATUS.put("node4", 2);
							}
						}
						//点击了出清发布
						if(map.containsKey(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_JGFB)&&map.get(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_JGFB) ==1){
							//PROCESS_BTN_STATUS.put("node1", 3);
							//PROCESS_BTN_STATUS.put("node2", 3);
							//PROCESS_BTN_STATUS.put("node3", 3);
							PROCESS_BTN_STATUS.put("node4", 3);
							if(!PROCESS_BTN_STATUS.containsKey("node5")||PROCESS_BTN_STATUS.get("node5")==1){
								PROCESS_BTN_STATUS.put("node5", 2);
							}
						}
					}
				//}
				
			}
			for(int i = 1;i<6;i++){
				if(!PROCESS_BTN_STATUS.containsKey("node"+i)){
					PROCESS_BTN_STATUS.put("node"+i, 1);
				}
			}
//			if(processTime != null){
//				if (isBeforeTime(processTime.getValue())) {
//					PROCESS_BTN_STATUS.put("node2", 2);
//					
//				}else{
//					PROCESS_BTN_STATUS.put("node2", 3);
//					PROCESS_BTN_STATUS.put("node3", 2);
//					//PROCESS_BTN_STATUS.put("node1", 3);
//				}
//			}else{
//				if (isBeforeTime("10:00")) {
//					PROCESS_BTN_STATUS.put("node2", 2);
//					
//				}else{
//					PROCESS_BTN_STATUS.put("node2", 3);
//					PROCESS_BTN_STATUS.put("node3", 2);
//					//PROCESS_BTN_STATUS.put("node1", 3);
//				}
//			}
		}else if(TimeUtil.strToDate(mdate).after(TimeUtil.strToDate(time))){
			for(int i = 1;i<6;i++){
				if(!PROCESS_BTN_STATUS.containsKey("node"+i)){
					PROCESS_BTN_STATUS.put("node"+i, 1);
				}
			}
			if(processConfig != null){
				String date = processConfig.getKey();
				//if(date.equals(mdate)){
					String config = processConfig.getValue();
					if(CommonUtil.ifEmpty(config)){
						Map<String,Integer> map = CommonUtil.jsonToMapInteger(config);
						//判断是否已经点击了可用容量发布
						if(map.containsKey(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_KYRLFB)&&map.get(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_KYRLFB) ==1){
							PROCESS_BTN_STATUS.put("node1", 3);
							PROCESS_BTN_STATUS.put("node2", 2);
						}
						//点击了集中竞价
						if(map.containsKey(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_JZJJ)&&map.get(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_JZJJ) ==1){
							//PROCESS_BTN_STATUS.put("node1", 3);
							PROCESS_BTN_STATUS.put("node2", 3);
							PROCESS_BTN_STATUS.put("node3", 3);
							PROCESS_BTN_STATUS.put("node4", 2);
						}
						//点击了出清发布
						if(map.containsKey(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_JGFB)&&map.get(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_JGFB) ==1){
							//PROCESS_BTN_STATUS.put("node1", 3);
							//PROCESS_BTN_STATUS.put("node2", 3);
							//PROCESS_BTN_STATUS.put("node3", 3);
							PROCESS_BTN_STATUS.put("node4", 3);
							PROCESS_BTN_STATUS.put("node5", 2);
						}
					}
				//}
				
			}
			//PROCESS_BTN_STATUS.put("node2", 2);
		}else if(TimeUtil.strToDate(mdate).before(TimeUtil.strToDate(time))){
			String currentTime = TimeUtil.getStringDateShort();
			if(mdate.equals(currentTime)){
				if(MatchUtil.isClickButton(mdate, Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_JGFB) || isBeforeTime(BtnTimeUtil.ISSUE_RESULT_DATE)){
					PROCESS_BTN_STATUS.put("node5", 2);
				}
			}
			for(int i = 1;i<6;i++){
				if(!PROCESS_BTN_STATUS.containsKey("node"+i)){
					PROCESS_BTN_STATUS.put("node"+i, 3);
				}
			}
			//PROCESS_BTN_STATUS.put("node2", 3);
		}
		Integer kyrlfbBtn = 0;
		boolean kyrlfbFlag = MatchUtil.isClickButton(mdate, Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_KYRLFB);
		if(kyrlfbFlag){
			kyrlfbBtn = 1;
		}
		PROCESS_BTN_STATUS.put("kyrlfbBtn", kyrlfbBtn);
		Integer jgfbBtn = 0;
		boolean jgfbFlag = MatchUtil.isClickButton(mdate, Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_JGFB);
		if(jgfbFlag){
			jgfbBtn = 1;
		}
		PROCESS_BTN_STATUS.put("jgfbBtn", jgfbBtn);
		//如果是10点之前
		//PROCESS_BTN_STATUS.put("node1", 2);
//		if(processConfig != null){
//			String date = processConfig.getKey();
//			//if(date.equals(mdate)){
//				String config = processConfig.getValue();
//				if(CommonUtil.ifEmpty(config)){
//					Map<String,Integer> map = CommonUtil.jsonToMapInteger(config);
//					//判断是否已经点击了可用容量发布
//					if(map.containsKey(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_KYRLFB)&&map.get(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_KYRLFB) ==1){
//						PROCESS_BTN_STATUS.put("node1", 3);
//						//PROCESS_BTN_STATUS.put("node2", 1);
//					}
//					//点击了集中竞价
//					if(map.containsKey(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_JZJJ)&&map.get(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_JZJJ) ==1){
//						//PROCESS_BTN_STATUS.put("node1", 3);
//						//PROCESS_BTN_STATUS.put("node2", 3);
//						PROCESS_BTN_STATUS.put("node3", 3);
//						PROCESS_BTN_STATUS.put("node4", 2);
//					}
//					//点击了出清发布
//					if(map.containsKey(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_JGFB)&&map.get(Enums_SystemConst.SYS_CONFIG_TYPE＿PROCESS_CONFIG_JGFB) ==1){
//						//PROCESS_BTN_STATUS.put("node1", 3);
//						//PROCESS_BTN_STATUS.put("node2", 3);
//						//PROCESS_BTN_STATUS.put("node3", 3);
//						PROCESS_BTN_STATUS.put("node4", 3);
//						PROCESS_BTN_STATUS.put("node5", 2);
//					}
//				}
//			//}
//			
//		}
//		for(int i = 1;i<6;i++){
//			if(!PROCESS_BTN_STATUS.containsKey("node"+i)){
//				PROCESS_BTN_STATUS.put("node"+i, 1);
//			}
//		}
	}
}
