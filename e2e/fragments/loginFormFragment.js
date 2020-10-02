const { I, homePage } = inject();

module.exports = {
    root: {id: 'loginSection'},

    elements: {
      login: '#loginLabel'
    },
    fields: {
        username: '#loginUsername',
        password: '#loginPassword',
    },
    buttons: {
        submit: {css: 'input[value="Log In"]'},
    },
    scripts: {
        showRegisterForm: "showRegister()"
    },

    loginUser(username, password) {
        within(this.root, () => {
            I.fillField(this.fields.username, username);
            I.fillField(this.fields.password, password);
            I.click(this.buttons.submit);
        });
        I.amOnPage(homePage.url);
    },

    showRegisterForm(){
        within(this.root, () => {
            I.executeScript(this.scripts.showRegisterForm);
        });
    },
}
