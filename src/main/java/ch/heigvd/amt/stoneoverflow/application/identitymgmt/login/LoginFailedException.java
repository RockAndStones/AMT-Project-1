package ch.heigvd.amt.StoneOverflow.application.identitymgmt.login;

import ch.heigvd.amt.StoneOverflow.application.BusinessException;

public class LoginFailedException extends BusinessException {
    public LoginFailedException(String message) {
        super(message);
    }
}
