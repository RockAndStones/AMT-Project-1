<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css" integrity="sha384-oS3vJWv+0UjzBfQzYUhtDYW+Pj2yciDJxpsK1OYPAYjqT085Qq/1cq5FLXAZQ7Ay" crossorigin="anonymous">
<nav class="bg-white fixed bottom-0 w-full border-t lg:hidden text-xs">
    <ul class="flex justify-around items-center text-center text-lg font-bold">
        <li class="p-4 bg-gray-200 border-b-4 border-gray-900">
            <a href="${pageContext.request.contextPath}/home">
                <i class="mx-auto mb-2 h-5 w-5 h-5 w-5 fas fa-bars"></i>
                <span class="block font-medium">Questions</span>
            </a>
        </li>
        <li class="p-4 border-b-4 border-white text-gray-600 hover:bg-gray-200 hover:text-gray-700 hover:border-gray-700">
            <a href="${pageContext.request.contextPath}/addQuestion">
                <i class="mx-auto mb-2 h-5 w-5 h-5 w-5 fas fa-plus"></i>
                <span class="block font-medium">New Question</span>
            </a>
        </li>
        <li class="p-4 border-b-4 border-white text-gray-600 hover:bg-gray-200 hover:text-gray-700 hover:border-gray-700">
            <form action="${pageContext.request.contextPath}/logout.do" method="post" class="flex items-center">
                <button type="submit">
                    <i class="mx-auto mb-2 h-5 w-5 h-5 w-5 fas fa-power-off"></i>
                    <span class="block tracking-wider font-medium">Logout</span>
                </button>
            </form>
        </li>
    </ul>
</nav>
