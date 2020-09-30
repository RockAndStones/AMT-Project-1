const { loginPage } = inject();

Feature('Login');

Scenario('Failed login', (I, loginFragment) => {
    I.amOnPage('login');
    loginPage.loginUser("M'aWrongBoii", "DaSecurePassword");
    I.seeElement(loginFragment.elements.login);
});

Scenario('Successful login', (I) => {
    I.amOnPage('login');
    I.loginTestUser();
    I.seeInCurrentUrl('home');
});
