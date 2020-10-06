const { I } = inject();

module.exports = {

    root: {id: 'sidebar'},

    links: {
        home: {css: 'a[href=home]'},
        newQuestions: {css: 'a[href=addQuestion]'}
    },

    goToHomePage() {
        within(this.root, () => {
            I.clickLink(this.links.home);
        });
    },

    goToNewQuestionPage() {
        within(this.root, () => {
            I.clickLink(this.links.newQuestions);
        });
    }
}
