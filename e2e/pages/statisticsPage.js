const { headerFragment, sidebarFragment } = inject();

module.exports = {
    url: 'statistics',
    pageTitle: 'Statistics',

    components: {
        header: headerFragment,
        sidebar: sidebarFragment,
    }
}
