<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page import="com.kosta.lsj.dao.GuestbookDao" %>
<%@ page import="com.kosta.lsj.dao.GuestbookDaoImpl" %>
<%@ page import="com.kosta.lsj.vo.GuestbookVo" %>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="/mysite/assets/css/guestbook.css" rel="stylesheet" type="text/css">
	<title>MySite</title>
</head>
<body>

	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		
		<div id="wrapper">
			<div id="content">
				<div id="guestbook">
					
					<form action="/mysite/gb" method="post">
						<input type="hidden" name="a" value="add" />
						
						<table>
							<tr>
								<td>이름</td><td><input type="text" name="name" /></td>
								<td>비밀번호</td><td><input type="password" name="password" /></td>
							</tr>
							<tr>
								<td colspan=4><textarea name="content" id="content"></textarea></td>
							</tr>
							<tr>
								<td colspan=4 align=right><input type="submit" VALUE=" 확인 " /></td>
							</tr>
						</table>
					</form>
					<ul>
						<li>
							<c:forEach items="${list}" var="vo">
								<table>
								<tr>
									<td>${vo.no}</td>
									<td>${vo.name}</td>
									<td>${vo.regDate}</td>
									<td><a href="/mysite/gb?a=deleteform&no=${vo.no}">삭제</a></td>
								</tr>
								<tr>
									<td colspan=4>${vo.content}</td>
								</tr>
								</table>
								<br/>
							</c:forEach>
							<br>
						</li>
					</ul>
					
					<c:if test="${param.result eq 'fail' }">
						<script>
							alert("방명록이 삭제되지 않았습니다.\n다시 시도해주세요");
						</script>
					</c:if>
					
				</div><!-- /guestbook -->
			</div><!-- /content -->
		</div><!-- /wrapper -->
		
		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
		
	</div> <!-- /container -->

</body>
</html>