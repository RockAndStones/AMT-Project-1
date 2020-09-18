<%--
  Created by IntelliJ IDEA.
  User: stéphane
  Date: 18.09.2020
  Time: 15:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Home</title>
</head>
<body>
    <h2>Questions</h2>
    <ul>
        <c:forEach items="${questions}" var="question">
            <li>${question.title} : "${question.description}"</li>
        </c:forEach>
    </ul>
</body>
</html>
