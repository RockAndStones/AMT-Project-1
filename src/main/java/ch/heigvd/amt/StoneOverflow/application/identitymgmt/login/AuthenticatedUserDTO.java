package ch.heigvd.amt.StoneOverflow.application.identitymgmt.login;

import ch.heigvd.amt.StoneOverflow.domain.user.UserId;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Builder
@Value
public class AuthenticatedUserDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
