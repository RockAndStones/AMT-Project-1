package ch.heigvd.amt.stoneoverflow.application.identitymgmt.login;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginCommand {
    private String username;
    private String plaintextPassword;
}
