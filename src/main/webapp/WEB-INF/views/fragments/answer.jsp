<div class="flex flex-shrink">
    <%@include file="postVoteAndContent.jsp"%>
</div>
<!-- TMP TESTING ANSWER -->
<c:if test="${not empty $post.comments}">
    <h2 class="leading-normal text-lg font-semibold text-gray-900 mt-6 mb-4">Comment(s)</h2>
    <c:forEach items="${$post.comments}" var="comment">
        <%@include file="comment.jsp" %>
    </c:forEach>
</c:if>
<form action="" method="post" class="mt-6">
    <input type="hidden" name="" value="">
    <button class="font-semibold hover:text-black">Add a comment</button>
</form>
