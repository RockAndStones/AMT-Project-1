const { I } = inject();

module.exports = {

    root: '#sidebar',

    links: {
        home: {css: 'a[href=home]'},
        newQuestions: {css: 'a[href=addQuestion]'}
    },

    goToHomePage() {
        within(this.root, function() {
            I.clickLink(this.links.home);
        });
    },

    goToNewQuestionPage() {
        within(this.root, function() {
            I.clickLink(this.links.newQuestions);
        });
    }
}
