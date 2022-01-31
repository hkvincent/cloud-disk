<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript" src='./js/jquery.min.js'></script>
<title>LIGHTBOX EXAMPLE</title>
<style>
.black_overlay {
	display: none;
	position: absolute;
	top: 0%;
	left: 0%;
	width: 100%;
	height: 100%;
	background-color: black;
	z-index: 1001;
	-moz-opacity: 0.8;
	opacity: .80;
	filter: alpha(opacity =           80);
}

.white_content {
	display: none;
	position: absolute;
	top: 25%;
	left: 25%;
	width: 50%;
	height: 50%;
	padding: 16px;
	border: 16px solid orange;
	background-color: white;
	z-index: 1002;
	overflow: auto;
}
</style>
<script type="text/javascript">
	$(document).click(function() {
		
		$('#fade').hide();
		$("#light").hide();
	});
</script>

</head>
<body>
	<p>
		可以根据自己要求修改css样式<a href="javascript:void(0)"
			onclick="document.getElementById('light').style.display='block';document.getElementById('fade').style.display='block'">点击这里打开窗口</a>
	</p>
	<div id="light" class="white_content">
		This is the lightbox content. 
		
		
		<a href="javascript:void(0)"
			onclick="document.getElementById('light').style.display='none';document.getElementById('fade').style.display='none'">
			Close</a>
	</div>
	<div id="fade" class="black_overlay"></div>
</body>
</html>


