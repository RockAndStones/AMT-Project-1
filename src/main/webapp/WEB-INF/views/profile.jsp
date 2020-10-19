<%--
  Created by IntelliJ IDEA.
  User: stéphane
  Date: 16.10.2020
  Time: 16:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User Profile</title>
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
    <!-- Main -->
    <div class="w-full lg:w-4/5 p-8 lg:mt-0 text-gray-900 leading-normal">
        <!-- Update Section -->
        <div id="registerSection" class="flex flex-col justify-center md:justify-start my-auto pt-8 md:pt-0 px-8 md:px-24 lg:px-32">
            <p id="registerLabel" class="text-center text-3xl">Your Profile</p>
            <form action="${pageContext.request.contextPath}/updateUser.do" method="POST" class="flex flex-col pt-3 md:pt-8">
                <c:if test="${errorMessage != null}">
                    <p class="text-center text-xl text-red-700">${errorMessage}</p>
                </c:if>
                <c:if test="${message != null}">
                    <p class="text-center text-xl text-green-700">${message}</p>
                </c:if>

                <div class="flex flex-col pt-4">
                    <label for="updateUsername" class="text-lg">Username</label>
                    <input type="text" id="updateUsername" name="username" placeholder="Your awesome username" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline"
                    value="<c:if test="${sessionScope.authenticatedUser != null}">${sessionScope.authenticatedUser.username}</c:if>">
                </div>

                <div class="flex flex-col pt-4">
                    <label for="updateEmail" class="text-lg">Email address</label>
                    <input type="email" id="updateEmail" name="email"  placeholder="Your email address" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline"
                           value="<c:if test="${sessionScope.authenticatedUser != null}">${sessionScope.authenticatedUser.email}</c:if>">
                </div>

                <div class="flex flex-col pt-4">
                    <label for="updateFirstName" class="text-lg">First name</label>
                    <input type="text" id="updateFirstName" name="firstName" placeholder="Your first name" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline"
                           value="<c:if test="${sessionScope.authenticatedUser != null}">${sessionScope.authenticatedUser.firstName}</c:if>">
                </div>

                <div class="flex flex-col pt-4">
                    <label for="updateLastName" class="text-lg">Last name</label>
                    <input type="text" id="updateLastName" name="lastName" placeholder="Your last name" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline"
                           value="<c:if test="${sessionScope.authenticatedUser != null}">${sessionScope.authenticatedUser.lastName}</c:if>">
                </div>

                <div class="flex flex-col pt-4">
                    <label for="updatePassword" class="text-lg">Password</label>
                    <input type="password" id="updatePassword" name="password" placeholder="New Password" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline">
                </div>

                <div class="flex flex-col pt-4">
                    <label for="confirmPassword" class="text-lg">Confirm Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm new Password" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline">
                </div>

                <input type="submit" value="Update" name="update" class="bg-black text-white font-bold text-lg hover:bg-gray-700 p-2 mt-8">
                <input type="submit" value="Cancel" name="cancel" class="bg-black text-white font-bold text-lg hover:bg-gray-700 p-2 mt-8">
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
