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

    errorMessages: {
      invalidLogin: 'Invalid username / password',
      invalidRegisterPassword: 'Password does not meet the minimum requirements (8 characters, 1 lower case, 1 upper case, 1 number, 1 special character)'
    },

    goToHomePage(){
      I.clickLink(this.links.home);
    }
}
