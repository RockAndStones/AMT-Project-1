const { I } = inject();

module.exports = {
    root: '#loginSection',

    elements: {
      login: '#loginLabel'
    },
    fields: {
        username: '#loginUsername',
        password: '#loginPassword',
    },
    buttons: {
        submit: {css: 'input[value="Log In"]'},
        showRegisterForm: {css: 'span[onClick=showRegister()]'}
    },

    loginUser(username, password) {
        within(this.root, () => {
            I.fillField(this.fields.username, username);
            I.fillField(this.fields.password, password);
            I.click(this.buttons.submit);
        });
    },

    showRegisterForm(){
        within(this.root, () => {
            I.click(this.buttons.showRegisterForm);
        });
    },
}
