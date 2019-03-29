/**
 * 
 */

$(function(){
		 $('.menu1').find('a[name=系统管理]').attr('class', 'menufocus');
		 $('#myTab li:eq(0) a').tab('show');
		 $('#myTab li:eq(0)').trigger("click");
		 $('#myTabContent').height($(document).height() - 290);
		 //$('#myTabContent').height(400);
		// $('#table_accredit').height($('#myTabContent').height()-20);
		 initLogTable();
         initLoginTable();
         initRoleTable();
         initTreeData();

});
function getLoginParams(params){
	//console.info(params);
	return {
		limit: params.limit,
         pageIndex: params.offset,
		startTime:$('#startTime').val(),
		endTime:$('#endTime').val()
	};
}
/**
 * 初始化登录记录
 */
function initLoginTable(){
	var currPageIndex = 1,currLimit=50;
	$('#loginData').bootstrapTable({
		url: "getLoginData", 
		height: $(document).height() - 200,//$(window).height() - 200,
        striped: true,//條紋行  
        sidePagination: "server",//服务器分页  
        //showRefresh: true,//刷新功能  
        //search: true,//搜索功能 
        //cache: false,   
        clickToSelect: true,//选择行即选择checkbox  
        singleSelect: true,//仅允许单选  
        search: false, //不显示 搜索框
        showColumns: false, //不显示下拉框（选择显示的列）
        //searchOnEnterKey: true,//ENTER键搜索  
        pagination: true,//启用分页  
        escape: true,//过滤危险字符  
        queryParams:  getLoginParams,//携带参数  
        pageCount: 10,//每页行数  
        pageIndex: 0,//其实页  
        method: "post",//请求格式  
        contentType: "application/x-www-form-urlencoded",
        pageSize: 50,
        pageList: [10, 50, 100, 200, 500],
        onPageChange: function (number, size) {  
            currPageIndex = number;  
            currLimit = size ;
            $('#loginData').bootstrapTable('resetView',{
            	height: $(document).height() - 190
            });
        }, 
        onRefresh:function (data)  {
        	$('#loginData').bootstrapTable('resetView',{
            	height: $(document).height() - 190
            });
        },
        onLoadSuccess: function (data)  {}, 
        onLoadError: function () {
        },/**/
        columns: [{
        	field: 'Number',
        	title: '序号',
        	formatter: function (value, row, index) {
        	return (currPageIndex== undefined?0:(currPageIndex-1)*currLimit)+index+1;
        	}
        	}, {
            field: 'userName',
            title: '用户名'
        }, {
            field: 'ip',
            title: 'IP地址'
        }, {
            field: 'createTime',
            title: '登录时间'
        }]
	});
	$('#home .fixed-table-body').height( $(document).height() - 270);
	//$('.fixed-table-body').hide();
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

/**
 * 初始化日志
 */
function initLogTable(){
	var currPageIndex = 1,currLimit=50;
	$('#operateData').bootstrapTable({
		url: "getLogData", 
		height: $(document).height() - 265,//$(window).height() - 200,
        striped: true,//條紋行  
        sidePagination: "server",//服务器分页  
        clickToSelect: true,//选择行即选择checkbox  
        singleSelect: true,//仅允许单选  
        search: false, //不显示 搜索框
        showColumns: false, //不显示下拉框（选择显示的列）
        pagination: true,//启用分页  
        escape: true,//过滤危险字符  
        queryParams:  getLoginParams,//携带参数  
        pageCount: 10,//每页行数  
        pageIndex: 0,//其实页  
        method: "post",//请求格式  
        contentType: "application/x-www-form-urlencoded",
        pageSize: 50,
        pageList: [10, 50, 100, 200, 500],
        onPageChange: function (number, size) {  
            currPageIndex = number;  
            currLimit = size ;
            $('#operateData').bootstrapTable('resetView',{
            	height: $(document).height() - 190
            });
        }, 
        onRefresh:function (data)  {
        	$('#operateData').bootstrapTable('resetView',{
            	height: $(document).height() - 190
            });
        },
        onLoadSuccess: function (data)  
        {  
        }, 
        onClickRow: function (item, e) {
        	var content="<div style='padding:10px;'><p>操作用户："+item['addNameCn']+"</p>";
        	content += "<p>操作时间："+item['createTime']+"</p>";
        	content += "<p>操作服务："+item['className']+"</p>";
        	content += "<p>操作方法："+item['operName']+"</p>";
        	content += "<p>操作参数：<div style='width:100%;height:100px;overflow-y: auto;'>"+item['operParams']+"</div></p>";
        	content += "<p>结果信息：<div style='width:100%;height:100px;overflow-y: auto;'>"+item['resultMsg']+"</div></p></div>";
        	createDivWin(content,500,410,"日志详情");
        	//console.info(item);
        },
        onLoadError: function () {
        },/**/
        columns: [{
        	field: 'Number',
        	title: '序号',
        	formatter: function (value, row, index) {
        	return (currPageIndex== undefined?0:(currPageIndex-1)*currLimit)+index+1;
        	}
        	}, {
            field: 'addNameCn',
            title: '用户名'
        }, {
            field: 'ip',
            title: 'IP地址'
        }, {
            field: 'createTime',
            title: '登录时间'
        }, {
            field: 'modelName',
            title: '模块'
        }, {
            field: 'operNameCn',
            title: '操作'
        }, {
            field: 'opreResult',
            title: '结果',
        	formatter: function (value, row, index) {
            	if(row['operResult'] == "success"){
            		return "<a class=\"btn5\">成功</a>";
            	}else{
            		return "<a class=\"btn4\">失败</a>";
            	}
            }
        }]
	});
}

function changeTab(tabId){
	//alert("ok");
	selectTabId = tabId;
	if(tabId=="accreditData"||tabId=="usersData"){
		$(".input-append").hide();
	}else{
		$(".input-append").show();
	}
}

/**
 * 点击查询
 */
function changeTime(){
	//mask();
	$('#'+selectTabId).bootstrapTable('selectPage',1);
	$('#'+selectTabId).bootstrapTable('refresh');
}
//-----------------------------------------------------------------------------
/**
 * 权限树数据加载
 */
var on_off=true;
function initTreeData(){
      $.ajax({
				url : 'getResourcesList',
				type : 'POST',
				dataType : 'json',
				success : function(data) {
					//alert(JSON.stringify(data));
					$("#right_menu").tree({
						data:data,
						checkbox:true,
						cascadeCheck:false,
						//height: $(document).height()-450,
						animate:true,
						onCheck:function(node,checked){                 //当点击 checkbox 时触发
							if(on_off){
								on_off=false;
								var parentNode = $("#right_menu").tree('getParent', node.target);
								var childNode = $("#right_menu").tree('getChildren', node.target);
								if (checked) {
									checkParentNode(node);
				                    on_off=true;
				                    if (childNode.length > 0) {
				                        for (var i = 0; i < childNode.length; i++) {
				                            $("#right_menu").tree('check', childNode[i].target);
				                        }
				                    }
				                    
				                } else {
				                	on_off=true;
				                	if(parentNode!=null&&!checkChildNode(parentNode)){
				                		//关联递归取消选中父节点
				                			//$("#right_menu").tree('uncheck', parentNode.target);
				                    		//console.debug(parentNode);
				                		
				                	}
				                	
				                    if (childNode.length > 0) {
				                        for (var i = 0; i < childNode.length; i++) {
				                            $("#right_menu").tree('uncheck', childNode[i].target);
				                        }
				                    }
				                }
							}
							
				        }
					});
					$("#right_menu").tree("loadData",data);
				}
      });
}
/**
 * 选中父节点
 * @param node
 * @param checked
 */
function checkParentNode(node){
	var parentNode = $("#right_menu").tree('getParent', node.target);
	if(parentNode!=null){
		$("#right_menu").tree('check', parentNode.target);
		checkParentNode(parentNode);
	}
}
/**
 * 判断子节点中是否有选中
 * @param node
 * @returns {Boolean}
 */
function checkChildNode(node) {
	var bnode = false;
	var childNode = $("#right_menu").tree('getChildren', node.target);
	if (childNode.length > 0) {
        for (var i = 0; i < childNode.length; i++) {
            if(childNode[i].checked==true){
            	bnode = true;
            	break;
            }
        }
    }
	return bnode;
};

/**
 * 初始化角色列表
 */
function initRoleTable(){
	var editRow = undefined;
	var columns;
	columns = [[ {
        field: 'roleName',
        title: '角色列表',
        width: "100%"
    }]];
	
	$('#leftTable').datagrid({
		//title:'角色列表',
		//height: $(document).height()-300,
		//height:'100%',
        //sortName: "rkey",//排序列  
		width:'100%',
		//onClickCell: onClickCell,
		singleSelect: true,
		onClickRow:function onClickRow(index,row){
			type_ = row['type'];
			initChecked(row['id']);			
		},
		nowrap: false,
	    striped: true,
		//pagination:true,
		rownumbers : true,
        columns: columns,
        data:timeData
        
	});
	
	$('#leftTable').datagrid("loadData",timeData);
	$('#leftTable').show();
}


/**
 * 点击角色传值
 */
function initChecked(roleUid) {
	//alert(roleUid);
	var nodes = $('#right_menu').tree('getChecked');
	for(var i=0;i<nodes.length;i++){
		$('#right_menu').tree('uncheck',nodes[i].target);
	}
	$("#roleUid").val(roleUid);
	$.ajax({
		url :baseUrl+'/system/getRoleResourcesList',
		type : 'POST',
		dataType : 'json',
		data : {
			roleUid : roleUid
		},
		success : function(data) {		
			if(!validUtil.isNull(data)){
				var rs = jQuery.parseJSON(data);
				
				for ( var i = 0; i < rs.length; i++) {
					var node = $('#right_menu').tree('find',rs[i].resourceUid);
					if (node) {
						on_off=false;
						$('#right_menu').tree('check', node.target);
						on_off=true;
					}
				}
			}
		}
	});
}
/**
 * 保存角色权限数据
 */
function saveData() {

	var roleUid =$("#roleUid").val();
	if(roleUid==""){
		alert("请选择角色");
		return;
	}
	var ids = null;
	var data = new Array();
	var resourcesData = new Array();

	var checkedNodes = $('#right_menu').tree('getChecked');
	for(var i=0;i<checkedNodes.length;i++){
		data.push(checkedNodes[i]['id']);
		resourcesData.push(checkedNodes[i]['attributes'].rname);
		
	}
	console.info(checkedNodes);
	ids = data.join(",");
	var resourcesIds = resourcesData.join(",");
	
	mask("正在保存..");
	$.ajax({
		url : baseUrl+"/system/saveRoleResources",
		type : "post",
		dataType : "json",
		data : {
			roleUid:roleUid,
			ids : ids,
			resourcesIds:resourcesIds
		},
		success : function(response) {
			var rs=response;
			if (rs.status) {
				unmask();
				alert("保存成功");
			} else {
				unmask();
				alert(rs.msg);
			}			
		},
		error : function() {
			alert("系统错误");
		}
	});
	
	
}
