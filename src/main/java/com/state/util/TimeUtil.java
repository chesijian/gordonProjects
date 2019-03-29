package com.state.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.sql.Timestamp;
import java.text.*;

import com.state.util.sys.SystemConstUtil;

public abstract class TimeUtil {

	// ---当前日期的年，月，日，时，分，秒
	public static Calendar now = Calendar.getInstance();
	int year = now.get(Calendar.YEAR);
	int date = now.get(Calendar.DAY_OF_MONTH);
	int month = now.get(Calendar.MONTH) + 1;
	int hour = now.get(Calendar.HOUR);
	int min = now.get(Calendar.MINUTE);
	int sec = now.get(Calendar.SECOND);

	// -------------------------------日期类型转换---------------------------------------------------------------------------
	/**
	 * 字符型日期转化util.Date型日期
	 * 
	 * @Param:p_strDate 字符型日期
	 * @param p_format
	 *            格式:"yyyy-MM-dd" / "yyyy-MM-dd hh:mm:ss"
	 * @Return:java.util.Date util.Date型日期
	 * @Throws: ParseException
	 * @Author: zhuqx
	 * @Date: 2006-10-31
	 */
	public static java.util.Date toUtilDateFromStrDateByFormat(String p_strDate, String p_format) throws ParseException {
		java.util.Date l_date = null;
		java.text.DateFormat df = new java.text.SimpleDateFormat(p_format);
		if (p_strDate != null && (!"".equals(p_strDate)) && p_format != null && (!"".equals(p_format))) {
			l_date = df.parse(p_strDate);
		}
		return l_date;
	}

	/**
	 * 字符型日期转化成sql.Date型日期
	 * 
	 * @param p_strDate
	 *            字符型日期
	 * @return java.sql.Date sql.Date型日期
	 * @throws ParseException
	 * @Author: zhuqx
	 * @Date: 2006-10-31
	 */
	public static java.sql.Date toSqlDateFromStrDate(String p_strDate) throws ParseException {
		java.sql.Date returnDate = null;
		java.text.DateFormat sdf = new java.text.SimpleDateFormat();
		if (p_strDate != null && (!"".equals(p_strDate))) {
			returnDate = new java.sql.Date(sdf.parse(p_strDate).getTime());
		}
		return returnDate;
	}

	/**
	 * util.Date型日期转化指定格式的字符串型日期
	 * 
	 * @param p_date
	 *            Date
	 * @param p_format
	 *            String 格式1:"yyyy-MM-dd" 格式2:"yyyy-MM-dd hh:mm:ss EE" 格式3:"yyyy年MM月dd日 hh:mm:ss EE" 说明: 年-月-日 时:分:秒 星期 注意MM/mm大小写
	 * @return String
	 * @Author: zhuqx
	 * @Date: 2006-10-31
	 */
	public static String toStrDateFromUtilDateByFormat(java.util.Date p_utilDate, String p_format) throws ParseException {
		String l_result = "";
		if (p_utilDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(p_format);
			l_result = sdf.format(p_utilDate);
		}
		return l_result;
	}

	/**
	 * util.Date型日期转化转化成Calendar日期
	 * 
	 * @param p_utilDate
	 *            Date
	 * @return Calendar
	 * @Author: zhuqx
	 * @Date: 2006-10-31
	 */
	public static Calendar toCalendarFromUtilDate(java.util.Date p_utilDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(p_utilDate);
		return c;
	}

	/**
	 * util.Date型日期转化sql.Date(年月日)型日期
	 * 
	 * @Param: p_utilDate util.Date型日期
	 * @Return: java.sql.Date sql.Date型日期
	 * @Author: zhuqx
	 * @Date: 2006-10-31
	 */
	public static java.sql.Date toSqlDateFromUtilDate(java.util.Date p_utilDate) {
		java.sql.Date returnDate = null;
		if (p_utilDate != null) {
			returnDate = new java.sql.Date(p_utilDate.getTime());
		}
		return returnDate;
	}

	/**
	 * util.Date型日期转化sql.Time(时分秒)型日期
	 * 
	 * @Param: p_utilDate util.Date型日期
	 * @Return: java.sql.Time sql.Time型日期
	 * @Author: zhuqx
	 * @Date: 2006-10-31
	 */
	public static java.sql.Time toSqlTimeFromUtilDate(java.util.Date p_utilDate) {
		java.sql.Time returnDate = null;
		if (p_utilDate != null) {
			returnDate = new java.sql.Time(p_utilDate.getTime());
		}
		return returnDate;
	}

	/**
	 * util.Date型日期转化sql.Date(时分秒)型日期
	 * 
	 * @Param: p_utilDate util.Date型日期
	 * @Return: java.sql.Timestamp sql.Timestamp型日期
	 * @Author: zhuqx
	 * @Date: 2006-10-31
	 */
	public static java.sql.Timestamp toSqlTimestampFromUtilDate(java.util.Date p_utilDate) {
		java.sql.Timestamp returnDate = null;
		if (p_utilDate != null) {
			returnDate = new java.sql.Timestamp(p_utilDate.getTime());
		}
		return returnDate;
	}
	/**
	 * 
	 * @author 大雄
	 * @Title toTimestamp
	 * @Date 2014-8-20
	 * @Description 字符串转换为SqlTimestamp
	 * @Params @param strDate
	 * @Params @return
	 * @Return java.sql.Timestamp
	 */
	public static java.sql.Timestamp toSqlTimestamp(String strDate){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = df.format(TimeUtil.strToDateLong(strDate));
		Timestamp ts = Timestamp.valueOf(time);
		return ts;
	}
	

	/**
	 * sql.Date型日期转化util.Date型日期
	 * 
	 * @Param: sqlDate sql.Date型日期
	 * @Return: java.util.Date util.Date型日期
	 * @Author: zhuqx
	 * @Date: 2006-10-31
	 */
	public static java.util.Date toUtilDateFromSqlDate(java.sql.Date p_sqlDate) {
		java.util.Date returnDate = null;
		if (p_sqlDate != null) {
			returnDate = new java.util.Date(p_sqlDate.getTime());
		}
		return returnDate;
	}

	// -----------------获取指定日期的年份，月份，日份，小时，分，秒，毫秒----------------------------
	/**
	 * 获取指定日期的年份
	 * 
	 * @param p_date
	 *            util.Date日期
	 * @return int 年份
	 * @author zhuqx
	 * @Date: 2006-10-31
	 */
	public static int getYearOfDate(java.util.Date p_date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(p_date);
		return c.get(java.util.Calendar.YEAR);
	}

	/**
	 * 获取指定日期的月份
	 * 
	 * @param p_date
	 *            util.Date日期
	 * @return int 月份
	 * @author zhuqx
	 * @Date: 2006-10-31
	 */
	public static int getMonthOfDate(java.util.Date p_date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(p_date);
		return c.get(java.util.Calendar.MONTH) + 1;
	}

	/**
	 * 获取指定日期的日份
	 * 
	 * @param p_date
	 *            util.Date日期
	 * @return int 日份
	 * @author zhuqx
	 * @Date: 2006-10-31
	 */
	public static int getDayOfDate(java.util.Date p_date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(p_date);
		return c.get(java.util.Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取指定日期的小时
	 * 
	 * @param p_date
	 *            util.Date日期
	 * @return int 日份
	 * @author zhuqx
	 * @Date: 2006-10-31
	 */
	public static int getHourOfDate(java.util.Date p_date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(p_date);
		return c.get(java.util.Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取指定日期的分钟
	 * 
	 * @param p_date
	 *            util.Date日期
	 * @return int 分钟
	 * @author zhuqx
	 * @Date: 2006-10-31
	 */
	public static int getMinuteOfDate(java.util.Date p_date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(p_date);
		return c.get(java.util.Calendar.MINUTE);
	}

	/**
	 * 获取指定日期的秒钟
	 * 
	 * @param p_date
	 *            util.Date日期
	 * @return int 秒钟
	 * @author zhuqx
	 * @Date: 2006-10-31
	 */
	public static int getSecondOfDate(java.util.Date p_date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(p_date);
		return c.get(java.util.Calendar.SECOND);
	}

	/**
	 * 获取指定日期的毫秒
	 * 
	 * @param p_date
	 *            util.Date日期
	 * @return long 毫秒
	 * @author zhuqx
	 * @Date: 2006-10-31
	 */
	public static long getMillisOfDate(java.util.Date p_date) {
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.setTime(p_date);
		return c.getTimeInMillis();
	}

	// -----------------获取当前/系统日期(指定日期格式)-----------------------------------------------------------------------------------
	/**
	 * 获取指定日期格式当前日期的字符型日期
	 * 
	 * @param p_format
	 *            日期格式 格式1:"yyyy-MM-dd" 格式2:"yyyy-MM-dd hh:mm:ss EE" 格式3:"yyyy年MM月dd日 hh:mm:ss EE" 说明: 年-月-日 时:分:秒 星期 注意MM/mm大小写
	 * @return String 当前时间字符串
	 * @author zhuqx
	 * @Date: 2006-10-31
	 */
	public static String getNowOfDateByFormat(String p_format) {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(p_format);
		String dateStr = sdf.format(d);
		return dateStr;
	}

	/**
	 * 获取指定日期格式系统日期的字符型日期
	 * 
	 * @param p_format
	 *            日期格式 格式1:"yyyy-MM-dd" 格式2:"yyyy-MM-dd hh:mm:ss EE" 格式3:"yyyy年MM月dd日 hh:mm:ss EE" 说明: 年-月-日 时:分:秒 星期 注意MM/mm大小写
	 * @return String 系统时间字符串
	 * @author zhuqx
	 * @Date: 2006-10-31
	 */
	public static String getSystemOfDateByFormat(String p_format) {
		long time = System.currentTimeMillis();
		Date d2 = new Date();
		Date d = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat(p_format);
		String dateStr = sdf.format(d);
		return dateStr;
	}

	/**
	 * 获取字符日期一个月的天数
	 * 
	 * @param p_date
	 * @return 天数
	 * @author zhuqx
	 */
	public static long getDayOfMonth(Date p_date) throws ParseException {
		int year = getYearOfDate(p_date);
		int month = getMonthOfDate(p_date) - 1;
		int day = getDayOfDate(p_date);
		int hour = getHourOfDate(p_date);
		int minute = getMinuteOfDate(p_date);
		int second = getSecondOfDate(p_date);
		Calendar l_calendar = new GregorianCalendar(year, month, day, hour, minute, second);
		return l_calendar.getActualMaximum(l_calendar.DAY_OF_MONTH);
	}

	// -----------------获取指定月份的第一天,最后一天 ---------------------------------------------------------------------------
	/**
	 * 获取指定月份的第一天
	 * 
	 * @param p_strDate
	 *            指定月份
	 * @param p_formate
	 *            日期格式
	 * @return String 时间字符串
	 * @author zhuqx
	 * @Date: 2006-10-31
	 */
	public static String getDateOfMonthBegin(String p_strDate, String p_format) throws ParseException {
		java.util.Date date = toUtilDateFromStrDateByFormat(p_strDate, p_format);
		return toStrDateFromUtilDateByFormat(date, "yyyy-MM") + "-01";
	}

	/**
	 * 获取指定月份的最后一天
	 * 
	 * @param p_strDate
	 *            指定月份
	 * @param p_formate
	 *            日期格式
	 * @return String 时间字符串
	 * @author zhuqx
	 * @Date: 2006-10-31
	 */
	public static String getDateOfMonthEnd(String p_strDate, String p_format) throws ParseException {
		java.util.Date date = toUtilDateFromStrDateByFormat(getDateOfMonthBegin(p_strDate, p_format), p_format);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		return toStrDateFromUtilDateByFormat(calendar.getTime(), p_format);
	}

	/**
	   * 获取现在时间
	   * 
	   * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
	   */
	public static Date getNowDate() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String dateString = formatter.format(currentTime);
	   ParsePosition pos = new ParsePosition(8);
	   Date currentTime_2 = formatter.parse(dateString, pos);
	   return currentTime_2;
	}

	/**
	   * 获取现在时间
	   * 
	   * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
	   */
	public static String getStringDate() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String dateString = formatter.format(currentTime);
	   return dateString;
	}
	/**
	   * 获取现在时间
	   * 
	   * @return 返回短时间字符串格式yyyy-MM-dd
	   */
	public static String getStringDateShort() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	   String dateString = formatter.format(currentTime);
	   return dateString;
	}
	/**
	   * 获取现在时间
	   * 
	   * @return 返回短时间字符串格式yyyyMMdd
	   */
	public static String getStringDateSimple() {
		   Date currentTime = new Date();
		   SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		   String dateString = formatter.format(currentTime);
		   return dateString;
		}
	
	/**
	   * 获取时间 小时:分;秒 HH:mm:ss
	   * 
	   * @return
	   */
	public static String getTimeShort() {
	   SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	   Date currentTime = new Date();
	   String dateString = formatter.format(currentTime);
	   return dateString;
	}
	/**
	   * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	   * 
	   * @param strDate
	   * @return
	   */
	public static Date strToDateLong(String strDate) {
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   ParsePosition pos = new ParsePosition(0);
	   Date strtodate = formatter.parse(strDate, pos);
	   return strtodate;
	}
	
	
	
	/**
	   * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
	   * 
	   * @param dateDate
	   * @return
	   */
	public static String dateToStrLong(java.util.Date dateDate) {
		if(!CommonUtil.ifEmpty(dateDate)){
			return "";
		}
		DateFormat mediumDateFormat = 
				DateFormat.getDateTimeInstance( 
				DateFormat.MEDIUM, 
				DateFormat.MEDIUM);
		//System.out.println(dateDate.toGMTString());;
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String dateString = formatter.format(dateDate);
	   return dateString;
	}
	
	public static String dateToStrLong(java.util.Date dateDate,String format) {
		if(!CommonUtil.ifEmpty(dateDate)){
			return "";
		}
		//System.out.println(dateDate.toGMTString());;
	   SimpleDateFormat formatter = new SimpleDateFormat(format);
	   String dateString = formatter.format(dateDate);
	   return dateString;
	}
	
	public static Timestamp getTimestamp(){
		return new Timestamp(new Date().getTime());
	}
	
	public static String timestampToStrLong(Timestamp time){
		if(time == null){
			return "";
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
		return df.format(time);
	}
	
	public static String timestampStrToStrLong(String dateTime){
		if(dateTime == null){
			return "";
		}
		Timestamp ts = Timestamp.valueOf(dateTime); 
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
		return df.format(dateTime);
	}
	
	/**
	   * 将短时间格式时间转换为字符串 yyyy-MM-dd
	   * 
	   * @param dateDate
	   * @param k
	   * @return
	   */
	public static String dateToStr(java.util.Date dateDate) {
		if(!CommonUtil.ifEmpty(dateDate)){
			return "";
		}
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	   String dateString = formatter.format(dateDate);
	   return dateString;
	}
	/**
	   * 将短时间格式字符串转换为时间 yyyy-MM-dd 
	   * 
	   * @param strDate
	   * @return
	   */
	public static Date strToDate(String strDate) {
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	   ParsePosition pos = new ParsePosition(0);
	   Date strtodate = formatter.parse(strDate, pos);
	   return strtodate;
	}
	/**
	   * 得到现在时间
	   * 
	   * @return
	   */
	public static Date getNow() {
	   Date currentTime = new Date();
	   return currentTime;
	}
	/**
	   * 提取一个月中的最后一天
	   * 
	   * @param day
	   * @return
	   */
	public static Date getLastDate(long day) {
	   Date date = new Date();
	   long date_3_hm = date.getTime() - 3600000 * 34 * day;
	   Date date_3_hm_date = new Date(date_3_hm);
	   return date_3_hm_date;
	}
	/**
	   * 得到现在时间
	   * 
	   * @return 字符串 yyyyMMdd HHmmss
	   */
	public static String getStringToday() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
	   String dateString = formatter.format(currentTime);
	   return dateString;
	}
	/**
	   * 得到现在小时
	   */
	public static String getHour() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String dateString = formatter.format(currentTime);
	   String hour;
	   hour = dateString.substring(11, 13);
	   return hour;
	}
	/**
	   * 得到现在分钟
	   * 
	   * @return
	   */
	public static String getTime() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String dateString = formatter.format(currentTime);
	   String min;
	   min = dateString.substring(14, 16);
	   return min;
	}
	/**
	   * 根据用户传入的时间表示格式，返回当前时间的格式 如果是yyyyMMdd，注意字母y不能大写。
	   * 
	   * @param sformat
	   *             yyyyMMddhhmmss
	   * @return
	   */
	public static String getUserDate(String sformat) {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat(sformat);
	   String dateString = formatter.format(currentTime);
	   return dateString;
	}
	
	public static Calendar getCurrentCalendar(Date date){
		Calendar cal=Calendar.getInstance();
		cal.clear();
		cal.setTime(date);
		return cal;
	}
	/**
	 * // 获得当前日期与本周一相差的天数
	 * @return
	 */
	private static int getMondayPlus(Date date) {  
		
	    Calendar cd = Calendar.getInstance();  
	    cd.clear();
	    cd.setTime(date);
	    // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......   
	    int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);  
	    if (dayOfWeek == 1) {  
	        return -6;  
	    } else {  
	        return 2 - dayOfWeek;  
	    }  
	}  
	
	/**
	 * 获取当前日期所在周的周一
	 * @param date
	 * @return
	 */
	public static Calendar getCurrentMonday(Date date){
		
	    Calendar cd = Calendar.getInstance();  
	    cd.clear();
	    cd.setTime(date);
	    cd.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
	    /*
	     * 
	    int weeks = 0;  
	    int mondayPlus = getMondayPlus(date); 
	    GregorianCalendar currentDate = new GregorianCalendar();  
	    currentDate.add(GregorianCalendar.DATE, mondayPlus);  
	    Date monday = currentDate.getTime();  
	    DateFormat df = DateFormat.getDateInstance();  
	    String preMonday = df.format(monday);  
	    */
	    SimpleDateFormat sdf =new SimpleDateFormat("y年M月d日 E H时m分s秒",Locale.CHINA);
	    //System.out.println("周一时间:"+sdf.format(cd.getTime())); 
	    return cd;  
	}
	/**
	 *  Jun 26,2014 4:15:04 PM 转换成日期 yyyy-MM-dd HH:mm:ss
	 * @author 大雄
	 * @Title convertWorldDateToCommon
	 * @Date 2016-6-20
	 * @Description TODO
	 * @param dateStr
	 * @return
	 * @throws ParseException 
	 * @Return String
	 */
	public static String convertWorldDateToCommon(String dateStr) throws ParseException{
		DateFormat formatFrom = new SimpleDateFormat("MMM dd,yyyy KK:mm:ss aa", Locale.ENGLISH);
		  Date date = formatFrom.parse(dateStr);
		  DateFormat formatTo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  return formatTo.format(date);
	}
	
	public static void main(String[] args) {
		//System.out.println(getNowOfDateByFormat("yyyyMMdd"));
		System.out.println(getWeekInYear("20160104"));
	}
	/**
	 * 获取当前是周几
	 * @description
	 * @author 大雄
	 * @date 2016年9月12日下午6:32:30
	 * @param dt
	 * @return
	 */
	public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
	
	/**
	 * 获取是周几
	 * @description
	 * @author 大雄
	 * @date 2016年9月12日下午6:33:28
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	public static String getWeekOfDate(String date) throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dBegin = df.parse(date);
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dBegin);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
	
	/**
	 * 获取日期的前一天
	 * @description
	 * @author 大雄
	 * @date 2016年10月17日上午10:10:32
	 * @param date
	 * @return
	 */
	public static Date getPreDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return date;
	}
	/**
	 * 获取日期的下一天
	 * @description
	 * @author 大雄
	 * @date 2016年10月17日上午10:10:32
	 * @param date
	 * @return
	 */
	public static Date getNextDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		date = calendar.getTime();
		return date;
	}
	/**
	 * 
	 * @author 车斯剑
	 * @date 2016年10月20日下午1:43:21
	 * @param date
	 * @return
	 */
	public static int getWeekInYear(String date) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		try {
			cal.setTime(format.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String week = getWeekDay(date, "yyyyMMdd");
		int weekNum = cal.get(Calendar.WEEK_OF_YEAR);
		if(week.equals("周日")){
			weekNum--;
		}
		//System.out.println("----"+week);
		return weekNum;
	}
	
	/**
	 * 
	 * @author 刘新平
	 * @date 2016年10月17日上午10:18:08
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static String getNextDayDate(String dateStr,String pattern){
		SimpleDateFormat sdFormat = new SimpleDateFormat(pattern);
		Calendar cal = Calendar.getInstance();
		Date date = null;
		try {
			date = sdFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		date = cal.getTime();
		return sdFormat.format(date)+"";
	}
	/**
	 * 得到当前时间是周几
	 * @author 刘新平
	 * @param str 20161010
	 * @param pattern yyyyMMdd
	 * @return
	 */
	public static String getWeekDay(String str,String pattern){
		String week = "";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int a = cal.get(Calendar.DAY_OF_WEEK)-1;
		//System.out.println(a);
		if(a==1){
			week = "周一";
		}else if(a==2){
			week = "周二";
		}else if(a==3){
			week = "周三";
		}else if(a==4){
			week = "周四";
		}else if(a==5){
			week = "周五";
		}else if(a==6){
			week = "周六";
		}else if(a==0){
			week = "周日";
		}
		return week;
	}
	/**
	 * 判断是否在该节点对应的时间之前
	 * @param time
	 * @param nodeName
	 * @return
	 */
	public static boolean isOverTime(String time,String nodeName){
		//当前系统时间
		String nodeTime = "";
		if(nodeName.equals("node1")){
			nodeTime = BtnTimeUtil.LINE_LIMIT_DATE;
		}else if(nodeName.equals("node2")){
			nodeTime = BtnTimeUtil.DECLARE_DATE;
		}else if(nodeName.equals("node3")){
			nodeTime = BtnTimeUtil.MATCH_DATE;
		}else if(nodeName.equals("node4")){
			nodeTime = BtnTimeUtil.ISSUE_DATE;
		}
		if(SystemConstUtil.isBeforeTime(nodeTime)){
			return true;
		}
		return false;
	}
}