<%--
  Group:        RockAndStone (https://github.com/orgs/tweb-classroom/teams/rockandstone)
  Authors:      Bécaud Arthur, Egremy Bruno, Muller Robin & Teixeira Carvalho Stéphane
  Date:         07.10.2020
  Description:  Question's details page of StoneOverflow.

  JSP VARIABLE NEEDED:

  question:
    id,
    title,
    nbViews,
    nbVotes,
    description,
    creator,
    date,
    answers: [
        {
            id,
            description,
            creator,
            date,
            comments: [
                {
                    content,
                    creator,
                    date
                },
                ...
            ]
        },
        ...
    ]

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Home</title>
    <link href="assets/css/custom.css" rel="stylesheet" type="text/css"/>
    <link href="assets/css/styles.css" rel="stylesheet" type="text/css"/>
    <link href="assets/css/normalize.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css" integrity="sha384-oS3vJWv+0UjzBfQzYUhtDYW+Pj2yciDJxpsK1OYPAYjqT085Qq/1cq5FLXAZQ7Ay" crossorigin="anonymous">
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
            <!-- Details header -->
            <div class="w-full pb-6 mb-4 border-b">
                <h1 class="leading-normal text-lg font-semibold text-gray-900 mt-0">${question.title}TMP: TITLE</h1>
                <span>${question.nbViews}TMP: NBVIEWS</span>
                <i class="mx-auto mb-2 h-5 w-5 far fa-eye"></i>
                <span class="ml-2">${questions.comments.size()}TMP: NBRESPONSE</span>
                <i class="mx-auto mb-2 h-5 w-5 far fa-comment"></i>
            </div>
            <!-- Details body -->
            <div class="pb-8 mb-8 border-b">
                <div class="flex flex-shrink">
                    <%@include file="fragments/postVoteAndContent.jsp"%>
                </div>
            </div>
            <!-- Details answer(s) -->
            <div class="mb-8 pb-8 border-b">
                <c:choose>
                    <c:when test="${empty question.answers}">
                        <h2 class="leading-normal text-lg font-semibold text-gray-900 mt-0">No one responded for now <i class="far fa-sad-tear"></i></h2>
                    </c:when>
                    <c:otherwise>
                        <h2 class="leading-normal text-lg font-semibold text-gray-900 mt-0 mb-4">Answer(s)</h2>
                        <c:forEach items="${question.answer}" var="post">
                            <%@include file="fragments/answer.jsp" %>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
            <!-- Response form -->
            <h2 class="leading-normal text-lg font-semibold text-gray-900 mt-0 mb-4">Write your answer</h2>
            <form action="" method="post">
                <input type="hidden" name="" value="">
                <div class="flex flex-col">
                    <label for="responseDescription" class="text-lg">Description</label>
                    <textarea id="responseDescription" name="description" placeholder="Please be sure to provide a clear and detailed answer to contribute to StoneOverflow." class="h-48 shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline"></textarea>
                </div>
                <button type="submit" class="w-auto bg-black text-white font-bold text-lg hover:bg-gray-700 p-2 mt-8">Submit</button>
            </form>
        </div>
    </div>
    <!-- Footer -->
    <%@include file="fragments/footer.jsp" %>
    <!-- BottomBar -->
    <%@include file="fragments/bottombar.jsp" %>
</body>
</html>