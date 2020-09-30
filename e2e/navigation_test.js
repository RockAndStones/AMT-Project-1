const {homePage, loginPage, newQuestionPage } = inject();

Feature('Navigation');

Scenario('Navigate from home page to login page', (I) => {
  I.amOnPage('home');
  homePage.goToLoginPage();
  I.seeInCurrentUrl(loginPage.url);
});

Scenario('Navigate from login page to home page', (I) => {
  I.amOnPage('login');
  loginPage.goToHomePage();
  I.seeInCurrentUrl(homePage.url);
});

Scenario('Navigate from home page to new question page', (I) => {
  I.amOnPage('login');
  I.loginTestUser();
  loginPage.goToNewQuestionPage();
  I.seeInCurrentUrl(newQuestionPage.url);
});

