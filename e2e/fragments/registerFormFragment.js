const { I, homePage } = inject();

module.exports = {
   root: {id: 'registerSection'},

   elements: {
       register: '#registerLabel'
   },
  fields: {
      username:         '#registerUsername',
      email:            '#registerEmail',
      firstName:        '#registerFirstName',
      lastName:         '#registerLastName',
      password:         '#registerPassword',
      confirmPassword : '#confirmPassword'
  },
  buttons: {
      submit: {css: 'input[value=Register]'},
  },
  scripts: {
      showLoginForm: "showLogin()"
  },

  registerUser(username, email, firstName, lastName, password) {
      within(this.root, () => {
          I.fillField(this.fields.username, username);
          I.fillField(this.fields.email, email);
          I.fillField(this.fields.firstName, firstName);
          I.fillField(this.fields.lastName, lastName);
          I.fillField(this.fields.password, password);
          I.fillField(this.fields.confirmPassword, password);
          I.click(this.buttons.submit);
      });
  },

  showLoginForm(){
      within(this.root, () => {
          I.executeScript(this.scripts.showLoginForm);
      });
  }
}
