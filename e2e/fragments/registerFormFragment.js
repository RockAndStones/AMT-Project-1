const { I, homePage } = inject();

module.exports = {
   root: {id: 'registerSection'},

   elements: {
       register: '#registerLabel'
   },
  fields: {
      username: '#registerUsername',
      password: '#registerPassword',
      confirmPassword : '#confirmPassword'
  },
  buttons: {
      submit: {css: 'input[value=Register]'},
  },
    scripts: {
        showLoginForm: "showLogin()"
    },

  registerUser(username, password) {
      within(this.root, () => {
          I.fillField(this.fields.username, username);
          I.fillField(this.fields.password, password);
          I.fillField(this.fields.confirmPassword, password);
          I.click(this.buttons.submit);
      });
      I.amOnPage(homePage.url);
  },

  showLoginForm(){
      within(this.root, () => {
          I.executeScript(this.scripts.showLoginForm);
      });
  }
}
