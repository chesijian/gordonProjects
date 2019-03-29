/**
 * 
 */
function close() {
	parent.d.close();
}

function validUser() {
	var option = {
		url : baseUrl + "/login/containUser",
		type : "post",
		data : {
			user : user
		},
		dataType : "json",
		success : function(response) {
			if ("fail" == response) {
				alert("此帐户已存在");
				return false;
			} else if ("noapprove" == response) {
				alert("此帐户未审核");
			} else {

			}
		},
		error : function() {
			alert("检查用户是否存在系统出错");
		}
	};
	$.ajax(option);
}
$(document).ready(function() {
	// try your js
	mask();
	$('[data-form=select2]').select2();
	if(status == "edit"){
		initData();
	}
	//initRoles();
	initArea();
	// validate form
	// $('#form-validate').validate();
	$("#form-validate").validate({
		success : "valid",
		submitHandler : function() {
			saveData();
		}
	});
	unmask();
	
});
function initData(){
	if(!validUtil.isNull(userInfo)){
		$("#userId").val(userInfo['userId']);
		$("#mname").val(userInfo['mname']);
		$("#selectArea").select2().val(userInfo['area']).trigger("change");
		$('#drole').select2().val(userInfo['drole']).trigger("change");
	}
}
function saveData(){
	
	var userId = $("#userId").val();
	var mname = $("#mname").val();
	var password = $("#password").val();
	var area = $("#selectArea  option:selected").val();
	var drole = $("#drole  option:selected").val();
	var role = $("#role  option:selected").val();
	var depart = $("#depart  option:selected").val();//新加
	var autoGraph = $("#isSucess").val();
	
	var data = {};
	
	if(status == "edit"){
		
		if($("#isSucess").val()=='false'||$("#isSucess").val()==''){
			alert('请重新上传签名！');
			return;
		}
		var isEdit=false;
		if(userId !=userInfo['userId']){
			isEdit = true;
			data['userId'] = userId;
		}
		if(mname !=userInfo['mname']){
			isEdit = true;
			data['mname'] = mname;
		}
		if(area !=userInfo['area']){
			isEdit = true;
			data['area'] = area;
		}
		if(drole !=userInfo['drole']){
			isEdit = true;
			data['drole'] = drole;
		}
		if(role !=userInfo['role']){
			isEdit = true;
			data['role'] = role;
		}
		if(depart !=userInfo['depart']){
			isEdit = true;
			data['depart'] = depart;
		}
		
		
		if(!isEdit){
			//alert("您没有修改任何内容!");
			//return ;
		}
		data['id'] = userInfo['id'];
	}else{
		if(validUtil.isNull(role)){
			alert("请选择角色");
			return;
		}
		if(validUtil.isNull(area)){
			alert("请选择区域");
			return;
		}
		if(validUtil.isNull(drole)){
			alert("请选择身份");
			return;
		}
		
		if(autoGraph=='false'){
			alert("请选择电子签名");
			return;
		}
		data['userId'] = userId;
		data['mname'] = mname;
		data['area'] = area;
		data['drole'] = drole;
		data['role'] = role;
		data['mkey'] = password;
		data['depart'] = depart;//新增
	}
	
	mask("正在保存..");
	
	var options = {
		url : baseUrl+"/manager/updateUser",
		type : "post",
		data : data,
		dataType : "json",
		success : function(response) {
			var rs = jQuery.parseJSON(response);
			if (true == rs.status) {
				unmask();
				alert("保存成功");
				parent.location.reload();
				close();
			} else {
				unmask();
				alert(rs.msg);
			}
			
		},
		error : function() {
			alert("系统错误");
		}
	};
	$.ajax(options);

}
/**
 * 加载角色数据
 */
function initRoles(){
	var drole = $("#drole  option:selected").val();

	var type;
	if(drole=="all"){
		type="GD";
	}else if(drole="region"){
		type="FZX";
	}else{
		type="SD";
	}
	var drole = $("#role  option").remove();
	$.ajax({  
        type:"POST",  
        url:baseUrl+"/org/orgRole/getRoles",  
        dataType:"json", 
        data:{
        	type:type
        },
        success:function(data){ 
        	$("<option value='' selected='selected'>---请选择---</option>").appendTo("#role");
        	if(!validUtil.isNull(data)){
        		var rs = jQuery.parseJSON(data);
        		if(rs != null && rs.length > 0){ 
        			for(var i = 0; i< rs.length; i++){  
        				if(rs[i]['roleId'] == userInfo["role"]){
        					$("<option value='"+rs[i]['roleId']+"' selected='selected'>"+rs[i]['roleName']+"</option>").appendTo("#role");
        				}else{
        				     $("<option value='"+rs[i]['roleId']+"'>"+rs[i]['roleName']+"</option>").appendTo("#role");      
        				}
                    } 
        			$('#role').select2().val(userInfo['role']).trigger("change");
        		}
        	} 
        },  
    });  
	
}

/**
 * 加载部门数据
 */
function initDepart(){
	
	var area = $("#selectArea  option:selected").val();
	if(area!="国调"){
		document.getElementById("deptDiv").style.display="none";
		$("#depart option").remove();
		return;
	}
	document.getElementById("deptDiv").style.display="";
	$.ajax({  
        type:"POST",  
        url:baseUrl+"/org/orgDepart/getDeparts",  
        dataType:"json",  
        success:function(data){ 
        	if(!validUtil.isNull(data)){
        		var rs = jQuery.parseJSON(data);
        		if(rs != null && rs.length > 0){ 
        			for(var i = 0; i< rs.length; i++){  
        				if(rs[i]['departId'] == userInfo["depart"]){
        					
        					$("<option value='"+rs[i]['departId']+"' selected='selected'>"+rs[i]['departName']+"</option>").appendTo("#depart");
        				}else{
        				     $("<option value='"+rs[i]['departId']+"'>"+rs[i]['departName']+"</option>").appendTo("#depart");      
        				}
                    } 
        			$('#depart').select2().val(userInfo['departName']).trigger("change");
        		}
        	} 
        },  
    });  
	
}

/**
 * 加载省份数据
 */
var regionArr =['华北','华东','华中','东北','西北','西南']
function initArea(){
	$.ajax({  
        type:"POST",  
        url:baseUrl+"/area/getAllAreas",  
        dataType:"json", 
        success:function(data){ 
        	if(!validUtil.isNull(data)){
        		var rs = jQuery.parseJSON(data);
        		for(var i =0;i<regionArr.length;i++){
        			rs.push({area:regionArr[i]});
        		}
        		
        		if(rs != null && rs.length > 0){ 
        			for(var i = 0; i< rs.length; i++){  
        				if(rs[i]['area'] == userInfo["area"]){
        					$("<option value='"+rs[i]['area']+"' selected='selected'>"+rs[i]['area']+"</option>").appendTo("#selectArea");
        				}else{
        				     $("<option value='"+rs[i]['area']+"'>"+rs[i]['area']+"</option>").appendTo("#selectArea");      
        				}
                    } 
        			$('#selectArea').select2().val(userInfo['area']).trigger("change");
        		}
        	} 
        },  
    });  
	
}