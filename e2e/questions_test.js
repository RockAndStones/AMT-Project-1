const { homePage, newQuestionPage } = inject();
const randomstring= require('randomstring');

Feature('Adding a new question');

Scenario('Adding a new question', (I) => {
    const title = randomstring.generate(10);
    const description = randomstring.generate(20);

    I.loginTestUser();
    homePage.components.sidebar.goToNewQuestionPage();
    newQuestionPage.components.newQuestionForm.addQuestion(title, description);
    I.seeInTitle(homePage.pageTitle);
    within(homePage.components.questionList.root, () => {
        I.see(title);
        I.see(description);
    })
});

