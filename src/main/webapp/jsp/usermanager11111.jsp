<%@ page language="java" import="java.util.*,com.state.util.VersionCtrl" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.state.com/state" prefix="state"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 

<state:override name="head">
	<title>用户管理</title>
	<link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/style.css?rnd=<%=Math.random()%>" Type="Text/Css">
	<script type="text/javascript">
	$(function(){
		$('.menu1').find('a[name=用户管理]').attr('class', 'menufocus');
	});
	//注册按钮事件
	function approve(user){
		var pd=confirm("您确定要对用户："+user+"审批吗？");
		if(pd==true){
			var option = {
					url : "${pageContext.request.contextPath }/manager/approveUser",
					type : "post",
					data : {
						user : user,
					},
					dataType : "json",
					success : function(response) {
						if ("success" == response) {
							window.location.href = "${pageContext.request.contextPath }/manager/init";
						}else {
							alert("审批失败");
						}
					},
					error : function() {
						alert("系统错误");
					}
				};
				$.ajax(option);
		}
	}
	
</script>
</state:override>
<state:override name="content">
	<div>
			<div class="mid">
				<div class="contop">
					<div class="fl"><span class="xmenu">用户管理</span><span class="count">未审批:${fn:length(nopass)},已审批:${fn:length(pass)}</span></div>
					<div class="cl"></div>
				</div>
				<div>
					<div class="fl bd1 ustb">
						<div class="usyes">未审批通过</div> <div class="usno">审批通过</div>
						<div class="cl"></div>
						<div class="fl">
							<table width="580" cellpadding="0" cellspacing="0">
								<thead>
									<th width="25%">用户名</th>
									<th width="25%">地区</th>
									<th width="25%">权限</th>
									<th width="25%">状态</th>
								</thead>
								<c:forEach var="n" items="${nopass}" varStatus="status">
									<tr <c:if test="${status.count%2==0}">class="bgh"</c:if>>
									<td>${n.mname}</td>
									<td>${n.area}</td>
									<c:if test="${n.drole=='buy'}">
									<td style="color: red">买家</td>
									</c:if>
									<c:if test="${n.drole=='sale'}">
									<td  style="color: green">卖家</td>
									</c:if>
									<td><input type="button" class="btn4" value="审批" onclick="approve('${n.mname}')"/></td>
								</tr>
								</c:forEach>
							</table>
						</div>
						<div class="fl pdl10">
							<table width="580" cellpadding="0" cellspacing="0">
								<thead>
									<th width="25%">用户名</th>
									<th width="25%">地区</th>
									<th width="25%">权限</th>
									<th width="25%">状态</th>
									
								</thead>
								<c:forEach var="n" items="${pass}" varStatus="status">
									<tr <c:if test="${status.count%2==0}">class="bgh"</c:if>>
									<td>${n.mname}</td>
									<td>${n.area}</td>
									<c:if test="${n.drole=='buy'}">
									<td style="color: red">买家</td>
									</c:if>
									<c:if test="${n.drole=='sale'}">
									<td  style="color: green">卖家</td>
									</c:if>
									<td><a class="btn5">审批已通过</a></td>
								</tr>
								</c:forEach>
							</table>
						
						<div class="cl"></div>
					</div>
				</div>
			    
			</div>
		</div>
</state:override>

<%@ include file="/common/block/block.jsp" %>

