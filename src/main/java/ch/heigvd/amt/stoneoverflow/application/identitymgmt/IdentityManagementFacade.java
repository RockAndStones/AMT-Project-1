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
        Optional<User> existingUsername = userRepository.findByUsername(registerCommand.getUsername());
        if (existingUsername.isPresent())
            throw new RegistrationFailedException("Username is already taken");

        try {
            User newUser = User.builder()
                    .username(registerCommand.getUsername())
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
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
