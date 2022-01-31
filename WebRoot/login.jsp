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
<!--Google Fonts
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
			<h1>LOGIN FORM</h1>
			<form action="${proPath}/UserServlet?method=login" method="post"
				onsubmit="return toVaild()">
				<input type="text" name="userName" onfocus="this.value = '';">
				<input type="password" name="password" onfocus="this.value = '';">
				<table border="0" align="center">
					<tr>
						<td><input size="3" type="text" id="checkCode"
							name="checkCode" maxlength="4" placeholder="code"
							onfocus="this.placeholder = '';"
							onblur="this.placeholder = 'code'" /></td>
					</tr>
					<tr>
						<td colspan="3" rowspan="1" align="right" id="codeImage"><img
							alt="invalid" src="image.jsp" onclick="showmore()"></td>
						<input hidden="true" type="text" id="deCode">
						<td align="right" id="res"></td>
					</tr>
				</table>
				<div class="forgot">
					<c:if test="${requestScope.msg != null}">
						<p>
							<font color="#FF0000">${requestScope.msg}</font>
						</p>
					</c:if>
					<a href="#">forgot Password</a> <input type="submit" value="Login">
				</div>
			</form>
		</div>
		<div class="login-bottom">
			<h3>
				New User &nbsp;<a href="${proPath}/register.jsp">Register</a>&nbsp
				Here
			</h3>
		</div>
	</div>
	<div class="copyright">
		<p>Copyright &copy; 2015.Company name All rights reserved.</p>
	</div>
</body>

<script type="text/javascript">
	function trim(str) {
		str = str.replace(/^\s*/, "");
		str = str.replace(/\s*$/, "");
		return str;
	}

	function createAJAX() {
		var ajax = null;
		try {
			ajax = new ActiveXObject("microsoft.xmlhttp");
		} catch (e1) {
			ajax = new XMLHttpRequest();
		}
		return ajax;
	}
</script>
<script type="text/javascript">
	window.onload = function() {
		document.getElementById("checkCode").onkeyup = function() {
			var checkCode = this.value;
			checkCode = trim(checkCode);
			if (checkCode.length == 4) {
				var ajax = createAJAX();
				var method = "POST";
				var uri = "${pageContext.request.contextPath}/UserServlet?method=check&time="
						+ new Date().getTime();
				ajax.open(method, uri);
				ajax.setRequestHeader("content-type",
						"application/x-www-form-urlencoded");
				var content = "checkCode=" + checkCode;
				ajax.send(content);

				ajax.onreadystatechange = function() {
					if (ajax.readyState == 4) {

						if (ajax.status == 200) {
							var tip = ajax.responseText;
							var img = document.createElement("img");
							img.src = tip;
							img.style.width = "28px";
							img.style.height = "28px";
							var td = document.getElementById("res");
							var deCode = document.getElementById("deCode");
							deCode.value = "no";
							if (tip == "images/MsgSent.png")
								deCode.value = "yes";
							td.innerHTML = "";
							td.appendChild(img);
						}
					}

				}
			} else {
				var td = document.getElementById("res");
				td.innerHTML = "";
			}
		}
	}

	function showmore() {
		var codeImage = document.getElementById("codeImage");
		var img = document.createElement("img");
		img.src = "image.jsp?id=" + new Date().getTime();
		img.setAttribute("onclick", "showmore()");
		codeImage.innerHTML = "";
		codeImage.appendChild(img);
		var td = document.getElementById("res");
		td.innerHTML = "";
	}

	function toVaild() {
		var val = document.getElementById("deCode").value;
		if (val == "yes") {
			return true;
		} else {
			alert("code error");
			return false;
		}
	}
</script>
</html>