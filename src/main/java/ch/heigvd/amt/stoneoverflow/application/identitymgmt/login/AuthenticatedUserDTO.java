package ch.heigvd.amt.stoneoverflow.application.identitymgmt.login;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class AuthenticatedUserDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
