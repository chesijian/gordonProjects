package com.state.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取主机信息
 * 
 * @author 大雄
 * @version 创建时间:2013-12-30 17:36:14
 * 
 */
public class SystemTools {

	public final static String WINDWOS_DIR = "c:/windows/SysSerialNum.license";
	public final static String LINUX_DIR = "/root/SysSerialNum.license";
	private final static String[] MOBILE_AGENT = { "Android", "iPhone", "iPod", "iPad", "Windows Phone", "MQQBrowser" };
	/**
	 * 换行符
	 */
	public final static String _BR = "\r\n";
	/**
	 * 判断是否是手机端请求
	 * @author 大雄
	 * @Title isMobile
	 * @Date 2016-6-6
	 * @Description TODO
	 * @return
	 * @Return boolean
	 */
	public static boolean isMobile(){
		String agent = SessionUtil.getCurrentRequest().getHeader("User-Agent");
		return checkAgentIsMobile(agent);
	}
	
	/**
	 * 判断User-Agent 是不是来自于手机
	 * 
	 * @param ua
	 * @return
	 */
	public static boolean checkAgentIsMobile(String ua) {
		//SessionUtil.get
		boolean flag = false;
		if (!ua.contains("Windows NT") || (ua.contains("Windows NT") && ua.contains("compatible; MSIE 9.0;"))) {
			// 排除 苹果桌面系统
			if (!ua.contains("Windows NT") && !ua.contains("Macintosh")) {
				for (String item : MOBILE_AGENT) {
					if (ua.contains(item)) {
						flag = true;
						break;
					}
				}
			}
		}
		return flag;
	}

	
	/**
	 * windows 7 专用 获取MAC地址
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getMACAddress() throws Exception {

		// 获取本地IP对象
		InetAddress ia = InetAddress.getLocalHost();
		// 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();

		// 下面代码是把mac地址拼装成String
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			// mac[i] & 0xFF 是为了把byte转化为正整数
			String s = Integer.toHexString(mac[i] & 0xFF);
			sb.append(s.length() == 1 ? 0 + s : s);
		}

		// 把字符串所有小写字母改为大写成为正规的mac地址并返回
		return sb.toString().toUpperCase();
	}

	/**
	 * 
	 * 获取当前操作系统名称
	 * 
	 * @return 操作系统名称，例如windows xp，linux等。
	 */

	public static String getOSName() {

		return System.getProperty("os.name").toLowerCase();

	}

	/**
	 * 
	 * 获取当前操作系统的语言
	 * 
	 * @return 操作系统语言，例如zh（中文），en（英文）
	 */

	public static String getOSLanguage() {

		return System.getProperty("user.language");

	}

	/**
	 * 
	 * 获取当前操作系统的版本
	 * 
	 * @return 操作系统版本
	 */

	public static String getOSVersion() {

		return System.getProperty("os.version");

	}

	/**
	 * 
	 * 获取当前系统架构
	 * 
	 * @return 系统架构
	 */

	public static String getOSArch() {

		return System.getProperty("os.arch");

	}

	/**
	 * 获取当期的操作系统
	 * 
	 * @author 大雄
	 * @Title getOsName
	 * @Date 2015-9-6
	 * @Description TODO
	 * @return
	 * @Return String
	 */
	public static String getOsName() {
		String os = "";
		os = System.getProperty("os.name");
		return os;
	}

	public static boolean isLinux() {
		String os = getOsName();
		// System.out.println("======os======="+os);
		if (os.startsWith("Linux")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * 获取unix网卡的当前主机的MAC地址
	 * 
	 * 非windows的系统默认调用本方法，如果有特殊系统请继续扩充新的取得mac地址方法
	 * 
	 * @return mac地址
	 */

	public static String getUnixMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("ifconfig");
			bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				index = line.toLowerCase().indexOf("hwaddr");
				// index = line.toLowerCase().indexOf("硬件地址");
				if (index >= 0) {
					mac = line.substring(index + "hwaddr".length() + 1).trim();
					// mac = line.substring(index+"硬件地址".length()+1).trim();
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (process != null) {
				process.destroy();
			}
		}
		return mac;
	}

	/**
	 * 
	 * 获取windows系统下的网卡的mac地址
	 * 
	 * @return mac地址
	 */

	public static String getWindowsMACAddress() {
		String mac = "";
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("ipconfig /all");
			bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				index = line.toLowerCase().indexOf("physical address");
				if (index >= 0) {
					index = line.indexOf(":");
					if (index >= 0) {
						mac = line.substring(index + 1).trim();
					}
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if (process != null) {
				process.destroy();
			}
		}
		return mac;
	}

	/**
	 * 
	 * 获取本机主机名称
	 * 
	 * @return 本机主机名称
	 */

	public static String getHostName() {

		String hostname = "";

		InetAddress ia = null;

		try {

			ia = InetAddress.getLocalHost();

			if (ia == null) {

				hostname = "some error...";

			} else {

				hostname = ia.getHostName();

			}

		} catch (UnknownHostException e) {

			e.printStackTrace();

		}

		return hostname;

	}

	/**
	 * 
	 * 获取本机IP地址
	 * 
	 * @return 本机IP
	 */

	public static String getIPAddress() {

		String ipaddr = "";

		InetAddress ia = null;

		try {

			ia = InetAddress.getLocalHost();

			if (ia == null) {

				ipaddr = "some error...";

			} else {

				ipaddr = ia.getHostAddress();

			}

		} catch (UnknownHostException e) {

			e.printStackTrace();

		}

		return ipaddr;

	}

	public static String callCmd(String[] cmd) {
		String result = "";
		String line = "";
		try {
			Process proc = Runtime.getRuntime().exec(cmd);
			InputStreamReader is = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(is);
			while ((line = br.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @param cmd
	 *            第一个命令
	 * @param another
	 *            第二个命令
	 * @return 第二个命令的执行结果
	 */
	public static String callCmd(String[] cmd, String[] another) {
		String result = "";
		String line = "";
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(cmd);
			proc.waitFor(); // 已经执行完第一个命令，准备执行第二个命令
			proc = rt.exec(another);
			InputStreamReader is = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(is);
			while ((line = br.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @param ip
	 *            目标ip,一般在局域网内
	 * @param sourceString
	 *            命令处理的结果字符串
	 * @param macSeparator
	 *            mac分隔符号
	 * @return mac地址，用上面的分隔符号表示
	 */
	public static String filterMacAddress(final String ip, final String sourceString, final String macSeparator) {
		String result = "";
		String regExp = "((([0-9,A-F,a-f]{1,2}" + macSeparator + "){1,5})[0-9,A-F,a-f]{1,2})";
		Pattern pattern = Pattern.compile(regExp);
		Matcher matcher = pattern.matcher(sourceString);
		while (matcher.find()) {
			result = matcher.group(1);
			if (sourceString.indexOf(ip) <= sourceString.lastIndexOf(matcher.group(1))) {
				break; // 如果有多个IP,只匹配本IP对应的Mac.
			}
		}

		return result;
	}

	/**
	 * 
	 * @param ip
	 *            目标ip
	 * @return Mac Address
	 * 
	 */
	public static String getMacInWindows(final String ip) {
		String result = "";
		String[] cmd = { "cmd", "/c", "ping " + ip };
		String[] another = { "cmd", "/c", "arp -a" };

		String cmdResult = callCmd(cmd, another);
		result = filterMacAddress(ip, cmdResult, "-");

		return result;
	}

	/**
	 * @param ip
	 *            目标ip
	 * @return Mac Address
	 * 
	 */
	public static String getMacInLinux(final String ip) {
		String result = "";
		String[] cmd = { "/bin/sh", "-c", "ping " + ip + " -c 2 && arp -a" };
		String cmdResult = callCmd(cmd);
		result = filterMacAddress(ip, cmdResult, ":");

		return result;
	}

	/**
	 * 获取MAC地址
	 * 
	 * @return 返回MAC地址
	 */
	public static String getMacAddress(String ip) {
		String macAddress = "";
		macAddress = getMacInWindows(ip).trim();
		if (macAddress == null || "".equals(macAddress)) {
			macAddress = getMacInLinux(ip).trim();
		}
		return macAddress;
	}

	/**
	 * 获取客户端IP
	 * 
	 * @param request
	 * @return
	 */
	public static String getClientIpAddr(HttpServletRequest request) {
		if (request == null) {
			return "";
		}
		String ip = request.getHeader("x-forwarded-for");

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	

	/**
	 * 
	 * @author 大雄
	 * @Title getMotherboardSN
	 * @Date 2014-7-9
	 * @Description 主板序列号
	 * @Params @return
	 * @Return String
	 */
	public static synchronized String getMotherboardSN() {
		String result = "";
		try {
			File file = new File("realhowto.vbs");
			// File file = File.createTempFile("realhowto", ".vbs");
			file.deleteOnExit();
			FileWriter fw = new java.io.FileWriter(file);
			String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n" + "Set colItems = objWMIService.ExecQuery _ \n" + "   (\"Select * from Win32_BaseBoard\") \n" + "For Each objItem in colItems \n" + "    Wscript.Echo objItem.SerialNumber \n" + "    exit for  ' do the first cpu only! \n" + "Next \n";
			fw.write(vbs);
			fw.close();
			// System.out.println(file.getAbsolutePath());
			Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = input.readLine()) != null) {
				result += line;
			}
			input.close();
		} catch (Exception e) {
			System.out.println("-----------------");
			e.printStackTrace();
		}
		return result.trim();
	}

	/**
	 * 
	 * @author 大雄
	 * @Title getBasePath
	 * @Date 2015-2-5
	 * @Description 获取当前系统URL路径
	 * @Params @return
	 * @Return String
	 */
	public static String getBasePath() {
		HttpServletRequest request = SessionUtil.getCurrentRequest();
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		return basePath;
	}

	/**
	 * 
	 * @author 大雄
	 * @Title getBrowserName
	 * @Date 2015-12-18
	 * @Description TODO
	 * @return
	 * @Return String
	 */
	public static String getBrowserName() {

		String agent = SessionUtil.getCurrentRequest().getHeader("User-Agent").toLowerCase();
		if (agent.indexOf("msie 7") > 0) {
			return "ie7";
		} else if (agent.indexOf("msie 8") > 0) {
			return "ie8";
		} else if (agent.indexOf("msie 9") > 0) {
			return "ie9";
		} else if (agent.indexOf("msie 10") > 0) {
			return "ie10";
		} else if (agent.indexOf("msie") > 0) {
			return "ie";
		} else if (agent.indexOf("opera") > 0) {
			return "opera";
		} else if (agent.indexOf("opera") > 0) {
			return "opera";
		} else if (agent.indexOf("firefox") > 0) {
			return "firefox";
		} else if (agent.indexOf("webkit") > 0) {
			return "webkit";
		} else if (agent.indexOf("gecko") > 0 && agent.indexOf("rv:11") > 0) {
			return "ie11";
		} else {
			return "Others";
		}
	}
}
