<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	pageContext.setAttribute("newLine", "\n");
%>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8">
	<link href="/mysite/assets/css/board.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="//code.jquery.com/ui/1.13.1/themes/base/jquery-ui.css">
	<link rel="stylesheet" href="/resources/demos/style.css">
	<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
	<script src="https://code.jquery.com/ui/1.13.1/jquery-ui.js"></script>
	<title>MySite</title>
	
	
	<style type="text/css">
		
		.reply{
			 border-collapse: collapse;
			 border-top: 3px solid #168;
			 width:100%;
		}
		
		.reply th {
		  color: #168;
		  background: #f0f6f9;
		  font-size:15px;
		  text-align:center;
		  width:100%;
		  padding: 10px;
		  border: 1px solid #ddd;
		}
		
		td {
			padding: 5px;
		}
		
		#userName{
			font-weight: bold;
		}
		
		#regDate{
			color: grey;
		}
		
		#reply{
			color: green;
		}
	</style>
	
	<script>
		$(document).ready(function(){
			for(var i=0; i<=$("textarea").length; i++){
				$("#replyForm"+i).hide();
				$("#replyBtn"+i).hide();
			}
			$("#contentReplyForm").hide();
			$("#contentReplyBtn").hide();
		});

		function inputTextArea(i, no){
			document.write('<tr><td><textarea id="replyForm' + cnt +'" name="content" rows="5" cols="70"></textarea></td>');
			document.write('<td><input type="button" id="replyBtn' + cnt +'" value="등록" onClick="javascript:reply(' + cnt + ', '+ no +')"></td></tr>')
		}
		
		function showReplyForm(i){
			if(i != ""){
				$("#replyForm"+i).show();
				$("#replyBtn"+i).show();
			}else{
				$("#contentReplyForm").show();
				$("#contentReplyBtn").show();
			}
		}
	
		function reply(i, no){
	        document.replyFrm.action = "/mysite/board";
	         
	        var hiddenField = document.createElement("input");
	        hiddenField.setAttribute("type", "hidden");
	        hiddenField.setAttribute("name", "no");
	       	hiddenField.setAttribute("value", no);
	       	replyFrm.appendChild(hiddenField);
	       	
	       	if(i != ""){
		        var content = document.getElementById('replyForm' + i);
	       	}else{
	       		var content = document.getElementById('contentReplyForm');
	       	}
	       	hiddenField = document.createElement("input");
	        hiddenField.setAttribute("type", "hidden");
	        hiddenField.setAttribute("name", "content");
	       	hiddenField.setAttribute("value", content.value);
	       	replyFrm.appendChild(hiddenField);
		       	
	        document.replyFrm.submit();
		}
	</script>
</head>
<body>
	<div id="container">

		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>

		<div id="content">
			<div id="board" class="board-form">
				<table class="tbl-ex">
					<tr>
						<th colspan="4">글보기</th>
					</tr>
					<tr>
						<td class="label">제목</td>
						<td>${boardVo.title }</td>
					</tr>
					<tr>
						<td class="label">작성자</td>
						<td>${boardVo.userName }</td>
						
						<td class="label">작성일시</td>
						<td>${boardVo.regDate }</td>
					</tr>
					<tr>
						<td class="label">내용</td>
						<td>
							<div class="view-content">${fn:replace(boardVo.content, newLine, "<br>")}
							</div>
						</td>
					</tr>
				</table>

				<div class="bottom">
					<a href="/mysite/board?a=list&nowPage=${nowPage}&keyWord=${keyWord}&keyField=${keyField}&startDate=${startDate}&endDate=${endDate}">글목록</a>

					<c:if test="${authUser.no == boardVo.userNo }">
						<a href="/mysite/board?a=modifyform&no=${boardVo.no }&nowPage=${nowPage}
														   &keyWord=${keyWord}&keyField=${keyField}
														   &startDate=${startDate}&endDate=${endDate}">
						글수정</a>
					</c:if>
					
					<c:if test="${authUser ne null }">
						<a href="javascript:showReplyForm('')">
						답변</a>
					</c:if>
				</div>
				
				<textarea id="contentReplyForm" name="content" rows="5" cols="70"></textarea>
				<input type="button" id="contentReplyBtn" value="등록" onClick="javascript:reply('', ${boardVo.no})">
				
				<c:if test="${0 ne fn:length(replyList) }">
					<div>
						<table class="reply">
							<tr>
								<th colspan=4>답변</th>
							</tr>
							
							<c:forEach items="${replyList }" var="replyVo" varStatus="status">
								<c:if test="${replyVo.depth > 1}">
									<tr>
										<td id="reply">
											<c:forEach var="i" begin="1" end="${replyVo.depth}">
												&nbsp;
											</c:forEach>
											↳ ${replyVo.userName}
										</td>
									</tr>
							
									<tr id="content">
										<td>
											<c:forEach var="i" begin="1" end="${replyVo.depth}">
												&nbsp;&nbsp;
											</c:forEach>
											${fn:replace(replyVo.content, newLine, "<br>")}
										</td>
									</tr>
								
									<tr id="regDate">	
										<td>
											<c:forEach var="i" begin="1" end="${replyVo.depth}">
												&nbsp;&nbsp;
											</c:forEach>
											${replyVo.regDate } 
											&nbsp; 
											<c:if test="${authUser ne null }">
												<a href="javascript:showReplyForm(${status.count}, ${replyVo.no})">답변</a> 
											</c:if>
											&nbsp; 
											<c:if test="${authUser.no == replyVo.userNo }">
												<a href="/mysite/board?a=delete&no=${replyVo.no }">삭제</a>
											</c:if>
										</td>
									</tr>
  								</c:if>
  								
  								<c:if test="${replyVo.depth eq 1}">
									<tr id="userName">
										<td>${replyVo.userName} </td>
									</tr>
							
									<tr id="content">
											<td>
												<div class="view-content">${fn:replace(replyVo.content, newLine, "<br>")}</div>
											</td>
									</tr>
								
									<tr id="regDate">	
										<td>
											${replyVo.regDate } 
											&nbsp; 
											<c:if test="${authUser ne null }">
												<a href="javascript:showReplyForm(${status.count}, ${replyVo.no})">답변</a> 
											</c:if> 
											&nbsp; 
											<c:if test="${authUser.no == replyVo.userNo }">
												<a href="/mysite/board?a=delete&no=${replyVo.no }">삭제</a>
											</c:if>
										</td>
									</tr>
								</c:if>
								
								<script>
									inputTextArea(${status.count}, ${replyVo.no});
								</script>
								
								<tr>
									<td><hr/></td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</c:if>
			</div>
		</div>
		
		<form name="replyFrm" method="post">
			<input type="hidden" name="a" value="reply"> 
			<input type="hidden" name="nowPage" value="${nowPage }"> 
			<input type="hidden" name="keyField" value="${keyField}"> 
			<input type="hidden" name="keyWord" value="${keyWord}">
			<input type="hidden" name="startDate" value="${startDate}"> 
			<input type="hidden" name="endDate" value="${endDate}">
		</form>

		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>

	</div>
	<!-- /container -->
</body>
</html>

