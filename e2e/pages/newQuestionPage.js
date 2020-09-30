const { I } = inject();

module.exports = {
    url: "addQuestion",

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
        I.fillField(this.fields.title, title);
        I.fillField(this.fields.description, description);
        I.click(this.buttons.submit);
    }
}
