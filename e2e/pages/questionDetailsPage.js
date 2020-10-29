const { I, headerFragment, sidebarFragment } = inject();

module.exports = {
    url: 'questionDetails',

    pageTitle: 'Question details',

    components: {
        header: headerFragment,
        sidebar: sidebarFragment,
        question: '#question',
        answersList: '#answersList',
        answer: '.userMessageClass',
        commentForm: {
            root: '#commentForm',
            elements: {
                comment: '.commentClass',
            },
            buttons: {
                showForm: '#showCommentForm',
                submitComment: '.submitCommentClass'
            },
            fields: {
                commentContent: '.textAreaCommentClass'
            },
        },
        voteForm: {
            voteUp: '',
            voteDown: ''
        },
        answerForm: {
            answer: '#responseDescription',
            submit: '#answerFormSubmit'
        }
    },

    addComment(uuid, commentContent){
        I.click(this.components.commentForm.buttons.showForm + uuid);
        within(this.components.commentForm.root +  uuid, () => {
            I.fillField(this.components.commentForm.fields.commentContent, commentContent);
            I.click(this.components.commentForm.buttons.submitComment);
        });
    },

    addAnswer(answerContent){
        I.fillField(this.components.answerForm.answer, answerContent);
        I.click(this.components.answerForm.submit);
    }
}
