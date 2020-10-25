const { profilePage, homePage } = inject();
const randomstring= require('randomstring');

Feature('Handling profile informations');

    Scenario('Seeing correct informations', (I) => {

        I.loginTestUser();
        homePage.components.header.goToProfilePage();
        I.seeInTitle(profilePage.pageTitle);
        within(profilePage.components.profileForm.root, () => {
            I.seeInField(profilePage.fields.username, I.credentials.username);
            I.seeInField(profilePage.fields.firstName, I.credentials.firstName);
            I.seeInField(profilePage.fields.lastName, I.credentials.lastName);
            I.seeInField(profilePage.fields.email, I.credentials.email);
        })
    });

    let wrongPasswords = new DataTable(['password']);
    wrongPasswords.add(['Abcde6!']);  // Only 7 chars
    wrongPasswords.add(['abcdef7!']); // No uppercase
    wrongPasswords.add(['ABCDEF7!']); // No lowercase
    wrongPasswords.add(['Abcdefg!']); // No number
    wrongPasswords.add(['Abcdef78']); // No special char

    Data(wrongPasswords).Scenario('Failed at modifying password', (I, current) => {
        I.loginTestUser();
        homePage.components.header.goToProfilePage();
        I.seeInTitle(profilePage.pageTitle);
        profilePage.updateProfile(I.credentials.username, I.credentials.email, I.credentials.firstName, I.credentials.lastName, current.password);
        I.seeInTitle(profilePage.pageTitle);
        within(profilePage.components.profileForm.root, () => {
            I.see(profilePage.errorMessages.invalidPassword);
        })
    });

Scenario('Modifying every informations except password', (I) => {
    const newUsername   = 'new' + I.credentials.username;
    const newEmail      = 'new' + I.credentials.email;
    const newFirstName  = 'new' + I.credentials.firstName;
    const newLastName   = 'new' + I.credentials.lastName;

    I.loginTestUser();
    homePage.components.header.goToProfilePage();
    I.seeInTitle(profilePage.pageTitle);
    profilePage.updateProfile(newUsername, newEmail, newFirstName, newLastName, "");
    I.seeInTitle(profilePage.pageTitle);
    within(profilePage.components.profileForm.root, () => {
        I.seeInField(profilePage.fields.username, newUsername);
        I.seeInField(profilePage.fields.email, newEmail);
        I.seeInField(profilePage.fields.firstName, newFirstName);
        I.seeInField(profilePage.fields.lastName, newLastName);
    })

    // Set initial values back for all following tests to works
    profilePage.updateProfile(I.credentials.username, I.credentials.email, I.credentials.firstName, I.credentials.lastName, "");
});

