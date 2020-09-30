const { I } = inject();

module.exports = {
   root: '#registerSection',

   elements: {
       register: '#registerLabel'
   },
  fields: {
      username: '#registerUsername',
      password: '#registerPassword',
      confirmPassword : '#confirmPassword'
  },
  buttons: {
      submit: {css: 'input[value=register]'},
      showLoginForm: {css: 'span[onClick=showLogin()]'}
  },

  registerUser(username, password) {
      within(this.root, () => {
          I.fillField(this.fields.username, username);
          I.fillField(this.fields.password, password);
          I.fillField(this.fields.confirmPassword, password);
          I.click(this.buttons.submit);
      });
  },

  showLoginForm(){
      within(this.root, () => {
          I.click(this.buttons.showLoginForm);
      });
  }
}
