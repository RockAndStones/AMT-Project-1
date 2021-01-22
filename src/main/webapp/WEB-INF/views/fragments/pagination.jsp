<%--
  Created by IntelliJ IDEA.
  User: stéphane
  Date: 19.10.2020
  Time: 14:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="flex items-center justify-between">
    <div class="flex-1 flex justify-between sm:hidden">
        <a href="<c:if test="${pagination.currentPage == 1}">#</c:if><c:if test="${pagination.currentPage != 1}">${path}?${pageArg}=${pagination.currentPage-1}</c:if><c:if test="${param.f != null}">&f=${param.f}</c:if><c:if test="${param.s != null}">&s=${param.s}</c:if>"
           class="relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm leading-5 font-medium rounded-md text-gray-700 bg-white hover:text-gray-500 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 active:bg-gray-100 active:text-gray-700 transition ease-in-out duration-150">
            Previous
        </a>
        <a href="<c:if test="${pagination.currentPage == pagination.totalPages}">#</c:if><c:if test="${pagination.currentPage != pagination.totalPages}">${path}?${pageArg}=${pagination.currentPage+1}</c:if><c:if test="${param.f != null}">&f=${param.f}</c:if><c:if test="${param.s != null}">&s=${param.s}</c:if>" class="ml-3 relative inline-flex items-center px-4 py-2 border border-gray-300 text-sm leading-5 font-medium rounded-md text-gray-700 bg-white hover:text-gray-500 focus:outline-none focus:shadow-outline-blue focus:border-blue-300 active:bg-gray-100 active:text-gray-700 transition ease-in-out duration-150">
            Next
        </a>
    </div>
    <div class="hidden sm:flex-1 sm:flex sm:items-center sm:justify-between">
        <div>
            <p class="text-sm leading-5 text-gray-700">
                Showing
                <span class="font-medium">${pagination.startItem + 1}</span>
                to
                <span class="font-medium">${pagination.lastItem}</span>
                of
                <span class="font-medium">${pagination.itemRepoSize}</span>
                results
            </p>
        </div>
        <div>
            <nav class="relative z-0 inline-flex shadow-sm">
                <a href="<c:if test="${pagination.currentPage == 1}">#</c:if><c:if test="${pagination.currentPage != 1}">${path}?${pageArg}=${pagination.currentPage-1}<c:if test="${param.f != null}">&f=${param.f}</c:if><c:if test="${param.s != null}">&s=${param.s}</c:if></c:if>" class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm leading-5 font-medium text-gray-500 hover:text-gray-400 focus:z-10 focus:outline-none focus:border-blue-300 focus:shadow-outline-blue active:bg-gray-100 active:text-gray-500 transition ease-in-out duration-150" aria-label="Previous">
                    <!-- Heroicon name: chevron-left -->
                    <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                        <path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd" />
                    </svg>
                </a>

                <a href="${path}?${pageArg}=1<c:if test="${param.f != null}">&f=${param.f}</c:if><c:if test="${param.s != null}">&s=${param.s}</c:if>" class="-ml-px relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm leading-5 font-medium text-gray-700 hover:text-gray-500 focus:z-10 focus:outline-none focus:border-blue-300 focus:shadow-outline-blue active:bg-gray-100 active:text-gray-700 transition ease-in-out duration-150">
                    1
                </a>

                <c:if test="${pagination.startPage != 1 && pagination.startPage - 1 != 1}">
                        <span class="-ml-px relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm leading-5 font-medium text-gray-700">
                          ...
                        </span>
                </c:if>

                <!-- Show the pages available -->
                <c:forEach begin="${pagination.startPage}" end="${pagination.lastPage}" varStatus="loop">
                    <c:choose>
                        <c:when test="${loop.index <= 1}">
                        </c:when>
                        <c:when test="${loop.index >= pagination.totalPages}">
                        </c:when>
                        <c:otherwise>
                            <a href="${path}?${pageArg}=${loop.index}<c:if test="${param.f != null}">&f=${param.f}</c:if><c:if test="${param.s != null}">&s=${param.s}</c:if>" class="-ml-px relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm leading-5 font-medium text-gray-700 hover:text-gray-500 focus:z-10 focus:outline-none focus:border-blue-300 focus:shadow-outline-blue active:bg-gray-100 active:text-gray-700 transition ease-in-out duration-150">
                                    ${loop.index}
                            </a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${pagination.lastPage != pagination.totalPages && pagination.lastPage + 1 != pagination.totalPages}">
                        <span class="-ml-px relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm leading-5 font-medium text-gray-700">
                          ...
                        </span>
                </c:if>

                <c:if test="${pagination.totalPages > 1}">
                    <a href="${path}?${pageArg}=${pagination.totalPages}<c:if test="${param.f != null}">&f=${param.f}</c:if><c:if test="${param.s != null}">&s=${param.s}</c:if>" class="-ml-px relative inline-flex items-center px-4 py-2 border border-gray-300 bg-white text-sm leading-5 font-medium text-gray-700 hover:text-gray-500 focus:z-10 focus:outline-none focus:border-blue-300 focus:shadow-outline-blue active:bg-gray-100 active:text-gray-700 transition ease-in-out duration-150">
                            ${pagination.totalPages}
                    </a>
                </c:if>
                <a href="<c:if test="${pagination.currentPage == pagination.totalPages}">#</c:if><c:if test="${pagination.currentPage != pagination.totalPages}">${path}?${pageArg}=${pagination.currentPage+1}<c:if test="${param.f != null}">&f=${param.f}</c:if><c:if test="${param.s != null}">&s=${param.s}</c:if></c:if>" class="-ml-px relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm leading-5 font-medium text-gray-500 hover:text-gray-400 focus:z-10 focus:outline-none focus:border-blue-300 focus:shadow-outline-blue active:bg-gray-100 active:text-gray-500 transition ease-in-out duration-150" aria-label="Next">
                    <!-- Heroicon name: chevron-right -->
                    <svg class="h-5 w-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                        <path fill-rule="evenodd" d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z" clip-rule="evenodd" />
                    </svg>
                </a>
            </nav>
        </div>
    </div>
</div>
