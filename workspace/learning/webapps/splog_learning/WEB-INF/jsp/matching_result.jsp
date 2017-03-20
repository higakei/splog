<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="trial" scope="request" class="jp.co.hottolink.splogfilter.learning.logic.trial.TrialEntity" />
<jsp:useBean id="matchings" scope="request" class="java.util.ArrayList" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>splog learning - 照合</title>
</head>
<body>
<h1>照合結果</h1>
<h3>学習データのトライアル結果を照合しました。</h3>
<table>
	<tr>
		<th>データタイプ</th>
		<td><c:out value="${trial.dataTypeLabel}" /></td>
	</tr>
	<tr>
		<th>判別タイプ</th>
		<td><c:out value="${trial.classifiedTypeLabel}" /></td>
	</tr>
</table>
<br />
<table>
	<tr>
		<th>&nbsp;</th>
		<th>解答数</th>
		<th>正解数</th>
		<th>正解率</th>
	</tr>
<c:forEach items="${matchings}" var="matching">
	<tr>
		<td><c:out value="${matching.classifierLabel}" /></td>
		<td><c:out value="${matching.answer}" /></td>
		<td><c:out value="${matching.correct}" /></td>
		<td><c:out value="${matching.rate}" /></td>
		
	</tr>
</c:forEach>
</table>
<br />
<div>
	<a href="javascript:history.back();">戻る</a>
	<a href="/splog_learning/training.html">訓練</a>
	<a href="/splog_learning">トップ</a>
</div>
</body>
</html>
