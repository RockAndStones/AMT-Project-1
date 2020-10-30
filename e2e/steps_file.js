const { I, loginPage } = inject();

module.exports = function() {
  return actor({

    sampleData: {
      userInfo: {
        username:   'e2eTester',
        email:      'e2e@test.com',
        firstName:  'John',
        lastName:   'Smith',
        password:   'Abcdef7!'
      },
      initialQuestion: {
        title: 'E2e testing question',
        description: 'E2e testing question description'
      },
      initialAnswer: {
        content: 'E2e testing answer'
      },
      initialComments: {
        toQuestion: 'E2e testing comment to question',
        toAnswer: 'E2e testing comment to answer'
      }
    },

    loginTestUser: function() {
      I.amOnPage(loginPage.url);
      loginPage.components.loginForm.loginUser(this.sampleData.userInfo.username, this.sampleData.userInfo.password);
    },

  });
}
