<%--
  Created by IntelliJ IDEA.
  User: rmuller
  Date: 19.10.20
  Time: 13:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean scope="request" id="badgesRank" type="ch.heigvd.amt.stoneoverflow.application.statistics.BadgesRankingsDTO"/>
<jsp:useBean scope="request" id="pointsRank" type="ch.heigvd.amt.stoneoverflow.application.statistics.PointsRankingsDTO"/>
<jsp:useBean scope="request" id="pointsQRank" type="ch.heigvd.amt.stoneoverflow.application.statistics.PointsRankingsDTO"/>
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
        <!-- A few numbers -->
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
        <c:if test="${isGamificationOn}">
            <!-- Gamification stats -->
            <!-- Most active users (by questions asked) -->
            <div class="pb-8 mb-8 border-b">
                <span class="leading-normal text-2xl font-semibold text-gray-900 mt-1">Most active users</span>
                <span class="leading-normal text-lg italic text-gray-600 mt-1">(by questions asked)</span>
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
                    <c:forEach items="${pointsQRank.rankings}" var="rank">
                        <tr class="userRow">
                            <td class="border px-4 py-2">${rank.username}</td>
                            <td class="border px-4 py-2 text-center">
                                <c:choose>
                                    <c:when test="${rank.points == null}">
                                        0
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:parseNumber var="nbQ" integerOnly="true"
                                                         type="number" value="${rank.points}" />
                                        ${nbQ}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <!-- Pagination -->
                <div class="mx-3 mt-8">
                    <c:set var="path" value="statistics"/>
                    <c:set var="pageArg" value="pointsQPage"/>
                    <c:set var="pagination" value="${pointsQRank.pagination}"/>
                    <%@include file="fragments/pagination.jsp" %>
                </div>
            </div>
            <!-- Most active users (by number of points) -->
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
                            <td class="border px-4 py-2 text-center">
                                <c:choose>
                                    <c:when test="${rank.points == null}">
                                        0.0
                                    </c:when>
                                    <c:otherwise>
                                        ${rank.points}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <!-- Pagination -->
                <div class="mx-3 mt-8">
                    <c:set var="path" value="statistics"/>
                    <c:set var="pageArg" value="pointsPage"/>
                    <c:set var="pagination" value="${pointsRank.pagination}"/>
                    <%@include file="fragments/pagination.jsp" %>
                </div>
            </div>
            <!-- Most active users (by number of badges) -->
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
                            <td class="border px-4 py-2 text-center">
                                <c:choose>
                                    <c:when test="${rank.badges == null}">
                                        0
                                    </c:when>
                                    <c:otherwise>
                                        ${rank.badges}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <!-- Pagination -->
                <div class="mx-3 mt-8">
                    <c:set var="path" value="statistics"/>
                    <c:set var="pageArg" value="badgesPage"/>
                    <c:set var="pagination" value="${badgesRank.pagination}"/>
                    <%@include file="fragments/pagination.jsp" %>
                </div>
            </div>
        </c:if>
        <!-- Most popular questions (by number of votes) -->
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
