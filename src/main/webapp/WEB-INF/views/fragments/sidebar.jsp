<%--
  Group:        RockAndStone (https://github.com/orgs/tweb-classroom/teams/rockandstone)
  Authors:      Bécaud Arthur, Egremy Bruno, Muller Robin & Teixeira Carvalho Stéphane
  Date:         23.09.2020
  Description:  Sidebar fragment for StoneOverflow.
--%>
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css" integrity="sha384-oS3vJWv+0UjzBfQzYUhtDYW+Pj2yciDJxpsK1OYPAYjqT085Qq/1cq5FLXAZQ7Ay" crossorigin="anonymous">
<div class="hidden lg:block lg:w-1/5 lg:border-r"></div>
<div class="hidden lg:block lg:fixed w-full lg:w-1/5 text-xl text-gray-800 lg:border-r leading-normal">
    <c:set var="baseURL" value="${pageContext.request.requestURI}" />
    <c:set var="activeButton" value="class=\"flex items-center py-2 px-8 bg-gray-200 text-gray-900 border-r-4 border-gray-900\""/>
    <c:set var="inactiveButton" value="class=\"flex items-center mt-5 mb-5 py-2 px-8 text-gray-600 border-r-4 border-white hover:bg-gray-200 hover:text-gray-700 hover:border-gray-700\""/>
    <nav id="sidebar" class="mt-10">
        <!-- Current page (gray-900) -->
        <a href="home" <c:if test="${baseURL.contains(\"home\") == true}">${activeButton}</c:if>
                       <c:if test="${baseURL.contains(\"home\") != true}">${inactiveButton}</c:if>>
            <i class="h-5 w-5 fas fa-bars"></i>
            <span class="mx-4 font-medium">Questions</span>
        </a>

        <a href="addQuestion" <c:if test="${baseURL.contains(\"addQuestion\") == true}">${activeButton}</c:if>
                              <c:if test="${baseURL.contains(\"addQuestion\") != true}">${inactiveButton}</c:if>>
            <i class="h-5 w-5 fas fa-plus"></i>
            <span class="mx-4 font-medium">New Question</span>
        </a>

        <a href="statistics" <c:if test="${baseURL.contains(\"statistics\") == true}">${activeButton}</c:if>
                             <c:if test="${baseURL.contains(\"statistics\") != true}">${inactiveButton}</c:if>>
            <i class="h-5 w-5 fas fa-chart-bar"></i>
            <span class="mx-4 font-medium">Statistics</span>
        </a>

        <c:if test="${sessionScope.authenticatedUser != null}">
            <form action="logout.do" method="post" class="flex items-center mt-5 text-gray-600 border-r-4 border-white hover:bg-gray-200 hover:text-gray-700 hover:border-gray-700">
                <button type="submit" class="flex items-center w-full leading-normal py-2 px-8">
                    <i class="h-5 w-5 fas fa-power-off"></i>
                    <span class="mx-4 tracking-wider font-medium">Logout</span>
                </button>
            </form>
        </c:if>
    </nav>
</div>
