const { I, loginFormFragment, registerFormFragment } = inject();

module.exports = {
    url: 'login',
    pageTitle: 'Login',

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
