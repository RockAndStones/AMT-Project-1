package ch.heigvd.amt.StoneOverflow.domain;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class User {
    String username;
    String password;
}
