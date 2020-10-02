const { I, loginFormFragment, registerFormFragment, headerFragment, sidebarFragment } = inject();

module.exports = {
    url: 'login',

    components: {
        loginForm: loginFormFragment,
        registerForm: registerFormFragment
    },

    links: {
      home: {css: 'a[href=home]'}
    },

    goToHomePage(){
      I.clickLink(this.links.home);
    }
}
