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
            <form name="filter-form" method="get" class="pb-8 border-b flex justify-between">
                <span class="leading-normal text-2xl font-semibold text-gray-900 mt-1">Welcome to StoneOverflow</span>
                <c:if test="${param.s != null}">
                    <input type="hidden" name="s" value="${param.s}"/>
                </c:if>
                <div class="inline-block relative w-64">
                    <select name="f" onchange="document.forms['filter-form'].submit();" class="block appearance-none w-full bg-white border border-gray-400 hover:border-gray-500 px-4 py-2 pr-8 rounded shadow leading-tight focus:outline-none focus:shadow-outline">
                        <option value="votes_desc" <c:if test="${param.f == 'votes_desc'}">selected</c:if>>Votes (descending)</option>
                        <option value="date_desc"  <c:if test="${param.f == 'date_desc'}" >selected</c:if>>Newest</option>
                        <option value="date_asc"   <c:if test="${param.f == 'date_asc'}"  >selected</c:if>>Oldest</option>
                        <option value="votes_asc"  <c:if test="${param.f == 'votes_asc'}" >selected</c:if>>Votes (ascending)</option>
                        <option value="views_desc" <c:if test="${param.f == 'views_desc'}">selected</c:if>>Views (descending)</option>
                        <option value="views_asc"  <c:if test="${param.f == 'views_asc'}" >selected</c:if>>Views (ascending)</option>
                    </select>
                    <div class="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-gray-700">
                        <svg class="fill-current h-4 w-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><path d="M9.293 12.95l.707.707L15.657 8l-1.414-1.414L10 10.828 5.757 6.586 4.343 8z"/></svg>
                    </div>
                </div>
            </form>
            <!-- Question Card -->
            <c:forEach items="${questions.questions}" var="question">
                <%@include file="fragments/question.jsp" %>
            </c:forEach>
            <c:choose>
                <c:when test="${empty questions.questions}">
                    <div class="flex items-center justify-center flex-col pt-8 h-48">
                        <i class="fas fa-search fa-3x mb-4 text-gray-700"></i>
                        <h2 class="leading-normal text-lg font-semibold text-gray-900">We couldn't find any question</h2>
                    </div>

                </c:when>
                <c:otherwise>
                    <!-- Pagination -->
                    <div class="mx-3 mt-8">
                        <c:set var="path" value="home"/>
                        <c:set var="pageArg" value="page"/>
                        <%@include file="fragments/pagination.jsp" %>
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
