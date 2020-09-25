package ch.heigvd.amt.StoneOverflow.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RegisterCommand {
    private final String username;
    private final String password;
}
