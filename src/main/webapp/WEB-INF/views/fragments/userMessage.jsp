<div class="flex flex-shrink">
    <!-- Vote -->
    <div class="flex flex-col inline-flex items-center">
        <form action="${pageContext.request.contextPath}/vote.do" method="post">
            <input type="hidden" name="questionUUID" value="${question.uuid}">
            <input type="hidden" name="targetUUID"   value="${msg.uuid}">
            <input type="hidden" name="targetType"   value="${msg.type}">
            <input type="hidden" name="voteUUID"     value="${msg.vote.uuid}">
            <input type="hidden" name="voteType"     value="UP">
            <button type="submit"><i class="leading-normal fas fa-caret-up text-6xl <c:if test="${msg.vote.voteType.name() == 'UP' }">text-black</c:if>"></i></button>
        </form>
        <span id="nbVotes" class="text-lg">${msg.nbVotes}</span>
        <form action="${pageContext.request.contextPath}/vote.do" method="post">
            <input type="hidden" name="questionUUID" value="${question.uuid}">
            <input type="hidden" name="targetUUID"   value="${msg.uuid}">
            <input type="hidden" name="targetType"   value="${msg.type}">
            <input type="hidden" name="voteUUID"     value="${msg.vote.uuid}">
            <input type="hidden" name="voteType"     value="DOWN">
            <button type="submit"><i class="leading-normal fas fa-caret-down text-6xl <c:if test="${msg.vote.voteType.name() == 'DOWN' }">text-black</c:if>"></i></button>
        </form>
    </div>
    <!-- Post content -->
    <div class="ml-6 mt-4 text-gray-700">
        <p>
            ${msg.description}
        </p>
        <span class="inline-block text-sm font-semibold mt-4">${msg.creator}, ${msg.date.dateFormatted()} at ${msg.date.timeFormatted()}</span>
    </div>
</div>
<!-- Comments -->
<c:if test="${not empty msg.comments}">
    <h2 class="leading-normal text-lg font-semibold text-gray-900 mt-6 mb-4">Comment(s)</h2>
    <c:forEach items="${msg.comments}" var="comment">
        <%@include file="comment.jsp" %>
    </c:forEach>
</c:if>
<!-- Comment Form -->
<jsp:include page="fragments/commentForm.jsp" >
    <jsp:param name="commentType" value="${msg.type}" />
    <jsp:param name="targetUUID" value="${msg.uuid}" />
</jsp:include>
