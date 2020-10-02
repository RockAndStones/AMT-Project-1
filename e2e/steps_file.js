const { I, loginPage } = inject();

module.exports = function() {
  return actor({

    loginTestUser: function() {
      I.amOnPage(loginPage.url);
      loginPage.components.loginForm.loginUser("test", "test");
    }

  });
}
