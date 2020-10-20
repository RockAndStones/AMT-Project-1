<%--
  Group:        RockAndStone (https://github.com/orgs/tweb-classroom/teams/rockandstone)
  Authors:      Bécaud Arthur, Egremy Bruno, Muller Robin & Teixeira Carvalho Stéphane
  Date:         23.09.2020
  Description:  Header fragment for StoneOverflow.
--%>
<script type="text/javascript" src="assets/javascript/header.js"></script>
<nav id="header" class="bg-white sticky w-full flex flex-wrap justify-between z-10 top-0 py-4 border-b">
    <!-- Logo -->
    <div class="pl-4 flex lg:w-1/5 justify-center">
        <a href="home" class="bg-black text-white font-bold text-l p-3">StoneOverflow</a>
    </div>

    <div class="block lg:hidden flex lg:w-4/5 justify-end pr-4">
        <button id="nav-toggle" onclick="checkHeaderButton()" class="flex items-center px-3 py-2 border rounded text-gray-500 border-gray-600 hover:text-gray-900 hover:border-purple-500 appearance-none focus:outline-none">
            <svg class="fill-current h-3 w-3" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                <title>Menu</title>
                <path d="M0 3h20v2H0V3zm0 6h20v2H0V9zm0 6h20v2H0v-2z"/>
            </svg>
        </button>
    </div>
    <div class="w-full flex-grow lg:flex lg:w-4/5 lg:content-center lg:items-center lg:w-auto hidden lg:block mt-2 lg:mt-0 z-20" id="nav-content">
        <div class="flex-1 w-full mx-auto max-w-sm content-center py-4 lg:py-0">
            <div class="relative pull-right pl-4 pr-4 md:pr-0">
                <form name="search-form" method="get" action="${pageContext.request.contextPath}/home">
                    <input
                        type="search"
                        placeholder="Search"
                        value="<c:if test="${param.s != null}">${param.s}</c:if>"
                        name="s"
                        class="w-full bg-gray-100 text-sm text-gray-800 transition border focus:outline-none focus:border-gray-900 rounded py-1 px-2 pl-10 appearance-none leading-normal">
                    <input type="submit" hidden>
                    <div class="absolute search-icon" style="top: 0.375rem;left: 1.75rem; cursor: pointer;" onclick="document.forms['search-form'].submit();">
                        <svg class="fill-current pointer-events-none text-gray-800 w-4 h-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20">
                            <path d="M12.9 14.32a8 8 0 1 1 1.41-1.41l5.35 5.33-1.42 1.42-5.33-5.34zM8 14A6 6 0 1 0 8 2a6 6 0 0 0 0 12z"></path>
                        </svg>
                    </div>
                </form>
            </div>
        </div>
        <ul class="list-reset lg:flex justify-end items-center">
            <li class="mr-3 py-2 lg:py-0">
                <a class="inline-block py-2 px-4 text-gray-900 font-bold no-underline" href="#">Active</a>
            </li>
            <li class="mr-3 py-2 lg:py-0">
                <c:choose>
                    <c:when test="${sessionScope.authenticatedUser != null}">
                        <a class="inline-block text-gray-600 no-underline hover:text-gray-900 hover:underline py-2 px-4 capitalize" href="#">${sessionScope.authenticatedUser.username}</a>
                    </c:when>
                    <c:otherwise>
                        <a class="inline-block text-gray-600 no-underline hover:text-gray-900 hover:underline py-2 px-4" href="login">Log in/Sign up</a>
                    </c:otherwise>
                </c:choose>
            </li>
        </ul>
    </div>
</nav>
