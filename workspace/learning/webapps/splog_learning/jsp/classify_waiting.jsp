<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="javax.servlet.http.Cookie" %>
<%@page import="jp.co.hottolink.splogfilter.learning.web.util.CookieUtil" %>
<%
	String message = "しばらくお待ちください。";

	try {
		Integer total = null;
		Integer count = null;

		String progressId = CookieUtil.getCookieValue(request, "progress");
		if (progressId != null) {
			total = (Integer)session.getAttribute(progressId + "_total");
			count = (Integer)session.getAttribute(progressId + "_count");
		}

		if (count != null) {
			message = total + "件中" + count + "件終了";
		} else if (total != null) {
			message = "データ数:" + total + "件";
		}

	} catch (Exception e) {
		e.printStackTrace();
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<script type="text/javascript">
<!--
var intervalId = null;

function doReload() {
	self.location.reload();
}

function doStop() {
	if (window.top.stop) {
		window.top.stop();
	} else {
		window.top.document.execCommand('Stop');
	}
	window.top.tb_remove();
	clearTimeout(intervalId);
}
// -->
</script>
</head>
<body onload="intervalId = setInterval(doReload, 5000);">
	<div style="font: 12px Arial, Helvetica, sans-serif;">
		<div style="padding:5px 0px 5px 0px;">ただいま判定中です。</div>
		<div style="padding:5px 0px 5px 0px;"><%= message %></div>
		<div style="padding:5px 0px 5px 0px;">
			<a href="javascript:void(0);" onclick="doStop();">中止</a>
		</div>
	</div>
</body>
</html>
