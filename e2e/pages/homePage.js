const { headerFragment, sidebarFragment } = inject();

module.exports = {
    url: "home",

    components: {
        header: headerFragment,
        sidebar: sidebarFragment
    }
}
