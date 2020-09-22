Feature('Login page');

Scenario('Show register form then login form', (I) => {
    I.amOnPage('http://localhost:8080/StoneOverflow-1.0-SNAPSHOT/login');
    I.click('//html/body/div/div[1]/div[2]/div/p/span'); // "Register here."
    I.seeElement("//html/body/div/div[1]/div[3]/form/input"); // "Register" button
    I.click('//html/body/div/div[1]/div[3]/div/p/span'); // "Log in here."
    I.seeElement("//html/body/div/div[1]/div[2]/form/input"); // "Log In" button
});