const { loginPage, homePage } = inject();

Feature('Login');

Scenario('Failed login', (I) => {
    I.amOnPage(loginPage.url);
    loginPage.components.loginForm.loginUser("M'aWrongBoii", "DaSecurePassword");
    I.seeInCurrentUrl(loginPage.url);
});

Scenario('Successful login', (I) => {
    I.amOnPage(loginPage.url);
    I.loginTestUser();
    I.seeInCurrentUrl(homePage.url);
});

/*
Feature('Register');

Scenario('Successful register', (I) => {
    I.amOnPage(loginPage);
    loginPage.components.loginForm.showRegisterForm();
    I.seeElement(loginPage.components.registerForm.elements.register);
    loginPage.components.registerForm.registerUser("test2", "test2");
    I.seeInCurrentUrl(homePage.url);
    homePage.components.sidebar.goToNewQuestionPage();
    I.seeInCurrentUrl(newQuestionPage.url);
});
*/
