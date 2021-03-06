const { I } = inject();

module.exports = {

    root: {xpath: '//nav[@id="sidebar"]'},

    links: {
        home: {css: 'a[href=home]'},
        newQuestions: {css: 'a[href=addQuestion]'},
        statistics: {css: 'a[href=statistics]'},
        history: {css: 'a[href=history]'},
    },

    elements: {
        logoutButton: 'Logout'
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
    },

    goToStatisticsPage(){
        within(this.root, () => {
            I.clickLink(this.links.statistics);
        });
    },

    goToHistoryPage(){
        within(this.root, () => {
            I.clickLink(this.links.history);
        });
    },

    logout(){
        within(this.root, () => {
            I.click(this.elements.logoutButton);
        });
    }
}
