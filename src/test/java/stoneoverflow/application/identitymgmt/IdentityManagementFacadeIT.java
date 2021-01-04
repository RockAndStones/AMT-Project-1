package stoneoverflow.application.identitymgmt;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.IdentityManagementFacade;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginCommand;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginFailedException;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.register.RegisterCommand;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.register.RegistrationFailedException;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.updateprofile.UpdateProfileCommand;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.updateprofile.UpdateProfileFailedException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.io.File;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class IdentityManagementFacadeIT {

    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    private ServiceRegistry serviceRegistry;

    private IdentityManagementFacade identityManagementFacade;

    private static RegisterCommand registerCommandForLogin;
    private static UpdateProfileCommand lastUpdate;
    private static final String plaintextPassword = "P@ssW0rd";

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt")
                .addPackages(true, "com.squareup.okhttp3")
                .addPackages(true, "org.springframework.security.crypto.bcrypt")
                .addAsResource(new File("src/main/resources/environment.properties"), "environment.properties");
        return archive;
    }

    @Before
    public void initializeIdentityManagementFacade() throws RegistrationFailedException, LoginFailedException {
        this.identityManagementFacade = serviceRegistry.getIdentityManagementFacade();

        registerCommandForLogin = RegisterCommand.builder()
                .username("MyTestUsername")
                .email("mail@mail.com")
                .firstName("First name")
                .lastName("Last name")
                .plaintextPassword(plaintextPassword)
                .plaintextPasswordConfirmation(plaintextPassword)
                .build();

        RegisterCommand registerCommandForUpdate = RegisterCommand.builder()
                .username("MyUpdatedTestUsername")
                .email("updatemail@mail.com")
                .firstName("Update First name")
                .lastName("Update Last name")
                .plaintextPassword(plaintextPassword)
                .plaintextPasswordConfirmation(plaintextPassword)
                .build();

        try {
            this.identityManagementFacade.login(LoginCommand.builder().username("MyTestUsername").plaintextPassword(plaintextPassword).build());
        } catch (LoginFailedException exception) {
            this.identityManagementFacade.register(registerCommandForLogin);
        }

        try{
            // start of the test lastUpdate is null
            if(lastUpdate == null){
                // Will throw an exception if oldUser cannot login or will set the lastUpdate in case we work with jdbc user can already be set
                lastUpdate = UpdateProfileCommand.builder()
                        .oldUser(this.identityManagementFacade.login(LoginCommand.builder()
                                .username(registerCommandForUpdate.getUsername())
                                .plaintextPassword(plaintextPassword)
                                .build()))
                        .username(registerCommandForUpdate.getUsername())
                        .email(registerCommandForUpdate.getEmail())
                        .firstName(registerCommandForUpdate.getFirstName())
                        .lastName(registerCommandForUpdate.getLastName())
                        .plaintextPassword(registerCommandForUpdate.getPlaintextPassword())
                        .plaintextPasswordConfirmation(registerCommandForUpdate.getPlaintextPasswordConfirmation())
                        .build();
            }
            this.identityManagementFacade.login(LoginCommand.builder().username(lastUpdate.getUsername()).plaintextPassword(lastUpdate.getPlaintextPassword()).build());
        } catch (LoginFailedException exception) {
            this.identityManagementFacade.register(registerCommandForUpdate);

            lastUpdate = UpdateProfileCommand.builder()
                    .oldUser(this.identityManagementFacade.login(LoginCommand.builder()
                            .username(registerCommandForUpdate.getUsername())
                            .plaintextPassword(plaintextPassword)
                            .build()))
                    .username(registerCommandForUpdate.getUsername())
                    .email(registerCommandForUpdate.getEmail())
                    .firstName(registerCommandForUpdate.getFirstName())
                    .lastName(registerCommandForUpdate.getLastName())
                    .plaintextPassword(registerCommandForUpdate.getPlaintextPassword())
                    .plaintextPasswordConfirmation(registerCommandForUpdate.getPlaintextPasswordConfirmation())
                    .build();
        }
    }

    @Test
    public void shouldRegisterValidUser() throws LoginFailedException, RegistrationFailedException {
        String username = "shouldRegisterValidUser";
        RegisterCommand registerCommand = RegisterCommand.builder()
                .username(username)
                .email("shouldRegisterValidUser@mail.com")
                .firstName("First name")
                .lastName("Last name")
                .plaintextPassword("P@ssW0rd")
                .plaintextPasswordConfirmation("P@ssW0rd")
                .build();

        identityManagementFacade.register(registerCommand);
        assertNotNull(identityManagementFacade.login(LoginCommand.builder().username("shouldRegisterValidUser").plaintextPassword("P@ssW0rd").build()));
    }

    @Test(expected = RegistrationFailedException.class)
    public void shouldNotRegisterDuplicatedUser() throws RegistrationFailedException {
        String username = "shouldNotRegisterDuplicatedUser";
        RegisterCommand registerCommand = RegisterCommand.builder()
                .username(username)
                .email("shouldNotRegisterDuplicatedUser@mail.com")
                .firstName("First name")
                .lastName("Last name")
                .plaintextPassword("P@ssW0rd")
                .plaintextPasswordConfirmation("P@ssW0rd")
                .build();

        identityManagementFacade.register(registerCommand);
        identityManagementFacade.register(registerCommand);
    }

    @Test(expected = RegistrationFailedException.class)
    public void shouldNotRegisterUserWithMissingInfo() throws RegistrationFailedException {
        String username = "shouldNotRegisterUserWithMissingInfo";
        RegisterCommand registerCommand = RegisterCommand.builder()
                .username(username)
                .email("shouldNotRegisterUserWithMissingInfo@mail.com")
                .plaintextPassword("P@ssW0rd")
                .plaintextPasswordConfirmation("P@ssW0rd")
                .build();
        identityManagementFacade.register(registerCommand);
    }

    @Test(expected = RegistrationFailedException.class)
    public void shouldNotRegisterUserWithDifferentPasswords() throws RegistrationFailedException {
        RegisterCommand registerCommand = RegisterCommand.builder()
                .username("shouldNotRegisterUserWithDifferentPasswords")
                .email("shouldNotRegisterUserWithDifferentPasswords@mail.com")
                .plaintextPassword("P@ssW0rd")
                .plaintextPasswordConfirmation("P4ssW0rd")
                .build();

        identityManagementFacade.register(registerCommand);
    }

    @Test(expected = RegistrationFailedException.class)
    public void shouldNotRegisterUserWithWeakPassword() throws RegistrationFailedException {
        RegisterCommand registerCommand = RegisterCommand.builder()
                .username("shouldNotRegisterUserWithWeakPassword")
                .email("shouldNotRegisterUserWithWeakPassword@mail.com")
                .plaintextPassword("Weak1994")
                .plaintextPasswordConfirmation("Weak1994")
                .build();

       identityManagementFacade.register(registerCommand);
    }

    @Test
    public void shouldLoginWithRegisteredUser() throws LoginFailedException {
        LoginCommand loginCommand = LoginCommand.builder()
                .username(registerCommandForLogin.getUsername())
                .plaintextPassword(plaintextPassword)
                .build();

        assertNotNull(identityManagementFacade.login(loginCommand));
    }

    @Test
    public void shouldReturnValidUserDTOOnLogin() throws LoginFailedException {
        LoginCommand loginCommand = LoginCommand.builder()
                .username(registerCommandForLogin.getUsername())
                .plaintextPassword(plaintextPassword)
                .build();

        AuthenticatedUserDTO userDTO = identityManagementFacade.login(loginCommand);
        assertEquals(userDTO.getUsername(), registerCommandForLogin.getUsername());
        assertEquals(userDTO.getEmail(), registerCommandForLogin.getEmail());
        assertEquals(userDTO.getFirstName(), registerCommandForLogin.getFirstName());
        assertEquals(userDTO.getLastName(), registerCommandForLogin.getLastName());
    }

    @Test(expected = LoginFailedException.class)
    public void shouldNotLoginNonExistingUser() throws LoginFailedException {
        LoginCommand loginCommand = LoginCommand.builder()
                .username("IDoNotExist")
                .plaintextPassword("Wha7@ver")
                .build();

        identityManagementFacade.login(loginCommand);
    }

    @Test(expected = LoginFailedException.class)
    public void shouldNotLoginInvalidPassword() throws LoginFailedException {
        LoginCommand loginCommand = LoginCommand.builder()
                .username(registerCommandForLogin.getUsername())
                .plaintextPassword("Inval1dP@ssword")
                .build();

        identityManagementFacade.login(loginCommand);
    }

    @Test
    public void shouldUpdateValidUser() throws LoginFailedException, UpdateProfileFailedException {
        String username = "shouldUpdateUser";

        lastUpdate = UpdateProfileCommand.builder()
                .oldUser(lastUpdate.getOldUser())
                .username(username)
                .email("shouldUpdateValidUser@mail.com")
                .firstName("First name")
                .lastName("Last name")
                .plaintextPassword("P@ssW0rd2")
                .plaintextPasswordConfirmation("P@ssW0rd2")
                .build();

        AuthenticatedUserDTO userDTO = identityManagementFacade.update(lastUpdate);

        assertEquals(userDTO.getUsername(), username);
        assertEquals(userDTO.getEmail(), lastUpdate.getEmail());
        assertEquals(userDTO.getFirstName(), lastUpdate.getFirstName());
        assertEquals(userDTO.getLastName(), lastUpdate.getLastName());

        assertNotNull(identityManagementFacade.login(LoginCommand.builder().username("shouldUpdateUser").plaintextPassword("P@ssW0rd2").build()));
    }

    @Test(expected = UpdateProfileFailedException.class)
    public void shouldNotUpdateUserWithUsedUsername() throws UpdateProfileFailedException {
        String username = "MyTestUsername";
        UpdateProfileCommand updateProfileCommand = UpdateProfileCommand.builder()
                .oldUser(lastUpdate.getOldUser())
                .username(username)
                .email("shouldNotRegisterDuplicatedUser@mail.com")
                .firstName("First name")
                .lastName("Last name")
                .plaintextPassword("P@ssW0rd")
                .plaintextPasswordConfirmation("P@ssW0rd")
                .build();

        identityManagementFacade.update(updateProfileCommand);
    }

    @Test(expected = UpdateProfileFailedException.class)
    public void shouldNotUpdateUserWithMissingInfo() throws UpdateProfileFailedException {
        String username = "shouldNotUpdateUserWithMissingInfo";
        UpdateProfileCommand updateProfileCommand = UpdateProfileCommand.builder()
                .username(username)
                .email("shouldNotUpdateUserWithMissingInfo@mail.com")
                .plaintextPassword("P@ssW0rd")
                .plaintextPasswordConfirmation("P@ssW0rd")
                .build();
        identityManagementFacade.update(updateProfileCommand);
    }

    @Test(expected = UpdateProfileFailedException.class)
    public void shouldNotUpdateUserWithDifferentPasswords() throws UpdateProfileFailedException {
        UpdateProfileCommand updateProfileCommand = UpdateProfileCommand.builder()
                .oldUser(lastUpdate.getOldUser())
                .username("shouldNotUpdateUserWithDifferentPasswords")
                .email("shouldNotUpdateUserWithDifferentPasswords@mail.com")
                .plaintextPassword("P@ssW0rd")
                .plaintextPasswordConfirmation("P4ssW0rd")
                .build();

        identityManagementFacade.update(updateProfileCommand);
    }

    @Test(expected = UpdateProfileFailedException.class)
    public void shouldNotUpdateUserWithWeakPassword() throws UpdateProfileFailedException {
        UpdateProfileCommand updateProfileCommand = UpdateProfileCommand.builder()
                .oldUser(lastUpdate.getOldUser())
                .username("shouldNotUpdateUserWithWeakPassword")
                .email("shouldNotUpdateUserWithWeakPassword@mail.com")
                .plaintextPassword("Weak1994")
                .plaintextPasswordConfirmation("Weak1994")
                .build();

        identityManagementFacade.update(updateProfileCommand);
    }
}
