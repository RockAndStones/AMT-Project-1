const { headerFragment, sidebarFragment } = inject();

module.exports = {
    url: "home",

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
