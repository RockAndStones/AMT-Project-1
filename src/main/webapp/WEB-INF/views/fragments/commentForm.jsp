<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<button id="showCommentForm${param.targetUUID}" onClick="showCommentForm('commentForm${param.targetUUID}')" class="showFormCommentClass font-semibold hover:text-black">Add a comment</button>

<form id="commentForm${param.targetUUID}" action="${pageContext.request.contextPath}/submitComment.do" method="post" class="hidden max-w-3xl">
    <input type="hidden" name="targetUUID" value="${param.targetUUID}">
    <input type="hidden" name="targetType" value="${param.commentType}">
    <input type="hidden" name="questionUUID" value="${question.uuid}">
    <div class="flex flex-col mt-1 mb-2">
        <textarea name="commentContent" placeholder="Please be sure to provide a clear and detailed comment to contribute to StoneOverflow." class="textAreaCommentClass h-16 shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"></textarea>
    </div>
    <button type="submit" class="submitCommentClass w-auto bg-black text-white font-bold text-lg hover:bg-gray-700 p-2">Submit</button>
</form>
