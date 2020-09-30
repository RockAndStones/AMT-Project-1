const { I } = inject();

module.exports = {

  root: '#header',

  links: {
      home: {css: 'a[href=home]'},
      login: {css: 'a[href=login]'}
  },

  goToHomePage() {
      within(this.root, function() {
          I.clickLink(this.links.home);
      });
  },

  goToLoginPage() {
      within(this.root, function() {
          I.clickLink(this.links.login);
      });
  }
}
