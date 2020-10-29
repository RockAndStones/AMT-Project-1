<%--
  Group:        RockAndStone (https://github.com/orgs/tweb-classroom/teams/rockandstone)
  Authors:      Bécaud Arthur, Egremy Bruno, Muller Robin & Teixeira Carvalho Stéphane
  Date:         23.09.2020
  Description:  Home page of StoneOverflow.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean scope="request" id="questions" type="ch.heigvd.amt.stoneoverflow.application.question.QuestionsDTO"/>
<!DOCTYPE html>
<html>
<head>
    <title>Home</title>
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
        <div id="questionList" class="w-full lg:w-4/5 p-8 lg:mt-0 text-gray-900 leading-normal">
            <!-- Question Card -->
            <c:forEach items="${questions.questions}" var="question">
                <%@include file="fragments/question.jsp" %>
            </c:forEach>
            <c:choose>
                <c:when test="${empty questions.questions}">
                    <div class="flex items-center justify-center flex-col h-48">
                        <i class="fas fa-search fa-3x mb-4 text-gray-700"></i>
                        <h2 class="leading-normal text-lg font-semibold text-gray-900">We couldn't find any questions</h2>
                    </div>

                </c:when>
                <c:otherwise>
                    <!-- Pagination -->
                    <div class="mx-3 mt-8">
                        <%@include file="fragments/homePagination.jsp" %>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

    </div>
    <!-- Footer -->
    <%@include file="fragments/footer.jsp" %>
    <!-- BottomBar -->
    <%@include file="fragments/bottombar.jsp" %>
</body>
</html>
