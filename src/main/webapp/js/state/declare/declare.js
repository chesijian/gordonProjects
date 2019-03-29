/**
 * 
 */
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

Cookies.clear = function(name) {
  if(Cookies.get(name)){
    document.cookie = name + "=" +
    "; expires=Thu, 01-Jan-70 00:00:01 GMT";
  }
};

Cookies.getCookieVal = function(offset){
   var endstr = document.cookie.indexOf(";", offset);
   if(endstr == -1){
       endstr = document.cookie.length;
   }
   return unescape(document.cookie.substring(offset, endstr));
};
//var comboxData = new Array();
comboxData = [{"text":"00:15","value":"00:15","selected":true},{"text":"00:30","value":"00:30"},{"text":"00:45","value":"00:45"},{"text":"01:00","value":"01:00"},{"text":"01:15","value":"01:15"},{"text":"01:30","value":"01:30"},{"text":"01:45","value":"01:45"},{"text":"02:00","value":"02:00"},{"text":"02:15","value":"02:15"},{"text":"02:30","value":"02:30"},{"text":"02:45","value":"02:45"},{"text":"03:00","value":"03:00"},{"text":"03:15","value":"03:15"},{"text":"03:30","value":"03:30"},{"text":"03:45","value":"03:45"},{"text":"04:00","value":"04:00"},{"text":"04:15","value":"04:15"},{"text":"04:30","value":"04:30"},{"text":"04:45","value":"04:45"},{"text":"05:00","value":"05:00"},{"text":"05:15","value":"05:15"},{"text":"05:30","value":"05:30"},{"text":"05:45","value":"05:45"},{"text":"06:00","value":"06:00"},{"text":"06:15","value":"06:15"},{"text":"06:30","value":"06:30"},{"text":"06:45","value":"06:45"},{"text":"07:00","value":"07:00"},{"text":"07:15","value":"07:15"},{"text":"07:30","value":"07:30"},{"text":"07:45","value":"07:45"},{"text":"08:00","value":"08:00"},{"text":"08:15","value":"08:15"},{"text":"08:30","value":"08:30"},{"text":"08:45","value":"08:45"},{"text":"09:00","value":"09:00"},{"text":"09:15","value":"09:15"},{"text":"09:30","value":"09:30"},{"text":"09:45","value":"09:45"},{"text":"10:00","value":"10:00"},{"text":"10:15","value":"10:15"},{"text":"10:30","value":"10:30"},{"text":"10:45","value":"10:45"},{"text":"11:00","value":"11:00"},{"text":"11:15","value":"11:15"},{"text":"11:30","value":"11:30"},{"text":"11:45","value":"11:45"},{"text":"12:00","value":"12:00"},{"text":"12:15","value":"12:15"},{"text":"12:30","value":"12:30"},{"text":"12:45","value":"12:45"},{"text":"13:00","value":"13:00"},{"text":"13:15","value":"13:15"},{"text":"13:30","value":"13:30"},{"text":"13:45","value":"13:45"},{"text":"14:00","value":"14:00"},{"text":"14:15","value":"14:15"},{"text":"14:30","value":"14:30"},{"text":"14:45","value":"14:45"},{"text":"15:00","value":"15:00"},{"text":"15:15","value":"15:15"},{"text":"15:30","value":"15:30"},{"text":"15:45","value":"15:45"},{"text":"16:00","value":"16:00"},{"text":"16:15","value":"16:15"},{"text":"16:30","value":"16:30"},{"text":"16:45","value":"16:45"},{"text":"17:00","value":"17:00"},{"text":"17:15","value":"17:15"},{"text":"17:30","value":"17:30"},{"text":"17:45","value":"17:45"},{"text":"18:00","value":"18:00"},{"text":"18:15","value":"18:15"},{"text":"18:30","value":"18:30"},{"text":"18:45","value":"18:45"},{"text":"19:00","value":"19:00"},{"text":"19:15","value":"19:15"},{"text":"19:30","value":"19:30"},{"text":"19:45","value":"19:45"},{"text":"20:00","value":"20:00"},{"text":"20:15","value":"20:15"},{"text":"20:30","value":"20:30"},{"text":"20:45","value":"20:45"},{"text":"21:00","value":"21:00"},{"text":"21:15","value":"21:15"},{"text":"21:30","value":"21:30"},{"text":"21:45","value":"21:45"},{"text":"22:00","value":"22:00"},{"text":"22:15","value":"22:15"},{"text":"22:30","value":"22:30"},{"text":"22:45","value":"22:45"},{"text":"23:00","value":"23:00"},{"text":"23:15","value":"23:15"},{"text":"23:30","value":"23:30"},{"text":"23:45","value":"23:45"},{"text":"24:00","value":"24:00"}];
$(function(){
	
	
	$('body').bind(
			'click',
			function(e) {
				//console.info(e.target);
				var target = e.target;
				var field = target.getAttribute("field");
				if(field == null){
						if(target.getAttribute("class") != null && target.getAttribute("class").indexOf("datagrid-cell") > -1){
							
						}else{
							cancleEdit();
						}
					
				}else{
					if(field == "startTime" || field == "endTime"){
						commit("powerTable");
					}else{
						if(field == "power" || field == "price"){
							commit("timeTable");
						}else{
							cancleEdit();
						}
					}
				}
				
	});
	
	// datatables
	initTimeTable();
	initPowerTable();
	initFormData();
});



//初始化表单数据
function initFormData(){
	$('#area').textbox('setValue', dataInfo['area']);
	$('#user').textbox('setValue', dataInfo['user']);
	if(status == "new"){
		$('#startDate').datebox('setValue', dataInfo['startDate']);
		$('#copyTime').datebox('setValue', dataInfo['startDate']);
		$('#endDate').datebox('setValue', dataInfo['endDate']);
	}else{
		//alert(parent.$('#time').val());
		$('#time').datebox('setValue', parent.$('#time').val());
		$('#sheetName').textbox('setValue',dataInfo['sheetName']);
	}
}
var editIndex = undefined;
function cancleEdit(){
	//$('#timeTable').datagrid('endEdit', editIndex);
	commit("timeTable");
	commit("powerTable");
	//alert("ok");
}


$.extend($.fn.datagrid.methods, {
	editCell: function(jq,param){
		return jq.each(function(){
			
			var dg = $(this);
			var opts = $(this).datagrid('options');
			var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
			for(var i=0; i<fields.length; i++){
				var col = $(this).datagrid('getColumnOption', fields[i]);
				col.editor1 = col.editor;
				if (fields[i] != param.field){
					col.editor = null;
				}
			}
			
			//alert("editCell1");
			opts.editIndex = param.index;
            //console.info(param);
			$(this).datagrid('beginEdit', param.index);
            var ed = $(this).datagrid('getEditor', param);
           // console.info(ed);
            if (ed){
                if ($(ed.target).hasClass('textbox-f')){
                    $(ed.target).textbox('textbox').focus();
                } else {
                    $(ed.target).focus();
                }
                
                //alert($(ed.target).html());
                //alert($(".textbox-text").prop('outerHTML'));
                
            }
            
            /*
			*/
            
			for(var i=0; i<fields.length; i++){
				var col = $(this).datagrid('getColumnOption', fields[i]);
				col.editor = col.editor1;
			}
			//$(".textbox-text").focus();
			$(".textbox-text").keydown(function(e){
				if(e.keyCode == 13){
					//alert("tab")
					if(dg['context']['id'] == "timeTable"){
						if(param['field'] == "startTime"){
							//则跳转到下一个字段
							commit("timeTable");
							dg.datagrid('selectRow', param['index']).datagrid('editCell', {
			                    index: param['index'],
			                    field: 'endTime'
			                });
							
						}else{
							//则跳转到电力表格
							commit("timeTable");
							$('#powerTable').datagrid('selectRow', 0).datagrid('editCell', {
			                    index: 0,
			                    field: 'power'
			                });
						}
					}else{
						if(param['field'] == "power"){
							commit("powerTable");
							dg.datagrid('selectRow', param['index']).datagrid('editCell', {
			                    index: param['index'],
			                    field: 'price'
			                });
						}else{
							commit("powerTable");
							var rows = dg.datagrid('getRows');
							//console.info(rows);
							var count = rows.length;
							if(count>(param['index']+1)){
								dg.datagrid('selectRow', (param['index']+1)).datagrid('editCell', {
				                    index: (param['index']+1),
				                    field: 'power'
				                });
							}else{
								$('#timeTable').datagrid('updateRow',{
									index: editIndex,
									row: {
										data:$('#powerTable').datagrid("getRows")
									}
								});
							}
						}
					}
					//console.info(dg);
					//console.info(param);
				}
				 
			});
            
		});
	},
    enableCellEditing: function(jq){
        return jq.each(function(){
        	
            var dg = $(this);
            var opts = dg.datagrid('options');
            opts.oldOnClickCell = opts.onClickCell;
            opts.onClickCell = function(index, field){
            	//alert("onClickCell");
            	//alert("enableCellEditing--"+opts.editIndex);	
                if (opts.editIndex != undefined){
                    if (dg.datagrid('validateRow', opts.editIndex)){
                        dg.datagrid('endEdit', opts.editIndex);
                        opts.editIndex = undefined;
                    } else {
                        return;
                    }
                }
                /**/
                dg.datagrid('selectRow', index).datagrid('editCell', {
                    index: index,
                    field: field
                });
                
                //opts.editIndex = index;
                //opts.oldOnClickCell.call(this, index, field);
            }
        });
    }
});



function del(tableId,index){  //删除操作  
    
            //var selectedRow = $('#tt').datagrid('getSelected');  //获取选中行  
	editIndex = undefined;      
	if($('#'+tableId).datagrid("getRows").length==1){
	}else{
		$('#'+tableId).datagrid('deleteRow',index); 
		var rows = $("#"+tableId).datagrid("getRows");
		//console.info(rows);
		$("#"+tableId).datagrid("loadData",rows);
	}
	if(tableId=="timeTable"){
		var data=$("#timeTable").datagrid('getData');
		if(index>=data.rows.length){
			$('#timeTable').datagrid("selectRow",index-1);
		}else{
			$('#timeTable').datagrid("selectRow",index);
		}
		
	}
	
}  
function add(tableId,index){  //删除操作  
	if("timeTable" == tableId){
		$('#'+tableId).datagrid('appendRow',{startTime:"00:15",endTime:"24:00"});
	}else{
		$('#'+tableId).datagrid('appendRow',{});
	}
		
} 

function commit(tableId){
	var dg = $('#'+tableId);
	var opts = dg.datagrid('options');
	//alert(opts.editIndex);
        if (opts.editIndex != undefined){
            if (dg.datagrid('validateRow', opts.editIndex)){
                dg.datagrid('endEdit', opts.editIndex);
            }
        }
}

function loadTimeTableData(){
	if(typeof timeData == "undefined" || timeData == null || timeData.length ==0){
		$('#timeTable').datagrid("loadData",[{"startTime":"00:15","endTime":"24:00"}]);
		//add("timeTable");
	}else{
		$('#timeTable').datagrid("loadData",timeData);
	}
}


function initTimeTable(){
	var editRow = undefined;
	var columns;
	if(readOnly=="true"){
		columns = [[ {
            field: 'startTime',
            title: '开始时段',
            width: 100
        }, {
            field: 'endTime',
            title: '结束时段',
            width: 100
        } ]];
	}else{
		columns = [[ {
            field: 'startTime',
            title: '开始时段',
            width: 100,
            editor: { type: 'combobox', options: { 
            	editable:false,
            	//required: true,
            	panelHeight:150,
            	data:  comboxData,  
            	valueField:'value',  
            	textField:'text',
            	onChange:function(){
            		//console.info($(this).combobox('textbox'));
            		$(this).combobox('textbox').focus();
            	}
            	
		
            } }
        }, {
            field: 'endTime',
            title: '结束时段',
            editor: { type: 'combobox', options: { 
            	editable:false,
            	//required: true,
            	panelHeight:150,
            	data:  comboxData,  
            	valueField:'value',  
            	textField:'text',
            	onChange:function(){
            		//console.info($(this).combobox('textbox'));
            		$(this).combobox('textbox').focus();
            	}
            } },
            width: 100
        },{field:'opt',title:'操作',width:100,align:'center',  
            formatter:function(value,rec,index){  
            	
                var d = '<a href="javascript:;"  onclick="add(\'timeTable\',\''+ index +'\');return false;">添加</a> &nbsp;&nbsp;';  
                d =d+ '<a href="javascript:;"  onclick="del(\'timeTable\',\''+ index +'\');return false;">删除</a> ';  
                
                return d;  
            }  
          }  ]];
	}
	$('#timeTable').datagrid({
		title:'报价时段',
		height: 200,//$(window).height() - 200,
        //sortName: "rkey",//排序列  
		width:'100%',
		//onClickCell: onClickCell,
		singleSelect: true,
		/**/
		toolbar: [{
			text:"复制",
			iconCls: 'icon-copy',
			handler: function(){
				if(readOnly=="true"){
					return;
				}
				$("#copyDialog").dialog({
					modal:true
				});
				
			}
		},'-'/*,{
			text:"粘贴",
			iconCls: 'icon-copy',
			handler: function(){
				pasteField();
			}
		},'-'*/],
		onSelect:function(index,row){
			commit("powerTable");
			
			if(editIndex != undefined){
				//console.info($('#powerTable').datagrid("getRows"));
				//alert(JSON.stringify($('#powerTable').datagrid("getRows")));
				$('#timeTable').datagrid('updateRow',{
					index: editIndex,
					row: {
						data:$('#powerTable').datagrid("getRows")
					}
				});
			}
			/**/
			if(row['data'] == null || row['data'] == undefined){
				$('#powerTable').datagrid("loadData",[]);
				add("powerTable");
			}else{
				//console.info(row['data']);
				$('#powerTable').datagrid("loadData",row['data']);
			}
			editIndex = index;
			
			
		},
		nowrap: false,
	    striped: true,
		//pagination:true,
		rownumbers : true,
        columns: columns
	});
	$('#timeTable').datagrid().datagrid('enableCellEditing');
	loadTimeTableData();
}
var test = "";
function initPowerTable(){
	var columns;
	//alert(readOnly);
	if(readOnly=="true"){
		if(priceStatus != 1){
			columns = [[ {
	            field: 'power',
	            title: '电力(MW)',
	            sortable:true,
	            width: 100
	        }, {
	            field: 'price',
	            title: '电价(元/MWh)',
	            sortable:true,
	            width: 100
	        }]];
		}else{
			columns = [[{
	            field: 'power',
	            title: '电力(MW)',
	            sortable:true,
	            width: 100
	        }]];
		}
		
	}else{
		columns = [[ {
            field: 'power',
            title: '电力(MW)',
            width: 100,
            sortable:true,
            editor: { type: 'numberbox', options: { 
            	//precision:2
            } }
        }, {
            field: 'price',
            title: '电价(元/MWh)',
            sortable:true,
            editor: { type: 'numberspinner', options: { 
            	increment:10,
            	max:100000000,
            	onChange:function(newValue,oldValue){
            		if(newValue%10 != 0){
            			alert("电价的最小单位是10元/MWh");
            			
            		}
            	}
            	
            } },
            width: 100
        },{field:'opts',title:'操作',width:100,align:'center',  
            formatter:function(value,rec,index){  
                var d = '<a href="javascript:;"  onclick="add(\'powerTable\',\''+ index +'\');return false;">添加</a> &nbsp;&nbsp;';  
                d =d+ '<a href="javascript:;"  onclick="del(\'powerTable\',\''+ index +'\');return false;">删除</a> ';  
                return d;  
            }  
          }  ]];
	}
	$('#powerTable').datagrid({
		title:'电力报价',
		height: 200,//$(window).height() - 200,
        //sortName: "rkey",//排序列  
		width:'100%',
		singleSelect: true,
		remoteSort:false,
		onAfterEdit:function(){
			//alert("ok");
		},
		columns: columns
	});
	$('#powerTable').datagrid().datagrid('enableCellEditing');
	$('#timeTable').datagrid("selectRow",0);
}

/**
 * 检测数据合法性
 * 如果合法则返回true
 */
function checkValid(){
	var rows = $('#timeTable').datagrid("getRows");
	if(status == "new"){
		var startDate = $('#startDate').datebox('getValue');
		//var endDate = $('#endDate').datebox('getValue');
		if(validUtil.isNull(startDate)){
			alert("日期不能为空!");
			return false;
		}
	}
	console.debug(rows);
	if(rows.length>0){
		for(var i = 0;i<rows.length;i++){
			if(validUtil.isNull(rows[i]['startTime']) || validUtil.isNull(rows[i]['endTime'])){
				alert("时段不能为空!");
				return false;
			}
			var data = rows[i]['data'];
			if(data != null){
				for(var j = 0;j<data.length;j++){
					//alert("----");
					if(validUtil.isNull(data[j]['power']) || validUtil.isNull(data[j]['price'])){
						alert("时段"+rows[i]['startTime']+"~"+rows[i]['endTime']+"的电力或者电价不能为空!");
						return false;
					}
					if(!validUtil.isInteger(data[j]['power'])){
						alert("时段"+rows[i]['startTime']+"~"+rows[i]['endTime']+"的电力不能是小数!");
            			return false;
					}
					if(!validUtil.isInteger(data[j]['price'])){
						alert("时段"+rows[i]['startTime']+"~"+rows[i]['endTime']+"的电价不能是小数!");
            			return false;
					}
				}
			}else{
				alert("时段"+rows[i]['startTime']+"~"+rows[i]['endTime']+"的电力电价不能为空!");
    			return false;
			}
		}
	}
	return true;
}


/**
 * 保存数据
 */
function saveData(){
	commit("powerTable");
	commit("timeTable");
	if(!checkValid()){
		return false;
	}
	if(!checkTime()){
		return false;
	}
	var data = JSON.stringify($('#timeTable').datagrid("getRows"));
	//alert(data);
	//alert(dataInfo['time']);
	mask("正在保存..");
	
	var param=null;
	if(status == "new"){
		var startDate = $('#startDate').datebox('getValue');
		//var endDate = $('#endDate').datebox('getValue');
		var endDate = startDate;
		param =  {
				startDate:startDate,
				endDate:endDate,
				time:dataInfo['time'],
				data :data
			};
	}else{
		param =  {
				id:dataInfo['id'],
				time:dataInfo['time'],
				data :data
			};
	}
	var options = {
		url : baseUrl+"/declare/save",
		type : "post",
		data :param,
		dataType : "json",
		success : function(response) {
			var rs = response;
			if (true == rs.status) {
				unmask();
				alert("保存成功");
				if(param['id'] != null && param['id'] != undefined && param['id'] != '' ){
					parent.loadTreeData(param['id']);
				}else{
					if(rs.msg != null){
						parent.loadTreeData(rs.msg);
					}else{
						parent.loadTreeData();
					}
				}
				//parent.location.reload();
				
				parent.closeWin();
			} else {
				unmask();
				if(rs.msg=='先签名后保存！'){
					$("#dialog").dialog();
				}else{
					alert(rs.msg);
				}
				
				
			}
			
		},
		error : function() {
			alert("系统错误");
		}
	};
	$.ajax(options);
}



/**
 * 检测时段是否有重叠
 * 如果合法则返回true
 */
function checkTime(){
	var rows = $('#timeTable').datagrid("getRows");
	
	if(rows.length>0){
		var startTime;
		var endTime;
		for(var i = 0;i<rows.length;i++){
			
			if(rows[i]['startTime'] > rows[i]['endTime']){
				alert("开始时段不能大于结束时段!");
				return false;
			}
			for(var j=0;j<rows.length;j++){
				if(j==i){
					continue;
				}
				startTime = rows[j]['startTime'];
				endTime = rows[j]['endTime'];
				if(rows[i]['startTime'] >startTime&& rows[i]['startTime']<endTime && rows[i]['endTime']>endTime){
					alert("第"+(i+1)+"行时段与第"+(j+1)+"行时段重叠!");
					return false;
				}
				if(rows[i]['startTime'] <startTime&& rows[i]['endTime']>startTime && rows[i]['endTime']<endTime){
					alert("第"+(i+1)+"行时段与第"+(j+1)+"行时段重叠!");
					return false;
				}
			}
			
			
			
			
		}
	}
	return true;
}

/**
 * 
 *复制数据到cookie
 * 车斯剑
 * 2016-9-11
 */
function copyField(){
	commit("powerTable");
	commit("timeTable");
	if(!checkValid()){
		return false;
	}
	var data = JSON.stringify($('#timeTable').datagrid("getRows"));
	//console.debug(data);
	Cookies.set("storeData",data);
	alert("已经复制到剪贴板！");
}

/**
 * 粘贴cookie中数据到
 * 车斯剑
 * 2016-9-11
 */
function pasteField(){
	//alert(Cookies.get("storeData"));
	var data =JSON.parse(Cookies.get("storeData"));
	var count = $('#timeTable').datagrid("getRows").length;
	
	for(var i = 0;i<data.length;i++){
		//console.info(data[i]);
		$('#timeTable').datagrid("insertRow",{
			index: count,	// 索引从0开始
			row: data[i]
		});
	}
}
//关闭弹窗
function closeDialog(){
	$("#copyDialog").dialog("close");
}
/**
 * 车斯剑
 * 复制数据
 */
function copyData(){
	var area = $("#area").val();
	var time = $('#copyTime').datebox('getValue');
	$("#copyDialog").dialog("close");
	 if(time==null){
		 return;
	 }
	 $.ajax({
		 url:"copyData",
		 type : "post",
		 async : false,
		 data:{
			 time :time,
			 area : area
		 },
		 success:function(result){
			 if(result=="null"){
				 alert("没有数据");
			 }else{
				 Cookies.set("storeData",result);
				 pasteField();
				 alert("复制成功！");
			 }
		 },
		 error : function() {
				alert("系统错误");
		}
	});
}
