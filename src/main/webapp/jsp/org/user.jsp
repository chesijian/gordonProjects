<%@ page language="java" import="java.util.*,com.state.util.VersionCtrl"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 

<!DOCTYPE html>
<html>
<head>
<title>用户信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style type="text/css">

</style>
<link href="${pageContext.request.contextPath }/css/common.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath }/bootstrap/plugin/css/bootstrap.css" rel="stylesheet" />
<link href="${pageContext.request.contextPath }/bootstrap/plugin/css/bootstrap-responsive.css" rel="stylesheet" />

<link href="${pageContext.request.contextPath }/bootstrap/plugin/css/select2.css" rel="stylesheet" />
<script type="text/javascript" src="${pageContext.request.contextPath }/js/common/common-public-1.0.js?v=<%=VersionCtrl.getVesrsion()%>"></script>

</head>
<body>

	<!--controls-->
	          
	</div>
				
	</div>
					<!--grid sizing-->
					<form class="form-horizontal" style="margin: 0px;" id="form-validate" ">
					      <input type="hidden" id="isSucess" value="${upLoadSucess}">
						  <fieldset>
                             
				 
							<div class="control-group">
								<label class="control-label" for="userId">英文名称</label>
								 <div class="controls">
									<input type="text" class="grd-white" id="userId"
										data-validate="{required: true,isEnglish:true, messages:{required:'用户编号不能为空'}}" />
								</div> 
							</div>
							
							<div class="control-group">
								<label class="control-label" for="mname">中文名称</label>
								<div class="controls">
									 <input type="text" class="grd-white" id="mname"
										data-validate="{required: true, messages:{required:'请输入用户名'}}" />  
										<!-- <input type="file" name="attachment" id="file_1" onchange="upfileCallBack(this)" />  -->
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="drole">用户身份</label>
								<div class="controls">
									<select onchange="initRoles();" id="drole" style="width: 220px;" data-form="select2">
										<option selected="selected" value="">---请选择---</option>
										<option value="buy">买方</option>
										<option value="sale">卖方</option>
										<option value="all">国调</option>
										<option value="region">分中心</option>
									</select>
								</div>

							</div>
							<div class="control-group">
								<label class="control-label" for="role">所属角色</label>
								<div class="controls">
									<select id="role" style="width: 220px;" data-form="select2">
										<option  selected="selected" value="">---请选择---</option>
										

									</select>
								</div>
							</div>
							<div class="control-group">
								<label class="control-label" for="selectArea">所属省份</label>
								<div class="controls">
									<select id="selectArea" data-validate="{required: true, messages:{required:'请选择区域'}}" style="width: 220px;"
										onchange="initDepart();" data-form="select2">
										<option selected="selected" value="">---请选择---</option>

									</select>
								</div>
							</div>
							
							<div id="deptDiv" class="control-group" style="display: none">
								<label class="control-label" for="depart">所属部门</label>
								<div class="controls">
									<select id="depart" style="width: 220px;" data-form="select2">
										<option  selected="selected" value="">---请选择---</option>
									</select>
								</div>
							</div>
							
							
							<c:if test="${status=='new'}">
								<div class="control-group">
									<label class="control-label" for="password">密码</label>
									<div class="controls">
										<input type="password" class="grd-white"
											data-validate="{required: true, messages:{required:'请输入密码'}}"
											name="password" id="password" />
									</div>
								</div>
							<div class="control-group">
								<label class="control-label" for="cpassword">确认密码</label>
								<div class="controls">
									<input type="password" class="grd-white"
										data-validate="{required: true, equalTo: '#password', messages:{required:'请输入确认密码', equalTo: '两次输入的密码不一致'}}"
										name="cpassword" id="cpassword" />
								</div>
							</div>
							
							
							
								
							</c:if>
							 <div class="control-group">
								<label class="control-label" for="autoGraph">电子签名</label>
								<div class="controls" id="imageDiv">
								    <c:if test="${user.autoGraph==null||user.autoGraph==''||status=='new'}">
								       <div id="image">
								           <input type="file" name="autoGraph" size="20" id="autoGraph" onchange="upfileCallBack(this)" />
								       </div>
								      
								    </c:if>
								    
								     <c:if test="${user.autoGraph!=''&&user.autoGraph!=null}">
									     <div style="margin-top: -10px;" id="image">
									         <a href="#" onclick="selectFile();"><img alt="" src="${pageContext.request.contextPath }/manager/getImage?id=${user.id}&sign=false" ></a>  
									         <input type="file" name="autoGraph" id="autoGraph" onchange="upfileCallBack(this)" style="display:none;"/>
									     </div>
								      
								    </c:if>
									
								</div>
							</div> 
							
							<div class="form-actions">
								<button type="submit" class="btn btn-primary">保存</button>
								<button type="button" class="btn" onclick="close();">取消</button>
							</div>

						</fieldset>  
					</form>			
				</div> 
		
			
	<!--/controls-->


	<!-- javascript
        ================================================== -->
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/jquery.js"></script>
    
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/jquery-ui.min.js"></script>
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/bootstrap.js"></script>

	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/select2/select2.js"></script>

	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/validate/jquery.validate.js"></script>
	<!-- -->
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/validate/jquery.metadata.js"></script>


	<script src="${pageContext.request.contextPath }/js/org/user.js"></script>
   
    <script type="text/javascript" src="${pageContext.request.contextPath }/jsp/upload/ajaxfileupload.js"></script>
   
	<script type="text/javascript">
	var status = "${status}";
	var userInfo = ${userInfo};
	var fileNum = 0;
	function GetUrlParam(name) {
		var reg = new RegExp("(^|\\?|&)" + name + "=([^&]*)(\\s|&|$)", "i");
		if (reg.test(location.href))
			return unescape(RegExp.$2.replace(/\+/g, " "));
		return "";
	};
	
	function selectFile() {
		
		var ie = navigator.appName == "Microsoft Internet Explorer" ? true : false;
		if (ie) {
			//document.getElementById("file_1").click(); 
			var a = document.createEvent("MouseEvents");//FF的处理 
			a.initEvent("click", true, true);
			document.getElementById("autoGraph").dispatchEvent(a);
			
		} else {
			var a = document.createEvent("MouseEvents");//FF的处理 
			a.initEvent("click", true, true);
			document.getElementById("autoGraph").dispatchEvent(a);
		}

	}
	
	function upfileCallBack(obj) {
		upLoad(obj);
	}
	
	/*上传电子签字图片*/
	function upLoad(obj) {
		
		var mName = document.getElementById("mname").value;
		$.ajaxFileUpload({
			url : 'upload?mName='+mName,
			secureuri : false,
			fileElementId : 'autoGraph',
			dataType : 'json',
			data : {
				//formId : document.getElementById("extraParams").value
			},
			success : function(data) {
				if (data.status == true) {
					 alert(data.msg);
					 var name = document.getElementById("mname").value;
					 var content ="<div style='margin-top: -10px;' id='image'>"+
					 "<a href='#' onclick='selectFile();'><img alt='点击更换' src='${pageContext.request.contextPath }/manager/getImage?sign=false&mname="+name+"&ran="+Math.random()+"' style='height: 50px; width: 100px;'></a>"+
					 " <input type='file' name='autoGraph' id='autoGraph' onchange='upfileCallBack(this)' style='display:none;'/></div>";
					 $('#imageDiv').empty();
					 $('#imageDiv').html(content);
					 $("#isSucess").val(data.upLoadSucess);
				}else {
					alert(data.msg);
					$("#isSucess").val(data.upLoadSucess);
					//parent.alert(data.msg);
				}
			},
			error : function(data, status, e) {
				parent.alert("导入失败！error");
			}
		});
	}
	//验证英文
	$.validator.addMethod("isEnglish", function(value, element){
		return this.optional(element) ||/^[a-zA-Z]+$/.test(value) || value.indexOf("-")>-1;
	}, "只能输入英文字母"); 
	</script>
</body>
</html>