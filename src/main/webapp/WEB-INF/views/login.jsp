<%--
  Created by IntelliJ IDEA.
  User: stéphane
  Date: 21.09.2020
  Time: 20:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/login" method="post">
    <label for="usernameLogin">Username :</label><br>
    <input type="text" id="usernameLogin" name="username"/><br>
    <input type="submit" name="login" value="Login">
</form>

<form action="${pageContext.request.contextPath}/login" method="post">
    <label for="usernameRegister">Username :</label><br>
    <input type="text" id="usernameRegister" name="username"/><br>
    <input type="submit" name="register" value="Register">
</form>
</body>
</html>
