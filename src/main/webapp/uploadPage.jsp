<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>첨부 업로드</title>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
	<script>
		var cnt = $("input[type=file]").length +1;
		
		function inputFile(){
			if($("input[type=file]").length == 2){
				alert("최대 2개의 파일만 업로드 할 수 있습니다.");
				$("#addBtn").hide();
			}else{
				$("form").append("<br id=file" + cnt + "><br id=file" + cnt + "><label id=file" + cnt + ">첨부파일&nbsp;:&nbsp;");
				$("form").append("<input type=file class=files id=file" + cnt + " name=file" + cnt + ">");
				$("form").append("<button type=button id=file" + cnt + " onClick=javascript:deleteFile(" + cnt + ")>삭제</button></label>");
				cnt++;
			}
		}
		
		function checkFile(path){
			var fileInput = document.getElementsByClassName("files");
			for( var i=0; i<fileInput.length; i++ ){
				if( fileInput[i].files.length == 0 ){
					alert("파일이 선택 되지 않았습니다.");
					return;
				}
			}
	       	
			document.uploadFrm.setAttribute("action", path);
			document.uploadFrm.submit();
		}
		
		function deleteFile(i){
			$("br").remove("#file"+i);
			$("label").remove("#file"+i);
			$("input").remove("#file"+i);
			$("button").remove("#file"+i);
			
			if($("input[type=file]").length != 2){
				$("#addBtn").show();
			}
		}
	</script>
</head>
<body>
	<form name="uploadFrm" action="javascript:checkFile('<%= application.getContextPath() %>/fileUpload')" method="post" enctype="multipart/form-data">
		<button type="button" id="addBtn" onClick="javascript:inputFile()"> 추가 </button>
		<input type="submit" value="전송">
	</form>
	
</body>
</html>