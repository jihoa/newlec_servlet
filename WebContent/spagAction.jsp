<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		//Spag.java와 동일한 소스.
		int num = 0;
		String num_ = request.getParameter("n");
		if (num_ != null && !num_.equals(""))
			num = Integer.parseInt(num_);
		String result;

		if (num % 2 != 0)
			result = "홀수";
		else
			result = "짝수";

		request.setAttribute("result", result);

		String[] names = { "newlec", "dragon" };
		request.setAttribute("names", names);
		Map<String, Object> notice = new HashMap<String, Object>();
		notice.put("id", 1);
		notice.put("title", "EL은 좋아요");
		request.setAttribute("notice", notice);

		//redirect : 현재 작업한 내용과 전혀 상관없이 새로운 요청을 하게함
		//forward: 현재 작업한 내용을 이어갈수있도록 무언가를공유
		RequestDispatcher dispatcher = request.getRequestDispatcher("spag.jsp");
		dispatcher.forward(request, response);
	%>
</body>
</html>