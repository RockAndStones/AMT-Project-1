const { headerFragment, sidebarFragment } = inject();

module.exports = {

  goToHomePageUsingHeader() {
      headerFragment.goToHomePage();
  },

  goToHomePageUsingSidebar() {
      sidebarFragment.goToHomePage();
  },

  goToLoginPage() {
      headerFragment.goToLoginPage();
  },

  goToNewQuestionPage() {
      sidebarFragment.goToNewQuestionPage();
  }
}
