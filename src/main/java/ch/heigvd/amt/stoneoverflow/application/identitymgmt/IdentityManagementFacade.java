package ch.heigvd.amt.stoneoverflow.application.identitymgmt;

import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginCommand;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginFailedException;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.register.RegisterCommand;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.register.RegistrationFailedException;
import ch.heigvd.amt.stoneoverflow.domain.user.IUserRepository;
import ch.heigvd.amt.stoneoverflow.domain.user.User;

import java.util.Optional;

public class IdentityManagementFacade {
    private IUserRepository userRepository;

    public IdentityManagementFacade(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(RegisterCommand registerCommand) throws RegistrationFailedException {
        // If passwords are not equals
        if (!registerCommand.getPlaintextPassword().equals(registerCommand.getPlaintextPasswordConfirmation()))
            throw new RegistrationFailedException("Passwords are not equal");

        if (!isPasswordStrong(registerCommand.getPlaintextPassword()))
            throw new RegistrationFailedException("Password does not meet the minimum requirements " +
                    "(8 characters, 1 lower case, 1 upper case, 1 number, 1 special character)");

        Optional<User> existingUsername = userRepository.findByUsername(registerCommand.getUsername().toLowerCase());
        if (existingUsername.isPresent())
            throw new RegistrationFailedException("Username is already taken");

        try {
            User newUser = User.builder()
                    .username(registerCommand.getUsername().toLowerCase())
                    .email(registerCommand.getEmail())
                    .firstName(registerCommand.getFirstName())
                    .lastName(registerCommand.getLastName())
                    .plaintextPassword(registerCommand.getPlaintextPassword())
                    .build();

            userRepository.save(newUser);
        } catch (Exception e) {
            throw new RegistrationFailedException(e.getMessage());
        }
    }

    public AuthenticatedUserDTO login(LoginCommand loginCommand) throws LoginFailedException {
        User user = userRepository.findByUsername(loginCommand.getUsername())
                .orElseThrow(() -> new LoginFailedException("User not found"));

        if (!user.authenticate(loginCommand.getPlaintextPassword()))
            throw new LoginFailedException("Invalid password");

        return AuthenticatedUserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    private boolean isPasswordStrong(String plaintextPassword) {
        boolean hasUpperCase = plaintextPassword.matches("(?=.*[A-Z]).*");
        boolean hasLowerCase = plaintextPassword.matches("(?=.*[a-z]).*");
        boolean hasNumber = plaintextPassword.matches("(?=.*[0-9]).*");
        boolean hasSpecialChar = plaintextPassword.matches("(?=.*[\\W]).*");

        return hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar && plaintextPassword.length() >= 8;
    }
}
