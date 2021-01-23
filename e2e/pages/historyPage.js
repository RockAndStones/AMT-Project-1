const { headerFragment, sidebarFragment } = inject();

module.exports = {
    url: 'history',
    pageTitle: 'History',

    components: {
        header: headerFragment,
        sidebar: sidebarFragment,
    }
}