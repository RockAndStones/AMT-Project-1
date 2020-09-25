package ch.heigvd.amt.StoneOverflow.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginCommand {
    private final String username;
    private final String password;
}
