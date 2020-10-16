package stoneoverflow.application.identitymgmt;

import ch.heigvd.amt.stoneoverflow.application.ServiceRegistry;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.IdentityManagementFacade;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginCommand;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginFailedException;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.register.RegisterCommand;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.register.RegistrationFailedException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class IdentityManagementFacadeIT {

    private final static String WARNAME = "arquillian-managed.war";

    @Inject
    private ServiceRegistry serviceRegistry;

    private IdentityManagementFacade identityManagementFacade;

    private static RegisterCommand registerCommandForLogin;
    private static final String plaintextPasswordLogin = "P@ssW0rd";

    @Deployment(testable = true)
    public static WebArchive createDeployment() {
        WebArchive archive = ShrinkWrap.create(WebArchive.class, WARNAME)
                .addPackages(true, "ch.heigvd.amt")
                .addPackages(true, "org.springframework.security.crypto.bcrypt");
        return archive;
    }

    @Before
    public void initializeIdentityManagementFacade() throws RegistrationFailedException {
        this.identityManagementFacade = serviceRegistry.getIdentityManagementFacade();

        registerCommandForLogin = RegisterCommand.builder()
                .username("MyTestUsername")
                .email("mail@mail.com")
                .firstName("First name")
                .lastName("Last name")
                .plaintextPassword(plaintextPasswordLogin)
                .plaintextPasswordConfirmation(plaintextPasswordLogin)
                .build();
        try {
            this.identityManagementFacade.login(LoginCommand.builder().username("MyTestUsername").plaintextPassword(plaintextPasswordLogin).build());
        } catch (LoginFailedException exception) {
            this.identityManagementFacade.register(registerCommandForLogin);
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
                .plaintextPassword(plaintextPasswordLogin)
                .build();

        assertNotNull(identityManagementFacade.login(loginCommand));
    }

    @Test
    public void shouldReturnValidUserDTOOnLogin() throws LoginFailedException {
        LoginCommand loginCommand = LoginCommand.builder()
                .username(registerCommandForLogin.getUsername())
                .plaintextPassword(plaintextPasswordLogin)
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
}
