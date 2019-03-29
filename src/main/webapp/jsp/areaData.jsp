<%@ page language="java" import="java.util.*,com.state.util.VersionCtrl" pageEncoding="UTF-8"
	contentType="text/html; charset=utf-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<thead>
	<th width="7%" class="cgr">地区</th>
	<th width="7%" class="cgr">申报电量</th>
	<th width="7%" class="cgr">出清电量</th>
	<th width="7%" class="cgr">结算电量</th>
	<th width="7%" class="cgr">出清费用</th>
	<th width="7%" class="cgr">结算费用</th>
	<th width="7%" class="cgr"><font color="#f44336">'+ts+'</font></th>
	<th width="7%" class="cgr">说明</th>
</thead>
<c:forEach var="n" items="${list}" varStatus="status">
	<tr>
		<td width="7%">${n.area}</td>
		<td width="7%">${n.sumq}</td>
		<td width="7%">${n.cqq}</td>
		<td width="7%">/</td>
		<td width="7%">${n.cqp}</td>
		<td width="7%">/</td>
		<td width="7%" align="center"><font color="#f44336">${n.year}</font></td>
		<td width="7%"></td>
	</tr>
</c:forEach>

<script type="text/javascript">
	$(function(){
		$("#pagination").twbsPagination({
			totalPages:${pageResult.totalPage},
			currentPage:${pageResult.currentPage},
			onPageClick : function(event, page) {
				$("#currentPage").val(page);
				$("#searchForm").submit();
			}
		});
	});
</script>