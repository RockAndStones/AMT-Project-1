<%--
  Created by IntelliJ IDEA.
  User: stéphane
  Date: 28.09.2020
  Time: 17:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.bundle.min.js" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
</head>
<body>
<form action="${pageContext.request.contextPath}/submitQuestion.do" method="POST">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-12 col-md-8 col-lg-8 col-xl-6">
                <div class="row">
                    <div class="col text-center">
                        <h1>New Question</h1>
                        <p class="text-h3">Far far away, behind the word mountains, far from the countries Vokalia and Consonantia. </p>
                    </div>
                </div>
                <div class="row align-items-center">
                    <div class="col mt-4">
                        <label for="title">Title</label>
                        <input type="text" id="title" name="title" class="form-control" placeholder="Title">
                    </div>
                </div>
                <div class="row align-items-center mt-4">
                    <div class="col">
                        <label for="description">Description</label>
                        <textarea id="description" rows = "5" cols = "60"  class="form-control" placeholder="Enter details here..." name="description" ></textarea>
                    </div>
                </div>

                <div class="row justify-content-start mt-4">
                    <div class="col">
                        <button type="submit" class="btn btn-primary mt-4">Submit</button>
                        <button type="submit" class="btn btn-secondary mt-4" formaction="../inbox.php">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
</body>
</html>
