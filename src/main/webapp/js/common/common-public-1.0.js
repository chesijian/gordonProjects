/**
 * @author 大雄 页面的通用js
 */

baseUrl = getBasePath();

/**
 * @author 大雄
 * 
 * 获取项目路径
 * 
 * @returns object
 */
function getBasePath() {
	// 获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
	var curWwwPath = window.document.location.href;
	// 获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
	var pathName = window.document.location.pathname;
	var pos = curWwwPath.indexOf(pathName);
	// 获取主机地址，如： http://localhost:8083
	var localhostPaht = curWwwPath.substring(0, pos);
	// 获取带"/"的项目名，如：/uimcardprj
	var projectName = pathName
			.substring(0, pathName.substr(1).indexOf('/') + 1);
	return (localhostPaht + projectName);
}
/**
 * @author 大雄 验证对象是否存在
 */
function isExtis(obj) {
	if (obj == null || typeof obj == "undefined") {
		return false;
	} else {
		return true;
	}
};
/**
 * 四舍五入数字，事例：zkjs=toFix((zkjs*100),2);
 * 
 * @param num
 * @param n
 * @returns
 */
function toFix(num,n){
	  // num = 0.007;//要四舍五入的数字
	  var fixNum = new Number(num+1).toFixed(n);// 四舍五入之前加1
	  var fixedNum = new Number(fixNum - 1).toFixed(n);// 四舍五入之后减1，再四舍五入一下
	  // alert(fixedNum);//弹出的数字就是正确的四舍五入结果啦
	  return fixedNum;
}
/**
 * @author 大雄
 * 
 * @requires 参数
 * 
 * 返回URl中的参数值
 * 
 * @returns string
 */
function getUrlParam(name) {
	var reg = new RegExp("(^|\\?|&)" + name + "=([^&]*)(\\s|&|$)", "i");
	if (reg.test(location.href))
		return unescape(RegExp.$2.replace(/\+/g, " "));
	return "";
};

/**
 * @author 大雄 调试
 */
function log(obj) {
	console.info(obj);
};


function mask(msg){
	if(validUtil.isNull(msg)){
		msg="正在加载中...";
	}
	var loading = '<div id="div_loading">'+
		'<div id="dov_loading_mask" class="css_loading_mask"></div>'+
		'<div class="css_loading">'+
		    '<div class="css_loading_indicator">'+
		       '<div  class="css_loading_content"><img width="30" height="30" style="float: left;" src="'+baseUrl+'/img/loading.gif"></img><span><font style="font-size:20px;">'+msg+'</font></span></div>'+
		    '</div>'+
		'</div>'+
		'</div>';
	$("body").prepend(loading);
}

function unmask(){
	var loading = $("#div_loading");
	
	if(!validUtil.isNull(loading)){
		loading.remove();
	}
}
/**
 * 创建模态窗口
 * @param url
 * @param width
 * @param height
 * @param title
 */
function createWin(url,width,height,title){
	if(typeof _win != "undefined" && _win != null){
		_win.close();
		_win = null;
	}
	var contentStr='<iframe id="_win_frame" src="'+url+'" style="width: 100%;height: 100%;border:0"></iframe>';
	_win = dialog({
	    title: title,
	    width: width,  
	    height:height,   
	    content:contentStr,
	    padding: 0
	});
	_win.show();
}

function createDivWin(contentStr,width,height,title){
	if(typeof _win != "undefined" && _win != null){
		_win.close();
		_win = null;
	}
	_win = dialog({
	    title: title,
	    width: width,  
	    height:height,   
	    content:contentStr,
	    padding: 0
	});
	_win.show();
}

function closeWin(){
	if(typeof _win != "undefined" && _win != null){
		_win.close();
		_win = null;
	}
}

/**
 * @author 大雄
 */
function ValidUtil() {
	/*
	 * 用途：检查输入字符串是否为空或者全部都是空格 输入：str 返回： 如果全是空返回true,否则返回false
	 */
	this.isNull = function(str) {
		if(typeof str == "undefined")
			return true;
		if(str == null){
			return true;
		}
		if (str == "")
			return true;
		var regu = "^[ ]+$";
		var re = new RegExp(regu);
		return re.test(str);
	};

	this.isEmail = function(strEmail) {
		if (strEmail
				.search(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) != -1)
			return true;
		else
			return false;
	};

	this.isEnglish = function(name) // 英文值检测
	{
		if (isNull(name))
			return false;
		var re = /[^a-zA-Z]/g;
		return !re.test(name);
	};

	this.isChinese = function(name) // 中文值检测
	{
		if (isNull(name))
			return false;
		var re = /[^\u4E00-\u9FA5]/g;
		return !re.test(name);
	};

	/*
	 * 用途：校验ip地址的格式 输入：strIP：ip地址 返回：如果通过验证返回true,否则返回false；
	 * 
	 */
	this.isIP = function(strIP) {
		if (isNull(strIP))
			return false;
		var re = /^(\d+)\.(\d+)\.(\d+)\.(\d+)$/g;
		if (re.test(strIP)) {
			if (RegExp.$1 < 256 && RegExp.$2 < 256 && RegExp.$3 < 256
					&& RegExp.$4 < 256)
				return true;
		}
		return false;
	};

	/*
	 * 用途：检查输入对象的值是否符合整数格式 输入：str 输入的字符串 返回：如果通过验证返回true,否则返回false
	 * 
	 */
	this.isInteger = function(str) {
		var regu = /^[-]{0,1}[0-9]{1,}$/;
		return regu.test(str);
	};

	/**
	 * js验证是否是数字（包括正整数,０,负整数,小数) 大雄 2015-1-26
	 * 
	 * @param obj
	 * @returns {Boolean}
	 */
	this.isNumber = function(value) {
		// alert(obj);
		var re = /^-?[1-9]+(\.\d+)?$|^-?0(\.\d+)?$|^-?[1-9]+[0-9]*(\.\d+)?$/;
		if (!re.test(value)) {
			return false;
		} else {
			return true;
		}
	};

	/*
	 * 用途：检查输入手机号码是否正确 输入： s：字符串 返回： 如果通过验证返回true,否则返回false
	 * 
	 */
	this.isMobile = function(s) {
		var myreg = /^(13|15|18|17)[0-9]{9}$/;
		if (s == "") {
			return false;
		} else if (s.length != 11) {
			return false;

		} else if (!myreg.test(s)) {
			return false;

		}
		return true;
	};

	/*
	 * 用途：检查输入字符串是否是带小数的数字格式,可以是负数 输入： s：字符串 返回： 如果通过验证返回true,否则返回false
	 * 
	 */
	this.isDecimal = function(oNum) {
		if (!oNum)
			return false;
		var strP = /^\d+(\.\d+)?$/;
		if (!strP.test(oNum))
			return false;
		try {
			if (parseFloat(oNum) != oNum)
				return false;
		} catch (ex) {
			return false;
		}
		return true;
	};

	/*
	 * 用途：检查输入字符串是否只由英文字母和数字和下划线组成 输入： s：字符串 返回： 如果通过验证返回true,否则返回false
	 * 
	 */
	this.isNumberOr_Letter = function(s) { // 判断是否是数字或字母

		var regu = "^[0-9a-zA-Z\_]+$";
		var re = new RegExp(regu);
		if (re.test(s)) {
			return true;
		} else {
			return false;
		}
	};
	/*
	 * 用途：检查输入字符串是否只由英文字母和数字组成 输入： s：字符串 返回： 如果通过验证返回true,否则返回false
	 * 
	 */
	this.isNumberOrLetter = function(s) { // 判断是否是数字或字母

		var regu = "^[0-9a-zA-Z]+$";
		var re = new RegExp(regu);
		if (re.test(s)) {
			return true;
		} else {
			return false;
		}
	};
	/*
	 * 用途：检查输入字符串是否只由汉字、字母、数字组成 输入： value：字符串 返回： 如果通过验证返回true,否则返回false
	 * 
	 */
	this.isChinaOrNumbOrLett = function(s) { // 判断是否是汉字、字母、数字组成

		var regu = "^[0-9a-zA-Z\u4e00-\u9fa5]+$";
		var re = new RegExp(regu);
		if (re.test(s)) {
			return true;
		} else {
			return false;
		}
	};

	/*
	 * 用途：判断是否是日期 输入：date：日期；fmt：日期格式 返回：如果通过验证返回true,否则返回false
	 */
	this.isDate = function(date, fmt) {
		if (fmt == null)
			fmt = "yyyyMMdd";
		var yIndex = fmt.indexOf("yyyy");
		if (yIndex == -1)
			return false;
		var year = date.substring(yIndex, yIndex + 4);
		var mIndex = fmt.indexOf("MM");
		if (mIndex == -1)
			return false;
		var month = date.substring(mIndex, mIndex + 2);
		var dIndex = fmt.indexOf("dd");
		if (dIndex == -1)
			return false;
		var day = date.substring(dIndex, dIndex + 2);
		if (!isNumber(year) || year > "2100" || year < "1900")
			return false;
		if (!isNumber(month) || month > "12" || month < "01")
			return false;
		if (day > getMaxDay(year, month) || day < "01")
			return false;
		return true;
	};
}
var validUtil = new ValidUtil();