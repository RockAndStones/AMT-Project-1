package ch.heigvd.amt.StoneOverflow.application.identitymgmt;

import ch.heigvd.amt.StoneOverflow.application.identitymgmt.register.RegisterCommand;
import ch.heigvd.amt.StoneOverflow.application.identitymgmt.register.RegistrationFailedException;
import ch.heigvd.amt.StoneOverflow.domain.user.IUserRepository;
import ch.heigvd.amt.StoneOverflow.domain.user.User;
import lombok.EqualsAndHashCode;

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
}
