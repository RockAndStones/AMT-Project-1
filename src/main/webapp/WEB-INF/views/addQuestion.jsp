<%--
  Group:        RockAndStone (https://github.com/orgs/tweb-classroom/teams/rockandstone)
  Authors:      Bécaud Arthur, Egremy Bruno, Muller Robin & Teixeira Carvalho Stéphane
  Date:         23.09.2020
  Description:  Home page of StoneOverflow.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>New question</title>
    <link href="assets/css/custom.css" rel="stylesheet" type="text/css"/>
    <link href="assets/css/styles.css" rel="stylesheet" type="text/css"/>
    <link href="assets/css/normalize.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css" integrity="sha384-oS3vJWv+0UjzBfQzYUhtDYW+Pj2yciDJxpsK1OYPAYjqT085Qq/1cq5FLXAZQ7Ay" crossorigin="anonymous">
    <script type="text/javascript" src="assets/javascript/header.js"></script>
</head>
<body class="bg-white tracking-wider tracking-normal border-gray-400">
<!-- Navigation -->
<%@include file="fragments/header.jsp" %>
<!-- Main -->
<div class="w-full flex flex-wrap">
    <!-- Sidebar -->
    <%@include file="fragments/sidebar.jsp" %>
    <!-- Main content -->
    <div class="w-full lg:w-4/5 p-8 lg:mt-0 text-gray-900 leading-normal">
        <!-- New question form -->
        <div id="newQuestionForm" class="flex flex-col justify-center md:justify-start my-auto pt-8 md:pt-0 px-8 md:px-24 lg:px-32">
            <p id="newQuestionLabel" class="text-center text-3xl">Your question.</p>
            <form action="${pageContext.request.contextPath}/submitQuestion.do" method="POST" class="flex flex-col pt-3 md:pt-8">
                <div class="flex flex-col pt-4">
                    <label for="questionTitle" class="text-lg">Title</label>
                    <input type="text" id="questionTitle" name="title" placeholder="Your awesome question's title" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline" required>
                </div>

                <div class="flex flex-col pt-4">
                    <label for="questionDescription" class="text-lg">Description</label>
                    <textarea id="questionDescription" name="description" placeholder="Include all the information someone would need to answer your question" class="h-48 shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline" required></textarea>
                </div>

                <button id="newQuestionSubmitButton" type="submit" class="self-center w-auto bg-black text-white font-bold text-lg hover:bg-gray-700 p-2 mt-8">Submit</button>
            </form>
        </div>
    </div>
</div>
<!-- Footer -->
<%@include file="fragments/footer.jsp" %>
<!-- BottomBar -->
<%@include file="fragments/bottombar.jsp" %>
</body>
</html>
