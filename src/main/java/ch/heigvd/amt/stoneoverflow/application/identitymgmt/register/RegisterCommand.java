package ch.heigvd.amt.stoneoverflow.application.identitymgmt.register;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class RegisterCommand {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String plaintextPassword;
    private String plaintextPasswordConfirmation;

    public RegisterCommand withoutPasswords() {
        return this.toBuilder()
                .plaintextPassword(null)
                .plaintextPasswordConfirmation(null)
                .build();
    }
}
