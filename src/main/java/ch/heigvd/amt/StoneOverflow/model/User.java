package ch.heigvd.amt.StoneOverflow.model;

import lombok.Builder;
import lombok.Value;

import javax.servlet.http.HttpSession;

@Builder
@Value
public class User {
    String username;
    String password;
    HttpSession session;
}
