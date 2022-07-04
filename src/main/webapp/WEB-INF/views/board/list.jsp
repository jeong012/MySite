<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- jstl 변수 선언 -->
<c:set var="totalRecord" value = "${totalRecord}" /> 	<!-- 전체 레코드수 -->
<c:set var="numPerPage" value = "10"/>					<!-- 페이지당 레코드 수 -->
<c:set var="pagePerBlock" value = "15"/>				<!-- 블럭당 페이지수 -->

<c:set var="totalPage" value = "0"/>					<!-- 전체 페이지 수 -->
<c:set var="totalBlock" value = "0"/>					<!-- 전체 블럭수 -->

<c:set var="nowPage" value = "${nowPage}"/>				<!-- 현재페이지 -->
<c:set var="nowBlock" value = "${nowBlock }"/>			<!-- 현재블럭 -->

<c:set var="listSize" value = "${fn:length(list)}"/>	<!-- 현재 읽어온 게시물의 수 -->


<c:set var="keyWord" value="${keyWord }"/>
<c:set var="keyField" value="${keyField }"/>

<fmt:formatNumber var="totalPageFormated" value="${Math.ceil(totalRecord / numPerPage)}" pattern="0"/>
<c:set var="totalPage" value = "${totalPageFormated}" />

<fmt:formatNumber var="nowBlockFormated" value="${Math.ceil(nowPage/pagePerBlock)}" pattern="0"/>
<c:set var="nowBlock" value = "${nowBlockFormated}" />

<fmt:formatNumber var="totalBlockFormated" value="${Math.ceil(totalPage / pagePerBlock)}" pattern="0"/>
<c:set var="totalBlock" value = "${totalBlockFormated}" />

<!DOCTYPE html>
<html>
<head>
	<title>MySite</title>
	
	<meta http-equiv="content-type" content="text/html; charset=utf-8">
	<link href="/mysite/assets/css/board.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="//code.jquery.com/ui/1.13.1/themes/base/jquery-ui.css">
	<link rel="stylesheet" href="/resources/demos/style.css">
	<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
	<script src="https://code.jquery.com/ui/1.13.1/jquery-ui.js"></script>
	
	<script>
		$(document).ready(function(){
			$("#startDatePicker").hide(); 
			$("#endDatePicker").hide(); 
		});
		
		function change(){
			if(document.searchForm.keyField.value == "regDate"){
				$("#keyWord").hide(); 
				
				$("#startDatePicker").show(); 
				$("#endDatePicker").show(); 
				
				$("input#startDatePicker").attr("placeholder", "시작일");
				$("#startDatePicker").datepicker({ dateFormat: 'yy-mm-dd' });
				
				$("input#endDatePicker").attr("placeholder", "종료일");
				$("#endDatePicker").datepicker({ dateFormat: 'yy-mm-dd' });
				
			}else{
				$("#keyWord").show(); 
				
				$("#startDatePicker").hide(); 
				$("#endDatePicker").hide(); 
				
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
					   && (document.searchForm.startDatePicker.value == "" || document.searchForm.endDatePicker.value == "")) {
				alert("검색하고자 하는 날짜를 선택하세요.");
				
			}else if(document.searchForm.keyField.value != "default" 
					 && document.searchForm.keyField.value != "regDate"
					 && document.searchForm.keyWord.value == ""){
				alert("검색어를 입력하세요.");
				document.searchForm.keyWord.focus();
				return;
			}else{
				document.searchForm.submit();
			}
		 }
	</script>
</head>
<body>
	<div id="container">
	
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		
		<div id="content">
			<br>
			<div id="board">
				<form name="searchForm" action="/mysite/board" method="post">
					<table width="600" cellpadding="4" cellspacing="0" align="center">
				 		<tr>
				  			<td align="center" valign="bottom">
				   				<select name="keyField" size="1" onChange="javascript:change()">
						 			<option value="default"> 검색 필드</option>
						 			<option value="name"> 작성자</option>
						 			<option value="regDate"> 작성일시</option>
					   				<option value="title"> 제목</option>
					   				<option value="content"> 내용</option>
								</select>
				   				
				   				<input type="text" id="keyWord" name="keyWord" value="">
				   				<input type="date" id="startDatePicker" name="startDate">
				   				<input type="date" id="endDatePicker" name="endDate">
				   				<input type="button" id="searchBtn" value="검색" onClick="javascript:search()">
				  			</td>
				 		</tr>
					</table>
					<input type="hidden" name="a" value="list">
				</form>
				
				<br><br>
				<c:if test= "${0 ne totalRecord}">
					<table align="center" width="600">
						<tr>
							<td>Total : ${totalRecord} Posts 
								(<font color="red">${nowPage}/${totalPage}Pages</font>)
							</td>
						</tr>
					</table>
				</c:if>
				
				<c:if test="${!empty keyField}">
					<br>
					<c:if test="${keyField eq 'regDate'}">
						<h3>${startDate}부터 ${endDate}까지 검색한 결과입니다.</h3>
					</c:if>
					<c:if test="${keyField ne 'regDate'}">
						<h3>${keyWord}에 대해 검색한 결과입니다.</h3>
					</c:if>
					<br>
				</c:if>
				
				<c:choose>
					<c:when test="${0 eq listSize && empty keyField}">
						<br><br><br>
						<center><h2>등록된 글이 없습니다.</h2></center>
						<br><br><br>
					</c:when>
					
					<c:when test="${0 ne listSize}">
						<table class="tbl-ex">
							<tr>
								<th>번호</th>
								<th>제목</th>
								<th>글쓴이</th>
								<th>조회수</th>
								<th>작성일</th>
								<th>답변수</th>
								<th>&nbsp;</th>
							</tr>	

							<c:set var="doneLoop" value="false"/>
							<c:forEach var="i" begin="0" end="${numPerPage-1}">
								<c:if test="${not doneLoop}">
									<c:set var="vo" value="${list.get(i)}"/>
									<tr>
										<td>${vo.no }</td>
										<td>
											<a href="/mysite/board?a=read&no=${vo.no}&nowPage=${nowPage}
																		 &keyWord=${keyWord}&keyField=${keyField}
																		 &startDate=${startDate}&endDate=${endDate}">
												${vo.title }
											</a>
										</td>
										<td>${vo.userName }</td>
										<td>${vo.hit }</td>
										<td>${vo.regDate }</td>
										<td>${replyCountList[i]}</td>
										<td>
											<c:if test="${authUser.no == vo.userNo }">
												<a href="/mysite/board?a=delete&no=${vo.no }" class="del">삭제</a>
											</c:if>
										</td>
									</tr>
									<c:if test="${i+1 eq listSize}">
										<c:set var="doneLoop" value="true"/>
									</c:if>
								</c:if>
							</c:forEach>
						</table>	
						
						<c:set var="pageStart" value="${(nowBlock -1)*pagePerBlock + 1 }"/>
						<c:set var="pageEnd" value="${((pageStart + pagePerBlock ) <= totalPage) ?  (pageStart + pagePerBlock): totalPage+1}"/>

						<div class="pager">
							<ul>
								<c:if test="${0 ne totalPage }">
									<c:if test="${nowBlock > 1}">
										<li><a href="/mysite/board?a=list&nowPage=${pagePerBlock * (nowBlock - 2 ) + 1}&nowBlock=${nowBlock}&keyWord=${keyWord}&keyField=${keyField}&startDate=${startDate}&endDate=${endDate}">◀</a></li>
									</c:if>
									<c:forEach var="pageStart" begin="${pageStart }" end="${pageEnd-1}" step="1">
										<li><a href="/mysite/board?a=list&nowPage=${pageStart}&keyWord=${keyWord}&keyField=${keyField}&startDate=${startDate}&endDate=${endDate}">
										<c:if test="${pageStart eq nowPage }">
											<font color="blue">
												${pageStart}
											</font>
										</c:if>
										<c:if test="${pageStart ne nowPage }">
											${pageStart}
										</c:if>
										</a></li> 
									</c:forEach>
									
								</c:if>
								<c:if test="${totalBlock > nowBlock }" >
									<li><a href="/mysite/board?a=list&nowPage=${pagePerBlock * nowBlock +1}&nowBlock=${nowBlock}&keyWord=${keyWord}&keyField=${keyField}&startDate=${startDate}&endDate=${endDate}">▶</a></li>
								</c:if>
							</ul>
						</div>
					</c:when>
				</c:choose>
				
				<c:if test="${authUser != null }">
					<div class="bottom">
						<a href="/mysite/board?a=writeform" id="new-book">글쓰기</a>
					</div>
				</c:if>				
			</div>
		</div>
		
		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
		
	</div><!-- /container -->
</body>
</html>		
		
