<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="jp.co.hottolink.splogfilter.common.loader.DataLoaderImpl" %>
<%@page import="java.util.Map" %>
<%@page import="jp.co.hottolink.splogfilter.learning.logic.trial.TrainingDataTrier" %>
<%@page import="jp.co.hottolink.splogfilter.learning.writer.TrialResultDBWriter" %>
<%@page import="jp.co.hottolink.splogfilter.learning.logic.trial.TrialEntity" %>
<%
	TrialEntity trial = (TrialEntity)request.getAttribute("trial");
	DataLoaderImpl loader = (DataLoaderImpl)request.getAttribute("loader");
	TrialResultDBWriter writer = (TrialResultDBWriter)request.getAttribute("writer");
	Integer total = (Integer)request.getAttribute("total");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>splog learning - トライアル</title>
</head>
<body>
	<h1>トライアル</h1>
	<div id="message"></div>
<%
	try {
		TrainingDataTrier trier = new TrainingDataTrier(trial);
		loader.open();
		writer.open();

		for (int i = 1;;i++) {
			Map<String, Object> data = loader.fetch();
			if (data == null) {
				break;
			}
	
			data.put("data_id", i);
			trier.doTraial(writer, data);
			String meaasage = "トライアル中：" + total + "件中" + i + "件終了";
			out.println("<script type=\"text/javascript\">document.getElementById(\"message\").innerHTML = \"" + meaasage + "\";</script>");
			out.flush();
		}
	} catch (Exception e) {
		throw e;
	} finally {
		if (loader != null) try { loader.close(); } catch (Exception e) {}
		if (writer != null) try { writer.close(); } catch (Exception e) {}
	}

	String location = request.getContextPath() + "/trial_completed.html";
	out.println("<script type=\"text/javascript\">window.location.href(\""+ location + "\")</script>");
%>
</body>
</html>
