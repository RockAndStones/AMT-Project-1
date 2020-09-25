package ch.heigvd.amt.StoneOverflow.application.identitymgmt.authenticate;

import ch.heigvd.amt.StoneOverflow.application.BusinessException;

public class AuthenticationFailedException extends BusinessException {
    public AuthenticationFailedException(String message) {
        super(message);
    }
}
