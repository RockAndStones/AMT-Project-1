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
    <title>Home</title>
    <link href="css/custom.css" rel="stylesheet" type="text/css"/>
    <link href="css/styles.css" rel="stylesheet" type="text/css"/>
    <link href="css/normalize.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="javascript/header.js"></script>
</head>
<body class="bg-white tracking-wider tracking-normal border-gray-400">
    <!-- Navigation -->
    <%@include file="fragments/header.jsp" %>
    <!-- Main -->
    <div class="w-full flex flex-wrap">
        <!-- Sidebar -->
        <%@include file="fragments/sidebar.jsp" %>
        <!-- Main content -->
        <div class="w-full lg:w-4/5 p-8 mt-6 lg:mt-0 text-gray-900 leading-normal">

            <!-- Question Card -->
            <c:forEach items="${questions}" var="question">
                <%@include file="fragments/question.jsp" %>
            </c:forEach>
        </div>
    </div>
    <!-- Footer -->
    <%@include file="fragments/footer.jsp" %>
</body>
</html>
