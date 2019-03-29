<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.state.com/state" prefix="state" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html >
<head>
	<script src="${pageContext.request.contextPath }/js/jquery/jquery-1.8.3.min.js" type="text/javascript" ></script>
	<script src="${pageContext.request.contextPath }/js/jquery/jquery.form.js" type="text/javascript" ></script>
	<script src="${pageContext.request.contextPath }/js/jquery/jquery.json.min.js" type="text/javascript" ></script>
	<Link Rel="StyleSheet" Href="${pageContext.request.contextPath }/css/head.css" Type="Text/Css">
	<state:block name="head"></state:block>
</head>
<body>
	
	<state:block name="header"></state:block>

	<state:block name="content"></state:block>

	<state:block name="footer"></state:block>
</body>
</html>
