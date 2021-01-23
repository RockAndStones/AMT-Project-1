const {homePage, loginPage, newQuestionPage, profilePage, statisticsPage, historyPage, sidebarFragment } = inject();

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

  Scenario('Home => History', (I) => {
    I.loginTestUser();
    I.waitForElement(sidebarFragment.root, 5);
    homePage.components.sidebar.goToHistoryPage();
    I.seeInTitle(historyPage.pageTitle);
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

  Scenario('New question => History', (I) => {
    I.loginTestUser();
    I.amOnPage(newQuestionPage.url);
    I.waitForElement(sidebarFragment.root, 5);
    newQuestionPage.components.sidebar.goToHistoryPage();
    I.seeInTitle(historyPage.pageTitle);
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

  Scenario('Statistics => History', (I) => {
    I.loginTestUser();
    I.amOnPage(statisticsPage.url);
    I.waitForElement(sidebarFragment.root, 5);
    statisticsPage.components.sidebar.goToHistoryPage();
    I.seeInTitle(historyPage.pageTitle);
  });

  Scenario('History => Home', (I) => {
    I.loginTestUser();
    I.amOnPage(historyPage.url);
    historyPage.components.header.goToHomePage();
    I.seeInTitle(homePage.pageTitle);
  });

  Scenario('History => New Question', (I) => {
    I.loginTestUser();
    I.amOnPage(historyPage.url);
    I.waitForElement(sidebarFragment.root, 5);
    historyPage.components.sidebar.goToNewQuestionPage();
    I.seeInTitle(newQuestionPage.pageTitle);
  });

  Scenario('History => Statistics', (I) => {
    I.loginTestUser();
    I.amOnPage(historyPage.url);
    I.waitForElement(sidebarFragment.root, 5);
    historyPage.components.sidebar.goToStatisticsPage();
    I.seeInTitle(statisticsPage.pageTitle);
  });

  Scenario('History => Profile', (I) => {
    I.loginTestUser();
    I.amOnPage(historyPage.url);
    historyPage.components.header.goToProfilePage();
    I.seeInTitle(profilePage.pageTitle);
  });





