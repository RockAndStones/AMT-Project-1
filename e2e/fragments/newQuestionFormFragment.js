const { I } = inject();

module.exports = {
    root: '#newQuestionForm',

    labels: {
        newQuestion: {css: '#newQuestionLabel'}
    },

    fields: {
        title: {css: '#questionTitle'},
        description: {css: '#questionDescription'}
    },

    buttons: {
        submit: {css: '#newQuestionSubmitButton'}
    },

    addQuestion(title, description){
        within(this.root, () => {
            I.fillField(this.fields.title, title);
            I.fillField(this.fields.description, description);
            I.click(this.buttons.submit);
        });
    }
}