package com.state.util;
/**
 * 特殊字符过滤
 * @description
 * @author 大雄
 * @date 2016年8月18日上午10:44:24
 */
public class CharFilterUtil {
	//sql危险字符
	public static String[] BAD_STR_ARR = { "mssql", "sqlmap.py", "hex", "A%22+or+isnull%281%2F0%29+%2F*", "isnull", "!", "'","&", "{", "}", "[", "]", "$", "#", "^", "insert", "select", "delete", "update", "count", "drop", "*", "chr", "mid", "master", "truncate", "char", "declare", "sitename", "net user", "xp_cmdshell", "like", "and", "exec", "execute", "create", "drop", "table", "from", "grant", "use", "group_concat", "column_name", "information_schema.columns", "table_schema", "union", "where", "order", "by", " ", "|", "or", ";", "--", "+", "\"", "<", ">", "(", ")", ",", "like", "//", "/", "%", "#", "?", "\n", "\r", "%27", "%22", "%281", "%2F0", "%2F0", "%29", "%3E", "%3C", "%3D", "%2F","script", "<script>", "</script>", "/", "\\", "../", "\\.." };
	
	/**
	 * 返回字符串中是否有危险字符
	 * @description
	 * @author 大雄
	 * @date 2016年8月18日上午10:44:44
	 * @param str
	 * @return
	 */
	public static synchronized boolean ifHasBadChar(String str) {
		str = str.toLowerCase();
		synchronized (str) {
			for (String temp : BAD_STR_ARR) {
				if (str.indexOf(temp) > -1) {
					return true;
				}
			}
			return false;
		}

	}
}
