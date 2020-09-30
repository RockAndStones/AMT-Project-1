const { setHeadlessWhen } = require('@codeceptjs/configure');

// turn on headless mode when running with HEADLESS=true environment variable
// export HEADLESS=true && npx codeceptjs run
setHeadlessWhen(process.env.HEADLESS);

exports.config = {
  tests: './*_test.js',
  output: './output',
  helpers: {
    Puppeteer: {
      url: 'http://localhost:8080/StoneOverflow/',
      show: false,
      windowSize: '1200x900'
    }
  },
  include: {
    I: './steps_file.js',
    __comment1__:     '---------- PAGES ----------',
    homePage:         './pages/homePage.js',
    loginPage:        './pages/loginPage.js',
    newQuestionPage:  './pages/newQuestionsPage.js',
    __comment2__:     '---------- FRAGMENTS ----------',
    loginFragment:    './fragments/loginFragment.js',
    registerFragment: './fragments/registerFragment.js',
    headerFragment:   './fragments/headerFragment.js',
    sidebarFragment:  './fragments/sidebarFragment.js'
  },
  name: 'e2e',
  plugins: {
    retryFailedStep: {
      enabled: true
    },
    screenshotOnFail: {
      enabled: true
    }
  }
}