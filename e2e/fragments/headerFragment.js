const { I } = inject();

module.exports = {

  root: {id: 'header'},

  links: {
      home: {css: 'a[href=home]'},
      login: {css: 'a[href=login]'},
      profile: {css: 'a[href=profile]'}
  },

  goToHomePage() {
      within(this.root, () => {
          I.clickLink(this.links.home);
      });
  },

  goToLoginPage() {
      within(this.root, () => {
          I.clickLink(this.links.login);
      });
  },

  goToProfilePage(){
      within(this.root, () => {
          I.clickLink(this.links.profile);
      });
  }
}
