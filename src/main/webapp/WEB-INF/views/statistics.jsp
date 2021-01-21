<%--
  Created by IntelliJ IDEA.
  User: rmuller
  Date: 19.10.20
  Time: 13:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean scope="request" id="badgesRank" type="ch.heigvd.amt.stoneoverflow.application.statistics.BadgesRankingsDTO"/>
<jsp:useBean scope="request" id="pointsRank" type="ch.heigvd.amt.stoneoverflow.application.statistics.PointsRankingsDTO"/>
<jsp:useBean scope="request" id="isGamificationOn" type="java.lang.Boolean"/>
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
        <div class="pb-8 mb-8 border-b">
            <span class="leading-normal text-2xl font-semibold text-gray-900 mt-1">A few numbers</span>
        </div>
        <div class="pb-0 md:pb-8 mb-8 border-b">
            <ul class="flex flex-grow flex-wrap ">
                <li class="flex-grow min-w-1/3 p-8 mx-4 mb-8 md:mb-0 shadow-xl border border-gray-200">
                    <span class="block text-gray-500">Total users</span>
                    <span class="block text-gray-900 text-2xl">${statistics.nbUsers}</span>
                </li>
                <li class="flex-grow min-w-1/3 p-8 mx-4 mb-8 md:mb-0 shadow-xl border border-gray-200">
                    <span class="block text-gray-500">Total questions</span>
                    <span class="block text-gray-900 text-2xl">${statistics.nbQuestions}</span>
                </li>
                <li class="flex-grow min-w-1/3 p-8 mx-4 mb-8 md:mb-0 shadow-xl border border-gray-200">
                    <span class="block text-gray-500">Total views</span>
                    <span class="block text-gray-900 text-2xl">${statistics.nbViews}</span>
                </li>
                <li class="flex-grow min-w-1/3 p-8 mx-4 md:mb-0 shadow-xl border border-gray-200">
                    <span class="block text-gray-500">Total votes</span>
                    <span class="block text-gray-900 text-2xl">${statistics.nbVotes}</span>
                </li>

            </ul>
        </div>
        <div class="pb-8 mb-8 border-b">
            <span class="leading-normal text-2xl font-semibold text-gray-900 mt-1">Most active users</span>
            <span class="leading-normal text-lg italic text-gray-600 mt-1">(by questions asked)</span>
        </div>
        <div class="pb-0 md:pb-8 mb-8 border-b">
            <table class="table-auto">
                <thead>
                <tr>
                    <th class="border px-4 py-2">User</th>
                    <th class="border px-4 py-2">Number of questions asked</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${statistics.mostActiveUsers}" var="user">
                    <tr class="userRow">
                        <td class="border px-4 py-2">${user.username}</td>
                        <td class="border px-4 py-2 text-center">${user.nbQuestions}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <c:if test="${isGamificationOn}">
            <div class="pb-8 mb-8 border-b">
                <span class="leading-normal text-2xl font-semibold text-gray-900 mt-1">Most active users</span>
                <span class="leading-normal text-lg italic text-gray-600 mt-1">(by number of points)</span>
            </div>
            <div class="pb-0 md:pb-8 mb-8 border-b">
                <table class="table-auto w-full">
                    <thead>
                    <tr>
                        <th class="border px-4 py-2 text-left">User</th>
                        <th class="border px-4 py-2">Number of points</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${pointsRank.rankings}" var="rank">
                        <tr class="userRow">
                            <td class="border px-4 py-2">${rank.username}</td>
                            <td class="border px-4 py-2">${rank.points}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <!-- Pagination -->
                <div class="mx-3 mt-8">
                    <c:set var="path" value="statistics"/>
                    <c:set var="pageArg" value="pointsPage"/>
                    <%@include file="fragments/pagination.jsp" %>
                </div>
            </div>
            <div class="pb-8 mb-8 border-b">
                <span class="leading-normal text-2xl font-semibold text-gray-900 mt-1">Most active users</span>
                <span class="leading-normal text-lg italic text-gray-600 mt-1">(by number of badges)</span>
            </div>
            <div class="pb-0 md:pb-8 mb-8 border-b">
                <table class="table-auto w-full">
                    <thead>
                    <tr>
                        <th class="border px-4 py-2 text-left">User</th>
                        <th class="border px-4 py-2">Number of badges</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${badgesRank.rankings}" var="rank">
                        <tr class="userRow">
                            <td class="border px-4 py-2">${rank.username}</td>
                            <td class="border px-4 py-2 text-center">${rank.badges}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <!-- Pagination -->
                <div class="mx-3 mt-8">
                    <c:set var="path" value="statistics"/>
                    <c:set var="pageArg" value="badgesPage"/>
                    <%@include file="fragments/pagination.jsp" %>
                </div>
            </div>
        </c:if>
        <div class="pb-8 mb-8 border-b">
            <span class="leading-normal text-2xl font-semibold text-gray-900 mt-1">Most popular questions</span>
            <span class="leading-normal text-lg italic text-gray-600 mt-1">(by number of votes)</span>
        </div>
        <div>
            <table class="table-auto w-full">
                <thead>
                <tr>
                    <th class="border px-4 py-2 text-left">Question</th>
                    <th class="border px-4 py-2">Number of votes</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${statistics.mostVotedQuestions.questions}" var="question">
                    <tr class="userRow">
                        <td class="border px-4 py-2">${question.title}</td>
                        <td class="border px-4 py-2 text-center">${question.nbVotes}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<!-- Footer -->
<%@include file="fragments/footer.jsp" %>
<!-- BottomBar -->
<%@include file="fragments/bottombar.jsp" %>
</body>
</html>
