const { loginPage, homePage } = inject();
const randomstring= require('randomstring');

Feature('Login');

    Scenario('Failed login', (I) => {
        const wrongUsername = randomstring.generate(15);
        const wrongPassword = randomstring.generate(15);

        I.amOnPage(loginPage.url);
        loginPage.components.loginForm.loginUser(wrongUsername, wrongPassword);
        I.seeInCurrentUrl(loginPage.url);
        I.see(loginPage.errorMessages.invalidLogin);
    });


    Scenario('Successful login', (I) => {
        I.loginTestUser();
        I.seeInCurrentUrl(homePage.url);
        within(homePage.components.header.root, () =>{
            I.see(I.sampleData.userInfo.username.charAt(0).toUpperCase() + I.sampleData.userInfo.username.slice(1));
        });
    });

Feature ('Logout');
    Scenario('Login, logout', (I) => {
        I.loginTestUser();
        I.seeInCurrentUrl(homePage.url);
        within(homePage.components.header.root, () =>{
            I.see(I.sampleData.userInfo.username.charAt(0).toUpperCase() + I.sampleData.userInfo.username.slice(1));
        });
        homePage.components.sidebar.logout();
        I.seeElement(homePage.components.header.links.login);
    });

Feature('Register');

    let wrongPasswords = new DataTable(['password']);
    wrongPasswords.add(['Abcde6!']);  // Only 7 chars
    wrongPasswords.add(['abcdef7!']); // No uppercase
    wrongPasswords.add(['ABCDEF7!']); // No lowercase
    wrongPasswords.add(['Abcdefg!']); // No number
    wrongPasswords.add(['Abcdef78']); // No special char


    Data(wrongPasswords).Scenario('Failed register - Missing minimum password requirements', (I, current) => {
        const newUsername   = randomstring.generate(10);
        const newEmail      = randomstring.generate(10) + '@test.com';
        const newFirstName  = randomstring.generate(10);
        const newLastName   = randomstring.generate(10);

        I.amOnPage(loginPage);
        loginPage.components.loginForm.showRegisterForm();
        I.seeElement(loginPage.components.registerForm.elements.register);
        loginPage.components.registerForm.registerUser(newUsername, newEmail, newFirstName, newLastName, current.password);
        I.seeInCurrentUrl(loginPage.url);
        I.see(loginPage.errorMessages.invalidRegisterPassword);
    });
/*
    Scenario('Successful register', (I) => {
        const newUsername   = randomstring.generate(10);
        const newEmail      = randomstring.generate(10) + '@test.com';
        const newFirstName  = randomstring.generate(10);
        const newLastName   = randomstring.generate(10);
        const newPassword   = I.sampleData.userInfo.password;

        I.amOnPage(loginPage);
        loginPage.components.loginForm.showRegisterForm();
        I.seeElement(loginPage.components.registerForm.elements.register);
        loginPage.components.registerForm.registerUser(newUsername, newEmail, newFirstName, newLastName, newPassword);
        I.seeInCurrentUrl(homePage.url);
        within(homePage.components.header.root, () =>{
            I.see(newUsername.charAt(0).toUpperCase() + newUsername.slice(1));
        });
    });
*/