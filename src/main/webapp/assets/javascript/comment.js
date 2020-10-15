function showCommentForm(formId) {
    if(document.getElementById(formId).classList.contains("hidden")){
        document.getElementById(formId).classList.remove("hidden");
    } else {
        document.getElementById(formId).classList.add("hidden");
    }
}