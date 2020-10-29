const {homePage, newQuestionPage, questionDetailsPage } = inject();
const randomstring= require('randomstring');

Feature('Adding a new question');

    Scenario('Adding a new question', (I) => {
        const title = randomstring.generate(10);
        const description = randomstring.generate(30);

        I.loginTestUser();
        I.amOnPage(homePage.url);
        homePage.components.sidebar.goToNewQuestionPage();
        newQuestionPage.components.newQuestionForm.addQuestion(title, description);
        I.amOnPage(homePage.url);
        within(homePage.components.questionList.root, () => {
            I.see(title);
            I.see(description);
        })
    });

Feature('Question details');

    Scenario('Verify question details', ( I ) => {
        I.loginTestUser();
        I.amOnPage(homePage.url);
        within(homePage.components.questionList.root, () => {
            I.click(I.sampleData.initialQuestion.title);
        });
        I.seeInCurrentUrl(questionDetailsPage.url);
        I.see(I.sampleData.initialQuestion.title);
        I.see(I.sampleData.initialQuestion.description);
        I.see(I.sampleData.initialAnswer.content);
        I.see(I.sampleData.initialComments.toQuestion);
        I.see(I.sampleData.initialComments.toAnswer);
    });

    Scenario('Add an answer', ( I ) => {
        const answer = randomstring.generate(30);

        I.loginTestUser();
        I.amOnPage(homePage.url);
        within(homePage.components.questionList.root, () => {
            I.click(I.sampleData.initialQuestion.title);
        });
        I.seeInCurrentUrl(questionDetailsPage.url);
        questionDetailsPage.addAnswer(answer);
        I.see(answer);
    });

    Scenario('Vote up a question and removes the upvote', async( I ) => {
        I.loginTestUser();
        I.amOnPage(homePage.url);
        within(homePage.components.questionList.root, () => {
            I.click(I.sampleData.initialQuestion.title);
        });
        I.seeInCurrentUrl(questionDetailsPage.url);
        const questionUUID = await I.grabAttributeFrom(questionDetailsPage.components.question, 'questionuuid');
        var voteCount = await I.grabTextFrom(questionDetailsPage.components.voteForm.voteCount + questionUUID);
        voteCount = parseInt(voteCount);
        voteCount++;

        questionDetailsPage.voteUp(questionUUID);
        I.see(voteCount.toString(), questionDetailsPage.components.voteForm.voteCount + questionUUID);

        // Remove the upvote
        voteCount--;
        questionDetailsPage.voteUp(questionUUID);

        I.see(voteCount.toString(), questionDetailsPage.components.voteForm.voteCount + questionUUID);

    });

    Scenario('Vote down a question and removes the downvote', async( I ) => {
        I.loginTestUser();
        I.amOnPage(homePage.url);
        within(homePage.components.questionList.root, () => {
            I.click(I.sampleData.initialQuestion.title);
        });
        I.seeInCurrentUrl(questionDetailsPage.url);
        const questionUUID = await I.grabAttributeFrom(questionDetailsPage.components.question, 'questionuuid');
        var voteCount = await I.grabTextFrom(questionDetailsPage.components.voteForm.voteCount + questionUUID);
        voteCount = parseInt(voteCount);
        voteCount--;

        questionDetailsPage.voteDown(questionUUID);
        I.see(voteCount.toString(), questionDetailsPage.components.voteForm.voteCount + questionUUID);

        // Remove the downvote
        voteCount++;
        questionDetailsPage.voteDown(questionUUID);

        I.see(voteCount.toString(), questionDetailsPage.components.voteForm.voteCount + questionUUID);
    });

    Scenario('Vote up an answer and removes the upvote', async( I ) => {
        var answerUUID = '';

        I.loginTestUser();
        I.amOnPage(homePage.url);
        within(homePage.components.questionList.root, () => {
            I.click(I.sampleData.initialQuestion.title);
        });
        I.seeInCurrentUrl(questionDetailsPage.url);
        await within(questionDetailsPage.components.answersList, async () => {
            answerUUID = await I.grabAttributeFrom(questionDetailsPage.components.answer, 'messageuuid');
        });
        var voteCount = await I.grabTextFrom(questionDetailsPage.components.voteForm.voteCount + answerUUID);
        voteCount = parseInt(voteCount);
        voteCount++;

        questionDetailsPage.voteUp(answerUUID);
        I.see(voteCount.toString(), questionDetailsPage.components.voteForm.voteCount + answerUUID);

        // Remove the upvote
        voteCount--;
        questionDetailsPage.voteUp(answerUUID);

        I.see(voteCount.toString(), questionDetailsPage.components.voteForm.voteCount + answerUUID);

    });

    Scenario('Vote down an answer and removes the downvote', async( I ) => {
        var answerUUID = '';

        I.loginTestUser();
        I.amOnPage(homePage.url);
        within(homePage.components.questionList.root, () => {
            I.click(I.sampleData.initialQuestion.title);
        });
        I.seeInCurrentUrl(questionDetailsPage.url);
        await within(questionDetailsPage.components.answersList, async () => {
            answerUUID = await I.grabAttributeFrom(questionDetailsPage.components.answer, 'messageuuid');
        });
        var voteCount = await I.grabTextFrom(questionDetailsPage.components.voteForm.voteCount + answerUUID);
        voteCount = parseInt(voteCount);
        voteCount--;

        questionDetailsPage.voteDown(answerUUID);
        I.see(voteCount.toString(), questionDetailsPage.components.voteForm.voteCount + answerUUID);

        // Remove the downvote
        voteCount++;
        questionDetailsPage.voteDown(answerUUID);

        I.see(voteCount.toString(), questionDetailsPage.components.voteForm.voteCount + answerUUID);
    });

Feature('Comments');

    Scenario('Adding a comment to a question', async( I ) => {
        const commentContent = randomstring.generate(20);

        I.loginTestUser();
        I.amOnPage(homePage.url);
        within(homePage.components.questionList.root, () => {
            I.click(I.sampleData.initialQuestion.title);
        });
        I.seeInCurrentUrl(questionDetailsPage.url);
        const questionUUID = await I.grabAttributeFrom(questionDetailsPage.components.question, 'questionuuid');
        questionDetailsPage.addComment(questionUUID, commentContent);
        I.see(commentContent, questionDetailsPage.components.commentForm.elements.comment);
    });

    Scenario('Adding a comment to an answer', async( I ) => {
        const commentContent = randomstring.generate(20);
        var answerUUID = '';

        I.loginTestUser();
        I.amOnPage(homePage.url);
        within(homePage.components.questionList.root, () => {
            I.click(I.sampleData.initialQuestion.title);
        });
        I.seeInCurrentUrl(questionDetailsPage.url);
        await within(questionDetailsPage.components.answersList, async () => {
            answerUUID = await I.grabAttributeFrom(questionDetailsPage.components.answer, 'messageuuid');
        });
        questionDetailsPage.addComment(answerUUID, commentContent);
        I.see(commentContent, questionDetailsPage.components.commentForm.elements.comment);
    });
