<div messageuuid="${msg.uuid}" class="userMessageClass flex flex-shrink mb-4">
    <!-- Vote -->
    <div class="voteClass flex flex-col inline-flex items-center">
        <form action="${pageContext.request.contextPath}/vote.do" method="post">
            <input type="hidden" name="questionUUID" value="${question.uuid}">
            <input type="hidden" name="targetUUID"   value="${msg.uuid}">
            <input type="hidden" name="targetType"   value="${msg.type}">
            <input type="hidden" name="voteType"     value="UP">
            <button id="voteUp${msg.uuid}" class="voteUp" type="submit"><i class="leading-normal fas fa-caret-up text-6xl <c:if test="${msg.vote.voteType.name() == 'UP' }">text-black</c:if>"></i></button>
        </form>
        <span id="nbVotes${msg.uuid}" class="text-lg">${msg.nbVotes}</span>
        <span id="nbVotes" hidden class="text-lg">${msg.nbVotes}</span>
        <form action="${pageContext.request.contextPath}/vote.do" method="post">
            <input type="hidden" name="questionUUID" value="${question.uuid}">
            <input type="hidden" name="targetUUID"   value="${msg.uuid}">
            <input type="hidden" name="targetType"   value="${msg.type}">
            <input type="hidden" name="voteType"     value="DOWN">
            <button id="voteDown${msg.uuid}" class="voteDown" type="submit"><i class="leading-normal fas fa-caret-down text-6xl <c:if test="${msg.vote.voteType.name() == 'DOWN' }">text-black</c:if>"></i></button>
        </form>
    </div>
    <!-- Post content -->
    <div class="messageBody w-full ml-6 mt-4 text-gray-700">
        <p>
            ${msg.description}
        </p>
        <span class="inline-block text-sm font-semibold mt-4">${msg.creator}, ${msg.date.dateFormatted()} at ${msg.date.timeFormatted()}</span>

        <!-- Comments -->
        <div class="commentSectionClass mt-10 border-l pl-4">
            <c:if test="${not empty msg.comments}">
                <c:forEach items="${msg.comments}" var="comment">
                    <%@include file="comment.jsp" %>
                </c:forEach>
            </c:if>
            <!-- Comment Form -->
            <jsp:include page="fragments/commentForm.jsp" >
                <jsp:param name="commentType" value="${msg.type}" />
                <jsp:param name="targetUUID" value="${msg.uuid}" />
            </jsp:include>
        </div>
    </div>
</div>
