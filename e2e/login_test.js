const { loginPage, homePage, newQuestionPage } = inject();
const randomstring= require('randomstring');

Feature('Login');

    Scenario('Failed login', (I) => {
        const wrongUsername = randomstring.generate(10);
        const wrongPassword = randomstring.generate(10);

        I.amOnPage(loginPage.url);
        loginPage.components.loginForm.loginUser(wrongUsername, wrongPassword);
        I.amOnPage(newQuestionPage.url);
        I.seeInTitle(loginPage.pageTitle);
    });

    Scenario('Successful login', (I) => {
        I.amOnPage(loginPage.url);
        I.loginTestUser();
        I.amOnPage(newQuestionPage.url);
        I.seeInTitle(newQuestionPage.pageTitle);
    });


Feature('Register');

    Scenario('Successful register', (I) => {
        const newUsername = randomstring.generate(10);
        const newPassword = randomstring.generate(10);

        I.amOnPage(loginPage);
        loginPage.components.loginForm.showRegisterForm();
        I.seeElement(loginPage.components.registerForm.elements.register);
        loginPage.components.registerForm.registerUser(newUsername, newPassword);
        I.seeInTitle(homePage.pageTitle);
        homePage.components.sidebar.goToNewQuestionPage();
        I.seeInTitle(newQuestionPage.pageTitle);
    });

