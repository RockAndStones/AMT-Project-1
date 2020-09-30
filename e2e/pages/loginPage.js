const { loginFragment, registerFragment } = inject();

module.exports = {
    url: 'login',

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
