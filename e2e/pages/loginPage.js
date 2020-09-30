const { I, loginFragment, registerFragment } = inject();

module.exports = {
    url: 'login',

    links: {
      home: {css: 'a[href=home]'}
    },

    goToHomePage(){
      I.clickLink(this.links.home);
    },

    loginUser(username, password) {
      loginFragment.loginUser(username, password);
    },

    registerUser(username, password) {
        registerFragment.registerUser(username, password);
    },

    showLoginForm() {
        registerFragment.showLoginForm();
    },

    showRegisterForm() {
        loginFragment.showRegisterForm();
    }
}
