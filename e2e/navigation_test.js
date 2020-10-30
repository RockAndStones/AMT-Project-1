const {homePage, loginPage, newQuestionPage, profilePage, statisticsPage, sidebarFragment } = inject();

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
    I.waitForElement(sidebarFragment.root, 5);
    homePage.components.sidebar.goToNewQuestionPage();
    I.seeInTitle(newQuestionPage.pageTitle);
  });

  Scenario('Home => Profile', (I) => {
    I.loginTestUser();
    homePage.components.header.goToProfilePage();
    I.seeInTitle(profilePage.pageTitle);
  });

  Scenario('Home => Statistics', (I) => {
    I.loginTestUser();
    I.waitForElement(sidebarFragment.root, 5);
    homePage.components.sidebar.goToStatisticsPage();
    I.seeInTitle(statisticsPage.pageTitle);
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

  Scenario('New question => Statistics', (I) => {
    I.loginTestUser();
    I.amOnPage(newQuestionPage.url);
    I.waitForElement(sidebarFragment.root, 5);
    newQuestionPage.components.sidebar.goToStatisticsPage();
    I.seeInTitle(statisticsPage.pageTitle);
  });

  Scenario('Statistics => Home', (I) => {
    I.loginTestUser();
    I.amOnPage(statisticsPage.url);
    statisticsPage.components.header.goToHomePage();
    I.seeInTitle(homePage.pageTitle);
  });

  Scenario('Statistics => New question', (I) => {
    I.loginTestUser();
    I.amOnPage(statisticsPage.url);
    I.waitForElement(sidebarFragment.root, 5);
    statisticsPage.components.sidebar.goToNewQuestionPage();
    I.seeInTitle(newQuestionPage.pageTitle);
  });

  Scenario('Statistics => Profile', (I) => {
    I.loginTestUser();
    I.amOnPage(statisticsPage.url);
    statisticsPage.components.header.goToProfilePage();
    I.seeInTitle(profilePage.pageTitle);
  });





