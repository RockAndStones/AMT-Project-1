const { loginPage } = inject();

module.exports = function() {
  return actor({

    loginTestUser: function() {
      loginPage.loginUser("test", "test");
    }

  });
}
