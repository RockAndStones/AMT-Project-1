package ch.heigvd.amt.StoneOverflow.model;

import lombok.Builder;
import lombok.Getter;

import javax.servlet.http.HttpSession;

@Builder
@Getter
public class LoginCommand {
    private String username;
    private HttpSession session;
}
