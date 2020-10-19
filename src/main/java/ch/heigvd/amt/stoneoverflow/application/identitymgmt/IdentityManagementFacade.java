package ch.heigvd.amt.stoneoverflow.application.identitymgmt;

import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginCommand;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.LoginFailedException;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.register.RegisterCommand;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.register.RegistrationFailedException;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.updateprofile.UpdateProfileCommand;
import ch.heigvd.amt.stoneoverflow.application.identitymgmt.updateprofile.UpdateProfileFailedException;
import ch.heigvd.amt.stoneoverflow.domain.user.IUserRepository;
import ch.heigvd.amt.stoneoverflow.domain.user.User;

import java.util.Optional;

public class IdentityManagementFacade {
    private IUserRepository userRepository;

    public IdentityManagementFacade(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(RegisterCommand registerCommand) throws RegistrationFailedException {
        // TODO refactor by using same class for exceptions with update
        // If passwords are not equals
        if (!registerCommand.getPlaintextPassword().equals(registerCommand.getPlaintextPasswordConfirmation()))
            throw new RegistrationFailedException("Passwords are not equal");

        if (!isPasswordStrong(registerCommand.getPlaintextPassword()))
            throw new RegistrationFailedException("Password does not meet the minimum requirements " +
                    "(8 characters, 1 lower case, 1 upper case, 1 number, 1 special character)");

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
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public AuthenticatedUserDTO update(UpdateProfileCommand updateProfileCommand) throws UpdateProfileFailedException {
        if(updateProfileCommand.getOldUser() == null){
            throw new UpdateProfileFailedException("User is not authenticated");
        }
        // Boolean that will indicated if a new password has been entered
        boolean newPassword = false;
        // TODO refactor
        if(!updateProfileCommand.getPlaintextPassword().isEmpty()) {
            // If passwords are not equals
            if (!updateProfileCommand.getPlaintextPassword().equals(updateProfileCommand.getPlaintextPasswordConfirmation()))
                throw new UpdateProfileFailedException("Passwords are not equal");

            if (!isPasswordStrong(updateProfileCommand.getPlaintextPassword()))
                throw new UpdateProfileFailedException("Password does not meet the minimum requirements " +
                        "(8 characters, 1 lower case, 1 upper case, 1 number, 1 special character)");

            newPassword = true;
        }

        // CHeck if the username is different and check in the repository if he exists
        if(!updateProfileCommand.getOldUser().getUsername().equals(updateProfileCommand.getUsername())) {
            Optional<User> existingUsername = userRepository.findByUsername(updateProfileCommand.getUsername());
            if (existingUsername.isPresent())
                throw new UpdateProfileFailedException("Username is already taken");
        }

        try {
            User updatedUser;
            // If no new password is entered we use the hashed password value in the builder
            if(!newPassword) {
                updatedUser = User.builder()
                        .id(updateProfileCommand.getOldUser().getId())
                        .username(updateProfileCommand.getUsername())
                        .email(updateProfileCommand.getEmail())
                        .firstName(updateProfileCommand.getFirstName())
                        .lastName(updateProfileCommand.getLastName())
                        .hashedPassword(userRepository.findByUsername(updateProfileCommand.getOldUser().getUsername()).get().getHashedPassword())
                        .build();
            } else{
                // If a new password is entered we use the plaintextPassword function in the builder
                updatedUser = User.builder()
                        .id(updateProfileCommand.getOldUser().getId())
                        .username(updateProfileCommand.getUsername())
                        .email(updateProfileCommand.getEmail())
                        .firstName(updateProfileCommand.getFirstName())
                        .lastName(updateProfileCommand.getLastName())
                        .plaintextPassword(updateProfileCommand.getPlaintextPassword())
                        .build();
            }

            userRepository.update(updatedUser);

            return AuthenticatedUserDTO.builder()
                    .id(updatedUser.getId())
                    .username(updatedUser.getUsername())
                    .email(updatedUser.getEmail())
                    .firstName(updatedUser.getFirstName())
                    .lastName(updatedUser.getLastName())
                    .build();
        } catch (Exception e) {
            throw new UpdateProfileFailedException(e.getMessage());
        }
    }

    private boolean isPasswordStrong(String plaintextPassword) {
        boolean hasUpperCase = plaintextPassword.matches("(?=.*[A-Z]).*");
        boolean hasLowerCase = plaintextPassword.matches("(?=.*[a-z]).*");
        boolean hasNumber = plaintextPassword.matches("(?=.*[0-9]).*");
        boolean hasSpecialChar = plaintextPassword.matches("(?=.*[\\W]).*");

        return hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar && plaintextPassword.length() >= 8;
    }
}
