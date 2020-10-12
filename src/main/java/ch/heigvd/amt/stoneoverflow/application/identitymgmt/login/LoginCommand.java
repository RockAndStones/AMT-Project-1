package ch.heigvd.amt.StoneOverflow.application.identitymgmt.login;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginCommand {
    private String username;
    private String plaintextPassword;
}
