const { headerFragment, sidebarFragment, newQuestionFormFragment } = inject();

module.exports = {
    url: "addQuestion",

    components: {
        header: headerFragment,
        sidebar: sidebarFragment,
        newQuestionForm: newQuestionFormFragment
    }
}
