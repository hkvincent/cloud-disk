<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="proPath" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>Home</title>
<!-- Custom Theme files -->
<link href="css/style.css" rel="stylesheet" type="text/css" media="all" />
<!-- Custom Theme files -->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="keywords"
	content="Login form web template, Sign up Web Templates, Flat Web Templates, Login signup Responsive web template, Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyErricsson, Motorola web design" />
<!--Google Fonts-->
<link
	href='http://fonts.useso.com/css?family=Roboto:500,900italic,900,400italic,100,700italic,300,700,500italic,100italic,300italic,400'
	rel='stylesheet' type='text/css'>
<link
	href='http://fonts.useso.com/css?family=Droid+Serif:400,700,400italic,700italic'
	rel='stylesheet' type='text/css'>
<!--Google Fonts-->
</head>
<body>
	<div class="login">
		<h2>Vindisk</h2>
		<div class="login-top">
			<h1>Register</h1>
			<form action="${proPath}/UserServlet?method=register" method="post">

				<input type="text" name="userName" onfocus="this.value = '';">
				<input type="password" name="password" onfocus="this.value = '';">
				<div class="forgot">
					<input type="submit" value="sign up">
				</div>
			</form>

		</div>
		<div class="login-bottom">
			<h3>
				Old User &nbsp;<a href="${proPath}/login.jsp">Login</a>&nbsp Here
			</h3>
		</div>
	</div>
	<div class="copyright">
		<p>
			Copyright &copy; 2017.vincent All rights reserved.<a
				target="_blank" href="http://sc.chinaz.com/moban/">&#x7F51;&#x9875;&#x6A21;&#x677F;</a>
		</p>
	</div>
</body>
</html>