package ch.heigvd.amt.stoneoverflow.application.identitymgmt.updateprofile;

import ch.heigvd.amt.stoneoverflow.application.identitymgmt.login.AuthenticatedUserDTO;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateProfileCommand {
    private AuthenticatedUserDTO oldUser;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String plaintextPassword;
    private String plaintextPasswordConfirmation;
}
