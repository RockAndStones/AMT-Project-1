const { headerFragment, sidebarFragment, questionListFragment } = inject();

module.exports = {
    url: "home",

    components: {
        header: headerFragment,
        sidebar: sidebarFragment,
        questionList: questionListFragment
    }
}
