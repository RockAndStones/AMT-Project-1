package StoneOverflow.application.identitymgmt;

import ch.heigvd.amt.StoneOverflow.application.identitymgmt.IdentityManagementFacade;
import ch.heigvd.amt.StoneOverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import ch.heigvd.amt.StoneOverflow.application.identitymgmt.login.LoginCommand;
import ch.heigvd.amt.StoneOverflow.application.identitymgmt.login.LoginFailedException;
import ch.heigvd.amt.StoneOverflow.application.identitymgmt.register.RegisterCommand;
import ch.heigvd.amt.StoneOverflow.application.identitymgmt.register.RegistrationFailedException;
import ch.heigvd.amt.StoneOverflow.domain.user.IUserRepository;
import ch.heigvd.amt.StoneOverflow.infrastructure.persistance.memory.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IdentityManagementFacadeTest {
    private IUserRepository userRepository;
    private IdentityManagementFacade identityManagementFacade;

    @BeforeEach
    public void initializeIdentityManagementFacade() {
        this.userRepository = new InMemoryUserRepository();
        this.identityManagementFacade = new IdentityManagementFacade(userRepository);
    }

    @Nested
    public class Register {
        @Test
        public void shouldRegisterValidUser() {
            String username = "shouldRegisterValidUser";
            RegisterCommand registerCommand = RegisterCommand.builder()
                    .username(username)
                    .email("shouldRegisterValidUser@mail.com")
                    .firstName("First name")
                    .lastName("Last name")
                    .plaintextPassword("P@ssW0rd")
                    .plaintextPasswordConfirmation("P@ssW0rd")
                    .build();

            assertDoesNotThrow(() -> identityManagementFacade.register(registerCommand));
            assertTrue(userRepository.findByUsername(username).isPresent());
        }

        @Test
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
            assertThrows(RegistrationFailedException.class, () ->
                    identityManagementFacade.register(registerCommand));
        }

        @Test
        public void shouldNotRegisterUserWithMissingInfo() {
            String username = "shouldNotRegisterUserWithMissingInfo";
            RegisterCommand registerCommand = RegisterCommand.builder()
                    .username(username)
                    .email("shouldNotRegisterUserWithMissingInfo@mail.com")
                    .plaintextPassword("P@ssW0rd")
                    .plaintextPasswordConfirmation("P@ssW0rd")
                    .build();

            assertThrows(RegistrationFailedException.class, () ->
                    identityManagementFacade.register(registerCommand));
        }

        @Test
        public void shouldNotRegisterUserWithDifferentPasswords() {
            RegisterCommand registerCommand = RegisterCommand.builder()
                    .username("shouldNotRegisterUserWithDifferentPasswords")
                    .email("shouldNotRegisterUserWithDifferentPasswords@mail.com")
                    .plaintextPassword("P@ssW0rd")
                    .plaintextPasswordConfirmation("P4ssW0rd")
                    .build();

            assertThrows(RegistrationFailedException.class, () ->
                    identityManagementFacade.register(registerCommand));
        }

        @Test
        public void shouldNotRegisterUserWithWeakPassword() {
            RegisterCommand registerCommand = RegisterCommand.builder()
                    .username("shouldNotRegisterUserWithWeakPassword")
                    .email("shouldNotRegisterUserWithWeakPassword@mail.com")
                    .plaintextPassword("Weak1994")
                    .plaintextPasswordConfirmation("Weak1994")
                    .build();

            assertThrows(RegistrationFailedException.class, () ->
                    identityManagementFacade.register(registerCommand));
        }
    }

    @Nested
    public class Login {
        private RegisterCommand registerCommand;
        private final String plaintextPassword = "P@ssW0rd";

        @BeforeEach
        public void registerUser() throws RegistrationFailedException {
            registerCommand = RegisterCommand.builder()
                    .username("MyTestUsername")
                    .email("mail@mail.com")
                    .firstName("First name")
                    .lastName("Last name")
                    .plaintextPassword(plaintextPassword)
                    .plaintextPasswordConfirmation(plaintextPassword)
                    .build();
            identityManagementFacade.register(registerCommand);
        }

        @Test
        public void shouldLoginWithRegisteredUser() {
            LoginCommand loginCommand = LoginCommand.builder()
                    .username(registerCommand.getUsername())
                    .plaintextPassword(plaintextPassword)
                    .build();

            assertDoesNotThrow(() -> identityManagementFacade.login(loginCommand));
        }

        @Test
        public void shouldReturnValidUserDTOOnLogin() throws LoginFailedException {
            LoginCommand loginCommand = LoginCommand.builder()
                    .username(registerCommand.getUsername())
                    .plaintextPassword(plaintextPassword)
                    .build();

            AuthenticatedUserDTO userDTO = identityManagementFacade.login(loginCommand);
            assertEquals(userDTO.getUsername(), registerCommand.getUsername());
            assertEquals(userDTO.getEmail(), registerCommand.getEmail());
            assertEquals(userDTO.getFirstName(), registerCommand.getFirstName());
            assertEquals(userDTO.getLastName(), registerCommand.getLastName());
        }

        @Test
        public void shouldNotLoginNonExistingUser() {
            LoginCommand loginCommand = LoginCommand.builder()
                    .username("IDoNotExist")
                    .plaintextPassword("Wha7@ver")
                    .build();

            assertThrows(LoginFailedException.class, () -> identityManagementFacade.login(loginCommand));
        }

        @Test
        public void shouldNotLoginInvalidPassword() {
            LoginCommand loginCommand = LoginCommand.builder()
                    .username(registerCommand.getUsername())
                    .plaintextPassword("Inval1dP@ssword")
                    .build();

            assertThrows(LoginFailedException.class, () -> identityManagementFacade.login(loginCommand));
        }
    }
}
