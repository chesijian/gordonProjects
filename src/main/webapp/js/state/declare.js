/**
 * 对cookie操作的代码
 */
Cookies = {};
Cookies.set = function(name, value){
     var argv = arguments;
     var argc = arguments.length;
     var expires = (argc > 2) ? argv[2] : null;
     var path = (argc > 3) ? argv[3] : '/';
     var domain = (argc > 4) ? argv[4] : null;
     var secure = (argc > 5) ? argv[5] : false;
     document.cookie = name + "=" + escape (value) +
       ((expires == null) ? "" : ("; expires=" + expires.toGMTString())) +
       ((path == null) ? "" : ("; path=" + path)) +
       ((domain == null) ? "" : ("; domain=" + domain)) +
       ((secure == true) ? "; secure" : "");
};

Cookies.get = function(name){
	var arg = name + "=";
	var alen = arg.length;
	var clen = document.cookie.length;
	var i = 0;
	var j = 0;
	while(i < clen){
		j = i + alen;
		if (document.cookie.substring(i, j) == arg)
			return Cookies.getCookieVal(j);
		i = document.cookie.indexOf(" ", i) + 1;
		if(i == 0)
			break;
	}
	return null;
};

function Declare() {
	var myDeclare = this;
	var changed = true;
	var unchanged = false;
	this.timeType;
	this.areaList;
	this.selectedDalare;
	this.dalareType;
	//设置选中的单个节点
	this.selectSingleNode;
	//导入数据
	this.importData = function(){
		var contentStr = '';
		contentStr+='<div id="_attachment_fujian" name="_attachment_fujian"> '+
	     ' <iframe id="_attachment_iframe_fujian" name="_attachment_iframe_fujian" frameborder="0" height="22px" width="160px" src="../attachment/init?xtype=fujian" scrolling="no"></iframe>'+ 
	      '<div id="_attachment_filelist_fujian" style="padding-left: 15px;" name="_attachment_filelist_fujian"></div> '+
	      '<div id="_attachment_loading_fujian" style="display:none"> '+
	       '<img width="30" height="30" src="../img/loading.gif" /> '+
	      ' <span><font style="font-family:微软雅黑">正在上传...</font></span> '+
	      '</div> '+
	     '</div>';
		createDivWin(contentStr,300,150,"导入申报单");
	};
	//加载右侧单个单据详细96值和图表
	this.loadDeclareSheet = function(id){
		if(typeof document.getElementsByTagName("iframe")["_frame"].contentWindow.initData != "undefined"){
			document.getElementsByTagName("iframe")["_frame"].contentWindow.initData(id);
		}
	};
	//设置保存96值的按钮和选中的数据的id
	this.setSave96Btn = function(checkNodes){
		//log("==="+checkNodes.length);
		//log(document.getElementsByTagName("iframe")["_frame"].contentWindow.initData);
		if(checkNodes == null || checkNodes.length == 0){
			this.loadDeclareSheet('');
			return;
		}
		
		if(checkNodes.length == 1){
			this.selectSingleNode = checkNodes[0];
			if(this.selectSingleNode['attributes']['allowEdit'] == true){
				this.set96InputDisabled(false);
				$('#btn_save96Data').show();
				
			}else{
				this.set96InputDisabled(true);
				
			}
			//alert(this.selectSingleNode);
			//显示新报表
			//document.getElementById("_frame").src = baseUrl+"/declare/chart?id="+this.selectSingleNode['id'];
			//document.getElementsByTagName("iframe")["_frame"].contentWindow.initData(this.selectSingleNode['id']);
			this.loadDeclareSheet(this.selectSingleNode['id']);
			//alert(this.selectSingleNode['attributes']['allowEdit']+"ok"+this.selectSingleNode['id']);
		}else{
			var singleNum = 0;
			var index = -1;
			for(var i = 0;i<checkNodes.length;i++){
				if(checkNodes[i]['id'].indexOf("-")<=-1){
					index = i;
					singleNum++;
				}
			}
			if(singleNum == 1){
				this.selectSingleNode = checkNodes[index];
				//log(this.selectSingleNode);
				if(this.selectSingleNode['attributes']['allowEdit'] == true){
					this.set96InputDisabled(false);
					$('#btn_save96Data').show();
					//alert("11");
					}else{
					this.set96InputDisabled(true);
				}
				//显示新报表
				//document.getElementById("_frame").src = baseUrl+"/declare/chart?id="+this.selectSingleNode['id'];
				///document.getElementsByTagName("iframe")["_frame"].contentWindow.initData(this.selectSingleNode['id']);
				this.loadDeclareSheet(this.selectSingleNode['id']);
			}else{
				this.selectSingleNode = null;
				$('#btn_save96Data').hide();
				this.set96InputDisabled(true);
			}
			
		}
	};
	//设置96值是否可编辑
	this.set96InputDisabled = function(flag){
		for(var i = 1;i<97;i++){
			keyStr = (100+i)+"";
			keyStr = keyStr.substring(1, keyStr.length);
			key = "h"+keyStr;
			$('#declareDataDiv input[name=' + key + ']').attr('disabled', flag);
		}
	};
	//保存单条数据的96值
	this.save96Data = function(){
		if(this.selectSingleNode == null){
			alert("请选择一条数据");
			return;
		}
		var key;
		var keyStr;
		var value;
		var data = {};
		for(var i = 1;i<97;i++){
			keyStr = (100+i)+"";
			keyStr = keyStr.substring(1, keyStr.length);
			key = "h"+keyStr;
			value = $('#declareDataDiv input[name=' + key + ']').val();
			value = value.replace(/,/g,'');
			data[key] =  value;
		}
		//log(this.selectSingleNode);
		$.ajax({
			url : 'saveDeclareData',
			type : 'POST',
			dataType : 'json',
			data : {
				id : this.selectSingleNode['id'],
				data:JSON.stringify(data)
			},
			success : function(result) {
				//alert(result);
				var rs = result;
				if (rs.status) {
					alert("保存成功!");
				} else {
					alert("保存失败!");
				}
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
	};
	/**
	 * 通过接口导入各省申报数据
	 */
	this.importFromInterface = function(){
		var time = $('#time').val()
		//log(this.selectSingleNode);
		mask("正在导入...");
		$.ajax({
			url : 'importFromInterface',
			type : 'POST',
			dataType : 'json',
			data : {
				mdate:time
			},
			success : function(result) {
				if(result){
					loadTreeData();
					alert(result['msg']);
					
				}else{
					alert("导入失败!");
				}
				unmask();
			},
			error : function(xhr, status) {
				unmask();
				alert("系统错误!");
			}
		});
	};
	//提交数据
	this.submitDeclare = function(){
		var ids = null;
		var checkedNodes = $('#declareMenu').tree('getChecked');
		if(checkedNodes == null || checkedNodes.length ==0){
			ids = $("#paramid").val();
			if(validUtil.isNull(ids)){
				alert("请选择一条数据");
				return;
			}
		}else{
			var data = new Array();
			for(var i = 0;i<checkedNodes.length;i++){
				data.push(checkedNodes[i]['id']);
			}
			ids = data.join(",");
		}
		
		
		//alert(ids);
		//return ;
		$.ajax({
			url : 'submitDeclare',
			type : 'POST',
			dataType : 'json',
			data : {
				ids : ids
			},
			success : function(result) {
				//alert(result);
				var rs = result;
				if (rs.status) {
					alert("提交成功!");
					window.location.reload();
				} else {
					alert("提交失败!");
				}
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
	};
	
	// 加载EL按钮数据
	this.loadElData = function(timeType, areaList) {
		var area = $('#area').val();
		myDeclare.timeType = timeType;
		myDeclare.areaList = areaList;
		var content="";
		/*
		if(area=="国调"){
			content="<span><a class='button button-glow button-rounded button-caution' style='font-size:15px;line-height:25px;height:25px;padding:0 20px' href='#' onclick='match.matchData();'>优化计算</a></span><span><a class='btn1' href='#' onclick='declare.loadDeclarMeg();'>*查看</a></span><div class='cl'></div>";
			$('#btnData').append(content);
		}else{
			content="<span><a class='btn1'  href='#' onclick='declare.addData();'>+添加</a></span>"+
			  "<span><a class='btn1' href='#' onclick='declare.updData();'>*修改</a></span>"+
			  "<span><a class='btn1' href='#' onclick='declare.loadDeclarMeg();'>*查看</a></span>"+
			  "<span><a class='btn1' href='#' onclick='declare.deleteDeclare();'>-删除</a></span><div class='cl'></div>";
		     $('#btnData').append(content);
		}
		*/
	};
	
	// 显示区域
	this.showArea = function(userArea) {
		//alert("===");
		for (var i = 0; i < myDeclare.areaList.length; i++) {
			var area = myDeclare.areaList[i].area;
			if (area != '国调') {
				$('#area').append('<option value="' + area + '">' + area + '</option>');
			}
		}		
		if (userArea == '国调') {
			$('.posty').css('display', 'block');
		} else {
			$("#area  option[value="+userArea+"]").attr("selected",true);
			$('.posty').css('display', 'none');
		}
		declare.getDeclare();
	};
	
	// 申报单中数据变动flag(变动为true,未变动为false)
	this.dataFlag = false;
	
	// 修改数据变动flag状态
	this.changeDataFlag = function(dataFlag) {
		myDeclare.dataFlag = dataFlag;
	};
	
	// 数据被修改
	this.changeData = function() {
		myDeclare.changeDataFlag(changed);
	};
	
	// 重置数据修改状态
	this.initChangeData = function() {
		myDeclare.changeDataFlag(unchanged);
	};
	
	// 校验数据是否有变动(变动返回true,未变动返回false)
	this.checkDataChanged = function() {
		if (myDeclare.dataFlag) {
			if (confirm("数据变动,是否保存?")) {
				myDeclare.updateDeclare();
				myDeclare.initChangeData();
				return changed;
			} else {
				myDeclare.selectedDalare.find('input').val(myDeclare.selectedDalare.attr('declareName'));
				// 还原文本域失效
				myDeclare.inputValueToArea(myDeclare.selectedDalare.attr('declareComment'));
				myDeclare.initChangeData();
				return unchanged;
			}
		}
		return unchanged;
	};
	
	
	// 显示申报单类型DIV
	this.showDeclareDataDiv = function() {
		//$('#declareDataDiv').show();
	};
	// 修改申报单数据
	this.updData = function() {
		declare.addData("update");
	};
	// 隐藏申报单类型DIV
	this.hideDeclareDataDiv = function() {
		$('#declareDataDiv').hide();
	};
	
	// 切换区域
	this.changeArea = function() {
		myDeclare.getDeclare();
	};
	// 删除申报单数据(包含申报类型数据)
	this.deleteDeclare = function() {
		var paramid = $('#paramid').val();
		/******2016.08.17修改新界面******start******/
		if (!validUtil.isNull(paramid)) {
		 //if (myDeclare.selectedDalare && !myDeclare.checkDataChanged()) {
		 /******2016.08.17修改新界面******end******/
		
			if (confirm("是否确定删除?")) {
			$.ajax({
				url : 'delete',
				type : 'POST',
				dataType : 'json',
				data : {
					id : paramid
				},
				success : function(result) {
					if (result.status) {
						alert("删除成功!");
						loadTreeData();
					} else {
						alert(result.msg);
					}
				},
				error : function(xhr, status) {
					alert("系统错误!");
				}
			});
			}
		}
	};
	
	// 修改申报单数据
	this.updateDeclare = function() {
		$.ajax({
			url : 'update',
			type : 'POST',
			dataType : 'json',
			data : {
				declarePo : myDeclare.makeDeclareData()
			},
			success : function(result) {
				if (result) {
					myDeclare.initChangeData();
					myDeclare.refreshDeclareData();
					alert("保存成功!");
				} else {
					alert("保存失败!");
				}
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
	};
/*	this.selectDeclareData=function(dname){
	    var time = $("#time").val().replace(/[^0-9]/ig, ""); 
	    var countBillMeg="";
		$.ajax({
			url : 'getIssueCountMeg',
			type : 'POST',
			dataType : 'json',
			data : {
				time : time,
				sheetName:dname
			},
			success : function(result) {
				if (result) {
					if(result.sumQ!=null){
						 countBillMeg='<tr><td align="center" valign="middle" colspan="4" style="font-size:22px;">发布信息</td></tr>'+
							 '<tr><td align="right" valign="middle">出清电量(Mwh)：</td><td align="left" valign="middle"><input id="buyNum" size="20" value="'+result.clear+'"/><span style="color:red;"></span></td>'+
					        '<td align="right" valign="middle">出清费用(元)：</td><td align="left" valign="middle"><input id="buyNum" size="20" value="'+result.fdfy+'"/><span style="color:red;"></span></td></tr>'+
					        '<tr><td align="right" valign="middle">结算电量(Mwh)：</td><td align="left" valign="middle"><input id="buyNum" size="20" value="/"/><span style="color:red;"></span></td>'+
					        '<td align="right" valign="middle">结算费用(元)：</td><td align="left" valign="middle"><input id="buyNum" size="20" value="/"/><span style="color:red;"></span></td></tr>';
					}
				} 
			},
			error : function(xhr, status) {
				alert("系统错误!");
			},
			async:false
		});
		return countBillMeg;
	};*/
	//加载回显申报单详细
	this.loadDeclarMeg= function() {
		var paramid = $('#paramid').val();
		if(paramid==null || paramid=="" || paramid=="undefined"){
			alert("请选择申购单！");
		}else{
			
			var url = baseUrl+"/declare/new1?readOnly=true&id="+paramid+"&mdate="+mdate;
			createWin(url,720,350,"查看");
			return;
			
			 var time= $('#time').val();
			 var idArr = paramid.split(",");
			 if(idArr.length>6){
				 alert("只能一次查看6张单据!");
				 return false;
			 }
			/*var clearstate="0";
			var user = $('#user').val();
			var area = $('#area').val();
	  	    var dname = $("#dname").val();
	  	    var sumprice = $("#sumprice").val();
	  	    var sumq = $("#sumq").val();
	  	   
	  	    var startdate = $('#time').val();
	  	    var enddate = $('#time').val();
	  	    var rname = $("#rname").val();
	  	    var descr = $("#descr").val();
	  	    var ave_p=Math.round(sumq*4/96);
	  	    var opt=$('#opt').val();
	  	    var mdate=$('#mdate').val();
			var drole = $('#drole').val()=="buy"?"买单":"卖单";
			var clearstate=$('#clearstate').val();*/
			/*var contentStr='<div id="doc" style="margin:0 auto;text-align:center;">'+
				'<iframe id="test-iframe" src="/market/iframe/main.jsp?paramid='+paramid+'" style="width: 640;height: 480;border:0"></iframe>'+
				'</div>';*/
			var contentStr='<iframe id="test-iframe" src="/market/iframe/main.jsp?paramid='+paramid+'" style="width: 640;height: 480;border:0"></iframe>';
			//contentStr=contentStr+'</table></div>';
				var d = dialog({
				    title: ' ',
				    width: 660,  
				    height:480,   
				    content:contentStr,
				    okValue: '导出',
				    padding: 0,
				    ok: function () {
				    	window.open("/market/declare/exportDeclarDoc?paramid="+paramid+"&time="+time.replace(/[^0-9]/ig, ""));
				    }
				});
			$('#exportWord input').each(function(){
			    $(this).attr("disabled","disabled");
			});
			d.show();	
		}

	};
	//添加申报单
	this.addData= function(param) {
		var paramid = $('#paramid').val();
		//console.info(param)
		//alert("0=="+paramid);
		if(validUtil.isNull(paramid) || paramid.indexOf(",")>-1){
			if(this.selectSingleNode != undefined){
				paramid = this.selectSingleNode['id'];
			}
			
		}
		//alert("1=="+paramid);
		if(validUtil.isNull(paramid)){
			if(_isState){
				alert("当前没有申报单！");
				return;
			}
			//alert("请选择一个申购单！");
			//return ;
			param=="update";
		}else{
			param=="add";
		}
		
		if(param=="update"){
			//alert(paramid);
			
			
				
				//新的
				//新页面
				var mdate=$('#time').val();
				var url = baseUrl+"/declare/new1?id="+paramid+"&mdate="+mdate;
				createWin(url,720,460,_isState?"查阅":"编辑");
				return;
				//旧的
				var user = $('#user').val();
				var area = $('#area').val();
		  	    var dname="";
		  	    var sumprice="";
		  	    var sumq="";
		     	var time = $("#time").val(); 
		  	    var startdate = $('#time').val();
		  	    var enddate = $('#time').val();
		  	    var rname="*";
		  	    var descr="*";
		  	    var ave_p="";
		  	    var opt=$('#opt').val();
		  	    var mdate=$('#mdate').val();
		  	   /* var type=$('#type').val();
		        var a2=$('#a2').val();
		        var a3=$('#a3').val();*/
				if(param=="update"){
					dname = this.selectSingleNode['text'];//$("#dname").val();
					sumprice = this.selectSingleNode['attributes']['sumPrice'];//$("#sumprice").val();
					sumq = this.selectSingleNode['attributes']['sumQ'];//$("#sumq").val();
					startdate =mdate;
					enddate = mdate;
					rname = this.selectSingleNode['attributes']['rname'];//$("#rname").val();
					descr = this.selectSingleNode['attributes']['descr'];//$("#descr").val();
					ave_p="";
				}
				var drole = $('#drole').val()=="sale"?"买单":"卖单";
				var contenStr='<div class="_css_main_form"><span style="margin-left: 220px; font-size: 25px;">跨省区电力市场申报单</span><table id="addTable" width="100%" border="0"  class="table">'+
			                  '<tr><td align="right" valign="middle" width="20%">申报单位：</td><td width="30%" align="left" valign="middle">'+area+'</td>'+
			                  '<td align="right" valign="middle" width="20%">申报用户：</td><td width="30%" align="left" valign="middle">'+user+'</td></tr>'+
			                  '<tr><td align="right" valign="middle">申报交易名称：</td><td align="left" valign="middle"><input placeholder="默认值为'+drole+'" id="sheetName" size="20" value="'+dname+'"/></td>'+
			                  '<td align="right" valign="middle">申报电量(Mwh)：</td><td align="left" valign="middle"><input id="buyNum" size="20" value="'+sumq+'" oninput="OnInput (event)"; onpropertychange="OnPropChanged (event)";/><span style="color:red;">*</span></td></tr>'+
			                  '<tr><td align="right" valign="middle">电价(元/Mwh)：</td><td align="left" valign="middle"><input id="price" size="20" value="'+sumprice+'"/><span style="color:red;">*</span></td>'+
			                  '<td align="right" valign="middle">平均电力(MW)：</td><td align="left" valign="middle"><lable id="avep">'+ave_p+'</lable></td></tr>'+
			                  '<td align="right" valign="middle">交易日期：</td><td align="left" valign="middle"><input type="text" class="Wdate input" id="jyDate"  onfocus="WdatePicker({isShowWeek:true})" readonly="readonly"  value="'+time+'"/></td>'+
					          '<td align="right" valign="middle"></td><td align="left" valign="middle"></td></tr>';
				contenStr=contenStr+'<tr><td align="right" valign="middle">备注：</td><td align="left" valign="middle" colspan="3"><textarea id="adescr" cols="61" >'+descr+'</textarea><select style="display:none" id="type" onchange="selectChange();"><option value="1a" id="1a"  selected="selected">全天</option><option value="2a" id="2a">高峰</option><option value="3a" id="3a">低谷</option></select></td></tr>'+'</table></div>';
				var d = dialog({
					    title: ' ',
					    width: 650,  
					    height: 320,
					    content:contenStr,
					    okValue: '确定',
					    padding: 0,
					    ok: function () {
							if(saveData()){
					        var num = $('#buyNum').val();
					        var sheetName=$('#sheetName').val();
					        var price=$('#price').val();
					        var type=$('#type').val();
					        var a2=$('#a2').val();
					        var a3=$('#a3').val();
					        var startDate=$('#startDate').val();
					        var endDate=$('#endDate').val();
					        var adescr=$('#adescr').val();
					        var arname=$('#arname').val();
					        var avgnum="";
					        if(type=="1a"){
					        	avgnum=num*4/96;
					        }else if(type=="2a"){
					        	avgnum=num*4/a2;
					        }else if(type=="3a"){
					        	avgnum=num*4/a3;
					        }
					        var str = sheetName+','+avgnum.toFixed(0)+','+price+','+type+','+num+','+startDate+','+endDate+','+adescr+','+arname;
						    var arr=str.split(",");
						    if (!myDeclare.checkDataChanged()) {
						    	if(param=="update"){
									time=$("#jyDate").val();
								}
								$.ajax({
									url : 'add',
									type : 'POST',
									dataType : 'json',
									data : {
										str : str,
										time:time,
										param:param,
										paramid:paramid

									},
									success : function(result) {
										if (result!=0) {
											 d.close().remove();
											 $('#paramid').val("");
											/*if(param=="update"){
												var index = parseInt(paramid)+1;
												var node = $('#declareMenu').tree('find', index);
												$('#declareMenu').tree('check', node.target);
											}*/
											loadTreeData(result);
										} else {
											d.close().remove();
											alert("新增失败!");
										}
									},
									error : function(xhr, status) {
										d.close().remove();
										alert("系统错误!");
									},
									async:false
								});
							}
					
							 }else{
								return false;
							}
					    }
					});
						$('#sheetName').attr("disabled","disabled");
						$('#startDate').attr("disabled","disabled");
						$('#endDate').attr("disabled","disabled");
						$('#type').find("option[value='"+opt+"']").attr("selected",true);
						var ave_p="";
						var a2=$('#a2').val();
					    var a3=$('#a3').val();
						if(opt=="1a"){
							   ave_p=sumq*4/96;
					        }else if(opt=="2a"){
					        	ave_p=sumq*4/a2;
					        }else if(opt=="3a"){
					        	ave_p=sumq*4/a3;
					        }
						$("#avep").html(Math.round(ave_p)); 
				        d.show();	
				        var fileds_v_ = {"price":{"allowBlank":"不能为空","number":"必须是数字"},"buyNum":{"allowBlank":"不能为空","number":"必须是数字"},"startDate":{"allowBlank":"不能为空"}};
						$("#addTable").find("input").bind("blur",function(){
							formUtil.checkValid(this);
						});
						saveData();
						/*setTimeout(function () {
						    d.close().remove();
						}, 2000);*/
			
		
		}else{
			//新页面
			var mdate=$('#time').val();
			var url = baseUrl+"/declare/new1?mdate="+mdate;
			createWin(url,720,450,"编辑");
			return;
			//旧的做法
			var user = $('#user').val();
			var area = $('#area').val();
	  	    var dname="";
	  	    var sumprice="";
	  	    var sumq="";
	     	var time = $("#time").val(); 
	  	    var startdate = $('#time').val();
	  	    var enddate = $('#time').val();
	  	    var rname="*";
	  	    var descr="*";
	  	    var ave_p="";
	  	    var opt=$('#opt').val();
	  	    var mdate=$('#mdate').val();
			if(param=="update"){
				dname = $("#dname").val();
				sumprice = $("#sumprice").val();
				sumq = $("#sumq").val();
				startdate =mdate;
				enddate = mdate;
				rname = $("#rname").val();
				descr = $("#descr").val();
				ave_p=Math.round(sumq*4/96);
			}
			//var drole = $('#drole').val()=="sale"?"买单":"卖单";
			var contenStr='<div class="_css_main_form"><span style="margin-left: 220px; font-size: 25px;">跨省区电力市场申报单</span><table id="addTable" width="100%" border="0"  class="table">'+
		                  '<tr><td align="right" valign="middle" width="20%">申报单位：</td><td width="30%" align="left" valign="middle">'+area+'</td>'+
		                  '<td align="right" valign="middle" width="20%">申报用户：</td><td width="30%" align="left" valign="middle">'+user+'</td></tr>'+
		                  '<tr><td align="right" valign="middle">申报交易名称：</td><td align="left" valign="middle"><input placeholder="默认名称可修改" id="sheetName" size="20" value="'+dname+'"/></td>'+
		                  '<td align="right" valign="middle">申报电量(Mwh)：</td><td align="left" valign="middle"><input id="buyNum" size="20" value="'+sumq+'"  oninput="OnInput (event)"; onpropertychange="OnPropChanged (event)";/><span class="css_span_required">*</span></td></tr>'+
		                  '<tr><td align="right" valign="middle">电价(元/Mwh)：</td><td align="left" valign="middle"><input id="price" size="20" value="'+sumprice+'"/><span class="css_span_required">*</span></td>'+
		                  '<td align="right" valign="middle">平均电力(MW)：</td><td align="left" valign="middle"><lable id="avep">'+ave_p+'</lable></td></tr>'+
		                  
		                  '<tr><td align="right" valign="middle">开始时间：</td><td align="left" valign="middle"><input type="text" class="Wdate input" id="startDate"  onfocus="WdatePicker({isShowWeek:true})" readonly="readonly" value="'+startdate+'"/><span style="color:red;">*</span></td>'+
	                      '<td align="right" valign="middle">结束时间：</td><td align="left" valign="middle"><input type="text" class="Wdate input" id="endDate"  onfocus="WdatePicker({isShowWeek:true})" readonly="readonly"  value="'+enddate+'"/><span style="color:red;">*</span></td></tr>'+
	                      '<tr><td align="right" valign="middle">备注：</td><td align="left" valign="middle" colspan="3"><textarea id="adescr" cols="61" >'+descr+'</textarea><select style="display:none" id="type" onchange="selectChange();"><option value="1a" id="1a"  selected="selected">全天</option><option value="2a" id="2a">高峰</option><option value="3a" id="3a">低谷</option></select></td></tr>'+'</table></div>';
				var d = dialog({
				    title: ' ',
				    width: 650,  
				    height: 320,
				    content:contenStr,
				    okValue: '确定',
				    padding: 0,
				    ok: function () {
				    //	alert(saveData());
						if(saveData()){
						    var num = $('#buyNum').val();
					        var sheetName=$('#sheetName').val();
					        var price=$('#price').val();
					        var type=$('#type').val();
					        var a2=$('#a2').val();
					        var a3=$('#a3').val();
					        var startDate=$('#startDate').val();
					        var endDate=$('#endDate').val();
					        var adescr=$('#adescr').val();
					        var arname=$('#arname').val();
					        var avgnum="";
					        if(type=="1a"){
					        	avgnum=num*4/96;
					        }else if(type=="2a"){
					        	avgnum=num*4/a2;
					        }else if(type=="3a"){
					        	avgnum=num*4/a3;
					        }
					        var str = sheetName+','+avgnum.toFixed(0)+','+price+','+type+','+num+','+startDate+','+endDate+','+adescr+','+arname;
						    var arr=str.split(",");
						    if (!myDeclare.checkDataChanged()) {
								$.ajax({
									url : 'add',
									type : 'POST',
									dataType : 'json',
									data : {
										str : str,
										time:time,
										param:param,
										paramid:paramid
									},
									success : function(result) {
										 $('#paramid').val("");
										if (result!=0) {
											loadTreeData(result);
										} else {
											d.close().remove();
											alert("新增失败!");
										}
									},
									error : function(xhr, status) {
										d.close().remove();
										alert("系统错误!");
									}
								});
							}
						   // d.remove();
						    //return true;
					}else{
						return false;
					}
						
				  }
				
				});
				
			d.show();	
			var fileds_v_ = {"price":{"allowBlank":"不能为空","number":"必须是数字"},"buyNum":{"allowBlank":"不能为空","number":"必须是数字"},"startDate":{"allowBlank":"不能为空"}};
			$("#addTable").find("input").bind("blur",function(){
				formUtil.checkValid(this);
			});
			saveData();
		}
		
	};
	// 批量处理表格数据
	this.updateDeclare = function() {
		$.ajax({
			url : 'update',
			type : 'POST',
			dataType : 'json',
			data : {
				declarePo : myDeclare.makeDeclareData()
			},
			success : function(result) {
				if (result) {
					myDeclare.initChangeData();
					myDeclare.refreshDeclareData();
					alert("保存成功!");
				} else {
					alert("保存失败!");
				}
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
	};
	// 获取申报单数据
	this.getDeclare = function() {
		$.ajax({
			url : 'getDeclareList',
			type : 'POST',
			dataType : 'json',
			data : {
				area : $('#area').val(),
				time : time
			},
			success : function(result) {
				if (result) {
					$('#declareMenu').text('');
					for (var i = 0; i < result.length; i++) {
						var declareId = result[i].id;
						var declareName = result[i].sheetName;
						var type = result[i].drloe;
						var declareComment = result[i].descr;
						var typeCls;
						if ('buy' == type) {
							typeCls = 'bgy';
						} else {
							typeCls = 'bgi';
						}
						$('#declareMenu').append('<li declareId="' + declareId + '" declareName="' + result[i].sheetName + '" declareComment="' + declareComment + '">'
								+ '<div class="fl ' + typeCls + '"><input value="'
								+ result[i].sheetName
								+ '" readonly="readonly"></div><div class="cl"></div></li>');
					}
					myDeclare.hideDeclareDataDiv();
				
					$(".lmenu").children("ul:first").children("li:first").trigger("click");
				} else {
					alert("获取失败!");
				}
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
	}
	
	

	
	
	// 获取申报单类型数据
	this.getDeclareData = function(selectedDalare, type,id) {	
		//if ((!myDeclare.selectedDalare || myDeclare.selectedDalare.attr('declareId') != selectedDalare.attr('declareId')) && !myDeclare.checkDataChanged()) {	
			
		if (myDeclare.selectedDalare) {
				myDeclare.selectedDalare.attr('class', '');
				myDeclare.selectedDalare.find('input').attr('class', '');
				myDeclare.selectedDalare.find('input').attr('readonly', 'readonly');
			}
			if(id.indexOf(",")>0){
				//alert(id);
				//电价
				$("#fl_price").hide();
				//alert(id);
				var idArr = id.split(",");
				var firstDrole=null;
				var node = null;
				for(var i = 0;i<idArr.length;i++){
					
					//alert(idArr[i]);
					node = $('#declareMenu').tree('find',idArr[i]);
					
					if(!validUtil.isNull(node.attributes)){
						//alert(firstDrole+"====="+node.attributes['drloe']);
						if(firstDrole == null){
							firstDrole = node.attributes['drloe'];
						}else{
							if(firstDrole != node.attributes['drloe']){
								alert("只能统计买方或者卖方一种类型数据");
								return false;
							}
						}
					}
				}
				
			}else{
				$("#fl_price").show();
			}
			//log(myDeclare.selectedDalare);
			//log(selectedDalare);
			myDeclare.changeDeclareTypeStyle(type);
			myDeclare.selectedDalare = selectedDalare;
			
			
			
			myDeclare.dalareType = type;
			$.ajax({
				url : 'getDeclareSumData',
				type : 'POST',
				dataType : 'json',
				data : {
					id : id,
					type : type
				},
				success : function(result) {
					if (result) {
						myDeclare.inputValueToTable(result);
						myDeclare.makeDataToChart(result);
						//myDeclare.inputValueToArea(selectedDalare.attr('declareComment'));
						myDeclare.showDeclareDataDiv();
					} else {
						alert("获取失败!");
					}
				},
				error : function(xhr, status) {
					alert("系统错误!");
				}
			});
		//}
	};
	
	// 刷新申报单类型数据
	this.refreshDeclareData = function() {
		myDeclare.selectedDalare.attr('declareName', myDeclare.selectedDalare.find('input').val());
		myDeclare.selectedDalare.attr('declareComment', $('#comment').text());
		$.ajax({
			url : 'getDeclareData',
			type : 'POST',
			dataType : 'json',
			data : {
				id : myDeclare.selectedDalare.attr('declareId'),
				type : myDeclare.dalareType
			},
			success : function(result) {
				if (result) {
					myDeclare.inputValueToTable(result);
					myDeclare.makeDataToChart(result);
					myDeclare.inputValueToArea(myDeclare.selectedDalare.attr('declareComment'));
					myDeclare.showDeclareDataDiv();
				} else {
					alert("获取失败!");
				}
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
	};
	
	// 根据申报单类型获取申报单类型数据
	this.getDeclareDataByDeclareType = function(type) {
		if(validUtil.isNull(myDeclare.selectedDalare.attr('declareId'))){
			return ;
		}
		if (!myDeclare.checkDataChanged()) {
			myDeclare.changeDeclareTypeStyle(type);
			myDeclare.dalareType = type;
			declareType = type;
			$.ajax({
				url : 'getDeclareData',
				type : 'POST',
				dataType : 'json',
				data : {
					id : myDeclare.selectedDalare.attr('declareId'),
					type : type
				},
				success : function(result) {
					if (result) {
						myDeclare.inputValueToTable(result);
						myDeclare.makeDataToChart(result);
					} else {
						alert("获取失败!");
					}
				},
				error : function(xhr, status) {
					alert("系统错误!");
				}
			});
		}
	}
	
	// 将申报单类型数据插入表格中
	this.inputValueToTable = function(data) {
		var isAllowEdit = false;
		if(this.selectSingleNode != null && this.selectSingleNode['attributes']['allowEdit'] == true){
			isAllowEdit = true;
		}
		if (data) {
			for (var key in data) {
				if (/^h[0-9]{2}$/.test(key)) {
					if (myDeclare.dalareType == '1a' && isAllowEdit) {
						$('#declareDataDiv input[name=' + key + ']').attr('disabled', false);
					} else if (myDeclare.dalareType == '2a' && myDeclare.timeType[key] == '峰') {
						$('#declareDataDiv input[name=' + key + ']').attr('disabled', true);
					} else if (myDeclare.dalareType == '3a' && myDeclare.timeType[key] == '谷') {
						$('#declareDataDiv input[name=' + key + ']').attr('disabled', true);
					} else {
						$('#declareDataDiv input[name=' + key + ']').attr('disabled', true);
					}
					$('#declareDataDiv input[name=' + key + ']').val($.format(data[key], 3, ','));
				}
			}
			if (!data.sumQ || data.sumQ == 'null') {
				data.sumQ = 0;
			}
			if (!data.aveP || data.aveP == 'null') {
				data.aveP = 0;
			}
			$('#declareDataDiv span[name=sumData]').html($.format(data.sumQ, 3, ',')+' &nbsp;MWh');
			//document.getElementById("sumData").setAttribute("size",$('#declareDataDiv input[name=sumData]').val().length-1);
			$('#declareDataDiv span[name=sumPrice]').html($.format(data.sumPrice, 3, ',')+' &nbsp; 元/MWh');
		//	document.getElementById("sumPrice").setAttribute("size",$('#declareDataDiv input[name=sumPrice]').val().length-1);
			$('#declareDataDiv span[name=avgValue]').html($.format(data.aveP, 3, ',')+' &nbsp;MW');
			
		}
	}
	
	// 将申报单类型数据插入曲线图中
	this.makeDataToChart = function(data) {
		var serieData = null;;
		if(data == null){
			serisData = [];
		}else{
			serisData = [
					        data.h01, data.h02, data.h03, data.h04, data.h05, data.h06, data.h07, data.h08,
					        data.h09, data.h10, data.h11, data.h12, data.h13, data.h14, data.h15, data.h16,
					        data.h17, data.h18, data.h19, data.h20, data.h21, data.h22, data.h23, data.h24,
					        data.h25, data.h26, data.h27, data.h28, data.h29, data.h30, data.h31, data.h32,
					        data.h33, data.h34, data.h35, data.h36, data.h37, data.h38, data.h39, data.h40,
					        data.h41, data.h42, data.h43, data.h44, data.h45, data.h46, data.h47, data.h48,
					        data.h49, data.h50, data.h51, data.h52, data.h53, data.h54, data.h55, data.h56,
					        data.h57, data.h58, data.h59, data.h60, data.h61, data.h62, data.h63, data.h64,
					        data.h65, data.h66, data.h67, data.h68, data.h69, data.h70, data.h71, data.h72,
					        data.h73, data.h74, data.h75, data.h76, data.h77, data.h78, data.h79, data.h80,
					        data.h81, data.h82, data.h83, data.h84, data.h85, data.h86, data.h87, data.h88,
					        data.h89, data.h90, data.h91, data.h92, data.h93, data.h94, data.h95, data.h96
						];
		}
		//alert(serisData);
			var charts = {
					title : {
						text : '电量图',
						x : -50 //center
					},
					xAxis : {
						categories : [
				              '00:00','00:15','00:30','00:45','01:00','01:15','01:30','01:45',
				              '02:00','02:15','02:30','02:45','03:00','03:15','03:30','03:45',
				              '04:00','04:15','04:30','04:45','05:00','05:15','05:30','05:45',
				              '06:00','06:15','06:30','06:45','07:00','07:15','07:30','07:45',
				              '08:00','08:15','08:30','08:45','09:00','09:15','09:30','09:45',
				              '10:00','10:15','10:30','10:45','11:00','11:15','11:30','11:45',
				              '12:00','12:15','12:30','12:45','13:00','13:15','13:30','13:45',
				              '14:00','14:15','14:30','14:45','15:00','15:15','15:30','15:45',
				              '16:00','16:15','16:30','16:45','17:00','17:15','17:30','17:45',
				              '18:00','18:15','18:30','18:45','19:00','19:15','19:30','19:45',
				              '20:00','20:15','20:30','20:45','21:00','21:15','21:30','21:45',
				              '22:00','22:15','22:30','22:45','23:00','23:15','23:30','23:45'
						] ,
						showLastLabel: true
					},
					yAxis : {
						title : {
							text : '单位：MW'
						},
						plotLines : [{
							value : 0,
							width : 1,
							color : '#ffef00'
						}]
					},
					tooltip : {
						valueSuffix : 'MW'
					},
					legend : {
						layout : 'vertical',
						align : 'right',
						verticalAlign : 'middle',
						borderWidth : 0
					},
					series : [{
						name : '电量',
						data : serieData
					}]
				};
			$('.cchart').highcharts(charts);	
		
	}
	
	// 将申报单类型说明插入文本域中
	this.inputValueToArea = function(data) {
		if (data == 'null') {
			data = '';
		}
		$('#comment').text(data);
	}
	
	// 整理需要修改的申报单数据
	this.makeDeclareData = function() {
		var declare = {};
		var declareId = myDeclare.selectedDalare.attr('declareId');
		var declareName = myDeclare.selectedDalare.find('input').val();
		var comment = $('#comment').text();
		var declareType = myDeclare.dalareType;
		var declareTypeData = myDeclare.makeDataByTable();
		declare['id'] = declareId;
		declare['sheetName'] = declareName;
		declare['descr'] = comment;
		declare['declareDatas'] = [declareTypeData];
		return JSON.stringify(declare);
	};
	
	// 根据表格整理申报单类型数据
	this.makeDataByTable = function() {
		var declareTypeDataInputs = $('#declareDataDiv').find('table input');
		var declareTypeData = {};
		var sum = 0;
		for (var index in declareTypeDataInputs) {
			var declareTypeDataInput = declareTypeDataInputs[index];
			declareTypeData[declareTypeDataInput.name] = declareTypeDataInput.value;
			if (declareTypeDataInput.value) {
				sum += Number(declareTypeDataInput.value);
			}
		}
		var avg = sum / myDeclare.timeType['count' + myDeclare.dalareType];
		declareTypeData['id'] = myDeclare.selectedDalare.attr('declareId');
		declareTypeData['dtype'] = myDeclare.dalareType;
		declareTypeData['sumQ'] = sum;
		declareTypeData['aveP'] = avg.toFixed(2);
		return declareTypeData;
	}
	
	// 申报单类型数据修改后按回车修改所有单元格数据
	this.copyTableValue = function(thisInput, e) {
		// 兼容FF和IE和Opera
		var theEvent = e || window.event;
		var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
		if (thisInput.focus() && code == 13) {
			var value = thisInput.val();
			for (var i = 1; i <= 96 ; i++) {
				var key;
				if (i < 10) {
					key = 'h0' + i;
				} else {
					key = 'h' + i;
				}
				if (myDeclare.dalareType == '1a') {
					$('#declareDataDiv input[name=' + key + ']').val(value);
				}
				if (myDeclare.dalareType == '2a' && myDeclare.timeType[key] == '峰') {
					$('#declareDataDiv input[name=' + key + ']').val(value);
				}
				if (myDeclare.dalareType == '3a' && myDeclare.timeType[key] == '谷') {
					$('#declareDataDiv input[name=' + key + ']').val(value);
				}
			}
		}
	}
	
	// 修改申报单类型样式
	this.changeDeclareTypeStyle = function(type) {
		var declareTypes = $('#declareDataDiv .conrightt1 a');
		for (var i = 0; i < 1 ; i++) {
			var declareType = declareTypes[i];
			declareType.style.color = '#D1B664';
			declareType.style.fontWeight = 'bold';
		}
	}
	
	// 修改申报单名称
	this.changeDeclareName = function() {
		myDeclare.selectedDalare.find('input').attr('class', '');
		myDeclare.selectedDalare.find('input').attr('readonly', false);
		declare.changeData();
	}
	
	// 完成修改申报单名称
	this.finishChangeDeclareName = function() {
		myDeclare.selectedDalare.find('input').attr('class', 'bghh');
		myDeclare.selectedDalare.find('input').attr('readonly', true);
	};
	
	//添加申报单
	this.upload= function(param) {		
		//var mdate=$('#time').val();
		var url = baseUrl+"/declare/upload";
		createWin(url,720,350,"导入");
		return;
	};
	//复制前天申报单
	this.copyData= function(param) {		
		var time = $("#time").val().replace(/[^0-9]/ig, "")-1;
		var mdate=$('#time').val();
		$.ajax({
			url : 'copyDeclareData',
			type : 'POST',
			data :{
				time:time,
				mdate:mdate
			},
			dataType:'json',
			success : function(result) {
				if(true==result.status){
					loadTreeData();
				}else{
					alert(result.msg);
				}
				
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
		return;
	};
	
	//复制前天申报单
	this.copyData= function(param) {		
		var time = $("#time").val().replace(/[^0-9]/ig, "")-1;
		var mdate=$('#time').val();
		$.ajax({
			url : 'copyDeclareData',
			type : 'POST',
			data :{
				time:time,
				mdate:mdate
			},
			dataType:'json',
			success : function(result) {
				if(true==result.status){
					loadTreeData();
				}else{
					alert(result.msg);
				}
				
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
		return;
	};
	this.showTab = function(index){
		$('#tab'+index).show();
		$('#tab'+(index==1?2:1)).hide();
		$('#a_'+index).css("color","#D1B664");
		$('#a_'+index).css("fontWeight","bold");
		$('#a_'+(index==1?2:1)).css("color","");
		$('#a_'+(index==1?2:1)).css("fontWeight","");
		
		/*$('#tab'+index).show();
		$('#a_'+index).css("color","#D8AD27");
		$('#a_'+index).css("font-weight","bold");
		$('#a_'+index).css("font-size","16px");
		$('.mne').find('a').each(function(){
			var id = $(this).attr('id');
			if(id.substring(2,id.length) != index){
				$(this).css("color","");
				$(this).css("fontWeight","");
				$(this).css("font-size","15px");
				$('#tab'+id.substring(2,id.length)).hide();
			}
		});*/
		if(index == 1){
			$('#btngetPlan').show();
			$('#btnSBJSImg').hide();
		}else{
			$('#btnSBJSImg').show();
			$('#btngetPlan').hide();
		}
	};
	/**
	 * 加载tab2
	 */
	this.colorArr = [ "rgb(164, 157, 153)","rgb(6, 142, 147)","rgb(51, 122, 183)"];
	this.loadArea = function(){
		var time = $("#time").val().replace(/[^0-9]/ig, "");
		$.ajax({
			url : 'getAreaSheet',
			type : 'POST',
			data :{
				mdate:time
			},
			dataType:'json',
			success : function(result) {
				if(result != null){
					var str = "";
					//console.info(result);
					for(var i = 0;i<result.length;i++){
						var obj = result[i];
						var region,areaArr;
						for(var key in obj){
							region = key;
							areaArr = obj[key];
						}
						var tr1 ='<tr class="bgh">';
						var tr2 ='<tr >';
						tr1 += '<td width="16%" class="cgr">'+region+'</td>';
						tr2 += '<td width="16%" ></td>';
						var n = 1;
						for(var j = 0;j<areaArr.length;j++){
							var areaObj = areaArr[j];
							var area,areaVal;
							for(var areaKey in areaObj){
								area = areaKey;
								areaVal = areaObj[areaKey];
							}
							tr1 += '<td width="7%" class="cgr">'+area+'</td>';
							//tr2 += '<td width="7%" align="center"><div  class="circle" style="background-color: '+declare.colorArr[areaVal]+';"></div></td>';
							
								tr2 += '<td width="7%" align="center"><div  class="circle" style="background-color: '+declare.colorArr[areaVal]+';"></div></td>';
							
							n++;
						}
						if(n<7){
							for(var j=n;j<=7;j++){
								tr1 += '<td width="7%" class="cgr"></td>';
								tr2 += '<td width="7%" align="center"></td>';
								
							}
						}
						str += tr1;
						str += tr2;
					}
					
					$('#table_area').html(str);
				}
				
			},
			error : function(xhr, status) {
				alert("系统错误!");
			}
		});
	};
	//清楚图表数据
	this.clearChartData = function(){
		if(typeof document.getElementsByTagName("iframe")["_frame"].contentWindow.clearChartData != "undefined"){
			document.getElementsByTagName("iframe")["_frame"].contentWindow.clearChartData();
		}
	};
	
	this.addBidOffer = function(id){
		document.getElementById(id).style.display = 'block';
	}
}
