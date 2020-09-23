Feature('Navigation');

Scenario('From home to login', (I) => {
  I.amOnPage('http://localhost:8080/StoneOverflow-1.0-SNAPSHOT/home');
  I.clickLink('Log in/Sign up'); // Upper nav bar link
  I.seeElement("//html/body/div/div[1]/div[2]/form/input"); // "Log In" button on login page
});

Scenario('From login to home', (I) => {
  I.amOnPage('http://localhost:8080/StoneOverflow-1.0-SNAPSHOT/login');
  I.clickLink('StoneOverflow'); // Upper left "StoneOverflow" logo
  I.see("Questions"); // In-page title, main content
});
