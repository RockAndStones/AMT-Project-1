const { headerFragment, sidebarFragment, questionListFragment } = inject();

module.exports = {
    url: 'home',
    pageTitle: 'Home',

    components: {
        header: headerFragment,
        sidebar: sidebarFragment,
        questionList: questionListFragment
    }
}
