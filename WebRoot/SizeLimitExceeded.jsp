<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="proPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>vindisk</title>
<link rel="shortcut icon" href="images/logo_small.png">
<link rel="stylesheet" href="css/reset.css">
<link rel="stylesheet" href="css/bootstrap.css">
<script type="text/javascript"
	src='<c:url value="/js/jquery.min.js"></c:url>'></script>


</head>

<body>
	your update file is over size.
	<a href="${proPath }/index.jsp">back to home page</a>
</body>

</html>