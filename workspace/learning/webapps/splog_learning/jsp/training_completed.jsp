<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="javax.servlet.http.Cookie" %>
<%@page import="jp.co.hottolink.splogfilter.learning.logic.training.TrainingEntity" %>
<%@page import="jp.co.hottolink.splogfilter.learning.web.util.CookieUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	TrainingEntity training = null;
	Cookie cookie = null;

	try {
		cookie = CookieUtil.getCookie(request, "training");
		if (cookie != null) {
			training = (TrainingEntity)session.getAttribute(cookie.getValue());
		}
	} finally {
		if (cookie != null) {
			try { cookie.setMaxAge(0); response.addCookie(cookie); } catch (Exception e) {}
			try { session.removeAttribute(cookie.getValue()); } catch (Exception e) {}
		}
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>splog learning - 訓練</title>
</head>
<body>
	<div>学習機械を訓練しました。</div>
<% if (training != null) { %>
	<% pageContext.setAttribute("training", training); %>
	<br />
	<table>
		<tr>
			<th>データタイプ</th>
			<td><c:out value="${training.trial.dataTypeLabel}" /></td>
		</tr>
		<tr>
			<th>判別タイプ</th>
			<td><c:out value="${training.trial.classifiedTypeLabel}" /></td>
		</tr>
	</table>
	<br />
	<table>
		<tr>
			<th>判別器</th>
			<th>信頼度</th>
		</tr>
	<c:forEach items="${training.classifiers}" var="classifier">
		<tr>
			<td><c:out value="${classifier.label}" /></td>
			<td><c:out value="${training.learner[classifier.name]}" /></td>
		</tr>
	</c:forEach>
	</table>
<% } %>
	<br />
	<div>
		<a href="javascript:history.back();">戻る</a>
		<a href="/splog_learning">トップ</a>
	</div>
</body>
</html>