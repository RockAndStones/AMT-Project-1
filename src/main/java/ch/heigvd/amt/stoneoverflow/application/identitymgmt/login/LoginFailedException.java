package ch.heigvd.amt.stoneoverflow.application.identitymgmt.login;

import ch.heigvd.amt.stoneoverflow.application.BusinessException;

public class LoginFailedException extends BusinessException {
    public LoginFailedException(String message) {
        super(message);
    }
}
