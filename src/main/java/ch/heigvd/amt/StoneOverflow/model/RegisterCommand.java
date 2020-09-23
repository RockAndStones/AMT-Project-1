package ch.heigvd.amt.StoneOverflow.model;

import lombok.Builder;
import lombok.Getter;

import javax.servlet.http.HttpSession;

@Builder
@Getter
public class RegisterCommand {
    private String username;
    private HttpSession session;
}
