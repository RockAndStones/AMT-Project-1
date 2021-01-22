<%--
  Group:        RockAndStone (https://github.com/orgs/tweb-classroom/teams/rockandstone)
  Authors:      Bécaud Arthur, Egremy Bruno, Muller Robin & Teixeira Carvalho Stéphane
  Date:         20.01.2020
  Description:  History page of StoneOverflow
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean scope="request" id="historyOverall" class="java.lang.String"/>
<jsp:useBean scope="request" id="historyPointscale" class="java.lang.String"/>
<jsp:useBean scope="request" id="pointscales" type="ch.heigvd.amt.stoneoverflow.application.gamification.PointScaleHistoryDTO"/>
<!DOCTYPE html>
<html>
<head>
    <title>History</title>
    <link href="assets/css/custom.css" rel="stylesheet" type="text/css"/>
    <link href="assets/css/styles.css" rel="stylesheet" type="text/css"/>
    <link href="assets/css/normalize.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript">
        window.onload = function() {
                <c:if test="${not empty historyOverall}">
                var chartHistoryO = new CanvasJS.Chart("chartHistoryO", {
                    theme: "light2",
                    title: {
                        text: "History Overall"
                    },
                    axisX: {
                        title: "Date"
                    },
                    axisY: {
                        title: "Points",
                        includeZero: true
                    },
                    data: [{
                        type: "line",
                        yValueFormatString: "#,##0p",
                        dataPoints: <%out.print(historyOverall);%>
                    }]
                });
                chartHistoryO.render();
        </c:if>
        <c:if test="${not empty historyPointscale}">
            var chartHistoryP = new CanvasJS.Chart("chartHistoryP", {
                theme: "light2",
                axisX: {
                    title: "Date"
                },
                axisY: {
                    title: "Points",
                    includeZero: true
                },
                data: [{
                    type: "line",
                    yValueFormatString: "#,##0p",
                    dataPoints : <%out.print(historyPointscale);%>
                }]
            });
            chartHistoryP.render();
        </c:if>
        }
    </script>
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
            <c:if test="${not empty historyOverall}">
                <div id="chartHistoryO" style="height: 370px; width: 100%;"></div>
                <script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
                <c:if test="${not empty historyPointscale}">
                    <form name="filter-form" method="get" class="pb-8 border-b flex justify-between">
                        <span class="leading-normal text-2xl font-semibold text-gray-900 mt-1">History by point scale</span>
                        <div class="inline-block relative w-64">
                            <select name="f" onchange="document.forms['filter-form'].submit();" class="block appearance-none w-full bg-white border border-gray-400 hover:border-gray-500 px-4 py-2 pr-8 rounded shadow leading-tight focus:outline-none focus:shadow-outline">
                                <c:forEach items="${pointscales.pointscales}" var="pointscale">
                                    <option value="${pointscale.id}" <c:if test="${param.f == pointscale.id}">selected</c:if>>${pointscale.name}</option>
                                </c:forEach>
                            </select>
                            <div class="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-gray-700">
                                <svg class="fill-current h-4 w-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"><path d="M9.293 12.95l.707.707L15.657 8l-1.414-1.414L10 10.828 5.757 6.586 4.343 8z"/></svg>
                            </div>
                        </div>
                    </form>
                    <div id="chartHistoryP" style="height: 370px; width: 100%;"></div>
                    <script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>
                </c:if>
                <c:if test="${empty historyPointscale}">
                    <span class="leading-normal text-2xl font-semibold text-gray-900 mt-1">Sorry no history was found...</span>
                </c:if>
            </c:if>
            <c:if test="${empty historyOverall}">
                <span class="leading-normal text-2xl font-semibold text-gray-900 mt-1">Sorry no history was found...</span>
            </c:if>
        </div>
</div>
<!-- Footer -->
<%@include file="fragments/footer.jsp" %>
<!-- BottomBar -->
<%@include file="fragments/bottombar.jsp" %>
</body>
</html>
