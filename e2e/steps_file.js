const { I, loginPage } = inject();

module.exports = function() {
  return actor({

    credentials: {
      username:   'test',
      email:      'test@test.com',
      firstName:  'John',
      lastName:   'Smith',
      password:   'test'
    },

    loginTestUser: function() {
      I.amOnPage(loginPage.url);
      loginPage.components.loginForm.loginUser(this.credentials.username, this.credentials.password);
    },

  });
}
