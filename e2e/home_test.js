Feature('home');

Scenario('test something', (I) => {
  I.amOnPage('http://localhost:8080/StoneOverflow-1.0-SNAPSHOT/home');
  I.see('Questions');
});
