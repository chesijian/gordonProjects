<%@ page language="java" import="java.util.*,com.state.util.VersionCtrl,com.state.security.EncryptUtil"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 

<!DOCTYPE html>
<html>
<head>
<title>修改密码</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<style type="text/css">
html{
	height:100%
}
</style>
<link
	href="${pageContext.request.contextPath }/css/common.css"
	rel="stylesheet" />
<link
	href="${pageContext.request.contextPath }/bootstrap/plugin/css/bootstrap.css"
	rel="stylesheet" />
<link
	href="${pageContext.request.contextPath }/bootstrap/plugin/css/bootstrap-responsive.css"
	rel="stylesheet" />


<script type="text/javascript" src="${pageContext.request.contextPath }/js/common/common-public-1.0.js?v=<%=VersionCtrl.getVesrsion()%>"></script>
    
</head>
<body>

	<!--controls-->
	
				<div class="box-body" style="margin-top: 10px;">
					<!--grid sizing-->
					<form class="form-horizontal" id="form-validate">
						<fieldset>
								
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
								
						
							
							<div class="form-actions">
								<button type="submit" class="btn btn-primary">保存</button>
							</div>

						</fieldset>
					</form>
					<!--/grid sizing-->
				</div>
				<!--/box body-->
			
	<!--/controls-->

	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/jquery.js"></script>
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/jquery-ui.min.js"></script>
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/bootstrap.js"></script>
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/validate/jquery.validate.js"></script>
	<script
		src="${pageContext.request.contextPath }/bootstrap/plugin/js/validate/jquery.metadata.js"></script>

	<script type="text/javascript">
	
	$(document).ready(function() {
		$("#form-validate").validate({
			success : "valid",
			submitHandler : function() {
				if(!valiPassword()){
					saveData();
				}else{
					alert("新密码和原密码重复");
				}
				
			}
		});
		
	});
	
	function valiPassword(){
		var result;
		var password = $("#password").val();
		var data={password:password};
		data['id'] = "${id}";
		$.ajax({
			url : baseUrl+"/manager/valiPassword",
			type : "post",
			async : false,
			data : data,
			dataType : "json",
			success : function(response) {
				result= response;
			},
			error : function() {
				alert("系统错误");
			}
		});
		return result;
	}
	
	function saveData(){
		var password = $("#password").val();
		var data={mkey:password};
		mask("正在修改..");
		data['id'] = "${id}";
		var options = {
			url : baseUrl+"/manager/updateUser",
			type : "post",
			data : data,
			dataType : "json",
			success : function(response) {
				var rs = jQuery.parseJSON(response);
				if (true == rs.status) {
					unmask();
					alert("修改成功");
					parent._win.close();
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
	</script>
</body>
</html>