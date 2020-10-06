const { headerFragment, sidebarFragment, newQuestionFormFragment } = inject();

module.exports = {
    url: 'addQuestion',
    pageTitle: 'New question',

    components: {
        header: headerFragment,
        sidebar: sidebarFragment,
        newQuestionForm: newQuestionFormFragment
    }
}
