const { I, loginPage } = inject();

module.exports = function() {
  return actor({

    credentials: {
      username:   'e2e-tester',
      email:      'e2e@test.com',
      firstName:  'John',
      lastName:   'Smith',
      password:   'Abcdef7!'
    },

    loginTestUser: function() {
      I.amOnPage(loginPage.url);
      loginPage.components.loginForm.loginUser(this.credentials.username, this.credentials.password);
    },

  });
}
