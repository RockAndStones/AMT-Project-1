const { I, loginFragment, registerFragment, headerFragment, sidebarFragment } = inject();

module.exports = {
    url: 'login',

    components: {
        loginForm: loginFragment,
        registerForm: registerFragment
    },

    links: {
      home: {css: 'a[href=home]'}
    },

    goToHomePage(){
      I.clickLink(this.links.home);
    }
}
