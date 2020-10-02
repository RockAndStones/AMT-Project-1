const { homePage, newQuestionPage } = inject();
const assert = require('assert');

Feature('Adding a new question');

Scenario('Adding a new question', (I) => {
    const title = "Test title";
    const description = "Test description";

    I.loginTestUser();
    homePage.components.sidebar.goToNewQuestionPage();
    newQuestionPage.components.newQuestionForm.addQuestion(title, description);
    I.seeInCurrentUrl(homePage.url);
    within(homePage.components.questionList.root, () => {
        I.see(title);
        I.see(description);
    })
});

