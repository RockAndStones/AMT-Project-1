package ch.heigvd.amt.StoneOverflow.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginCommand {
    private final String username;
    private final String password;
}
