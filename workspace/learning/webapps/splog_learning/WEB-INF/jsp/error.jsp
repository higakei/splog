<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="error" class="jp.co.hottolink.splogfilter.learning.web.servlet.entity.ErrorEntity" scope="request" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>splog learning - エラー</title>
</head>
<body>
	<div><c:out value="${error.message}" /></div>
	<br />
	<pre>原因：<c:out value="${error.cause}" /></pre>
	<br />
	<pre><c:out value="${error.stackTrace}" /></pre>
	<br />
	<div>
		<a href="javascript:history.back();">戻る</a>
		<a href="/splog_learning">トップ</a>
	</div>
</body>
</html>
