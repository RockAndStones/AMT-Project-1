const { headerFragment, sidebarFragment, I } = inject();

module.exports = {
    url: 'profile',
    pageTitle: 'User Profile',

    components: {
        header: headerFragment,
        sidebar: sidebarFragment,
        profileForm:{
            root: '#updateSection'
        }
    },

    fields: {
        username: '#updateUsername',
        email: '#updateEmail',
        firstName: '#updateFirstName',
        lastName: '#updateLastName',
        password: '#updatePassword',
        confirmPassword: '#confirmPassword'
    },

    buttons: {
        update: {css: 'input[name=update]'},
        cancel: {css: 'input[name=cancel]'}
    },

    elements: {
        profileLabel: '#updateLabel'
    },

    errorMessages: {
      invalidPassword: 'Password does not meet the minimum requirements (8 characters, 1 lower case, 1 upper case, 1 number, 1 special character)'
    },

    updateProfile(username, email, firstName, lastName, password){
        within(this.components.profileForm.root, () => {
            I.fillField(this.fields.username, username);
            I.fillField(this.fields.email, email);
            I.fillField(this.fields.firstName, firstName);
            I.fillField(this.fields.lastName, lastName);
            I.fillField(this.fields.password, password);
            I.fillField(this.fields.confirmPassword, password);
            I.click(this.buttons.update);
        })
    }
}
