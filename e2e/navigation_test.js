const {homePage, loginPage, newQuestionPage, profilePage } = inject();

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

Scenario('Home => Profile', (I) => {
  I.loginTestUser();
  homePage.components.header.goToProfilePage();
  I.seeInTitle(profilePage.pageTitle);
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

Scenario('New question => Profile', (I) => {
  I.loginTestUser();
  I.amOnPage(newQuestionPage.url);
  newQuestionPage.components.header.goToProfilePage();
  I.seeInTitle(profilePage.pageTitle);
});


