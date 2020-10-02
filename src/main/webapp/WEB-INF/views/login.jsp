<%--
  Created by IntelliJ IDEA.
  User: Bruno
  Date: 22.09.2020
  Time: 14:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Login</title>
    <link href="assets/css/custom.css" rel="stylesheet" type="text/css"/>
    <link href="assets/css/styles.css" rel="stylesheet" type="text/css"/>
    <link href="assets/css/normalize.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="assets/javascript/login.js"></script>
</head>
<body class="bg-white font-family-karla h-screen">

    <div class="w-full flex flex-wrap">

        <div class="w-full md:w-1/3 flex flex-col">
            <!-- Logo -->
            <div class="flex justify-center md:justify-start pt-12 md:pl-12 md:-mb-24">
                <a href="home" class="bg-black text-white font-bold text-xl p-4">StoneOverflow</a>
            </div>

            <!-- Login Section -->
            <div id="loginSection" class="flex flex-col justify-center md:justify-start my-auto pt-8 md:pt-0 px-8 md:px-24 lg:px-32">
                <p id="loginLabel" class="text-center text-3xl">Welcome.</p>
                <form action="${pageContext.request.contextPath}/loginCommand" method="POST" class="flex flex-col pt-3 md:pt-8">
                    <c:if test="${errorMessage != null}">
                        <p class="text-center text-xl text-red-700">${errorMessage}</p>
                    </c:if>
                    <div class="flex flex-col pt-4">
                        <label for="loginUsername" class="text-lg">Username</label>
                        <input type="text" id="loginUsername" name="username" placeholder="Your awesome username" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline">
                    </div>

                    <div class="flex flex-col pt-4">
                        <label for="loginPassword" class="text-lg">Password</label>
                        <input type="password" id="loginPassword" name="password" placeholder="Password" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline">
                    </div>

                    <input type="submit" value="Log In" class="bg-black text-white font-bold text-lg hover:bg-gray-700 p-2 mt-8">
                </form>
                <div class="text-center pt-12 pb-12">
                    <p>Don't have an account? <span onClick="showRegister()" class="underline font-semibold">Register here.</span></p>
                </div>
            </div>

            <!-- Register Section -->
            <div id="registerSection" class="hidden flex flex-col justify-center md:justify-start my-auto pt-8 md:pt-0 px-8 md:px-24 lg:px-32">
                <p id="registerLabel" class="text-center text-3xl">Join Us.</p>
                <form action="${pageContext.request.contextPath}/registerCommand" method="POST" class="flex flex-col pt-3 md:pt-8">
                    <div class="flex flex-col pt-4">
                        <label for="registerUsername" class="text-lg">Username</label>
                        <input type="text" id="registerUsername" name="username" placeholder="Your awesome username" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline">
                    </div>

                    <div class="flex flex-col pt-4">
                        <label for="registerPassword" class="text-lg">Password</label>
                        <input type="password" id="registerPassword" name="password" placeholder="Password" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline">
                    </div>

                    <div class="flex flex-col pt-4">
                        <label for="confirmPassword" class="text-lg">Confirm Password</label>
                        <input type="password" id="confirmPassword" placeholder="Password" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mt-1 leading-tight focus:outline-none focus:shadow-outline">
                    </div>

                    <input type="submit" value="Register" class="bg-black text-white font-bold text-lg hover:bg-gray-700 p-2 mt-8">
                </form>
                <div class="text-center pt-12 pb-12">
                    <p>Already have an account? <span onClick="showLogin()" class="underline font-semibold">Log in here.</span></p>
                </div>
            </div>

        </div>

        <!-- Image Section -->
        <div class="w-2/3 shadow-2xl">
            <img class="object-cover w-full h-screen hidden md:block" src="https://api.time.com/wp-content/uploads/2018/03/rocks-students-armed.jpg">
        </div>
    </div>
</body>
</html>


