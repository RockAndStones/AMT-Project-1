package ch.heigvd.amt.stoneoverflow.application.identitymgmt.login;

import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class AuthenticatedUserDTO {
    private UserId id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
