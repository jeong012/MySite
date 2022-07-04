<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>첨부 업로드</title>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<script>
		function download(fileName){
			document.downFrm.fileName.value=fileName;
	        document.downFrm.submit();
		}
	</script>
</head>
<body>
<%
	String fileName = "솜사탕_UHD.jpg";
%>
	<form name="downFrm" action="<%= application.getContextPath()%>/fileDownload" method="post">
		<input type="hidden" name="fileName">
	</form>
	
	<a href="javascript:download('<%=fileName%>')"><%=fileName%></a>
		
</body>
</html>