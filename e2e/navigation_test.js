const {homePage, loginPage } = inject();

Feature('Navigation');

Scenario('Navigate from home page to login page', (I) => {
  I.amOnPage('home');
  homePage.goToLoginPage();
  I.seeInCurrentUrl('login');
});

Scenario('Navigate from login page to home page', (I) => {
  I.amOnPage('login');
  loginPage.goToHomePage();
  I.seeInCurrentUrl('login');
});

