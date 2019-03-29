//输入查询的名称，返回所要的url地址中的数据。
var request = function(paras){ 	 
	var url = location.href;  
	var paraString = url.substring(url.indexOf("?")+1,url.length).split("&");  
	var paraObj = {}  
	for (i=0; j=paraString[i]; i++){  
		paraObj[j.substring(0,j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=")+1,j.length);  
	}  
	var returnValue = paraObj[paras.toLowerCase()];  
	if(typeof(returnValue)=="undefined"){  
		return "";  
	}else{  
		return returnValue == "null"?"":returnValue;
	}  
} 
	
//打印对象中所有的公共属性
var printMap=function(data){
	var _ ="";
	for(var key in data){
		_ += key+':'+data[key]+'\n\r';
	}
	alert(_)
}

//将Timestamp转换成Y-m-d格式
var TimestampToYmd= function(v){
	if(v == null || v == "")return "";
	var _1 =new Date(v);
	if(isNaN(_1)){
		return v;
	}
	return _1.getFullYear()+'-'+(_1.getMonth()+1)+'-'+_1.getDate();
}
//将日期型转换成 Y-m-d h:M;s 格式
var dateFormatter = function(_1){
	var _2 = new Date();
	return _1.getFullYear()+'-'+(_1.getMonth()+1)+'-'+_1.getDate()+' '+_2.getHours()+':'+_2.getMinutes()+':'+_2.getSeconds();
}

//删除Json中指定的属性
var deleteJsonAttr = function(_1,_2){
	for(var i=0;i<_2.length;i++) delete _1[_2[i]];
}

//jQuery自定义插件
$.extend({
	//这是一个公共的ajax post提交方式
	ajaxPost:function(url,param,successFun) {
		$.ajax({
			type: "POST",
			url:url,
			data : $.toJSON(param),
			dataType:"json" ,
			contentType : "application/json",
			success: successFun,
			error : this.ajaxError
		});
	},
	//这是一个公共的Ajax erro信息回调,如果使用个性化的信息请在具体函数中修改.
	ajaxError: function(XMLHttpRequest, textStatus, errorThrown) { 
		alert("Ajax请求失败\n\r**********************方法参数**********************"+
		  "\n\rurl:\t"+this.url+
		  "\n\rdata:\t"+this.data+
		  "\n\rtype:\t"+this.type+
		  "\n\r**********************Ajax信息**********************"+
		  "\n\rXMLHttpRequest.statusText:\t"+XMLHttpRequest.statusText+
		  "\n\rtextStatus:\t"+textStatus+
		  "\n\rerrorThrown:\t"+errorThrown)
	},
	//公共的客户选择插件....三脚架插件........
	queryClient:function(winId,btnId,gridId,showId,valueId,callback){		
		$("#"+btnId).keyup(function(){
			var name = $(this).val()
			if(name.length>0){
				$.get("../../getClientListName.json",{clientName:name},function(data){
					$('#'+gridId).datagrid('loadData',tirmData(data));
				});
			}
		});
			
		$('#'+winId).dialog({title:'选择客户',width:610,height:300});
		$('#'+winId).dialog('close');
		$("#"+showId).click(function(){
			$('#'+winId).dialog('open');
			$('#'+gridId).datagrid({
				width:580,
				loadMsg : '查询中....',
				height:220,
				columns:[[
					{field:'clientName',title:'姓名',width:80},
					{field:'clientSex',title:'性别',width:40},
					{field:'positionCode',title:'职务或职称',width:80},
					{field:'clientIdentityCode',title:'身份证号',width:100},
					{field:'unitCode',title:'工作单位',width:80},
					{field:'clientRemarks',title:'备注',width:120},
					{field:'clientId',title:'标识',width:40}
				]],
				onDblClickRow : function(rowIndex,rowData){
					$("#"+showId).val(rowData.clientName);
					$("#"+valueId).val(rowData.clientId);
					callback(rowIndex,rowData);
					$('#'+winId).dialog('close');
				}
			});
		});
	}
});
// 创建一个闭包     
(function($) {
	$.fn.daTunClientQuery = function(options){// 插件的定义-大屯用户选择插件
		var obj = $(this);
		var opts = $.extend({}, $.fn.daTunClientQuery.defaults, options);     
		obj.combogrid(opts);
		obj.combogrid('disable');
		$.get("../../getAllUsers.json",function(json){
			obj.combogrid('grid').datagrid('loadData',tirmJson(json));
			obj.combogrid('enable');
			$.get("../../getUserBean.json",function(data){
				obj.combogrid('setValue', data.id);
			});
		});
		
	};   
	$.fn.daTunClientQuery.defaults = {// 插件的defaults-大屯用户选择插件的defaults
		panelWidth:150,
		idField:'id',
		textField:'name',
		columns:[[
			{field:'name',title:'姓名',width:120}
		]]    
	}; 
	
	$.fn.daTunProjectQuery = function(options){// 插件的定义-大屯区域选择插件
		var obj = $(this);
		var opts = $.extend({}, $.fn.daTunProjectQuery.defaults, options);     
		obj.combogrid(opts);
		obj.combogrid('disable');
		$.get("../../getProjectList.json",function(json){
			obj.combogrid('grid').datagrid('loadData',tirmJson(json));
			obj.combogrid('enable');
		});
	}; 
	$.fn.daTunProjectQuery.defaults = {// 插件的defaults-大屯区域选择插件的defaults
		panelWidth:200,
		idField:'projectCode',
		textField:'projectName',
		columns:[[
			{field:'projectName',title:'名称',width:80},
			{field:'projectCode',title:'编号',width:80}
		]],
		onSelect : function(record){
			
			$('#areaCode').combogrid('clear');
			$('#buildingCode').combogrid('clear');
			var cord = $('#projectCode').combogrid('getValue');
			$.get("../../getAreaListSelect.json",{project_code:cord},function(json){
				var data = tirmData(json);
				$('#areaCode').combogrid('grid').datagrid('loadData',data);
			});
		}
	};
	
	$.fn.daTunAreaQuery = function(options){// 插件的定义-大屯小区选择插件
		var obj = $(this);
		var opts = $.extend({}, $.fn.daTunAreaQuery.defaults, options);     
		obj.combogrid(opts);
	}; 
	$.fn.daTunAreaQuery.defaults = {// 插件的defaults-大屯小区选择插件的defaults
		panelWidth:200,
		idField:'areaCode',
		textField:'areaName',
		columns:[[
			{field:'areaName',title:'名称',width:80},
			{field:'areaCode',title:'编号',width:80}
		]],
		onSelect : function(record){
			var cord = $('#projectCode').combogrid('getValue');
			var cord2 = $('#areaCode').combogrid('getValue');
			$('#buildingCode').combogrid('clear');
			$.ajaxPost("../../getBuildingListSelect.json",{projectCode:cord,areaCode:cord2},function(json){
				var data = tirmData(json);
				$('#buildingCode').combogrid('grid').datagrid('loadData',data);
			});
		}
	};
	
	$.fn.daTunBuildingQuery = function(options){// 插件的定义-大屯大楼选择插件
		var obj = $(this);
		var opts = $.extend({}, $.fn.daTunBuildingQuery.defaults, options);     
		obj.combogrid(opts);
	}; 
	$.fn.daTunBuildingQuery.defaults = {// 插件的defaults-大屯大楼选择插件的defaults
		panelWidth:200,
		idField:'buildingCode',
		textField:'buildingName',
		columns:[[
			{title:"楼号",width:80,field:'buildingName'},
			{title:"大楼编码",width:80,field:'buildingCode'}/*,
			{title:"业态类型",width:75,field:'buildingBusinessType'},
			{title:"小区名称",width:75,field:'areaName'},
			{title:"区域名称",width:75,field:'projectName'}*/
		]]
	};
	
	//银行帐号combox定义//////////////////////////////////////////////////////////////////////////
	var comJson = [{value:"中国建设银行",bankname:"中国建设银行"},{value:"中国工商银行",bankname:"中国工商银行"}];
	
	$.fn.daTunEnterBankCombo = function(options){// 插件的定义-大屯区域选择插件
		var obj = $(this);
		var opts = $.extend({}, $.fn.daTunEnterBankCombo.defaults, options);     
		obj.combobox(opts);
		obj.combobox("loadData",comJson);
	}; 
	$.fn.daTunEnterBankCombo.defaults = {// 插件的defaults-大屯区域选择插件的defaults
	    idFied:"value",
		valueFied:'value',
		textField:'bankname',
		onSelect : function(bankinfo){
			if(bankinfo.bankname=='中国建设银行')
			{
				$('#bankinfo').html('银行帐号:32001717242050749215');
				$('#enterAccount').val("32001717242050749215");
			}
			else
			{
			 	$('#bankinfo').html('银行帐号:1106029519300041879');
			    $('#enterAccount').val("1106029519300041879");
		    }
		}
	};
	
	$.fn.daTunOutBankCombo = function(options){// 插件的定义-大屯区域选择插件
		var obj = $(this);
		var opts = $.extend({}, $.fn.daTunOutBankCombo.defaults, options);     
		obj.combobox(opts);
		obj.combobox("loadData",comJson);
	}; 
	$.fn.daTunOutBankCombo.defaults = {// 插件的defaults-大屯区域选择插件的defaults
		idFied:'value',
		valueFied:'value',
		textField:'bankname',
		onSelect : function(bankinfo){
			if(bankinfo.bankname=='中国建设银行')
			{
				$('#bankinfo').html('银行帐号:32001717242050749215');
			}
			else
			{
			 	$('#bankinfo').html('银行帐号:1106029519300041879');
		    }
		}
	};
	
	
	// 私有函数-将List转换成easyui grid需要的数据结构
	function tirmJson(_) {     
		if(_ == null)return {"total":0,"rows":[]};
		var _1 = {"total":_.length,"rows":[]};
		$.each(_,function(i,n){
			_1["rows"].push(n);
		});
		return _1;    
	};
})(jQuery);// 闭包结束     
 
//按照easyui 规定的grid数据格式整理数据
var tirmData = function(data){
	if(data == null){
		return {"total":0,"rows":[]};
	}
	var temp = {"total":data.length,"rows":[]};
	$.each(data,function(i,n){
		temp["rows"].push(n);
	});
	return temp;
}

//获取封装成bean对象的 Form 值
var getFromValues = function(id){
	var _json = "{",_falg = true;
	$.each($("#"+id).serializeArray(),function(i,n){
		if(_falg){
			_json += "\""+n.name +"\":\""+ n.value+"\"";
			_falg = false;
		}else{
			_json += ",\""+ n.name +"\":\""+ n.value+"\"";
		}
	});
	_json += "}";
	return $.evalJSON(_json);
}

var borderColor =  function (num){
	switch(num){
		case "1" ://销售进行中
			return "#FF6666";
		case "2" ://未售
			return "#FFFFFF";
		case "3" ://出租
			return "#ABDC65";
		case "4" ://保留
			return "#B3BED1";
		case "5" ://已售
			return "#2D7CFF";
		default : return "#FFFFFF";
	}
}

var returnHouseMarketMode= function(v){
	switch(v){
		case "1" :return "销售进行中";
		case "2" :return "未售"; 
		case "3" :return "出租"; 
		case "4" :return "保留"; 
		case "5" :return "已售"; 
		default : return v;
	}
}

//绘制楼盘表
var drawTable = function(array,divId){
	var outtable='<table border="0" cellpadding="0" cellspacing="0"><tr>';
	$.each(array,function(i,danYuanHao){
				
		var rooms=danYuanHao["louPanTu1"];tr=danYuanHao["floorNum"],td=danYuanHao["roomNum"];
		var table = ['<td border="0" cellpadding="0" cellspacing="0"><table border="1" cellspacing="0"> ', //style="float:left"
					'<thead>',
					  '<tr>',
						'<th colspan="'+td+'"><label title="单元号">'+danYuanHao.danYuanhHao+'单元</label></th>',
					  '</tr>',
					'</thead>',
					'<tbody>'];
		
		for(var i=0;i<tr;i++){
			table.push('<tr>');
			room = rooms[i]["houseParameter"];
			for(var j=0;j<td;j++){
				if(room[j] == null){
					table.push('<td>&nbsp;</td>');
				}else{
					table.push('<td width="50px" houseId="'+room[j].houseId+'" marketId="'+room[j].marketId+'" houseMarketMode="'+room[j].houseMarketMode+'" align="center" bgcolor=');
					table.push(borderColor(room[j].houseMarketMode));
					table.push('><label title="');
					table.push(returnHouseMarketMode(room[j].houseMarketMode));
					table.push('">');
					table.push(room[j].houseCode);
					table.push('</label></td>');
				}
			}
			table.push('</tr>');
		}
	//	table.push('</tbody></table><br/>');
		table.push('</tbody></table></td>');
		outtable=outtable+table.join('');
		
	//	$("#"+divId).append(table.join('')); //将表格填充至指定的位置
	});
	    outtable=outtable+"</tr></table>";
		
		
		//console.log(outtable);
		$("#"+divId).append(outtable); //将表格填充至指定的位置
}

//开打方法
var windowShow = function(){
	var fillHeight = parseInt(arguments[1]) +60;
	var fillWidth = parseInt(arguments[2])+8;
	var winoption='dialogHeight:'+fillHeight+'px;dialogWidth:'+fillWidth+'px;status:no;'
	if(arguments.length == 3){
		window.showModalDialog(arguments[0],window,winoption);
	}else if(arguments.length == 4){
		window.showModalDialog(arguments[0],arguments[3],winoption);
	}
	//window.open(arguments[0],"","height="+arguments[1]+",width="+arguments[2]+",toolbar= no,menubar=no,scrollbars=no,resizable=no,location=no,status=no,top=0,left=0");
}
//维护其他产权证号
function otherHouseCertificateButton(){
	var json = getFromValues("MarketHeadCertificateForm");
	windowShow("../SaleRoom/otherHouseCertificate.html?record="+escape($.toJSON(json)),250,450);
}
//产权办理确定事件
function propertyTransactButton(){
	var json = getFromValues("MarketHeadCertificateForm");
	$.ajaxPost("../../updateMarketHeadCertificate.json",json,function(){
		//try{_refurbishCourseTable();}catch(e){}
		$('#w').window('close');
	});
}
//入住通知
function RuZhuTongZhi(){
	var json = getFromValues("RuZhuTongZhiForm");
	$.ajaxPost("../../updateMarketHeadRZTZ.json",json,function(){//根据入住通知修改销售流程头表
		json.flowName = "入住通知";
		$.ajaxPost("../../updateMarketFlowComplete.json",json,function(){
		   $('#RuZhuTongZhiDiv').window('close')
		   $.messager.alert("提示","您已经办妥入住通知");
			_refurbishCourseTable();//刷新流程表
		});
	});
}
//办理房屋产权证协议书
function chanQuanZheng(){
	open("../../ireport/blfwcqzxys.jsp?id="+_houseId);
	//open("test.jsp?id=1111111");
}
var beginSubscribeSwitch = function(rowIndex,rowData){
	var flowCode = rowData.flowCode;
	var flowName = rowData.flowName;
	switch(flowName){
		case '审核' ://审核
			$('#MarketExamineListDivDIV').dialog('open');
			$('#MarketExamineListYDiv').datagrid({
				fit : 'true',
				singleSelect : true,
				columns:[[
					{field:'marketExamineDate',title:'审核日期',width:70,formatter:TimestampToYmd},
					{field:'marketExamineExamineContent',title:'审核内容',width:210},
					{field:'marketExamineState',title:'审核状态',width:70,formatter:function(v){
						switch(v){
							case "1" : return "资格审核";
							case "2" : return "一级审核";
							case "3" : return "二级审核";
							case "4" : return "三级审核";
							default : return v;
						}
					}},
					{field:'marketExamineFlag',title:'审核标识',width:70,formatter:function(v){
						switch(v){
							case "1" : return "未审核";
							case "2" : return "已审核通过";
							case "3" : return "已审核未通过";
							default : return v;
						}
					}},
					{field:'marketExamineUsersName',title:'审核人',width:50}
				]]
			});
			if(_marketId != ""){
				$.ajaxPost("../../getMarketExamineListY.json",{
					marketId : _marketId,
					houseId : _houseId
				},function(json){
					var data = tirmData(json);
					$('#MarketExamineListYDiv').datagrid('loadData',data);
				});
			}
			
		break;
		case '收取定金' ://收取定金
			windowShow("../SaleRoom/financeManager.html?financeFlag=1&flowName="+escape(flowName)+"&record="+escape($.toJSON(rowData)),450,650);
			//open("financeManager.html?financeFlag=1&flowName="+escape(flowName)+"&record="+escape($.toJSON(rowData)));
		break;
		case '签订认购书' ://签订认购书
			windowShow("../SaleRoom/SubscribeInfo.html?record="+escape($.toJSON(rowData)),190,450);
			//open("SubscribeInfo.html?record="+escape($.toJSON(rowData)));
		break;
	
		case '签订购房合同' ://签订购房合同
			windowShow("../SaleRoom/ContractInfo.html?record="+escape($.toJSON(rowData)),280,660);
			//open("ContractInfo.html?record="+escape($.toJSON(rowData)));
		break;
		case '收取首付款' ://收取首付款
			windowShow("../SaleRoom/financeManager.html?financeFlag=2&flowName="+escape(flowName)+"&record="+escape($.toJSON(rowData)),450,650);
			//open("financeManager.html?financeFlag=2&flowName="+escape(flowName)+"&record="+escape($.toJSON(rowData)));
		break;
		case '办妥银行按揭手续' ://办妥银行按揭手续
			if(rowData.flowEndFlag == "2"){
				$.messager.alert("提示","您已经办妥银行按揭手续");
			}else{
				$.messager.confirm('提示', '您确认办妥银行按揭手续了吗?', function(r){
					if(r){
						$.ajaxPost("../../updateMarketFlowComplete.json",{
							marketId:rowData.marketId,//销售头表ID
							houseId:rowData.houseId,//房产ID
							flowName:rowData.flowName,//步骤名称
							flowCode:rowData.flowCode//步骤编号
						},function(){
							_refurbishCourseTable();//刷新流程表
						});
					}
				});
			}
		break;
		case '合同备案' ://合同备案
			if(rowData.flowEndFlag == "2"){
				$.messager.alert("提示","您已经办妥合同备案");
			}else{
				$.messager.confirm('提示', '您确认办妥合同备案了吗?', function(r){
					if(r){
						$.ajaxPost("../../updateMarketFlowComplete.json",{
							marketId:rowData.marketId,//销售头表ID
							houseId:rowData.houseId,//房产ID
							flowName:rowData.flowName,//步骤名称
							flowCode:rowData.flowCode//步骤编号
						},function(){
							_refurbishCourseTable();//刷新流程表
						});
					}
				});
			}
		break;
		case '按揭到帐'://按揭到帐
			windowShow("../SaleRoom/financeManager.html?financeFlag=3&flowName="+escape(flowName)+"&record="+escape($.toJSON(rowData)),450,650);
			//open("financeManager.html?financeFlag=3&flowName="+escape(flowName)+"&record="+escape($.toJSON(rowData)));
		break;
		case '入住通知' ://入住通知
			if(rowData.flowEndFlag == "2"){
				$.messager.confirm('提示', '您已经办妥入住通知.<br/>是否打印职工购房房票?', function(r){
					if(r){
						open("../ePrint/fangPiao.html?houseId="+rowData.houseId);
					}
				});
			}else{
				$('#RuZhuTongZhiForm').form('load',rowData);
				$('#RuZhuTongZhiDiv').window('open');
			}
		break;
		case '交房' ://交房
			if(rowData.flowEndFlag == "2"){
				$.messager.alert("提示","您已经办妥交房");
			}else{
				$.messager.confirm('提示', '您确认办妥交房了吗?', function(r){
					if(r){
						$.ajaxPost("../../updateMarketFlowComplete.json",{
							marketId:rowData.marketId,//销售头表ID
							houseId:rowData.houseId,//房产ID
							flowName:rowData.flowName,//步骤名称
							flowCode:rowData.flowCode//步骤编号
						},function(){
							_refurbishCourseTable();//刷新流程表
						});
					}
				});
			}
		break;
		case '面积补差' : //面积补差
			windowShow("../SaleRoom/MianJiFinanceManager.html?financeFlag=6&flowName="+escape(flowName)+"&record="+escape($.toJSON(rowData)),650,650);
			//open("MianJiFinanceManager.html?financeFlag=6&flowName="+escape(flowName)+"&record="+escape($.toJSON(rowData)));
		break;
		case '交纳维修基金':
			windowShow("../SaleRoom/UpkeepManager.html?financeFlag=8&flowName="+escape(flowName)+"&record="+escape($.toJSON(rowData)),150,350);
			//open("financeManager.html?financeFlag=8&flowName="+escape(flowName)+"&record="+escape($.toJSON(rowData)));
		break;
		case '产权办理' : //产权办理
			$.ajaxPost("../../getMarketHead.json",{
				marketId:rowData.marketId,//销售头表ID
				houseId:rowData.houseId//房产ID
			},function(data){
				$('#MarketHeadCertificateForm').form('load',rowData);
				if(data.marketEndDate != ""){
					$("#MarketHeadCertificateForm input[name='marketEndDate']").val(data.marketEndDate);
				}
				if(data.marketEndDate != ""){
					$("#MarketHeadCertificateForm input[name='certificateCode']").val(data.certificateCode);
				}
				if(data.marketEndDate != ""){
					$("#MarketHeadCertificateForm input[name='landCode']").val(data.landCode);
				}
				$('#w').window('open');
			});
		break;
		
		case '房款缴纳' : //收取一次性全款
			windowShow("../SaleRoom/financeManager.html?financeFlag=4&flowName="+escape(flowName)+"&record="+escape($.toJSON(rowData)),450,650);
		break;
		
		case '签订出租合同':
			windowShow("../SaleRoom/RentContractInfo.html?record="+escape($.toJSON(rowData)),220,660);
		break;
		case '退房':
			$.messager.confirm('提示', '您确认退房了吗?', function(r){
				if(r){
					$.ajaxPost("../../updateTrunHouse.json",{
						houseId:rowData.houseId,//房产ID
						marketId:rowData.marketId,//销售头表ID
						flowCode:rowData.flowCode//步骤名称
					},function(){
						_refurbishCourseTable();//刷新流程表
					});
				}
			});
		break;
	}
}
//转换产权性质
var toChanQuanXingZhi = function(v){
	switch(v){
		case "1" : return "部分产权"; break;
		case "2" : return "完全产权"; break;
		case "3" : return "房改房"; break;
		case "4" : return "经济适用房"; break;
		case "5" : return "商品房"; break;
		
	}
}