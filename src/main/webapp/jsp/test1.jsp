<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<Link Rel="StyleSheet"
	Href="${pageContext.request.contextPath }/css/common.css"
	Type="Text/Css">
</head>
<body>
	<div class="_css_main_form" style="width: 48%;margin-left: 2px;">
		<h3 align="center">跨省区电力市场申报单</h3>
		<table class="table" border="0" width="100%">
			<tbody>
				<tr>
					<td align="right" valign="middle" width="20%" class="td_text">申报单位：</td>
					<td align="left" valign="middle" width="30%">四川</td>
					<td align="right" valign="middle" width="20%" class="td_text">申报用户：</td>
					<td align="left" valign="middle" width="30%">四川</td>
				</tr>

				<tr>
					<td align="right" valign="middle" class="td_text">申报交易名称：</td>
					<td align="left" valign="middle"><input placeholder="默认值为卖单"
						id="sheetName" size="20" value=""></td>
					<td align="right" valign="middle" class="td_text">电价(元/Mwh)：</td>
					<td align="left" valign="middle"><input id="price" size="20"
						value=""><span style="color: red;">*</span></td>
				</tr>

				<tr>
					<td align="right" valign="middle" class="td_text">申报电量(Mwh)：</td>
					<td align="left" valign="middle"><input id="buyNum" size="20"
						value=""><span style="color: red;">*</span></td>
					<td align="right" valign="middle" class="td_text">平均电力(MW)：</td>
					<td align="left" valign="middle"></td>
				</tr>
				<tr>
					<td align="right" valign="middle" class="td_text">类型：</td>
					<td align="left" valign="middle"><select id="type"><option
								value="1a" id="1a">全天</option>
							<option value="2a" id="2a">高峰</option>
							<option value="3a" id="3a">低谷</option></select></td>
					<td align="right" valign="middle" class="td_text"></td>
					<td align="left" valign="middle"></td>
				</tr>

				<tr>
					<td align="right" valign="middle" class="td_text">开始时间：</td>
					<td align="left" valign="middle"><input class="Wdate input"
						id="startDate" onfocus="WdatePicker({isShowWeek:true})"
						readonly="readonly" value="2016-06-18" type="text"><span
						style="color: red;">*</span></td>
					<td align="right" valign="middle" class="td_text">结束时间：</td>
					<td align="left" valign="middle"><input class="Wdate input"
						id="endDate" onfocus="WdatePicker({isShowWeek:true})"
						readonly="readonly" value="2016-06-18" type="text"><span
						style="color: red;">*</span></td>
				</tr>

				<tr>
					<td align="right" valign="middle" class="td_text" width="20%">备注：</td>
					<td align="left" valign="middle" colspan="3" width="80%"><textarea
							id="adescr"  cols="63">*</textarea></td>
				</tr>
				<tr>
					<td align="right" valign="middle" class="td_text">签字：</td>
					<td align="left" valign="middle" colspan="3"><input id="arname" size="20"
						value="*"></td>
					
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>