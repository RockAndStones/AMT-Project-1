<div class="flex flex-shrink">
    <!-- Vote -->
    <div class="flex flex-col inline-flex items-center">
        <form action="" method="post">
            <input type="hidden" name="type" value="question">
            <input type="hidden" name="id" value="${answer.uuid}">
            <input type="hidden" name="vote" value="up">
            <button type="submit"><i class="leading-normal fas fa-caret-up text-6xl"></i></button>
        </form>
        <span class="text-lg">${answer.nbVotes}</span>
        <form action="" method="post">
            <input type="hidden" name="type" value="question">
            <input type="hidden" name="id" value="${answer.uuid}">
            <input type="hidden" name="vote" value="down">
            <button type="submit"><i class="leading-normal fas fa-caret-down text-6xl"></i></button>
        </form>
    </div>
    <!-- Post content -->
    <div class="ml-6 mt-4 text-gray-700">
        <p>
            ${answer.description}
        </p>
        <span class="inline-block text-sm font-semibold mt-4">${answer.creator}, ${question.date.dateFormatted()} at ${question.date.timeFormatted()}</span>
    </div>
</div>
<!-- TMP TESTING ANSWER -->
<c:if test="${not empty answer.comments}">
    <h2 class="leading-normal text-lg font-semibold text-gray-900 mt-6 mb-4">Comment(s)</h2>
    <c:forEach items="${answer.comments}" var="comment">
        <%@include file="comment.jsp" %>
    </c:forEach>
</c:if>
<form action="" method="post" class="mt-6">
    <input type="hidden" name="" value="">
    <button class="font-semibold hover:text-black">Add a comment</button>
</form>
