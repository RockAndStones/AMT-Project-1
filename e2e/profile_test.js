const { profilePage, homePage } = inject();

Feature('Handling profile informations');

    Scenario('Seeing correct informations', (I) => {

        I.loginTestUser();
        homePage.components.header.goToProfilePage();
        I.seeInTitle(profilePage.pageTitle);
        within(profilePage.components.profileForm.root, () => {
            I.seeInField(profilePage.fields.username, I.sampleData.userInfo.username);
            I.seeInField(profilePage.fields.firstName, I.sampleData.userInfo.firstName);
            I.seeInField(profilePage.fields.lastName, I.sampleData.userInfo.lastName);
            I.seeInField(profilePage.fields.email, I.sampleData.userInfo.email);
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
        profilePage.updateProfile(I.sampleData.userInfo.username, I.sampleData.userInfo.email, I.sampleData.userInfo.firstName, I.sampleData.userInfo.lastName, current.password);
        I.seeInTitle(profilePage.pageTitle);
        within(profilePage.components.profileForm.root, () => {
            I.see(profilePage.errorMessages.invalidPassword);
        })
    });

    Scenario('Succeed at modifying password', (I) => {
        const newPassword = 'new' + I.sampleData.userInfo.password;

        I.loginTestUser();
        homePage.components.header.goToProfilePage();
        I.seeInTitle(profilePage.pageTitle);
        profilePage.updateProfile(I.sampleData.userInfo.username, I.sampleData.userInfo.email, I.sampleData.userInfo.firstName, I.sampleData.userInfo.lastName, newPassword);
        I.seeInTitle(profilePage.pageTitle);
        within(profilePage.components.profileForm.root, () => {
            I.see(profilePage.successMessages.infoUpdated);
        })
        // Set initial password back
        profilePage.updateProfile(I.sampleData.userInfo.username, I.sampleData.userInfo.email, I.sampleData.userInfo.firstName, I.sampleData.userInfo.lastName, I.sampleData.userInfo.password);
    });

    Scenario('Modifying every informations except password', (I) => {
        const newUsername   = 'new' + I.sampleData.userInfo.username;
        const newEmail      = 'new' + I.sampleData.userInfo.email;
        const newFirstName  = 'new' + I.sampleData.userInfo.firstName;
        const newLastName   = 'new' + I.sampleData.userInfo.lastName;

        I.loginTestUser();
        homePage.components.header.goToProfilePage();
        I.seeInTitle(profilePage.pageTitle);
        profilePage.updateProfile(newUsername, newEmail, newFirstName, newLastName, "");
        I.seeInTitle(profilePage.pageTitle);
        within(profilePage.components.profileForm.root, () => {
            I.see(profilePage.successMessages.infoUpdated);
            I.seeInField(profilePage.fields.username, newUsername);
            I.seeInField(profilePage.fields.email, newEmail);
            I.seeInField(profilePage.fields.firstName, newFirstName);
            I.seeInField(profilePage.fields.lastName, newLastName);
        })

        // Set initial values back for all following tests to works
        profilePage.updateProfile(I.sampleData.userInfo.username, I.sampleData.userInfo.email, I.sampleData.userInfo.firstName, I.sampleData.userInfo.lastName, "");
    });

