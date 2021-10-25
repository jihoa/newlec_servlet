<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<%
pageContext.setAttribute("result","hello");
%>
<body>
	<%=request.getAttribute("result") %>입니다.
	${requestScope.result}<br >
	${names[1]}<br >
	${notice.title}<br >
	${result}<br >
	${param.n/2}  <br>
	${header.accept}
<!-- 	하나의 서블릿 페이지에 대한 저장소는 pageContext -->
<!-- 하나의 Web application에 대한 저장소는 ServletContext(전역 변수와 비슷한 느낌) -->
<!-- forward 관계에서 사용되는 저장소는 Request -->
<!-- 특정 Session에 대한 저장소는 Session -->
<!-- Client에 저장하는 저장소는 Cookie -->
</body>
</html>