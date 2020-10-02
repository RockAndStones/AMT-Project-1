const {homePage, loginPage, newQuestionPage } = inject();

Feature('Navigation');

Scenario('Home => Home', (I) => {
  I.amOnPage(homePage.url);
  homePage.components.header.goToHomePage();
  I.seeInCurrentUrl(homePage.url);
});

Scenario('Home => Login', (I) => {
  I.amOnPage(homePage.url);
  homePage.components.header.goToLoginPage();
  I.seeInCurrentUrl(loginPage.url);
});

Scenario('Home => New question', (I) => {
  I.loginTestUser();
  homePage.components.sidebar.goToNewQuestionPage();
  I.seeInCurrentUrl(newQuestionPage.url);
});

Scenario('Login => Home', (I) => {
  I.amOnPage('login');
  loginPage.goToHomePage();
  I.seeInCurrentUrl(homePage.url);
});

Scenario('New question => Home', (I) => {
  I.loginTestUser();
  I.amOnPage(newQuestionPage.url);
  newQuestionPage.components.header.goToHomePage();
  I.seeInCurrentUrl(homePage.url);
});

Scenario('New question => Login', (I) => {
  I.loginTestUser();
  I.amOnPage(newQuestionPage.url);
  newQuestionPage.components.header.goToLoginPage();
  I.seeInCurrentUrl(loginPage.url);
});


