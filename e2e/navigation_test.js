const {homePage, loginPage, newQuestionPage } = inject();

Feature('Navigation');

Scenario('Home => Home', (I) => {
  I.amOnPage(homePage.url);
  homePage.components.header.goToHomePage();
  I.seeInTitle(homePage.pageTitle);
});

Scenario('Home => Login', (I) => {
  I.amOnPage(homePage.url);
  homePage.components.header.goToLoginPage();
  I.seeInTitle(loginPage.pageTitle);
});

Scenario('Home => New question', (I) => {
  I.loginTestUser();
  homePage.components.sidebar.goToNewQuestionPage();
  I.seeInTitle(newQuestionPage.pageTitle);
});

Scenario('Login => Home', (I) => {
  I.amOnPage('login');
  loginPage.goToHomePage();
  I.seeInTitle(homePage.pageTitle);
});

Scenario('New question => Home', (I) => {
  I.loginTestUser();
  I.amOnPage(newQuestionPage.url);
  newQuestionPage.components.header.goToHomePage();
  I.seeInTitle(homePage.pageTitle);
});

Scenario('New question => Login', (I) => {
  I.loginTestUser();
  I.amOnPage(newQuestionPage.url);
  newQuestionPage.components.header.goToLoginPage();
  I.seeInTitle(loginPage.pageTitle);
});


