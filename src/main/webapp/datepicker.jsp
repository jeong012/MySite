<%@ page contentType="text/html; charset=UTF-8" %>

<%	
	request.setCharacterEncoding("UTF-8");

	String keyWord = request.getParameter("keyWord");
	if(keyWord == null) { keyWord = ""; }
	
	String keyField = request.getParameter("keyField");
	if(keyField == null) { keyField = ""; }
	
	String startDate = request.getParameter("startDate");
	if(startDate == null) { startDate = ""; }
	
	String endDate = request.getParameter("endDate");
	if(endDate == null) { endDate = ""; }
%>
<html>
<head>
	<title>JSP Board</title>
	<link href="style.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="//code.jquery.com/ui/1.13.1/themes/base/jquery-ui.css">
	<link rel="stylesheet" href="/resources/demos/style.css">
	<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
	<script src="https://code.jquery.com/ui/1.13.1/jquery-ui.js"></script>
	<script>	
	
		$(document).ready(function(){
			document.getElementById("endDatePicker").style.visibility = "hidden";
		});
		
		function change(){
			if(document.searchForm.keyField.value == "regDate"){
				$("input#keyWord").attr("placeholder", "시작일");
				$("#keyWord").datepicker();
				
				document.getElementById("endDatePicker").style.visibility = "visible";
				$("input#endDatePicker").attr("placeholder", "종료일");
				$("#endDatePicker").datepicker();
				
			}else{
				$("#keyWord").datepicker('option', 'disabled', true);
				document.getElementById("endDatePicker").style.visibility = "hidden";
				
				if(document.searchForm.keyField.value == "default"){
					$("input#keyWord").attr("placeholder", "");
				}else{
					$("input#keyWord").attr("placeholder", "키워드");
				}
			}
		}
		
		function search() {
			if(document.searchForm.keyField.value == "default"){
				alert("검색 필드를 선택하세요.");
				
			} else if (document.searchForm.keyField.value == "regDate" 
						&& (document.searchForm.keyWord.value == "" || document.searchForm.endDatePicker.value == "")) {
				alert("검색하고자 하는 날짜를 선택하세요.");
				
			}else{
				if(document.searchForm.keyWord.value == ""){
					alert("검색어를 입력하세요.");
					document.searchForm.keyWord.focus();
					return;
				}
				else{
					document.searchForm.submit();
				}
			}
		 }
	
	</script>

</head>
<body bgcolor="#FFFFCC">
	keyField : <%=keyField %>
	keyWord : <%=keyWord %>
	startDate : <%=startDate %>
	endDate : <%=endDate %>


	<div align="center">
		<form  name="searchForm"  method="get" action="datepicker.jsp">
		
			<select name="keyField" size="1" onChange="javascript:change()">
	 			<option value="default"> 검색 필드</option>
	 			<option value="name"> 작성자</option>
	 			<option value="regDate"> 작성일시</option>
   				<option value="title"> 제목</option>
   				<option value="content"> 내용</option>
			</select>
					
			<p><input type="text" id="keyWord" name="keyWord" ></p>
			<p><input type="text" id="endDatePicker" name="endDate"></p>
		
			<p><input type="button" id="searchBtn" value="검색" onClick="javascript:search()"></p>
			
		</form>
	</div>
</body>
</html>