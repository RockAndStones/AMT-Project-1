<%--
  Created by IntelliJ IDEA.
  User: rmuller
  Date: 19.10.20
  Time: 13:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Statistics</title>
    <link href="assets/css/custom.css" rel="stylesheet" type="text/css"/>
    <link href="assets/css/styles.css" rel="stylesheet" type="text/css"/>
    <link href="assets/css/normalize.css" rel="stylesheet" type="text/css"/>
</head>
<body class="bg-white tracking-wider tracking-normal border-gray-400">
    <!-- Navigation -->
    <%@include file="fragments/header.jsp" %>
    <!-- Main -->
    <div class="w-full flex flex-wrap">
        <!-- Sidebar -->
        <%@include file="fragments/sidebar.jsp" %>

        <!-- Main content -->
        <div class="w-full lg:w-4/5 p-8 lg:mt-0 text-gray-700 leading-normal">
            <p>TODO: Update this page</p>
            <p>Number of users: ${statistics.nbUsers}</p>
            <p>Number of questions: ${statistics.nbQuestions}</p>
            <p>Number of views across all questions: ${statistics.nbViews}</p>
            <p>Sum of votes across all questions: ${statistics.nbVotes}</p>

            <br />
            <h2>Most active users (question wise)</h2>
            <div id="users">
                <c:forEach items="${statistics.mostActiveUsers}" var="user">
                    <p>${user.username} - ${user.nbQuestions} questions</p>
                </c:forEach>
            </div>

            <br />
            <h2>Most popular questions (vote wise)</h2>
            <div id="questions">
                <c:forEach items="${statistics.mostVotedQuestions.questions}" var="question">
                    <p>${question.title} - ${question.nbVotes} votes</p>
                </c:forEach>
            </div>
            <br /><br /><br /><br /><br /><br /><br /><br />
        </div>
    </div>
    <!-- Footer -->
    <%@include file="fragments/footer.jsp" %>
    <!-- BottomBar -->
    <%@include file="fragments/bottombar.jsp" %>
</body>
</html>
